package com.easy.sdk.demo.common.controller;

import com.easy.sdk.common.entity.Response;
import com.easy.sdk.common.extra.access.annotation.AccessLimit;
import com.easy.sdk.common.extra.emoji.annotation.EmojiFilter;
import com.easy.sdk.common.extra.ftp.IFileOperation;
import com.easy.sdk.common.web.crypto.Crypto;
import com.easy.sdk.common.web.crypto.CryptoTypeEnum;
import com.easy.sdk.common.web.crypto.SecretBody;
import com.easy.sdk.common.web.sign.Sign;
import com.easy.sdk.demo.common.entity.DemoEntity;
import com.easy.sdk.demo.common.service.DemoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 6:50 PM
 * @description demo controller
 */

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Resource
    private DemoService demoService;

    @Resource
    private IFileOperation iFileOperation;

    @RequestMapping("/demo")
    @SecretBody
    public Response<String> demo(String msg) {
        log.info("demo msg:{}", msg);
        return Response.ok(demoService.demo(msg));
    }

    @RequestMapping("/exception")
    public Response<String> exception() {
        int a = 1/0;
        return Response.ok("exception");
    }

    @RequestMapping("/get")
    public Response<DemoEntity> get(Long id) {
        return Response.ok(demoService.get(id));
    }

    /**
     * 校验 @Crypto 解密
     * @param name
     * @param pwd
     * @return
     */
    @GetMapping("/checkCrypto")
    public Response<DemoEntity> checkCrypto(String name,@Crypto(CryptoTypeEnum.DES) @Sign String pwd) {
        return Response.ok(demoService.check(name, pwd));
    }

    /**
     * 校验 @Crypto 解密
     * @param demoEntity
     * @return
     */
    @PostMapping("/checkCrypto")
    @SecretBody
    public Response<DemoEntity> checkCrypto(@RequestBody @Crypto DemoEntity demoEntity) {
        return Response.ok(demoService.check(demoEntity));
    }

    /**
     * 校验 @Sign 接口参数签名
     * @param name
     * @param pwd
     * @return
     */
    @GetMapping("/checkSign")
    public Response<DemoEntity> checkSign(String name,@Sign String pwd) {
        return Response.ok(demoService.check(name, pwd));
    }

    /**
     * 校验 @Sign 接口参数签名
     * @param demoEntity
     * @return
     */
    @PostMapping("/checkSign")
    public Response<DemoEntity> checkSign(@RequestBody @Sign DemoEntity demoEntity) {
        return Response.ok(demoService.check(demoEntity));
    }

    /**
     * 校验 @Sign 接口参数签名
     * @param demoEntity
     * @return
     */
    @PostMapping("/checkSign2")
    @Sign
    public Response<DemoEntity> checkSign2(@RequestBody DemoEntity demoEntity) {
        return Response.ok(demoService.check(demoEntity));
    }

    /**
     * 校验 @AccessLimit 接口限流
     * @param name
     * @param pwd
     * @return
     */
    @AccessLimit(enable = true, count = 2, timeout = 5000)
    @GetMapping("/limit")
    @EmojiFilter
    public Response<DemoEntity> limit(String name, String pwd) {
        return Response.ok(demoService.check(name, pwd));
    }


    /**
     * 校验 @EmojiFilter 表情过滤
     * @return
     */
    @PostMapping("/emoji")
    @EmojiFilter
    public Response<DemoEntity> emoji(@RequestBody DemoEntity demoEntity) {
        return Response.ok(demoService.check(demoEntity));
    }


    /**
     * ftp 文件传输
     * @param file
     * @return
     */
    @SneakyThrows
    @PostMapping("/ftp")
    public Response<String> ftp(@RequestParam("file") MultipartFile file) {
        return Response.ok(iFileOperation.uploadImg(file.getInputStream(), file.getOriginalFilename()));
    }

}
