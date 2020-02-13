package dk.kb.api.webservice;

import dk.kb.api.DefaultApi;
import dk.kb.model.DistanceReplyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;

public class ApiServiceImpl  implements DefaultApi {

    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

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

    /**
     * Returns the distance between the two hashvalues, which is calculated by going through each binary
     * hash value and increase the distance by one for each bit. This can be done by xoring the 2 hash values.
     * @param hash1
     * @param hash2
     * @return The distance beween the 2 hash values.
     */
    @Override
    public DistanceReplyDto getHashDistance(String hash1, String hash2) {
        DistanceReplyDto reply = new DistanceReplyDto();
        if ((hash1 != null) && (!hash1.isEmpty()) && (hash2 != null) && (!hash2.isEmpty())) {
            try {
                BigInteger numHash1 = new BigInteger(hash1);
                BigInteger numHash2 = new BigInteger(hash2);
                if ((numHash1.compareTo(BigInteger.ZERO) == -1) || (numHash2.compareTo(BigInteger.ZERO) == -1)) {
                    reply.setMessage("Both hash values have to be positive");
                    return reply;
                }

                int distance = 0;
                // Increase distance by going through the xor'ed number
                BigInteger x = numHash1.xor(numHash2);
                while (x.compareTo(BigInteger.ZERO) == 1)
                {
                    // Similar to distance += x & 1
                    if (x.and(BigInteger.ONE).compareTo(BigInteger.ZERO) == 1) {
                        distance++;
                    }
                    x = x.shiftRight(1);
                }
                reply.setMessage(String.valueOf(distance));
            } catch (NumberFormatException nfe) {
                reply.setMessage("Both values have to be BigInteger");
                log.error("Both values have to be BigInteger", nfe);
                return reply;
            }
        }
        else {
            reply.setMessage("Both values have to be defined");
            log.warn("Both values have to be defined");
        }
        return reply;
    }

}
