package cz.kb.openbanking.adaa.example.web.resource;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.oauth2.TokenResult;

/**
 * This resource serves to receive authorization code (after user grants access to this application)
 * and then call KB OAuth2 API to get access token.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see OAuth2FlowProvider
 * @since 1.0
 */
@Path(EndpointUris.AUTHORIZATION)
public class AuthorizationResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Receives the authorization code, call KB OAuth2 API to get access token
     * and set it to the {@link OAuth2FlowProvider}.
     *
     * @param authCode OAuth2 authorization code
     * @param state described by OAuth2 specification and serves to prevent possible CSRF attack
     *
     * @return redirect user to transaction's resource
     */
    @GET
    @Path(EndpointUris.OAUTH2_PATH)
    public Response authorize(@QueryParam("code") String authCode, @QueryParam("state") String state) {
        if (StringUtils.isBlank(authCode)) {
            throw new IllegalArgumentException("authCode must not be empty");
        }
        if (StringUtils.isBlank(state)) {
            throw new IllegalArgumentException("state must not be empty");
        }

        // call KB OAuth2 API to get access token
        TokenResult tokenResult = OAuth2FlowProvider.getFlow().finish(authCode, state);
        OAuth2FlowProvider.setAccessToken(tokenResult.getAccessToken());

        // authorization is finished -> now redirecting back to the 'transactions' resource
        URI uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path(EndpointUris.TRANSACTIONS_URI).build();
        return Response.seeOther(uri).build();
    }
}
