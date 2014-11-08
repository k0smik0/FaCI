#!/bin/bash

[ -f etc/facri.conf ] && source etc/facri.conf

source bin/_facri-friends
source bin/_facri-mutual_friends

friends_ids=$(getFriends)

# echo $friends_ids

for friend_id in ${friends_ids}; do
  getMutualFriends $friend_id
done
