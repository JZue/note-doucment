package com.jzue.concurrency;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author jzue
 * @date 2020/12/28 11:29 上午
 **/
public class Demo {
    //设置APPID/AK/SK
    public static final String APP_ID = "23401867";
    public static final String API_KEY = "99u7wTFsISERbR9Kr9N8brW0";
    public static final String SECRET_KEY = "LWv6RNyCT4f7ndgbiamM6I17yGEMgylx";

    public static void main1(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "/Users/jzue/Desktop/WechatIMG184.jpeg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));

    }

    public static void main(String[] args) {
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("probability", "true");


        // 参数为本地图片路径
        String image = "/Users/jzue/Desktop/WechatIMG184.jpeg";
        JSONObject res = client.basicAccurateGeneral(image, options);
        System.out.println(res.toString(2));

//        // 参数为本地图片二进制数组
//        byte[] file = readImageFile(image);
//        res = client.basicAccurateGeneral(file, options);
//        System.out.println(res.toString(2));

    }
}

