package com.wordjp.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.wordjp.client.util.Constant;


public class HelpInfoView extends Composite {
  private FlowPanel m_helpInfoViewPanel = new FlowPanel();
  private Label m_instructionLabel = new Label(Constant.Label_Instruction);
  private Label m_creditsLabel = new Label(Constant.Label_Credits);
  
  public HelpInfoView()
  {    
    m_helpInfoViewPanel.add(m_instructionLabel);
    m_helpInfoViewPanel.add(m_creditsLabel);
    
    initWidget(m_helpInfoViewPanel);      
    addStyle();
    
    //should be the last action of the view creation
    viewCreated();
  }
  
  private void addStyle()
  {
    m_instructionLabel.addStyleName("instructionLabel");
    m_creditsLabel.addStyleName("creditsLabel");
  }
  
  private void viewCreated()
  {
    
  }

}
