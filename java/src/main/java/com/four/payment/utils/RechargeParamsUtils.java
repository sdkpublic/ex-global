package com.four.payment.utils;

import com.four.payment.request.RechargeOrderRequest;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment
 * @Author: george
 * @CreateTime: 2024-12-30  15:36
 * @Description: 代收工具类
 * @Version: 1.0.0
 */
public class RechargeParamsUtils {

    /**
     * 签名验证参数转化
     *
     * @param request
     * @return
     */
    public static Map<String, Object> convertVerifySignParams(RechargeOrderRequest request) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("uid", request.getUid());
        params.put("currencyCoinName", request.getCurrencyCoinName());
        params.put("channelCode", request.getChannelCode());
        params.put("amount", request.getAmount());
        params.put("merchantOrderNo", request.getMerchantOrderNo());
        params.put("bankCode", request.getBankCode());
        params.put("paymentMethod",request.getPaymentMethod());

        return params;
    }

    /**
     * 签名验证参数转化
     *
     * @param request
     * @return
     */
    public static Map<String, Object> convertVerifySignOrderStatusParams(RechargeOrderRequest request) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("uid", request.getUid());
        params.put("merchantOrderNo", request.getMerchantOrderNo());

        return params;
    }
}
