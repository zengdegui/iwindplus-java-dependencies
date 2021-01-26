package com.iwindplus.boot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * MultipartFile转File工具类.
 *
 * @author zengdegui
 * @since 2021/1/11
 */
@Slf4j
public class MultipartFileToFileUtil {
    private static final int BSIZE = 8192;

    /**
     * MultipartFile转File.
     *
     * @param file 文件
     * @return File
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            toFile = inputStreamToFile(file, toFile);
        }
        return toFile;
    }

    private static File inputStreamToFile(MultipartFile file, File toFile) {
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            os = new FileOutputStream(toFile);
            int bytesRead = 0;
            byte[] buffer = new byte[BSIZE];
            while ((bytesRead = ins.read(buffer, 0, BSIZE)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("IO Exception [{}]", e);
        } finally {
            closeStream(ins, os);
        }
        return toFile;
    }

    private static void closeStream(InputStream ins, OutputStream os) {
        if (null != ins) {
            try {
                ins.close();
            } catch (IOException ex) {
                log.error("Closing input stream exception [{}]", ex);
            }
        }
        if (null != os) {
            try {
                os.close();
            } catch (IOException ex) {
                log.error("Closing output stream exception [{}]", ex);
            }
        }
    }

    /**
     * 删除本地临时文件.
     *
     * @param file 文件
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
}
