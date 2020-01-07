# KB API

REST API for internal & external use.

# Purpose

Primary entry point for access to digital material at the Royal Danish Library.

## Updating the API

It is easiest to use the [online Swagger editor](https://editor.swagger.io/) with the Swagger YAML from
`src/main/swagger/ds-pictureHash.yml`. It supports contextual parameter suggestions & code completion with `CTRL-space`
and warns on syntactical errors.

## How to build

Adapted from
https://github.com/swagger-api/swagger-codegen/wiki/Server-stub-generator-HOWTO#java-jax-rs-apache-cxf-2--3
(execute from the `ds-pictureHash`-folder)

 1: Build Swagger codegen:  
```
pushd .. 
git clone https://github.com/swagger-api/swagger-codegen
cd swagger-codegen
git checkout 3.0.0
mvn package
popd
```

2: Use Swagger codegen:
   
   
```
java -jar ../swagger-codegen/modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate \
  --api-package dk.kb.api \
  --model-package dk.kb.api.model \
  -i src/main/swagger/ds-pictureHash.yml \
  -l jaxrs-cxf
```

This will *not* overwrite the implementation classes! Only the `gen`-folder will be updated.

## Relevant links for future investigation

No command line, all maven:
https://github.com/swagger-api/swagger-samples/tree/samples-3.0/java/java-jersey2

## How to build by Maven

Project uses Swagger Codegen with Maven plug-in for OpenAPI 3.0.2 <br>
```
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>${openapi-generator-version}</version>
</dependency>
```
More information can be found here: <br>
https://openapi-generator.tech/docs/plugins <br>
https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md

## Project structure

Project contains two modules: ds-pictureHash-swagger and ds-pictureHash-ws. <br>
Module ds-pictureHash-swagger generates server code and APIs specifikation via openapi plugin. <br>
Module ds-pictureHash-ws contains implementation of the interface and responsible for code presentation via Swagger UI.<br>
yml file used to generate a code must have the same name as project. The file is located src / main / swagger in the ds-pictureHash-swagger module.<br>

## Development

http://localhost:8080/ds-pictureHash/swagger-ui/index.html <br>
http://localhost:8080/ds-pictureHash/swagger-ui/openapi.json<br>
http://localhost:8080/ds-pictureHash/api/hello<br>



