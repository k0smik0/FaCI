#!/bin/bash

ROOT=$PWD
FACRI=$ROOT/facri
[ -d $FACRI ] || mkdir $FACRI

FBCMD=$ROOT/libs/fbcmd/fbcmd.php
EXEC_DIR=$ROOT/libs/fbcmd/support/

friends_id=$(php $FBCMD friends | awk '{print $1}' | grep -v ID)
echo $friends_id > $FACRI/friends_ids.txt

me_id=$(php $FBCMD whoami | awk '{print $1}')

for u in $friends_id $me_id; do 
   echo -n "doing user $u ... "; 
   
   TARGET_DIR=$ROOT/facri/$u;
   [ -d $TARGET_DIR ] || mkdir $TARGET_DIR
   
   cd $EXEC_DIR
   php my_fbcmd.php FLSTREAM $u 150
   
   ls -1 *json >/dev/null && mv -i *json $TARGET_DIR 

   sleep 2

   echo " ok"
done
