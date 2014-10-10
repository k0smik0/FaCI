#!/bin/bash

#ROOT=$PWD
#FACRI=$ROOT/facri_output/feed
#[ -d $FACRI ] || mkdir $FACRI
#
#FBCMD=$ROOT/libs/fbcmd/fbcmd.php
#EXEC_DIR=$ROOT/libs/fbcmd/support/

source ../etc/facri-env

#"friends_id=$(php $FBCMD friends | awk '{print $1}' | grep -v ID)
#friends_ids_file=$FACRI/../friends_ids.txt
#[ ! -f ${friends_ids_file} ] && ( echo $friends_id > ${friends_ids_file} )

FEEDS_OUTPUT=$FACRI_OUTPUT/feeds

friends_ids=$(facri-friends.sh)

me_id=$(php $FBCMD whoami | awk '{print $1}')

start=$(date +%s.%3N)

for u in $friends_ids $me_id; do 
   echo -n "doing user $u ... "; 
   
   TARGET_DIR=$FEEDS_OUTPUT/$u;
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

mkdir $FEEDS_OUTPUT/friends
mv $FEEDS_OUTPUT/* $FEEDS_OUTPUT/friends/

mkdir $FEEDS_OUTPUT/me
mv $FEEDS_OUTPUT/friends/$me_id $FEEDS_OUTPUT/me/
