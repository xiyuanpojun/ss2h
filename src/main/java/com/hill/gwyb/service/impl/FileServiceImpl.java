package com.hill.gwyb.service.impl;

import com.hill.gwyb.api.ExcelTools;
import com.hill.gwyb.api.ZipTools;
import com.hill.gwyb.service.IFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class FileServiceImpl implements IFileService {
    @Override
    public synchronized Map create(List<String> headList, List<String[]> bodyList) throws Exception {
        Map dataMap = new HashMap();
        Map<String, Object> map = null;
        String name = null;//文件名
        List head = null, body = null;//工作簿名、工作簿里面的内容合集
        List titleList, detailList;

        //文件名
        name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        map = new HashMap();
        head = new ArrayList();
        //sheet
        head.add("Sheet1");

        body = new ArrayList();
        titleList = new ArrayList();
        for (int i = 0; i < head.size(); i++) {
            List title = new ArrayList();
            //添加表头
            for (String s : headList) {
                title.add(s);
            }
            titleList.add(title);

        }
        body.add(titleList);
        detailList = new ArrayList();
        for (int j = 0; j < head.size(); j++) {
            List detail = new ArrayList();
            //内容
            for (String[] o : bodyList) {
                List item = new ArrayList();
                for (String s : o) {
                    item.add(s);
                }
                detail.add(item);
            }
            detailList.add(detail);
        }
        body.add(detailList);

        map.put("name", name);
        map.put("head", head);
        map.put("body", body);
        String url = ExcelTools.createFile(map);
        File[] files = new File[1];
        files[0] = new File(url);
        File file = ZipTools.compress(files, name);
        String pathTemp = file.getAbsolutePath().split("upload", 2)[1];
        dataMap.put("name", pathTemp.replace("\\", ""));
        dataMap.put("error", 0);
        return dataMap;
    }

    @Override
    public Map getData(List<String> head, List<String> body) throws Exception {
        List<String> headList = null;
        //内容
        List<String[]> bodyList = null;
        //文件名字
        String fName;
        Map datMap = new HashMap();

        headList = head;

        bodyList = new ArrayList<>();
        if (body != null) {
            for (String ss : body) {
                if (ss != null) {
                    String[] s = ss.split(",");
                    bodyList.add(s);
                }
            }
        }
        datMap.put("headList", headList);
        datMap.put("bodyList", bodyList);
        return datMap;
    }
}
