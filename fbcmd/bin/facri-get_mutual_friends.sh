#!/bin/bash

[ -f etc/facri.conf ] && source etc/facri.conf

source bin/_facri-friends
source bin/_facri-mutual_friends

friends_ids=$(getFriends)
#friends_ids=$(find ${FACRI_OUTPUT}/friends -maxdepth 1 -type d | awk -F"/" '{print $NF}' | grep -v friends)

# echo $friends_ids

for friend_id in ${friends_ids}; do
 getMutualFriends $friend_id
done
