<?php

require_once('japHttpClient.class.php');

class JapDicYahooExample{

  private $httpClientObj = NULL;


  function JapDicYahooExample($siteUrl)
  {
    $this->httpClientObj = new HttpClient($siteUrl);
  }
  

  
  public function getWordExamples( $siteUrl )
  {
    $content = NULL;
    
    if (!$this->httpClientObj->get($siteUrl)) {
      return NULL;
    }
    else{
      
      $content = $this->httpClientObj->getContent();
    }
    
    return $this->parseExamples($content);
  }
  
  protected function parseExamples($pageContent)
  {
    //The start pattern is <font color="#008800"><b>
    //the end pattern is </b>
    $patternStr = '<font color="#008800"><b>';
    $patternLen = strlen($patternStr); 
    $patternTransStr = '<font color="#666666">';
    $patternTransLen = strlen($patternStr); 
    
    $index = 0;
    $linkArry = NULL;
    
    //restrict the search content to only related part
    $detailPatternStart = "<!--詳細-->";
    $detailPatternEnd = "<!--/詳細-->";
    $posStart = strpos($pageContent, $detailPatternStart);
    $posEnd = strpos($pageContent, $detailPatternEnd);
    $pageContent = substr($pageContent,$posStart,$posEnd - $posStart); 
    
    
    while(1)
    {
    	//Firstly find the Translation
      $stringFound = strstr($pageContent, $patternStr);
      
      if($stringFound === FALSE){
        break;
      }
      
      $posLeftSymbol = strlen($patternStr);
      $stringFoundLen = strlen($stringFound);
      $strExample = "";
      $skipMode = 0;
      while( $posLeftSymbol < $stringFoundLen )
      {
        if( $stringFound[$posLeftSymbol] == '<')
        {
          if($stringFound[$posLeftSymbol+1] == '/' && $stringFound[$posLeftSymbol+2] == 'b') //the link ends with a " </b> "
          {
            break;
          }
          else 
          {
            $skipMode = 1;
          }
        }
        else if($stringFound[$posLeftSymbol] == '>')
        {
          $skipMode = 0;
        }
        else
        {
          if($skipMode == 0)
          {
            $strExample = $strExample . $stringFound[$posLeftSymbol];
          }
        }
        $posLeftSymbol ++;
      }
      
      $pageContent = substr($stringFound, strlen($strExample) + $patternLen);
      $linkArry[$index] = $strExample;      
      echo "Example: $strExample\n";      
      $index ++;
      
      //Secondly, find Example's English Translation
      $stringFound = strstr($pageContent, $patternTransStr);
      
      if($stringFound === FALSE){
        break;
      }
      
      $posLeftSymbol = strlen($patternTransStr);
      $stringFoundLen = strlen($stringFound);
      $strExample = "";
      $skipMode = 0;
      while( $posLeftSymbol < $stringFoundLen )
      {
        if( $stringFound[$posLeftSymbol] == '<')
        {
          if($stringFound[$posLeftSymbol+1] == '/' && $stringFound[$posLeftSymbol+2] == 'f' && $stringFound[$posLeftSymbol+3] == 'o') //the link ends with a " </font> "
          {
            break;
          }
          else 
          {
            $skipMode = 1;
          }
        }
        else if($stringFound[$posLeftSymbol] == '>')
        {
          $skipMode = 0;
        }
        else
        {
          if($skipMode == 0)
          {
            $strExample = $strExample . $stringFound[$posLeftSymbol];
          }
        }
        $posLeftSymbol ++;
      }
      
      $pageContent = substr($stringFound, strlen($strExample) + $patternLen);
      $linkArry[$index] = $strExample;      
      echo "Example English: $strExample\n";      
      $index ++;      
    }
    
    return $linkArry;
  }
}

?>
