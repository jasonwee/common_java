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
all the current dependencies should be available in dependencies folder
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
1. check current version first
```
$ export JAVA_HOME=/usr/lib/jvm/jdk-11.0.5/
$ ./gradlew --version
```
2. check gradle current release version at https://gradle.org/releases/
3. upgrade it if no problem
```
$ ./gradlew wrapper --gradle-version 6.1.1
```
4. check again version and test build
```
$ ./gradlew --version
$ ./gradlew clean build
```




# Deployment
1. build and release
   ```
   $ git checkout master
   $ # update VERSION IN build.gradle
   $ git commit -m "version bump"
   $ ./gradlew clean build uploadArchives
   ```
3. login to https://s01.oss.sonatype.org/#stagingRepositories
   * click close
   * if all verifications run ok,  then click release
4. check release
   * https://mvnrepository.com/search?q=ch.weetech
   * https://search.maven.org/search?q=ch.weetech
   * https://s01.oss.sonatype.org/content/repositories/releases/ch/weetech/
