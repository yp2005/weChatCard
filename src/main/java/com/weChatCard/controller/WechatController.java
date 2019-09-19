package com.weChatCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.entities.Card;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.CardRepository;
import com.weChatCard.repositories.UserRepository;
import com.weChatCard.service.CardService;
import com.weChatCard.service.UserService;
import com.weChatCard.utils.Constants;
import com.weChatCard.utils.WeChatUtil;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.WechatPushData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信推送接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/wechat")
@Api(description = "微信推送接口")
public class WechatController {
    private static Logger log = LoggerFactory.getLogger(WechatController.class);
    @Autowired
    private CardService cardService;

    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId; //公众号APPID

    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret; //公众号AppSecret

    @PostMapping(path = "/msgpush", consumes = "application/xml", produces = { MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(value = "消息推送", notes = "消息推送")
    public void msgpush(@RequestBody WechatPushData wechatPushData) throws BusinessException {
        switch (wechatPushData.getEvent()) {
            case "submit_membercard_user_info":
                this.cardService.activeCard(wechatPushData);
                break;
            default:
                break;
        }
    }
}
