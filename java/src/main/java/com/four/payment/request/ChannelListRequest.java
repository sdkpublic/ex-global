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
public class ChannelListRequest implements Serializable {

    private Integer uid;

    public  boolean validateParams(){
        //TODO do something
        return true;
    }
}
