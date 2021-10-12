package com.inspur.cloud.console.rootcert.filetools;

import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.console.rootcert.entity.CreateRootCertParams;
import com.inspur.cloud.console.rootcert.service.RootCertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.Arrays;

@Slf4j
public class FileUtils {

    public static final String FAILED = "404";

    public static final String ENCODING = "UTF-8";


    /**
     * read content from file, and return string
     */
    public static String readToString(String folderPath, String fileName) {
        File folder = new File(folderPath);
        AssertUtils.isTrue(folder.exists(), FAILED, folderPath + " folder path does not exist.", HttpStatus.OK);
        File file = new File(folderPath + fileName);
        AssertUtils.isTrue(file.exists(), FAILED, fileName + " file does not exist.", HttpStatus.OK);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
        } catch (Exception e) {
            log.error("read file error: {}", e);
        }

        try {
            return new String(fileContent, ENCODING);
        } catch (Exception e) {
            log.error("Error: The OS does not support {}.", ENCODING);
            return null;
        }
    }

    public static String readToString(String folderPath) {
        File file = new File(folderPath);
        /*AssertUtils.isTrue(file.exists(), FAILED, folderPath + " file does not exist.", HttpStatus.OK);*/
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
            in.close();
        } catch (Exception e) {
            log.error("read file error: {}", e);
        }

        try {
            return new String(fileContent, ENCODING);
        } catch (Exception e) {
            log.error("Error: The OS does not support {}.", ENCODING);
            return null;
        }
    }

    public static String readToStringByLinux(String folderPath) throws Exception{
        log.error("=======================开始读取");
        ClassPathResource classPathResource = new ClassPathResource(folderPath);
        InputStream input = classPathResource.getInputStream();
        int len = 2048;
        StringBuffer sf = new StringBuffer();
        byte[] by=new byte[len];
        int tem;
        while((tem=input.read(by))!=-1){
            sf.append(new String(by,0,tem));
        }
        input.close();
        return sf.toString();
    }

    /**
     * write string to file
     */
    public static void writeToFile(String content, String folderPath, String outFileName) {
        File folder = new File(folderPath);
        AssertUtils.isTrue(folder.exists(), FAILED, folderPath + " path does not exist.", HttpStatus.OK);
        try {
            File file = new File(folderPath + outFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
                byte[] bytes = content.getBytes();
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
            }
            log.info("Successfully write string to file!");
        } catch (Exception e) {
            log.error("Failed to write file: ", e);
        }

    }

    public static void writeToFile(String content, String folderPath) {
        //File folder = new File(folderPath);
        //AssertUtils.isTrue(folder.exists(), FAILED, folderPath + " path does not exist.", HttpStatus.OK);
        try {
            File file = new File(folderPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
                byte[] bytes = content.getBytes();
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            log.info("Successfully write string to file!");
        } catch (Exception e) {
            log.error("Failed to write file: ", e);
        }

    }

    /**
     * delete multiple files in the folderPath folder.
     */
    public static void deleteFile(String folderPath, String... fileNames) {
        File folder = new File(folderPath);
        AssertUtils.isTrue(folder.exists(), FAILED, folderPath + " path does not exist.", HttpStatus.OK);
        try {
            for (String fileName : fileNames) {
                File file = new File(folderPath + fileName);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            log.error("============== Failed to delete files: ", e);
        }

    }
}
