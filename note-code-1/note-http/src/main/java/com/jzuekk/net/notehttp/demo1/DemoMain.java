package com.jzuekk.net.notehttp.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;


/**
 * @author jzue
 * @date 2019/11/20 下午10:27
 **/
public class DemoMain {
    private static ObjectMapper mapper = new ObjectMapper();
    static {

    }
    public static void main(String[] args) {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        HttpGet[] requests = new HttpGet[2];
        requests[0]=new HttpGet("http://localhost:8081/ArticleCommentApi/demo");
        requests[1]=new HttpGet("http://localhost:8081/ArticleCommentApi/demo1");
        try {

            long start =System.currentTimeMillis();
            final CountDownLatch latch = new CountDownLatch(0);
            HashMap<String,AsyncResponse> asyncResponseHashMap = new HashMap<>();
            for (HttpUriRequest request:requests){
                DefaultCallBack<HttpResponse> defaultCallBack = new DefaultCallBack<>();
                client.execute(request, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        latch.countDown();
                        System.out.println("~~~~~~~~~~~"+httpResponse.toString());
                        try {
                            InputStream content = httpResponse.getEntity().getContent();
                            String s = new BufferedReader(new InputStreamReader(content)).readLine();
                            AsyncResponse<RpcResponse> response = new AsyncResponse<>();
                            asyncResponseHashMap.put(Double.valueOf(request.hashCode()+ Math.random()).toString(),response);
                            defaultCallBack.completed(response);
                        } catch (IOException e) {
                        }
                    }
                    @Override
                    public void failed(Exception e) {
                        defaultCallBack.failed(e);
                        latch.countDown();
                    }

                    @Override
                    public void cancelled() {
                        defaultCallBack.cancelled();
                        latch.countDown();
                    }
                });
            }
            latch.await();
            System.out.println("花费时间："+(System.currentTimeMillis()-start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


}
