package com.wordjp.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.wordjp.client.presenter.RootViewPresenter;
import com.wordjp.client.test.UtilLogOnPage;
import com.wordjp.client.util.Constant;

public class RootView {
  
  //private RootViewPresenter m_rootViewPresenter= null;
  
  private FlowPanel m_rootPanel = new FlowPanel();
  
  private Label m_titleLabel = new Label(Constant.Label_RootViewTitle);
  
  private TabPanel m_tabPanel = new TabPanel();
  private FlowPanel m_tabRecentPanel = new FlowPanel(); 
  private FlowPanel m_tabSearchPanel = new FlowPanel(); 
  private FlowPanel m_tabCollectionPanel = new FlowPanel();
  private UserManagerView m_tabUserManagerView = new UserManagerView();
  private HelpInfoView m_tabHelpView = new HelpInfoView(); 
  
  
  public RootView()
  {
    RootView1();
  }
  
  
  public void RootView1()
  {        
    RootPanel.get(Constant.Html_RootContainer).add(m_rootPanel);
    m_rootPanel.add(m_tabPanel);
    if(UtilLogOnPage.debug_flag)
    {
      m_rootPanel.add(UtilLogOnPage.getLogWidget());
      UtilLogOnPage.log("Log is output to here");
    }

    addStyle();
    addSubViews();
    
    viewCreated();
  }
  
  //seems not working
  public void RootView2()
  {
    
  }
  
  private void viewCreated()
  {
    //create the presenter  
    new RootViewPresenter(this);   
  }
  
  
  public void addSubViews()
  {
	  //String width = "100%";
    
    //tab0
    //*
    m_tabPanel.add(m_tabRecentPanel, Constant.Label_TabListRecent);
    WordEntryListView wordEntryListViewListRecent = new WordEntryListView(WordEntryListView.ListWordRecent);
    m_tabRecentPanel.add(wordEntryListViewListRecent.getWidget());
    wordEntryListViewListRecent.initLayout();
	
    //tab1
	  //*
    m_tabPanel.add(m_tabSearchPanel, Constant.Label_TabSearchWord);
    SearchWordView searchWordView = new SearchWordView();
    WordEntryListView wordEntryListViewSearch = new WordEntryListView(WordEntryListView.ListWordSearchResult);        
    m_tabSearchPanel.add(searchWordView);
    m_tabSearchPanel.add(wordEntryListViewSearch.getWidget()); 
    wordEntryListViewSearch.initLayout();
    //*/
	  
    //tab2
	  //*
    m_tabPanel.add(m_tabCollectionPanel, Constant.Label_TabListCollection);
    WordEntryListView wordEntryListViewListCollection = new WordEntryListView(WordEntryListView.ListWordCollection);
    m_tabCollectionPanel.add(wordEntryListViewListCollection.getWidget());
    wordEntryListViewListCollection.initLayout();
    //*/
	  
    //tab3
	  //*
    m_tabPanel.add(m_tabUserManagerView, Constant.Label_TabUserManager);
    //*/
    
    //tab4
    m_tabPanel.add(m_tabHelpView, Constant.Label_TabHelp);
    
    m_tabPanel.selectTab(0);
  }
  
  private void addStyle()
  {
    m_titleLabel.addStyleName("titleLabel");  
  }
  
}
