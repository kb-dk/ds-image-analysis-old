package dk.kb.digisam.webservice;

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
public class PictureHashService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public PictureHashService() {
        log.info("Initializing service");
    }
    
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




