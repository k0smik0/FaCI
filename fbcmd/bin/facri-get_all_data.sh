#!/bin/bash

source etc/facri.conf

source bin/_facri-posts
source bin/_facri-posts_comments
source bin/_facri-mutual_friends


# FEEDS_OUTPUT=${FACRI_OUTPUT}/friends

friends_ids=$($FACRI_ROOT/bin/facri-friends.sh)

me_id=$(getMe)

start=$(date +%s)

total_users=$(echo $ids | wc -w)
total_users=$((total_users+1))
echo "$total_users users"
user_counter=0

for user in ${friends_ids} ${me_id}; do 
   user_counter=$((user_counter+1))
   echo -n "grab data for [$user_counter/$total_users] $u user: "
   
  # get mutual friends
  getMutualFriends $user

  # get posts
  getPosts $user
  
  # get comments
  getComments $user
done

# echo -n "grab my data ($u): "
# getMutualFriends ${me_id}
# getPosts ${me_id}
# getComments ${FACRI_OUTPUT}/me ${me_id}

me_feeds_output="${FACRI_OUTPUT}/me"
mv ${friends_feeds_output}/${me} ${me_feeds_output}

end=$(date +%s)
r=$(echo $end - $start | bc)
echo -e "\n\ndone in: $($FACRI_ROOT/bin/displaytime $r).\n"

#mkdir $FEEDS_OUTPUT/friends
#mv $FEEDS_OUTPUT/* $FEEDS_OUTPUT/friends/

#mkdir ${FACRI_OUTPUT}/me/feeds
#mv ${FACRI_OUTPUT}/${me_id} ${FACRI_OUTPUT}/me/
