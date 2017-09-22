package com.wordjp.client.model;

public class AddWordRequest {
  private int m_userId = 0;
  private String m_wordEntSeq = null;

  public AddWordRequest(int userId, String wordEntSeq)
  {
    this.m_userId = userId;
    this.m_wordEntSeq = wordEntSeq;
  }
  
  public int getUserId()
  {
    return m_userId;
  }
  
  public String getWordEntSeq()
  {
    return m_wordEntSeq;
  }
}
