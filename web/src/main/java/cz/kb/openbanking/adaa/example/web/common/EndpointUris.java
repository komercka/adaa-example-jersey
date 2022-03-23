package cz.kb.openbanking.adaa.example.web.common;

import cz.kb.openbanking.adaa.example.web.resource.AccountResource;
import cz.kb.openbanking.adaa.example.web.resource.AccountStatementsResource;
import cz.kb.openbanking.adaa.example.web.resource.AuthorizationResource;
import cz.kb.openbanking.adaa.example.web.resource.ClientRegistrationResource;
import cz.kb.openbanking.adaa.example.web.resource.TransactionHistoryResource;

/**
 * Contains application endpoints' URIs.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public final class EndpointUris {

    /**
     * Base URI for all authorization URIs in {@link AuthorizationResource}.
     */
    public static final String AUTHORIZATION = "authorize";

    /**
     * Last path from OAuth2 authorization URI.
     */
    public static final String OAUTH2_PATH = "/oauth2";

    /**
     * Authorization back URI to Oauth2 for getting OAuth2 authorization code.
     */
    public static final String AUTHORIZATION_OAUTH2_URI = AUTHORIZATION + OAUTH2_PATH;

    /**
     * Base URI for all registration URI in {@link ClientRegistrationResource}.
     */
    public static final String REGISTRATION = "register";

    /**
     * Last path from getting client id and client secret URI.
     */
    public static final String CLIENT_REGISTRATION_PATH = "/client";

    /**
     * URI to get client id and client secret URI.
     */
    public static final String CLIENT_REGISTRATION_URI = REGISTRATION + CLIENT_REGISTRATION_PATH;

    /**
     * Last path from registration form URI.
     */
    public static final String CLIENT_REGISTRATION_FORM_PATH = "/form";

    /**
     * URI to show application registration form.
     */
    public static final String CLIENT_REGISTRATION_FORM_URI = REGISTRATION + CLIENT_REGISTRATION_FORM_PATH;

    /**
     * Last path from software statement URI.
     */
    public static final String SOFTWARE_STATEMENT_REGISTRATION_PATH = "/software-statement";

    /**
     * URI to get transaction history in {@link TransactionHistoryResource}.
     */
    public static final String TRANSACTIONS_URI = "transactions";

    /**
     * URI to get accounts in {@link AccountResource}.
     */
    public static final String ACCOUNTS = "accounts";

    /**
     * URI to call KB login page for register application instance.
     */
    public static final String SAML_REGISTRATION = "saml/register";

    /**
     * URI to get account statements in {@link AccountStatementsResource}.
     */
    public static final String STATEMENTS_URI = "statements";

    /**
     * No instance.
     */
    private EndpointUris() {
    }
}
