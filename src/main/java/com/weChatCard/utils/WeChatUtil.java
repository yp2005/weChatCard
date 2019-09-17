package com.weChatCard.utils;

import com.alibaba.fastjson.JSONObject;

public class WeChatUtil {

    private HttpUtil httpUtil;

    public WeChatUtil() {
        this.httpUtil = new HttpUtil();
    }

    private String wechatUrl = "https://api.weixin.qq.com";

    // 登陆
    public JSONObject login(String appId, String appSecret, String code) {
        String url = this.wechatUrl + "/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        return this.httpUtil.doGet(url, null);
    }

    // 获取api_ticket
    public JSONObject getApiTicket(String accessToken) {
        String url = this.wechatUrl + "/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=wx_card";
        return this.httpUtil.doGet(url, null);
        // 正确消息格式 {"ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKdvsdshFKA","expires_in":7200}
        // 错误消息根式 {"errcode":40013,"errmsg":"invalid appid"}
    }

    // 获取token 注意小程序和公众号的appId和appSecret不同
    public JSONObject getAccessToken(String appId, String appSecret) {
        String url = this.wechatUrl + "/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        return this.httpUtil.doGet(url, null);
        // 正确消息格式 {"access_token":"ACCESS_TOKEN","expires_in":7200}
        // 错误消息根式 {"errcode":40013,"errmsg":"invalid appid"}
    }

    // 创建卡券 accessToken是公众号的，param前台传来，格式参考 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1451025056
    public JSONObject createCard(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/card/create?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // 正确消息格式 {"errcode":0,"errmsg":"ok","card_id":"plqmg1eUNAIyv2sc6lzpQ0o695YI"}
        // 错误消息根式 {"errcode":40164,"errmsg":"invalid ip 125.34.15.103, not in whitelist hint: [qpKwUa0785hb10]"}
    }

    // 更改卡券 accessToken是公众号的，param前台传来，格式参考 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1451025056
    public JSONObject updateCard(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/card/update?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // {"errcode":0,"errmsg":"ok","send_check":false}
        // 错误消息根式 {"errcode":40164,"errmsg":"invalid ip 125.34.15.103, not in whitelist hint: [qpKwUa0785hb10]"}
    }

    // 删除卡券 accessToken是公众号的，param {"card_id": "pFS7Fjg8kV1IdDz01r4SQwMkuCKc"}
    public JSONObject deleteCard(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/card/delete?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // 正确消息格式 {"errcode":0,"errmsg":"ok"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 核销卡券 accessToken是公众号的，param {"code": "12312313"}
    public JSONObject consumeCard(String accessToken, JSONObject param) {
        JSONObject decryptCodeParam = new JSONObject();
        decryptCodeParam.put("encrypt_code", param.getString("code"));
        JSONObject result = this.decryptCode(accessToken, decryptCodeParam);
        if(result.getInteger("errcode").equals(0)) {
            param.put("code", result.getString("code"));
            String url = this.wechatUrl + "/card/code/consume?access_token=" + accessToken;
            return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        }
        return result;
        // 正确消息格式 {"errcode":0,"errmsg":"ok"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 卡券code解码 accessToken是公众号的，param {  "encrypt_code":"XXIzTtMqCxwOaawoE91+VJdsFmv7b8g0VZIZkqf4GWA60Fzpc8ksZ/5ZZ0DVkXdE"}
    public JSONObject decryptCode(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/card/code/decrypt?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // 正确消息格式 {"errcode":0,"errmsg":"ok","code":"751234212312"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 设置卡券失效接口 accessToken是公众号的，param {  "code": "12312313",  "reason":"退款"}
    public JSONObject unavailableCard(String accessToken, JSONObject param) {
        JSONObject decryptCodeParam = new JSONObject();
        decryptCodeParam.put("encrypt_code", param.getString("code"));
        JSONObject result = this.decryptCode(accessToken, decryptCodeParam);
        if(result.getInteger("errcode").equals(0)) {
            param.put("code", result.getString("code"));
            String url = this.wechatUrl + "/card/code/unavailable?access_token=" + accessToken;
            return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        }
        return result;
        // 正确消息格式 {"errcode":0,"errmsg":"ok"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 查询Code接口 accessToken是公众号的，param {
    //   "card_id" : "card_id_123+",
    //   "code" : "123456789",
    //   "check_consume" : true
    //}
    public JSONObject getCode(String accessToken, JSONObject param) {
        JSONObject decryptCodeParam = new JSONObject();
        decryptCodeParam.put("encrypt_code", param.getString("code"));
        JSONObject result = this.decryptCode(accessToken, decryptCodeParam);
        if(result.getInteger("errcode").equals(0)) {
            param.put("code", result.getString("code"));
            String url = this.wechatUrl + "/card/code/get?access_token=" + accessToken;
            return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        }
        return result;
        // 正确消息格式 {"errcode":0,"errmsg":"ok"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 查看卡券详情 accessToken是公众号的，param {"card_id": "pFS7Fjg8kV1IdDz01r4SQwMkuCKc"}
    public JSONObject getCard(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/card/get?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // 正确消息格式 见https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1451025272
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }

    // 发送模板消息 accessToken是小程序的，param：
    //        {
    //            "touser": "OPENID",
    //            "template_id": "TEMPLATE_ID",
    //            "page": "index",
    //            "form_id": "FORMID",
    //            "data": {
    //                "keyword1": {
    //                    "value": "339208499"
    //                },
    //                "keyword2": {
    //                    "value": "2015年01月05日 12:30"
    //                },
    //                "keyword3": {
    //                    "value": "腾讯微信总部"
    //                },
    //                "keyword4": {
    //                    "value": "广州市海珠区新港中路397号"
    //                }
    //            },
    //            "emphasis_keyword": "keyword1.DATA"
    //        }
    public JSONObject templateSend(String accessToken, JSONObject param) {
        String url = this.wechatUrl + "/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        return this.httpUtil.doPostResult(url, param.toJSONString(), null);
        // 正确消息格式 {"errcode":0,"errmsg":"ok"}
        // 错误消息根式 {"errcode":123,"errmsg":"xx"}
    }



}
