package dk.kb.api.webservice;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import dk.kb.api.DefaultApi;
import dk.kb.model.DistanceReplyDto;
import dk.kb.model.HashReplyDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiServiceImpl  implements DefaultApi {

    private Path uniquePath;
    private int hashSize;
    private StringBuilder replyBuilder = new StringBuilder();
    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

    public ApiServiceImpl() {
    }

    public Path getUniquePath() {
        return uniquePath;
    }

    public void setUniquePath(Path uniquePath) {
        this.uniquePath = this.uniquePath == null ? uniquePath : this.uniquePath;
    }

    /**
     * Find the distance between two hash values
     * @param hashes The string with two BigInteger hash values separated by a ; character
     * @return The hamming distance between the two hash values
     */
    @Override
    public DistanceReplyDto getHashDistance(String hashes) {
        DistanceReplyDto reply = new DistanceReplyDto();
        if ((hashes != null) && (!hashes.isEmpty()) && (hashes.contains(";"))) {
            String[] hashValues = hashes.split(";");
            try {
                BigInteger hash1 = new BigInteger(hashValues [0]);
                BigInteger hash2 = new BigInteger(hashValues[1]);
                if ((hash1.compareTo(BigInteger.ZERO) == -1) || (hash2.compareTo(BigInteger.ZERO) == -1)) {
                    reply.setMessage("Both hash values has to be positive");
                    return reply;
                }

                int distanceBits = 0;
                // Increase distanceBits by going through the xor'ed number
                BigInteger x = hash1.xor(hash2);
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
                reply.setMessage("Both values has to be BigInteger separated by ; character");
                return reply;
            }
        }
        else {
            reply.setMessage("The two hash values has to be divided by ; character");
        }
        return reply;
    }

    /**
     * Returns the discrete hash values of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The discrete hash values
     */
    @Override
    public HashReplyDto getImageDHash(String imgURL) {
        HashReplyDto reply = new HashReplyDto();
        try {
            try {
                File imgHash = getFile(imgURL);

                hashValue(replyBuilder, imgHash, DifferenceHash.Precision.Simple);
                hashValue(replyBuilder, imgHash, DifferenceHash.Precision.Double);
                hashValue(replyBuilder, imgHash, DifferenceHash.Precision.Triple);
                reply.setMessage(replyBuilder.toString());
            } catch (MalformedURLException e) {
                reply.setMessage("The URL: " + imgURL + " is malformed. Please use a valid URL");
                log.error("The URL: " + imgURL + " is malformed", e);
            } finally {
                Files.deleteIfExists(getUniquePath());
            }
        } catch (IOException ex) {
            reply.setMessage("A file error appeared.");
            log.error("A file error appeared", ex);
        }
        return reply;
    }

    private void hashValue(StringBuilder replyBuilder, File imgHash, DifferenceHash.Precision precision) throws IOException {
        for (int i = 4; i <= 16; i++) {
            hashSize = i*(i+1);
            replyBuilder
                    .append(hashSize)
                    .append(",")
                    .append(precision)
                    .append(",")
                    .append(getDHash(imgHash, hashSize, precision).getHashValue().toString())
                    .append(" ");
        }
    }

    private void hashValue(StringBuilder replyBuilder, File imgHash) throws IOException {
        for (int i = 4; i <= 16; i++) {
            hashSize = i*i;
            replyBuilder
                    .append(hashSize)
                    .append(",")
                    .append(getPHash(imgHash, hashSize).getHashValue().toString())
                    .append(" ");
        }
    }

    private Hash getDHash(File imgHash, int noBit, DifferenceHash.Precision precision) throws IOException {
        HashingAlgorithm hashAlg = new DifferenceHash(noBit, precision);
        return hashAlg.hash(imgHash);
    }

    private Hash getPHash(File imgHash, int noBit) throws IOException {
        HashingAlgorithm hashAlg = new PerceptiveHash(noBit);
        return hashAlg.hash(imgHash);
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

    private String toBinary(int noBit, BigInteger hashValue) {
        String hash = hashValue.toString(2);
        int numZeros = noBit - hash.length();
        String pad = StringUtils.repeat("0", numZeros);
        return pad + hash;
    }

    /**
     * Returns the perceptive hash value of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The perceptive hash value
     */
    @Override
    public HashReplyDto getImagePHash(String imgURL) {
        HashReplyDto reply = new HashReplyDto();
        try {
            File imgHash = getFile(imgURL);
            HashingAlgorithm pHashAlg = new PerceptiveHash(64);
            doHash(reply, imgHash);
        } catch (MalformedURLException e) {
            reply.setMessage("The URL: " + imgURL + " is malformed. Please use a valid URL");
            log.error("The URL: " + imgURL + " is malformed", e);
        } catch (IOException e) {
            reply.setMessage("A file error appeared.");
            log.error("A file error appeared", e);
        }

        return reply;
    }


    private void doHash(HashReplyDto reply, File imgHash) throws IOException {
        hashValue(replyBuilder, imgHash);
        reply.setMessage(replyBuilder.toString());
        Files.deleteIfExists(getUniquePath());
    }

    private File getFile(String imgPath) throws IOException {
        setUniquePath(Files.createTempFile("image", "jpg"));
        URL webImage = new URL(imgPath);
        File imgHash = new File(String.valueOf(getUniquePath()));
        FileUtils.copyURLToFile(webImage, imgHash);
        return imgHash;
    }
}
