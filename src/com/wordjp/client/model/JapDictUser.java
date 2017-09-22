package com.wordjp.client.model;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.wordjp.client.util.Constant;


public class JapDictUser { 
  
  static private JapDictUser m_userInstance = null;
  
  private String m_userEmail = "";
  private String m_userPassword = "";
  private int m_userId = -1;
  
  public static native String getUserAgent() /*-{
    var ua = navigator.userAgent.toLowerCase(); 
    
    if (ua.indexOf("iphone") != -1) {
      return "iphone" + ua;
    }
 
    return ua;
  }-*/;
  
  public static JapDictUser getInstance()
  {
    if(m_userInstance == null)
    {
      m_userInstance = new JapDictUser();   
    }
    
    return m_userInstance;
  }
  
  private JapDictUser()
  {
    
  }

  public void setEmail(String userEmailPara)
  {
    m_userEmail = userEmailPara;
    
  }
  
  public void setPassword( String userPasswordPara )
  {
    m_userPassword = userPasswordPara;
  }
    
  public String getUserEmail()
  {
    return m_userEmail;
  }
  
  public String getUserPassword()
  {
    return m_userPassword;
  }
    
  public void setUserId(int userId)
  {
    m_userId = userId;
  }
  
  public int getUserId()
  {
    return m_userId;
  }
  
  public void saveUserInfoToCookies()
  {
    Date now = new Date();
    long nowLong = now.getTime();
    nowLong = nowLong + (1000L * 60L * 60L * 24L * 7L * 54L);//seven days * 54 == a year
    now.setTime(nowLong);
   

    Cookies.setCookie(Constant.Cookie_UserId, Integer.toString(getUserId()), now);
    Cookies.setCookie(Constant.Cookie_Email, getUserEmail(), now);
    Cookies.setCookie(Constant.Cookie_Password, getUserPassword(), now);        
  }
  
  public void logout()
  {
    setUserId(-1);
    Cookies.removeCookie(Constant.Cookie_UserId);
    Cookies.removeCookie(Constant.Cookie_Email);
    Cookies.removeCookie(Constant.Cookie_Password);     
  }
  
  public void loadUserInfoFromCookies()
  { 
    int userId = -1;
    String userIdStr = Cookies.getCookie(Constant.Cookie_UserId);
    if(userIdStr != null)
    {
      userId = Integer.parseInt(userIdStr);      
    }
    else
    {
      return;
    }
      
    String userEmail = Cookies.getCookie(Constant.Cookie_Email);       
    String userPassword = Cookies.getCookie(Constant.Cookie_Password);
    
    if(userId != -1 && userEmail != null && userPassword != null)
    {
      setUserId(userId);
      setEmail(userEmail);      
      setPassword(userPassword);     
    }      
  }
}

