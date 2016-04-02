#!/bin/bash

#classpath=libs/guice/*.jar:$(echo $(find libs -type d ! -iname guice -exec echo {}/*.jar: \;) |sed 's/ //g' | sed 's/:$//'):bin

LIBS_DIR=../faci/libs

if [ $(find bin | grep class >/dev/null) ]; then
   ant build
fi

function getJars() {
   local jars=$(find $1 -type f -iname *.jar)
   echo $jars
}

function getPath() {
   echo $(find $1 -type d)
}

function getGuice() {
   local guice=$(find $LIBS_DIR/guice $LIBS_DIR/guava -iname *.jar |egrep -v "sisu|guice-3.0.jar|aopalliance")
   echo $guice
}

function getHeimdall() {
#   classes=$(getPath "../../../Heimdall/bin")
# using project
#   local c="../../../Heimdall/bin"
#   echo $c
# using jar
   echo $(getJars $LIBS_DIR/heimdall)
}

function getJBBP() {
   # using project
#   local d="../../../BerkeleyPersister"
#   echo "$(getJars $d/libs/berkeleydb):$d/bin"
   # using jar
   echo $(getJars $LIBS_DIR/jbbp)
}

function getGrph() {
   echo $(getJars $LIBS_DIR/grph)
}
function getGraphstream() {
   echo $(getJars $LIBS_DIR/graphstream/nightly)
}
function getStaxon() {
   echo $(getJars $LIBS_DIR/staxon)
}
function getOpencsv() {
   echo $(getJars $LIBS_DIR/opencsv)
}
function getCommons() {
   echo  $(getJars $LIBS/commons)
}
function getJFreeChart() {
   echo $(getJars $LIBS_DIR/jfreechart)
}
function getIshtaran() {
   echo "../../../Ishtaran/bin/"
}
function getFaciCore() {
   echo "../faci/class"
}

function getOthers() {
   echo $(find $LIBS_DIR -type f -iname *.jar | egrep -v "guice|guava")
}


classpath_to_build=$(getGuice):$(getHeimdall):$(getJBBP):$(getGraphstream):$(getStaxon):$(getOpencsv)
#classpath_to_build=$(getGuice):$(getHeimdall):$(getGrph):$(getGraphstream):$(getStaxon):$(getOpencsv)
classpath=$(echo $classpath_to_build| sed 's/ /:/g' | sed 's/:$//'):bin
#echo $classpath

#opengl="-Dsun.java2d.opengl=True"
#echo \
java $opengl -classpath ${classpath}:$(getIshtaran):$(getFaciCore) net.iubris.facri.gui.main.GUIMain  $@

