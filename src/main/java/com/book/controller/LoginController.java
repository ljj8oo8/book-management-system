package com.book.controller;

import com.book.constant.CommonConstant;
import com.book.dto.LoginDTO;
import com.book.jwt.JwtUtils;
import com.book.service.LogService;
import com.book.util.CodeGenerator;
import com.book.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 登录认证控制器
 * 处理验证码生成、用户登录、退出登录等逻辑
 */
@Slf4j
@Api(tags = "登录认证接口")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private LogService logService;

    /**
     * 生成验证码（Base64图片）
     */
    @ApiOperation("验证码")
    @GetMapping("/code")
    public Result<String> generateCode(HttpServletRequest request) {
        // 生成验证码
        String code = CodeGenerator.generateCode();
        String codeImage = CodeGenerator.generateCodeImage(code);
        String codeKey = UUID.randomUUID().toString();
        String ip = request.getRemoteAddr();
        log.debug("生成code:"+ip+"--"+codeKey+"--"+code);
        Cache cache = cacheManager.getCache(CommonConstant.CACHE_KEY_CODE);
        cache.put(codeKey + "_" + ip, code);

        log.debug("缓存code:"+cache.get(codeKey + "_" + ip,String.class));
        return Result.success(codeImage + "|" + codeKey);
    }

    /**
     * 用户登录（验证码+账号密码）
     */
    @ApiOperation("用户登录")
    @PostMapping
    public Result<String> login(
            @ApiParam("登录参数") @RequestBody LoginDTO loginDTO,
            HttpServletRequest request) {
        // 1. 验证验证码
        String ip = request.getRemoteAddr();
        String codeKey = loginDTO.getCodeKey();
        Cache cache = cacheManager.getCache(CommonConstant.CACHE_KEY_CODE);
        String cacheCode = cache.get(codeKey + "_" + ip, String.class);
        log.debug("取出缓存code:"+codeKey + "_" + ip+"_" +cacheCode);
        if (cacheCode == null || !cacheCode.equalsIgnoreCase(loginDTO.getCode())) {
            log.warn("IP：{} 验证码错误，输入：{}，正确：{}", ip, loginDTO.getCode(), cacheCode);
            return Result.error("验证码错误或已过期");
        }

        // 2. 认证账号密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 记录登录日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_LOGIN,
                "用户登录成功：" + loginDTO.getUsername()
        );

        // 4. 清除验证码缓存
        cache.evict(codeKey + "_" + ip);


        String jwt = jwtUtils.generateToken(loginDTO.getUsername());

        return Result.success(jwt);
    }

    /**
     * 退出登录
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 清除Security上下文
        SecurityContextHolder.clearContext();
        // 记录退出日志
        logService.recordLog(
                request,
                "LOGOUT",
                "用户退出登录"
        );
        return Result.success();
    }
}