package dk.kb.api.webservice;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import dk.kb.model.DHashReplyDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * Instantiation of the DHash class, which besides initiation of imgURL, start and stop
     * creates a temporary file, which is ordered to delete itself after use.
     * @param imgURL URL of the image
     * @param start The first DHash value that is be generated
     * @param end The last DHash value that is be generated
     */
    public DHash(String imgURL, Integer start, Integer end){
        super(imgURL, start, end);
        try {
            setHashPath(Files.createTempFile("image", ".hash"));
            getHashPath().toFile().deleteOnExit();
        } catch (IOException e) {
            log.error("An error occured while creating a temporary file.", e);
        }
    }

    /**
     * Generate DHash JSON
     * @return A list of string which includes the URL to the image on which the DHash value is calculated.
     * The list has
     * - the index number
     * - the number of bits that it is generated from
     * - hash precision (Simple, Double, Triple)
     * - the calculated DHash number (BigInteger converted to string)
     */
    protected List<DHashReplyDto> generateJSON() {
        List<DHashReplyDto> reply = new LinkedList<DHashReplyDto>();
        DHashReplyDto error = new DHashReplyDto();
        try {
            if ((getImgURL() != null) && (!getImgURL().toString().isEmpty())) {
                FileUtils.copyURLToFile(getImgURL(), getHashPath().toFile());

                if ((getStart() < 1) || (getEnd() < 1) || (getStart() > getEnd())) {
                    error = replyError("Both start and end have to be positive and start value has to be less or equal to end value");
                    reply.add(error);
                    return reply;
                }
                setPrecision(DifferenceHash.Precision.Simple);
                hashValue(reply);

                setPrecision(DifferenceHash.Precision.Double);
                hashValue(reply);

                setPrecision(DifferenceHash.Precision.Triple);
                hashValue(reply);
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

    private DHashReplyDto replyError(String error) {
        DHashReplyDto dHashReplyDto = new DHashReplyDto();
        dHashReplyDto.setBit(0);
        dHashReplyDto.setNumber(0);
        dHashReplyDto.setPrecision("");
        dHashReplyDto.setValue(error);
        return dHashReplyDto;
    }

    private void hashValue(List<DHashReplyDto> replyList) throws IOException {
        DHashReplyDto tmpDHash;
        for (int i = getStart(); i <= getEnd(); i++) {
            setNoBit(i*(i+1));
            tmpDHash = new DHashReplyDto();
            tmpDHash.setNumber(i);
            tmpDHash.setBit(getNoBit());
            tmpDHash.setPrecision(String.valueOf(getPrecision()));
            tmpDHash.setValue(getDHash().getHashValue().toString());
            replyList.add(tmpDHash);
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
