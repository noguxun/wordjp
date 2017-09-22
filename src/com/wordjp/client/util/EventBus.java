package com.wordjp.client.util;

import java.util.Vector;

import com.google.gwt.core.client.GWT;

public class EventBus {
  
  private static EventBus m_eventBusInstance = null;
  final static int MAPPING_VECTOR_INI_SIZE = 10;
  final static int MAPPING_VECTOR_INC_SIZE = 100;
  
  private Vector<EventHandlers> m_eventMapping = null; 
  
  class EventHandlers{
    
    final static int HANDLER_VECTOR_INI_SIZE = 10;
    final static int HANDLER_VECTOR_INC_SIZE = 100;
    
    private String m_eventType;
    private Vector<EventBusHandler> m_handlerVector;
    
    public EventHandlers(String eventType)
    {
      m_eventType = eventType;
      m_handlerVector = new Vector<EventBusHandler>(HANDLER_VECTOR_INI_SIZE, HANDLER_VECTOR_INC_SIZE);
    }
    
    public void addHandler(EventBusHandler eventHandler)
    {
      //We do not want duplicated handler
      m_handlerVector.remove(eventHandler);
      m_handlerVector.add(eventHandler);
    }
    
    public void removeHandler(EventBusHandler eventHandler)
    {      
      m_handlerVector.remove(eventHandler);  
    }
    
    public void dispatchEvent(EventBusEventObj eventObj)
    {
      int handlerNum = m_handlerVector.size();
      for(int index = 0; index < handlerNum; index ++)
      {
        m_handlerVector.get(index).onEvent(eventObj);
      }
    }
    
    public String getEventType()
    {
      return m_eventType;
    }
  }
  
    
  public static EventBus getInstance()
  {
    if(EventBus.m_eventBusInstance == null)
    {
      EventBus.m_eventBusInstance = new EventBus();
    }
    
    return EventBus.m_eventBusInstance;
  }
  
  private EventBus()
  {
    m_eventMapping = new Vector<EventHandlers>(MAPPING_VECTOR_INI_SIZE, MAPPING_VECTOR_INC_SIZE);  
  }
  
  public void registerEventHandler(String eventType, EventBusHandler eventHandler)
  {
    GWT.log("registerEventHandler" + eventType, null);
    int vectorNum = m_eventMapping.size();
    boolean foundFlag = false;
    for(int index = 0; index < vectorNum; index ++)
    {
      EventHandlers eventHandlers= m_eventMapping.get(index);
      if ( eventType.compareToIgnoreCase(eventHandlers.getEventType()) == 0 )
      {
        foundFlag = true;
        eventHandlers.addHandler(eventHandler);
        break;
      }
    } 
    
    if(foundFlag == false)
    {
      EventHandlers eventHandlers = new EventHandlers(eventType);
      eventHandlers.addHandler(eventHandler);
      m_eventMapping.add(eventHandlers);
      
    }
  }
  
  public void unRegisterEventHandlerForOneEvent(String eventType, EventBusHandler eventHandler)
  {
    int vectorNum = m_eventMapping.size();    
    for(int index = 0; index < vectorNum; index ++)
    {
      EventHandlers eventHandlers= m_eventMapping.get(index);
      if ( eventType.compareToIgnoreCase(eventHandlers.getEventType()) == 0 )
      {        
        eventHandlers.removeHandler(eventHandler);
        break;
      }
    } 
  }
  
  public void unRegisterEventHandlerForAllEvent(EventBusHandler eventHandler)
  {
    int vectorNum = m_eventMapping.size();    
    for(int index = 0; index < vectorNum; index ++)
    {
      EventHandlers eventHandlers= m_eventMapping.get(index);         
      eventHandlers.removeHandler(eventHandler);      
    }
  }
  
  public void unRegisterEventForAllHandler(String eventType)
  {
    int vectorNum = m_eventMapping.size();    
    for(int index = 0; index < vectorNum; index ++)
    {
      EventHandlers eventHandlers= m_eventMapping.get(index);
      if ( eventType.compareToIgnoreCase(eventHandlers.getEventType()) == 0 )
      {        
        m_eventMapping.remove(index);
        break;
      }
    }
  }
  
  
  public void fireEvent(EventBusEventObj eventObj)
  {
    int vectorNum = m_eventMapping.size();
    
    for(int index = 0; index < vectorNum; index ++)
    {
      EventHandlers eventHandlers= m_eventMapping.get(index);
      if ( eventObj.getEventType().compareToIgnoreCase(eventHandlers.getEventType()) == 0 )
      {        
        eventHandlers.dispatchEvent(eventObj);
        break;
      }
    }        
  }
 
}
