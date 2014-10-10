#!/bin/bash

source ../etc/facri-env

friends_ids=$($ROOT/facri-friends.sh)

OUTPUT=$FACRI_OUTPUT/mutual_

for i in ${friends_ids};
do
   TARGET=$OUTPUT/${i}_-_mutual_friends
   echo $FBCMD mutual $i > $TARGET
done
