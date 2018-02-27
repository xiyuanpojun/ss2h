package com.hill.gwyb.service.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ThreadInsertGw implements Runnable {

    private List<List> li;
    private String zxName;
    private HttpServletRequest request;

    public ThreadInsertGw(List<List> li, String zxName, HttpServletRequest request) {
        this.li = li;
        this.zxName = zxName;
        this.request = request;
    }

    @Override
    public void run() {
        GwUserInsert wwi = new GwUserInsert(request);
        wwi.doInsert(li, zxName);
    }

}
