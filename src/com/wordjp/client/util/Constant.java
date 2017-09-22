package com.wordjp.client.util;

import com.google.gwt.core.client.GWT;

public class Constant {
  
  
  final public static String Html_RootContainer                = "japDictManDivContainer";  
  
  final public static String Label_RootViewTitle               = "Japanese Word Collection Manager";    
  final public static String Label_SearchWordButton            = "Search";
  final public static String Label_TabSearchWord               = "Search";
  final public static String Label_TabUserManager              = "Account";
  final public static String Label_TabHelp                     = "?";
  final public static String Label_TabListCollection           = "Collection";
  final public static String Label_TabListRecent               = "Recent";
  final public static String Label_InputWord                   = "Input Japanese Word";
  final public static String Label_WaitForResult               = "Searching...Wait...";
  final public static String Label_Email                       = "Email:";
  final public static String Label_Password                    = "Password:";
  final public static String Label_PasswordConfirm             = "Confirm:";
  final public static String Label_RegisterButton              = "Register";    
  final public static String Label_LoginButton                 = "Login";
  final public static String Label_LogoutButton                = "Log Out";
  final public static String Label_PreviousButton              = " < ";
  final public static String Label_NextButton                  = " > ";
  final public static String Label_PreviousFastButton          = " << ";
  final public static String Label_NextFastButton              = " >> ";
  final public static String Label_AddWordButton               = "Add To Collection";
  final public static String Label_DeleteWordButton            = "Remove From Collection";
  final public static String Label_Credits                     = "This application uses the JMdict dictionary files, property of the Electronic Dictionary Research.";
  final public static String Label_Instruction                 = "Get registered from User Tab, this application will help you to manage the collection of the Japanese words you learned.";
  
  final public static String View_UserLogin                    = "View_UserLogin";
  final public static String View_UserInfo                     = "View_UserInfo";
  final public static String View_UserRegister                 = "View_UserRegister";
  
  final public static String Cookie_Email                      = "Cookie_Email";
  final public static String Cookie_Password                   = "Cookie_Password";
  final public static String Cookie_UserId                     = "Cookie_UserId";
  
  final public static String EventType_RegisterUserRequest    = "EventType_RegisterUserRequest";
  final public static String EventType_RegisterUserResponse   = "EventType_RegisterUserResponse";
  final public static String EventType_LoginUserRequest       = "EventType_LoginUserRequest";
  final public static String EventType_LoginUserResponse      = "EventType_LoginUserResponse";  
  final public static String EventType_UserLoggedIn           = "EventType_UserLoggedIn";    
  final public static String EventType_UserLoggedOut          = "EventType_UserLoggedOut";  
  final public static String EventType_SearchWordRequest      = "EventType_SearchWordRequest";
  final public static String EventType_SearchWordResponse     = "EventType_SearchWordResponse";
  final public static String EventType_AddWordRequest         = "EventType_AddWordRequest";
  final public static String EventType_AddWordResponse        = "EventType_AddWordResponse";
  final public static String EventType_DeleteWordRequest      = "EventType_DeleteWordRequest";
  final public static String EventType_DeleteWordResponse     = "EventType_DeleteWordResponse";
  final public static String EventType_ListCollectionRequest  = "EventType_ListCollectionRequest";
  final public static String EventType_ListCollectionResponse = "EventType_ListCollectionResponse";
  final public static String EventType_ListRecentRequest      = "EventType_ListRecentRequest";
  final public static String EventType_ListRecentResponse     = "EventType_ListRecentResponse";
  final public static String EventType_SearchWordStart        = "EventType_SearchWordStart";
  final public static String EventType_SearchWordEnd          = "EventType_SearchWordEnd";
  final public static String EventType_ErrorHappened          = "EventType_ErrorHappened";
  final public static String EventType_SystemStarted          = "EventType_SystemStarted";
  
  final public static String Url_JapWordSearch                = GWT.getHostPageBaseURL() + "japWordSearch.php?word=";
  final public static String Url_UserManager                  = GWT.getHostPageBaseURL() + "japWordUser.php?action=";
  final public static String Url_WordManager                  = GWT.getHostPageBaseURL() + "japWordManage.php?action=";
  
  final public static int    Index_PageStride                 = 10;
  final public static int    Index_PageFastMove               = 5;
  
  
}
