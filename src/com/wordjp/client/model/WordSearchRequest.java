package com.wordjp.client.model;

public class WordSearchRequest {
  private String m_word;
  
  public WordSearchRequest(String word)
  {
    m_word = word;
  }
  
  public String getSearchWord()
  {
    return m_word;
  }
}
