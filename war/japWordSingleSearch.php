<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">



<html>

<?php


  //Data type definition
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
  
  //function definition
  function FormatSearchWordString($strSrc)
  {    
    $rebLen = strlen($strSrc); 
    $beginIndex = 0;
    $endIndex = 0;
    $tmpStr = "";

    while(TRUE)
    {
      $beginIndex = strpos($strSrc, '|', $endIndex);
      if($beginIndex === FALSE || $beginIndex +1 >= $rebLen)
      {
        break;
      }
            
      $endIndex = strpos($strSrc, '|', $beginIndex + 1);
      if($endIndex >= $rebLen || $endIndex == -1 )
      {
        break;
      }
      
      $strLength = 0;
      if($endIndex > $beginIndex + 1)
      {
        $strLength = $endIndex - $beginIndex - 1;
      }

      $tmpStr = $tmpStr."[ " . substr($strSrc, $beginIndex + 1, $strLength) . " ]  ";
    }  
    
    return $tmpStr;
  }
  
  function DisplayWordEntry($wordEntry)
  {
    $rebFormated = FormatSearchWordString($wordEntry->reb);
    $kebFormated = FormatSearchWordString($wordEntry->keb);
    $examplesFormated = FormatSearchWordString($wordEntry->examples);
    $glossFormatedAll = "";
    
    $senseCount = count($wordEntry->senses);
    $indexSense = 0;    
    while($indexSense < $senseCount)
    {
      $gloss = $wordEntry->senses[$indexSense]->gloss;
      $glossFormated = FormatSearchWordString($gloss);
      
      $glossFormatedAll = $glossFormatedAll . "$glossFormated";
      $indexSense ++;
    }

    
    //Header
    echo '<head>';
    echo '<meta http-equiv="Content-Type" content="text/html; charset=utf-8">';
    echo '<meta name="title" content="' . $rebFormated . $kebFormated .  '" /> ';
    echo '<meta name="description" content="' . $glossFormatedAll . $examplesFormated  . '" />'; 
    echo '</head>';
    
    //Body
    echo '<body>';
    echo '<div style="color:#001212">';
    echo '<p>';    
    echo "$rebFormated";
    echo '</p>';
    
    echo '<p>';
    echo "$kebFormated";
    echo '</p>';
    echo '</div>';
    
        
    echo '<div style="color:#000000">';
    echo '<p>';
    echo "$glossFormatedAll";
    echo '</p>';
    echo '</div>';
    
    echo '<div style="color:#008800">';
    echo '<p>';
    echo "$examplesFormated";
    echo '</p>';
    echo '</div>';
    
    
    echo '<a href="http://www.wordjp.com">Check more Japanese word ... </a>';
            
  }
  
  
  //Main Flow from here           
  date_default_timezone_set('UTC');      
  $ent_seq = trim($_GET['ent_seq']);
    
  $wordEntry = NULL;
  
  while(TRUE)
  {
	$link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
    
    $query1="SELECT ent_seq,keb,reb,examples FROM word_entry WHERE ent_seq = $ent_seq";
     
    $result=mysql_query($query1);
    if($result == FALSE)
    {
      break;
    }
    
    $num=mysql_numrows($result);
    	
  	if ($num >=1 ) {
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
	  
    } 
    
    mysql_close();	  
    break;    
  }
  
  if($wordEntry == NULL)
  {
    echo "No entry is found";
  }    
  else
  {
    //echo json_encode($wordEntry);
    DisplayWordEntry($wordEntry);
  }
  
  
      
?>


</body>
</html>
























