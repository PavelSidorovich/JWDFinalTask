package com.sidorovich.pavel.buber.core.controller;

public enum PagePaths {

    INDEX("/", "?command=main"),
    MAIN("/WEB-INF/jsp/main.jsp", "?command=main"),
    LOGIN("/WEB-INF/jsp/login.jsp", "?command=show_login"),
    REGISTER("/WEB-INF/jsp/register.jsp", "?command=show_user_register"),
    USER_CONTROL_PAGE("/WEB-INF/jsp/userControl.jsp", "?command=show_user_control"),
    DRIVER_REGISTER("/WEB-INF/jsp/driverRegister.jsp", "?command=show_driver_register"),
    DRIVER_APPLICATION_SUCCESS("/WEB-INF/jsp/sendingApplicationSuccess.jsp", "?command=successful_application"),
    DRIVER_APPLICATIONS("/WEB-INF/jsp/driverControl.jsp", "?command=driver_applications"),
    ERROR("/WEB-INF/jsp/error.jsp", "?command=show_error");

    private final String path;
    private final String command;

    PagePaths(String path, String command) {
        this.path = path;
        this.command = command;
    }

    public String getPath() {
        return path;
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
