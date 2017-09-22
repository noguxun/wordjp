<?php

  require_once('japDicYahoo.php');
  
  
  $siteUrl="dic.yahoo.co.jp";
  $japDicYahoo = new JapDicYahooExample($siteUrl);
  
  $content = NULL;

  /*
  $wordExamples = $japDicYahoo->getWordExamples("/dsearch?enc=UTF-8&p=%E3%81%84%E3%81%8D%E3%81%AA%E3%82%8A&stype=0&dtype=3");
  if($wordExamples)
  {
    $num = sizeof($wordExamples);
    $index = 0;
    while($index < $num)
    {
      echo "\n $wordExamples[$index] \n";
      $index ++ ;
    }
  }
  */
  


  $wordURL = "/dsearch?enc=UTF-8&p=%E3%81%9D%E3%81%A3%E3%81%A8&stype=1&dtype=3";  
  $wordExamples = $japDicYahoo->getWordExamples($wordURL);
  
  if($wordExamples)
  {
    $num = sizeof($wordExamples);
    $index = 0;
    
    if($num < 1)
      return;
      

    while($index < $num)
    {
      echo "<example>$wordExamples[$index]</example>\n";   
      $index ++ ;
    }
  }
  
?>
