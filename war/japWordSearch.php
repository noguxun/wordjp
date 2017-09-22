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
  
  
  $word = trim($_GET['word']);
  if ($word) {
 
    $cur_encoding = mb_detect_encoding($word) ; 
    //echo $word . " encoding:" . $cur_encoding . " "; 

  }
  
  $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
  mysql_select_db('noguxun_jmdict') or die('Could not select database');
  
  // Performing SQL query
  ////$query="SELECT * FROM word_entry WHERE keb like '%" . "$word" . "%' or reb like '%" . "$word" . "%'"; //org
  
  $query="SELECT * FROM word_entry WHERE keb like '" . "%|$word|%" . "'" . " or reb like '" . "%|$word|%" . "'";
  $query1="SELECT * FROM word_entry WHERE (keb like '%" . "$word" . "%' or reb like '%" . "$word" . "%') and keb not like '" . "%|$word|%" . "'" . " and reb not like '" . "%|$word|%" . "' limit 0 , 20";
  $wordEntries = "";
  
  $result=mysql_query($query);
  $num=mysql_numrows($result);
    	
  $indexEntry=0;
  while ($indexEntry < $num) {
    $wordEntry = new WordEntry();
    
    $wordEntry->ent_seq = mysql_result($result,$indexEntry,"ent_seq");
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
  
  $result=mysql_query($query1);
  $num=mysql_numrows($result);
  
  $indexEntry=0;
  while ($indexEntry < $num) {
    $wordEntry = new WordEntry();
    
    $wordEntry->ent_seq = mysql_result($result,$indexEntry,"ent_seq");
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
  
?>