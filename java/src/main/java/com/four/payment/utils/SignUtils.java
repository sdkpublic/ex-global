package com.four.payment.utils;

import com.google.common.collect.Ordering;
import org.springframework.util.DigestUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment
 * @Author: george
 * @CreateTime: 2024-12-30  15:36
 * @Description: 签名工具
 * @Version: 1.0.0
 */

public class SignUtils {

    private static final Integer RANDOM = 652;

    public static String sign(Map<String, Object> params, String key) {
        List<String> keys = Ordering.usingToString().sortedCopy(params.keySet());
        StringBuilder sb = new StringBuilder();
        Iterator var4 = keys.iterator();

        while(var4.hasNext()) {
            String k = (String)var4.next();
            if (k.equals("time")) {
                Long timestamp = (Long)params.get(k);
                sb.append(k).append("=").append(timestamp - (long)RANDOM).append("&");
            } else {
                sb.append(k).append("=").append(params.get(k)).append("&");
            }
        }

        sb.append("key").append("=").append(key);
        System.out.println("验签str:" + sb.toString());
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }
}
