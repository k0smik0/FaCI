#!/bin/bash

ROOT=$PWD
FACRI=$ROOT/facri_output
[ -d $FACRI ] || mkdir $FACRI

FBCMD=$ROOT/libs/fbcmd/fbcmd.php
EXEC_DIR=$ROOT/libs/fbcmd/support/

friends_id=$(php $FBCMD friends | awk '{print $1}' | grep -v ID)
echo $friends_id > $FACRI/friends_ids.txt

me_id=$(php $FBCMD whoami | awk '{print $1}')

start=$(date +%s.%3N)

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

end=$(date +%s.%3N)
r=$(echo $end - $start | bc)


#echo -e "\n\ndone in $((end-start)).\n"

mkdir $FACRI/friends
mv $FACRI/* $FACRI/friends/

mkdir $FACRI/me
mv $FACRI/friends/$me_id $FACRI/me/
