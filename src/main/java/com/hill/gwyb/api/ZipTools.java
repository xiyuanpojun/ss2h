package com.hill.gwyb.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTools {
    private ZipTools() {
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static File compress(File[] files) throws Exception {
        String name = sdf.format(new Date());
        return compress(files, name);
    }

    public static File compress(File[] files, String name) throws Exception {
        File result = new File(WebContentHelper.getRootPath() + "/upload/" + name + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(result));
        BufferedInputStream bis;
        for (File file : files) {
            bis = new BufferedInputStream(new FileInputStream(file));
            zos.putNextEntry(new ZipEntry(file.getAbsolutePath().split("upload", 2)[1].replaceFirst("\\\\", "")));
            int index;
            byte[] bytes = new byte[1024];
            while ((index = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, index);
            }
            bis.close();
        }
        zos.finish();
        zos.close();
        delete();
        return result;
    }

    private static void delete() {
        try {
            File root = new File(WebContentHelper.getRootPath() + "/upload");
            if (!root.exists() || !root.isDirectory() || Objects.requireNonNull(root.listFiles()).length <= 30) return;
            for (File file : Objects.requireNonNull(root.listFiles())) {
                if (file.isDirectory()) continue;
                String[] temp = file.getName().split("\\.", 2);

                Date creaTime = sdf.parse(temp[0]);
                Date nowTime = new Date();
                if (nowTime.getTime() - creaTime.getTime() >= 1000 * 60 * 60 * 24) {
                    file.delete();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
