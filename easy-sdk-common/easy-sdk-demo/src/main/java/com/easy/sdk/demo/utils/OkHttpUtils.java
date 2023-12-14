package com.easy.sdk.demo.utils;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/12/7 10:04 AM
 * @description OkHttp3工具类
 */

@Slf4j
public class OkHttpUtils {

    private static OkHttpClient client = null;

    static {
        client = new OkHttpClient().newBuilder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 利用okhttp3 发送get请求
     * HTTP接口-GET方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendGet(String url, Map<String, String> paramMap) throws IOException {
        return request(Method.GET.name(), null, url, paramMap, null, null);
    }

    /**
     * 利用okhttp3 发送get请求
     * HTTP接口-GET方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendGet(String url, Map<String, String> paramMap, Map<String, String> headerMap) throws IOException {
        return request(Method.GET.name(), null, url, paramMap, null, headerMap);
    }

    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST FORM 方式
     * @param url
     * @param bodyMap
     * @return String
     */
    public static String sendPost(String url,  Map<String, String> bodyMap) throws IOException {
        return request(Method.POST.name(), null, url, null, bodyMap, null);
    }

    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST FORM 方式
     * @param url
     * @param bodyMap
     * @return String
     */
    public static String sendPostMulitFile(String url,  Map<String, String> bodyMap) throws IOException {
        return request(Method.POST.name(), ContentType.FORM_DATA.name(), url, null, bodyMap, null);
    }

    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST FORM 方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @param bodyMap
     * @return String
     */
    public static String sendPost(String url, Map<String, String> paramMap, Map<String, String> bodyMap) throws IOException {
        return request(Method.POST.name(), null, url, paramMap, bodyMap, null);
    }

    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST FORM 方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @param bodyMap
     * @return String
     */
    public static String sendPost(String url, Map<String, String> paramMap, Map<String, String> bodyMap, Map<String, String> headerMap) throws IOException {
        return request(Method.POST.name(), null, url, paramMap, bodyMap, headerMap);
    }

    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST JSON 方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @param bodyMap
     * @return String
     */
    public static String sendPostWithJson(String url, Map<String, String> paramMap, Map<String, String> bodyMap) throws IOException {
        return request(Method.POST.name(), ContentType.JSON.name(), url, paramMap, bodyMap, null);
    }


    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST JSON 方式，请求参数形式为 paramMap 形式
     * @param url
     * @param paramMap
     * @param bodyMap
     * @return String
     */
    public static String sendPostWithJson(String url, Map<String, String> paramMap, Map<String, String> bodyMap, Map<String, String> headerMap) throws IOException {
        return request(Method.POST.name(), ContentType.JSON.name(), url, paramMap, bodyMap, headerMap);
    }


    /**
     * 利用okhttp3 发送post请求
     * HTTP接口-POST方式，请求参数形式为 jsonString 形式
     * @param url
     * @param jsonString
     * @return String
     */
    public static String sendPostWithJson(String url, String jsonString){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        //返回请求结果
        String result = "";
        try {
            Response response = call.execute();
            result = response.body().string();
            return result;
        } catch (IOException e) {
            log.error("OkHttp3 sendPostWithJson error:{}", e);
        }
        return result;
    }





    /**
     * 发送http请求通用工具类
     *
     * @param method 请求方法
     * @param url    地址
     * @param headerMap header 参数
     * @param paramMap param 参数
     * @param bodyMap body 参数
     * @return 请求结果
     */
    public static String request(String method, String contentType, String url, Map<String, String> paramMap, Map<String, String> bodyMap, Map<String, String> headerMap) {
        if (method == null) {
            throw new RuntimeException("请求方法不能为空");
        }
        if (url == null) {
            throw new RuntimeException("url不能为空");
        }
        // url 拼接参数
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (!CollectionUtils.isEmpty(paramMap)) {
            for (Map.Entry<String, String> param : paramMap.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        // body 拼接参数 (由于 get 请求时 FormBody 必须为 null，否则请求异常，因此该参数设置方式不同)
        //method1
//        RequestBody body = getRequestBody(contentType, bodyMap);
        //method2
        RequestBody body = ContentType.getRequestBody(contentType, bodyMap);

        // 构造 request 请求
        Request.Builder builder = new Request.Builder()
                .url(httpBuilder.build())
                .method(method, body);

        // 构建 header 参数
        if (!CollectionUtils.isEmpty(headerMap)) {
            for (Map.Entry<String, String> param : headerMap.entrySet()) {
                builder.addHeader(param.getKey(), param.getValue());
            }
        }
        Request request = builder.build();
        // 发送 http 请求
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            return result;
        } catch (IOException e) {
            log.error("OkHttp3 send method:{} error:{}", method, e);
        }
        return result;
    }

    /**
     * 获取body中请求参数，并封装到OkHttp3 RequestBody中
     * @param contentType
     * @param bodyMap
     * @return
     */
    @Nullable
    private static RequestBody getRequestBody(String contentType, Map<String, String> bodyMap) {
        RequestBody body = null;
        if (!CollectionUtils.isEmpty(bodyMap)) {
            // 如果请求参数类型为 form 表单
            if (ContentType.FORM.name().equals(contentType)) {
                // 如果post 请求为 application/x-www-form-urlencoded ，则采用该 body 参数
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                body = bodyBuilder.build();
            }
            // 如果请求参数类型为 application/json ，则采用该 body 参数
            if (ContentType.JSON.name().equals(contentType)) {
                body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONUtil.toJsonStr(bodyMap));
            }
            // 如果请求参数类型为 multipart/form-data
            if (ContentType.FORM_DATA.name().equals(contentType)) {
                //MediaType 为全部类型
                final MediaType mediaType = MediaType.parse("application/octet-stream");

                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    //根据文件类型，将File装进RequestBody中
                    RequestBody fileBody = RequestBody.create(mediaType, new File(String.valueOf(param.getValue())));
                    requestBodyMap.put(param.getKey(), fileBody);
                }
                //将fileBody添加进MultipartBody
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    bodyBuilder.addFormDataPart(param.getKey(), param.getValue(), requestBodyMap.get(param.getKey()));
                }
                body = bodyBuilder.build();
            }
        }
        return body;
    }

    /**
     * http 请求方式
     */
    enum Method {
        GET, POST
    }

    /**
     * post 请求参数类型
     * 暂定：FORM 表单形式，JSON 形式, FORM_DATA 表单文件形式
     */
    @Getter
    enum ContentType implements ContentTypeFunction {
        FORM {
            /**
             * 如果post 请求为 application/x-www-form-urlencoded ，则采用该 body 参数
             * @param bodyMap
             * @return
             */
            @Override
            public RequestBody apply(Map<String, String> bodyMap) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                return bodyBuilder.build();
            }
        },
        FORM_DATA{
            /**
             * 如果请求参数类型为 multipart/form-data
             * @param bodyMap
             * @return
             */
            @Override
            public RequestBody apply(Map<String, String> bodyMap) {
                //MediaType 为全部类型
                final MediaType mediaType = MediaType.parse(ContentTypeFunction.FILE_FORM_DATA);

                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    //根据文件类型，将File装进RequestBody中
                    RequestBody fileBody = RequestBody.create(mediaType, new File(String.valueOf(param.getValue())));
                    requestBodyMap.put(param.getKey(), fileBody);
                }
                //将fileBody添加进MultipartBody
                MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                for (Map.Entry<String, String> param : bodyMap.entrySet()) {
                    bodyBuilder.addFormDataPart(param.getKey(), param.getValue(), requestBodyMap.get(param.getKey()));
                }
                return bodyBuilder.build();
            }
        },
        JSON {
            /**
             * 如果请求参数类型为 application/json ，则采用该 body 参数
             * @param bodyMap
             * @return
             */
            @Override
            public RequestBody apply(Map<String, String> bodyMap) {
                return RequestBody.create(MediaType.parse(ContentTypeFunction.JSON + ContentTypeFunction.CHARSET), JSONUtil.toJsonStr(bodyMap));

            }


        },
        ;

        /**
         * 根据不同的请求类型，返回不同的body
         * @param contentType
         * @param bodyMap
         * @return
         */
        public static RequestBody getRequestBody(String contentType, Map<String, String> bodyMap) {
            return Arrays.stream(values())
                    .filter(value -> value.name().equals(contentType))
                    .findFirst()
                    .orElse(null)
                    .apply(bodyMap);
        }

        /**
         * 判断是否包含该请求类型
         * @param contentType
         * @return
         */
        public static Boolean isInclude(String contentType) {
            return Arrays.stream(values()).anyMatch(value -> value.name().equals(contentType));
        }
    }

    interface ContentTypeFunction {
        String FORM = "application/x-www-form-urlencoded";
        String FORM_DATA = "multipart/form-data";
        String FILE_FORM_DATA = "application/octet-stream";
        String JSON = "application/json";
        String CHARSET = "charset=utf-8";

        RequestBody apply(Map<String, String> bodyMap);
    }


}
