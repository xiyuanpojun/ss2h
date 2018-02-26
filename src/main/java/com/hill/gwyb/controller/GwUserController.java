package com.hill.gwyb.controller;

import com.hill.gwyb.api.WebContentHelper;
import com.hill.gwyb.service.impl.ReadExcelGw;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class GwUserController extends ActionSupport {
    private static final long serialVersionUID = 1L;
    ResourceBundle resource = ResourceBundle.getBundle("db");
    private String fileDir = resource.getString("upload.dir");
    private String MaxSizeStr = resource.getString("MaxSize");
    Integer MaxSize = Integer.parseInt(MaxSizeStr);

    @Autowired
    private HttpSession session;
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

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
            DiskFileItemFactory fileFactory = new DiskFileItemFactory();
            fileFactory.setSizeThreshold(MaxSize);
            ServletFileUpload fu = new ServletFileUpload(fileFactory);
            List fileItems = fu.parseRequest(request);
            Iterator iter = fileItems.iterator();
            File file = null;
//            filePath = this.getServletConfig().getServletContext().getRealPath("/");
            filePath = WebContentHelper.getRootPath() + "/";
            file = new File(filePath + fileDir);
//			System.out.println(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            filePath = filePath + fileDir + "\\";
            String survyName = "";//问卷
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    fileName = item.getName();
                    int index = fileName.lastIndexOf("\\");
                    if (index != -1) {
                        fileName = fileName.substring(index + 1);
                    }
                    item.write(new File(filePath + fileName));// fileDir+"/"+
                } else {
                    survyName = item.getString("utf-8");
                }
            }
            result = ReadExcelGw.main(new String[]{filePath + fileName, survyName});
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }
}