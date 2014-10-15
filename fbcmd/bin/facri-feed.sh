#!/bin/bash

source etc/facri.conf

FEEDS_OUTPUT=$FACRI_OUTPUT/feeds/friends

friends_ids=$($FACRI_ROOT/bin/facri-friends.sh)

me_id=$($FBCMD whoami | awk '{print $1}')

start=$(date +%s)

for u in $friends_ids $me_id; do 
   echo -n "doing user $u ... "; 
   
   TARGET_DIR=$FEEDS_OUTPUT/$u;
   [ -d $TARGET_DIR ] || mkdir -p $TARGET_DIR
   
#   cd $EXEC_DIR
   cd $TARGET_DIR
   php my_fbcmd.php FLSTREAM $u 150
#   comments_ids=$(cat posts*.json | jq ".posts"  |grep post_id | cut -d":" -f2 | sed 's/ "//g' | sed 's/",//g')
   comments_ids=$(cat posts*.json | jq '.posts[] | select((.comment_info | .comment_count != "0") or (.likes | .count !="0") or (.share_count!="0")) | .post_id')
   for c in ${comments_ids}; do
      c_id=$(echo $c | cut -d"_" -f2 | sed 's/"//g')
      php my_fbcmd.php PCOMMENTS $c_id 150
   done
   
#   ls -1 *json >/dev/null && mv -i *json $TARGET_DIR 
   sleep 1

   echo " ok"
done

end=$(date +%s)
r=$(echo $end - $start | bc)
echo -e "\n\ndone in: $($FACRI_ROOT/bin/displaytime $r).\n"

#mkdir $FEEDS_OUTPUT/friends
#mv $FEEDS_OUTPUT/* $FEEDS_OUTPUT/friends/

mkdir $FEEDS_OUTPUT/../me
mv $FEEDS_OUTPUT/$me_id $FEEDS_OUTPUT/../me/
