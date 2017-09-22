package com.wordjp.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.wordjp.client.model.WordSearchRequest;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;
import com.wordjp.client.view.SearchWordView;

public class SearchWordViewPresenter  implements EventBusHandler{
  
  private SearchWordView m_searchWordView = null;
  
  SearchActionHandler m_handlerSearchAction = new SearchActionHandler();  
  HandlerRegistration m_handlerRegisterOnButton = null;
  HandlerRegistration m_handlerRegisterOnWordField = null;
  
  public void onEvent(EventBusEventObj eventObj)
  {
    if ( eventObj.getEventType().compareTo(Constant.EventType_SearchWordStart) == 0 )
    {      
      m_handlerRegisterOnWordField.removeHandler();
      m_searchWordView.searchWordStarted();  
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_SearchWordEnd) == 0 )
    {           
      m_handlerRegisterOnWordField = m_searchWordView.setSearchActionHandlerOnWordField(m_handlerSearchAction);
      m_searchWordView.searchWordEnded();
    }  
  }
  
  public SearchWordViewPresenter(SearchWordView searchWordView)
  {
    m_searchWordView = searchWordView;  
        
    m_handlerRegisterOnButton = m_searchWordView.setSearchActionHandlerOnButton(m_handlerSearchAction);
    m_handlerRegisterOnWordField = m_searchWordView.setSearchActionHandlerOnWordField(m_handlerSearchAction);
    
    registerEventBusEvents();
  }
  
  
  private void registerEventBusEvents()
  {
    EventBus.getInstance().registerEventHandler(Constant.EventType_SearchWordStart, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_SearchWordEnd, this);
  }
  
  
  class SearchActionHandler implements ClickHandler, KeyUpHandler {    
    public void onClick(ClickEvent event) {
      fireSearchWordEvent();
    }
    
    public void onKeyUp(KeyUpEvent event) {
      //It is very annoying behavior ... needs to find out enter key for input method or enter key for submit
      //if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      //  fireSearchWordEvent();            
      //}
      
    }
    
    private void fireSearchWordEvent()
    {
      String searchWord = m_searchWordView.getSearchWord();
      if(searchWord == null || searchWord.compareTo("") == 0)
      {
        return; //no action is needed
      }
      
      WordSearchRequest wordSearchRequest = new WordSearchRequest(searchWord);
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_SearchWordRequest, wordSearchRequest);
      EventBus.getInstance().fireEvent(eventObj);      
    }
  }
}
