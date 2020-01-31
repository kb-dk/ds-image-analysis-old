# Picture Hash

REST API for internal & external use.

# Purpose

Webservice that gives a series of gradually more refined hash values, where the input parameter is the path to the image. 
Possible hash values are either Perceptive Hash (PHash) or Difference Hash (DHash). 
The PHash can be accessed by 
http://localhost:8080/ds-image-analysis/api/imagePHash/imgURL/?imgPath=<imageURL> 
The DHash can be accessed by 
http://localhost:8080/ds-image-analysis/api/imageDHash/imgURL/?imgPath=<imageURL> 

The distance between two hash values can be calculated. 
This is done with hashDistance and as parameter hashes give 2 BigInteger values separated by a ; character
The hashDistance can be accessed by
http://localhost:8080/ds-image-analysis/api/hashDistance/hashes/?hashes=<BigInteger number>;<BigInteger number>

# Build 
The project can be built with 
mvn package
The war-file can then be started by using tomcat 9
