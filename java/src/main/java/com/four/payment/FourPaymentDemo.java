package com.four.payment;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.four.payment.request.ChannelListRequest;
import com.four.payment.request.RechargeOrderRequest;
import com.four.payment.request.WithdrawOrderRequest;
import com.four.payment.utils.CheckParamsUtils;
import com.four.payment.utils.RechargeParamsUtils;
import com.four.payment.utils.SignUtils;
import com.four.payment.utils.WithrawParamsUtils;

import java.util.Map;

/**
 * @BelongsProject: four-payment-demo
 * @BelongsPackage: com.four.payment
 * @Author: george
 * @CreateTime: 2024-12-30  15:36
 * @Description: 代收 代付 下单demo
 * @Version: 1.0.0
 */

public class FourPaymentDemo {

    public static final String HOST = "https://api.exlinked.global";

    /**
     * 创建代收订单-Url
     **/
    public static final String createRechargeOrderUrl = "/coin/pay/recharge/order/create";

    /**
     * 代收订单状态-Url
     **/
    public static final String rechargeOrderStatusUrl = "/coin/pay/recharge/order/status";
    /**
     * 代付订单状态-Url
     **/
    public static final String withdrawOrderStatusUrl = "/coin/pay/withdraw/order/status";

    /**
     * 汇率接口
     **/
    public static final String exchangeRateUrl = "/coin/pay/query/market-price-rate";

    /**
     * 创建代付订单-Url
     **/
    public static final String createWithdrawOrderUrl = "/coin/pay/withdraw/order/create";

    /**
     * 代收-获取已授权的渠道列表
     **/
    public static final String rechargeChannelListUrl = "/coin/pay/proxy/query/collectionChannelList";
    /**
     * 代付-获取已授权的渠道列表
     **/
    public static final String withdrawChannelListUrl = "/coin/pay/proxy/query/paymentChannelList";
    /**
     * 获取已授权-代收-的银行列表
     **/
    public static final String rechargeBankListUrl = "/coin/pay/proxy/query/collectionBank";
    /**
     * 获取已授权-代付-的银行列表
     **/
    public static final String withdrawBankListUrl = "/coin/pay/proxy/query/paymentBank";

    /**
     * 【商户后台】-【API设置】-【接入密钥】
     **/
    public static final String accessSecret = "ufMbBfJOrj9E0nKkoJwrXINhRIf2s4Gw";

    /**
     * 【商户uid】
     **/
    public static final Integer uid = 5588426;
    /**
     * 收银方式 1: JSON
     **/
    public static final Integer paymentMethodOfJSON = 1;
    /**
     * 收银方式  3: 平台收银台
     **/
    public static final Integer paymentMethodOfCheckouCounter = 3;
    /**
     * 银行直连
     **/
    public static final String BankDirect = "BankDirect";
    /**
     * 网银扫码
     **/
    public static final String ScanQRCode = "ScanQRCode";

    public static void main(String[] args) throws Exception {

        // 1、代收-渠道列表channelList
        rechargeChannelCodeListRequest();
        //2、 代收-银行列表
        rechargeBankCodeListRequest();
        //3、代收-创建订单
        sendCreateRechargeOrderRequest();
        //4、代收-查询订单状态
        sendRechargeOrderStatusRequest();
        //获取汇率
        exchangeRateRequest();
        // 5、代付-渠道列表channelList
        withdrawChannelCodeListRequest();
        //6、代付-银行列表
        withdrawBankCodeListRequest();
        //7、代付-创建订单
        sendCreateWithdrawOrderRequest();
        //8、代付-查询订单状态
        sendWithdrawOrderStatusRequest();
    }

    /**
     * @Author: george
     *  describe: Create - Collection Order
     * 创建-代收订单
     **/
    public static void sendCreateRechargeOrderRequest() {

        RechargeOrderRequest request = RechargeOrderRequest
                .builder()
                .uid(uid)
                .merchantOrderNo(System.currentTimeMillis() + "")
                .currencyCoinName("VND")
                .channelCode(ScanQRCode)
                .amount("200000")
                .bankCode("AllBanksSupported")
                .memo("george-test")
                .paymentMethod(paymentMethodOfJSON)
                .build();

        if (!request.validateParams()) {
            Map<String, Object> paramsMap = RechargeParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            System.err.println("request:{}" + paramsPost);

            String response = HttpUtil.createPost(HOST + createRechargeOrderUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();

            System.err.println("response:{}" + response);

            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     * describe: Create - Proxy payment order
     * 创建-代付订单
     **/
    public static void sendCreateWithdrawOrderRequest() {
        WithdrawOrderRequest request = WithdrawOrderRequest
                .builder()
                .amount("100015")
                .bankName("NGAN HANG TMCP A CHAU (ACB)")
                .bankBranchName("NGAN HANG TMCP A CHAU (ACB)")
                .bankUserName("george")
                .bankAccount("6100000000010")
                .bankCode("ACB")
                .channelCode("BankDirect")
                .currencyCoinName("VND")
                .merchantOrderNo(System.currentTimeMillis() + "")
                .uid(uid)
                .build();

        if (!request.validateParams()) {
            Map<String, Object> paramsMap = WithrawParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            System.err.println("request:{}" + paramsPost);

            String response = HttpUtil.createPost(HOST + createWithdrawOrderUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();

            System.err.println("response:{}" + response);
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     * describe: Obtain Authorized Payment Channel List
     * 获取已授权-代付-渠道列表 channelCodeList
     **/
    public static void withdrawChannelCodeListRequest() {
        ChannelListRequest request = ChannelListRequest
                .builder()
                .uid(uid)
                .build();

        if (request.validateParams()) {
            Map<String, Object> paramsMap = CheckParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            String response = HttpUtil.createPost(HOST + withdrawChannelListUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();
             System.err.println(response);
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     *  describe: Collection - Obtain authorized channel list
     * 代收-获取已授权渠道列表 channelCodeList
     **/
    public static void rechargeChannelCodeListRequest() {
        ChannelListRequest request = ChannelListRequest
                .builder()
                .uid(uid)
                .build();

        if (request.validateParams()) {
            Map<String, Object> paramsMap = CheckParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            String response = HttpUtil.createPost(HOST + rechargeChannelListUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();

            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     * describe: Obtain authorized collection bank list
     * 获取已授权-代收-银行列表bankCodeList
     **/
    public static void rechargeBankCodeListRequest() {
        ChannelListRequest request = ChannelListRequest
                .builder()
                .uid(uid)
                .build();

        if (request.validateParams()) {
            Map<String, Object> paramsMap = CheckParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            String response = HttpUtil.createPost(HOST + rechargeBankListUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();
            System.err.println(response);
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }


    /**
     * @Author: george
     * describe: Obtain authorized collection bank list
     * 获取已授权-代收-银行列表bankCodeList
     **/
    public static void withdrawBankCodeListRequest() {
        ChannelListRequest request = ChannelListRequest
                .builder()
                .uid(uid)
                .build();

        if (request.validateParams()) {
            Map<String, Object> paramsMap = CheckParamsUtils.convertVerifySignParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            String response = HttpUtil.createPost(HOST + withdrawBankListUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();
            System.err.println(response);
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     *  describe: Collection Order - Status Inquiry
     *  代收订单-状态查询
     **/
    public static void sendRechargeOrderStatusRequest() {

        RechargeOrderRequest request = RechargeOrderRequest
                .builder()
                .uid(uid)
                .merchantOrderNo("1742958213205")
                .build();

        if (!request.validateParams()) {
            Map<String, Object> paramsMap = RechargeParamsUtils.convertVerifySignOrderStatusParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            System.err.println("request:{}" + paramsPost);

            String response = HttpUtil.createPost(HOST + rechargeOrderStatusUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();

            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     *  describe: Payment Order - Status Inquiry
     * 代付订单-状态查询
     **/
    public static void sendWithdrawOrderStatusRequest() {

        RechargeOrderRequest request = RechargeOrderRequest
                .builder()
                .uid(5588359)
                .merchantOrderNo("202503221812123")
                .build();

        if (!request.validateParams()) {
            Map<String, Object> paramsMap = RechargeParamsUtils.convertVerifySignOrderStatusParams(request);
            String signature = SignUtils.sign(paramsMap, accessSecret);
            paramsMap.put("signature", signature);
            JSONObject paramsPost = (JSONObject) JSONObject.toJSON(paramsMap);

            System.err.println("request:{}" + paramsPost);

            String response = HttpUtil.createPost(HOST + withdrawOrderStatusUrl)
                    .header("Content-Type", "application/json")
                    .body(paramsPost.toString())
                    .execute()
                    .body();
            System.err.println(response);
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
        }
    }

    /**
     * @Author: george
     *  describe: Exchange rate query
     * 汇率查询
     **/
    public static void exchangeRateRequest() {

        String response = HttpUtil.createPost(HOST + exchangeRateUrl)
                .header("Content-Type", "application/json")
                .body("{}")
                .execute()
                .body();
        System.err.println(response);
        JSONObject parse = (JSONObject) JSONObject.parse(response);
        Integer code = (Integer) parse.get("code");
        Boolean success = (Boolean) parse.get("success");
    }


}
