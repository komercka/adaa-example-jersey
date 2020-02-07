package cz.kb.openbanking.adaa.example.web.common;

import org.glassfish.jersey.SslConfigurator;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getClientCertPassword;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getKeystorePassword;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getKeystorePath;

/**
 * Ensures SSL communication by adding client certificate.
 * JKS keystore with client certificate must be provided.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class ClientCertificateProvider {

    /**
     * Gets {@link Client} with set client certificate.
     * If input {@link Client} is {@code null} than a new one will be created,
     * otherwise - certificate will be added to input one.
     *
     * @param client JAX-RS {@link Client}
     * @return @link Client} with set client certificate
     */
    public static Client getClientWithCertificate(@Nullable Client client) {
        KeyStore clientKeyStore = null;
        try {
            clientKeyStore = KeyStore.getInstance("JKS");
            clientKeyStore.load(new FileInputStream(getKeystorePath()), getKeystorePassword().toCharArray());
        } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
            throw new IllegalStateException("Error was occurred during getting a keystore with a client certificate. " +
                    "Error: " + e.getMessage(), e);
        }

        SslConfigurator sslConfig = SslConfigurator.newInstance()
                .keyStore(clientKeyStore)
                .keyStorePassword(getKeystorePassword())
                .keyPassword(getClientCertPassword());
        SSLContext sslContext = sslConfig.createSSLContext();

        if (client == null) {
            return ClientBuilder.newBuilder()
                    .hostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier())
                    .sslContext(sslContext)
                    .build();
        }

        client.getSslContext().setDefault(sslContext);
        return client;
    }
}
