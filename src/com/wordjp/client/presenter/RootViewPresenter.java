package com.wordjp.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;
import com.wordjp.client.view.RootView;

public class RootViewPresenter implements EventBusHandler{
  //private RootView m_rootView = null;
  
  public void onEvent(EventBusEventObj eventObj)
  {
    if ( eventObj.getEventType().compareTo(Constant.EventType_ErrorHappened) == 0 )
    {
      //Handling the search word request
      GWT.log("RootViewPresenter got event:" + Constant.EventType_ErrorHappened, null);
      Window.alert((String)eventObj.getEventData());
    } 
  }
  
  public RootViewPresenter(RootView rootView)
  {
    //m_rootView = rootView;
    registerEventBusEvents();
  }
  
  private void registerEventBusEvents()
  {
    EventBus.getInstance().registerEventHandler(Constant.EventType_ErrorHappened, this);    
  }
}
