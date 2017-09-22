package com.wordjp.client.remote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.wordjp.client.model.*;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;

public class RemoteRequestHandler implements EventBusHandler{

  public RemoteRequestHandler()
  {
    registerEventBusEvents();
  }
  
  private void registerEventBusEvents()
  {
    EventBus.getInstance().registerEventHandler(Constant.EventType_ListCollectionRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_SearchWordRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_RegisterUserRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_LoginUserRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_AddWordRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_DeleteWordRequest, this);
    EventBus.getInstance().registerEventHandler(Constant.EventType_ListRecentRequest, this);
  }
  
  public void onEvent(EventBusEventObj eventObj)
  {
    if ( eventObj.getEventType().compareTo(Constant.EventType_SearchWordRequest) == 0 )
    {
      //Handling the search word request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_SearchWordRequest, null);
      searchWord(((WordSearchRequest)eventObj.getEventData()).getSearchWord());
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_RegisterUserRequest) == 0 )
    {
      //Handling the register user request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_RegisterUserRequest, null);
      registerUser(((RegisterUserRequest)eventObj.getEventData()).getUserEmail(), ((RegisterUserRequest)eventObj.getEventData()).getUserPassword());
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_LoginUserRequest) == 0 )
    {
      //Handling the login user request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_LoginUserRequest, null);
      loginUser(((LoginUserRequest)eventObj.getEventData()).getUserEmail(), ((LoginUserRequest)eventObj.getEventData()).getUserPassword());            
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_AddWordRequest) == 0 )
    {
      //Handling the Add word request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_AddWordRequest, null);
      addWord(((AddWordRequest)eventObj.getEventData()).getUserId(), ((AddWordRequest)eventObj.getEventData()).getWordEntSeq());            
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_DeleteWordRequest) == 0 )
    {
      //Handling the Add word request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_DeleteWordRequest, null);
      deleteWord(((DeleteWordRequest)eventObj.getEventData()).getUserId(), ((DeleteWordRequest)eventObj.getEventData()).getWordEntSeq());            
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_ListCollectionRequest) == 0 )
    {
      //Handling the list collection request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_ListCollectionRequest, null);
      listCollection(((ListCollectionRequest)eventObj.getEventData()).getUserId(), ((ListCollectionRequest)eventObj.getEventData()).getIndexStart(), ((ListCollectionRequest)eventObj.getEventData()).getIndexEnd());            
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_ListRecentRequest) == 0 )
    {
      //Handling the list collection request
      GWT.log("RemoteRequestHandler got event:" + Constant.EventType_ListCollectionRequest, null);
      listRecent(((ListRecentRequest)eventObj.getEventData()).getIndexStart(), ((ListRecentRequest)eventObj.getEventData()).getIndexEnd());            
    }
  }
  
  class SearchWordResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordEnd,null));  
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordEnd,null));
      
      if (200 == wordResponse.getStatusCode()) {
        WordSearchResponse wordSearchResponse= new WordSearchResponse(wordResponse.getText());          
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordResponse,wordSearchResponse));
      } else {
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  private void searchWord(String wordToSearch) {          
    //send event the we are starting to search ... 
    EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordStart,null));
    
    String url = Constant.Url_JapWordSearch;     
    url += wordToSearch;
    GWT.log("searchWord at url: " + url, null);    
    url = URL.encode(url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

    try {
      builder.sendRequest(null, new SearchWordResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Search Wording Requesting Error"));
    }          
  }
  
  class AddWordResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {  
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
      
      if (200 == wordResponse.getStatusCode()) {
        AddWordResponse addWordResponse= new AddWordResponse(wordResponse.getText());          
        //Window.alert("AddWordResponseHandler onResponseReceived");
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_AddWordResponse,addWordResponse));
      } else {
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  class DeleteWordResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {  
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
      
      if (200 == wordResponse.getStatusCode()) {
        DeleteWordResponse deleteWordResponse= new DeleteWordResponse(wordResponse.getText());          
        //Window.alert("AddWordResponseHandler onResponseReceived");
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_DeleteWordResponse,deleteWordResponse));
      } else {
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  private void addWord(int userId, String wordToSearch) {
    String url = Constant.Url_WordManager;     
    String userIdString = "" + userId;
    url += "register&user_id=" + userIdString + "&ent_seq=" + wordToSearch;
    GWT.log("Add word at url: " + url, null);    
    url = URL.encode(url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    
    try {
      builder.sendRequest(null, new AddWordResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Add Word Error"));
    }    
  }
  
  private void deleteWord(int userId, String wordToSearch) {
    String url = Constant.Url_WordManager;     
    String userIdString = "" + userId;
    url += "unregister&user_id=" + userIdString + "&ent_seq=" + wordToSearch;
    GWT.log("Delete word at url: " + url, null);    
    url = URL.encode(url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    
    try {
      builder.sendRequest(null, new AddWordResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Delete Word Error"));
    }    
  }
  
  class ListConnectionResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
     
      if (200 == wordResponse.getStatusCode()) {
        //Window.alert(wordResponse.getText());
        ListCollectionResponse listCollectionResponse= new ListCollectionResponse(wordResponse.getText());          
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ListCollectionResponse,listCollectionResponse));
      } else {
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  class ListRecentResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
      if (200 == wordResponse.getStatusCode()) {
        //Window.alert("listCollection response ok" );
        ListRecentResponse listCollectionResponse= new ListRecentResponse(wordResponse.getText());          
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ListRecentResponse,listCollectionResponse));
      } else {
        //Window.alert("listCollection response failed");
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  private void listCollection(int userId, int indexStart, int indexEnd) {
    String url = Constant.Url_WordManager;     
    String userIdString = "" + userId;
    url += "list&user_id=" + userIdString + "&index_start=" + indexStart + "&index_end=" + indexEnd;
    GWT.log("Add word at url: " + url, null);    
    url = URL.encode(url);
    //Window.alert("listCollection called " + url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    try {
      builder.sendRequest(null, new ListConnectionResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "List Collection Error"));
    }    
  }
  
  private void listRecent(int indexStart, int indexEnd) {
    String url = Constant.Url_WordManager;
    url += "list_recent&index_start=" + indexStart + "&index_end=" + indexEnd;
    GWT.log("Add word at url: " + url, null);    
    url = URL.encode(url);
    //Window.alert("listCollection called " + url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    try {
      builder.sendRequest(null, new ListRecentResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "List Recent Collection Error"));
    }    
  }
  
  class RegisterUserResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {
      //EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordEnd,null));  
      //EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {   
      
      if (200 == wordResponse.getStatusCode()) {
        RegisterUserResponse registerUserResponse = new RegisterUserResponse(wordResponse.getText());          
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_RegisterUserResponse, registerUserResponse));
      } else {
        
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  private void registerUser(String userEmail, String userPassword) {            
    String url = Constant.Url_UserManager;     
    url += "register&useremail=" + userEmail + "&userpassword=" + userPassword;
    GWT.log("Register User at url: " + url, null);    
    url = URL.encode(url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    JapDictUser.getInstance().setEmail(userEmail);
    JapDictUser.getInstance().setPassword(userPassword);

    try {
      builder.sendRequest(null, new RegisterUserResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Register User Requesting Error"));
    }       
  }
  
  class LoginUserResponseHandler implements RequestCallback {
    public void onError(Request wordRequest, Throwable exception) {
      //EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordEnd,null));  
      //EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }

    public void onResponseReceived(Request wordRequest, Response wordResponse) {
      //EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_SearchWordEnd,null));
      
      if (200 == wordResponse.getStatusCode()) {
        LoginUserResponse loginUserResponse = new LoginUserResponse(wordResponse.getText());          
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_LoginUserResponse, loginUserResponse));
        
      } else {
        EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Server Response not 200"));
      }
    }
  }
  
  private void loginUser(String userEmail, String userPassword) {          
    String url = Constant.Url_UserManager;     
    url += "login&useremail=" + userEmail + "&userpassword=" + userPassword;
    GWT.log("Login User at url: " + url, null);    
    url = URL.encode(url);
    
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    JapDictUser.getInstance().setEmail(userEmail);
    JapDictUser.getInstance().setPassword(userPassword);
    
    try {
      builder.sendRequest(null, new LoginUserResponseHandler());
    } catch (RequestException e) {
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "Register User Requesting Error"));
    }       
  }
  
}
