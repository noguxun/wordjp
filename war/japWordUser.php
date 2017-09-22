<?php

  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');
  
  class RegisterUserResult
  {
    public $result="";
    public $reason="";
    public $user_id=0;   
  }
  
  class LoginUserResult
  {
    public $result="";
    public $reason="";
    public $user_id=0;   
  }
  
  $RESULT_ERROR = "error";
  $RESULT_OK = "ok";
  
  $action = trim($_GET['action']);
  
  if($action == 'register')
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
  
    $regRes = new RegisterUserResult();
    
    $email = trim($_GET['useremail']);
    $email = strtolower($email);
    
    $password = trim($_GET['userpassword']);
    
    $insert_query = "insert into user_info (email,password) values ('$email','$password')";
    $result_insert = mysql_query($insert_query);
    if($result_insert)
    {
      $regRes->result = $RESULT_OK;
      $regRes->reason = "successful";  
      //get user_id information
      $search_query = "select user_id from user_info where email='$email'";
      $result_query = mysql_query($search_query);
      if($result_query == FALSE)
      {
        $regRes->result = $RESULT_ERROR;
        $regRes->reason = mysql_error();  
      }
      else{
      	$num_reslut =mysql_numrows($result_query);
      	if($num_reslut != 1)
      	{
          $regRes->result = $RESULT_ERROR;
          $regRes->reason = "Get more than 1 user_id"; 
      	}
      	else
      	{
          $regRes->result = $RESULT_OK;
          $regRes->reason = "successful";
          $regRes->user_id = intval(mysql_result($result_query, 0 ,"user_id"));  
      	}
      }
	}
	else{
	  $regRes->result = $RESULT_ERROR;
      $regRes->reason = mysql_error();  
	}
	
	mysql_close();
    echo json_encode($regRes);
  }
  else if($action == 'login')
  {
    $link = mysql_connect('localhost', 'noguxun_noguxun', 'noguxun') or die('Could not connect: ' . mysql_error());
    mysql_select_db('noguxun_jmdict') or die('Could not select database');
  
    $regRes = new LoginUserResult();
    
    $email = trim($_GET['useremail']);
    $email = strtolower($email);
    
    $password = trim($_GET['userpassword']);
    
    $select_query = "select user_id from user_info where email='$email' and password='$password'";
           
    $result_select = mysql_query($select_query);
    if($result_select)
    {
      $regRes->result = $RESULT_OK;
      $regRes->reason = "successful";  
                  
      $num_reslut =mysql_numrows($result_select);
      if($num_reslut != 1)
      {
        $regRes->result = $RESULT_ERROR;
        $regRes->reason = "no match"; 
      }
      else
      {
        $regRes->result = $RESULT_OK;
        $regRes->reason = "successful";
        $regRes->user_id = intval(mysql_result($result_select, 0 ,"user_id"));  
      }      
	}
	else{
	  $regRes->result = $RESULT_ERROR;
      $regRes->reason = mysql_error();  
	}
	
	mysql_close();
    echo json_encode($regRes);  
  }

  
?>