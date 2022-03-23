package cz.kb.openbanking.adaa.example.web.resource;

import java.util.logging.Logger;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

/**
 * Application's entry point.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@ApplicationPath("/")
public class AdaaApplication extends ResourceConfig {

    /**
     * Configures application context.
     */
    public AdaaApplication() {
        //register resources
        register(RootResource.class);
        register(AuthorizationResource.class);
        register(TransactionHistoryResource.class);
        register(ClientRegistrationResource.class);
        register(AccountStatementsResource.class);
        register(AccountResource.class);

        // register Freemarker MVC support components
        register(FreemarkerMvcFeature.class);
        property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH, "/freemarker");

        register(new LoggingFeature(Logger.getLogger("example.server"), LoggingFeature.Verbosity.PAYLOAD_ANY));
    }
}
