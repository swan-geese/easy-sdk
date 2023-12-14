package com.easy.sdk.demo.ftp;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.easy.sdk.common.extra.ftp.IFileOperation;
import com.easy.sdk.demo.EasySdkDemoApplication;
import com.easy.sdk.demo.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/12/06 14:13 PM
 * @description ftp文件上传连接池测试类
 * - 说明
 *   * 配置yaml
 *   * 在启动类增加@EnableFtp
 *   * 在调用类注入 IFileOperation
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class FtpTest {

    /**
     * local 图片上传
     */
    @Test
    public void ftpTest() {
        String url = "http://localhost:8080/demo/ftp";
        String result = null;
        try {
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("file", "/Users/dearzhang/Downloads/note-todo/default/2023-11-20-1700450246/images/1700450246-a039697ab77829bf66bf6373165e6816.png");
            result = OkHttpUtils.sendPostMulitFile(url, bodyMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-14 10:43:05.145 []  INFO 38464 --- com.easy.sdk.demo.ftp.FtpTest : {"body":"/img/2023/12/14/1735128243771674624.png","retCode":"0000","retMsg":"ok"}
     */



    /**
     *  上传图片并添加水印
     */
    @Test
    public void imgLogoTest() {
        String picUrl = "https://images.cnblogs.com/cnblogs_com/LiuFqiang/1429011/o_220528033315_star-squashed.jpg";
        BufferedImage bufferedImage = ImgUtil.read(URLUtil.url(picUrl));
        String path = IFileOperation.genFilePath(StrUtil.format("{}public/img", ClassUtil.getClassPath()));
        FileUtil.mkdir(path);
//        String path = "/Users/dearzhang/Downloads/";
        ImgUtil.pressText(
                bufferedImage,
                FileUtil.file(path + "logo.png"),
                "博客园-LiuFqiang", Color.WHITE,
                new Font("微软雅黑", Font.BOLD, 40),
                0 ,
                0,
                0.8f
        );
    }


}
