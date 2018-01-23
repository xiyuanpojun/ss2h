package com.hill.gwyb.api;

public class WebContentHelper {
    private static final WebContentHelper webContentHelper = new WebContentHelper();
    private static final String rootPath;

    private WebContentHelper() {
    }

    static {
        rootPath = webContentHelper.getClass().getClassLoader().getResource("").getPath().replace("/WEB-INF/classes/", "");
    }

    public static String getRootPath() {
        return rootPath;
    }
}
