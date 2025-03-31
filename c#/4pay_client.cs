using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using Newtonsoft.Json;

namespace pay_client
{
    class Program
    {
        private const string contentType = "application/json";
        private const string payHost = "https://api.4pay.com";
        private const string accessKey = "123456";
        private const string createWithdrawOrderUrl = payHost + "/coin/pay/withdraw/order/create";
        private const string queryWithdrawOrderUrl = payHost + "/coin/pay/recharge/order/status";

        static void Main(string[] args)
        {
            // 示例代码，此处可以添加具体的逻辑
        }

        static string Sign(Dictionary<string, string> params, string accessKey)
        {
            string signStr = "";
            foreach (var kvp in params)
            {
                signStr += kvp.Key + "=" + kvp.Value + "&";
            }
            signStr = signStr.TrimEnd('&');
            signStr += accessKey;
            return signStr;
        }

        class WithdrawOrder
        {
            [JsonProperty("uid")]
            public string Uid { get; set; }

            [JsonProperty("merchant_order_no")]
            public string Merchant_order_no { get; set; }

            [JsonProperty("currency_coin_name")]
            public string Currency_coin_name { get; set; }

            [JsonProperty("channel_code")]
            public string Channel_code { get; set; }

            [JsonProperty("amount")]
            public string Amount { get; set; }

            [JsonProperty("bank_code")]
            public string Bank_code { get; set; }

            [JsonProperty("memo")]
            public string Memo { get; set; }

            public Dictionary<string, string> ToRequest()
            {
                var params = new Dictionary<string, string>();
                // 此处可以添加具体的逻辑
                return params;
            }
        }

        class QueryWithdrawOrderResponse
            {
                [JsonProperty("merchantOrderNo")]
                public string MerchantOrderNo { get; set; }

                [JsonProperty("uid")]
                public string Uid { get; set; }

                [JsonProperty("status")]
                public string Status { get; set; }

                [JsonProperty("realAmount")]
                public string RealAmount { get; set; }

                [JsonProperty("orderAmount")]
                public string OrderAmount { get; set; }

                [JsonProperty("fee")]
                public string Fee { get; set; }

                [JsonProperty("recordId")]
                public string RecordId { get; set; }
            }

            class CreateWithdrawOrderResponse
            {
                [JsonProperty("uid")]
                public string Uid { get; set; }

                [JsonProperty("merchantOrderNo")]
                public string MerchantOrderNo { get; set; }

                [JsonProperty("recordId")]
                public string RecordId { get; set; }

                [JsonProperty("amount")]
                public string Amount { get; set; }

                [JsonProperty("url")]
                public string Url { get; set; }

                [JsonProperty("qrcode")]
                public string Qrcode { get; set; }

                [JsonProperty("bankId")]
                public string BankId { get; set; }

                [JsonProperty("accountNumber")]
                public string AccountNumber { get; set; }

                [JsonProperty("bankOwner")]
                public string BankOwner { get; set; }
            }

         static async Task<CreateWithdrawOrderResponse> CreateWithdrawOrder(WithdrawOrder w)
                {
                    params := w.toRequest();
                    params["sign"] = sign(params, accessKey);

                    var res = new CreateWithdrawOrderResponse();
                    var error = await postJSON(createWithdrawOrderUrl, params, res);
                    if (error != null)
                    {
                        return null, error;
                    }
                    return res, null;
                }

         static async Task<QueryWithdrawOrderResponse> QueryWithdrawOrder(QueryWithdrawOrder w)
                 {
                     params := w.toRequest();
                     params["sign"] = sign(params, accessKey);

                     var res = new QueryWithdrawOrderResponse();
                     var error = await postJSON(queryWithdrawOrderUrl, params, res);
                     if (error != null)
                     {
                         return null, error;
                     }
                     return res, null;
                 }
        static async Task PostJSON(string url, object data, object result)
                {
                    using (var httpClient = new HttpClient())
                    {
                        var jsonData = JsonConvert.SerializeObject(data);
                        var content = new StringContent(jsonData, Encoding.UTF8, "application/json");

                        var response = await httpClient.PostAsync(url, content);
                        if (response.IsSuccessStatusCode)
                        {
                            var responseContent = await response.Content.ReadAsStringAsync();
                            JsonConvert.PopulateObject(responseContent, result);
                        }
                        else
                        {
                            throw new Exception($"HTTP request failed with status code: {response.StatusCode}");
                        }
                    }
                }

    }
}
