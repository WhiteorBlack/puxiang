package com.puxiang.mall.config;

/**
 * 事件总线 事件编码
 */
public interface Event {
    int RELOAD = 2000;
    int LOGOUT_RELOAD = 2001;
    int GO_HOME = 2002;
    int FINISH = 2003;
    int RELOAD_WEB = 2004;
    int KILL_POST = 2005;
    int RV_SCROLL = 2006;
    int WEB_BACK = 2007;
    int KILL_PLATE = 2008;
    int RELOAD_PLATE = 2009;
    int KILL_WEB = 2010;
    int UPDATE_VERSION = 2011;
    int RELOAD_COMMENT = 2012;
    int GO_MALL = 2013;
    int KILL_PERSONAL = 2014;
    int GO_PLATES = 2015;
    int KILL_INFO = 2016;
    int KILL_LOGIN = 2017;
    int KILL_LOGIN_DELAYED = 2018;
    int HID_WINDOW = 2019;
    int RELOAD_HEAD = 2020;
    int LOGOUT = 2021;
    int SING_OK = 2022;
    int LOGIN_REFRESH=2023;
    int DISABLE_SCROLL=2024;
    int ENABLE_SCROLL=2025;
}
