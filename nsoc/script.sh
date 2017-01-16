mvn clean
mvn install:install-file -Dfile=libraries/bacnet-1.0.jar -Dversion=1.0 -DgroupId=nsoc
-DartifactId=bacnet -Dpackaging=jar
mvn install
