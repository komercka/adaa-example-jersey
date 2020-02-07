package cz.kb.openbanking.adaa.example.web.resource;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getApiKey;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getClientRegistrationUri;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getSecretKey;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getSoftwareStatementUri;
import static cz.kb.openbanking.adaa.example.web.common.ClientCertificateProvider.getClientWithCertificate;
import static cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider.authorizationRedirect;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.example.core.decryption.Aes256DecryptionService;
import cz.kb.openbanking.adaa.example.core.decryption.impl.Aes256DecryptionServiceImpl;
import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.dto.ClientIdDto;
import cz.kb.openbanking.adaa.example.web.dto.ClientRegistrationRequest;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;
import cz.kb.openbanking.clientregistration.client.api.SoftwareStatementsApi;
import cz.kb.openbanking.clientregistration.client.api.model.GrantTypesEnum;
import cz.kb.openbanking.clientregistration.client.api.model.Jwt;
import cz.kb.openbanking.clientregistration.client.api.model.ResponseTypesEnum;
import cz.kb.openbanking.clientregistration.client.api.model.SoftwareStatement;
import cz.kb.openbanking.clientregistration.client.api.model.TokenEndpointAuthMethodEnum;
import cz.kb.openbanking.clientregistration.client.jersey.SoftwareStatementsJerseyImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.server.mvc.Template;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * This resource serves for registration this application against KB OAuth2 server
 * and get {@link ClientIdentifier} to be able to call KB ADAA API.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ClientIdentifier
 * @see OAuth2FlowProvider
 * @since 1.0
 */
@Path(EndpointUris.REGISTRATION)
public class ClientRegistrationResource {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Aes256DecryptionService DECRYPTION_SERVICE = new Aes256DecryptionServiceImpl();
    private static final SoftwareStatementsApi softwareStatementsApi =
            new SoftwareStatementsJerseyImpl(getSoftwareStatementUri(), getApiKey(), getClientWithCertificate(null));

    @Context
    private UriInfo uriInfo;

    /**
     * Returns a registration page with a form. After completion of this form
     * user will be redirected to KB Login page, where should grant access to application registration.
     *
     * @return registration page
     */
    @GET
    @Path(EndpointUris.CLIENT_REGISTRATION_FORM_PATH)
    @Template(name = "/register.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Viewable registrationForm() {
        return new Viewable("/register.ftl");
    }

    /**
     * Gets software statement by calling Client Registration API, after that
     * user will be redirected to KB Login page, where should grant access to application registration.
     *
     * @param softwareName name of software
     * @return redirect to KB Login page
     */
    @GET
    @Path(EndpointUris.SOFTWARE_STATEMENT_REGISTRATION_PATH)
    public Response software(@QueryParam("softwareName") String softwareName) {
        if (StringUtils.isBlank(softwareName)) {
            throw new IllegalArgumentException("softwareName must not be empty");
        }

        SoftwareStatement request = getSoftwareStatement(softwareName);

        Jwt jwt = softwareStatementsApi.softwareStatement(request);

        ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(softwareName, null,
                "web", Collections.singletonList(getOauthRedirectUri()), Collections.singletonList("adaa"),
                jwt.getToken(), getSecretKey());
        byte[] registrationRequest;
        try {
            registrationRequest = mapper.writeValueAsString(clientRegistrationRequest).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot construct JSON from software statement request.", e);
        }

        URI registrationUri = UriBuilder.fromUri(getClientRegistrationUri())
                                        .path(EndpointUris.SAML_REGISTRATION)
                                        .queryParam("registrationRequest", Base64.getEncoder().encodeToString(registrationRequest))
                                        .build();
        return Response.seeOther(registrationUri).build();
    }

    /**
     * Retrieves AES-256 encrypted client's identifiers, decrypts it and set to the {@link OAuth2FlowProvider}.
     *
     * @param salt          salt that was used during encryption
     * @param encryptedData AES-256 encrypted client's identifiers
     * @return redirect user to continue OAuth2 authorization process
     */
    @GET
    @Path(EndpointUris.CLIENT_REGISTRATION_PATH)
    public Response retrieveClientIdentifier(@QueryParam("salt") String salt,
                                             @QueryParam("encryptedData") String encryptedData)
    {
        if (StringUtils.isBlank(salt)) {
            throw new IllegalArgumentException("salt must not be empty");
        }
        if (StringUtils.isBlank(encryptedData)) {
            throw new IllegalArgumentException("encryptedData must not be empty");
        }

        String clientIdentifierJson = DECRYPTION_SERVICE.decrypt(encryptedData, salt, getSecretKey());
        ObjectMapper mapper = new ObjectMapper();
        ClientIdDto clientIdDto;
        try {
            clientIdDto = mapper.readValue(clientIdentifierJson, ClientIdDto.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse client registration's data.", e);
        }
        ClientIdentifier clientIdentifier =
                new ClientIdentifier(clientIdDto.getClientId(), clientIdDto.getClientSecret());
        OAuth2FlowProvider.setClientIdentifier(clientIdentifier);

        // registration is finished -> now redirect client to get the authorization code
        return authorizationRedirect(getOauthRedirectUri());
    }

    /**
     * Gets {@link SoftwareStatement}.
     *
     * @param softwareName name of application
     * @return {@link SoftwareStatement}
     */
    private SoftwareStatement getSoftwareStatement(String softwareName) {
        if (StringUtils.isBlank(softwareName)) {
            throw new IllegalArgumentException("softwareName must not be empty");
        }

        SoftwareStatement request = new SoftwareStatement(softwareName, "softwareId", "1.0",
                Collections.singletonList(getOauthRedirectUri()),
                uriInfo.getBaseUri().toString() + EndpointUris.CLIENT_REGISTRATION_URI);
        request.setSoftwareUri(uriInfo.getBaseUri().toString());
        request.setTokenEndpointAuthMethod(TokenEndpointAuthMethodEnum.CLIENT_SECRET_POST);
        request.setGrantTypes(Collections.singletonList(GrantTypesEnum.AUTHORIZATION_CODE));
        request.setResponseTypes(Collections.singletonList(ResponseTypesEnum.CODE));

        return request;
    }

    /**
     * Gets redirect URI into OAuth2 (for getting authorization code).
     * This URI must be fill in software statement and client registration.
     *
     * @return redirect URI
     */
    private String getOauthRedirectUri() {
        return UriBuilder.fromUri(uriInfo.getBaseUri()).path(EndpointUris.AUTHORIZATION_OAUTH2_URI)
                         .build().toString();
    }
}
