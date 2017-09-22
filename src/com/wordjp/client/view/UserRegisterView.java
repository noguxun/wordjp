package com.wordjp.client.view;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wordjp.client.util.Constant;

public class UserRegisterView extends Composite{
  
  private VerticalPanel m_userRegisterViewPanel = new VerticalPanel();
  
  private FlexTable m_containerTable = new FlexTable();
  private TextBox m_emailField = new TextBox();
  private TextBox m_passwordField = new PasswordTextBox();
  private TextBox m_passwordFieldConfirm = new PasswordTextBox();
  
  private Button m_registerButton = new Button(Constant.Label_RegisterButton);
  private Label m_messageLabel  = new Label();
  
  
  public UserRegisterView()
  {
    m_userRegisterViewPanel.add(m_emailField);
    m_userRegisterViewPanel.add(m_passwordField);
    m_userRegisterViewPanel.add(m_passwordFieldConfirm);
    
    m_containerTable.setText(0, 0, Constant.Label_Email);             m_containerTable.setWidget(0, 1, m_emailField);
    m_containerTable.setText(1, 0, Constant.Label_Password);          m_containerTable.setWidget(1, 1, m_passwordField);
    m_containerTable.setText(2, 0, Constant.Label_PasswordConfirm);   m_containerTable.setWidget(2, 1, m_passwordFieldConfirm);
    
    m_userRegisterViewPanel.add(m_containerTable);
    m_userRegisterViewPanel.add(m_registerButton);
    m_userRegisterViewPanel.add(m_messageLabel);
        
    initWidget(m_userRegisterViewPanel);
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();
  }
  
  private void addStyle()
  {
    m_registerButton.addStyleName("registerButton");
  }
  
  private void viewCreated()
  {
    // No presenter of this view
  }
  
  public boolean validateUserInput()
  {      
    boolean result = false;
    
    String userEmail = m_emailField.getText();
    String userPassword = m_passwordField.getText();
    String userPasswordConfirm = m_passwordFieldConfirm.getText();
    
    while(true)
    {
      if(userPassword.length() < 4)
      {
        setMessage("Password too short" + userPassword);
        break;
      }
      
      if(userPasswordConfirm.compareTo(userPassword) != 0)
      {
        setMessage("Password confirmation failed");
        break;
      }
      
      userEmail = userEmail.toLowerCase();
      if(!userEmail.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"))  //http://www.regular-expressions.info/email.html
      {
        setMessage("Invalid EMail " + userEmail);
        break;
      }
      
      result = true;
      break;
    }
            
    return result;
  }
  
  public String getUserEmail()
  {
    return m_emailField.getText();
  }
  
  public String getUserPassword()
  {
    return m_passwordField.getText();
  }
  
  public void setRegisterActionHandler(ClickHandler handler)
  {
    m_registerButton.addClickHandler(handler);
  }
  
  public void setMessage(String message)
  {
    m_messageLabel.setText(message);
  }
  
  public void clearAllEntry()
  {
    m_emailField.setText("");
    m_passwordField.setText("");
    m_messageLabel.setText("");
  }
  
}
