#!/bin/bash

source etc/facri.conf

source bin/_facri-friends
source bin/_facri-posts_comments
source bin/_facri-me

#source bin/displaytime

start=$(date +%s)

function getAllUsersComments() {
#  local feeds_output=$1
#  shift 
  #local ids=$@
  local ids=$1
  local total_users=$(echo $ids | wc -w)
  echo "$total_users users"
  local user_counter=0
  
  for u in $ids; do
    local target=${feeds_output}/$u/feeds
    local user_counter=$((user_counter+1))
    echo -n "\t\tcomments for [$user_counter/$total_users] $u user posts: "
    [ ! -d $target ] && echo "not existant, go further." && continue

    getComments $target
  done
}

friends_feeds_output="${FACRI_OUTPUT}/friends/"
friends_ids=$(getFriends)
me_id=$(getMe)
# getAllUsersComments $friends_feeds_output ${friends_ids} ${me}
getAllUsersComments ${friends_ids} ${me}

me_feeds_output="${FACRI_OUTPUT}/me"
mv ${friends_feeds_output}/${me} ${me_feeds_output} 

end=$(date +%s)
r=$(echo "$end - $start" | bc)
echo -e "\n\ndone in $($FACRI_ROOT/bin/displaytime $r)\n"