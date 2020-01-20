package dk.kb.api.webservice;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import dk.kb.api.DefaultApi;
import dk.kb.model.HashReplyDto;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiServiceImpl  implements DefaultApi {
    /**
     * Returns the discrete hash value of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The discrete hash value
     */
    @Override
    public HashReplyDto getImageDHash(String imgURL) {
        HashReplyDto hashReplyDto = new HashReplyDto();
        try {
            File imgHash = getFile(imgURL);
            HashingAlgorithm dHashAlg = new DifferenceHash(64, DifferenceHash.Precision.Triple);
            doHash(hashReplyDto, imgHash, dHashAlg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashReplyDto;
    }

    /**
     * Returns the perceptive hash value of the image in imgURL
     * @param imgURL The path to the image given to the webservice.
     * @return The perceptive hash value
     */
    @Override
    public HashReplyDto getImagePHash(String imgURL) {
        HashReplyDto hashReplyDto = new HashReplyDto();
        try {
            File imgHash = getFile(imgURL);
            HashingAlgorithm pHashAlg = new PerceptiveHash(64);
            doHash(hashReplyDto, imgHash, pHashAlg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashReplyDto;
    }

    private static final String filePath= "imgHash";

    private void doHash(HashReplyDto reply, File imgHash, HashingAlgorithm algorithm) throws IOException {
        Hash hash = null;
        hash = algorithm.hash(imgHash);
        reply.setMessage(hash.getHashValue().toString());
        Files.deleteIfExists(Paths.get(filePath));
    }

    private File getFile(String imgPath) throws IOException {
        URL webImage;
        webImage = new URL(imgPath);
        File imgHash = new File(ApiServiceImpl.filePath);
        FileUtils.copyURLToFile(webImage, imgHash);
        return imgHash;
    }
}
