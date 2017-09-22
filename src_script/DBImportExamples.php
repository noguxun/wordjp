<?php 

require_once('WordEntry.php');
require_once('WordSense.php');

$currentEntry = NULL;
$file = "japdict_example.xml";
$currentElementName = "";
$insideDataFlag=false;

set_time_limit(900);

function content_escape($data)
{
  return mysql_real_escape_string($data);
  //return $data;
}

function contents($parser, $data)
{
  global $currentElementName;
  global $currentEntry;
  global $insideDataFlag;
    
  if(strcmp($currentElementName,"seq") == 0)
  {
    if($insideDataFlag)
      $currentEntry->ent_seq .= content_escape($data);
    else
      $currentEntry->ent_seq = content_escape($data);
    
    //echo "<br>$currentEntry->ent_seq"; 
  }
  else if(strcmp($currentElementName,"kreb") == 0)
  {    
    if($insideDataFlag)
    {
      $currentEntry->keb[$currentEntry->kebNum - 1] .= content_escape($data);  //if we are still in the same content, we move back
    }
    else
    {
      $currentEntry->keb[$currentEntry->kebNum] = content_escape($data);
      $currentEntry->kebNum ++;  //we move to the next firstly
    } 
  }
  else if(strcmp($currentElementName,"example") == 0)  //we are using the REB entry to store the example
  {
    if($insideDataFlag)
    {
      $currentEntry->reb[$currentEntry->rebNum - 1] .= content_escape($data); 
    }
    else
    {
      $currentEntry->reb[$currentEntry->rebNum] = content_escape($data);
      $currentEntry->rebNum ++;
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
  else if(strcmp($currentElementName,"japdict") == 0)
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());   
    mysql_select_db('noguxun_jmdict') or die('Could not select database');  
    
    echo "<br>connectinng to DB... importing examples ...<br>\n";
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
  else if(strcmp($name,"japdict") == 0)
  {
    mysql_close();
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
  
  
  $update_query = "UPDATE word_entry SET examples='$value_reb' WHERE ent_seq='$value_ent_seq'";
  $result = mysql_query($update_query);
  if(!$result)
  {
    echo "query:  $insert_query";
    die('Invalid query: ' . mysql_error());
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
