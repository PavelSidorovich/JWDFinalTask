package com.sidorovich.pavel.buber.core.controller;

public enum PagePaths {

    MAIN("/WEB-INF/jsp/main.jsp", "main"),
    LOGIN("/WEB-INF/jsp/login.jsp", "show_login"),
    REGISTER("/WEB-INF/jsp/register.jsp", "show_user_register"),
    USER_CONTROL("/WEB-INF/jsp/userControl.jsp", "show_user_control"),
    DRIVER_REGISTER("/WEB-INF/jsp/driverRegister.jsp", "show_driver_register"),
    DRIVER_APPLICATION_SUCCESS("/WEB-INF/jsp/sendingApplicationSuccess.jsp", "successful_application"),
    DRIVER_APPLICATIONS("/WEB-INF/jsp/driverControl.jsp", "driver_applications"),
    ISSUE_BONUSES("/WEB-INF/jsp/issueBonuses.jsp", "show_bonuses"),
    CLIENT_ORDER("/WEB-INF/jsp/userOrder.jsp", "make_order"),
    MY_BONUSES("/WEB-INF/jsp/myBonuses.jsp", "my_bonuses"),
    INCOMING_ORDER("/WEB-INF/jsp/driverOrder.jsp", "incoming_order"),
    CLIENT_WALLET("/WEB-INF/jsp/clientWallet.jsp", "my_wallet"),
    DRIVER_WALLET("/WEB-INF/jsp/driverWallet.jsp", "driver_wallet"),
    ACCOUNT_CONTROL("/WEB-INF/jsp/accountControl.jsp", "account_control"),
    PIE_CHART("/WEB-INF/jsp/pieChart.jsp", "show_pie_chart"),
    LINE_CHART("/WEB-INF/jsp/lineChart.jsp", "show_line_chart"),
    MY_TAXI("/WEB-INF/jsp/driverInfo.jsp", "my_taxi"),
    ERROR("/WEB-INF/jsp/error.jsp", "show_error");

    private static final String COMMAND = "?command=";

    private final String jspPath;
    private final String command;

    PagePaths(String jspPath, String pagePath) {
        this.jspPath = jspPath;
        this.command = COMMAND + pagePath;
    }

    public String getJspPath() {
        return jspPath;
    }

    public String getCommand() {
        return command;
    }

    public static PagePaths of(String name) {
        for (PagePaths page : values()) {
            if (page.name().equalsIgnoreCase(name)) {
                return page;
            }
        }
        return MAIN;
    }

}
