#!/bin/bash

for d in $(find . -mindepth 1 -type d); do 
   dire=$d
   old=$(find $d -type f -iname 2014*json)
#   query=$(head -1 $old | sed 's/\/\/QUERY:\ //g')
#   echo -e $query"\n" > $d/query.fql
#   r=$(grep -v QUERY $old | grep -v ^$)
#   echo $r > $d/tmp.json
   echo -n '{"posts":' > $d/output.json
   cat $old >> $d/output.json
   echo "}" >> $d/output.json
#   echo mv -vf $d/tmp.json $old

   echo; 
done
