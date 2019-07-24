package su.tiburon.atlassian.jira.rest;

import com.atlassian.jira.rest.api.util.ErrorCollection;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import su.tiburon.atlassian.jira.JiraApplicationHelper;
import su.tiburon.atlassian.jira.JiraWebAuthenticationHelper;
import org.springframework.stereotype.Component;
import com.atlassian.crowd.embedded.api.Directory;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 * Settings resource to get the licenses.
 */
@Path("/directory")
@AnonymousAllowed
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DirectoriesResource {

    private final JiraApplicationHelper applicationHelper;

    private final JiraWebAuthenticationHelper webAuthenticationHelper;

    /**
     * Constructor.
     *
     * @param applicationHelper       the injected {@link JiraApplicationHelper}
     * @param webAuthenticationHelper the injected {@link JiraWebAuthenticationHelper}
     */
    @Inject
    public DirectoriesResource(
            final JiraApplicationHelper applicationHelper,
            final JiraWebAuthenticationHelper webAuthenticationHelper) {

        this.applicationHelper = applicationHelper;
        this.webAuthenticationHelper = webAuthenticationHelper;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response syncDir(@QueryParam("id") Long dirId) throws WebApplicationException {

        webAuthenticationHelper.mustBeSysAdmin();

        final ErrorCollection errorCollection = ErrorCollection.of();

        if (dirId != null) {
            try {
                applicationHelper.syncDir(dirId);
            } catch (Exception e) {
                errorCollection.addErrorMessage(e.getMessage());
            }
        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (errorCollection.hasAnyErrors()) {
            return Response.ok(errorCollection).build();
        } else {
            return Response.ok(null).build();
        }
    }

    @GET
    public Response getDir() {
        webAuthenticationHelper.mustBeSysAdmin();
        
        ObjectMapper mapper = new ObjectMapper();
        final List<Directory> directories = applicationHelper.getDir();
        String jsn = "";
        try {
            jsn = mapper.writeValueAsString(directories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(jsn).build();
    }
}