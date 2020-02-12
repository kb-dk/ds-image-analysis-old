package dk.kb.api.webservice;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
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

public class DHash extends ImageHash {

    private static final Logger log = LoggerFactory.getLogger(DHash.class);
    private DifferenceHash.Precision precision;

    public DifferenceHash.Precision getPrecision() {
        return precision;
    }

    public void setPrecision(DifferenceHash.Precision precision) {
        this.precision = precision;
    }

    public DHash(String imgURL, int start, int end){
        super(imgURL, start, end);
        try {
            setHashPath(Files.createTempFile("image", ".hash"));
            getHashPath().toFile().deleteOnExit();
        } catch (IOException e) {
            log.error("An error occured while creating a temporary file.", e);
        }
    }

    protected List<String> generateJSON() {
        List<String> reply = new LinkedList<>();
        try {
            FileUtils.copyURLToFile(getImgURL(), getHashPath().toFile());
            File jsonFile = Files.createTempFile("DHash", ".JSON").toFile();
            jsonFile.deleteOnExit();
            JsonGenerator jsonGenerator = new JsonFactory()
                    .createGenerator(new FileOutputStream(jsonFile));
            //for pretty printing
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
            jsonGenerator.writeStartObject(); // start root object

            jsonGenerator.writeStringField("Algorithm", "Difference Hash");
            jsonGenerator.writeStringField("Image URL", getImgURL().toString());

            setPrecision(DifferenceHash.Precision.Simple);
            hashValue(jsonGenerator);

            setPrecision(DifferenceHash.Precision.Double);
            hashValue(jsonGenerator);

            setPrecision(DifferenceHash.Precision.Triple);
            hashValue(jsonGenerator);
            jsonGenerator.writeEndObject();

            jsonGenerator.flush();
            jsonGenerator.close();

            reply = Files.readAllLines(jsonFile.toPath());

        } catch (IOException e) {
            reply.add("A file error appeared.");
            log.error("A file error appeared", e);
        }
        return reply;
    }

    private void hashValue(JsonGenerator jsonGenerator) throws IOException {
        for (int i = getStart(); i <= getEnd(); i++) {
            setNoBit(i*(i+1));
            jsonGenerator.writeNumberField("Hash number", i);
            jsonGenerator.writeNumberField("Hash number of bits", getNoBit());
            jsonGenerator.writeStringField("Hash precision", String.valueOf(getPrecision()));
            jsonGenerator.writeStringField("Hash value", getDHash().getHashValue().toString());
        }
    }

    private Hash getDHash() throws IOException {
        HashingAlgorithm hashAlg = new DifferenceHash(getNoBit(), getPrecision());
        return hashAlg.hash(getHashPath().toFile());
    }

    private String toBinary(int noBit, DifferenceHash.Precision precision, BigInteger hashValue) {
        String hash = hashValue.toString(2);
        int numZeros = noBit;
        if (precision == DifferenceHash.Precision.Double) {numZeros = 2 * noBit;}
        if (precision == DifferenceHash.Precision.Triple) {numZeros = 3 * noBit;}
        numZeros -= hash.length();
        String pad = StringUtils.repeat("0", numZeros);
        return pad + hash;
    }

}
