#!/bin/bash

source etc/facri.conf

source bin/_facri-me
source bin/_facri-posts
source bin/_facri-posts_comments
source bin/_facri-mutual_friends

parallelize=false
if [ "$1" == "pACTHUNG" ]
then
  parallelize=true
  max_parallelize=60 # n * 3 task * 10 threads each
#   echo "parallelizing !"
fi

friends_ids=$(getFriends)

me_id=$(getMe)
# echo $me_id

start=$(date +%s)

# echo friends_ids: ${friends_ids[@]}
total_users=$(echo $friends_ids | wc -w)
total_users=$((total_users+1))
# echo $total_users
echo "$total_users users"
user_counter=0

function getData() {
   local user=$1
   local percent=$2
   local parallelize=$3
   
   # get mutual friends
   getMutualFriends $user $parallelize

   # get posts
   getPosts $user $parallelize
  
   # get comments
   getComments $user $parallelize

   echo "[$percent] ${user}: ok"

   if [ $parallelize == false ]; then
    if [ -f ${FACRI_DONE_CALL} ] && [ $(wc -l $FACRI_DONE_CALL | awk '{print $1}') -gt 0 ]; then
# 	echo $FACRI_DONE_CALL
	rm -f $FACRI_DONE_CALL
	for i in `seq 1 5`; do echo -n "."; sleep 1; done
	echo	
    fi
   fi
}

for user in ${friends_ids} ${me_id}; do
#for user in "A"; do  # dummy
  user_counter=$((user_counter+1))
  percent=${user_counter}/${total_users}
  echo "[$percent] grab data for $user user: "
   
  if [ $parallelize == true ]; then
#   echo here
    getData $user $percent $parallelize &

#     while (( $(jobs | wc -l) >= $max_parallelize )); do
    while (( $(ps axuw -e f | grep facri-get_all_data | wc -l) >= $max_parallelize )); do
	sleep 0.5
	echo -n "."
# 	jobs >/dev/null 
    done
  else
    getData $user $percent $parallelize
  fi

  echo
done
if [ ${parallelize} == true ]; then
  wait
fi

echo $me_id
me_feeds_output="${FACRI_OUTPUT}/me/${me_id}"
[ -d $me_feeds_output ] || mkdir -p $me_feeds_output
from="${FACRI_OUTPUT}/friends/${me_id}"
echo $from
mv ${from}/* ${me_feeds_output}/

corpus_dir=${FACRI_OUTPUT}/"${me_id}_corpus"
mkdir $corpus_dir
mv ${FACRI_OUTPUT}/${me_feeds_output} ${FACRI_OUTPUT}/friends ${corpus_dir}/

find ${FACRI_OUTPUT} -type f -iname ".comments" -or -iname ".posts" -exec echo {} \;

end=$(date +%s)
r=$(echo $end - $start | bc)
echo -e "\n\ndone in: $($FACRI_ROOT/bin/displaytime $r).\n"
