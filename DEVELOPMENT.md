export environment java home 
```sh
$ export JAVA_HOME=/usr/lib/jvm/jdk-11.0.5/
```

one time project setup
---------------------
```sh
$ gradle init --type java-library 
$ gradle wrapper --gradle-version=6.0.1 
```

how to build
-----------------
```sh
$ ./gradlew build
```

how to test
-----------------
```sh
$ # test everything
$ ./gradlew test

$ # test specific tests
$ ./gradlew test --tests *test1

$ # test a class
$ ./gradlew test --tests HttpTest
```



how do I dump all current project dependencies to a folder?
-----------------
all the current dependencies should be available in depdencies folder
after this command is executed
```sh
$ ./gradlew copyDependencies
```

how to import this project to eclipse
-----------------
```sh
# generate eclipse configuration
$ ./gradlew eclipse

# import as existing project into eclipse

```


how to upgrade gradle
-----------------





Deployment
-----------------
1. build and release
   git checkout master
   update VERSION IN build.gradle
   git commit -m "version bump"
   ./gradlew clean build uploadArchives

2. login to https://s01.oss.sonatype.org/#stagingRepositories
   click close
   if all verificaitons run ok,  then click release

3. check release
   https://mvnrepository.com/search?q=ch.weetech
   https://search.maven.org/search?q=ch.weetech
   https://s01.oss.sonatype.org/content/repositories/releases/ch/weetech/
