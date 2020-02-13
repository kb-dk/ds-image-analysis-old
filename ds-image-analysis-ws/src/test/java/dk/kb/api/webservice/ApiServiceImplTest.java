package dk.kb.api.webservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiServiceImplTest {



    @Test
    void getImageDHash() {
        String url = "http://kb-images.kb.dk/DAMJP2/online_master_arkiv/non-archival/KOB/bs_kistebilleder-2/bs000001/full/!345,2555/0/native.jpg";
        ApiServiceImpl tester = new ApiServiceImpl();
        String result = tester.getImageDHash(url, 1, 8).toString();
        Assertions.assertTrue(result.contains("520707519"));
        result = tester.getImageDHash(url,  -1, 8).toString();
        Assertions.assertTrue(result.contains("Both start and end have to be positive and start value has to be less or equal to end value"));
        result = tester.getImageDHash(url,  8, 1).toString();
        Assertions.assertTrue(result.contains("Both start and end have to be positive and start value has to be less or equal to end value"));
        url = "https://kb-images.kb.dk/DAMJP2/online_master_arkiv/non-archival/KOB/bs_kistebilleder-2/bs000001/full/!345,2555/0/native.jpg";
        result = tester.getImageDHash(url, 1, 8).toString();
        Assertions.assertTrue(result.contains("A file error appeared"));
        result = tester.getImageDHash(null, 1, 8).toString();
        Assertions.assertTrue(result.contains("The URL has to defined"));
        result = tester.getImageDHash("", 1, 8).toString();
        Assertions.assertTrue(result.contains("The URL has to defined"));
        result = tester.getImageDHash("https://test",  1, 8).toString();
        Assertions.assertTrue(result.contains("A file error appeared"));
    }

    @Test
    void getImagePHash() {
        String url = "http://kb-images.kb.dk/DAMJP2/online_master_arkiv/non-archival/KOB/bs_kistebilleder-2/bs000001/full/!345,2555/0/native.jpg";
        ApiServiceImpl tester = new ApiServiceImpl();
        String result = tester.getImagePHash(url,  1, 8).toString();
        Assertions.assertTrue(result.contains("17997"));
        result = tester.getImagePHash(url,  -1, 8).toString();
        Assertions.assertTrue(result.contains("Both start and end have to be positive and start value has to be less or equal to end value"));
        result = tester.getImagePHash(url,  8, 1).toString();
        Assertions.assertTrue(result.contains("Both start and end have to be positive and start value has to be less or equal to end value"));
        url = "https://kb-images.kb.dk/DAMJP2/online_master_arkiv/non-archival/KOB/bs_kistebilleder-2/bs000001/full/!345,2555/0/native.jpg";
        result = tester.getImagePHash(url,  1, 8).toString();
        Assertions.assertTrue(result.contains("A file error appeared"));
        result = tester.getImagePHash(null,  1, 8).toString();
        Assertions.assertTrue(result.contains("The URL has to defined"));
        result = tester.getImagePHash("",  1, 8).toString();
        Assertions.assertTrue(result.contains("The URL has to defined"));
        result = tester.getImagePHash("https://test",  1, 8).toString();
        Assertions.assertTrue(result.contains("A file error appeared"));
    }

    @Test
    void getHashDistance() {
        ApiServiceImpl tester = new ApiServiceImpl();
        String result = "";
        result = tester.getHashDistance("4","1").toString();
        Assertions.assertTrue(result.contains("message: 2"));
        result = tester.getHashDistance("-4","1").toString();
        Assertions.assertTrue(result.contains("message: Both hash values have to be positive"));
        result = tester.getHashDistance("4","-11").toString();
        Assertions.assertTrue(result.contains("message: Both hash values have to be positive"));
        result = tester.getHashDistance("-4","-11").toString();
        Assertions.assertTrue(result.contains("message: Both hash values have to be positive"));
        result = tester.getHashDistance("abc","def").toString();
        Assertions.assertTrue(result.contains("message: Both values have to be BigInteger"));
        result = tester.getHashDistance("", "").toString();
        Assertions.assertTrue(result.contains("message: Both values have to be defined"));
        result = tester.getHashDistance(null, null).toString();
        Assertions.assertTrue(result.contains("message: Both values have to be defined"));
        result = tester.getHashDistance("4","4").toString();
        Assertions.assertTrue(result.contains("message: 0"));
        result = tester.getHashDistance("00100011","100011100").toString();
        Assertions.assertTrue(result.contains("message: 19"));
    }
}