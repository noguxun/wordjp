package com.wordjp.client.model;

public class DeleteWordResponse {
  private String m_addWordResponse= null;
  
  public DeleteWordResponse(String response)
  {
    m_addWordResponse = response;
  }
  
  public String getWordSearchResponse()
  {
    return m_addWordResponse;
  }

}
