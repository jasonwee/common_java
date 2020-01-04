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


how to upgrade gradle
-----------------
