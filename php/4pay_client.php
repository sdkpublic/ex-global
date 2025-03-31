<?php

// 引入 hashlib 库
require_once 'path/to/hashlib.php';

// 定义常量
define('PAY_HOST', 'https://post.coinpay.io');
define('ACCESS_KEY', '654321');

// 定义 URL
$create_withdraw_order_url = PAY_HOST . '/coin/pay/withdraw/order/create';
$query_withdraw_order_url = PAY_HOST . '/coin/pay/recharge/order/status';

// 定义 WithdrawOrder 类
class WithdrawOrder
{
    private $uid;
    private $merchant_order_no;
    private $currency_coin_name;
    private $channel_code;
    private $amount;
    private $bank_code;
    private $memo;

    public function __construct($uid, $merchant_order_no, $currency_coin_name, $channel_code, $amount, $bank_code, $memo)
    {
        $this->uid = $uid;
        $this->merchant_order_no = $merchant_order_no;
        $this->currency_coin_name = $currency_coin_name;
        $this->channel_code = $channel_code;
        $this->amount = $amount;
        $this->bank_code = $bank_code;
        $this->memo = $memo;
    }

    public function toArray()
    {
        return [
            'uid' => $this->uid,
            'merchantOrderNo' => $this->merchant_order_no,
            'currencyCoinName' => $this->currency_coin_name,
            'channelCode' => $this->channel_code,
            'amount' => $this->amount,
            'bankCode' => $this->bank_code,
            'memo' => $this->memo
        ];
    }
}

// 定义 QueryWithdrawOrder 类
class QueryWithdrawOrder
{
    private $uid;
    private $merchant_order_no;

    public function __construct($uid, $merchant_order_no)
    {
        $this->uid = $uid;
        $this->merchant_order_no = $merchant_order_no;
    }

    public function toArray()
    {
        return [
            'uid' => $this->uid,
            'merchantOrderNo' => $this->merchant_order_no
        ];
    }
}

// 定义 BaseResponse 类
class BaseResponse
{
    public $code;
    public $msg;
    public $data;

    public function __construct($code, $msg, $data)
    {
        $this->code = $code;
        $this->msg = $msg;
        $this->data = $data;
    }

    public function __toString()
    {
        return "BaseResponse(code={$this->code}, msg={$this->msg})";
    }
}

// 定义 WithdrawOrderResponse 类
class WithdrawOrderResponse extends BaseResponse
{
    public $uid;
    public $merchant_order_no;
    public $record_id;
    public $amount;
    public $url;
    public $qrcode;
    public $bank_id;
    public $account_number;
    public $bank_owner;

    public function __construct($code, $msg, $data)
    {
        parent::__construct($code, $msg, $data);
        $this->uid = $data['uid'];
        $this->merchant_order_no = $data['merchantOrderNo'];
        $this->record_id = $data['recordId'];
        $this->amount = $data['amount'];
        $this->url = $data['url'];
        $this->qrcode = $data['qrcode'];
        $this->bank_id = $data['bankId'];
        $this->account_number = $data['accountNumber'];
        $this->bank_owner = $data['bankOwner'];
    }

    public function __toString()
    {
        return "WithdrawOrderResponse(code={$this->code}, msg={$this->msg}, uid={$this->uid}, merchant_order_no={$this->merchant_order_no}, record_id={$this->record_id}, amount={$this->amount}, url={$this->url}, qrcode={$this->qrcode}, bank_id={$this->bank_id}, account_number={$this->account_number}, bank_owner={$this->bank_owner})";
    }
}

// 定义 QueryWithdrawOrderResponse 类
class QueryWithdrawOrderResponse extends BaseResponse
{
    public $merchant_order_no;
    public $uid;
    public $status;
    public $real_amount;
    public $order_amount;
    public $fee;
    public $record_id;

    public function __construct($code, $msg, $data)
    {
        parent::__construct($code, $msg, $data);
        $this->merchant_order_no = $data['merchantOrderNo'];
        $this->uid = $data['uid'];
        $this->status = $data['status'];
        $this->real_amount = $data['realAmount'];
        $this->order_amount = $data['orderAmount'];
        $this->fee = $data['fee'];
        $this->record_id = $data['recordId'];
    }

    public function __toString()
    {
        return "QueryWithdrawOrderResponse(code={$this->code}, msg={$this->msg}, merchant_order_no={$this->merchant_order_no}, uid={$this->uid}, status={$this->status}, real_amount={$this->real_amount}, order_amount={$this->order_amount}, fee={$this->fee}, record_id={$this->record_id})";
    }
}

// 创建订单
function create_withdraw_order($withdraw_order)
{
    $params = $withdraw_order->toArray();
    $params['sign'] = sign($params, ACCESS_KEY);
    $response = postJSON($create_withdraw_order_url, $params);
    $data = json_decode($response, true);
    return new WithdrawOrderResponse($data['code'], $data['msg'], $data['data']);
}

// 查询订单
function query_withdraw_order($query_order)
{
    $params = $query_order->toArray();
    $params['sign'] = sign($params, ACCESS_KEY);
    $response = postJSON($query_withdraw_order_url, $params);
    $data = json_decode($response, true);
    return new QueryWithdrawOrderResponse($data['code'], $data['msg'], $data['data']);
}

// 计算签名
function sign($params, $access_key)
{
    ksort($params);
    $str = '';
    foreach ($params as $k => $v) {
        $str .= $k . '=' . $v . '&';
    }
    $str .= 'key=' . $access_key;
    return md5($str);
}

// 发送 POST 请求
function postJSON($url, $data)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}