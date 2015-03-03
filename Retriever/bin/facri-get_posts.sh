#!/bin/bash

[ -f etc/facri.conf ] && source etc/facri.conf

source bin/_facri-friends
source bin/_facri-posts
source bin/_facri-me

friends_ids=$(getFriends)
me_id=$(getMe)
for friend_id in ${friends_ids}; do
  getPosts $friend_id
done

cd ${FACRI_OUTPUT
mv tmp/* friends/

getPosts $me_id
mv tmp/* me/

