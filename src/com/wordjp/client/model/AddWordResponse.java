package com.wordjp.client.model;

public class AddWordResponse {
  private String m_addWordResponse= null;
  
  public AddWordResponse(String response)
  {
    m_addWordResponse = response;
  }
  
  public String getWordSearchResponse()
  {
    return m_addWordResponse;
  }

}
