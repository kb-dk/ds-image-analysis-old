package dk.kb.api.webservice;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import dk.kb.model.PHashReplyDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class PHash extends ImageHash {

    private static final Logger log = LoggerFactory.getLogger(PHash.class);

    /**
     * Instantiation of the PHash class, which besides initiation of imgURL, start and stop
     * creates a temporary file, which is ordered to delete itself after use.
     * @param imgURL URL of the image
     * @param start The first PHash value that should be generated
     * @param end The last PHash value that should be generated
     */
    public PHash(String imgURL, Integer start, Integer end){
        super(imgURL, start, end);
        try {
            setHashPath(Files.createTempFile("image", ".hash"));
            getHashPath().toFile().deleteOnExit();
        } catch (IOException e) {
            log.error("An error occured while creating a temporary file.", e);
        }
    }

    /**
     * Generate PHash JSON
     * @return A JSON list, which
     * - the index number
     * - the number of bits that it is generated from
     * - the calculated PHash number (BigInteger converted to string)
     */
    protected List<PHashReplyDto> generateJSON() {
        List<PHashReplyDto> reply = new LinkedList<PHashReplyDto>();
        PHashReplyDto error = new PHashReplyDto();

        try {
            if ((getImgURL() != null) && (!getImgURL().toString().isEmpty())) {
                FileUtils.copyURLToFile(getImgURL(), getHashPath().toFile());
                if ((getStart() < 1) || (getEnd() < 1) || (getStart() > getEnd())) {
                    error = replyError("Both start and end have to be positive and start value has to be less or equal to end value");
                    reply.add(error);
                    return reply;
                }
                PHashReplyDto tmpPHash;
                for (int i = getStart(); i <= getEnd(); i++) {
                    setNoBit(i * i);
                    tmpPHash = new PHashReplyDto();
                    tmpPHash.setNumber(i);
                    tmpPHash.setBit(getNoBit());
                    tmpPHash.setValue(getPHash().getHashValue().toString());
                    reply.add(tmpPHash);
                }
            }
            else {
                error = replyError("The URL has to defined");
                reply.add(error);
            }
        } catch (IOException e) {
            error = replyError("A file error appeared.");
            reply.add(error);
            log.error("A file error appeared", e);
        } catch (Exception ex) {
            error = replyError("An unhandled exception happened");
            reply.add(error);
            log.error("An unhandled exception happened", ex);
        }
        return reply;
    }

    private PHashReplyDto replyError(String error) {
        PHashReplyDto pHashReplyDto = new PHashReplyDto();
        pHashReplyDto.setBit(0);
        pHashReplyDto.setNumber(0);
        pHashReplyDto.setValue(error);
        return pHashReplyDto;
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
