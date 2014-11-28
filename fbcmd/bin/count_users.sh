#!/bin/bash

out=/tmp/authors.log

find . -type f -iname "posts*.json" -exec jq ' .posts[].actor_id ' {} \; >> $out 2>/dev/null
find . -type f -iname "posts*.json" -exec jq ' .posts[].likes.sample[] ' {} \; >> $out 2>/dev/null
find . -type f -iname "posts*.json" -exec jq ' .posts[].likes.friends[] ' {} \; >> $out 2>/dev/null

sed 's/"//g' $out | sort -n | uniq | wc -l

rm -f $out
