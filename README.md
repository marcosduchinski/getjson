## GETJSON

This project is form educational purposes only.


The goal is to develop a small framework in Kotlin addressing HTTP/GET endpoints that return JSON.
The framework will use the developed JSON library (https://github.com/marcosduchinski/ksonlib)  to automatically convert Kotlin values to JSON.
The framework should be launched by enumerating a list of REST controllers that define endpoints for HTTP/GET requests. Controllers define URL mappings through annotations, allowing programmers to map path segments and arguments into function arguments

## Instructions (after cloning the repository)

1. Download the ksonlib jar file from the releases page:

   https://github.com/marcosduchinski/ksonlib/releases/tag/v0.0.6


2. Install the ksonlib jar file in your local maven repository:

```bash
mvn install:install-file \
-Dfile=FILENAME.jar \
-DgroupId=pt.iscte.mei.pa \
-DartifactId=ksonlib \
-Dversion=VERSION \
-Dpackaging=jar
```

For example, if you downloaded the ksonlib-0.0.6.jar file, the command would be:
```bash
mvn install:install-file \
-Dfile=ksonlib-0.0.6.jar \
-DgroupId=pt.iscte.mei.pa \
-DartifactId=ksonlib \
-Dversion=0.0.6 \
-Dpackaging=jar
```

3. Update the VERSION in the pom.xml file to the version you installed in your local maven repository:

```xml
<dependency>
    <groupId>pt.iscte.mei.pa</groupId>
    <artifactId>ksonlib</artifactId>
    <version>VERSION</version>
</dependency>
```

For example, if you installed version 0.0.6, the dependency would be:

```xml
<dependency>
    <groupId>pt.iscte.mei.pa</groupId>
    <artifactId>ksonlib</artifactId>
    <version>0.0.6</version>
</dependency>
```

4. Run the tests:

```bash 
mvn test
```

5. Run the application:

```bash
 mvn exec:java -Dexec.mainClass="pt.iscte.mei.pa.MainKt"
 ```

6. Access the application in your browser:

```bash
http://localhost:8080/api/welcome
```





