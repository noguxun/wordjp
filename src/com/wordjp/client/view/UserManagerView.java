package com.wordjp.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wordjp.client.presenter.UserManagerViewPresenter;
import com.wordjp.client.util.Constant;

public class UserManagerView extends Composite{
  
  //private UserManagerViewPresenter m_userManagerViewPresenter = null;
  
  private UserRegisterView m_userRegisterView = new UserRegisterView();
  private UserInfoView m_userInfoView = new UserInfoView();
  private UserLoginView m_userLoginView = new UserLoginView();

  private VerticalPanel m_userManagerViewPanel = new VerticalPanel();
  

  
  public UserManagerView()
  {   
    m_userManagerViewPanel.add(m_userRegisterView);
        
    initWidget(m_userManagerViewPanel);      
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();
  }
  
  private void addStyle()
  {

  }
  
  private void viewCreated()
  {
    //m_userManagerViewPresenter = new UserManagerViewPresenter(this);
    new UserManagerViewPresenter(this);
  }
  
  public UserRegisterView getUserRegisterView()
  {    
    return m_userRegisterView;
  }
  
  public UserLoginView getUserLoginView()
  {
    return m_userLoginView;
  }
  
  public UserInfoView getUserInfoView()
  {
    return m_userInfoView;
  }
  
  public void switchViewTo(String viewName)
  {
    
    if(viewName.compareToIgnoreCase(Constant.View_UserInfo) == 0)
    {
      m_userManagerViewPanel.clear();
      m_userManagerViewPanel.add(m_userInfoView);      
      m_userInfoView.updateUserInfo();
    }
    else if(viewName.compareToIgnoreCase(Constant.View_UserRegister) == 0 || viewName.compareToIgnoreCase(Constant.View_UserLogin) == 0)
    {
      m_userManagerViewPanel.clear();
      m_userManagerViewPanel.add(m_userLoginView);
      m_userManagerViewPanel.add(m_userRegisterView);
      m_userRegisterView.clearAllEntry();
      m_userLoginView.clearAllEntry();
    }    
  }
}
