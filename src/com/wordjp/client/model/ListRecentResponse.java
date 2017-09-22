package com.wordjp.client.model;

public class ListRecentResponse {
  private String m_listCollectionResponse= null;
  
  public ListRecentResponse(String response)
  {
    m_listCollectionResponse = response;
  }
  
  public String getListRecentResponse()
  {
    return m_listCollectionResponse;
  }
}
