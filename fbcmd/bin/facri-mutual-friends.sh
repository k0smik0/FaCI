#!/bin/bash

[ -f etc/facri.conf ] && source etc/facri.conf

source bin/facri-friends.sh

# friends_ids=$($FACRI_ROOT/bin/facri-friends.sh)

friends_ids=$(getFriends)

function getMutualFriends() {

  local OUTPUT=$FACRI_OUTPUT/mutual_friends

  [ -d $OUTPUT ] || mkdir $OUTPUT

  for i in ${friends_ids};
  do
      echo -n "acting on ${i}: "
      local TARGET=$OUTPUT/${i}_-_mutual_friends
      $FBCMD mutual $i > $TARGET
      [ $(ls -s $TARGET | cut -d" " -f1) -gt 0 ] && echo "ok" || echo "error"
  done

}

[ $1 == "mf" ] && getMutualFriends
