# Image Analysis

REST API for internal & external use.

# Purpose

A hash value for an image can be found by scaling it to for instance 8X8 pixel. Then the image is grayscaled.
A hash value can now be found by for instance setting 0 if the pixel is darker than the pixel next to it; 1 otherwise.

# Usage

The Image Analysis Webservice gives series of gradually more refined hash values, where the start and end of the scale can be set.
The third input parameter is the URL to the image. 
 
Possible options to give hash values are either Perceptive Hash (PHash) or Difference Hash (DHash). 

A third option - HashDistance - can give the distance between 2 hash values, which can be found by putting 2 BigInteger values in hash1 and hash2. A rule of thumb is that 
if the value is below 10 the images are very similar.

Image analysis can be accessed at http://localhost:8080/ds-image-analysis/swagger-ui if a tomcat is running on the local server.

# Build 
The project can be built with 
mvn package
The war-file is then transferred to the tomcat 9 and can then be started/stopped by using tomcat 9 startup.sh/shutdown.sh
