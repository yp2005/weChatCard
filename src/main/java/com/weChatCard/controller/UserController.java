package com.weChatCard.controller;

import com.alibaba.fastjson.JSONObject;
import com.weChatCard.config.LoginRequired;
import com.weChatCard.entities.User;
import com.weChatCard.redis.RedisClient;
import com.weChatCard.repositories.UserRepository;
import com.weChatCard.service.UserService;
import com.weChatCard.utils.*;
import com.weChatCard.utils.exception.BusinessException;
import com.weChatCard.utils.message.Messages;
import com.weChatCard.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 用户接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisClient redisClient;

    @Value("${SUBSCRIPTION_APP_ID}")
    private String subscriptionAppId; //公众号APPID

    @Value("${SUBSCRIPTION_APP_SECRET}")
    private String subscriptionAppSecret; //公众号小程序AppSecret

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostConstruct
    private void initUser() throws Exception {
        User user = this.userRepository.findByUserName(Constants.ADMIN_USER_NAME);
        if (user == null) {
            user = new User();
            user.setPersonName(Constants.ADMIN_USER_NAME);
            user.setUserName(Constants.ADMIN_USER_NAME);
            user.setUserType(Constants.USER_TYPE_ADMIN);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(EncodeUtils.encodeSHA("123456".getBytes())));
            userService.add(user);
        }
    }

    @PostMapping(path = "/list")
    @ApiOperation(value = "用户列表", notes = "查询用户信息列表")
    @LoginRequired(adminRequired = "1")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(userService.list(listInput));
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改用户(管理员接口)", notes = "修改用户信息接口(管理员接口)")
    @LoginRequired(adminRequired = "1")
    public CommonResponse update(@Validated({User.Validation.class}) @RequestBody User user, @ApiIgnore User loginUser) throws BusinessException {
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.update(user);
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        return commonResponse;
    }

    @PutMapping(path = "modifyUserInfo")
    @ApiOperation(value = "修改用户(用户修改自己的用户信息)", notes = "修改用户信息接口(用户修改自己的用户信息)")
    @LoginRequired
    public CommonResponse modifyUserInfo(@Validated({User.Validation.class}) @RequestBody User newUser, @ApiIgnore User loginUser) throws BusinessException {
        if(!newUser.getId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        if(!newUser.getUserName().equals(loginUser.getUserName())) {
            throw new BusinessException(Messages.CODE_40010, "用户名不允许修改！");
        }
        User user = this.userRepository.findOne(newUser.getId());
        newUser.setPassword(user.getPassword());
        newUser = userService.update(newUser);
        CommonResponse commonResponse = CommonResponse.getInstance(loginUser);
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询用户", notes = "根据ID查询用户")
    @LoginRequired
    public CommonResponse get(@NotNull(message = "用户编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        User user = userService.get(id);
        // 管理账户可以查看所有用户信息，普通用户只能查看自己的信息
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !loginUser.getId().equals(user.getId())) {
            throw new BusinessException(Messages.CODE_50201);
        }
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        return commonResponse;
    }

    @GetMapping(path = "/current")
    @ApiOperation(value = "查询当前用户信息", notes = "查询当前用户信息接口")
    @LoginRequired
    public CommonResponse current(@ApiIgnore User loginUser) throws BusinessException {
        User user = userService.get(loginUser.getId());
        user.setToken(loginUser.getToken());
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        return commonResponse;
    }

    @GetMapping(path = "/getByCardCode/{cardCode}")
    @ApiOperation(value = "查询当前用户信息", notes = "查询当前用户信息接口")
    @LoginRequired
    public CommonResponse getByCardCode(@PathVariable("cardCode") String cardCode, @ApiIgnore User loginUser) throws BusinessException {
        User user = userService.getByCardCode(cardCode);
        user.setToken(loginUser.getToken());
        CommonResponse commonResponse = CommonResponse.getInstance(user);
        return commonResponse;
    }

    @PostMapping(path = "/login")
    @ApiOperation(value = "登陆", notes = "登陆接口")
    public CommonResponse login(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        User user = this.userRepository.findByUserName(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(password, user.getPassword())) {
            user = this.userRepository.save(user);
            String token = UUID.randomUUID().toString();
            redisClient.set(Constants.USER_TOKEN + token, JSONObject.toJSONString(user), Constants.TOKEN_EXPIRE_TIME);
            user.setToken(token);
            CommonResponse commonResponse = CommonResponse.getInstance(user);
            return commonResponse;
        }
        else {
            throw new BusinessException(Messages.CODE_40101);
        }
    }

    @PostMapping(path = "/modifyPassword")
    @ApiOperation(value = "修改密码", notes = "修改密码接口")
    @LoginRequired
    public CommonResponse modifyPassword(@RequestParam("password") String password, @ApiIgnore User loginUser) throws Exception {
        loginUser = this.userRepository.findOne(loginUser.getId());
        loginUser.setPassword(encoder.encode(password));
        loginUser = this.userService.update(loginUser);
        return CommonResponse.getInstance();
    }

    @PostMapping(value = "/wechatLogin")
    @ApiOperation(value = "微信登陆", notes = "微信登陆接口")
    public CommonResponse wechatLogin(@RequestBody JSONObject param) throws Exception {
        WeChatUtil wu = new WeChatUtil();
        JSONObject result = wu.login(this.subscriptionAppId, this.subscriptionAppSecret, param.getString("code"));
//        JSONObject result = JSONObject.parseObject("{\"openid\": \"123\", \"unionid\" : \"234\"}");
        if (result != null && result.getString("openid") != null) {
            String openid = result.getString("openid");
            String sessionKey = result.getString("session_key");
            this.redisClient.set(Constants.SUBSCRIPTION_SESSION_KEY_PRE + openid, sessionKey);
            User user = this.userRepository.findBySubscriptionOpenId(openid);
            if (user == null) {
                user = new User();
                user.setUserType("2");
                user.setSubscriptionOpenId(openid);
                user = this.userService.add(user);
            }
            String token = UUID.randomUUID().toString();
            this.redisClient.set(Constants.USER_TOKEN + token, JSONObject.toJSONString(user), Constants.TOKEN_EXPIRE_TIME);
            user.setToken(token);
            return CommonResponse.getInstance(user);
        } else {
            throw new BusinessException(Messages.CODE_40010, "微信登陆失败: " + result);
        }
    }

    @PostMapping(value = "/decryptData")
    @ApiOperation(value = "解密微信数据", notes = "解密微信数据接口")
    @LoginRequired
    public CommonResponse decryptData(@RequestBody JSONObject param, @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        String encryptedData = param.getString("encryptedData");
        String iv = param.getString("iv");
        try {
            String sessionKey = this.redisClient.get(Constants.SUBSCRIPTION_SESSION_KEY_PRE + loginUser.getSubscriptionOpenId());
            String result = WechatDecryptUtil.decryptData(encryptedData, sessionKey, iv);
            commonResponse.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Messages.CODE_50000);
        }
        return commonResponse;
    }
}
