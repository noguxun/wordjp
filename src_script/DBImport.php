<?php 

require_once('WordEntry.php');
require_once('WordSense.php');

$currentEntry = NULL;
//$file = "jmdict_e_small.xml";
$file = "jmdict_e.xml";
$currentElementName = "";
$insideDataFlag=false;

set_time_limit(300);

function contents($parser, $data)
{
  global $currentElementName;
  global $currentEntry;
  global $insideDataFlag;
    
  if(strcmp($currentElementName,"ent_seq") == 0)
  {
    if($insideDataFlag)
      $currentEntry->ent_seq .= mysql_real_escape_string($data);
    else
      $currentEntry->ent_seq = mysql_real_escape_string($data);
    
    //echo "<br>$currentEntry->ent_seq"; 
  }
  else if(strcmp($currentElementName,"keb") == 0)
  {    
    if($insideDataFlag)
    {
      $currentEntry->keb[$currentEntry->kebNum - 1] .= mysql_real_escape_string($data);  //if we are still in the same content, we move back
    }
    else
    {
      $currentEntry->keb[$currentEntry->kebNum] = mysql_real_escape_string($data);
      $currentEntry->kebNum ++;  //we move to the next firstly
    } 
  }
  else if(strcmp($currentElementName,"reb") == 0)
  {
    if($insideDataFlag)
    {
      $currentEntry->reb[$currentEntry->rebNum - 1] .= mysql_real_escape_string($data); 
    }
    else
    {
      $currentEntry->reb[$currentEntry->rebNum] = mysql_real_escape_string($data);
      $currentEntry->rebNum ++;
    }    
        
  }
  else if(strcmp($currentElementName,"gloss") == 0)
  {
    $index = $currentEntry->sense[$currentEntry->senseNum - 1]->glossNum;
      
    if($insideDataFlag)  
    {
      $currentEntry->sense[$currentEntry->senseNum - 1]->gloss[$index - 1] .= mysql_real_escape_string($data);
    }
    else
    {
      $currentEntry->sense[$currentEntry->senseNum - 1]->gloss[$index] = mysql_real_escape_string($data); 
      $currentEntry->sense[$currentEntry->senseNum - 1]->glossNum ++ ;   
    }  
         
  }
  else if(strcmp($currentElementName,"pos") == 0)
  {
    $index = $currentEntry->sense[$currentEntry->senseNum - 1]->posNum;
      
    if($insideDataFlag) 
    { 
      $currentEntry->sense[$currentEntry->senseNum - 1]->pos[$index - 1] .= mysql_real_escape_string($data);
    }
    else
    {  
      $currentEntry->sense[$currentEntry->senseNum - 1]->pos[$index] = mysql_real_escape_string($data);   
      $currentEntry->sense[$currentEntry->senseNum - 1]->posNum ++ ;
    } 
            
  }
  
  $insideDataFlag = true;
} 

function startElement($parser, $name, $attrs) 
{
  global $currentEntry; 
  global $currentElementName;
  global $insideDataFlag;
  
  
  $currentElementName = strtolower($name);
  if(strcmp($currentElementName,"entry") == 0)
  {
    $currentEntry = new WordEntry();
    //echo "\ncreating new entry $currentEntry->rebNum \n" ;
  }  
  else if(strcmp($currentElementName,"sense") == 0)
  {
    
    $currentEntry->sense[$currentEntry->senseNum] = new WordSense();
    $currentEntry->sense[$currentEntry->senseNum]->ent_seq = $currentEntry->ent_seq;
    $currentEntry->senseNum++;
  }
  else if(strcmp($currentElementName,"jmdict") == 0)
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());   
    mysql_select_db('noguxun_jmdict') or die('Could not select database');  
    
    $insert_query = "delete from  word_entry";
    mysql_query($insert_query);
    $insert_query = "delete from  word_sense";
    mysql_query($insert_query);
    
    echo "<br>connectinng to DB... deleting old records and processing ...<br>\n";
  }
  
  $insideDataFlag = false;
}

function endElement($parser, $name) 
{
  global $currentEntry; 
  global $currentElementName;
  global $insideDataFlag;
  
  $name = strtolower($name);
  if(strcmp($name,"entry") == 0)
  {
    importCurrentEntry();
  }
  else if(strcmp($name,"jmdict") == 0)
  {
    //mysql_close();
    echo "<br>disconnectinng to DB<br>\n";
  }
  
  $currentElementName = "";
  $insideDataFlag = false;
}


function importCurrentEntry()
{
  global $currentEntry; 
  global $currentElementName;
  global $insideDataFlag;
  
  //echo "<br> importCurrentEntry";
  
  $value_ent_seq = $currentEntry->ent_seq;


  $index = 0;
  $value_keb = "|";
  while($index < $currentEntry->kebNum )
  {
    $tmp = $currentEntry->keb[$index];
    $value_keb = $value_keb . $tmp . '|' ;
    $index ++;
  }

  
  $index = 0;
  $value_reb = "|";
  while($index < $currentEntry->rebNum )
  {
    $tmp = $currentEntry->reb[$index];
    $value_reb = $value_reb . $tmp. '|' ;
    $index ++;
  }
  
  $insert_query = "INSERT INTO word_entry VALUES ('$value_ent_seq','$value_keb','$value_reb',' ')";
  $result = mysql_query($insert_query);
  if(!$result)
  {
    echo "query:  $insert_query";
    die('Invalid query: ' . mysql_error());
  }
  
  if(strlen($insert_query) < 7)
  {
    echo "<br> $insert_query <br>";
  }
  
  
    
  $index1 = 0;    
  while($index1 < $currentEntry->senseNum )
  {              
    $index2 = 0;  
    $value_gloss = "|";
    $glossNum = $currentEntry->sense[$index1]->glossNum;
    while($index2 < $glossNum )
    {
      $tmp = $currentEntry->sense[$index1]->gloss[$index2];
      $value_gloss = $value_gloss . $tmp. '|' ;
      $index2 ++;
    }
    
    $index2 = 0;    
    $value_pos = "|";
    $posNum = $currentEntry->sense[$index1]->posNum;
    while($index2 < $posNum )
    {
      $tmp = $currentEntry->sense[$index1]->pos[$index2];
      $value_pos = $value_pos . $tmp. '|' ;
      $index2 ++;
    }        
    
    $index1++;  
    
    $insert_query = "INSERT INTO word_sense VALUES ('$value_ent_seq','$value_pos','$value_gloss')";
    //echo "\n $insert_query <br>\n";
    $result = mysql_query($insert_query);
    if(!$result)
    {
      echo "query:  $insert_query";
      die('Invalid query: ' . mysql_error());
    }
  }
}





$xml_parser = xml_parser_create();
xml_set_element_handler($xml_parser, "startElement", "endElement");
xml_set_character_data_handler($xml_parser, "contents"); 
if (!($fp = fopen($file, "r"))) {
    die("could not open XML input");
}


while ($data = fread($fp, 4096)) {
    if (!xml_parse($xml_parser, $data, feof($fp))) {
        die(sprintf("XML error: %s at line %d",
                    xml_error_string(xml_get_error_code($xml_parser)),
                    xml_get_current_line_number($xml_parser)));
    }
}
xml_parser_free($xml_parser);

?>
