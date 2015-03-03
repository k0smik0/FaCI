#!/bin/bash

source etc/facri.conf

declare tot=0
for i in $(find $FACRI_OUTPUT/feeds/friends -type f -iname posts*json)
do
   p=$(cat $i | jq '.posts[] | .post_id' | uniq | wc -l)
   tot=$((tot+p))
done
echo $tot
