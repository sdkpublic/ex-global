package _go

import (
	"bytes"
	"encoding/json"
	"net/http"
)

const (
	contentType            = "application/json"
	payHost                = "https://api.4pay.com"
	accessKey              = "123456"
	createWithdrawOrderUrl = payHost + "/coin/pay/withdraw/order/create"
	queryWithdrawOrderUrl  = payHost + "/coin/pay/recharge/order/status"
)

func sign(params map[string]string, accessKey string) string {
	signStr := ""
	for k, v := range params {
		signStr += k + "=" + v + "&"
	}
	signStr = signStr[:len(signStr)-1]
	signStr += accessKey
	return signStr
}

type WithdrawOrder struct {
	Uid                string `json:"uid"`
	Merchant_order_no  string `json:"merchant_order_no"`
	Currency_coin_name string `json:"currency_coin_name"`
	Channel_code       string `json:"channel_code"`
	Amount             string `json:"amount"`
	Bank_code          string `json:"bank_code"`
	Memo               string `json:"memo"`
}

func (w *WithdrawOrder) toRequest() map[string]string {
	params := make(map[string]string)
	params["uid"] = w.Uid
	params["merchantOrderNo"] = w.Merchant_order_no
	params["currencyCoinName"] = w.Currency_coin_name
	params["channelCode"] = w.Channel_code
	params["amount"] = w.Amount
	params["bankCode"] = w.Bank_code
	params["memo"] = w.Memo
	return params
}

type QueryWithdrawOrder struct {
	Uid               string `json:"uid"`
	Merchant_order_no string `json:"merchant_order_no"`
}

func (w *QueryWithdrawOrder) toRequest() map[string]string {
	params := make(map[string]string)
	params["uid"] = w.Uid
	params["merchantOrderNo"] = w.Merchant_order_no
	return params
}

type BaseResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Data    any    `json:"data"`
}

type CreateWithdrawOrderResponse struct {
	Uid             string `json:"uid"`
	MerchantOrderNo string `json:"merchantOrderNo"`
	RecordId        string `json:"recordId"`
	Amount          string `json:"amount"`
	Url             string `json:"url"`
	Qrcode          string `json:"qrcode"`
	BankId          string `json:"bankId"`
	AccountNumber   string `json:"accountNumber"`
	BankOwner       string `json:"bankOwner"`
}

type QueryWithdrawOrderResponse struct {
	MerchantOrderNo string `json:"merchantOrderNo"`
	Uid             string `json:"uid"`
	Status          string `json:"status"`
	RealAmount      string `json:"realAmount"`
	OrderAmount     string `json:"orderAmount"`
	Fee             string `json:"fee"`
	RecordId        string `json:"recordId"`
}

func (w *WithdrawOrder) CreateWithdrawOrder() (*CreateWithdrawOrderResponse, error) {
	params := w.toRequest()
	params["sign"] = sign(params, accessKey)

	var res CreateWithdrawOrderResponse
	err := postJSON(createWithdrawOrderUrl, params, &res)
	if err != nil {
		return nil, err
	}
	return &res, nil
}

func (w *QueryWithdrawOrder) QueryWithdrawOrder() (*QueryWithdrawOrderResponse, error) {
	params := w.toRequest()
	params["sign"] = sign(params, accessKey)

	var res QueryWithdrawOrderResponse
	err := postJSON(queryWithdrawOrderUrl, params, &res)
	if err != nil {
		return nil, err
	}
	return &res, nil
}

func postJSON(url string, data any, result any) error {
	jsonData, err := json.Marshal(data)
	if err != nil {
		return err
	}
	resp, err := http.Post(url, contentType, bytes.NewBuffer(jsonData))
	if err != nil {
		return err
	}
	defer resp.Body.Close()
	return json.NewDecoder(resp.Body).Decode(result)
}
