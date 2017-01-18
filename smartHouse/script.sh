mvn clean
mvn install:install-file -Dfile=libraries/bacnet.jar -DgroupId=nsoc -Dversion=1.0 -DartifactId=bacnet -Dpackaging=jar
mvn install
