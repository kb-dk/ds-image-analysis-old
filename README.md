# Image Analysis API

REST API for internal & external use.

# Purpose

Webservice that gives an image hash value, where the input parameter is the path to the image. Possible hash values are 
either Perceptive Hash (PHash) or Difference Hash (DHash). 
The PHash can be accessed by 
http://localhost:8080/ds-pictureHash/api/imagePHash/imgPath/?imgPath=<filepath> 
The DHash can be accessed by 
http://localhost:8080/ds-pictureHash/api/imageDHash/imgPath/?imgPath=<filepath> 

# Build 
The project can be built with 
mvn package
The war-file can then be started by using tomcat 9
