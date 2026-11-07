# WorldPay 2.0 · App 端接口文档

> RESTful 风格 · 统一前缀 `/card/api` · 仅包含 App 端接口，共 **221** 个，覆盖 37 个功能模块。

> 本文档由服务端 Controller 源码与实体字段注释自动生成，字段说明与代码保持一致。

---

## 通用规范

### 1. BASE URL

```
线上：https://<host>/worldpay
本地：http://localhost:8023/worldpay
```

### 2. 请求头

| Header | 必填 | 说明 |
|---|---|---|
| `token` | 需鉴权接口必填 | JWT 令牌，格式 `Bearer <jwt>` |
| `language` | 否 | 语言：`en`（默认）/ `zh` / `zh_TW` |
| `Content-Type` | 是（有请求体时） | `application/json` |

### 3. 请求与响应格式

**响应**：统一返回明文 JSON（**不再做 AES 加密**）：

```json
{ "rtncode": 200, "msg": "success", "data": { } }
```

- `rtncode = 200` 成功，`500` 业务失败，`403` 未登录 / 无权限；`msg` 为提示信息；`data` 为业务数据

**请求体**：支持两种方式：

1. 直接提交明文 JSON（推荐）
2. 加密提交（兼容旧客户端），格式为：

```json
{ "data": "<AES(Base64)>" }
```

- 算法：`AES/ECB/PKCS5Padding`，密钥 16 字节字符串（向服务端索取）
- `GET` 请求无请求体，参数放在 query / path 中

**不支持加密的接口**（只能传明文 JSON）：

- `/card/api/sign/encryption`
- `/card/api/sign/decryption`
- `/card/api/webhook/reset-addres`
- `/card/api/webhook/agent-notify`
- `/card/api/wallet/topin-usdt-notify`
- `/card/api/file/upload-image`
- `/card/api/file/api-upload-image`

### 4. 免鉴权接口

注册、登录、谷歌验证码、邮箱验证码、忘记密码、配置信息、App 版本、签名、Webhook、链上充值回调。

### 5. 完整请求示例

```http
POST /worldpay/card/api/app-user/login
Content-Type: application/json
language: en

{ "userEmail": "a@b.com", "userPassword": "***" }
```

响应：

```json
{ "rtncode": 200, "msg": "success", "data": { "token": "Bearer eyJ..." } }
```

---

## 模块索引

| 模块 | 路径 | 接口数 | 端 |
|---|---|---:|---|
| <a href="#mod-app-user-card">App 用户卡</a> | `/card/api/app-user-card` | 12 | App |
| <a href="#mod-app-user">App 用户</a> | `/card/api/app-user` | 16 | App |
| <a href="#mod-app-user-intiter">App 用户邀请</a> | `/card/api/app-user-intiter` | 4 | App |
| <a href="#mod-application-record">申请记录</a> | `/card/api/application-record` | 5 | App |
| <a href="#mod-apply-pay">支付申请</a> | `/card/api/apply-pay` | 1 | App |
| <a href="#mod-brokerage-log">佣金流水</a> | `/card/api/brokerage-log` | 3 | App |
| <a href="#mod-card-apply">开卡申请</a> | `/card/api/card-apply` | 6 | App |
| <a href="#mod-card-close">销卡</a> | `/card/api/card-close` | 1 | App |
| <a href="#mod-card">卡片</a> | `/card/api/card` | 19 | App |
| <a href="#mod-card-label">卡标签</a> | `/card/api/card-label` | 4 | App |
| <a href="#mod-card-synopsis">卡简介</a> | `/card/api/card-synopsis` | 5 | App |
| <a href="#mod-card-banner">卡 Banner</a> | `/card/api/card-banner` | 7 | App |
| <a href="#mod-cardholder">持卡人</a> | `/card/api/cardholder` | 5 | App |
| <a href="#mod-channel-user">渠道用户</a> | `/card/api/channel-user` | 10 | App |
| <a href="#mod-sign">签名 / 加解密</a> | `/card/api/sign` | 2 | App |
| <a href="#mod-dic">字典</a> | `/card/api/dic` | 14 | App |
| <a href="#mod-email-template">邮件模板</a> | `/card/api/email-template` | 5 | App |
| <a href="#mod-file">文件上传</a> | `/card/api/file` | 2 | App |
| <a href="#mod-forecast">预测 / 回调</a> | `/card/api/forecast` | 5 | App |
| <a href="#mod-help">帮助中心</a> | `/card/api/help` | 6 | App |
| <a href="#mod-insights">消费洞察</a> | `/card/api/insights` | 1 | App |
| <a href="#mod-ios-goods">iOS 商品</a> | `/card/api/ios-goods` | 8 | App |
| <a href="#mod-kyc">KYC 认证</a> | `/card/api/kyc` | 1 | App |
| <a href="#mod-merchant">商户</a> | `/card/api/merchant` | 2 | App |
| <a href="#mod-message-template">消息模板</a> | `/card/api/message-template` | 5 | App |
| <a href="#mod-notice">公告</a> | `/card/api/notice` | 7 | App |
| <a href="#mod-product">产品</a> | `/card/api/product` | 11 | App |
| <a href="#mod-product-type">产品类型</a> | `/card/api/product-type` | 5 | App |
| <a href="#mod-info">系统信息 / 配置</a> | `/card/api/info` | 7 | App |
| <a href="#mod-toll-address">收费地址</a> | `/card/api/toll-address` | 5 | App |
| <a href="#mod-app-user-sys">App 用户（后台管理）</a> | `/card/api/app-user-sys` | 11 | App |
| <a href="#mod-wallet">钱包</a> | `/card/api/wallet` | 10 | App |
| <a href="#mod-wallet-entrance">钱包入口</a> | `/card/api/wallet-entrance` | 2 | App |
| <a href="#mod-wallet-network-top-up">链上充值</a> | `/card/api/wallet-network-top-up` | 3 | App |
| <a href="#mod-wallet-to-web-log">钱包流水</a> | `/card/api/wallet-to-web-log` | 6 | App |
| <a href="#mod-withdrawal-rates">提现费率</a> | `/card/api/withdrawal-rates` | 2 | App |
| <a href="#mod-wallet-transfet-rates">钱包转账费率</a> | `/card/api/wallet-transfet-rates` | 3 | App |

---

<a id="mod-app-user-card"></a>
## App 用户卡 · `app-user-card`

接口数：**12** ｜ 端：**App** ｜ 基础路径：`/card/api/app-user-card`

### 1. 查询用户卡资产 · findUserCardAssets

```http
GET /card/api/app-user-card/user-card-assets
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询用户卡片列表 · findUserCardList

```http
POST /card/api/app-user-card/user-card-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserCardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserCardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `applyId` | Integer | — |
| `cardType` | String | — |
| `cardNo` | String | — |
| `userBankcardId` | Integer | — |
| `status` | String | — |
| `balance` | Double | — |
| `tag` | String | — |
| `shippingState` | Integer | — |
| `shippingStateName` | String | — |
| `shippingTime` | Date | — |
| `logisticsNum` | String | — |
| `pinNum` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardData` | CardEntity | — |
| `logisticsInfo` | Object | — |
| `holderData` | AppUserHolderEntity | — |
| `manData` | CardApplyManEntity | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `userName` | String | — |
| `statusId` | Integer | — |
| `stateName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询用户卡激活列表 · findUserCardActivationList

```http
POST /card/api/app-user-card/user-card-activation-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserCardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserCardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `applyId` | Integer | — |
| `cardType` | String | — |
| `cardNo` | String | — |
| `userBankcardId` | Integer | — |
| `status` | String | — |
| `balance` | Double | — |
| `tag` | String | — |
| `shippingState` | Integer | — |
| `shippingStateName` | String | — |
| `shippingTime` | Date | — |
| `logisticsNum` | String | — |
| `pinNum` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardData` | CardEntity | — |
| `logisticsInfo` | Object | — |
| `holderData` | AppUserHolderEntity | — |
| `manData` | CardApplyManEntity | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `userName` | String | — |
| `statusId` | Integer | — |
| `stateName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 后台查询用户卡片列表 · pcFindUserCardList

```http
POST /card/api/app-user-card/pc-find-user-card-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserCardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserCardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `applyId` | Integer | — |
| `cardType` | String | — |
| `cardNo` | String | — |
| `userBankcardId` | Integer | — |
| `status` | String | — |
| `balance` | Double | — |
| `tag` | String | — |
| `shippingState` | Integer | — |
| `shippingStateName` | String | — |
| `shippingTime` | Date | — |
| `logisticsNum` | String | — |
| `pinNum` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardData` | CardEntity | — |
| `logisticsInfo` | Object | — |
| `holderData` | AppUserHolderEntity | — |
| `manData` | CardApplyManEntity | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `userName` | String | — |
| `statusId` | Integer | — |
| `stateName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询用户卡片信息 · findUserCardInfo

```http
GET /card/api/app-user-card/user-card-info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 查询交易 · findTransaction

```http
POST /card/api/app-user-card/transaction
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardTransactionEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardTransactionEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `userBankcardId` | Integer | — |
| `cardNo` | String | — |
| `handlingFees` | Double | — |
| `localCurrency` | String | — |
| `localCurrencyAmt` | String | — |
| `transCurrency` | String | — |
| `transCurrencyAmt` | String | — |
| `requestOrderId` | String | — |
| `orderState` | String | — |
| `payType` | Integer | — |
| `transType` | String | — |
| `createAt` | Date | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `title` | String | — |
| `userEmail` | String | — |
| `cardData` | CardEntity | — |
| `orderStateName` | String | — |
| `totalManey` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 查询卡片 CVV · findCardCvv

```http
POST /card/api/app-user-card/card-cvv
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | PublicEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `PublicEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 查询卡片有效期 · findCardExpirationTime

```http
POST /card/api/app-user-card/card-expiration-time
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | PublicEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `PublicEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 注销卡片 · closeCard

```http
POST /card/api/app-user-card/close-card
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardCloseEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardCloseEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `cardType` | String | — |
| `cardNo` | String | — |
| `userBankcardId` | Integer | — |
| `balance` | Double | — |
| `refundAmt` | Double | — |
| `requestOrderId` | String | — |
| `reviewStatus` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `payPassword` | String | — |
| `appUserData` | AppUserEntity | — |
| `reviewStatusName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 更新卡标签 · upTag

```http
POST /card/api/app-user-card/up-tag
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | UserCardTagEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `UserCardTagEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `userBankcardId` | Integer | — |
| `tag` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 设置卡片 PIN 码 · setPin

```http
POST /card/api/app-user-card/set-pin
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardPinReqEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardPinReqEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `userEmail` | String | — |
| `uid` | String | — |
| `pin` | String | — |
| `userBankcardId` | Integer | — |
| `emailCode` | String | — |
| `payPassword` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 12. 解冻 · unfreeze

```http
GET /card/api/app-user-card/unfreeze
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-app-user"></a>
## App 用户 · `app-user`

接口数：**16** ｜ 端：**App** ｜ 基础路径：`/card/api/app-user`

### 1. 用户注册（通用版） · signUp

```http
POST /card/api/app-user/sign-up
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | 主键 |
| `uid` | String | 三方 UID（UCard 返回） |
| `userEmail` | String | 用户邮箱（登录账号） |
| `userPassword` | String | 登录密码（MD5 哈希，兼容旧版） |
| `mobileNumber` | String | 手机号 |
| `mobilePrefix` | String | 手机区号 |
| `userState` | Integer | 用户状态：1-正常，其他-冻结 |
| `payPassword` | String | 支付密码（MD5 哈希） |
| `googleSecretkey` | String | Google Authenticator 密钥 |
| `kycState` | Integer | KYC 状态 |
| `kycStateName` | String | KYC 状态名称 |
| `kycAuditResult` | String | kyc申请结果描述 |
| `invitationCode` | String | 邀请码 |
| `toInvitationCode` | String | 被邀请人的邀请码 |
| `walletBalance` | Double | 钱包可用余额 |
| `freezeBalance` | Double | 冻结资产 |
| `openFreezeBalance` | Double | 开卡临时冻结 |
| `withdrawableBalance` | Double | 可提现余额（佣金） |
| `walletState` | Boolean | 钱包入口是否开启 |
| `registrationId` | String | Jpush三方id |
| `tronUsdtAcount` | String | tron链usdt充值地址 |
| `cid` | String | 推送设备 ID |
| `deviceName` | String | 登录设备名称 |
| `setTime` | Date | 创建时间（兼容旧字段 setTime） |
| `gmtModified` | Date | 修改时间（兼容旧字段 gmtModified） |
| `googleState` | Boolean | 前端判断是否设置了 Google 验证 |
| `googleCode` | String | 谷歌验证码 |
| `payPasswordState` | Boolean | 前端判断是否设置了支付密码 |
| `invitationLable` | Integer | 邀请人标签 |
| `bandingChannel` | Boolean | 是否已绑定渠道商 |
| `bandingChannelUid` | String | 绑定的渠道商 UID |
| `iosWalledState` | Integer | iOS 钱包入口状态 |
| `iosTopUpState` | Integer | iOS USDT 充值入口状态 |
| `emailCode` | String | 邮箱验证码（非数据库字段） |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 用户注册（iOS V2 版，含钱包/充值开关） · signUpV2

```http
POST /card/api/app-user
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | 主键 |
| `uid` | String | 三方 UID（UCard 返回） |
| `userEmail` | String | 用户邮箱（登录账号） |
| `userPassword` | String | 登录密码（MD5 哈希，兼容旧版） |
| `mobileNumber` | String | 手机号 |
| `mobilePrefix` | String | 手机区号 |
| `userState` | Integer | 用户状态：1-正常，其他-冻结 |
| `payPassword` | String | 支付密码（MD5 哈希） |
| `googleSecretkey` | String | Google Authenticator 密钥 |
| `kycState` | Integer | KYC 状态 |
| `kycStateName` | String | KYC 状态名称 |
| `kycAuditResult` | String | kyc申请结果描述 |
| `invitationCode` | String | 邀请码 |
| `toInvitationCode` | String | 被邀请人的邀请码 |
| `walletBalance` | Double | 钱包可用余额 |
| `freezeBalance` | Double | 冻结资产 |
| `openFreezeBalance` | Double | 开卡临时冻结 |
| `withdrawableBalance` | Double | 可提现余额（佣金） |
| `walletState` | Boolean | 钱包入口是否开启 |
| `registrationId` | String | Jpush三方id |
| `tronUsdtAcount` | String | tron链usdt充值地址 |
| `cid` | String | 推送设备 ID |
| `deviceName` | String | 登录设备名称 |
| `setTime` | Date | 创建时间（兼容旧字段 setTime） |
| `gmtModified` | Date | 修改时间（兼容旧字段 gmtModified） |
| `googleState` | Boolean | 前端判断是否设置了 Google 验证 |
| `googleCode` | String | 谷歌验证码 |
| `payPasswordState` | Boolean | 前端判断是否设置了支付密码 |
| `invitationLable` | Integer | 邀请人标签 |
| `bandingChannel` | Boolean | 是否已绑定渠道商 |
| `bandingChannelUid` | String | 绑定的渠道商 UID |
| `iosWalledState` | Integer | iOS 钱包入口状态 |
| `iosTopUpState` | Integer | iOS USDT 充值入口状态 |
| `emailCode` | String | 邮箱验证码（非数据库字段） |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 用户登录 · login

```http
POST /card/api/app-user/login
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | 主键 |
| `uid` | String | 三方 UID（UCard 返回） |
| `userEmail` | String | 用户邮箱（登录账号） |
| `userPassword` | String | 登录密码（MD5 哈希，兼容旧版） |
| `mobileNumber` | String | 手机号 |
| `mobilePrefix` | String | 手机区号 |
| `userState` | Integer | 用户状态：1-正常，其他-冻结 |
| `payPassword` | String | 支付密码（MD5 哈希） |
| `googleSecretkey` | String | Google Authenticator 密钥 |
| `kycState` | Integer | KYC 状态 |
| `kycStateName` | String | KYC 状态名称 |
| `kycAuditResult` | String | kyc申请结果描述 |
| `invitationCode` | String | 邀请码 |
| `toInvitationCode` | String | 被邀请人的邀请码 |
| `walletBalance` | Double | 钱包可用余额 |
| `freezeBalance` | Double | 冻结资产 |
| `openFreezeBalance` | Double | 开卡临时冻结 |
| `withdrawableBalance` | Double | 可提现余额（佣金） |
| `walletState` | Boolean | 钱包入口是否开启 |
| `registrationId` | String | Jpush三方id |
| `tronUsdtAcount` | String | tron链usdt充值地址 |
| `cid` | String | 推送设备 ID |
| `deviceName` | String | 登录设备名称 |
| `setTime` | Date | 创建时间（兼容旧字段 setTime） |
| `gmtModified` | Date | 修改时间（兼容旧字段 gmtModified） |
| `googleState` | Boolean | 前端判断是否设置了 Google 验证 |
| `googleCode` | String | 谷歌验证码 |
| `payPasswordState` | Boolean | 前端判断是否设置了支付密码 |
| `invitationLable` | Integer | 邀请人标签 |
| `bandingChannel` | Boolean | 是否已绑定渠道商 |
| `bandingChannelUid` | String | 绑定的渠道商 UID |
| `iosWalledState` | Integer | iOS 钱包入口状态 |
| `iosTopUpState` | Integer | iOS USDT 充值入口状态 |
| `emailCode` | String | 邮箱验证码（非数据库字段） |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 获取谷歌验证二维码 · googleCode

```http
GET /card/api/app-user/google-code
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `email` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询当前登录 Token · findToken

```http
GET /card/api/app-user/token
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 查询当前登录 Token（iOS V2） · findTokenV2

```http
GET /card/api/app-user
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 发送邮箱验证码 · sendEmailCode

```http
GET /card/api/app-user/send-email-code
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userEmail` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 校验邮箱验证码 · checkEmailCode

```http
GET /card/api/app-user/email-code
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userEmail` | query | String | — | — |
| `emailCode` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 查询当前用户信息 · userinfo

```http
GET /card/api/app-user/userinfo
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 校验谷歌密钥 · checkGoogleSecretkey

```http
GET /card/api/app-user/google-secretkey
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `googleCode` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 查询可提现金额 · cashAmount

```http
GET /card/api/app-user/cash-amount
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 12. 提现到钱包 · withdrawalToWallet

```http
POST /card/api/app-user/withdrawal-to-wallet
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | AppUserEntity | 是 | 见下方字段表 |

**请求体字段 · `AppUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | 主键 |
| `uid` | String | 三方 UID（UCard 返回） |
| `userEmail` | String | 用户邮箱（登录账号） |
| `userPassword` | String | 登录密码（MD5 哈希，兼容旧版） |
| `mobileNumber` | String | 手机号 |
| `mobilePrefix` | String | 手机区号 |
| `userState` | Integer | 用户状态：1-正常，其他-冻结 |
| `payPassword` | String | 支付密码（MD5 哈希） |
| `googleSecretkey` | String | Google Authenticator 密钥 |
| `kycState` | Integer | KYC 状态 |
| `kycStateName` | String | KYC 状态名称 |
| `kycAuditResult` | String | kyc申请结果描述 |
| `invitationCode` | String | 邀请码 |
| `toInvitationCode` | String | 被邀请人的邀请码 |
| `walletBalance` | Double | 钱包可用余额 |
| `freezeBalance` | Double | 冻结资产 |
| `openFreezeBalance` | Double | 开卡临时冻结 |
| `withdrawableBalance` | Double | 可提现余额（佣金） |
| `walletState` | Boolean | 钱包入口是否开启 |
| `registrationId` | String | Jpush三方id |
| `tronUsdtAcount` | String | tron链usdt充值地址 |
| `cid` | String | 推送设备 ID |
| `deviceName` | String | 登录设备名称 |
| `setTime` | Date | 创建时间（兼容旧字段 setTime） |
| `gmtModified` | Date | 修改时间（兼容旧字段 gmtModified） |
| `googleState` | Boolean | 前端判断是否设置了 Google 验证 |
| `googleCode` | String | 谷歌验证码 |
| `payPasswordState` | Boolean | 前端判断是否设置了支付密码 |
| `invitationLable` | Integer | 邀请人标签 |
| `bandingChannel` | Boolean | 是否已绑定渠道商 |
| `bandingChannelUid` | String | 绑定的渠道商 UID |
| `iosWalledState` | Integer | iOS 钱包入口状态 |
| `iosTopUpState` | Integer | iOS USDT 充值入口状态 |
| `emailCode` | String | 邮箱验证码（非数据库字段） |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 13. 查询 KYC 认证状态 · findKycState

```http
GET /card/api/app-user/kyc-state
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 14. KYC 认证重试 · kycRetry

```http
GET /card/api/app-user/kyc-retry
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 15. 查询用户地址 · findAddress

```http
GET /card/api/app-user/address
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 16. KYC 全部重试（后台） · kycRetryAll

```http
GET /card/api/app-user/kyc-retry-all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-app-user-intiter"></a>
## App 用户邀请 · `app-user-intiter`

接口数：**4** ｜ 端：**App** ｜ 基础路径：`/card/api/app-user-intiter`

### 1. 查询App 用户邀请列表 · findAll

```http
POST /card/api/app-user-intiter/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserIntiterEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserIntiterEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `intiterUserType` | Integer | — |
| `intiterUserLevel` | Integer | — |
| `InviterUser` | Integer | — |
| `InviterUserUid` | String | — |
| `coverInviterUser` | Integer | — |
| `coverInviterUserUid` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `email` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询渠道邀请验证码按用户ID · findChannelInvitationCodeByUserId

```http
GET /card/api/app-user-intiter/channel-invitation-code-by-user-id/{userId}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userId` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询App 用户邀请详情 · findById

```http
GET /card/api/app-user-intiter/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 新增App用户邀请 · insertAppUserIntiter

```http
POST /card/api/app-user-intiter/app-user-intiter
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userIntiterEntity` | query/form | UserIntiterEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `UserIntiterEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `coverInviterUser` | String | — |
| `inviterUser` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-application-record"></a>
## 申请记录 · `application-record`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/application-record`

### 1. 查询申请记录 · findApplicationRecord

```http
GET /card/api/application-record
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ApplicationRecordEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ApplicationRecordEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `appId` | Integer | — |
| `appUid` | String | — |
| `appPhone` | String | — |
| `appEmail` | String | — |
| `channelId` | Integer | — |
| `channelUid` | String | — |
| `type` | Integer | — |
| `status` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. App 查询渠道绑定 · appFindChannel

```http
POST /card/api/application-record/channel
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `appBindChannelReqEntity` | body(JSON) | AppBindChannelReqEntity | 是 | 见下方字段表 |

**请求体字段 · `AppBindChannelReqEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `invitationCode` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. App 绑定渠道 · appBindChannel

```http
POST /card/api/application-record/app-bind-channel
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `appBindChannelEditEntity` | body(JSON) | AppBindChannelEditEntity | 是 | 见下方字段表 |

**请求体字段 · `AppBindChannelEditEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `appId` | Integer | — |
| `appUid` | String | — |
| `invitationCode` | String | — |
| `channelId` | Integer | — |
| `channelUid` | String | — |
| `paymentCode` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. App 查询是否渠道用户 · appIsChannel

```http
POST /card/api/application-record/app-is-channel
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `appBindChannelEditEntity` | body(JSON) | AppBindChannelEditEntity | 是 | 见下方字段表 |

**请求体字段 · `AppBindChannelEditEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `appId` | Integer | — |
| `appUid` | String | — |
| `invitationCode` | String | — |
| `channelId` | Integer | — |
| `channelUid` | String | — |
| `paymentCode` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 更新状态 · updateStatus

```http
PUT /card/api/application-record/status
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | ApplicationRecordReqEntity | 是 | 见下方字段表 |

**请求体字段 · `ApplicationRecordReqEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `appId` | String | — |
| `appUid` | String | — |
| `channelId` | String | — |
| `channelUid` | String | — |
| `channelLevel` | Integer | — |
| `status` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-apply-pay"></a>
## 支付申请 · `apply-pay`

接口数：**1** ｜ 端：**App** ｜ 基础路径：`/card/api/apply-pay`

### 1. 创建支付 · createPayment

```http
GET /card/api/apply-pay
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ApplyPayRequest | — | 对象字段展开，见下方 |

**参数对象字段 · `ApplyPayRequest`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `goodsNum` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-brokerage-log"></a>
## 佣金流水 · `brokerage-log`

接口数：**3** ｜ 端：**App** ｜ 基础路径：`/card/api/brokerage-log`

### 1. 查询佣金流水列表 · findAll

```http
POST /card/api/brokerage-log/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | BrokerageLogEntity | 是 | 见下方字段表 |

**请求体字段 · `BrokerageLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `orderId` | String | — |
| `intiterUserType` | Integer | — |
| `intiterUserLevel` | Integer | — |
| `inviterUser` | Integer | — |
| `inviterUserUid` | String | — |
| `coverInviterUser` | Integer | — |
| `email` | String | — |
| `coverInviterUserUid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `cardType` | String | — |
| `topupFee` | BigDecimal | — |
| `handlingFee` | BigDecimal | — |
| `brokerageRates` | BigDecimal | — |
| `brokerageFee` | BigDecimal | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询按渠道ID · findByChannelId

```http
GET /card/api/brokerage-log/sum
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `ChannelId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询佣金日志按ID · findBrokerageLogById

```http
GET /card/api/brokerage-log/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card-apply"></a>
## 开卡申请 · `card-apply`

接口数：**6** ｜ 端：**App** ｜ 基础路径：`/card/api/card-apply`

### 1. 开通卡片申请 · openCardApply

```http
POST /card/api/card-apply/open-card-apply
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardApplyEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardApplyEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `holderId` | Integer | — |
| `topupType` | Integer | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `cardType` | String | — |
| `applyState` | Integer | — |
| `applyStateName` | String | — |
| `kycState` | Integer | — |
| `kycStateName` | String | — |
| `kycAuditResult` | String | — |
| `monthFee` | Integer | — |
| `openCardCost` | Double | — |
| `preSaveCost` | Double | — |
| `openCardTotal` | Double | — |
| `rejectInfo` | String | — |
| `rejectUser` | Integer | — |
| `rejectUserName` | String | — |
| `shippingState` | Integer | — |
| `shippingStateName` | String | — |
| `shippingTime` | Date | — |
| `logisticsNum` | String | — |
| `logisticsMonery` | Double | — |
| `portType` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardData` | CardEntity | — |
| `applyManData` | CardApplyManEntity | — |
| `applySendData` | CardApplySendEntity | — |
| `kycData` | CardApplyKycEntity | — |
| `userCardData` | AppUserCardEntity | — |
| `logisticsInfo` | Object | — |
| `cardNo` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询开卡申请信息 · openCardApplyInfo

```http
GET /card/api/card-apply/open-card-apply-info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 开通卡片 · openCard

```http
GET /card/api/card-apply/open-card
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 驳回开卡申请 · reject

```http
POST /card/api/card-apply/reject
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | RejectApplyEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `RejectApplyEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `rejectInfo` | String | — |
| `rejectUser` | Integer | — |
| `rejectUserName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 驳回开卡申请（V2） · rejectV2

```http
POST /card/api/card-apply/reject-v2
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | RejectApplyEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `RejectApplyEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `rejectInfo` | String | — |
| `rejectUser` | Integer | — |
| `rejectUserName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 开通卡片（V2） · openCardV2

```http
GET /card/api/card-apply/open-card-v2
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card-close"></a>
## 销卡 · `card-close`

接口数：**1** ｜ 端：**App** ｜ 基础路径：`/card/api/card-close`

### 1. 查询用户卡片列表 · findUserCardList

```http
POST /card/api/card-close/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardCloseEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardCloseEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `cardType` | String | — |
| `cardNo` | String | — |
| `userBankcardId` | Integer | — |
| `balance` | Double | — |
| `refundAmt` | Double | — |
| `requestOrderId` | String | — |
| `reviewStatus` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `payPassword` | String | — |
| `appUserData` | AppUserEntity | — |
| `reviewStatusName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card"></a>
## 卡片 · `card`

接口数：**19** ｜ 端：**App** ｜ 基础路径：`/card/api/card`

### 1. 拉取卡片列表（三方同步） · pullList

```http
GET /card/api/card/pull-list
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 条件查询卡片列表 · findList

```http
POST /card/api/card/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uuid` | String | — |
| `applyDiscount` | Integer | — |
| `applyFee` | Double | — |
| `bankCardNature` | String | — |
| `bankCardSource` | String | — |
| `bankCardType` | String | — |
| `categoryId` | Integer | — |
| `ccy` | String | — |
| `description1` | String | — |
| `description2` | String | — |
| `enable` | Boolean | — |
| `hot` | Boolean | — |
| `id` | Integer | — |
| `img` | String | — |
| `listImg` | String | — |
| `monthFee` | Integer | — |
| `rechargeFee` | Double | — |
| `refundFee` | Double | — |
| `recommend` | Boolean | — |
| `sortParam` | Integer | — |
| `title` | String | — |
| `activeMinLimit` | Double | — |
| `rechargeMinLimit` | Double | — |
| `rechargeMaxLimit` | Double | — |
| `cardBin` | String | — |
| `openCardCost` | Double | — |
| `preSaveCost` | Double | — |
| `cardState` | Integer | — |
| `language` | String | — |
| `lableList` | List<CardLableEntity> | — |
| `lableIdList` | List<Integer> | — |
| `synopsisData` | CardSynopsisEntity | — |
| `synopsisIdList` | List<Integer> | — |
| `synopsisReq` | List<CardSynopsisEntity> | — |
| `cardBrand` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 分页查询 · findListPag

```http
POST /card/api/card/list-pag
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uuid` | String | — |
| `applyDiscount` | Integer | — |
| `applyFee` | Double | — |
| `bankCardNature` | String | — |
| `bankCardSource` | String | — |
| `bankCardType` | String | — |
| `categoryId` | Integer | — |
| `ccy` | String | — |
| `description1` | String | — |
| `description2` | String | — |
| `enable` | Boolean | — |
| `hot` | Boolean | — |
| `id` | Integer | — |
| `img` | String | — |
| `listImg` | String | — |
| `monthFee` | Integer | — |
| `rechargeFee` | Double | — |
| `refundFee` | Double | — |
| `recommend` | Boolean | — |
| `sortParam` | Integer | — |
| `title` | String | — |
| `activeMinLimit` | Double | — |
| `rechargeMinLimit` | Double | — |
| `rechargeMaxLimit` | Double | — |
| `cardBin` | String | — |
| `openCardCost` | Double | — |
| `preSaveCost` | Double | — |
| `cardState` | Integer | — |
| `language` | String | — |
| `lableList` | List<CardLableEntity> | — |
| `lableIdList` | List<Integer> | — |
| `synopsisData` | CardSynopsisEntity | — |
| `synopsisIdList` | List<Integer> | — |
| `synopsisReq` | List<CardSynopsisEntity> | — |
| `cardBrand` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询卡片详情 · findById

```http
GET /card/api/card/{uuid}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uuid` | path | String | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 更新卡片 · update

```http
PUT /card/api/card
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uuid` | String | — |
| `applyDiscount` | Integer | — |
| `applyFee` | Double | — |
| `bankCardNature` | String | — |
| `bankCardSource` | String | — |
| `bankCardType` | String | — |
| `categoryId` | Integer | — |
| `ccy` | String | — |
| `description1` | String | — |
| `description2` | String | — |
| `enable` | Boolean | — |
| `hot` | Boolean | — |
| `id` | Integer | — |
| `img` | String | — |
| `listImg` | String | — |
| `monthFee` | Integer | — |
| `rechargeFee` | Double | — |
| `refundFee` | Double | — |
| `recommend` | Boolean | — |
| `sortParam` | Integer | — |
| `title` | String | — |
| `activeMinLimit` | Double | — |
| `rechargeMinLimit` | Double | — |
| `rechargeMaxLimit` | Double | — |
| `cardBin` | String | — |
| `openCardCost` | Double | — |
| `preSaveCost` | Double | — |
| `cardState` | Integer | — |
| `language` | String | — |
| `lableList` | List<CardLableEntity> | — |
| `lableIdList` | List<Integer> | — |
| `synopsisData` | CardSynopsisEntity | — |
| `synopsisIdList` | List<Integer> | — |
| `synopsisReq` | List<CardSynopsisEntity> | — |
| `cardBrand` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 开通卡片申请（V2） · openCardApplyV2

```http
POST /card/api/card/open-card-apply
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardApplyQueryEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardApplyQueryEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `cardId` | Integer | — |
| `cardUuid` | String | — |
| `holderId` | Integer | — |
| `topupType` | Integer | — |
| `payPassword` | String | — |
| `mailingAddress` | CardApplySendEntity | — |
| `kycData` | CardApplyKycEntity | — |
| `mailingAddressStr` | String | — |
| `kycDataStr` | String | — |
| `appUserHolderData` | AppUserHolderEntity | — |
| `appUserHolderStr` | String | — |
| `invitationLable` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 卡片充值（V2） · topUpV2

```http
POST /card/api/card/top-up
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | RechargeCardEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `RechargeCardEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `userBankcardId` | Integer | — |
| `amount` | Double | — |
| `targetAmount` | Double | — |
| `handlingFees` | Double | — |
| `uid` | String | — |
| `payType` | Integer | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `payPasswrod` | String | — |
| `type` | Boolean | — |
| `requestOrderId` | String | — |
| `openCardAmount` | Double | — |
| `isFirst` | Boolean | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 卡片绑定 · cardBinding

```http
GET /card/api/card/card-binding
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `applyId` | query | Integer | — | — |
| `cardNumber` | query | String | — | — |
| `pinNum` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 卡片邮寄 · shipping

```http
POST /card/api/card/shipping
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardShippingEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardShippingEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `applyId` | Integer | — |
| `uid` | String | — |
| `userEmail` | String | — |
| `userName` | String | — |
| `userTel` | String | — |
| `logisticsProviders` | String | — |
| `logisticsNum` | String | — |
| `logisticsMonery` | Double | — |
| `logisticsState` | Integer | — |
| `logisticsStateName` | String | — |
| `operateUserId` | Integer | — |
| `operateUserName` | String | — |
| `operateUserIp` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 更新物流单号 · upLogisticsNum

```http
GET /card/api/card/up-logistics-num
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `applyId` | query | Integer | — | — |
| `logisticsNum` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 查询卡片物流信息 · findLogistics

```http
GET /card/api/card/logistics
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `logisticsNum` | query | String | — | — |
| `applyId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 12. 更新卡片图片 · updateImg

```http
PUT /card/api/card/img
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uuid` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 13. 更新卡片列表图片 · updateListImg

```http
PUT /card/api/card/list-img
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uuid` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 14. 复制卡片信息 · copyCard

```http
GET /card/api/card/copy-card
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uuid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 15. 发送银行充值验证码 · bankTopUpSend

```http
GET /card/api/card/bank-top-up-send
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `type` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 16. 设置卡片 PIN 码 · setPin

```http
GET /card/api/card/set-pin
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `userBankcardId` | query | Integer | — | — |
| `pinNum` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 17. 查询开通卡片手续费 · findOpenCardFee

```http
GET /card/api/card/open-card-fee
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `cardUuid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 18. 校验是否首次充值 · checkFirstTopUp

```http
GET /card/api/card/first-top-up
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `userBankcardId` | query | Integer | — | — |
| `cardUUid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 19. 更新卡片状态 · upState

```http
GET /card/api/card/up-state
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uuid` | query | String | — | — |
| `stateId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card-label"></a>
## 卡标签 · `card-label`

接口数：**4** ｜ 端：**App** ｜ 基础路径：`/card/api/card-label`

### 1. 新增卡标签 · add

```http
POST /card/api/card-label
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardLableEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardLableEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `name` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 删除卡标签 · delete

```http
DELETE /card/api/card-label
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 条件查询卡标签列表 · findList

```http
POST /card/api/card-label/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardLableEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardLableEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `name` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询卡标签列表 · findAll

```http
GET /card/api/card-label/all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card-synopsis"></a>
## 卡简介 · `card-synopsis`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/card-synopsis`

### 1. 新增卡简介 · add

```http
POST /card/api/card-synopsis
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardSynopsisEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardSynopsisEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `title` | String | — |
| `content` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新卡简介 · update

```http
PUT /card/api/card-synopsis
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardSynopsisEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardSynopsisEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `title` | String | — |
| `content` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除卡简介 · delete

```http
DELETE /card/api/card-synopsis
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 条件查询卡简介列表 · findList

```http
POST /card/api/card-synopsis/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardSynopsisEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardSynopsisEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `title` | String | — |
| `content` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询卡简介列表 · findAll

```http
GET /card/api/card-synopsis/all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-card-banner"></a>
## 卡 Banner · `card-banner`

接口数：**7** ｜ 端：**App** ｜ 基础路径：`/card/api/card-banner`

### 1. 查询卡 Banner列表 · findAll

```http
POST /card/api/card-banner/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | CardBannerEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `CardBannerEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `title` | String | — |
| `imgUrl` | String | — |
| `status` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 新增卡 Banner · add

```http
POST /card/api/card-banner
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `dataStr` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 更新卡 Banner · update

```http
PUT /card/api/card-banner
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `dataStr` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 删除卡 Banner · delete

```http
DELETE /card/api/card-banner
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询卡 Banner详情 · findById

```http
GET /card/api/card-banner/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | String | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 变更状态 · changeStatus

```http
GET /card/api/card-banner/change-status
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `statusType` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 查询正常信息 · findNormalInfo

```http
GET /card/api/card-banner/normal-info
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-cardholder"></a>
## 持卡人 · `cardholder`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/cardholder`

### 1. 新增持卡人 · add

```http
POST /card/api/cardholder
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserHolderEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserHolderEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `userName` | String | — |
| `userSurname` | String | — |
| `userTelDialCode` | String | — |
| `userTelCode` | String | — |
| `userTel` | String | — |
| `userEmail` | String | — |
| `userNumber` | String | — |
| `userSex` | String | — |
| `userSexNum` | Integer | — |
| `userAddress` | String | — |
| `userBirthday` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新持卡人 · update

```http
PUT /card/api/cardholder
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserHolderEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserHolderEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `userName` | String | — |
| `userSurname` | String | — |
| `userTelDialCode` | String | — |
| `userTelCode` | String | — |
| `userTel` | String | — |
| `userEmail` | String | — |
| `userNumber` | String | — |
| `userSex` | String | — |
| `userSexNum` | Integer | — |
| `userAddress` | String | — |
| `userBirthday` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除持卡人 · delete

```http
DELETE /card/api/cardholder
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询按UID · findByUid

```http
GET /card/api/cardholder/by-uid
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询持卡人详情 · findById

```http
GET /card/api/cardholder/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-channel-user"></a>
## 渠道用户 · `channel-user`

接口数：**10** ｜ 端：**App** ｜ 基础路径：`/card/api/channel-user`

### 1. 查询渠道用户列表 · findAll

```http
POST /card/api/channel-user/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | ChannelUserEntity | 是 | 见下方字段表 |

**请求体字段 · `ChannelUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `userId` | Integer | — |
| `username` | String | — |
| `levelNo` | Integer | — |
| `deleteStatus` | Integer | — |
| `levelName` | String | — |
| `invitationCode` | String | — |
| `commissionRate` | Double | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardOpeningDeductionFee` | Double | — |
| `tel` | String | — |
| `acctive` | String | — |
| `googleSecretkey` | String | — |
| `ipWhite` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. App 查询渠道用户列表 · appFindAll

```http
GET /card/api/channel-user/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ChannelUserEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ChannelUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `userId` | Integer | — |
| `username` | String | — |
| `levelNo` | Integer | — |
| `deleteStatus` | Integer | — |
| `levelName` | String | — |
| `invitationCode` | String | — |
| `commissionRate` | Double | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `cardOpeningDeductionFee` | Double | — |
| `tel` | String | — |
| `acctive` | String | — |
| `googleSecretkey` | String | — |
| `ipWhite` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. App 查询渠道用户详情 · appFindById

```http
GET /card/api/channel-user/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | String | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询渠道用户信息 · findChannelUserInfo

```http
GET /card/api/channel-user/channel-user-info
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 新增渠道用户 · add

```http
POST /card/api/channel-user
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | ChannelUserAddEntity | 是 | 见下方字段表 |

**请求体字段 · `ChannelUserAddEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `username` | String | — |
| `tel` | String | — |
| `acctive` | String | — |
| `password` | String | — |
| `googleSecretkey` | String | — |
| `commissionRate` | Double | — |
| `ipWhite` | String | — |
| `userId` | Integer | — |
| `levelNo` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 更新渠道用户 · update

```http
PUT /card/api/channel-user
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | ChannelUserAddEntity | 是 | 见下方字段表 |

**请求体字段 · `ChannelUserAddEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `username` | String | — |
| `tel` | String | — |
| `acctive` | String | — |
| `password` | String | — |
| `googleSecretkey` | String | — |
| `commissionRate` | Double | — |
| `ipWhite` | String | — |
| `userId` | Integer | — |
| `levelNo` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 更新渠道用户密码 · updatePwd

```http
PUT /card/api/channel-user/pwd
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | body(JSON) | ChannelUserAddEntity | 是 | 见下方字段表 |

**请求体字段 · `ChannelUserAddEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `username` | String | — |
| `tel` | String | — |
| `acctive` | String | — |
| `password` | String | — |
| `googleSecretkey` | String | — |
| `commissionRate` | Double | — |
| `ipWhite` | String | — |
| `userId` | Integer | — |
| `levelNo` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 删除渠道用户 · deleteChannelUser

```http
DELETE /card/api/channel-user/channel-user
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userId` | query | int | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 查询渠道等级 · findChannelLevel

```http
GET /card/api/channel-user/channel-level
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. App 查询渠道用户 · appFindChannelLevel

```http
GET /card/api/channel-user/app-find-channel-level
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-sign"></a>
## 签名 / 加解密 · `sign`

接口数：**2** ｜ 端：**App** ｜ 基础路径：`/card/api/sign`

### 1. 数据加密 · encryption

```http
POST /card/api/sign/encryption
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `requestBody` | body(JSON) | Map<String, Object> | 是 | 见下方字段表 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 数据解密 · Decryption

```http
POST /card/api/sign/decryption
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `requestBody` | body(JSON) | Map<String, Object> | 是 | 见下方字段表 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-dic"></a>
## 字典 · `dic`

接口数：**14** ｜ 端：**App** ｜ 基础路径：`/card/api/dic`

### 1. 查询语言列表 · getLanguage

```http
GET /card/api/dic/language
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询国家区号 · getCountryCode

```http
GET /card/api/dic/country-code
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询国家/地区列表 · findCountry

```http
GET /card/api/dic/country
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `parentId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询国家/地区列表 · findCountry

```http
GET /card/api/dic/card-state
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 查询支付类型 · findPayType

```http
GET /card/api/dic/pay-type
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 查询 KYC 认证状态 · findKycState

```http
GET /card/api/dic/kyc-state
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 查询用户状态 · findUserState

```http
GET /card/api/dic/user-state
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 查询协议类型 · findProtocolType

```http
GET /card/api/dic/protocol-type
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 查询跳转地址 · findJumpAddress

```http
GET /card/api/dic/jump-address
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 查询系统配置类型 · findSysConfigType

```http
GET /card/api/dic/sys-config-type
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 查询充值网络列表 · findNetwokList

```http
GET /card/api/dic/netwok-list
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 12. 查询充值网络列表 · getNetwokList

```http
GET /card/api/dic/get-netwok-list
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 13. 查询记录状态 · findRecordState

```http
GET /card/api/dic/record-state
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 14. 获取钱包操作类型 · getWalletOperationType

```http
GET /card/api/dic/wallet-operation-type
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-email-template"></a>
## 邮件模板 · `email-template`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/email-template`

### 1. 新增邮件模板 · add

```http
POST /card/api/email-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | EmailTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `EmailTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `templateNumber` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `templateName` | String | — |
| `templateSubject` | String | — |
| `templateContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新邮件模板 · update

```http
PUT /card/api/email-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | EmailTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `EmailTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `templateNumber` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `templateName` | String | — |
| `templateSubject` | String | — |
| `templateContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除邮件模板 · delete

```http
DELETE /card/api/email-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询验证码 · findCode

```http
GET /card/api/email-template/code
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `code` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 条件查询邮件模板列表 · findList

```http
POST /card/api/email-template/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | EmailTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `EmailTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `templateNumber` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `templateName` | String | — |
| `templateSubject` | String | — |
| `templateContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-file"></a>
## 文件上传 · `file`

接口数：**2** ｜ 端：**App** ｜ 基础路径：`/card/api/file`

### 1. 上传图片 · uploadImage

```http
POST /card/api/file/upload-image
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 上传图片（API） · apiUploadImage

```http
POST /card/api/file/api-upload-image
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `type` | query | Integer | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-forecast"></a>
## 预测 / 回调 · `forecast`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/forecast`

### 1. 预测系统登录 · forecastLogin

```http
GET /card/api/forecast/login
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 获取预测轮播图 · getForecastSlideshow

```http
GET /card/api/forecast/forecast-slideshow
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 预测系统登录（V2） · forecastLoginV2

```http
GET /card/api/forecast
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 回调 · callback

```http
POST /card/api/forecast/callback
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `json` | body(JSON) | String | 是 | 见下方字段表 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 保存预测轮播图 · saveSlideshow

```http
POST /card/api/forecast/slideshow
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ForecastSlideshowEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ForecastSlideshowEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `imgUrl` | String | — |
| `type` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-help"></a>
## 帮助中心 · `help`

接口数：**6** ｜ 端：**App** ｜ 基础路径：`/card/api/help`

### 1. 新增帮助中心 · add

```http
POST /card/api/help
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | HelpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `HelpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `articleName` | String | — |
| `articleUrl` | String | — |
| `articleContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新帮助中心 · update

```http
PUT /card/api/help
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | HelpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `HelpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `articleName` | String | — |
| `articleUrl` | String | — |
| `articleContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除帮助中心 · delete

```http
DELETE /card/api/help
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 条件查询帮助中心列表 · findList

```http
POST /card/api/help/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | HelpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `HelpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `articleName` | String | — |
| `articleUrl` | String | — |
| `articleContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 后台查询帮助中心列表 · pcFindList

```http
POST /card/api/help/pc-find-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | HelpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `HelpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `articleName` | String | — |
| `articleUrl` | String | — |
| `articleContent` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 查询帮助中心详情 · findById

```http
GET /card/api/help/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-insights"></a>
## 消费洞察 · `insights`

接口数：**1** ｜ 端：**App** ｜ 基础路径：`/card/api/insights`

### 1. 卡片消费洞察（区间合计+环比，按 dimension=day/week/month/quarter 聚合列表，默认 week） · query

```http
GET /card/api/insights
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `startDate` | query | String | — | — |
| `endDate` | query | String | — | — |
| `dimension` | query | String | — | — |
| `userBankcardId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-ios-goods"></a>
## iOS 商品 · `ios-goods`

接口数：**8** ｜ 端：**App** ｜ 基础路径：`/card/api/ios-goods`

### 1. 查询iOS 商品列表 · findAll

```http
GET /card/api/ios-goods/all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询iOS 商品详情 · findById

```http
GET /card/api/ios-goods/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询按编号 · findByNum

```http
GET /card/api/ios-goods/by-num
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `num` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 验证 · verify

```http
POST /card/api/ios-goods/verify
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | IosPayReqEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `IosPayReqEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `goodsNum` | String | — |
| `receiptData` | String | — |
| `receiptNum` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 新增iOS 商品 · add

```http
POST /card/api/ios-goods
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | IosGoodsEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `IosGoodsEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `goodsName` | String | — |
| `goodsNum` | String | — |
| `goodsInfo` | String | — |
| `goodsPrice` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 更新iOS 商品 · update

```http
PUT /card/api/ios-goods
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | IosGoodsEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `IosGoodsEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `goodsName` | String | — |
| `goodsNum` | String | — |
| `goodsInfo` | String | — |
| `goodsPrice` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 删除iOS 商品 · delete

```http
DELETE /card/api/ios-goods
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 条件查询iOS 商品列表 · findList

```http
POST /card/api/ios-goods/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | IosGoodsEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `IosGoodsEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `goodsName` | String | — |
| `goodsNum` | String | — |
| `goodsInfo` | String | — |
| `goodsPrice` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-kyc"></a>
## KYC 认证 · `kyc`

接口数：**1** ｜ 端：**App** ｜ 基础路径：`/card/api/kyc`

### 1. 申请 · apply

```http
GET /card/api/kyc/apply
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `applyId` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-merchant"></a>
## 商户 · `merchant`

接口数：**2** ｜ 端：**App** ｜ 基础路径：`/card/api/merchant`

### 1. 查询信息 · findInfo

```http
GET /card/api/merchant/info
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询商户充值列表 · rechargeList

```http
POST /card/api/merchant/recharge-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | RechargeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `RechargeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `pageNum` | Integer | — |
| `pageSize` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-message-template"></a>
## 消息模板 · `message-template`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/message-template`

### 1. 新增消息模板 · add

```http
POST /card/api/message-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | MessageTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `MessageTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `messageCode` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `msgTitles` | String | — |
| `messageInfo` | String | — |
| `messageUrl` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新消息模板 · update

```http
PUT /card/api/message-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | MessageTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `MessageTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `messageCode` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `msgTitles` | String | — |
| `messageInfo` | String | — |
| `messageUrl` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除消息模板 · delete

```http
DELETE /card/api/message-template
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询验证码 · findCode

```http
GET /card/api/message-template/code
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `code` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 条件查询消息模板列表 · findList

```http
POST /card/api/message-template/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | MessageTemplateEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `MessageTemplateEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `messageCode` | Integer | — |
| `language` | String | — |
| `languageName` | String | — |
| `msgTitles` | String | — |
| `messageInfo` | String | — |
| `messageUrl` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-notice"></a>
## 公告 · `notice`

接口数：**7** ｜ 端：**App** ｜ 基础路径：`/card/api/notice`

### 1. 查询公告列表 · findAll

```http
POST /card/api/notice/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | NoticeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `NoticeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `language` | String | — |
| `languageName` | String | — |
| `title` | String | — |
| `intro` | String | — |
| `content` | String | — |
| `type` | Integer | — |
| `typeName` | String | — |
| `status` | Integer | — |
| `statusName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `userData` | AppUserEntity | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询公告详情 · findById

```http
GET /card/api/notice/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 条件查询公告列表 · findList

```http
POST /card/api/notice/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | NoticeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `NoticeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `language` | String | — |
| `languageName` | String | — |
| `title` | String | — |
| `intro` | String | — |
| `content` | String | — |
| `type` | Integer | — |
| `typeName` | String | — |
| `status` | Integer | — |
| `statusName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `userData` | AppUserEntity | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 新增公告 · add

```http
POST /card/api/notice
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | NoticeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `NoticeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `language` | String | — |
| `languageName` | String | — |
| `title` | String | — |
| `intro` | String | — |
| `content` | String | — |
| `type` | Integer | — |
| `typeName` | String | — |
| `status` | Integer | — |
| `statusName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `userData` | AppUserEntity | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 更新公告 · update

```http
PUT /card/api/notice
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | NoticeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `NoticeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `language` | String | — |
| `languageName` | String | — |
| `title` | String | — |
| `intro` | String | — |
| `content` | String | — |
| `type` | Integer | — |
| `typeName` | String | — |
| `status` | Integer | — |
| `statusName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `userData` | AppUserEntity | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 删除公告 · delete

```http
DELETE /card/api/notice
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 发送 · send

```http
GET /card/api/notice/send
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-product"></a>
## 产品 · `product`

接口数：**11** ｜ 端：**App** ｜ 基础路径：`/card/api/product`

### 1. 新增产品 · add

```http
POST /card/api/product
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `dataStr` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新产品 · update

```http
PUT /card/api/product
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `dataStr` | query | String | — | — |
| `file` | form-data | MultipartFile | — | 文件上传 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 产品上下架 · OnAndOff

```http
GET /card/api/product/on-and-off
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `type` | query | Integer | — | — |
| `language` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 条件查询产品列表 · findList

```http
POST /card/api/product/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `coverImg` | String | — |
| `productName` | String | — |
| `productInfo` | String | — |
| `productTypeId` | Integer | — |
| `productType` | String | — |
| `fundsType` | String | — |
| `productStateId` | Integer | — |
| `productState` | String | — |
| `initialMonery` | Double | — |
| `productTime` | Integer | — |
| `interestRate` | Double | — |
| `whetherCurrent` | Integer | — |
| `deleteState` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 后台查询产品列表 · pcFindList

```http
POST /card/api/product/pc-find-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `coverImg` | String | — |
| `productName` | String | — |
| `productInfo` | String | — |
| `productTypeId` | Integer | — |
| `productType` | String | — |
| `fundsType` | String | — |
| `productStateId` | Integer | — |
| `productState` | String | — |
| `initialMonery` | Double | — |
| `productTime` | Integer | — |
| `interestRate` | Double | — |
| `whetherCurrent` | Integer | — |
| `deleteState` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 产品支付 · pay

```http
POST /card/api/product/pay
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductPayEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductPayEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `productId` | Integer | — |
| `payMonery` | Double | — |
| `payNum` | Integer | — |
| `payPassword` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 查询用户资产 · userAssets

```http
GET /card/api/product/user-assets
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 查询订单列表 · orderList

```http
POST /card/api/product/order-list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductOrderEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductOrderEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `productId` | Integer | — |
| `orderNum` | String | — |
| `orderState` | String | — |
| `orderMonery` | Double | — |
| `payNum` | Integer | — |
| `productName` | String | — |
| `productImg` | String | — |
| `productType` | String | — |
| `fundsType` | String | — |
| `productTime` | Integer | — |
| `interestRate` | Double | — |
| `whetherCurrent` | Integer | — |
| `estimateIncome` | Double | — |
| `actualIncome` | Double | — |
| `payTime` | Date | — |
| `expireTime` | Date | — |
| `finishTime` | Date | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `appUser` | AppUserEntity | — |
| `product` | ProductEntity | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 完成订单 · orderFinish

```http
GET /card/api/product/order-finish
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 删除产品 · delete

```http
DELETE /card/api/product
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 查询产品详情 · findById

```http
GET /card/api/product/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-product-type"></a>
## 产品类型 · `product-type`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/product-type`

### 1. 新增产品类型 · add

```http
POST /card/api/product-type
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductTypeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductTypeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `name` | String | — |
| `whetherCurrent` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 条件查询产品类型列表 · findList

```http
POST /card/api/product-type/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | ProductTypeEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `ProductTypeEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `language` | String | — |
| `name` | String | — |
| `whetherCurrent` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 删除产品类型 · delete

```http
DELETE /card/api/product-type
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询产品类型列表 · findAll

```http
GET /card/api/product-type/all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 后台查询产品类型列表 · pcFindAll

```http
GET /card/api/product-type/pc-find-all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `language` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-info"></a>
## 系统信息 / 配置 · `info`

接口数：**7** ｜ 端：**App** ｜ 基础路径：`/card/api/info`

### 1. 查询系统信息 / 配置列表 · findAll

```http
POST /card/api/info/all
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | SysInfoEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `SysInfoEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `configType` | Integer | — |
| `configTypeName` | String | — |
| `configLable` | Integer | — |
| `language` | String | — |
| `configName` | String | — |
| `configContent` | String | — |
| `configUrl` | String | — |
| `status` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 查询系统信息 / 配置详情 · findById

```http
GET /card/api/info/{id}
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | path | Integer | 是 | 路径参数 |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 新增系统信息 / 配置 · add

```http
POST /card/api/info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | SysInfoEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `SysInfoEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `configType` | Integer | — |
| `configTypeName` | String | — |
| `configLable` | Integer | — |
| `language` | String | — |
| `configName` | String | — |
| `configContent` | String | — |
| `configUrl` | String | — |
| `status` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 更新系统信息 / 配置 · update

```http
PUT /card/api/info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | SysInfoEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `SysInfoEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `configType` | Integer | — |
| `configTypeName` | String | — |
| `configLable` | Integer | — |
| `language` | String | — |
| `configName` | String | — |
| `configContent` | String | — |
| `configUrl` | String | — |
| `status` | Integer | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `languageName` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 删除系统信息 / 配置 · delete

```http
DELETE /card/api/info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 变更状态 · changeStatus

```http
GET /card/api/info/change-status
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `statusType` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 查询配置信息 · findConfigInfo

```http
GET /card/api/info/config-info
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `configType` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-toll-address"></a>
## 收费地址 · `toll-address`

接口数：**5** ｜ 端：**App** ｜ 基础路径：`/card/api/toll-address`

### 1. 查询收费地址列表 · findAll

```http
GET /card/api/toll-address/all
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 新增收费地址 · add

```http
POST /card/api/toll-address
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | TollAddressEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `TollAddressEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `addressType` | String | — |
| `addressSite` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 更新收费地址 · update

```http
PUT /card/api/toll-address
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | TollAddressEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `TollAddressEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `addressType` | String | — |
| `addressSite` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 条件查询收费地址列表 · findList

```http
POST /card/api/toll-address/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | TollAddressEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `TollAddressEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `addressType` | String | — |
| `addressSite` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 删除收费地址 · delete

```http
DELETE /card/api/toll-address
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-app-user-sys"></a>
## App 用户（后台管理） · `app-user-sys`

接口数：**11** ｜ 端：**App** ｜ 基础路径：`/card/api/app-user-sys`

### 1. 签发谷歌密钥 · IssueGoogleSecretkey

```http
GET /card/api/app-user-sys/issue-google-secretkey
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新谷歌密钥 · upGoogleSecretkey

```http
GET /card/api/app-user-sys/up-google-secretkey
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `googleSecretkey` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 校验谷歌密钥 · checkGoogleSecretkey

```http
GET /card/api/app-user-sys/google-secretkey
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |
| `googleCode` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 修改登录密码 · updaePwd

```http
POST /card/api/app-user-sys/updae-pwd
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | UpdatePwdEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `UpdatePwdEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `formerPassword` | String | — |
| `newPassword` | String | — |
| `emailCode` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 用户退出登录 · logout

```http
GET /card/api/app-user-sys/logout
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 退出登录 · signOut

```http
GET /card/api/app-user-sys/sign-out
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 条件查询App 用户（后台管理）列表 · findList

```http
POST /card/api/app-user-sys/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | AppUserEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `AppUserEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | 主键 |
| `uid` | String | 三方 UID（UCard 返回） |
| `userEmail` | String | 用户邮箱（登录账号） |
| `userPassword` | String | 登录密码（MD5 哈希，兼容旧版） |
| `mobileNumber` | String | 手机号 |
| `mobilePrefix` | String | 手机区号 |
| `userState` | Integer | 用户状态：1-正常，其他-冻结 |
| `payPassword` | String | 支付密码（MD5 哈希） |
| `googleSecretkey` | String | Google Authenticator 密钥 |
| `kycState` | Integer | KYC 状态 |
| `kycStateName` | String | KYC 状态名称 |
| `kycAuditResult` | String | kyc申请结果描述 |
| `invitationCode` | String | 邀请码 |
| `toInvitationCode` | String | 被邀请人的邀请码 |
| `walletBalance` | Double | 钱包可用余额 |
| `freezeBalance` | Double | 冻结资产 |
| `openFreezeBalance` | Double | 开卡临时冻结 |
| `withdrawableBalance` | Double | 可提现余额（佣金） |
| `walletState` | Boolean | 钱包入口是否开启 |
| `registrationId` | String | Jpush三方id |
| `tronUsdtAcount` | String | tron链usdt充值地址 |
| `cid` | String | 推送设备 ID |
| `deviceName` | String | 登录设备名称 |
| `setTime` | Date | 创建时间（兼容旧字段 setTime） |
| `gmtModified` | Date | 修改时间（兼容旧字段 gmtModified） |
| `googleState` | Boolean | 前端判断是否设置了 Google 验证 |
| `googleCode` | String | 谷歌验证码 |
| `payPasswordState` | Boolean | 前端判断是否设置了支付密码 |
| `invitationLable` | Integer | 邀请人标签 |
| `bandingChannel` | Boolean | 是否已绑定渠道商 |
| `bandingChannelUid` | String | 绑定的渠道商 UID |
| `iosWalledState` | Integer | iOS 钱包入口状态 |
| `iosTopUpState` | Integer | iOS USDT 充值入口状态 |
| `emailCode` | String | 邮箱验证码（非数据库字段） |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 更新钱包状态 · upWalletState

```http
GET /card/api/app-user-sys/up-wallet-state
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `state` | query | Boolean | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 查询KYC文件 · findKycFile

```http
GET /card/api/app-user-sys/kyc-file
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 忘记密码（重置密码） · forgetPasswrod

```http
GET /card/api/app-user-sys/forget-passwrod
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `userEmail` | query | String | — | — |
| `passwrod` | query | String | — | — |
| `emailCode` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 11. 修改支付密码 · upPayPasswrod

```http
POST /card/api/app-user-sys/up-pay-passwrod
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | UpdatePwdEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `UpdatePwdEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `uid` | String | — |
| `formerPassword` | String | — |
| `newPassword` | String | — |
| `emailCode` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-wallet"></a>
## 钱包 · `wallet`

接口数：**10** ｜ 端：**App** ｜ 基础路径：`/card/api/wallet`

### 1. 查询 USDT 充值地址 · topinUsdtAddress

```http
GET /card/api/wallet/topin-usdt-address
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. USDT 链上充值回调 · topinUsdtNotify

```http
POST /card/api/wallet/topin-usdt-notify
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | UsdtTopinNotifyEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `UsdtTopinNotifyEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `type` | String | — |
| `block` | String | — |
| `uid` | String | — |
| `email` | String | — |
| `orderNo` | String | — |
| `chain` | String | — |
| `coin` | String | — |
| `amount` | String | — |
| `amount_raw` | String | — |
| `time` | String | — |
| `outaddress` | String | — |
| `inaddress` | String | — |
| `sign` | String | — |
| `hash` | String | — |
| `requestOrderNum` | String | — |
| `stateCode` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 查询钱包日志 · walletLog

```http
POST /card/api/wallet/wallet-log
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletLogEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `orderNo` | String | — |
| `outOrderNo` | String | — |
| `userId` | Integer | — |
| `uid` | String | — |
| `tradeType` | Integer | — |
| `title` | String | — |
| `primevalMoney` | Double | — |
| `primevalMoneyUnit` | String | — |
| `realMoney` | BigDecimal | — |
| `serviceCharge` | BigDecimal | — |
| `formName` | String | — |
| `formAccount` | String | — |
| `toName` | String | — |
| `toAccount` | String | — |
| `memo` | String | — |
| `userBankcardId` | Integer | — |
| `operateType` | Integer | — |
| `status` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `networkType` | String | — |
| `tranHash` | String | — |
| `realMoneySum` | BigDecimal | — |
| `userEmail` | String | — |
| `oldMonery` | BigDecimal | — |
| `totalMoney` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 钱包日志统计 · walletLogStat

```http
POST /card/api/wallet/wallet-log-stat
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | PublicQueryEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `PublicQueryEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `begTime` | String | — |
| `endTime` | String | — |
| `status` | String | — |
| `statusId` | Integer | — |
| `openState` | Integer | — |
| `type` | String | — |
| `userId` | String | — |
| `uid` | List<String> | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 钱包充值 · walletTopUp

```http
POST /card/api/wallet/wallet-top-up
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletManualLogEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletManualLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `orderNo` | String | — |
| `userId` | Integer | — |
| `uid` | String | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `walletType` | String | — |
| `walletAddress` | String | — |
| `topupAmount` | Double | — |
| `topupAmountForward` | Double | — |
| `topupAmountBack` | Double | — |
| `operateUserId` | Integer | — |
| `operateUserName` | String | — |
| `operateUserIp` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `networkTopUpId` | Integer | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 查询钱包日志 · findWalletLog

```http
POST /card/api/wallet/find-wallet-log
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletLogEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `orderNo` | String | — |
| `outOrderNo` | String | — |
| `userId` | Integer | — |
| `uid` | String | — |
| `tradeType` | Integer | — |
| `title` | String | — |
| `primevalMoney` | Double | — |
| `primevalMoneyUnit` | String | — |
| `realMoney` | BigDecimal | — |
| `serviceCharge` | BigDecimal | — |
| `formName` | String | — |
| `formAccount` | String | — |
| `toName` | String | — |
| `toAccount` | String | — |
| `memo` | String | — |
| `userBankcardId` | Integer | — |
| `operateType` | Integer | — |
| `status` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `networkType` | String | — |
| `tranHash` | String | — |
| `realMoneySum` | BigDecimal | — |
| `userEmail` | String | — |
| `oldMonery` | BigDecimal | — |
| `totalMoney` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 7. 钱包转账 · transfer

```http
POST /card/api/wallet/transfer
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletTransfetListEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletTransfetListEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `orderNo` | String | — |
| `sendUid` | String | — |
| `sendEmail` | String | — |
| `sendForwardBalance` | Double | — |
| `sendBackBalance` | Double | — |
| `recipientUid` | String | — |
| `recipientEmail` | String | — |
| `recipientForwardBalance` | Double | — |
| `recipientBackBalance` | Double | — |
| `sendMoney` | Double | — |
| `sendRates` | Double | — |
| `actualMoney` | Double | — |
| `handlingFee` | Double | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `payPassword` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 8. 查询转账流水 · transferReading

```http
GET /card/api/wallet/transfer-reading
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `sendMoney` | query | Double | — | — |
| `uid` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 9. 查询钱包流水 · findReading

```http
GET /card/api/wallet/reading
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 10. 提现回调 · withdrawalNotify

```http
POST /card/api/wallet/withdrawal-notify
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WithdrawalCallbackEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WithdrawalCallbackEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `txHash` | String | — |
| `outOrderNo` | String | — |
| `orderNo` | String | — |
| `status` | String | — |
| `address` | String | — |
| `chain` | String | — |
| `sign` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-wallet-entrance"></a>
## 钱包入口 · `wallet-entrance`

接口数：**2** ｜ 端：**App** ｜ 基础路径：`/card/api/wallet-entrance`

### 1. 条件查询钱包入口列表 · findList

```http
POST /card/api/wallet-entrance/list
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 钱包入口开关操作 · operate

```http
GET /card/api/wallet-entrance/operate
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `type` | query | Boolean | — | — |
| `userName` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-wallet-network-top-up"></a>
## 链上充值 · `wallet-network-top-up`

接口数：**3** ｜ 端：**App** ｜ 基础路径：`/card/api/wallet-network-top-up`

### 1. 新增链上充值 · add

```http
POST /card/api/wallet-network-top-up
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletNetworkTopUpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletNetworkTopUpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `orderNo` | String | — |
| `networkType` | String | — |
| `walletAddress` | String | — |
| `tradeHash` | String | — |
| `topupAmount` | BigDecimal | — |
| `orderRemark` | String | — |
| `reviewRemark` | String | — |
| `orderState` | Integer | — |
| `orderStateName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `walletBalance` | Double | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 条件查询链上充值列表 · findList

```http
POST /card/api/wallet-network-top-up/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletNetworkTopUpEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletNetworkTopUpEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `orderNo` | String | — |
| `networkType` | String | — |
| `walletAddress` | String | — |
| `tradeHash` | String | — |
| `topupAmount` | BigDecimal | — |
| `orderRemark` | String | — |
| `reviewRemark` | String | — |
| `orderState` | Integer | — |
| `orderStateName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `walletBalance` | Double | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 关闭链上充值网络 · close

```http
GET /card/api/wallet-network-top-up/close
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `reviewRemark` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-wallet-to-web-log"></a>
## 钱包流水 · `wallet-to-web-log`

接口数：**6** ｜ 端：**App** ｜ 基础路径：`/card/api/wallet-to-web-log`

### 1. 创建 · create

```http
POST /card/api/wallet-to-web-log
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletToWebLogEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletToWebLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `orderNo` | String | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `networkType` | String | — |
| `tokenName` | String | — |
| `tokenAddress` | String | — |
| `walletAddress` | String | — |
| `transferAmount` | Double | — |
| `serverAmount` | Double | — |
| `handlingFee` | Double | — |
| `realAmount` | Double | — |
| `applyState` | Integer | — |
| `applyStateName` | String | — |
| `rejectContent` | String | — |
| `tranHash` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `payPassword` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 站内转账审核通过 · pass

```http
GET /card/api/wallet-to-web-log/pass
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `gooleCode` | query | Integer | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 站内转账审核驳回 · reject

```http
GET /card/api/wallet-to-web-log/reject
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | query | Integer | — | — |
| `rejectContent` | query | String | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 4. 查询费率 · findRates

```http
GET /card/api/wallet-to-web-log/rates
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 5. 条件查询钱包流水列表 · findList

```http
POST /card/api/wallet-to-web-log/list
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletToWebLogEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletToWebLogEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `uid` | String | — |
| `orderNo` | String | — |
| `userEmail` | String | — |
| `userTel` | String | — |
| `networkType` | String | — |
| `tokenName` | String | — |
| `tokenAddress` | String | — |
| `walletAddress` | String | — |
| `transferAmount` | Double | — |
| `serverAmount` | Double | — |
| `handlingFee` | Double | — |
| `realAmount` | Double | — |
| `applyState` | Integer | — |
| `applyStateName` | String | — |
| `rejectContent` | String | — |
| `tranHash` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |
| `payPassword` | String | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 6. 计算站内转账金额 · calculateAmount

```http
GET /card/api/wallet-to-web-log/calculate-amount
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `amount` | query | BigDecimal | — | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-withdrawal-rates"></a>
## 提现费率 · `withdrawal-rates`

接口数：**2** ｜ 端：**App** ｜ 基础路径：`/card/api/withdrawal-rates`

### 1. 查询提现费率 · find

```http
GET /card/api/withdrawal-rates
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 更新提现费率 · update

```http
PUT /card/api/withdrawal-rates
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletToWebRatesEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletToWebRatesEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `minAmount` | Double | — |
| `maxAmount` | Double | — |
| `handlingRates` | Double | — |
| `serverAmount` | Double | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---

<a id="mod-wallet-transfet-rates"></a>
## 钱包转账费率 · `wallet-transfet-rates`

接口数：**3** ｜ 端：**App** ｜ 基础路径：`/card/api/wallet-transfet-rates`

### 1. 条件查询钱包转账费率列表 · findList

```http
GET /card/api/wallet-transfet-rates/list
```

**请求参数**：无

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 2. 新增钱包转账费率 · add

```http
POST /card/api/wallet-transfet-rates
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletTransfetRatesEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletTransfetRatesEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `rates` | Double | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

### 3. 更新钱包转账费率 · update

```http
PUT /card/api/wallet-transfet-rates
```

**请求参数**

| 参数 | 位置 | 类型 | 必填 | 说明 |
|---|---|---|---|---|
| `entity` | query/form | WalletTransfetRatesEntity | — | 对象字段展开，见下方 |

**参数对象字段 · `WalletTransfetRatesEntity`**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | Integer | — |
| `rates` | Double | — |
| `setUser` | Integer | — |
| `setUserName` | String | — |
| `setTime` | Date | — |
| `gmtModified` | Date | — |

**响应**：明文 `ResponseBase` — `{"rtncode":200,"msg":"success","data":...}`。

---
