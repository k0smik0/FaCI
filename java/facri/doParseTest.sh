#!/bin/bash

#classpath=libs/guice/*.jar:$(echo $(find libs -type d ! -iname guice -exec echo {}/*.jar: \;) |sed 's/ //g' | sed 's/:$//'):bin

function getJars() {
   jars=$(find $@ -type f -iname *.jar)
   echo $jars
}

#classpath_to_build=$(find ../../../Heimdall/bin):$(find libs/guice libs/guava -iname *.jar):$(find libs -type f -iname *.jar | egrep -v "guice|guava")
classpath_to_build=$(find ../../../Heimdall/bin):$(find libs/guice libs/guava -type f -iname *jar):$(find libs -type f -iname *jar | egrep -v "guice|guava" | sort)

#others=$(getJars libs | egrep -v "guice|guava|NO" | sort)
#echo others $others
#echo $classpath_to_build
classpath=$(echo $classpath_to_build| sed 's/ /:/g' | sed 's/:$//'):bin
#echo $classpath

#opengl="-Dsun.java2d.opengl=True"
#echo \
java $opengl -classpath ${classpath} net.iubris.facri.parsers.ParseTestMain $@

