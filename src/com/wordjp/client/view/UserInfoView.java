package com.wordjp.client.view;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wordjp.client.model.JapDictUser;
import com.wordjp.client.util.Constant;

public class UserInfoView extends Composite{
  private VerticalPanel m_userLoginViewPanel = new VerticalPanel();
   
  private Label m_messageLabel  = new Label();
  private Button m_loginOutButton = new Button(Constant.Label_LogoutButton);
  
  
  
  public UserInfoView()
  {
    m_userLoginViewPanel.add(m_messageLabel);
    m_userLoginViewPanel.add(m_loginOutButton);    
        
        
    initWidget(m_userLoginViewPanel);
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();
  }
  
  private void addStyle()
  {
    m_loginOutButton.addStyleName("logoutButton");
  }
  
  private void viewCreated()
  {
    // No presenter of this view
  }
  
  
  public void updateUserInfo()
  {
    if( JapDictUser.getInstance().getUserId() < 0 )
    {
      m_messageLabel.setText("Error, Not Logged in");
    }
    else
    {
      m_messageLabel.setText("Logged in as " + JapDictUser.getInstance().getUserEmail());
    }    
  }
  
  public void setLogoutActionHandler(ClickHandler handler)
  {
    m_loginOutButton.addClickHandler(handler);
  }
}
