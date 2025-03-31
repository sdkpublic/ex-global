import hashlib
from typing import Dict, Final

import requests


def sign(params: Dict[str, str], access_key: str) -> str:
    """
    Calculates a signature for given params and access key.

    :param params: A dictionary of parameters to sign
    :param access_key: The key to use for signing
    :return: A signature as a hexadecimal string
    """
    return hashlib.md5(
        "&".join(f"{k}={v}" for k, v in sorted(params.items()))
        .encode()
        + access_key.encode()
    ).hexdigest()


pay_host = "https://post.coinpay.io"
access_key = "654321"

create_withdraw_order_url: Final[str] = f"{pay_host}/coin/pay/withdraw/order/create"
query_withdraw_order_url: Final[str] = f"{pay_host}/ /coin/pay/recharge/order/status"


class WithdrawOrder:
    def __init__(self, uid: int, merchant_order_no: str, currency_coin_name: str, channel_code: str, amount: float,
                 bank_code: str, memo: str):
        self.uid = uid
        self.merchant_order_no = merchant_order_no
        self.currency_coin_name = currency_coin_name
        self.channel_code = channel_code
        self.amount = amount
        self.bank_code = bank_code
        self.memo = memo

    def to_dict(self):
        return {
            "uid": self.uid,
            "merchantOrderNo": self.merchant_order_no,
            "currencyCoinName": self.currency_coin_name,
            "channelCode": self.channel_code,
            "amount": self.amount,
            "bankCode": self.bank_code,
            "memo": self.memo
        }

class QueryWithdrawOrder:
    def __init__(self, uid: int, merchant_order_no: str):
        self.uid = uid
        self.merchant_order_no = merchant_order_no
    def to_dict(self):
        return {
            "uid": self.uid,
            "merchantOrderNo": self.merchant_order_no,
        }

class BaseResponse:
    def __init__(self, code: int, msg: str, data: Dict[str, str]):
        self.code = code
        self.msg = msg
        self.data = data

    def __str__(self):
        return f"BaseResponse(code={self.code}, msg={self.msg})"


class WithdrawOrderResponse(BaseResponse):
    def __init__(self, code: int, msg: str, data: Dict[str, str]):
        super().__init__(code, msg, data)
        self.uid = data.get("uid")
        self.merchant_order_no = data.get("merchantOrderNo")
        self.record_id = data.get("recordId")
        self.amount = data.get("amount")
        self.url = data.get("url")
        self.qrcode = data.get("qrcode")
        self.bank_id = data.get("bankId")
        self.account_number = data.get("accountNumber")
        self.bank_owner = data.get("bankOwner")

    def __str__(self):
        return f"WithdrawOrderResponse(code={self.code}, msg={self.msg}, uid={self.uid}, merchant_order_no={self.merchant_order_no}, record_id={self.record_id}, amount={self.amount}, url={self.url}, qrcode={self.qrcode}, bank_id={self.bank_id}, account_number={self.account_number}, bank_owner={self.bank_owner})"

class QueryWithdrawOrderResponse(BaseResponse):
    def __init__(self, code: int, msg: str, data: Dict[str, str]):
        super().__init__(code, msg, data)
        self.merchant_order_no = data.get("merchantOrderNo")
        self.uid = data.get("uid")
        self.status = data.get("status")
        self.real_amount = data.get("realAmount")
        self.order_amount = data.get("orderAmount")
        self.fee = data.get("fee")
        self.record_id = data.get("recordId")

    def __str__(self):
        return f"QueryWithdrawOrderResponse(code={self.code}, msg={self.msg}, merchant_order_no={self.merchant_order_no}, uid={self.uid}, status={self.status}, real_amount={self.real_amount}, order_amount={self.order_amount}, fee={self.fee}, record_id={self.record_id})"

# 创建订单
def create_withdraw_order(withdraw_order: WithdrawOrder) -> WithdrawOrderResponse:
    params = withdraw_order.to_dict()
    params["sign"] = sign(params, access_key)
    with requests.Session() as session:
        response = session.post(create_withdraw_order_url, data=params)
    data: Dict[str, any] = response.json()
    return WithdrawOrderResponse(data["code"], data["msg"], data["data"])

# 查询订单
def query_withdraw_order(query_order: QueryWithdrawOrder) -> QueryWithdrawOrderResponse:
    """
    Queries the withdrawal order.

    :param query_order: The order to query, as a QueryWithdrawOrder instance.
    :return: The response for the query, as a QueryWithdrawOrderResponse instance.
    """
    params: Dict[str, str] = query_order.to_dict()
    params["sign"] = sign(params, access_key)
    with requests.Session() as s:
        response: requests.Response = s.post(query_withdraw_order_url, data=params)
    data: Dict[str, any] = response.json()
    return QueryWithdrawOrderResponse(data["code"], data["msg"], data["data"])