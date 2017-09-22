package com.wordjp.client.view;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wordjp.client.presenter.SearchWordViewPresenter;
import com.wordjp.client.util.Constant;



public class SearchWordView extends Composite {
  
  //private SearchWordViewPresenter m_searchWordPresenter = null;
  
  private VerticalPanel m_searchWordPanel = new VerticalPanel();

  private HorizontalPanel m_controlPanel = new HorizontalPanel();
  private Button m_searchButton = new Button(Constant.Label_SearchWordButton);
  private TextBox m_wordField = new TextBox();
  private Label m_instructionLabel = new Label(Constant.Label_InputWord);
  private Label m_searchedWordLabel = new Label(); 
  
  public SearchWordView()
  {
    m_controlPanel.add(m_wordField);
    m_controlPanel.add(m_searchButton);        
    m_searchWordPanel.add(m_instructionLabel);
    m_searchWordPanel.add(m_controlPanel);
    m_searchWordPanel.add(m_searchedWordLabel);
    
    m_searchWordPanel.setWidth("100%");    
    
    
    initWidget(m_searchWordPanel);       
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();
  }
  
  private void addStyle()
  {
    m_searchButton.addStyleName("searchButton");
    
    m_instructionLabel.addStyleName("instructionLabel");
    m_wordField.addStyleName("wordField");
    m_searchWordPanel.addStyleName("searchWordPanel");
    m_searchedWordLabel.addStyleName("searchedWordLabel");
    m_controlPanel.addStyleName("searchWordControlPanel");
  }
  
  private void viewCreated()
  {
    //m_searchWordPresenter = new SearchWordViewPresenter(this);
    new SearchWordViewPresenter(this);
  }
  
  public HandlerRegistration setSearchActionHandlerOnButton(ClickHandler handler)
  {
    return m_searchButton.addClickHandler(handler);  
  }
  
  public HandlerRegistration setSearchActionHandlerOnWordField(KeyUpHandler handler)
  {
    return m_wordField.addKeyUpHandler(handler);
  }
  
  public String getSearchWord()
  {
    return m_wordField.getText().trim();
  }
  
  public void searchWordStarted()
  {
    m_instructionLabel.setText(Constant.Label_WaitForResult);
    m_searchedWordLabel.setText(getSearchWord());
    m_wordField.setText("");
    m_searchButton.setEnabled(false);
  }
  
  public void searchWordEnded()
  {
    m_instructionLabel.setText(Constant.Label_InputWord);
    //m_wordField.setEnabled(true);
    m_searchButton.setEnabled(true);
    m_wordField.selectAll();
    m_wordField.setFocus(true);    
  }
}
