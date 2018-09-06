package com.rs.common.util.httpclient;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * httpUtil类
 *
 * @author liegou
 * @date
 */
public class HttpUtil {

    /**
     * 文件上传带用户参数
     *
     * @param file
     * @param userId 用户id
     * @return
     */
    public static synchronized Object uploadFile(MultipartFile file, String userId, String url) {
        try {
            return HttpHelper.executeUploadFileStream(url, file, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传至hadoop
     *
     * @param file
     * @return
     */
    public static synchronized boolean uploadFileToHadoop(String url, MultipartFile file, String ip, String fileName) {
        try {
            return HttpHelper.uploadFileStreamToHadoop(url, file, ip, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从hadoop nameNode获取文件地址
     *
     * @param url
     * @return
     */
    public static synchronized String getFilePathFromHadoop(String url, String ip) {
        try {
            return HttpHelper.getFilePathFromHadoop(url, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

