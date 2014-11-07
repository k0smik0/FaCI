#!/bin/bash

source etc/facri.conf

source bin/_facri-friends

friends_ids=$(getFriends)

output=${FACRI_OUTPUT}/likes

echo TO_SYSTEM
for friend_id in ${friends_idsA};
do
  target=$output/${friend_id}/likes
  [ -d $target] || mkdir $target
  cd $target
  echo $FBCMD flikes $friend_id
done
