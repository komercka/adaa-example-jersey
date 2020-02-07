package cz.kb.openbanking.adaa.example.web.oauth2;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getAccessTokenUri;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getAuthorizationUri;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;

/**
 * Store of OAuth2 credentials and authorization flow. This is very simple one user store for credentials.
 * In real usage tokens should be stored per user.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ClientIdentifier
 * @see OAuth2CodeGrantFlow
 * @since 1.0
 */
public class OAuth2FlowProvider {

    /**
     * ADAA authorization scope.
     */
    private static final String ADAA_SCOPE = "adaa";

    /**
     * Represents authorization flow.
     */
    private static OAuth2CodeGrantFlow flow;

    /**
     * Contains client's identity.
     */
    private static ClientIdentifier clientIdentifier;

    /**
     * OAuth2 access token.
     */
    private static String accessToken;

    /**
     * Prepare redirect response to KB OAuth2 authorization consent request.
     *
     * @param redirectUri URI to which authorization token should be redirected
     * @return redirect response to KB OAuth2 authorization consent request
     */
    public static Response authorizationRedirect(String redirectUri) {
        if (StringUtils.isBlank(redirectUri)) {
            throw new IllegalArgumentException("redirectUri must not be empty");
        }

        OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(
                getClientIdentifier(),
                getAuthorizationUri(),
                getAccessTokenUri())
                .redirectUri(redirectUri)
                .scope(ADAA_SCOPE)
                .build();

        setFlow(flow);

        // start the flow
        String kbAuthURI = flow.start();

        // redirect user to KB OAuth2 authorization server
        return Response.seeOther(UriBuilder.fromUri(kbAuthURI).build()).build();
    }

    /**
     * Gets access token.
     *
     * @return access token
     */
    @Nullable
    public static String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets access token.
     *
     * @param accessToken access token
     */
    public static void setAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty");
        }

        OAuth2FlowProvider.accessToken = accessToken;
    }

    /**
     * Gets {@link OAuth2CodeGrantFlow}.
     *
     * @return {@link OAuth2CodeGrantFlow}
     */
    @Nullable
    public static OAuth2CodeGrantFlow getFlow() {
        return flow;
    }

    /**
     * Sets {@link OAuth2CodeGrantFlow}.
     *
     * @param flow {@link OAuth2CodeGrantFlow}
     */
    public static void setFlow(OAuth2CodeGrantFlow flow) {
        if (flow == null) {
            throw new IllegalArgumentException("authorization flow must not be null");
        }

        OAuth2FlowProvider.flow = flow;
    }

    /**
     * Gets {@link ClientIdentifier}.
     *
     * @return {@link ClientIdentifier}
     */
    @Nullable
    public static ClientIdentifier getClientIdentifier() {
        return clientIdentifier;
    }

    /**
     * Sets {@link ClientIdentifier}.
     *
     * @param clientIdentifier {@link ClientIdentifier}
     */
    public static void setClientIdentifier(ClientIdentifier clientIdentifier) {
        if (clientIdentifier == null) {
            throw new IllegalArgumentException("clientIdentifier must not be null");
        }

        OAuth2FlowProvider.clientIdentifier = clientIdentifier;
    }
}
