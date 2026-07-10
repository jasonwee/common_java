export environment java home 
```sh
$ export JAVA_HOME=/usr/lib/jvm/jdk-21.0.11/
```

one time project setup
---------------------
```sh
$ gradle init --type java-library 
$ gradle wrapper --gradle-version=9.6.1
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
$ export JAVA_HOME=/usr/lib/jvm/jdk-21.0.11/
$ ./gradlew --version
```
2. check gradle current release version at https://gradle.org/releases/
3. upgrade it if no problem
```
$ ./gradlew wrapper --gradle-version 9.6.1
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
   $ # Test publish locally first
   $ ./gradlew publishToMavenLocal
   $ # Publish to Maven Central Portal
   $ ./gradlew publishMavenJavaPublicationToMavenCentralRepository
   ```
3. # this is old url: login to https://s01.oss.sonatype.org/#stagingRepositories
   # new url: https://central.sonatype.com/publishing/namespaces
   * click close
   * if all verifications run ok,  then click release
4. check release
   * https://mvnrepository.com/search?q=ch.weetech
   * https://search.maven.org/search?q=ch.weetech
   * https://s01.oss.sonatype.org/content/repositories/releases/ch/weetech/

* https://ralph.blog.imixs.com/2024/06/19/sonatype-401-content-access-is-protected-by-token/
* https://central.sonatype.org/publish/publish-gradle/#credentials
* https://central.sonatype.org/publish/generate-token/
* https://unix.stackexchange.com/questions/481939/how-to-export-a-gpg-private-key-and-public-key-to-a-file
* https://unix.stackexchange.com/questions/349645/unable-to-export-public-keys-with-gpg-using-keyring
