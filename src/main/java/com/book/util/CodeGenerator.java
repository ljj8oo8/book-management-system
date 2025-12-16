package com.book.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码生成工具类
 * 支持生成随机验证码、验证码图片（Base64格式）
 */
@Slf4j
public class CodeGenerator {

    // 验证码字符集（排除易混淆字符：0/O、1/I、8/B等）
    private static final String CODE_CHARSET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int IMG_WIDTH = 120;
    // 图片高度
    private static final int IMG_HEIGHT = 40;
    // 干扰线数量
    private static final int LINE_COUNT = 20;
    // 字体大小
    private static final int FONT_SIZE = 20;

    /**
     * 生成随机验证码字符串
     */
    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARSET.length());
            sb.append(CODE_CHARSET.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 根据验证码生成Base64格式图片
     * @param code 验证码字符串
     * @return Base64编码的图片字符串（不含data:image/png;base64,前缀）
     */
    public static String generateCodeImage(String code) {
        // 1. 创建图片缓冲区
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 2. 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);

        // 3. 设置字体
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

        // 4. 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(getRandomColor(100, 200));
            int x1 = random.nextInt(IMG_WIDTH);
            int y1 = random.nextInt(IMG_HEIGHT);
            int x2 = random.nextInt(IMG_WIDTH);
            int y2 = random.nextInt(IMG_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 5. 绘制验证码字符（每个字符随机颜色、位置、旋转角度）
        for (int i = 0; i < code.length(); i++) {
            // 随机颜色
            g.setColor(getRandomColor(50, 150));
            // 随机旋转角度（-30° ~ 30°）
            int rotate = random.nextInt(60) - 30;
            g.rotate(Math.toRadians(rotate), IMG_WIDTH / CODE_LENGTH * i + 15, IMG_HEIGHT / 2);
            // 绘制字符
            g.drawString(String.valueOf(code.charAt(i)),
                    IMG_WIDTH / CODE_LENGTH * i + 10,
                    IMG_HEIGHT / 2 + 10);
            // 恢复旋转
            g.rotate(-Math.toRadians(rotate), IMG_WIDTH / CODE_LENGTH * i + 15, IMG_HEIGHT / 2);
        }

        // 6. 绘制干扰点
        for (int i = 0; i < 50; i++) {
            g.setColor(getRandomColor(150, 200));
            int x = random.nextInt(IMG_WIDTH);
            int y = random.nextInt(IMG_HEIGHT);
            g.fillOval(x, y, 2, 2);
        }

        // 7. 释放资源
        g.dispose();

        // 8. 转换为Base64
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            log.error("生成验证码图片失败", e);
            return "";
        }
    }

    /**
     * 生成随机颜色
     * @param min 最小色值
     * @param max 最大色值
     */
    private static Color getRandomColor(int min, int max) {
        Random random = new Random();
        int r = min + random.nextInt(max - min);
        int g = min + random.nextInt(max - min);
        int b = min + random.nextInt(max - min);
        return new Color(r, g, b);
    }
}