#!/bin/bash

source etc/facri.conf
#source bin/displaytime

FEEDS_OUTPUT=$FACRI_OUTPUT/feeds/friends

friends_ids=$($FACRI_ROOT/bin/facri-friends.sh)

me_id=$(php $FBCMD whoami | awk '{print $1}')

start=$(date +%s)

cd $FEEDS_OUTPUT
total_users=$(echo $friends_ids | wc -w)
echo "$total_users users"
declare user_counter=1
for u in $friends_ids; do
   TARGET=$FEEDS_OUTPUT/$u
   [ ! -d $TARGET ] && echo "user $u not existing: go further" && continue

   echo -n "user $u [$user_counter/$total_users]: "
   cd $TARGET
   # all posts_ids
#   posts_ids=$(cat posts*.json | jq '.posts[] | .post_id')

   # posts_ids with non-zero comments
   posts_ids=$(cat posts*.json | jq '.posts[] | select((.comment_info | .comment_count != "0") or (.likes | .count !="0") or (.share_count!="0")) | .post_id')
#   echo posts_ids $posts_ids
   tot_post_ids=$(echo $posts_ids | wc -w)
   echo $tot_post_ids posts
   for c in ${posts_ids}; do
      c_id=$(echo $c | cut -d"_" -f2 | sed 's/"//g')
      echo -n -e "\t$c_id: "
      s=$($FBCMD PCOMMENTS $c_id 150) 
      [ $s ] && echo "." || echo "_"
   done
   echo -e "\n"
   user_counter=$((user_counter+1))
done

end=$(date +%s)
r=$(echo "$end - $start" | bc)
echo -e "\n\ndone in $($FACRI_ROOT/bin/displaytime $r)\n"
