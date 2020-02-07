package cz.kb.openbanking.adaa.example.web.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents request to register client application.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class ClientRegistrationRequest {
    /**
     * Default value for {@link #encryptionAlg}.
     */
    private static final String ENCRYPTION_ALGORITHM_DEFAULT_VALUE = "AES-256";

    /**
     * Name of the client application.
     */
    private final String clientName;

    /**
     * Name of the client application in English.
     */
    private final String clientNameEn;

    /**
     * Client application type.
     */
    private final String applicationType;

    /**
     * List of URIs to which authorization server will send redirect after finish client registration process.
     */
    private final List<String> redirectUris;

    /**
     * List of authorization scopes (see details: https://tools.ietf.org/html/rfc6749#section-3.3).
     */
    private final List<String> scope;

    /**
     * Base64 encoded software statement.
     */
    private final String softwareStatement;

    /**
     * Encryption algorithm.
     */
    private final String encryptionAlg;

    /**
     * Encryption key.
     */
    private final String encryptionKey;

    /**
     * New instance.
     * Default value {@value #ENCRYPTION_ALGORITHM_DEFAULT_VALUE} for attribute {@link #encryptionAlg} will be used.
     *
     * @param clientName        client application name
     * @param clientNameEn      client application name in English
     * @param applicationType   type of the client application
     * @param redirectUris      redirect URIs to which client registration data will be returned
     * @param scope             list of needed OAuth2 scopes
     * @param softwareStatement Base64 encoded software statement
     * @param encryptionKey     Base64 encoded 256-bit encryption key
     */
    public ClientRegistrationRequest(String clientName, @Nullable String clientNameEn, String applicationType,
                                     Collection<String> redirectUris, Collection<String> scope,
                                     String softwareStatement, String encryptionKey)
    {
        this(clientName, clientNameEn, applicationType, redirectUris, scope, softwareStatement,
                encryptionKey, ENCRYPTION_ALGORITHM_DEFAULT_VALUE);
    }

    /**
     * New instance.
     *
     * @param clientName        client application name
     * @param clientNameEn      client application name in English
     * @param applicationType   type of the client application
     * @param redirectUris      redirect URIs to which client registration data will be returned
     * @param scope             list of needed OAuth2 scopes
     * @param softwareStatement Base64 encoded software statement
     * @param encryptionKey     Base64 encoded 256-bit encryption key
     * @param encryptionAlg     encryption algorithm
     */
    public ClientRegistrationRequest(String clientName, @Nullable String clientNameEn, String applicationType,
                                     Collection<String> redirectUris, Collection<String> scope,
                                     String softwareStatement, String encryptionKey, String encryptionAlg)
    {
        if (StringUtils.isBlank(clientName)) {
            throw new IllegalArgumentException("clientName must not be empty");
        }
        if (StringUtils.isBlank(applicationType)) {
            throw new IllegalArgumentException("applicationType must not be empty");
        }
        if (CollectionUtils.isEmpty(redirectUris)) {
            throw new IllegalArgumentException("redirectUris must not be empty");
        }
        if (CollectionUtils.isEmpty(scope)) {
            throw new IllegalArgumentException("scope must not be empty");
        }
        if (StringUtils.isBlank(softwareStatement)) {
            throw new IllegalArgumentException("softwareStatement must not be empty");
        }
        if (StringUtils.isBlank(encryptionKey)) {
            throw new IllegalArgumentException("encryptionKey must not be empty");
        }

        this.clientName = clientName;
        this.clientNameEn = clientNameEn;
        this.applicationType = applicationType;
        this.redirectUris = new ArrayList<>(redirectUris);
        this.scope = new ArrayList<>(scope);
        this.softwareStatement = softwareStatement;
        this.encryptionKey = encryptionKey;
        this.encryptionAlg = encryptionAlg;
    }

    /**
     * Gets name of the client application.
     *
     * @return name of the client application
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Gets name of the client application in English.
     *
     * @return name of the client application in English
     */
    @Nullable
    public String getClientNameEn() {
        return clientNameEn;
    }

    /**
     * Gets client application type.
     *
     * @return client application type
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * Gets list of URIs to which authorization server will send redirect after finish client registration process.
     *
     * @return list of URIs to which authorization server will send redirect after finish client registration process
     */
    public List<String> getRedirectUris() {
        return Collections.unmodifiableList(redirectUris);
    }

    /**
     * Gets list of authorization scopes (see details: https://tools.ietf.org/html/rfc6749#section-3.3).
     *
     * @return list of authorization scopes
     */
    public List<String> getScope() {
        return Collections.unmodifiableList(scope);
    }

    /**
     * Gets Base64 encoded software statement.
     *
     * @return Base64 encoded software statement
     */
    public String getSoftwareStatement() {
        return softwareStatement;
    }

    /**
     * Gets encryption algorithm.
     *
     * @return encryption algorithm
     */
    public String getEncryptionAlg() {
        return encryptionAlg;
    }

    /**
     * Gets encryption key.
     *
     * @return encryption key
     */
    public String getEncryptionKey() {
        return encryptionKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ClientRegistrationRequest)) {
            return false;
        }

        ClientRegistrationRequest that = (ClientRegistrationRequest) o;

        return new EqualsBuilder()
                .append(getClientName(), that.getClientName())
                .append(getClientNameEn(), that.getClientNameEn())
                .append(getApplicationType(), that.getApplicationType())
                .append(getRedirectUris(), that.getRedirectUris())
                .append(getScope(), that.getScope())
                .append(getSoftwareStatement(), that.getSoftwareStatement())
                .append(getEncryptionAlg(), that.getEncryptionAlg())
                .append(getEncryptionKey(), that.getEncryptionKey())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getClientName())
                .append(getClientNameEn())
                .append(getApplicationType())
                .append(getRedirectUris())
                .append(getScope())
                .append(getSoftwareStatement())
                .append(getEncryptionAlg())
                .append(getEncryptionKey())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientName", clientName)
                .append("clientNameEn", clientNameEn)
                .append("applicationType", applicationType)
                .append("redirectUris", redirectUris)
                .append("scope", scope)
                .append("softwareStatement", softwareStatement)
                .append("encryptionAlg", encryptionAlg)
                .append("encryptionKey", "***********")
                .toString();
    }
}
