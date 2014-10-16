#!/bin/bash

for i in $(find . -mindepth 1 -type d)
do
#   echo -n "$i "
   fql="$i/posts.fql"
   if [ -f $fql ]
   then 
      datesuffix=$(cat $fql | jq '.query | .date' | sed 's/"//g')
      mv ${i}/posts.json ${i}/posts_${datesuffix}.json
   fi
done
