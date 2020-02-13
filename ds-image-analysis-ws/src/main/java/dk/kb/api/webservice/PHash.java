package dk.kb.api.webservice;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class PHash extends ImageHash {

    private static final Logger log = LoggerFactory.getLogger(PHash.class);

    public PHash(String imgURL, int start, int end){
        super(imgURL, start, end);
        try {
            setHashPath(Files.createTempFile("image", ".hash"));
            getHashPath().toFile().deleteOnExit();
        } catch (IOException e) {
            log.error("An error occured while creating a temporary file.", e);
        }
    }

    protected List<String> generateJSON() {
        List<String> reply = new LinkedList<String>();
        try {
            if ((getImgURL() != null) && (!getImgURL().toString().isEmpty())) {
                FileUtils.copyURLToFile(getImgURL(), getHashPath().toFile());
                File jsonFile = Files.createTempFile("PHash", ".JSON").toFile();
                jsonFile.deleteOnExit();
                JsonGenerator jsonGenerator = new JsonFactory()
                        .createGenerator(new FileOutputStream(jsonFile));
                //for pretty printing
                jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
                jsonGenerator.writeStartObject(); // start root object

                jsonGenerator.writeStringField("Algorithm", "Perceive Hash");
                jsonGenerator.writeStringField("Image URL", getImgURL().toString());
                if ((getStart() < 1) || (getEnd() < 1) || (getStart() > getEnd())) {
                    reply.add("Both start and end have to be positive and start value has to be less or equal to end value");
                    return reply;
                }
                for (int i = getStart(); i <= getEnd(); i++) {
                    setNoBit(i * i);
                    jsonGenerator.writeNumberField("Hash number", i);
                    jsonGenerator.writeNumberField("Hash number of bit", getNoBit());
                    jsonGenerator.writeStringField("Hash value", getPHash().getHashValue().toString());
                }
                jsonGenerator.writeEndObject();
                jsonGenerator.flush();
                jsonGenerator.close();

                reply = Files.readAllLines(jsonFile.toPath());
            }
            else {
                reply.add("The URL has to defined");
            }
        } catch (IOException e) {
            reply.add("A file error appeared.");
            log.error("A file error appeared", e);
        }

        return reply;
    }

    private Hash getPHash() throws IOException {
        HashingAlgorithm hashAlg = new PerceptiveHash(getNoBit());
        return hashAlg.hash(getHashPath().toFile());
    }

    private String toBinary(int noBit, BigInteger hashValue) {
        String hash = hashValue.toString(2);
        int numZeros = noBit - hash.length();
        String pad = StringUtils.repeat("0", numZeros);
        return pad + hash;
    }

}
