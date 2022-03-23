package cz.kb.openbanking.adaa.example.web.resource;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getAdaaUri;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getApiKey;
import static cz.kb.openbanking.adaa.example.web.common.ClientCertificateProvider.getClientWithCertificate;
import static cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider.authorizationRedirect;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.jersey.AccountApiJerseyImpl;
import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.mapper.AccountMapper;
import cz.kb.openbanking.adaa.example.web.model.AccountModel;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.mvc.Template;
import org.mapstruct.factory.Mappers;

/**
 * Resource that returns accounts provided by the KB ADAA API.
 *
 * @since 1.2
 */
@Path(EndpointUris.ACCOUNTS)
public class AccountResource {
    private static final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    private final AccountApi accountApi = new AccountApiJerseyImpl(getAdaaUri(), getApiKey(), getClientWithCertificate(null));

    @Context
    private UriInfo uriInfo;

    /**
     * Endpoint to get available accounts.
     *
     * @return list of available accounts
     */
    @GET
    @Template(name = "/accounts.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Response getAccounts() {
        // check access token
        String accessToken = OAuth2FlowProvider.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return authorizationRedirect(uriInfo.getBaseUri());
        }

        // get available accounts
        List<AccountModel> accounts = accountApi.accounts(accessToken)
                                                .find()
                                                .stream()
                                                .map(mapper::toAccountModel)
                                                .collect(Collectors.toList());

        return Response.ok(Collections.singletonMap("accounts", accounts)).build();
    }
}
