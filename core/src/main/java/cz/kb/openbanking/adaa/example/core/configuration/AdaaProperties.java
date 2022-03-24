package cz.kb.openbanking.adaa.example.core.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Singleton implementation of the application properties loader.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class AdaaProperties {

    /**
     * Path to the application properties.
     */
    private static final String PROPERTIES_PATH = "/application.properties";

    private static Properties properties = null;

    /**
     * Gets all application properties.
     *
     * @return application properties
     */
    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(AdaaProperties.class.getResourceAsStream(PROPERTIES_PATH));
            } catch (IOException e) {
                throw new IllegalStateException("Application properties could not be loaded.", e);
            }
        }

        return properties;
    }

    /**
     * Gets authorization API key generated at the KB API Developer portal.
     *
     * @return authorization API key generated at the KB API Developer portal
     */
    public static String getApiKey() {
        return getProperty("x-api-key");
    }

    /**
     * Gets Client Registration API URI.
     *
     * @return Client Registration API URI
     */
    public static String getClientRegistrationUri() {
        return getProperty("client-registration-uri");
    }

    /**
     * Gets URI of the endpoint of the KB Client Registration API for getting software statement.
     *
     * @return URI of the endpoint of the KB Client Registration API for getting software statement
     */
    public static String getSoftwareStatementUri() {
        return getProperty("software-statement-uri");
    }

    /**
     * Gets URI of the KB ADAA API.
     *
     * @return URI of the KB ADAA API
     */
    public static String getAdaaUri() {
        return getProperty("adaa-uri");
    }

    /**
     * Gets URI of the page of the KB Authorization server for getting OAuth2 authorization code.
     *
     * @return URI of the page of the KB Authorization server for getting OAuth2 authorization code
     */
    public static String getAuthorizationUri() {
        return getProperty("authorization-uri");
    }

    /**
     * Gets URI of the endpoint of the KB OAuth2 API for getting access token.
     *
     * @return URI of the endpoint of the KB OAuth2 API for getting access token
     */
    public static String getAccessTokenUri() {
        return getProperty("access-token-uri");
    }

    /**
     * Gets Base64 encoded 256-bit key that used during client registration process.
     *
     * @return encryption key
     */
    public static String getSecretKey() {
        return getProperty("secret");
    }

    /**
     * Gets path to the keystore with client certificate.
     *
     * @return path to the keystore with client certificate
     */
    public static String getKeystorePath() {
        return getProperty("keystore-location");
    }

    /**
     * Gets password of the keystore with client certificate.
     *
     * @return password of the keystore with client certificate
     */
    public static String getKeystorePassword() {
        return getProperty("keystore-password");
    }

    /**
     * Gets password of the client certificate.
     *
     * @return password of the client certificate
     */
    public static String getClientCertPassword() {
        return getProperty("client-cert-password");
    }

    /**
     * Gets application property.
     *
     * @param propertyName name of the desired property
     * @return application property
     */
    private static String getProperty(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("propertyName must not be empty");
        }

        String property = getProperties().getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Property '" + propertyName + "' does not exist.");
        }

        return property;
    }
}
