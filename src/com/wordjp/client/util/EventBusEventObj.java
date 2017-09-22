package com.wordjp.client.util;

public class EventBusEventObj {
  private String m_eventType = null;
  private Object m_eventData = null;
  
  public EventBusEventObj(String eventType, Object eventData)
  {
    m_eventType = eventType;
    m_eventData = eventData;
  }

  public String getEventType()
  {
    return m_eventType;
  }
  
  public Object getEventData()
  {
    return m_eventData;
  }
}
