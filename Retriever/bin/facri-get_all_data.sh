#!/bin/bash

source etc/facri.conf

source bin/_facri-me
source bin/_facri-posts
source bin/_facri-posts_comments
source bin/_facri-mutual_friends

friends_ids=$(getFriends)

me_id=$(getMe)

start=$(date +%s)

# echo friends_ids: ${friends_ids[@]}
total_users=$(echo $friends_ids | wc -w)
total_users=$((total_users+1))
# echo $total_users
echo "$total_users users"
user_counter=0

for user in ${friends_ids} ${me_id}; do 
   user_counter=$((user_counter+1))
   echo "[${user_counter}/${total_users}] grab data for $user user: "
   
  # get mutual friends
  getMutualFriends $user

  # get posts
  getPosts $user
  
  # get comments
   getComments $user
done

me_feeds_output="${FACRI_OUTPUT}/me/${me_id}"
[ -d $me_feeds_output ] || mkdir -p $me_feeds_output
mv ${FACRI_OUTPUT}/${me_id}/* ${me_feeds_output}/

end=$(date +%s)
r=$(echo $end - $start | bc)
echo -e "\n\ndone in: $($FACRI_ROOT/bin/displaytime $r).\n"
