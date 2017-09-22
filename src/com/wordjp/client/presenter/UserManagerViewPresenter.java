package com.wordjp.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.wordjp.client.model.JapDictUser;
import com.wordjp.client.model.LoginUserRequest;
import com.wordjp.client.model.LoginUserResponse;
import com.wordjp.client.model.RegisterUserRequest;
import com.wordjp.client.model.RegisterUserResponse;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;
import com.wordjp.client.view.UserInfoView;
import com.wordjp.client.view.UserLoginView;
import com.wordjp.client.view.UserManagerView;
import com.wordjp.client.view.UserRegisterView;

public class UserManagerViewPresenter implements EventBusHandler{
  
  private UserManagerView m_userManagerView = null;
  private UserRegisterView m_userRegisterView = null;
  private UserInfoView m_userInfoView = null;
  private UserLoginView m_userLoginView = null;
  
  private RegisterUserActionHandler m_registerUserActionHandler = new RegisterUserActionHandler();
  private LogoutActionHandler m_logoutActionHandler = new LogoutActionHandler();
  private LoginActionHandler m_loginActionHandler = new LoginActionHandler();
    

  public void onEvent(EventBusEventObj eventObj) 
  {
    if ( eventObj.getEventType().compareTo(Constant.EventType_RegisterUserResponse) == 0 )
    {
      handleUserRegisterResponse((RegisterUserResponse)eventObj.getEventData());      
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_LoginUserResponse) == 0 )
    {
      handleUserLoginResponse((LoginUserResponse)eventObj.getEventData());      
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_SystemStarted) == 0 )
    {
      handleSystemStarted();      
    }    
  }
    
  
  public UserManagerViewPresenter(UserManagerView userManagerView)
  {
    m_userManagerView = userManagerView;  
    m_userRegisterView = m_userManagerView.getUserRegisterView();
    m_userInfoView = m_userManagerView.getUserInfoView();
    m_userLoginView = m_userManagerView.getUserLoginView();
    
    m_userRegisterView.setRegisterActionHandler(m_registerUserActionHandler);
    m_userInfoView.setLogoutActionHandler(m_logoutActionHandler);    
    m_userLoginView.setLoginActionHandler(m_loginActionHandler);
    
    registerEventBusEvents();
  }

  private void registerEventBusEvents()
  {
    EventBus.getInstance().registerEventHandler(Constant.EventType_RegisterUserResponse, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_LoginUserResponse, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_SystemStarted, this);
  }
  
  class RegisterUserActionHandler implements ClickHandler {    
    public void onClick(ClickEvent event) {
      fireRegisterUserEvent();              
    }
    
    private void fireRegisterUserEvent()
    {
      if(m_userRegisterView.validateUserInput() == false )
      {
        return;
      }
            
      RegisterUserRequest registerUserRequest = new RegisterUserRequest( m_userRegisterView.getUserEmail(), m_userRegisterView.getUserPassword());
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_RegisterUserRequest, registerUserRequest);
      EventBus.getInstance().fireEvent(eventObj);         
    }
  }
  
  class LogoutActionHandler implements ClickHandler {    
    public void onClick(ClickEvent event) {
      JapDictUser.getInstance().logout();   
      m_userManagerView.switchViewTo(Constant.View_UserLogin);
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_UserLoggedOut, null);
      EventBus.getInstance().fireEvent(eventObj);      
    }      
  }
  
  class LoginActionHandler implements ClickHandler {    
    public void onClick(ClickEvent event) {
      fireLoginEvent();      
    }      
    
    private void fireLoginEvent()
    {      
      if(m_userLoginView.validateUserInput() == false )
      {
        return;
      }
      
      
      LoginUserRequest loginUserRequest = new LoginUserRequest(m_userLoginView.getUserEmail(), m_userLoginView.getUserPassword());
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_LoginUserRequest, loginUserRequest);
      EventBus.getInstance().fireEvent(eventObj);      
    }
  }
  
  private void handleUserRegisterResponse(RegisterUserResponse registerUserResponse)
  {
    int userId = registerUserResponse.getUserId();
    JapDictUser.getInstance().setUserId(userId);
    if(userId < 0 )
    {
      //Login failed
      m_userLoginView.setMessage("Registration failed: " + registerUserResponse.getReason());
    }
    else
    {      
      JapDictUser.getInstance().saveUserInfoToCookies();
      m_userManagerView.switchViewTo(Constant.View_UserInfo);      
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_UserLoggedIn, null);
      EventBus.getInstance().fireEvent(eventObj);    
    }      
  }
  
  private void handleUserLoginResponse(LoginUserResponse loginUserResponse)
  {
    int userId = loginUserResponse.getUserId();
    JapDictUser.getInstance().setUserId(userId);
    if(userId < 0 )
    {
      //Login failed
      m_userLoginView.setMessage("Login failed: " + loginUserResponse.getReason());
    }
    else
    {      
      JapDictUser.getInstance().saveUserInfoToCookies();
      m_userManagerView.switchViewTo(Constant.View_UserInfo);      
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_UserLoggedIn, null);
      EventBus.getInstance().fireEvent(eventObj);    
    }    
  }

  private void handleSystemStarted()
  {  
    int userId = JapDictUser.getInstance().getUserId();
    
    if(userId < 0 )
    {      
      //m_userManagerView.switchViewTo(Constant.View_UserRegister);      
      m_userManagerView.switchViewTo(Constant.View_UserLogin);
    }
    else
    {      
      m_userManagerView.switchViewTo(Constant.View_UserInfo);      
    }    
  }
  
}
