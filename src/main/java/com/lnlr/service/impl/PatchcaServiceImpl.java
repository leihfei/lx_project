package com.lnlr.service.impl;

import com.lnlr.common.utils.JedisUtil;
import com.lnlr.service.PatchcaService;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author:leihfei
 * @description 生成验证码数据service
 * @date:Create in 9:09 2018/8/8
 * @email:leihfein@gmail.com
 */
@Service
public class PatchcaServiceImpl implements PatchcaService {
    private static final long serialVersionUID = -656795564528376490L;
    private static int WIDTH = 110;
    private static int HEIGHT = 50;
    private static int MAX_LENGTH = 4;
    private static int MIN_LENGTH = 4;

    private static String CACHE_NAME = "pcatcha_";
    private static ConfigurableCaptchaService cs;

    @Autowired
    private JedisUtil jedisUtil;

    @Value("${patchcaExpire}")
    private int expire;

    static {
        cs = new ConfigurableCaptchaService();
        cs.setWidth(WIDTH);
        cs.setHeight(HEIGHT);
        RandomWordFactory wf = new com.lnlr.service.impl.MyRandomWordFactory();
        wf.setMaxLength(MAX_LENGTH);
        wf.setMinLength(MIN_LENGTH);
        cs.setWordFactory(wf);
    }

    /**
     * @param action 没用
     * @param appid  没用
     * @param key    随机数
     * @author: leihfei
     * @description: 生成验证码
     * @return: 空
     * @date: 13:09 2018/8/8
     * @email: leihfein@gmail.com
     */

    @Override
    public void doGet(String action, String appid, String key, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long newSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        // 清除缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        // 显示类型
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        String patchca = EncoderHelper.getChallangeAndWriteImage(cs, "png", os);
        // 放入缓存
        jedisUtil.set((CACHE_NAME + action + appid + key).getBytes(), patchca.getBytes(), expire);
        os.flush();
        os.close();
    }

    /**
     * @param sessionId sessionid
     * @param code      验证码
     * @author: leihfei
     * @description: 判断验证码是否正确
     * @return: 真假
     * @date: 9:20 2018/8/8
     * @email: leihfein@gmail.com
     */
    @Override
    public boolean validate(String sessionId, String code) {
        byte[] bytes = jedisUtil.get((CACHE_NAME + sessionId).getBytes());
        if (bytes != null) {
            if (new String(bytes).equals(code)) {
                return true;
            }
        }
        jedisUtil.del((CACHE_NAME + sessionId).getBytes());
        return false;
    }

    /**
     * 判断验证码是否过期
     *
     * @param sessionId sessionid
     * @return 真假
     */
    @Override
    public boolean validateExpire(String sessionId) {
        byte[] bytes = jedisUtil.get((CACHE_NAME + sessionId).getBytes());
        if (bytes == null) {
            return true;
        }
        return false;
    }
}
