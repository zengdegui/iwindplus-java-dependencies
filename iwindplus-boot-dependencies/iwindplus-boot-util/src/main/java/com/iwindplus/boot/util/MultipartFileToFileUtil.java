package com.iwindplus.boot.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * MultipartFile转File工具类.
 *
 * @author zengdegui
 * @since 2021/1/11
 */
public class MultipartFileToFileUtil {
	private static final int BSIZE = 8192;

	/**
	 * MultipartFile转File
	 *
	 * @param file 文件
	 * @throws Exception
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {
		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}

	private static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[BSIZE];
			while ((bytesRead = ins.read(buffer, 0, BSIZE)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除本地临时文件
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
