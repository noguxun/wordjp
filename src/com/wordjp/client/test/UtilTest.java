package com.wordjp.client.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;

public class UtilTest {
  
  class Handler1 implements EventBusHandler{
    public void onEvent(EventBusEventObj eventObj)
    {
      GWT.log("handler1 Called, event is " + eventObj.getEventType(), null);
    }
  }
  
  class Handler2 implements EventBusHandler{
    public void onEvent(EventBusEventObj eventObj)
    {
      GWT.log("handler2 Called, event is " + eventObj.getEventType(), null);
    }
  } 
  
  public void testEventBus()
  {
    Handler1 handler1Obj = new Handler1();
    Handler2 handler2Obj = new Handler2();    
    
    EventBus.getInstance().registerEventHandler("EventTest1", handler1Obj);
    EventBus.getInstance().registerEventHandler("EventTest1", handler2Obj);
    EventBus.getInstance().registerEventHandler("EventTest2", handler2Obj);
    EventBus.getInstance().registerEventHandler("EventTest3", handler2Obj);
    
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest1", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest3", null));
    
    GWT.log("unregistering EventTest1-----", null);
    EventBus.getInstance().unRegisterEventHandlerForOneEvent("EventTest1", handler2Obj);
    
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest1", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest3", null));
    
    GWT.log("unregistering EventTest1-----", null);
    EventBus.getInstance().unRegisterEventHandlerForAllEvent(handler2Obj);
    
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest1", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest2", null));
    EventBus.getInstance().fireEvent(new EventBusEventObj("EventTest3", null));
  }
  
  static public void testJsonP()
  {
    //String url = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=barack%20obama&callback=callback";
	  //String url2 = "http://www.google.com/calendar/feeds/developer-calendar@google.com/public/full?alt=json-in-script&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";	
    String url3 = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=barack%20obama";
	  JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
  	//jsonp.setCallbackParam("insertAgenda");
  	//*
    jsonp.requestObject(url3,
  	      new AsyncCallback<JavaScriptObject>() {
  	        public void onFailure(Throwable throwable) {
  	          GWT.log("got the JavaScriptObject");
  	          Window.alert("jsonp failed");
  	        }
  
  	        public void onSuccess(JavaScriptObject feed) {
  	          JSONObject obj = new JSONObject(feed);
  	          JSONValue val = obj.get("responseStatus");
  	          if(val != null)
  	          {
  	            	     
  	            JSONNumber num = val.isNumber();
  	            
  	            Window.alert("got the responseStatus " + num.doubleValue());  
  	          }
  	          else
  	          {
  	            Window.alert("not getting the responseData");
  	          }
  	        }
  	      });  
	  //*/
  	/*
  	jsonp.requestString(url3,
  		      new AsyncCallback<java.lang.String>() {
  		        public void onFailure(Throwable throwable) {
  		          Window.alert("got the JavaScriptObject");
  		          Window.alert("jsonp failed");
  		        }
  
  		        public void onSuccess(java.lang.String feed) {
  		          Window.alert("got the JavaScriptObject aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");	         
  		          Window.alert(feed);
  		        }
  		      });
      //*/
    }
}
