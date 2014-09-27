#!/bin/bash

ROOT=$PWD
mkdir $ROOT/facri

EXEC_DIR=$ROOT/libs/fbcmd/support/

for u in $(fbcmd friends | awk '{print $1}'); do 
   echo "doing user $u"; 
   
   TARGET_DIR=$ROOT/facri/$u;
   [ -d $TARGET_DIR ] || mkdir $TARGET_DIR
   
   cd $EXEC_DIR
   php my_fbcmd.php FLSTREAM $u 150
   
   ls -1 *json && mv -vi *json $TARGET_DIR 

   sleep 2
done
