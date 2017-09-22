<?php

  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');
  
  class WordEntry
  {
    public $ent_seq="";
    public $keb=""; 
    public $reb=""; 
    public $examples="";
  }
  
  class WordSense
  {
    public $pos="";
    public $gloss="";  
  }
    
  class RegisterWordResult
  {
    public $result="";
    public $reason="";
  }
  
  class UnRegisterWordResult
  {
    public $result="";
    public $reason="";
  }
  
  date_default_timezone_set('UTC');  
  
  $RESULT_ERROR = "error";
  $RESULT_OK = "ok";
  
  $action = trim($_GET['action']);
  
  if($action == 'register')
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
  
    $regRes = new RegisterWordResult();
    
    $user_id = trim($_GET['user_id']); 
    $user_id = strtolower($user_id);
    
    $ent_seq = trim($_GET['ent_seq']);
    $time_added = date(DATE_ATOM);
    
    $insert_query = "insert into user_collection (user_id,ent_seq,time_added) values ($user_id,'$ent_seq','$time_added')";
    $result_insert = mysql_query($insert_query);
    if($result_insert)
    {
      $regRes->result = $RESULT_OK;
      $regRes->reason = "successful";  
	}
	else{
	  $update_query = "update user_collection set time_added='$time_added' where user_id=$user_id and ent_seq='$ent_seq'";
	  $result_query = mysql_query($update_query);
	  
	  if($result_query == FALSE)
	  {
	    $regRes->result = $RESULT_ERROR;
        $regRes->reason = mysql_error();
	  }
	  else
	  {
	    $regRes->result = $RESULT_OK;
        $regRes->reason = "successful";
	  } 
	}
	
	mysql_close();
    echo json_encode($regRes);
  }
  else if($action == 'unregister')
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
  
    $regRes = new UnRegisterWordResult();
    $user_id = trim($_GET['user_id']);
    $user_id = strtolower($user_id);
    
    $ent_seq = trim($_GET['ent_seq']);
    
    $delete_query = "delete from user_collection where user_id = $user_id and ent_seq = '$ent_seq'"; 
    
    $result_delete = mysql_query($delete_query);
    if($result_delete == FALSE)
    {
      $regRes->result = $RESULT_ERROR;
      $regRes->reason = mysql_error();
	}
	else{
	  $regRes->result = $RESULT_OK;
      $regRes->reason = "successful";   
	}
	
	mysql_close();
    echo json_encode($regRes);  
  }
  else if($action == 'list')
  {
	$link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
    
    $user_id = trim($_GET['user_id']);
    $user_id = strtolower($user_id);
    
    $index_start = trim($_GET['index_start']);
    $index_end = trim($_GET['index_end']);
    $index_duration = $index_end - $index_start + 1;
    
    
    $query1="SELECT user_collection.ent_seq,keb,reb,examples,user_collection.time_added FROM user_collection,word_entry WHERE user_id=$user_id AND user_collection.ent_seq = word_entry.ent_seq ORDER BY user_collection.time_added  DESC LIMIT $index_start ,$index_duration "; 
    $result=mysql_query($query1);
    $num=mysql_numrows($result);
    $wordEntries="";
  
  	$indexEntry=0;
  	while ($indexEntry < $num) {
      $wordEntry = new WordEntry();
      $wordEntry->ent_seq = mysql_result($result,$indexEntry,"user_collection.ent_seq");
      $wordEntry->keb = mysql_result($result,$indexEntry,"keb");
      $wordEntry->reb = mysql_result($result,$indexEntry,"reb");
      $wordEntry->examples = mysql_result($result,$indexEntry,"examples");
      
      $query2 = "select pos,gloss from word_sense where ent_seq=" . "$wordEntry->ent_seq";
      $result2=mysql_query($query2);
      $num2=mysql_numrows($result2);
    
      $indexSense=0;
      while($indexSense < $num2)
      {
        $wordSense = new WordSense();
        $wordSense->pos = mysql_result($result2,$indexSense,"pos");
        $wordSense->gloss = mysql_result($result2,$indexSense,"gloss");  
        $wordEntry->senses[] = $wordSense;
        $indexSense ++;
      }
	
	  $wordEntries[] = $wordEntry;
	  $indexEntry++;
    } 
    
    mysql_close();
	  
    echo json_encode($wordEntries);
  }
  else if($action == 'list_recent')
  {
	$link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
    
    $index_start = trim($_GET['index_start']);
    $index_end = trim($_GET['index_end']);
    $index_duration = $index_end - $index_start + 1;
    
    
    $query1="SELECT user_collection.ent_seq,keb,reb,examples,user_collection.time_added FROM user_collection,word_entry WHERE user_collection.ent_seq = word_entry.ent_seq ORDER BY user_collection.time_added  DESC LIMIT $index_start ,$index_duration "; 
    $result=mysql_query($query1);
    $num=mysql_numrows($result);
    $wordEntries="";
  
  	$indexEntry=0;
  	while ($indexEntry < $num) {
      $wordEntry = new WordEntry();
      $wordEntry->ent_seq = mysql_result($result,$indexEntry,"user_collection.ent_seq");
      $wordEntry->keb = mysql_result($result,$indexEntry,"keb");
      $wordEntry->reb = mysql_result($result,$indexEntry,"reb");
      $wordEntry->examples = mysql_result($result,$indexEntry,"examples");
      
      $query2 = "select pos,gloss from word_sense where ent_seq=" . "$wordEntry->ent_seq";
      $result2=mysql_query($query2);
      $num2=mysql_numrows($result2);
    
      $indexSense=0;
      while($indexSense < $num2)
      {
        $wordSense = new WordSense();
        $wordSense->pos = mysql_result($result2,$indexSense,"pos");
        $wordSense->gloss = mysql_result($result2,$indexSense,"gloss");  
        $wordEntry->senses[] = $wordSense;
        $indexSense ++;
      }
	
	  $wordEntries[] = $wordEntry;
	  $indexEntry++;
    } 
    
    mysql_close();
	  
    echo json_encode($wordEntries);  
  }

  
?>




























