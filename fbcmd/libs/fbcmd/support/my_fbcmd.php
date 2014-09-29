#!/usr/bin/php
<?php

////////////////////////////////////////////////////////////////////////////////
//     __ _                        _                                          //
//    / _| |                      | |                                         //
//   | |_| |__   ___ _ __ ___   __| |                                         //
//   |  _| '_ \ / __| '_ ` _ \ / _` |                                         //
//   | | | |_) | (__| | | | | | (_| |                                         //
//   |_| |_.__/ \___|_| |_| |_|\__,_|                                         //
//                                                                            //
//   Facebook Command Line Interface Utility                                  //
//   http://facebook.com/fbcmd                                                //
//   http://fbcmd.dtompkins.com                                               //
//   Copyright (c) 2007,2009 Dave Tompkins                                    //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////
//                                                                            //
//  see fbcmd.php for copyright information                                   //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////

// This is a sample program to show how to create your own COMMAND(S) for FBCMD
// see: http://fbcmd.dtompkins.com/help/how-to/add-command for more info.

// *** WARNING: Do *NOT* modify this file: if you use the fbcmd UPDATE
//              this file will be updated & replaced.  Create your own instead.

// If you have a new command and would like to share it with others, visit:
// http http://fbcmd.dtompkins.com/contribute

////////////////////////////////////////////////////////////////////////////////
// Step One: include the fbcmd_include.php

  require 'fbcmd_include.php';

////////////////////////////////////////////////////////////////////////////////
// Step Two: Run the FbcmdInitInclude() procedure

  FbcmdIncludeInit();

////////////////////////////////////////////////////////////////////////////////
// Step Three: Add any arguments to be appended automatically

  FbcmdIncludeAddArgument('-quiet=0');
  FbcmdIncludeAddArgument('-facebook_debug=0');

////////////////////////////////////////////////////////////////////////////////
// Step Four: List your new commands so that FBCMD will recognize them

  FbcmdIncludeAddCommand('FNAMES','Display all your friend\'s names');
  FbcmdIncludeAddCommand('MYNOTES','Display all of your notes');
  FbcmdIncludeAddCommand('SINGLE','Display all of your single friends');
  FbcmdIncludeAddCommand('FLSTREAM','flist~Get last 150 post from your friend wallpaper');

////////////////////////////////////////////////////////////////////////////////
// Step Five: Include (run) FBCMD

  require '../fbcmd.php';

////////////////////////////////////////////////////////////////////////////////
// Step Six: Add your own commands:

  if ($fbcmdCommand == 'FNAMES') {
    GetFlistIds("=all");
    foreach ($flistMatchArray as $friendId) {
      print ProfileName($friendId) . "\n";
    }
  }

  if ($fbcmdCommand == 'MYNOTES') {
    $fbReturn = $fbObject->api_client->notes_get($fbUser);
    foreach ($fbReturn as $note) {
      Print "{$note['title']}\n\n{$note['content']}\n\n\n";
    }
  }

  if ($fbcmdCommand == 'SINGLE' ) {
    $fql = "SELECT name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1={$fbUser}) AND relationship_status='single'";
    $fbReturn = $fbObject->api_client->fql_query($fql);
    foreach ($fbReturn as $singleFriend) {
      Print $singleFriend['name'] . "\n";
    }
  }

  if ($fbcmdCommand == 'FLSTREAM' ) {
     ValidateParamCount(1,2);
    SetDefaultParam(1,$fbcmdPrefs['default_finfo_flist']);
//    SetDefaultParam(2,$fbcmdPrefs['default_finfo_count']);
    GetFlistIds($fbcmdParams[1],true);
    $count = 1;
    if ( $fbcmdParams[2] != null ) {
      $count = $fbcmdParams[2];
    }
    $fql = "SELECT actor_id,post_id,comment_info,created_time,is_popular,likes,permalink,privacy,share_count,source_id,tagged_ids,type,updated_time,with_tags FROM stream WHERE source_id IN ({$fbcmdParams[1]}) LIMIT 1,{$count}";
//     print $fql."\n";
     try {
        $fbReturn = $fbObject->api_client->fql_query($fql);
        TraceReturn($fbReturn);
     } catch(Exception $e) {
        FbcmdException($e);
     }
     if (!empty($fbReturn)) {
       // k0z
        $date = getdate();
//       var_dump( $date );
        $year = $date['year'];
        $month = $date['month'];
        $day = $date['mday'];
        $hour = $date['hours'];
        $minutes = $date['minutes'];
        $seconds = $date['seconds'];
        $date_now = $year."-".$month."-".$day."_".$hour.":".$minutes.":".$seconds;
        $fileName = "output.json";

//        file_put_contents($fileName, "//QUERY: ".$fql."\n\n".json_encode( $fbReturn)."\n" );
        file_put_contents($fileName, '{"posts":'.json_encode( $fbReturn )."}\n" );
        file_put_contents("query.fql", '{"query":{"date":"'.$date_now.'","fql":"'.$fql.'"}}');
       print "(writing on $fileName)";
    } else {
      print "error for {$fbUser}";
    }
  }

////////////////////////////////////////////////////////////////////////////////

?>
