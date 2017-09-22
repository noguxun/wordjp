package com.wordjp.client.view;


import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.wordjp.client.model.JapDictUser;
import com.wordjp.client.presenter.WordEntryListViewPresenter;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;



public class WordEntryListView   {
  static public int ListWordSearchResult = 0;
  static public int ListWordCollection   = 1;
  static public int ListWordRecent       = 2;
  
  private WordEntryListViewPresenter m_wordEntryListViewPresenter = null;   
  
  private String m_wordSearchResponse= null;
  private int m_listType = WordEntryListView.ListWordSearchResult;
  
  private FlowPanel m_containerPanel = new FlowPanel();
  private FlowPanel m_entryListPanel = new FlowPanel();
  private PageControlView m_pageControlView = new PageControlView();
  
  private int m_entryNumber = 0;
  
  
  public WordEntryListView(int listType)
  {
    wordEntryCreation(listType);
  }

  public WordEntryListView()
  {    
    wordEntryCreation(WordEntryListView.ListWordSearchResult);
  }
  
  private void wordEntryCreation(int listType)
  {
    m_listType = listType;
  
    //initWidget(m_containerPanel);
    
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();  
  }
  
  public FlowPanel getWidget()
  {
    return m_containerPanel;
    //return m_entryListPanel;
  }
  
  public void initLayout()
  {
    m_containerPanel.add(m_entryListPanel);
    if( m_listType == WordEntryListView.ListWordCollection || m_listType == WordEntryListView.ListWordRecent )
    {
      //m_containerPanel.addSouth(m_pageControlView,30);
      m_containerPanel.add(m_pageControlView);
    }
  }
  
  private void addStyle()
  {
    m_entryListPanel.addStyleName("entryListPanel");  
    m_containerPanel.addStyleName("entryListContainerPanel");
  }
    
  private void viewCreated()
  {
    //Presentation creation
    m_wordEntryListViewPresenter = new WordEntryListViewPresenter(this);
    
    //Setting handlers for previous and next button
    if( m_listType == WordEntryListView.ListWordCollection || m_listType == WordEntryListView.ListWordRecent )
    {
      m_wordEntryListViewPresenter.onPageControlButtonCreated(m_pageControlView.getPreviousButton(), 
                                                              m_pageControlView.getNextButton(),
                                                              m_pageControlView.getPreviousFastButton(), 
                                                              m_pageControlView.getNextFastButton());
    }
  }
  
  public int getListType()
  {
    return m_listType;
  }
  
  public void clearWordEntryList()
  {
    m_entryNumber = 0;
    m_entryListPanel.clear();
  }
  
  public int getEntryNumber()
  {
    return m_entryNumber;
  }
  
  public void setWordSearchResponse(String wordSearchResponse)
  {
    m_wordSearchResponse = wordSearchResponse;
    
    clearWordEntryList();
    parseSearchWordResponse();
  }
  
  public void setAddWordResponse(String addWordResponse)
  {
    //currently we did not need to do anything 
  }
  
  private void parseSearchWordResponse()
  {
    try{
      JSONArray tmpJsonArray;
      
      // parse the response text into JSON
      JSONValue jsonValue = JSONParser.parseLenient(m_wordSearchResponse);
      tmpJsonArray = jsonValue.isArray();
      int entryNum = tmpJsonArray.size();
      for(int i=0; i<entryNum; i++)
      {
        JSONObject jsonObjectEntry = tmpJsonArray.get(i).isObject();  
        addEntry(jsonObjectEntry);
      }
    }catch(JSONException e){
      EventBus.getInstance().fireEvent(new EventBusEventObj(Constant.EventType_ErrorHappened, "JSON Parsing Error"));
    }
  }
  
  private String formatSearchWordString(String strSrc)
  {
    String tmpStr = "";
    int rebLen = strSrc.length(); 
    int beginIndex = 0;
    int endIndex = 0;
    
    while(true)
    {
      beginIndex = strSrc.indexOf("|",endIndex);
      if(beginIndex +1 >= rebLen || beginIndex == -1 )
      {
        break;
      }
      endIndex = strSrc.indexOf("|",beginIndex + 1);
      if(endIndex >= rebLen || endIndex == -1 )
      {
        break;
      }
      
      tmpStr = tmpStr + "[ " + strSrc.substring(beginIndex + 1, endIndex) + " ]  ";
    }  
    
    return tmpStr;
  }
  
  private void addExampleString(Panel entryPanel, String strSrc)
  {
    int rebLen = strSrc.length(); 
    int beginIndex = 0;
    int endIndex = 0;
    int exampleJap = 1;
    String tmpString = "";
    
    while(true)
    {
      beginIndex = strSrc.indexOf("|",endIndex);
      if(beginIndex +1 >= rebLen || beginIndex == -1 )
      {
        break;
      }
      endIndex = strSrc.indexOf("|",beginIndex + 1);
      if(endIndex >= rebLen || endIndex == -1 )
      {
        break;
      }
      
      tmpString = strSrc.substring(beginIndex + 1, endIndex);
      Label exampleLabel = new Label(tmpString);
      if(exampleJap == 1)
      {
        exampleLabel.addStyleName("exampleLabel");
        exampleJap = 0;
      }
      else
      {
        exampleLabel.addStyleName("exampleLabelEnglish");
        exampleJap = 1;
      }
      entryPanel.add(exampleLabel);
    }  
  }
  
  private final void addEntry(JSONObject jsonEntry)
  {
    JSONValue tmpJsonValue; 
    JSONString tmpJsonString;
    JSONArray tmpJsonArray;
    JSONObject tmpJsonObject;
    String tmpString;
    
    FlowPanel entryPanel = new FlowPanel(); 
    entryPanel.addStyleName("entryPanel");
    
    //FlowPanel containerPanel = new FlowPanel();
    tmpJsonValue = jsonEntry.get("reb");
    tmpJsonString = tmpJsonValue.isString();
    tmpString = tmpJsonString.stringValue();  
    tmpString = formatSearchWordString(tmpString);
    
    
    
    //adding the expand button
    //Button tmpButton = new Button();
    //tmpButton.setStylePrimaryName("expandButton");
    //entryPanel.add(tmpButton);
    
    Label rebLabel = new Label(tmpString);
    rebLabel.addStyleName("rebLabel");
    entryPanel.add(rebLabel);
    
    //Adding a clearer panel to remove the float property
    FlowPanel clearerPanel = new FlowPanel();
    clearerPanel.addStyleName("clearer");
    entryPanel.add(clearerPanel);
    
    tmpJsonValue = jsonEntry.get("keb");
    tmpJsonString = tmpJsonValue.isString();
    tmpString = tmpJsonString.stringValue();
    tmpString = formatSearchWordString(tmpString); 
    Label kebLabel = new Label(tmpString);
    kebLabel.addStyleName("kebLabel");
    entryPanel.add(kebLabel);
    
    tmpJsonValue = jsonEntry.get("senses");
    tmpJsonArray = tmpJsonValue.isArray();
    
    int senseNum = tmpJsonArray.size();
    for(int i=0; i<senseNum; i++)
    {
      tmpJsonObject = tmpJsonArray.get(i).isObject();
      
      //Currently we skip "pos"
      //tmpJsonValue = tmpJsonObject.get("pos");
      //tmpJsonString = tmpJsonValue.isString();
      //tmpString = tmpJsonString.stringValue();
      //Label posLabel = new Label(tmpString);
      //posLabel.addStyleName("posLabel");
      //entryPanel.add(posLabel);
      
      tmpJsonValue = tmpJsonObject.get("gloss");
      tmpJsonString = tmpJsonValue.isString();
      tmpString = tmpJsonString.stringValue(); 
      tmpString = formatSearchWordString(tmpString);
      Label glossLabel = new Label(tmpString);
      glossLabel.addStyleName("glossLabel");
      entryPanel.add(glossLabel);
    }
    
    tmpJsonValue = jsonEntry.get("examples");
    tmpJsonString = tmpJsonValue.isString();
    tmpString = tmpJsonString.stringValue();
    addExampleString(entryPanel, tmpString);
    //tmpString = formatSearchWordString(tmpString); 
    //Label exampleLabel = new Label(tmpString);
    //exampleLabel.addStyleName("exampleLabel");
    //entryPanel.add(exampleLabel);
    
    //Add the add to collection button
    String entSeq = null;
    tmpJsonValue = jsonEntry.get("ent_seq");
    tmpJsonString = tmpJsonValue.isString();
    tmpString = tmpJsonString.stringValue();
    entSeq = tmpString;
    if(JapDictUser.getInstance().getUserId() > 0 && (m_listType == WordEntryListView.ListWordSearchResult || m_listType == WordEntryListView.ListWordRecent ) )
    {
      Button tmpButton1 = new Button(Constant.Label_AddWordButton);
      tmpButton1.addStyleName("addToCollectionButton");
      
      m_wordEntryListViewPresenter.onAddWordButtonCreated(tmpButton1, entSeq); 
      entryPanel.add(tmpButton1);
    }
    else if(JapDictUser.getInstance().getUserId() > 0 && m_listType == WordEntryListView.ListWordCollection)
    {
      Button tmpButton1 = new Button(Constant.Label_DeleteWordButton);    
      tmpButton1.addStyleName("addToCollectionButton");
      m_wordEntryListViewPresenter.onDeleteWordButtonCreated(tmpButton1, entSeq); 
      entryPanel.add(tmpButton1);
    }
    
    //Adding the facebook share
    {    	
      //<a name="fb_share" type="button_count" share_url="www.wordjp.com?ent_seq=1111" href="http://www.facebook.com/sharer.php">Share</a><script src="http://static.ak.fbcdn.net/connect.php/js/FB.Share" type="text/javascript"></script>      
      String shareButtonCode = "<a href=\"http://www.facebook.com/sharer.php?u=http%3A%2F%2Fwww.wordjp.com%2FjapWordSingleSearch.php%3Fent_seq%3D"  + entSeq + "\" target=\"_blank\">Share to Facebook</a>";
      HTML shareLink = new HTML(shareButtonCode);
      shareLink.addStyleName("shareToFaceBookLink");
      
      entryPanel.add(shareLink);    	
    }   
    
    m_entryListPanel.add(entryPanel);
    m_entryNumber ++;
  }
  
  public void setIndexInfo(int startIndex, int endIndex)
  {
    this.m_pageControlView.setIndexInfo(startIndex, endIndex);
  }
}
