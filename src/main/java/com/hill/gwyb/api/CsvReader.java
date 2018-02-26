package com.hill.gwyb.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
    /**
     * 导入
     *
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<List> importCsv(File file) {
        List<List> dataList = new ArrayList<List>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            int i = 0;
            while ((line = br.readLine()) != null) {
                i++;
                if (i == 1) {
                    continue;
                }
                dataList.add(Arrays.asList(line.split(",")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }
}
