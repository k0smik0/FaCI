#!/bin/bash

source etc/facri.conf

friends_ids=$($FACRI_ROOT/bin/facri-friends.sh)

OUTPUT=$FACRI_OUTPUT/likes

for i in ${friends_ids};
do
   TARGET=$OUTPUT/${i}/
   cd $TARGET
echo $FBCMD flikes $i
done
