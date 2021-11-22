package com.sidorovich.pavel.buber.core.controller;

public enum PagePaths {

    INDEX("/", "?command=main"),
    MAIN("/WEB-INF/jsp/main.jsp", "?command=main"),
    LOGIN("/WEB-INF/jsp/login.jsp", "?command=login"),
    ERROR("/WEB-INF/jsp/error.jsp", "?command=error");

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
