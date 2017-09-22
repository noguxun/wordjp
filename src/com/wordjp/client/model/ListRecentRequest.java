package com.wordjp.client.model;

public class ListRecentRequest {
  
  private int m_indexStart;
  private int m_indexEnd;
  
  public ListRecentRequest( int indexStart, int indexEnd )
  {
    m_indexStart = indexStart;
    m_indexEnd = indexEnd;
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
