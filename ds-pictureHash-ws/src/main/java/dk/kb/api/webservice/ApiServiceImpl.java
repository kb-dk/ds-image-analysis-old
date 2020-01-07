package dk.kb.api.webservice;

import dk.kb.api.DefaultApi;
import dk.kb.model.HelloReplyDto;
import dk.kb.model.IdMetaPairsDto;

import java.io.File;
import java.util.List;

public class ApiServiceImpl  implements DefaultApi {
    @Override
    public HelloReplyDto getGreeting(String alternateHello) {
        HelloReplyDto helloReplyDto = new HelloReplyDto();
        helloReplyDto.setMessage("Hello back");
        return helloReplyDto;
    }

    @Override
    public String getImagePHash(String imgPath) {
        return null;
    }

    @Override
    public  List<IdMetaPairsDto> getMetaData(List<String> ids, String format) {
        return null;
    }

    @Override
    public File getResource(String id) {
        return null;
    }
}
