package dk.kb.digisam.webservice;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PictureHashApplication extends Application {

    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(JacksonJsonProvider.class, PictureHashService.class));
    }


}
