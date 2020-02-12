package dk.kb.api.webservice;

import dk.kb.api.DefaultApi;
import dk.kb.model.DistanceReplyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.List;

public class ApiServiceImpl  implements DefaultApi {

    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    private Path imgHash;
    private int hashSize;

    public ApiServiceImpl() {
    }

    /**
     * Returns the discrete hash values of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The discrete hash values
     */
    @Override
    public List<String> getImageDHash(String imgURL, Integer start, Integer end) {
        DHash dHash = new DHash(imgURL, start, end);
        return dHash.generateJSON();
    }

    /**
     * Returns the perceptive hash value of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The perceptive hash value
     */
    @Override
    public List<String> getImagePHash(String imgURL, Integer start, Integer end) {
        PHash pHash = new PHash(imgURL, start, end);
        return pHash.generateJSON();
    }

    @Override
    public DistanceReplyDto getHashDistance(String hash1, String hash2) {
        DistanceReplyDto reply = new DistanceReplyDto();
        if ((hash1 != null) && (!hash1.isEmpty()) &&
                (hash1 != null) && (!hash1.isEmpty())) {
            try {
                BigInteger numHash1 = new BigInteger(hash1);
                BigInteger numHash2 = new BigInteger(hash2);
                if ((numHash1.compareTo(BigInteger.ZERO) == -1) || (numHash2.compareTo(BigInteger.ZERO) == -1)) {
                    reply.setMessage("Both hash values have to be positive");
                    return reply;
                }

                int distanceBits = 0;
                // Increase distanceBits by going through the xor'ed number
                BigInteger x = numHash1.xor(numHash2);
                while (x.compareTo(BigInteger.ZERO) == 1)
                {
                    // Similar to distanceBits += x & 1
                    if (x.and(BigInteger.ONE).compareTo(BigInteger.ZERO) == 1) {
                        distanceBits++;
                    }
                    x = x.shiftRight(1);
                }
                reply.setMessage(Integer.toString(distanceBits));
            } catch (NumberFormatException nfe) {
                reply.setMessage("Both values has to be BigInteger");
                return reply;
            }
        }
        else {
            reply.setMessage("The two hash values has to be present");
        }
        return reply;
    }

}
