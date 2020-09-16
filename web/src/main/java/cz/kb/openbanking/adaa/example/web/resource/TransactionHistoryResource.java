package cz.kb.openbanking.adaa.example.web.resource;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getAdaaUri;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getApiKey;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getCurrency;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getIban;
import static cz.kb.openbanking.adaa.example.web.common.ClientCertificateProvider.getClientWithCertificate;
import static cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider.authorizationRedirect;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.api.model.Account;
import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.jersey.AccountApiJerseyImpl;
import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;
import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.mapper.AccountMapper;
import cz.kb.openbanking.adaa.example.web.model.TransactionModel;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.mvc.Template;
import org.iban4j.Iban;
import org.mapstruct.factory.Mappers;

/**
 * Resource that returns a transactions history by the KB ADAA API.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@Path(EndpointUris.TRANSACTIONS_URI)
public class TransactionHistoryResource {
    private static final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    private final AccountApi accountApi = new AccountApiJerseyImpl(getAdaaUri(), getApiKey(),
            getClientWithCertificate(null));

    @Context
    private UriInfo uriInfo;

    /**
     * Endpoint that serves for getting client's transactions history.
     *
     * @return HTML page with client's transactions history
     */
    @GET
    @Template(name = "/transactions.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Response transactions() {
        // check access token
        String accessToken = OAuth2FlowProvider.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return authorizationRedirect(uriInfo.getBaseUri());
        }

        Account account = new Account(Iban.valueOf(getIban()), Currency.getInstance(getCurrency()));

        // calling ADAA Client API SDK for getting transaction history
        PageSlice<AccountTransaction> pageSlice = accountApi.transactions(account, accessToken)
                .page(0).find();
        List<TransactionModel> transactions = pageSlice.getContent().stream()
                .map(mapper::toTransactionModel).collect(Collectors.toList());

        List<AccountBalance> accountBalances = accountApi.balances(account, accessToken).find();

        Map<String, Object> model = new HashMap<>();
        model.put("iban", getIban());
        model.put("transactions", transactions);
        // for the example purposes only the first one account's balance was used
        model.put("balance", mapper.toAccountBalanceModel(accountBalances.get(0)));

        return Response.ok(model).build();
    }
}
