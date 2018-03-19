package com.hill.gwyb.controller;

import com.hill.gwyb.service.impl.ReadExcelGw;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ResourceBundle;

public class GwUserController extends ActionSupport {
    ResourceBundle resource = ResourceBundle.getBundle("db");
    private String fileDir = resource.getString("upload.dir");
    private String MaxSizeStr = resource.getString("MaxSize");
    Integer MaxSize = Integer.parseInt(MaxSizeStr);

    @Autowired
    private HttpSession session;
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    private File file;//得到上传的文件
    private String survyName;//参数二

    private String fileContentType; //得到文件的类型
    private String fileFileName; //得到文件的名称

    @Override
    public String execute() throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter p = response.getWriter();
        String result = "";
        result = doUpload(request);
        p.print(result);
        p.close();
        return ActionSupport.NONE;
    }

    private String doUpload(HttpServletRequest request) {
        String result = "";
        String fileName = "";
        String filePath = "";
        try {
            File file = null;
            filePath = ServletActionContext.getServletContext().getRealPath("/");
            file = new File(filePath + fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = filePath + fileDir + "\\";
            fileName = getFileFileName();
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(getFile()));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath + fileName)));
            byte[] bytes = new byte[1024];
            int index;
            while ((index = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, index);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            survyName = getSurvyName();
            result = ReadExcelGw.main(new String[]{filePath + fileName, survyName}, request);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSurvyName() {
        return survyName;
    }

    public void setSurvyName(String survyName) {
        this.survyName = survyName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
}