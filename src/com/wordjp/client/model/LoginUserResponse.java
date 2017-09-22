package com.wordjp.client.model;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class LoginUserResponse {
  private String m_loginUserResponse= null;
  
  private int m_userId = -1;
  private String m_reason = null;
  
  public LoginUserResponse(String response)
  {
    m_loginUserResponse = response;
    parseLoginUserResponse();
  }
  
  public String getWordSearchResponse()
  {
    return m_loginUserResponse;
  }
  
  private void parseLoginUserResponse()
  {           
    String jsonResponse = m_loginUserResponse;
    
    try{
      JSONObject tmpJsonObject;
      JSONValue tmpJsonValue; 
      JSONString tmpJsonString;
      JSONNumber tmpJsonNumber;
      
      JSONValue jsonValue = JSONParser.parseStrict(jsonResponse);
      tmpJsonObject = jsonValue.isObject();
      tmpJsonValue = tmpJsonObject.get("result");
      tmpJsonString = tmpJsonValue.isString();
      String result = tmpJsonString.stringValue();
      
      tmpJsonValue = tmpJsonObject.get("reason");
      tmpJsonString = tmpJsonValue.isString();
      String reason = tmpJsonString.stringValue();
      
      tmpJsonValue = tmpJsonObject.get("user_id");
      tmpJsonNumber = tmpJsonValue.isNumber();
      int userId = (int)tmpJsonNumber.doubleValue();
      
      if(result.equals("ok"))
      {
        m_userId = userId; 
        m_reason = reason;
      }
      else
      {
        m_userId = -1; 
        m_reason = reason; 
      }
      
    }catch(JSONException e){
      m_userId = -1; 
      m_reason = "Json Parsing Error";
    }        
  }
  
  public int getUserId()
  {
    return m_userId;  
  }
  
  public String getReason()
  {
    return m_reason;  
  }  
}
