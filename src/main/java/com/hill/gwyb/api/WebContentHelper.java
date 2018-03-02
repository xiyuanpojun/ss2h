package com.hill.gwyb.api;

import java.util.Objects;

public class WebContentHelper {
    private static final WebContentHelper WEB_CONTENT_HELPER = new WebContentHelper();
    private static final String REAL_PATH;

    private WebContentHelper() {
    }

    static {
        REAL_PATH = Objects.requireNonNull(WEB_CONTENT_HELPER.getClass().getClassLoader().getResource("")).getPath().replace("/WEB-INF/classes/", "");
    }

    public static String getRealPath() {
        return REAL_PATH;
    }
}
