#!/bin/bash

for d in $(find . -mindepth 1 -type d); do 
   dire=$d
   old=$(find $d -type f -iname 2014*json)
   query=$(head -1 $old | sed 's/\/\/QUERY:\ //g')
   qdate=$(echo $old | cut -d"/" -f3 | sed 's/\.json//g')
   echo '{"query":{"date":"'${qdate}'","fql":"'${query}'"}}' > $d/query.fql
   
   echo $old
#   echo -e $query"\n" > $d/query.fql



#   r=$(grep -v QUERY $old | grep -v ^$)
#   echo -n '{"posts":' > $d/tmp.json
#   echo -n $r >> $d/tmp.json
#   cat $d/tmp.json | tr -d '\n' > $d/output.json
#   rm -f $d/tmp.json
#   echo -n "}" >> $d/output.json

   echo; 
done
