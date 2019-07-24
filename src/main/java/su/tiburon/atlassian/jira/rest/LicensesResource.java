package su.tiburon.atlassian.jira.rest;

import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import su.tiburon.atlassian.jira.JiraApplicationHelper;
import su.tiburon.atlassian.jira.JiraWebAuthenticationHelper;
import su.tiburon.atlassian.jira.bean.LicenseBean;
import su.tiburon.atlassian.jira.bean.LicensesBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Licenses resource to get the licenses.
 */
@Path("/licenses")
@AnonymousAllowed
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LicensesResource {

    private final JiraApplicationHelper applicationHelper;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param applicationHelper       the injected {@link JiraApplicationHelper}
     * @param webAuthenticationHelper the injected {@link JiraWebAuthenticationHelper}
     */
    @Inject
    public LicensesResource(
            final JiraApplicationHelper applicationHelper,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.applicationHelper = applicationHelper;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @GET
    public Response getLicenses() {
        webAuthenticationHelper.mustBeSysAdmin();

        final Collection<LicenseDetails> licenseDetails = applicationHelper.getLicenses();
        return Response.ok(LicensesBean.from(licenseDetails)).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public Response setLicense(
            @QueryParam("clear") @DefaultValue("false") boolean clear,
            final String licenseKey) throws WebApplicationException {

        webAuthenticationHelper.mustBeSysAdmin();

        if (licenseKey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        final LicenseDetails licenseDetail = applicationHelper.setLicense(licenseKey, clear);
        return Response.ok(LicenseBean.from(licenseDetail)).build();
    }

}
