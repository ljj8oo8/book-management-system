package com.book.util;

import com.book.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 文件操作工具类
 * 封装文件上传、删除、复制、目录创建等通用操作
 */
@Slf4j
public class FileUtil {


    /**
     * 创建目录（不存在则创建，支持多级目录）
     * @param dirPath 目录路径
     * @return 创建结果
     */
    public static boolean createDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            return true;
        }
        return dir.mkdirs();
    }

    /**
     * 删除文件/目录
     * @param filePath 文件/目录路径
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            log.warn("文件路径为空，无需删除");
            return true;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            log.warn("文件不存在：{}", filePath);
            return true;
        }
        // 目录递归删除
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }
        boolean deleted = file.delete();
        if (deleted) {
            log.info("文件删除成功：{}", filePath);
        } else {
            log.error("文件删除失败：{}", filePath);
        }
        return deleted;
    }

    /**
     * 上传文件（生成唯一文件名，避免覆盖）
     * @param fileBytes 文件字节数组
     * @param originalName 原始文件名
     * @param uploadDir 上传目录
     * @return 最终文件路径
     */
    public static String uploadFile(byte[] fileBytes, String originalName, String uploadDir) {
        // 1. 创建上传目录
        createDir(uploadDir);
        // 2. 获取文件后缀
        String suffix = getFileSuffix(originalName);
        // 3. 生成唯一文件名
        String fileName = UUID.randomUUID() + suffix;
        String filePath = uploadDir + File.separator + fileName;
        // 4. 写入文件
        try {
            Files.write(Paths.get(filePath), fileBytes);
            log.info("文件上传成功：{}", filePath);
            return filePath;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 复制文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 复制结果
     */
    public static boolean copyFile(String sourcePath, String targetPath) {
        try {
            Files.copy(Paths.get(sourcePath),
                    Paths.get(targetPath),
                    StandardCopyOption.REPLACE_EXISTING);
            log.info("文件复制成功：{} -> {}", sourcePath, targetPath);
            return true;
        } catch (IOException e) {
            log.error("文件复制失败", e);
            return false;
        }
    }

    /**
     * 获取文件后缀（含.）
     * @param fileName 文件名
     * @return 后缀（如.pdf、.png）
     */
    public static String getFileSuffix(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 校验文件类型（仅允许指定后缀）
     * @param fileName 文件名
     * @param allowSuffixes 允许的后缀数组
     * @return 校验结果
     */
    public static boolean checkFileSuffix(String fileName, String[] allowSuffixes) {
        String suffix = getFileSuffix(fileName).toLowerCase();
        for (String allow : allowSuffixes) {
            if (suffix.equals(allow.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验文件大小
     * @param fileSize 文件大小（字节）
     * @param maxSize 最大允许大小（字节）
     * @return 校验结果
     */
    public static boolean checkFileSize(long fileSize, long maxSize) {
        return fileSize <= maxSize;
    }

    /**
     * 获取文件大小（字节）
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : 0;
    }

    // ====================== 业务专用方法 ======================

    /**
     * 上传PDF文件（封装业务逻辑）
     * @param fileBytes PDF文件字节数组
     * @param originalName 原始文件名
     * @return PDF文件路径
     */
    public static String uploadPdfFile(byte[] fileBytes, String originalName) {
        // 校验文件类型
        if (!checkFileSuffix(originalName, new String[]{CommonConstant.FILE_SUFFIX_PDF})) {
            throw new IllegalArgumentException("仅支持PDF格式文件");
        }
        // 校验文件大小
        if (!checkFileSize(fileBytes.length, CommonConstant.FILE_MAX_SIZE)) {
            throw new IllegalArgumentException("文件大小超过限制（最大50MB）");
        }
        // 上传文件
        return uploadFile(fileBytes, originalName, CommonConstant.UPLOAD_PATH_BOOK);
    }
}