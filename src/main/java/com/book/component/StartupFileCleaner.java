package com.book.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

/**
 * SpringBoot启动后执行一次的文件清理器
 * 用于删除指定目录下的所有文件（可配置过滤规则）
 */
@Component
@Slf4j
public class StartupFileCleaner implements CommandLineRunner {


    @Value("${book.uploads.pdf.path}")
    private String pdfDir;

   
    
    @Override
    public void run(String... args) throws Exception {
        log.debug("开始执行启动后文件清理任务，目标目录：{}", pdfDir);
        File cleanDir = new File(pdfDir);

        // 校验目录是否存在
        if (!cleanDir.exists()) {
            log.warn("清理目录不存在，自动创建：{}", pdfDir);
            boolean mkdirs = cleanDir.mkdirs();
            if (!mkdirs) {
                log.error("创建清理目录失败：{}", pdfDir);
                return;
            }
            return;
        }

        // 校验是否为目录
        if (!cleanDir.isDirectory()) {
            log.error("指定路径不是有效目录：{}", pdfDir);
            return;
        }

        // 执行目录清理
        cleanDirectory(cleanDir);
        log.debug("启动后文件清理任务执行完成");
    }

    /**
     * 递归清理目录下的文件
     * @param dir 待清理的目录
     */
    private void cleanDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        // 遍历目录下的所有文件/子目录
        Arrays.stream(files).forEach(file -> {
            try {
                if (file.isFile()) {
                    // 删除文件
                    boolean deleted = file.delete();
                    if (deleted) {
                        log.debug("成功删除文件：{}", file.getAbsolutePath());
                    } else {
                        log.error("删除文件失败：{}", file.getAbsolutePath());
                    }
                } else if (file.isDirectory()) {
                    // 递归清理子目录
                    cleanDirectory(file);
                }
            } catch (Exception e) {
                log.error("处理文件时发生异常：{}", file.getAbsolutePath(), e);
            }
        });
    }

}
