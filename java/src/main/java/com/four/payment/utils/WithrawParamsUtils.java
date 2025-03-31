package com.four.payment.utils;

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
public class WithrawParamsUtils {

    /**
     * 签名验证参数转化
     *
     * @param request
     * @return
     */
    public static Map<String, Object> convertVerifySignParams(WithdrawOrderRequest request) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("uid", request.getUid());
        params.put("merchantOrderNo", request.getMerchantOrderNo());
        params.put("currencyCoinName", request.getCurrencyCoinName());
        params.put("channelCode", request.getChannelCode());
        params.put("amount", request.getAmount());
        params.put("bankCode", request.getBankCode());
        params.put("bankName", request.getBankName());
        params.put("bankBranchName", request.getBankBranchName());
        params.put("bankUserName", request.getBankUserName());
        params.put("bankAccount", request.getBankAccount());
        return params;
    }
}
