package cz.kb.openbanking.adaa.example.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents JSON object with client's identifiers.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientIdDto {

    /**
     * Client id from client registration.
     */
    private final String clientId;

    /**
     * Client secret from client registration.
     */
    private final String clientSecret;

    /**
     * New instance.
     *
     * @param clientId     client id
     * @param clientSecret client secret
     */
    @JsonCreator
    public ClientIdDto(@JsonProperty("client_id") String clientId, @JsonProperty("client_secret") String clientSecret) {
        if (StringUtils.isBlank(clientId)) {
            throw new IllegalArgumentException("clientId must not be empty");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new IllegalArgumentException("clientSecret must not be empty");
        }

        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Gets client ID.
     *
     * @return client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets client secret.
     *
     * @return client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ClientIdDto)) {
            return false;
        }

        ClientIdDto that = (ClientIdDto) o;

        return new EqualsBuilder()
                .append(getClientId(), that.getClientId())
                .append(getClientSecret(), that.getClientSecret())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getClientId())
                .append(getClientSecret())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientId", clientId)
                .append("clientSecret", clientSecret)
                .toString();
    }
}
