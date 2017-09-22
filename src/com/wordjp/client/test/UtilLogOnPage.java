package com.wordjp.client.test;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class UtilLogOnPage {
  final public static boolean debug_flag                       = false;
  
  private static TextArea logWidget = new TextArea();
  
  public static Widget getLogWidget()
  {
    logWidget.setWidth("100%");
    logWidget.setHeight("80%");
    return logWidget;
  }
  
  public static void log(String logText)
  {
    logWidget.setText(logWidget.getText() + "\n" + logText);
  }
}
