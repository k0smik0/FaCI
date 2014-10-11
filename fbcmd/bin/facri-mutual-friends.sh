#!/bin/bash

source etc/facri.conf

friends_ids=$($ROOT/bin/facri-friends.sh)

OUTPUT=$FACRI_OUTPUT/mutual/

for i in ${friends_ids};
do
   TARGET=$OUTPUT/${i}_-_mutual_friends
   $FBCMD mutual $i > $TARGET
done