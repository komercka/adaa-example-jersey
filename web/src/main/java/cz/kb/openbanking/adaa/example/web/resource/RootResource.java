package cz.kb.openbanking.adaa.example.web.resource;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;

/**
 * Root resource.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@Path("/")
public class RootResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Resolves application's entry point.
     *
     * @return redirect response depends on previous actions
     */
    @GET
    public Response entryPoint() {
        URI uri;
        if (OAuth2FlowProvider.getClientIdentifier() == null
                || OAuth2FlowProvider.getClientIdentifier().getClientId() == null
                || OAuth2FlowProvider.getClientIdentifier().getClientSecret() == null) {
            uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path(EndpointUris.CLIENT_REGISTRATION_FORM_URI).build();
        } else {
            uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path(EndpointUris.TRANSACTIONS_URI).build();
        }

        return Response.seeOther(uri).build();
    }
}
