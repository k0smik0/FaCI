#!/bin/bash

source ../etc/facri-env

OUT_FILE=$FACRI_OUTPUT/friends_ids.txt

if [ -f ${OUT_FILE} ]
then
   friends_ids=$(cat ${OUT_FILE}) 
else
   friends_ids=$(php $FBCMD friends | awk '{print $1}' | grep -v ID)
   echo $friends_ids > ${OUT_FILE}
fi

echo ${friends_ids}
