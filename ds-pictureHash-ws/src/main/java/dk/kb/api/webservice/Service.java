package dk.kb.api.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class Service {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public Service() {
        log.info("Initializing service");
    }

    /* Unmodified from the templade webapp. Not related to the Swagger definition! */
    @GET
    @Path("getHello")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHello() {
        List<String> helloLines = new ArrayList<>();
        helloLines.add("Hello");
        helloLines.add("world");
        return Response.ok(helloLines, MediaType.APPLICATION_JSON).build();
    }


    
}




