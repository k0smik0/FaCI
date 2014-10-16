#!/bin/bash
for i in $(seq 1 5);
do
   echo $i;
   fbcmd whoami
   sleep 1;
done
times
