<?php
  $agentStr = $_SERVER['HTTP_USER_AGENT'];
  
  if(stristr($agentStr,"iphone") != FALSE)
  {
    echo "iphone";
  }
  else
  {
    echo $agentStr;
  }
  
?>