#!/bin/bash

#classpath=libs/guice/*.jar:$(echo $(find libs -type d ! -iname guice -exec echo {}/*.jar: \;) |sed 's/ //g' | sed 's/:$//'):bin

classpath_to_build=$(find libs/guice libs/guava -iname *.jar):$(find libs -type f -iname *.jar | egrep -v "guice|guava")
classpath=$(echo $classpath_to_build| sed 's/ /:/g' | sed 's/:$//'):bin

opengl="-Dsun.java2d.opengl=True"
#echo \
java $opengl -classpath ${classpath} net.iubris.facri.main.Main $@

