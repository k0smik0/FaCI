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
  FbcmdIncludeAddCommand('PCOMMENTS','postid~Get last 150 comments from specified postid');

////////////////////////////////////////////////////////////////////////////////
// Step Five: Include (run) FBCMD

  require dirname(__FILE__).'/../fbcmd.php';
  

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

  // use: fbcmd FLSTREAM a_user_id
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
       $date_now = getDateNow();
       $fileNamePrefix = "posts_-_".$date_now;

//        file_put_contents($fileName, "//QUERY: ".$fql."\n\n".json_encode( $fbReturn)."\n" );
       file_put_contents($fileNamePrefix.".json", '{"posts":'.json_encode( $fbReturn )."}\n" );
       file_put_contents($fileNamePrefix.".fql", '{"query":{"date":"'.$date_now.'","fql":"'.$fql.'"}}');
       print " done.\n";
    } else {
      print "error for {$fbUser}";
    }
  }
  
  if ($fbcmdCommand == 'PCOMMENTS') {
     ValidateParamCount(1,2);
     #     SetDefaultParam(1,$fbReturn
     
     /*
     $url = "https://www.facebook.com//Tanzen80/posts/10152505358313440";
     $fqlComments1 = "select post_fbid, fromid, object_id, text, time from comment where object_id in (select comments_fbid from link_stat where url ='{$url}')";
     $fqlComments2 = 'select post_fbid, fromid, object_id, text, time from comment where object_id in (select post_fbid from #fqlComments1)';
     $fqlProfile = 'select name, id, url, pic_square from profile where id in (select fromid from #fqlComments1) or id in (select fromid from #fqlComments2)';
     
     MultiFQL(array('Comments1','Comments2','Profile'));
     if (!empty($dataStream)) {
        PrintPostHeader();
        PrintPostObject($url,$dataStream[0],$dataComments);
      }
     */
     
     $post_id = $fbcmdParams[1];
//      print "post_id ".$post_id;
//      $fql = 'SELECT fromid,likes,post_id,text_tags FROM comment WHERE post_id IN ("'.$post_id.'") LIMIT 1,150';
     $fql = 'SELECT fromid,time FROM comment WHERE object_id='.$post_id.' and parent_id=0';
//      print $fql;
     try {
	$fbReturn = $fbObject->api_client->fql_query($fql);
        TraceReturn($fbReturn);
     } catch(Exception $e) {
        FbcmdException($e);
     }
     if (!empty($fbReturn)) {
       $date_now = getDateNow();
       $fileNamePrefix = $post_id."_comments";
       
       file_put_contents($fileNamePrefix.'.json', '{"comments":{"post_id":"'.$post_id.'","comment_data":'.json_encode( $fbReturn ).'}}' );
       file_put_contents($fileNamePrefix.'.fql', '{"query":{"date":"'.$date_now.'","fql":"'.$fql.'"}}');
       print $post_id." ";
     } else {
//        print "error for {$post_id}";
         exit(1);
     }
  }

  // to improve with mutual likes
  if ($fbcmdCommand == 'FLIKES' ) {
    ValidateParamCount(1,2);
    $count = 1;
    if ( $fbcmdParams[2] != null ) {
      $count = $fbcmdParams[2];
    }
    $fql = "select name, page_id from page where page_id in ( SELECT page_id FROM page_fan WHERE uid IN ( {$fbcmdParams[1]} ) ) LIMIT 1,{$count}";
       
    try {
      $fbReturn = $fbObject->api_client->fql_query($fql);
      TraceReturn($fbReturn);
    } catch(Exception $e) {
      FbcmdException($e);
    }
    if (!empty($fbReturn)) {
       $date_now = getDateNow();

      $fileName = "output.json";

      file_put_contents($fileName, '{"likes":'.json_encode( $fbReturn )."}\n" );
      file_put_contents("query.fql", '{"query":{"date":"'.$date_now.'","fql":"'.$fql.'"}}');
      print "(writing on $fileName)";
    } else {
      print "error for {$fbUser}";
    }
  }

  function getDateNow() {
      $date = getdate();
      $year = $date['year'];
      $month = $date['mon'];
      $day = $date['mday'];
      $hour = $date['hours'];
      $minutes = $date['minutes'];
      $seconds = $date['seconds'];
      $date_now = $year."-".$month."-".$day."_".$hour.":".$minutes.":".$seconds;
      
      return $date_now;
  }
  
  ////////////////////////////////////////////////////////////////////////////////

  exit(0);
?>
