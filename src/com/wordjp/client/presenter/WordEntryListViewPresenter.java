package com.wordjp.client.presenter;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.wordjp.client.model.*;
import com.wordjp.client.util.Constant;
import com.wordjp.client.util.EventBus;
import com.wordjp.client.util.EventBusEventObj;
import com.wordjp.client.util.EventBusHandler;
import com.wordjp.client.view.WordEntryListView;

public class WordEntryListViewPresenter  implements EventBusHandler{
  
  private WordEntryListView m_wordEntryListView = null;
  private int m_indexStart = 0;
  private int m_indexEnd   = 0;
  
  public void onEvent(EventBusEventObj eventObj)
  {
    if ( eventObj.getEventType().compareTo(Constant.EventType_SearchWordResponse) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordSearchResult)
    {
      m_wordEntryListView.setWordSearchResponse(((WordSearchResponse)eventObj.getEventData()).getWordSearchResponse());  
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_AddWordResponse) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      //m_wordEntryListView.setAddWordResponse(((WordSearchResponse)eventObj.getEventData()).getWordSearchResponse());
      //TODO: to do some optimization here? When a word is added, the list is refreshed every time
      //Window.alert("WordEntryListViewPresenter on EventType_AddWordResponse");
      m_wordEntryListView.clearWordEntryList(); 
      fireListCollectionEvent();
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_DeleteWordResponse) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      //m_wordEntryListView.setAddWordResponse(((WordSearchResponse)eventObj.getEventData()).getWordSearchResponse());
      //TODO: to do some optimization here? When a word is added, the list is refreshed every time
      //Window.alert("WordEntryListViewPresenter on EventType_AddWordResponse");
      m_wordEntryListView.clearWordEntryList(); 
      fireListCollectionEvent();
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_ListCollectionResponse) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      //Window.alert(((ListCollectionResponse)eventObj.getEventData()).getListCollectionResponse());
      m_wordEntryListView.setWordSearchResponse(((ListCollectionResponse)eventObj.getEventData()).getListCollectionResponse());  
      m_wordEntryListView.setIndexInfo(m_indexStart, m_indexEnd);
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_ListRecentResponse) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordRecent )
    {
      //Window.alert("list recent data got ,type is " + m_wordEntryListView.getListType());
      m_wordEntryListView.setWordSearchResponse(((ListRecentResponse)eventObj.getEventData()).getListRecentResponse());  
      m_wordEntryListView.setIndexInfo(m_indexStart, m_indexEnd);
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_UserLoggedIn) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      //Window.alert("WordEntryListViewPresenter on logged in");
      m_wordEntryListView.clearWordEntryList(); 
      fireListCollectionEvent();
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_UserLoggedOut) == 0 && m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      //Window.alert("WordEntryListViewPresenter on logged out");
      m_wordEntryListView.clearWordEntryList(); 
    }
    else if ( eventObj.getEventType().compareTo(Constant.EventType_SystemStarted) == 0 )
    {
      handleSystemStarted();      
    }   
  }
  
  private void handleSystemStarted()
  { 
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      if( JapDictUser.getInstance().getUserId() > 0 )
      {
        fireListCollectionEvent();
      }
    }
    else if(m_wordEntryListView.getListType() == WordEntryListView.ListWordRecent )
    {
      fireListRecentEvent();
    }
  }
  
  public WordEntryListViewPresenter(WordEntryListView wordEntryListView)
  {
    m_wordEntryListView = wordEntryListView;
    
    registerEventBusEvents();
    
    //presenterCreated();
  }
  
 
  private void registerEventBusEvents()
  {
    EventBus.getInstance().registerEventHandler(Constant.EventType_SearchWordResponse, this);
    
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordSearchResult )
    {
      EventBus.getInstance().registerEventHandler(Constant.EventType_SearchWordResponse, this);
    }
    
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection )
    {
      EventBus.getInstance().registerEventHandler(Constant.EventType_ListCollectionResponse, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_UserLoggedIn, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_UserLoggedOut, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_AddWordResponse, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_DeleteWordResponse, this);
    }
    
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordRecent )
    {
      EventBus.getInstance().registerEventHandler(Constant.EventType_ListRecentResponse, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_UserLoggedIn, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_UserLoggedOut, this);
      EventBus.getInstance().registerEventHandler(Constant.EventType_AddWordResponse, this);
    }
    
    EventBus.getInstance().registerEventHandler(Constant.EventType_SystemStarted, this);
  }
  
  //private void presenterCreated()
  //{
    //if( m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection && JapDictUser.getInstance().getUserId() > 0)
    //{
    //  fireListCollectionEvent();
    //}
  //}
  
  public void fireListCollectionEvent()
  {
    fireListCollectionEvent(0, Constant.Index_PageStride - 1);
  }
  
  public void fireListRecentEvent()
  {
    fireListRecentEvent(0, Constant.Index_PageStride - 1);
  }
  
  public void fireListCollectionEvent(int indexStart, int indexEnd)
  {
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection && JapDictUser.getInstance().getUserId() > 0)
    {
      m_indexStart = indexStart;
      m_indexEnd   = indexEnd;
      
      ListCollectionRequest listCollectionRequest = new ListCollectionRequest(JapDictUser.getInstance().getUserId(), indexStart, indexEnd);
    
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_ListCollectionRequest, listCollectionRequest);
      EventBus.getInstance().fireEvent(eventObj);  
    }    
  }
  
  public void fireListRecentEvent(int indexStart, int indexEnd)
  {
    if( m_wordEntryListView.getListType() == WordEntryListView.ListWordRecent)
    {
      m_indexStart = indexStart;
      m_indexEnd   = indexEnd;
      
      ListRecentRequest listRecentRequest = new ListRecentRequest( indexStart, indexEnd);
    
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_ListRecentRequest, listRecentRequest);
      //Window.alert("fireListRecentEvent");
      EventBus.getInstance().fireEvent(eventObj);  
    }    
  }
  
  public void onAddWordButtonCreated(Button addWordButton, String wordEntSeq)
  {
    AddWordHandler handler = new AddWordHandler(JapDictUser.getInstance().getUserId(), wordEntSeq);
    addWordButton.addClickHandler(handler);
  }
  
  public void onDeleteWordButtonCreated(Button deleteWordButton, String wordEntSeq)
  {
    DeleteWordHandler handler = new DeleteWordHandler(JapDictUser.getInstance().getUserId(), wordEntSeq);
    deleteWordButton.addClickHandler(handler);
  }
  
  class DeleteWordHandler implements ClickHandler {   
    private int m_userId = 0;
    private String m_wordEntSeq = null;
    
    public DeleteWordHandler(int userId, String wordEntSeq)
    {
      this.m_userId = userId;
      this.m_wordEntSeq = wordEntSeq;
    }
    
    public void onClick(ClickEvent event) {
      //Window.alert("onClick");
      fireDeleteWordEvent();      
      ((Button)event.getSource()).setEnabled(false);
    }      
    
    private void fireDeleteWordEvent()
    {      
      DeleteWordRequest deleteWordRequest = new DeleteWordRequest(m_userId, m_wordEntSeq);
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_DeleteWordRequest, deleteWordRequest);
      EventBus.getInstance().fireEvent(eventObj);       
    }
  }
  
  class AddWordHandler implements ClickHandler {   
    private int m_userId = 0;
    private String m_wordEntSeq = null;
    
    public AddWordHandler(int userId, String wordEntSeq)
    {
      this.m_userId = userId;
      this.m_wordEntSeq = wordEntSeq;
    }
    
    public void onClick(ClickEvent event) {
      fireAddWordEvent();      
      ((Button)event.getSource()).setEnabled(false);
    }      
    
    private void fireAddWordEvent()
    {      
      AddWordRequest addWordRequest = new AddWordRequest(m_userId, m_wordEntSeq);
      
      EventBusEventObj eventObj = new EventBusEventObj(Constant.EventType_AddWordRequest, addWordRequest);
      EventBus.getInstance().fireEvent(eventObj);       
    }
  }
  
  public void onPageControlButtonCreated(Button pagePreviousButton, Button pageNextButton, Button pagePreviousFastButton, Button pageNextFastButton)
  {
    PageNextHandler pageNextHandler = new PageNextHandler(this);
    PagePreviousHandler pagePreviousHandler = new PagePreviousHandler(this);
    PageNextFastHandler pageNextFastHandler = new PageNextFastHandler(this);
    PagePreviousFastHandler pagePreviousFastHandler = new PagePreviousFastHandler(this);
    
    pagePreviousButton.addClickHandler(pagePreviousHandler);
    pageNextButton.addClickHandler(pageNextHandler); 
    pagePreviousFastButton.addClickHandler(pagePreviousFastHandler);
    pageNextFastButton.addClickHandler(pageNextFastHandler);    
  }
  
  class PageNextHandler implements ClickHandler {   
    
    WordEntryListViewPresenter m_presenter = null;
    
    public PageNextHandler(WordEntryListViewPresenter presenter)
    {
      m_presenter = presenter;
    }
    
    public void onClick(ClickEvent event) {
      firePageNext();      
      //((Button)event.getSource()).setEnabled(false);
    }      
    
    
    private void firePageNext()
    {      
      m_indexStart += Constant.Index_PageStride;
      m_indexEnd += Constant.Index_PageStride;
      firePageEvent();
    }
  }
  
  
  
  class PagePreviousHandler implements ClickHandler {   
    
    WordEntryListViewPresenter m_presenter = null;
    
    public PagePreviousHandler(WordEntryListViewPresenter presenter)
    {
      m_presenter = presenter;
    }
    
    public void onClick(ClickEvent event) {
      firePagePrevious();      
      //((Button)event.getSource()).setEnabled(false);
    }      
    
    private void firePagePrevious()
    {      
      if( m_indexStart - Constant.Index_PageStride >= 0 )
      {
        m_indexStart -= Constant.Index_PageStride;
        m_indexEnd -= Constant.Index_PageStride;
        firePageEvent();
      }
    }
  }
  
  class PageNextFastHandler implements ClickHandler {   
    
    WordEntryListViewPresenter m_presenter = null;
    
    public PageNextFastHandler(WordEntryListViewPresenter presenter)
    {
      m_presenter = presenter;
    }
    
    public void onClick(ClickEvent event) {
      firePageNext();      
      //((Button)event.getSource()).setEnabled(false);
    }      
    
    
    private void firePageNext()
    {
      m_indexStart += Constant.Index_PageStride * Constant.Index_PageFastMove;
      m_indexEnd += Constant.Index_PageStride * Constant.Index_PageFastMove;
      firePageEvent();
    }
  }
  
  
  
  class PagePreviousFastHandler implements ClickHandler {   
    
    WordEntryListViewPresenter m_presenter = null;
    
    public PagePreviousFastHandler(WordEntryListViewPresenter presenter)
    {
      m_presenter = presenter;
    }
    
    public void onClick(ClickEvent event) {
      firePagePrevious();      
      //((Button)event.getSource()).setEnabled(false);
    }      
    
    private void firePagePrevious()
    { 
      m_indexStart -= Constant.Index_PageStride * Constant.Index_PageFastMove;
      m_indexEnd -= Constant.Index_PageStride * Constant.Index_PageFastMove;
      firePageEvent();
    }
  }
  
  void firePageEvent()
  {
    if(m_wordEntryListView.getListType() == WordEntryListView.ListWordCollection)
    {
      fireListCollectionEvent(m_indexStart, m_indexEnd);
    }
    else if(m_wordEntryListView.getListType() == WordEntryListView.ListWordRecent)
    {
      fireListRecentEvent(m_indexStart, m_indexEnd);
    }  
  }
  
}
