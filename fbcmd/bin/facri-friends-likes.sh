#!/bin/bash

source etc/facri.conf

friends_ids=$($ROOT/bin/facri-friends.sh)

OUTPUT=$FACRI_OUTPUT/likes

for i in ${friends_ids};
do
   TARGET=$OUTPUT/${i}/
   cd $TARGET
echo  $TARGET "->" $FBCMD flikes $i
done
