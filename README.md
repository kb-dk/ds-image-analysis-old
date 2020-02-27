# Image Analysis

REST API for internal & external use.

## Purpose

A hash value for an image can be found by scaling it to for instance 8X8 pixel. Then the image is grayscaled.
A hash value can now be found by for instance setting 0 if the pixel is darker than the pixel next to it; 1 otherwise.

## Usage

The Image Analysis Webservice gives series of gradually more refined hash values, where the start and end of the scale can be set.
The third input parameter is the URL to the image. 
 
Possible options to give hash values are either Perceptive Hash (PHash) or Difference Hash (DHash). 

A third option - HashDistance - can give the distance between 2 hash values, which can be found by putting 2 BigInteger values in hash1 and hash2. A rule of thumb is that 
if the value is below 10 the images are very similar.

Image analysis can be accessed either through TomCat (local installation of TomCat 9 required) or through jetty (local installation is not required). <br>

The project is configured to run Jetty locally as well (local installation is not required). <br>
It is important to use ```jetty: run-war``` when starting jetty server. It wraps the application in a war file first and deploys to Jetty server afterwards. Jetty is watching the pom.xml file and re-deploying the application as soon as there are changes.

### Jetty
http://localhost:8080/ds-image-analysis-ws/swagger-ui/index.html <br>
http://localhost:8080/ds-image-analysis-ws/api/ping<br>

### Tomcat
http://localhost:8080/ds-image-analysis/swagger-ui/index.html <br>
http://localhost:8080/ds-image-analysis/api/ping<br>


## Build 
The project is built with 
```mvn package```
The war-file has then to be transferred to the tomcat 9 and can then be started/stopped by using tomcat 9 startup.sh/shutdown.sh
