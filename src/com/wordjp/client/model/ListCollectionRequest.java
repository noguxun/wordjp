package com.wordjp.client.model;

public class ListCollectionRequest {
  
  private int m_userId;
  private int m_indexStart;
  private int m_indexEnd;
  
  public ListCollectionRequest( int userId , int indexStart, int indexEnd )
  {
    m_userId = userId;  
    m_indexStart = indexStart;
    m_indexEnd = indexEnd;
  }
  
  public int getUserId()
  {
    return m_userId;
  }
  
  public int getIndexStart()
  {
    return m_indexStart;
  }
  
  public int getIndexEnd()
  {
    return m_indexEnd;
  }
}
