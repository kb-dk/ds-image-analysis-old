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

public class ApiServiceImpl  implements DefaultApi {
    /**
     * Returns the discrete hash value of the image in imgPath
     * @param imgPath The path to the image given to the webservice.
     * @return The discrete hash value
     */
    @Override
    public HashReplyDto getImageDHash(String imgPath) {
        HashReplyDto HashReplyDto = new HashReplyDto();
        try {
            File imgHash = getFile(imgPath);
            HashingAlgorithm dHashAlg = new DifferenceHash(64, DifferenceHash.Precision.Triple);
            doHash(HashReplyDto, imgHash, dHashAlg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return HashReplyDto;
    }

    /**
     * Returns the perceptive hash value of the image in imgPath
     * @param imgPath The path to the image given to the webservice.
     * @return The perceptive hash value
     */
    @Override
    public HashReplyDto getImagePHash(String imgPath) {
        HashReplyDto HashReplyDto = new HashReplyDto();
        try {
            File imgHash = getFile(imgPath);
            HashingAlgorithm pHashAlg = new PerceptiveHash(64);
            doHash(HashReplyDto, imgHash, pHashAlg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HashReplyDto;
    }

    private void doHash(HashReplyDto hashReplyDto, File imgHash, HashingAlgorithm hashingAlgorithm) throws IOException {
        Hash hash = null;
        hash = hashingAlgorithm.hash(imgHash);
        hashReplyDto.setMessage(hash.getHashValue().toString());
    }

    private static final String filePath= "~/Pictures/imgPHash";
    private File getFile(String imgPath) throws IOException {
        URL webImage;
        webImage = new URL(imgPath);
        File imgHash = new File(ApiServiceImpl.filePath);
        FileUtils.copyURLToFile(webImage, imgHash);
        return imgHash;
    }
}
