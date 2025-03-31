package com.four.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment.request
 * @Author: george
 * @CreateTime: 2024-12-30  16:01
 * @Description:
 * @Version: 1.0.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderRequest implements Serializable {


    private String amount;

    private String bankAccount;

    private String bankBranchName;
    /**
     * 详见:支持的银行列表 ${host} /coin/pay/proxy/query/bank
     **/
    private String bankCode;

    private String bankName;

    private String bankUserName;
    /**
     * 详见:支持的渠道列表 ${host} /coin/pay/proxy/query/channelList
     **/
    private String channelCode;

    /**
     * 详见:支持的法币列表 ${host} /coin/pay/proxy/query/supportCountry
     **/
    private String currencyCoinName;

    private String merchantOrderNo;

    private Integer uid;

    private Integer userId;

    public boolean validateParams() {
        return false;
    }
}
