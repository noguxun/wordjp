package com.wordjp.client.model;

public class RegisterUserRequest {
  
  private String m_email;
  private String m_password;
  
  public RegisterUserRequest(String userEmail, String userPassword)
  {
    m_email = userEmail;
    m_password = userPassword;
  }
  
  public String getUserEmail()
  {
    return m_email;  
  }
  
  public String getUserPassword()
  {
    return m_password;  
  }
}
