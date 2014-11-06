#!/bin/bash

[ -f etc/facri.conf ] && source etc/facri.conf

function getFriends() {
  OUT_FILE=$FACRI_OUTPUT/friends_ids.txt

  if [ -f ${OUT_FILE} ]
  then
    friends_ids=$(cat ${OUT_FILE}) 
  else
    friends_ids=$($FBCMD friends | awk '{print $1}' | grep -v ID | sort -n)
    echo $friends_ids > ${OUT_FILE}
  fi

  echo ${friends_ids}
}

[ $# == "get" ] && getFriends

