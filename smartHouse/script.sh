mvn clean
mvn install:install-file -Dfile=libraries/bacnet.jar -DgroupId=nsoc -Dversion=1.0 -DartifactId=bacnet -Dpackaging=jar
mvn install:install-file -Dfile=libraries/commons.jar -DgroupId=nsoc -Dversion=1.0 -DartifactId=commons -Dpackaging=jar
mvn install:install-file -Dfile=libraries/seroUtils.jar -DgroupId=nsoc -Dversion=1.0 -DartifactId=seroUtils -Dpackaging=jar
mvn package
mvn install
