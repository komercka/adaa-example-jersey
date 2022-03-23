package cz.kb.openbanking.adaa.example.web.resource;

import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getAdaaUri;
import static cz.kb.openbanking.adaa.example.core.configuration.AdaaProperties.getApiKey;
import static cz.kb.openbanking.adaa.example.web.common.ClientCertificateProvider.getClientWithCertificate;
import static cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider.authorizationRedirect;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.jersey.AccountApiJerseyImpl;
import cz.kb.openbanking.adaa.example.web.common.EndpointUris;
import cz.kb.openbanking.adaa.example.web.mapper.AccountMapper;
import cz.kb.openbanking.adaa.example.web.model.StatementModel;
import cz.kb.openbanking.adaa.example.web.oauth2.OAuth2FlowProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.mvc.Template;
import org.mapstruct.factory.Mappers;

/**
 * Resource that returns account statements provided by the KB ADAA API.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.1
 */
@Path(EndpointUris.STATEMENTS_URI)
public class AccountStatementsResource {
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    private static final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    private final AccountApi accountApi = new AccountApiJerseyImpl(getAdaaUri(), getApiKey(), getClientWithCertificate(null));

    @Context
    private UriInfo uriInfo;

    /**
     * Endpoint that serves for downloading PDF account statement.
     *
     * @return account statement as PDF file
     */
    @GET
    @Path("/list")
    @Template(name = "/statements.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Response getAccountStatements(@QueryParam("accountId") String accountId) {
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be empty");
        }

        // check access token
        String accessToken = OAuth2FlowProvider.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return authorizationRedirect(uriInfo.getBaseUri());
        }

        // get statements for last 30 days
        OffsetDateTime fromDateTime = OffsetDateTime.now(ZoneId.systemDefault()).minusDays(31).truncatedTo(ChronoUnit.DAYS);
        List<StatementModel> statements = accountApi.statements(accountId, OAuth2FlowProvider.getAccessToken(), fromDateTime)
                                                    .find().stream()
                                                    .map(mapper::toStatementModel)
                                                    .collect(Collectors.toList());

        Map<String, Object> model = new HashMap<>();
        model.put("statements", statements);
        model.put("accountId", accountId);

        return Response.ok(model).build();
    }

    /**
     * Endpoint that serves for downloading PDF account statement.
     *
     * @param statementId ID of PDF statement
     * @return account statement as PDF file
     */
    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response getPdfStatement(@QueryParam("id") Long statementId, @QueryParam("accountId") String accountId) {
        if (statementId == null) {
            throw new IllegalArgumentException("statementId must not be null");
        }
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be empty");
        }

        // check access token
        String accessToken = OAuth2FlowProvider.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return authorizationRedirect(uriInfo.getBaseUri());
        }

        byte[] pdfStatement = accountApi.statementPdf(accountId, OAuth2FlowProvider.getAccessToken(), statementId).find();
        File statement = new File(statementId.toString());
        try {
            FileUtils.writeByteArrayToFile(statement, pdfStatement);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Response.ResponseBuilder response = Response.ok(statement);
        response.header(CONTENT_DISPOSITION_HEADER, "attachment; filename=" + statementId + ".pdf");
        return response.build();


    }
}
