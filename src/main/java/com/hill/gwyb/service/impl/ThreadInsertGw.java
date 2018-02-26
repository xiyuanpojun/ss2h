package com.hill.gwyb.service.impl;

import java.util.List;

public class ThreadInsertGw implements Runnable {

    private List<List> li;
    private String zxName;

    public ThreadInsertGw(List<List> li, String zxName) {
        this.li = li;
        this.zxName = zxName;
    }

    @Override
    public void run() {
        GwUserInsert wwi = new GwUserInsert();
        wwi.doInsert(li, zxName);
    }

}
