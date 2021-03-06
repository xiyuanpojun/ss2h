package com.hill.gwyb.controller;

import com.hill.gwyb.api.WebContentHelper;
import com.hill.gwyb.service.IFileService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileController extends ActionSupport {
    private File file;
    private Map dataMap;
    //要下载的文件名
    private String name;
    @Autowired
    private IFileService fileService;

    private List<String> head;
    private List<String> body;

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getDataMap() {
        return dataMap;
    }

    //下载时的文件名，转码是为了防止中文乱码。
    public String getFileName() throws Exception {
        return new String(file.getName().getBytes(), "ISO8859-1");
    }

    //下载时的文件流
    public InputStream getDownload() throws Exception {
        return new BufferedInputStream(new FileInputStream(file));
    }

    //创建文件调用的方法
    public String create() throws Exception {
        try {
            Map map = fileService.getData(head, body);
            //表头
            List<String> headList = (List<String>) map.get("headList");
            //内容
            List<String[]> bodyList = (List<String[]>) map.get("bodyList");

            if (headList != null && bodyList != null && headList.size() > 0) {
                dataMap = fileService.create(headList, bodyList);
                return "createSuccess";
            } else {
                dataMap = new HashMap();
                dataMap.put("error", 1);
                dataMap.put("message", "参数不正确，生成文件失败。");
                return "downloadError";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataMap = new HashMap();
        dataMap.put("error", 1);
        dataMap.put("message", "参数不正确，生成文件失败。");
        return "downloadError";
    }

    //下载文件调用的方法
    public String download() throws Exception {
        file = new File(WebContentHelper.getRealPath() + "/upload/" + name);
        if (!file.exists()) {
            dataMap = new HashMap();
            dataMap.put("message", "该文件不存在或已被删除");
            return "downloadError";
        }
        return "downloadSuccess";
    }
}
