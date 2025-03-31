package com.four.payment.utils;

import com.four.payment.request.ChannelListRequest;
import com.four.payment.request.WithdrawOrderRequest;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment
 * @Author: george
 * @CreateTime: 2024-12-30  15:36
 * @Description: 代付工具类
 * @Version: 1.0.0
 */
public class CheckParamsUtils {

    /**
     * 签名验证参数转化
     *
     * @param request
     * @return
     */
    public static Map<String, Object> convertVerifySignParams(ChannelListRequest request) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("uid", request.getUid());
        return params;
    }
}
