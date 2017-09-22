<?php 

require_once('WordEntry.php');
require_once('WordSense.php');
require_once('japDicYahoo.php');

ini_set('memory_limit', '2000M');

$currentEntry = NULL;
//$file = "jmdict_e_small.xml";
$file = "jmdict_e.xml";
$currentElementName = "";
$insideDataFlag=false;
$fileExample = NULL;
$startFromSeq = "";
$canStartProcessing = 0;

set_time_limit(300);

//overwrite sql function
function real_escape_string($data)
{
  return $data;
}

function contents($parser, $data)
{
  global $currentElementName;
  global $currentEntry;
  global $insideDataFlag;
  global $fileExample;
    
  if(strcmp($currentElementName,"ent_seq") == 0)
  {
    if($insideDataFlag)
      $currentEntry->ent_seq .= real_escape_string($data);
    else
      $currentEntry->ent_seq = real_escape_string($data);
    
    //echo "<br>$currentEntry->ent_seq"; 
  }
  else if(strcmp($currentElementName,"keb") == 0)
  {    
    if($insideDataFlag)
    {
      $currentEntry->keb[$currentEntry->kebNum - 1] .= real_escape_string($data);  //if we are still in the same content, we move back
    }
    else
    {
      $currentEntry->keb[$currentEntry->kebNum] = real_escape_string($data);
      $currentEntry->kebNum ++;  //we move to the next firstly
    } 
  }
  else if(strcmp($currentElementName,"reb") == 0)
  {
    if($insideDataFlag)
    {
      $currentEntry->reb[$currentEntry->rebNum - 1] .= real_escape_string($data); 
    }
    else
    {
      $currentEntry->reb[$currentEntry->rebNum] = real_escape_string($data);
      $currentEntry->rebNum ++;
    }    
        
  }
  else if(strcmp($currentElementName,"gloss") == 0)
  {
    $index = $currentEntry->sense[$currentEntry->senseNum - 1]->glossNum;
      
    if($insideDataFlag)  
    {
      $currentEntry->sense[$currentEntry->senseNum - 1]->gloss[$index - 1] .= real_escape_string($data);
    }
    else
    {
      $currentEntry->sense[$currentEntry->senseNum - 1]->gloss[$index] = real_escape_string($data); 
      $currentEntry->sense[$currentEntry->senseNum - 1]->glossNum ++ ;   
    }  
         
  }
  else if(strcmp($currentElementName,"pos") == 0)
  {
    $index = $currentEntry->sense[$currentEntry->senseNum - 1]->posNum;
      
    if($insideDataFlag) 
    { 
      $currentEntry->sense[$currentEntry->senseNum - 1]->pos[$index - 1] .= real_escape_string($data);
    }
    else
    {  
      $currentEntry->sense[$currentEntry->senseNum - 1]->pos[$index] = real_escape_string($data);   
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
  global $fileExample;
  global $startFromSeq;
  
  
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
    if(strcmp($startFromSeq,"") == 0)
    {
      $fileExample = fopen ("japdict_example.xml","w");
      fwrite($fileExample, '<?xml version="1.0" encoding="UTF-8"?>');
      fwrite($fileExample, "\n");
      fwrite($fileExample, "<japdict>\n");
    }
    else
    {
      $fileExample = fopen ("japdict_example.xml","a");
    }    
  }
  
  $insideDataFlag = false;
}

function endElement($parser, $name) 
{
  global $currentEntry; 
  global $currentElementName;
  global $insideDataFlag;
  global $fileExample;
  global $startFromSeq;
  
  $name = strtolower($name);
  if(strcmp($name,"entry") == 0)
  {
    importCurrentEntry();
  }
  else if(strcmp($name,"jmdict") == 0)
  {
    //We always need to finish the ending
    //if(strcmp($startFromSeq,"") == 0)
    {  
      fwrite($fileExample, "\n");
      fwrite($fileExample, "</japdict>\n");
    }
  }
  
  $currentElementName = "";
  $insideDataFlag = false;
}

function printExampleFromYahoo($value_ent_seq, $wordToSearch)
{
  global $fileExample;
  global $currentEntry;
  
  echo "-------------------------$value_ent_seq  $wordToSearch ---------------------\n";
    
    
  $siteUrl="dic.yahoo.co.jp";
  $japDicYahoo = new JapDicYahooExample($siteUrl);
  $content = NULL;
  
  $wordToSearchURLEncoded = urlencode($wordToSearch);

  $wordURL = "/dsearch?enc=UTF-8&p=$wordToSearchURLEncoded&stype=1&dtype=3";  
  $wordExamples = $japDicYahoo->getWordExamples($wordURL);
   
  //Do not retrieve data too quickly, make Yahoo think I am hacker
  //sleep(1);
  
  if($wordExamples)
  {
    $num = sizeof($wordExamples);
    $index = 0;
    
    if($num < 1)
    {
      return 0;
    }
      
    
    {
      fwrite($fileExample, "<entry>\n");
      fwrite($fileExample, "<seq>$value_ent_seq</seq>\n");
      fwrite($fileExample, "<kreb>$wordToSearch</kreb>\n");
    }
    
    while($index < $num)
    {
      fwrite($fileExample, "<example>$wordExamples[$index]</example>\n");      
      $index ++ ;
    }
    
    
    {
      fwrite($fileExample, "</entry>\n");
    }
    
    return $num;
  }
  else
  {
  	return 0;
  }
}

function importCurrentEntry()
{
  global $currentEntry; 
  global $currentElementName;
  global $insideDataFlag;
  global $fileExample;
  global $startFromSeq;
  global $canStartProcessing;
  
  $value_ent_seq = $currentEntry->ent_seq;
  
  if($canStartProcessing == 0)
  {
    if(strcmp($startFromSeq, $value_ent_seq) == 0)
    {
      //Now we can start to process from the next item
      $canStartProcessing = 1;      
    }
    return;
  }
  
  $value_keb = "|";
  $numExample = 0;
  
  //We try to get some examples, if no example is get from keb[0], 
  //We try keb[1],keb[2]...
  //If still no example, we try reb[0], reb[1]...
  if(0 < $currentEntry->kebNum )
  {
  	$index = 0;
  	while($index < $currentEntry->kebNum)
  	{
  	  $wordToSearch = $currentEntry->keb[$index];
      $numExample = printExampleFromYahoo($value_ent_seq, $wordToSearch);    
      if($numExample > 0)
      {
      	echo "got example from keb $wordToSearch\n";
        break;	
      }      
      $index ++;
      echo "trying to get example from other keb\n";
    }
  }
  
  if( $numExample == 0 && 0 < $currentEntry->rebNum)
  {
  	$index = 0;
  	while($index < $currentEntry->rebNum)
  	{
  	  $wordToSearch = $currentEntry->reb[$index];
      $numExample = printExampleFromYahoo($value_ent_seq, $wordToSearch);
      if($numExample > 0)
      {
      	echo "got example from reb $wordToSearch\n";
        break;	
      }
      $index ++;
      echo "trying to get example from other reb\n";
    }
  }   
}

//get the start from command line
if($argc == 2)
{
  $startFromSeq = $argv[1];
  echo "Starting from $startFromSeq";
}
else
{
  $canStartProcessing = 1;
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
