package com.weChatCard.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.List;

/**
 * http工具类
 *
 * @author: yupeng
 **/
public class HttpUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public JSONObject doPostResult(String url, String param, String authorization) {
        log.info("doPostResult url: " + url + "\nparam: " + param);
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpClient = new SSLClient();
            // 设置超时时间
            httpPost = new HttpPost(url);
            // 构造消息头
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            if (!StringUtils.isEmpty(authorization)) {
                httpPost.setHeader("Authorization", authorization);
            }
            // 构建消息实体
            StringEntity entity = new StringEntity(param, "utf-8");
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doPostResult result: " + result);
                    try {
                        return JSONObject.parseObject(result);
                    } catch (Exception e) {
                        JSONObject resultJO = new JSONObject();
                        resultJO.put("result", result);
                        return resultJO;
                    }
                }
            } else if (response != null) {
                log.info("StatusCode: " + response.getStatusLine().getStatusCode() + " " + response.getEntity());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject doPutResult(String url, String param, String authorization) {
        log.info("doPutResult url: " + url + "\nparam: " + param);
        HttpClient httpClient = null;
        HttpPut httpPut = null;
        try {
            httpClient = new SSLClient();
            // 设置超时时间
            httpPut = new HttpPut(url);
            // 构造消息头
            httpPut.setHeader("Content-type", "application/json; charset=utf-8");
            if (!StringUtils.isEmpty(authorization)) {
                httpPut.setHeader("Authorization", authorization);
            }
            // 构建消息实体
            StringEntity entity = new StringEntity(param, "utf-8");
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doPutResult result: " + result);
                    try {
                        return JSONObject.parseObject(result);
                    } catch (Exception e) {
                        JSONObject resultJO = new JSONObject();
                        resultJO.put("result", result);
                        return resultJO;
                    }
                }
            } else if (response != null) {
                log.info("StatusCode: " + response.getStatusLine().getStatusCode() + " " + response.getEntity());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject doFormPostResult(String url, List<BasicNameValuePair> parames) {
        log.info("doFormPostResult url: " + url + "\nparam: " + parames);
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(parames, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doFormPostResult result: " + result);
                    try {
                        return JSONObject.parseObject(result);
                    } catch (Exception e) {
                        JSONObject resultJO = new JSONObject();
                        resultJO.put("result", result);
                        return resultJO;
                    }
                }
            } else if (response != null) {
                log.info("StatusCode: " + response.getStatusLine().getStatusCode() + " " + response.getEntity());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public JSONObject doDeleteResult(String url, String param, String authorization) {
        log.info("doDeleteResult url: " + url + "\nparam: " + param);
        HttpClient httpClient = null;
        HttpDeleteWidthBody httpDelete = null;
        try {
            httpClient = new SSLClient();
            // 设置超时时间
            httpDelete = new HttpDeleteWidthBody(url);
            // 构造消息头
            httpDelete.setHeader("Content-type", "application/json; charset=utf-8");
            if (!StringUtils.isEmpty(authorization)) {
                httpDelete.setHeader("Authorization", authorization);
            }
            // 构建消息实体
            StringEntity entity = new StringEntity(param, "utf-8");
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpDelete.setEntity(entity);
            HttpResponse response = httpClient.execute(httpDelete);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doDeleteResult result: " + result);
                    try {
                        return JSONObject.parseObject(result);
                    } catch (Exception e) {
                        JSONObject resultJO = new JSONObject();
                        resultJO.put("result", result);
                        return resultJO;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject doGetResult(String url, String authorization) {
        log.info("doGetResult url: " + url);
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            if (!StringUtils.isEmpty(authorization)) {
                httpGet.setHeader("Authorization", authorization);
            }
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doGetResult result: " + result);
                    try {
                        return JSONObject.parseObject(result);
                    } catch (Exception e) {
                        JSONObject resultJO = new JSONObject();
                        resultJO.put("result", result);
                        return resultJO;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject doGet(String url, String authorization) {
        log.info("doGet url: " + url);
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            if (!StringUtils.isEmpty(authorization)) {
                httpGet.setHeader("Authorization", authorization);
            }
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "utf-8");
                    log.info("doGet result: " + result);
                    return JSONObject.parseObject(result);
                }
            } else if (response != null) {
                log.info("StatusCode: " + response.getStatusLine().getStatusCode() + " " + response.getEntity());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("doGet result: " + null);
        return null;
    }

    public static void main(String[] args) {

    }

    class HttpDeleteWidthBody extends HttpEntityEnclosingRequestBase {

        public final static String METHOD_NAME = "DELETE";

        public HttpDeleteWidthBody() {
            super();
        }

        public HttpDeleteWidthBody(final URI uri) {
            super();
            setURI(uri);
        }

        /**
         * @throws IllegalArgumentException if the uri is invalid.
         */
        public HttpDeleteWidthBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

    }
}
