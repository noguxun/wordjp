package com.wordjp.client.view;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.wordjp.client.util.Constant;

public class PageControlView extends Composite {
  
  static String PanelWidth = "300px";
  static String PanelHeight = "28px";
  
  //private WordEntryListViewPresenter m_wordEntryListViewPresenter = null;   
  //private HorizontalPanel m_pageControlPanel = new HorizontalPanel();
  private DockPanel m_pageControlPanel = new DockPanel();
  
  private Button m_buttonPrevious = new Button(Constant.Label_PreviousButton);
  private Button m_buttonNext = new Button(Constant.Label_NextButton);
  private Button m_buttonPreviousFast = new Button(Constant.Label_PreviousFastButton);
  private Button m_buttonNextFast = new Button(Constant.Label_NextFastButton);
  
  private Label m_labelIndex = new Label("1 - 10");
  
  
  public PageControlView()
  {
    wordEntryCreation();
  }

  
  private void wordEntryCreation()
  {
    m_pageControlPanel.setSize(PanelWidth, PanelHeight);
    
    m_labelIndex.addStyleName("indexLabel");
    m_pageControlPanel.add(m_labelIndex, DockPanel.CENTER);
    
    m_pageControlPanel.add(m_buttonPreviousFast,DockPanel.WEST);
    m_pageControlPanel.add(m_buttonPrevious,DockPanel.WEST);
  
    m_pageControlPanel.add(m_buttonNextFast,DockPanel.EAST);
    m_pageControlPanel.add(m_buttonNext, DockPanel.EAST);
    
    initWidget(m_pageControlPanel);
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();  
  }
  
  private void addStyle()
  {
    m_pageControlPanel.addStyleName("pageControlPanel");  
    
    m_buttonPrevious.addStyleName("navigateCollectionButton");
    m_buttonNext.addStyleName("navigateCollectionButton");
    m_buttonPreviousFast.addStyleName("navigateCollectionButton");
    m_buttonNextFast.addStyleName("navigateCollectionButton");
  }
    
  private void viewCreated()
  {
    
  }
  
  public Button getPreviousButton()
  {
    return m_buttonPrevious;
  }
  
  public Button getNextButton()
  {
    return m_buttonNext;
  }
  
  public Button getPreviousFastButton()
  {
    return m_buttonPreviousFast;
  }
  
  public Button getNextFastButton()
  {
    return m_buttonNextFast;
  }
  
  public void setIndexInfo(int startIndex, int endIndex)
  {
    String str = "    " + startIndex + " ---  " + endIndex + "    ";
    //String str = " " + startIndex + " - " + endIndex + " ";
    m_labelIndex.setText(str);
  }
}

/*
public class PageControlView extends Composite {
  
  static String PanelWidth = "300px";
  static String PanelHeight = "28px";
  
  //private WordEntryListViewPresenter m_wordEntryListViewPresenter = null;   
  //private HorizontalPanel m_pageControlPanel = new HorizontalPanel();
  private HorizontalPanel m_pageControlPanel = new HorizontalPanel();
  
  private Button m_buttonPrevious = new Button(Constant.Label_PreviousButton);
  private Button m_buttonNext = new Button(Constant.Label_NextButton);
  private Button m_buttonPreviousFast = new Button(Constant.Label_PreviousFastButton);
  private Button m_buttonNextFast = new Button(Constant.Label_NextFastButton);
  
  private Label m_labelIndex = new Label("1 - 10");
  
  
  public PageControlView()
  {
    wordEntryCreation();
  }

  
  private void wordEntryCreation()
  {
    m_buttonPreviousFast.addStyleName("buttonNavigation");
    m_buttonPrevious.addStyleName("buttonNavigation");
    m_pageControlPanel.add(m_buttonPreviousFast);
    m_pageControlPanel.add(m_buttonPrevious);
    
    m_labelIndex.addStyleName("indexLabel");
    m_pageControlPanel.add(m_labelIndex);
  
    m_buttonNext.addStyleName("buttonNavigation");
    m_buttonNextFast.addStyleName("buttonNavigation");
    m_pageControlPanel.add(m_buttonNext);
    m_pageControlPanel.add(m_buttonNextFast);

    initWidget(m_pageControlPanel);
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();  
  }
  
  private void addStyle()
  {
    m_pageControlPanel.addStyleName("pageControlPanel");      
  }
    
  private void viewCreated()
  {
    
  }
  
  public Button getPreviousButton()
  {
    return m_buttonPrevious;
  }
  
  public Button getNextButton()
  {
    return m_buttonNext;
  }
  
  public Button getPreviousFastButton()
  {
    return m_buttonPreviousFast;
  }
  
  public Button getNextFastButton()
  {
    return m_buttonNextFast;
  }
  
  public void setIndexInfo(int startIndex, int endIndex)
  {
    Integer start = new Integer(startIndex);
    Integer end = new Integer(endIndex);
    String str = "    " + startIndex + " ---  " + endIndex + "    ";
    //String str = " " + startIndex + " - " + endIndex + " ";
    m_labelIndex.setText(str);
  }
}


//Currently only designed for iphone resolution 
public class PageControlView extends Composite {
  
  static String PanelWidth = "300px";
  static String PanelHeight = "28px";
  
  //private WordEntryListViewPresenter m_wordEntryListViewPresenter = null;   
  //private HorizontalPanel m_pageControlPanel = new HorizontalPanel();
  private AbsolutePanel m_pageControlPanel = new AbsolutePanel();
  
  private Button m_buttonPrevious = new Button(Constant.Label_PreviousButton);
  private Button m_buttonNext = new Button(Constant.Label_NextButton);
  private Button m_buttonPreviousFast = new Button(Constant.Label_PreviousFastButton);
  private Button m_buttonNextFast = new Button(Constant.Label_NextFastButton);
  
  private Label m_labelIndex = new Label("1 - 10");
  
  
  public PageControlView()
  {
    wordEntryCreation();
  }

  
  private void wordEntryCreation()
  {
    
    m_pageControlPanel.setSize(PageControlView.PanelWidth, PageControlView.PanelHeight); 
    
    m_pageControlPanel.add(m_buttonPreviousFast, 10, 0);
    m_pageControlPanel.add(m_buttonPrevious, 80, 0);
    
    m_labelIndex.setSize("40px", "25px");
    m_labelIndex.addStyleName("indexLabel");
    m_pageControlPanel.add(m_labelIndex, 150, 0);
   
    
    m_pageControlPanel.add(m_buttonNext, 200, 0);
    m_pageControlPanel.add(m_buttonNextFast, 250, 0);

    initWidget(m_pageControlPanel);
    
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();  
  }
  
  private void addStyle()
  {
    m_pageControlPanel.addStyleName("pageControlPanel");      
  }
    
  private void viewCreated()
  {
    
  }
  
  public Button getPreviousButton()
  {
    return m_buttonPrevious;
  }
  
  public Button getNextButton()
  {
    return m_buttonNext;
  }
  
  public Button getPreviousFastButton()
  {
    return m_buttonPreviousFast;
  }
  
  public Button getNextFastButton()
  {
    return m_buttonNextFast;
  }
  
  public void setIndexInfo(int startIndex, int endIndex)
  {
    Integer start = new Integer(startIndex);
    Integer end = new Integer(endIndex);
    String str = " " + "1" + " - " + "2" + " ";
    //String str = " " + startIndex + " - " + endIndex + " ";
    m_labelIndex.setText(str);
  }
}
*/
