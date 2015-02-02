#!/bin/bash

#classpath=libs/guice/*.jar:$(echo $(find libs -type d ! -iname guice -exec echo {}/*.jar: \;) |sed 's/ //g' | sed 's/:$//'):bin

function getJars() {
   local jars=$(find $1 -type f -iname *.jar)
   echo $jars
}

function getPath() {
   echo $(find $1 -type d)
}

function getGuice() {
   local guice=$(find libs/guice libs/guava -iname *.jar |egrep -v "sisu|guice-3.0.jar|aopalliance")
   echo $guice
}

function getHeimdall() {
#   classes=$(getPath "../../../Heimdall/bin")
# using project
#   local c="../../../Heimdall/bin"
#   echo $c
# using jar
   echo $(getJars libs/heimdall)
}

function getBerkeleyPersister() {
   # using project
#   local d="../../../BerkeleyPersister"
#   echo "$(getJars $d/libs/berkeleydb):$d/bin"
   # using jar
   echo $(getJars libs/berkeley_persister)
}

function getGrph() {
   echo $(getJars libs/grph)
}
function getGraphstream() {
   echo $(getJars libs/graphstream/nightly)
}
function getStaxon() {
   echo $(getJars libs/staxon)
}
function getOpencsv() {
   echo $(getJars libs/opencsv)
}
function getCommons() {
   echo  $(getJars libs/commons)
}
function getJFreeChart() {
   echo $(getJars libs/jfreechart)
}

function getOthers() {
   echo $(find libs -type f -iname *.jar | egrep -v "guice|guava")
}


classpath_to_build=$(getGuice):$(getHeimdall):$(getBerkeleyPersister):$(getGrph):$(getGraphstream):$(getStaxon):$(getOpencsv):$(getCommons):$(getJFreeChart)
#classpath_to_build=$(getGuice):$(getHeimdall):$(getGrph):$(getGraphstream):$(getStaxon):$(getOpencsv)
classpath=$(echo $classpath_to_build| sed 's/ /:/g' | sed 's/:$//'):bin
#echo $classpath

#opengl="-Dsun.java2d.opengl=True"
#echo \
java $opengl -classpath ${classpath} net.iubris.facri.main.ConsoleMain $@

