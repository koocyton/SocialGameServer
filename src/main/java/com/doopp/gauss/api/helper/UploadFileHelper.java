package com.doopp.gauss.api.helper;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 上传文件的 Helper
 *
 * Created by henry on 2017/7/18.
 */
public class UploadFileHelper {

    /**
     * 将上传的文件，保存到指定的目录里
     *
     * @param file 上传的文件
     * @param dir 保存到的目标目录
     * @return boolean
     */
    public static String savePhoto(MultipartFile file, String dir) throws IOException {
        IdWorker idWorker = new IdWorker(1,1);
        Long fileName = idWorker.nextId();
        return UploadFileHelper.savePhoto(file, dir, String.valueOf(fileName));
    }

    /**
     * 将上传的文件，保存到指定的目录里
     *
     * @param file 上传的文件
     * @param dir 保存到的目标目录
     * @param fileName 除去后缀的文件名
     * @return boolean
     */
    public static String savePhoto(MultipartFile file, String dir, String fileName) throws IOException {
        // 如果上传了文件
        if (file!=null) {
            // 上传的文件原名称
            String originalFilename = file.getOriginalFilename();
            // 上传的文件类型 <转小写了>
            String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase() : null;
            // 如果有上传文件类型
            // 文件类型是图片
            if (extension!=null && ( extension.equals("gif") || extension.equals("jpg") || extension.equals("png") )) {
                // 自定义的文件名称
                String filePath = dir + System.getProperty("file.separator") + fileName + "." + extension;
                // 转存文件到指定的路径
                file.transferTo(new File(filePath));
                return filePath;
            }
        }
        return null;
    }
}
