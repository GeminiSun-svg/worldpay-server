# 旧接口 → 新接口 对应关系

> 旧接口：旧项目 `com.ucard`（context-path `/ucard`，无版本前缀，Service 直接充当 Controller）
> 新接口：本项目（context-path `/worldpay`，统一前缀 `/card/api`，独立 Controller 层）

- 旧接口总数：**295**
- 已迁移：**297**
- 未迁移（旧接口在新项目无对应实现）：**0**
- 新接口总数：**291**（其中新增无旧对应：**0**）

> 说明：旧接口中同一 URL 可能因 `headers` 版本区分（如 v1/v2）出现多行；新接口已按 RESTful 规范重写路径与动词。

## 一、已迁移接口对照

### AppUser（16 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/appUser/cashAmount` | `GET` | `/card/api/app-user/cash-amount` |
| `GET` | `/ucard/appUser/checkEmailCode` | `GET` | `/card/api/app-user/email-code` |
| `GET` | `/ucard/appUser/checkGoogleSecretkey` | `GET` | `/card/api/app-user/google-secretkey` |
| `GET` | `/ucard/appUser/findAddress` | `GET` | `/card/api/app-user/address` |
| `GET` | `/ucard/appUser/findKycState` | `GET` | `/card/api/app-user/kyc-state` |
| `GET` | `/ucard/appUser/findToken` | `GET` | `/card/api/app-user/token` |
| `GET` | `/ucard/appUser/findToken` | `GET` | `/card/api/app-user` |
| `GET` | `/ucard/appUser/googleCode` | `GET` | `/card/api/app-user/google-code` |
| `GET` | `/ucard/appUser/kycRetry` | `GET` | `/card/api/app-user/kyc-retry` |
| `GET` | `/ucard/appUser/kycRetryAll` | `GET` | `/card/api/app-user/kyc-retry-all` |
| `POST` | `/ucard/appUser/login` | `POST` | `/card/api/app-user/login` |
| `GET` | `/ucard/appUser/sendEmailCode` | `GET` | `/card/api/app-user/send-email-code` |
| `POST` | `/ucard/appUser/signUp` | `POST` | `/card/api/app-user/sign-up` |
| `POST` | `/ucard/appUser/signUp` | `POST` | `/card/api/app-user` |
| `GET` | `/ucard/appUser/userinfo` | `GET` | `/card/api/app-user/userinfo` |
| `POST` | `/ucard/appUser/withdrawalToWallet` | `POST` | `/card/api/app-user/withdrawal-to-wallet` |

### AppUserCard（14 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/appUserCard/closeCard` | `POST` | `/card/api/app-user-card/close-card` |
| `POST` | `/ucard/appUserCard/findCardCvv` | `POST` | `/card/api/app-user-card/card-cvv` |
| `POST` | `/ucard/appUserCard/findCardExpirationTime` | `POST` | `/card/api/app-user-card/card-expiration-time` |
| `POST` | `/ucard/appUserCard/findTransaction` | `POST` | `/card/api/app-user-card/transaction` |
| `POST` | `/ucard/appUserCard/findUserCardActivationList` | `POST` | `/card/api/app-user-card/user-card-activation-list` |
| `POST` | `/ucard/appUserCard/findUserCardActivationList` | `POST` | `/card/api/app-user-card/pc-find-user-card-list` |
| `GET` | `/ucard/appUserCard/findUserCardAssets` | `GET` | `/card/api/app-user-card/user-card-assets` |
| `GET` | `/ucard/appUserCard/findUserCardInfo` | `GET` | `/card/api/app-user-card/user-card-info` |
| `POST` | `/ucard/appUserCard/findUserCardList` | `POST` | `/card/api/app-user-card/user-card-list` |
| `POST` | `/ucard/appUserCard/findUserCardList` | `POST` | `/card/api/app-user-card/pc-find-user-card-list` |
| `POST` | `/ucard/appUserCard/pcFindUserCardList` | `POST` | `/card/api/app-user-card/pc-find-user-card-list` |
| `POST` | `/ucard/appUserCard/setPin` | `POST` | `/card/api/app-user-card/set-pin` |
| `GET` | `/ucard/appUserCard/unfreeze` | `GET` | `/card/api/app-user-card/unfreeze` |
| `POST` | `/ucard/appUserCard/upTag` | `POST` | `/card/api/app-user-card/up-tag` |

### AppUserIntiter（4 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/appUserIntiter/addAppUserIntiter` | `POST` | `/card/api/app-user-intiter/app-user-intiter` |
| `POST` | `/ucard/appUserIntiter/findAll` | `POST` | `/card/api/app-user-intiter/all` |
| `GET` | `/ucard/appUserIntiter/findById/{id}` | `GET` | `/card/api/app-user-intiter/{id}` |
| `GET` | `/ucard/appUserIntiter/findChannelInvitationCodeByUserId/{userId}` | `GET` | `/card/api/app-user-intiter/channel-invitation-code-by-user-id/{userId}` |

### ApplicationRecord（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/applicationRecord/appBindChannel` | `POST` | `/card/api/application-record/app-bind-channel` |
| `POST` | `/ucard/applicationRecord/appFindChannel` | `POST` | `/card/api/application-record/channel` |
| `POST` | `/ucard/applicationRecord/appIsChannel` | `POST` | `/card/api/application-record/app-is-channel` |
| `GET` | `/ucard/applicationRecord/find` | `GET` | `/card/api/application-record` |
| `POST` | `/ucard/applicationRecord/updateStatus` | `PUT` | `/card/api/application-record/status` |

### ApplyPay（1 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/applyPay/create` | `GET` | `/card/api/apply-pay` |

### BrokerageLog（3 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/brokerageLog/findAll` | `POST` | `/card/api/brokerage-log/all` |
| `GET` | `/ucard/brokerageLog/findById/{id}` | `GET` | `/card/api/brokerage-log/{id}` |
| `GET` | `/ucard/brokerageLog/getSum` | `GET` | `/card/api/brokerage-log/sum` |

### Card（21 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/card/bankTopUpSend` | `GET` | `/card/api/card/bank-top-up-send` |
| `GET` | `/ucard/card/cardBinding` | `GET` | `/card/api/card/card-binding` |
| `GET` | `/ucard/card/checkFirstTopUp` | `GET` | `/card/api/card/first-top-up` |
| `GET` | `/ucard/card/copyCard` | `GET` | `/card/api/card/copy-card` |
| `GET` | `/ucard/card/findById` | `GET` | `/card/api/card/{uuid}` |
| `POST` | `/ucard/card/findList` | `POST` | `/card/api/card/list` |
| `POST` | `/ucard/card/findListPag` | `POST` | `/card/api/card/list-pag` |
| `GET` | `/ucard/card/findLogistics` | `GET` | `/card/api/card/logistics` |
| `GET` | `/ucard/card/findOpenCardFee` | `GET` | `/card/api/card/open-card-fee` |
| `POST` | `/ucard/card/openCardApply` | `POST` | `/card/api/card/open-card-apply` |
| `POST` | `/ucard/card/openCardApplyV1` | `POST` | `/card/api/card/open-card-apply` |
| `GET` | `/ucard/card/pullList` | `GET` | `/card/api/card/pull-list` |
| `GET` | `/ucard/card/setPin` | `GET` | `/card/api/card/set-pin` |
| `POST` | `/ucard/card/shipping` | `POST` | `/card/api/card/shipping` |
| `POST` | `/ucard/card/topUp` | `POST` | `/card/api/card/top-up` |
| `POST` | `/ucard/card/topUpV1` | `POST` | `/card/api/card/open-card-apply` |
| `GET` | `/ucard/card/upLogisticsNum` | `GET` | `/card/api/card/up-logistics-num` |
| `GET` | `/ucard/card/upState` | `GET` | `/card/api/card/up-state` |
| `POST` | `/ucard/card/update` | `PUT` | `/card/api/card` |
| `POST` | `/ucard/card/updateImg` | `PUT` | `/card/api/card/img` |
| `POST` | `/ucard/card/updateListImg` | `PUT` | `/card/api/card/list-img` |

### CardApply（6 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/cardApply/openCard` | `GET` | `/card/api/card-apply/open-card` |
| `POST` | `/ucard/cardApply/openCardApply` | `POST` | `/card/api/card-apply/open-card-apply` |
| `GET` | `/ucard/cardApply/openCardApplyInfo` | `GET` | `/card/api/card-apply/open-card-apply-info` |
| `GET` | `/ucard/cardApply/openCardV2` | `GET` | `/card/api/card-apply/open-card-v2` |
| `POST` | `/ucard/cardApply/reject` | `POST` | `/card/api/card-apply/reject` |
| `POST` | `/ucard/cardApply/rejectV2` | `POST` | `/card/api/card-apply/reject-v2` |

### CardClose（1 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/cardClose/findList` | `POST` | `/card/api/card-close/list` |

### CardLable（4 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/cardLable/add` | `POST` | `/card/api/card-label` |
| `GET` | `/ucard/cardLable/delete` | `DELETE` | `/card/api/card-label` |
| `GET` | `/ucard/cardLable/findAll` | `GET` | `/card/api/card-label/all` |
| `POST` | `/ucard/cardLable/findList` | `POST` | `/card/api/card-label/list` |

### CardSynopsis（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/cardSynopsis/add` | `POST` | `/card/api/card-synopsis` |
| `GET` | `/ucard/cardSynopsis/delete` | `DELETE` | `/card/api/card-synopsis` |
| `GET` | `/ucard/cardSynopsis/findAll` | `GET` | `/card/api/card-synopsis/all` |
| `POST` | `/ucard/cardSynopsis/findList` | `POST` | `/card/api/card-synopsis/list` |
| `POST` | `/ucard/cardSynopsis/update` | `PUT` | `/card/api/card-synopsis` |

### Cardbanner（7 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/cardBanner/add` | `POST` | `/card/api/card-banner` |
| `GET` | `/ucard/cardBanner/changeStatus` | `GET` | `/card/api/card-banner/change-status` |
| `GET` | `/ucard/cardBanner/delete` | `DELETE` | `/card/api/card-banner` |
| `POST` | `/ucard/cardBanner/findAll` | `POST` | `/card/api/card-banner/all` |
| `GET` | `/ucard/cardBanner/findById` | `GET` | `/card/api/card-banner/{id}` |
| `GET` | `/ucard/cardBanner/findNormalInfo` | `GET` | `/card/api/card-banner/normal-info` |
| `POST` | `/ucard/cardBanner/update` | `PUT` | `/card/api/card-banner` |

### Cardholder（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/cardholder/add` | `POST` | `/card/api/cardholder` |
| `GET` | `/ucard/cardholder/delete` | `DELETE` | `/card/api/cardholder` |
| `GET` | `/ucard/cardholder/findById` | `GET` | `/card/api/cardholder/{id}` |
| `GET` | `/ucard/cardholder/findByUid` | `GET` | `/card/api/cardholder/by-uid` |
| `POST` | `/ucard/cardholder/update` | `PUT` | `/card/api/cardholder` |

### ChannelCard（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/channelCard/appFindAll` | `GET` | `/card/api/channel-card/all` |
| `POST` | `/ucard/channelCard/findAll` | `POST` | `/card/api/channel-card/all` |
| `GET` | `/ucard/channelCard/findById/{id}` | `GET` | `/card/api/channel-card/{id}` |
| `GET` | `/ucard/channelCard/oneClickUpdate` | `GET` | `/card/api/channel-card/one-click-update` |
| `POST` | `/ucard/channelCard/update` | `PUT` | `/card/api/channel-card` |

### ChannelLevel（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/channelLevel/add` | `POST` | `/card/api/channel-level` |
| `GET` | `/ucard/channelLevel/delete` | `DELETE` | `/card/api/channel-level` |
| `POST` | `/ucard/channelLevel/findAll` | `POST` | `/card/api/channel-level/all` |
| `POST` | `/ucard/channelLevel/update` | `PUT` | `/card/api/channel-level` |
| `POST` | `/ucard/channelLevel/updateStatus` | `PUT` | `/card/api/channel-level/status` |

### ChannelUser（10 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/channelUser/add` | `POST` | `/card/api/channel-user` |
| `GET` | `/ucard/channelUser/appFindAll` | `GET` | `/card/api/channel-user/all` |
| `GET` | `/ucard/channelUser/appFindById/{id}` | `GET` | `/card/api/channel-user/{id}` |
| `GET` | `/ucard/channelUser/appFindChannelLevel` | `GET` | `/card/api/channel-user/app-find-channel-level` |
| `GET` | `/ucard/channelUser/deleteChannelUser` | `DELETE` | `/card/api/channel-user/channel-user` |
| `POST` | `/ucard/channelUser/findAll` | `POST` | `/card/api/channel-user/all` |
| `GET` | `/ucard/channelUser/findChannelLevel` | `GET` | `/card/api/channel-user/channel-level` |
| `GET` | `/ucard/channelUser/findChannelUserInfo` | `GET` | `/card/api/channel-user/channel-user-info` |
| `POST` | `/ucard/channelUser/update` | `PUT` | `/card/api/channel-user` |
| `POST` | `/ucard/channelUser/updatePwd` | `PUT` | `/card/api/channel-user/pwd` |

### DecryptApi（2 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/sign/Decryption` | `POST` | `/card/api/sign/decryption` |
| `POST` | `/ucard/sign/encryption` | `POST` | `/card/api/sign/encryption` |

### Dic（16 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/dic/findCardState` | `GET` | `/card/api/dic/country` |
| `GET` | `/ucard/dic/findCardState` | `GET` | `/card/api/dic/card-state` |
| `GET` | `/ucard/dic/findCountry` | `GET` | `/card/api/dic/country` |
| `GET` | `/ucard/dic/findCountry` | `GET` | `/card/api/dic/card-state` |
| `GET` | `/ucard/dic/findJumpAddress` | `GET` | `/card/api/dic/jump-address` |
| `GET` | `/ucard/dic/findKycState` | `GET` | `/card/api/dic/kyc-state` |
| `GET` | `/ucard/dic/findNetwokList` | `GET` | `/card/api/dic/netwok-list` |
| `GET` | `/ucard/dic/findPayType` | `GET` | `/card/api/dic/pay-type` |
| `GET` | `/ucard/dic/findProtocolType` | `GET` | `/card/api/dic/protocol-type` |
| `GET` | `/ucard/dic/findRecordState` | `GET` | `/card/api/dic/record-state` |
| `GET` | `/ucard/dic/findSysConfigType` | `GET` | `/card/api/dic/sys-config-type` |
| `GET` | `/ucard/dic/findUserState` | `GET` | `/card/api/dic/user-state` |
| `GET` | `/ucard/dic/getCountryCode` | `GET` | `/card/api/dic/country-code` |
| `GET` | `/ucard/dic/getLanguage` | `GET` | `/card/api/dic/language` |
| `GET` | `/ucard/dic/getNetwokList` | `GET` | `/card/api/dic/get-netwok-list` |
| `GET` | `/ucard/dic/getWalletOperationType` | `GET` | `/card/api/dic/wallet-operation-type` |

### EmailTemplate（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/emailTemplate/add` | `POST` | `/card/api/email-template` |
| `GET` | `/ucard/emailTemplate/delete` | `DELETE` | `/card/api/email-template` |
| `GET` | `/ucard/emailTemplate/findCode` | `GET` | `/card/api/email-template/code` |
| `POST` | `/ucard/emailTemplate/findList` | `POST` | `/card/api/email-template/list` |
| `POST` | `/ucard/emailTemplate/update` | `PUT` | `/card/api/email-template` |

### FileUpload（2 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/wiki/apiUploadImage` | `POST` | `/card/api/file/api-upload-image` |
| `POST` | `/ucard/wiki/uploadImage` | `POST` | `/card/api/file/upload-image` |

### FirmAccount（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/firmAccount/add` | `POST` | `/card/api/firm-account` |
| `GET` | `/ucard/firmAccount/delete` | `DELETE` | `/card/api/firm-account` |
| `GET` | `/ucard/firmAccount/find` | `GET` | `/card/api/firm-account` |
| `POST` | `/ucard/firmAccount/findList` | `POST` | `/card/api/firm-account/list` |
| `POST` | `/ucard/firmAccount/update` | `PUT` | `/card/api/firm-account` |

### Forecast（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/forecast/callback` | `POST` | `/card/api/forecast/callback` |
| `GET` | `/ucard/forecast/getForecastSlideshow` | `GET` | `/card/api/forecast/forecast-slideshow` |
| `GET` | `/ucard/forecast/login` | `GET` | `/card/api/forecast/login` |
| `GET` | `/ucard/forecast/login` | `GET` | `/card/api/forecast` |
| `POST` | `/ucard/forecast/saveSlideshow` | `POST` | `/card/api/forecast/slideshow` |

### Help（6 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/help/add` | `POST` | `/card/api/help` |
| `GET` | `/ucard/help/delete` | `DELETE` | `/card/api/help` |
| `GET` | `/ucard/help/findById` | `GET` | `/card/api/help/{id}` |
| `POST` | `/ucard/help/findList` | `POST` | `/card/api/help/list` |
| `POST` | `/ucard/help/pcFindList` | `POST` | `/card/api/help/pc-find-list` |
| `POST` | `/ucard/help/update` | `PUT` | `/card/api/help` |

### IosGoods（8 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/iosGoods/add` | `POST` | `/card/api/ios-goods` |
| `GET` | `/ucard/iosGoods/delete` | `DELETE` | `/card/api/ios-goods` |
| `GET` | `/ucard/iosGoods/findAll` | `GET` | `/card/api/ios-goods/all` |
| `GET` | `/ucard/iosGoods/findById` | `GET` | `/card/api/ios-goods/{id}` |
| `GET` | `/ucard/iosGoods/findByNum` | `GET` | `/card/api/ios-goods/by-num` |
| `POST` | `/ucard/iosGoods/findList` | `POST` | `/card/api/ios-goods/list` |
| `POST` | `/ucard/iosGoods/update` | `PUT` | `/card/api/ios-goods` |
| `POST` | `/ucard/iosGoods/verify` | `POST` | `/card/api/ios-goods/verify` |

### IpWhitelist（3 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/ipWhitelist/add` | `POST` | `/card/api/ip-whitelist` |
| `GET` | `/ucard/ipWhitelist/delete` | `DELETE` | `/card/api/ip-whitelist` |
| `POST` | `/ucard/ipWhitelist/findList` | `POST` | `/card/api/ip-whitelist/list` |

### Kyc（1 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/kyc/apply` | `GET` | `/card/api/kyc/apply` |

### Log（1 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/log/findList` | `POST` | `/card/api/log/list` |

### Menu（8 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/menu/add` | `POST` | `/card/api/menu` |
| `GET` | `/ucard/menu/delete` | `DELETE` | `/card/api/menu` |
| `POST` | `/ucard/menu/findAll` | `POST` | `/card/api/menu/all` |
| `GET` | `/ucard/menu/findById` | `GET` | `/card/api/menu/{menuId}` |
| `POST` | `/ucard/menu/findByUser` | `POST` | `/card/api/menu/by-user` |
| `GET` | `/ucard/menu/roleMenuTreeselect` | `GET` | `/card/api/menu/role-menu-treeselect` |
| `POST` | `/ucard/menu/treeselect` | `POST` | `/card/api/menu/treeselect` |
| `POST` | `/ucard/menu/update` | `PUT` | `/card/api/menu` |

### Merchant（2 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/merchant/findInfo` | `GET` | `/card/api/merchant/info` |
| `POST` | `/ucard/merchant/rechargeList` | `POST` | `/card/api/merchant/recharge-list` |

### MessageTemplate（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/messageTemplate/add` | `POST` | `/card/api/message-template` |
| `GET` | `/ucard/messageTemplate/delete` | `DELETE` | `/card/api/message-template` |
| `GET` | `/ucard/messageTemplate/findCode` | `GET` | `/card/api/message-template/code` |
| `POST` | `/ucard/messageTemplate/findList` | `POST` | `/card/api/message-template/list` |
| `POST` | `/ucard/messageTemplate/update` | `PUT` | `/card/api/message-template` |

### Notice（7 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/notice/add` | `POST` | `/card/api/notice` |
| `GET` | `/ucard/notice/delete` | `DELETE` | `/card/api/notice` |
| `POST` | `/ucard/notice/findAll` | `POST` | `/card/api/notice/all` |
| `GET` | `/ucard/notice/findById` | `GET` | `/card/api/notice/{id}` |
| `POST` | `/ucard/notice/findList` | `POST` | `/card/api/notice/list` |
| `GET` | `/ucard/notice/send` | `GET` | `/card/api/notice/send` |
| `POST` | `/ucard/notice/update` | `PUT` | `/card/api/notice` |

### PayTypeSate（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/PayTypeSate/add` | `POST` | `/card/api/pay-type-sate` |
| `GET` | `/ucard/PayTypeSate/find` | `GET` | `/card/api/pay-type-sate` |
| `POST` | `/ucard/PayTypeSate/findList` | `POST` | `/card/api/pay-type-sate/list` |
| `GET` | `/ucard/PayTypeSate/findManage` | `GET` | `/card/api/pay-type-sate/manage` |
| `POST` | `/ucard/PayTypeSate/update` | `PUT` | `/card/api/pay-type-sate` |

### Product（11 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/product/OnAndOff` | `GET` | `/card/api/product/on-and-off` |
| `POST` | `/ucard/product/add` | `POST` | `/card/api/product` |
| `GET` | `/ucard/product/delete` | `DELETE` | `/card/api/product` |
| `GET` | `/ucard/product/findById` | `GET` | `/card/api/product/{id}` |
| `POST` | `/ucard/product/findList` | `POST` | `/card/api/product/list` |
| `GET` | `/ucard/product/orderFinish` | `GET` | `/card/api/product/order-finish` |
| `POST` | `/ucard/product/orderList` | `POST` | `/card/api/product/order-list` |
| `POST` | `/ucard/product/pay` | `POST` | `/card/api/product/pay` |
| `POST` | `/ucard/product/pcFindList` | `POST` | `/card/api/product/pc-find-list` |
| `POST` | `/ucard/product/update` | `PUT` | `/card/api/product` |
| `GET` | `/ucard/product/userAssets` | `GET` | `/card/api/product/user-assets` |

### ProductType（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/productType/add` | `POST` | `/card/api/product-type` |
| `GET` | `/ucard/productType/delete` | `DELETE` | `/card/api/product-type` |
| `GET` | `/ucard/productType/findAll` | `GET` | `/card/api/product-type/all` |
| `POST` | `/ucard/productType/findList` | `POST` | `/card/api/product-type/list` |
| `GET` | `/ucard/productType/pcFindAll` | `GET` | `/card/api/product-type/pc-find-all` |

### RechargeHistory（1 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/rechargeHistory/findList` | `POST` | `/card/api/recharge-history/list` |

### Role（7 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/role/add` | `POST` | `/card/api/role` |
| `POST` | `/ucard/role/changeStatus` | `POST` | `/card/api/role/change-status` |
| `GET` | `/ucard/role/delete` | `DELETE` | `/card/api/role` |
| `POST` | `/ucard/role/findAll` | `POST` | `/card/api/role/all` |
| `GET` | `/ucard/role/findById` | `GET` | `/card/api/role/{roleId}` |
| `GET` | `/ucard/role/findNormalRole` | `GET` | `/card/api/role/normal-role` |
| `POST` | `/ucard/role/update` | `PUT` | `/card/api/role` |

### Stat（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/stat/adminStat` | `POST` | `/card/api/stat/admin-stat` |
| `POST` | `/ucard/stat/appStat` | `POST` | `/card/api/stat/app-stat` |
| `GET` | `/ucard/stat/appTotal` | `GET` | `/card/api/stat/app-total` |
| `POST` | `/ucard/stat/channelTotal` | `POST` | `/card/api/stat/channel-total` |
| `GET` | `/ucard/stat/total` | `GET` | `/card/api/stat/total` |

### SysApk（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/sysApk/add` | `POST` | `/card/api/sys-apk` |
| `GET` | `/ucard/sysApk/findLastVersion` | `GET` | `/card/api/sys-apk/last-version` |
| `POST` | `/ucard/sysApk/findList` | `POST` | `/card/api/sys-apk/list` |
| `GET` | `/ucard/sysApk/release` | `GET` | `/card/api/sys-apk/release` |
| `POST` | `/ucard/sysApk/update` | `PUT` | `/card/api/sys-apk` |

### SysBrokerageRates（3 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/sysBrokerageRates/add` | `POST` | `/card/api/sys-brokerage-rates` |
| `POST` | `/ucard/sysBrokerageRates/findAll` | `POST` | `/card/api/sys-brokerage-rates/all` |
| `POST` | `/ucard/sysBrokerageRates/update` | `PUT` | `/card/api/sys-brokerage-rates` |

### SysInfo（7 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/info/add` | `POST` | `/card/api/info` |
| `GET` | `/ucard/info/changeStatus` | `GET` | `/card/api/info/change-status` |
| `GET` | `/ucard/info/delete` | `DELETE` | `/card/api/info` |
| `POST` | `/ucard/info/findAll` | `POST` | `/card/api/info/all` |
| `GET` | `/ucard/info/findById` | `GET` | `/card/api/info/{id}` |
| `GET` | `/ucard/info/findConfigInfo` | `GET` | `/card/api/info/config-info` |
| `POST` | `/ucard/info/update` | `PUT` | `/card/api/info` |

### SysUser（14 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/user/IssueGoogleSecretkey` | `GET` | `/card/api/user/issue-google-secretkey` |
| `DELETE` | `/ucard/user/delUser/{userId}` | `DELETE` | `/card/api/user/{userId}` |
| `POST` | `/ucard/user/findList` | `POST` | `/card/api/user/list` |
| `GET` | `/ucard/user/findToken` | `GET` | `/card/api/user/token` |
| `GET` | `/ucard/user/generateToken` | `GET` | `/card/api/user/generate-token` |
| `GET` | `/ucard/user/getPasswordOrKey` | `GET` | `/card/api/user/password-or-key` |
| `POST` | `/ucard/user/resetPwd` | `POST` | `/card/api/user/reset-pwd` |
| `POST` | `/ucard/user/resetUserPwd` | `POST` | `/card/api/user/reset-user-pwd` |
| `POST` | `/ucard/user/signUp` | `POST` | `/card/api/user/sign-up` |
| `GET` | `/ucard/user/upGoogleSecretkey` | `GET` | `/card/api/user/up-google-secretkey` |
| `POST` | `/ucard/user/updateDeviceModel` | `PUT` | `/card/api/user/device-model` |
| `POST` | `/ucard/user/updateState` | `PUT` | `/card/api/user/state` |
| `POST` | `/ucard/user/updateUser` | `PUT` | `/card/api/user` |
| `POST` | `/ucard/user/verifyPwd` | `POST` | `/card/api/user/verify-pwd` |

### TollAddress（5 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/tollAddress/add` | `POST` | `/card/api/toll-address` |
| `GET` | `/ucard/tollAddress/delete` | `DELETE` | `/card/api/toll-address` |
| `GET` | `/ucard/tollAddress/findAll` | `GET` | `/card/api/toll-address/all` |
| `POST` | `/ucard/tollAddress/findList` | `POST` | `/card/api/toll-address/list` |
| `POST` | `/ucard/tollAddress/update` | `PUT` | `/card/api/toll-address` |

### User（11 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/appUserSys/IssueGoogleSecretkey` | `GET` | `/card/api/app-user-sys/issue-google-secretkey` |
| `GET` | `/ucard/appUserSys/checkGoogleSecretkey` | `GET` | `/card/api/app-user-sys/google-secretkey` |
| `GET` | `/ucard/appUserSys/findKycFile` | `GET` | `/card/api/app-user-sys/kyc-file` |
| `POST` | `/ucard/appUserSys/findList` | `POST` | `/card/api/app-user-sys/list` |
| `GET` | `/ucard/appUserSys/forgetPasswrod` | `GET` | `/card/api/app-user-sys/forget-passwrod` |
| `GET` | `/ucard/appUserSys/logout` | `GET` | `/card/api/app-user-sys/logout` |
| `GET` | `/ucard/appUserSys/signOut` | `GET` | `/card/api/app-user-sys/sign-out` |
| `GET` | `/ucard/appUserSys/upGoogleSecretkey` | `GET` | `/card/api/app-user-sys/up-google-secretkey` |
| `POST` | `/ucard/appUserSys/upPayPasswrod` | `POST` | `/card/api/app-user-sys/up-pay-passwrod` |
| `GET` | `/ucard/appUserSys/upWalletState` | `GET` | `/card/api/app-user-sys/up-wallet-state` |
| `POST` | `/ucard/appUserSys/updaePwd` | `POST` | `/card/api/app-user-sys/updae-pwd` |

### Wallet（10 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/wallet/findReading` | `GET` | `/card/api/wallet/reading` |
| `POST` | `/ucard/wallet/findWalletLog` | `POST` | `/card/api/wallet/find-wallet-log` |
| `GET` | `/ucard/wallet/topinUsdtAddress` | `GET` | `/card/api/wallet/topin-usdt-address` |
| `POST` | `/ucard/wallet/topinUsdtNotify` | `POST` | `/card/api/wallet/topin-usdt-notify` |
| `POST` | `/ucard/wallet/transfer` | `POST` | `/card/api/wallet/transfer` |
| `GET` | `/ucard/wallet/transferReading` | `GET` | `/card/api/wallet/transfer-reading` |
| `POST` | `/ucard/wallet/walletLog` | `POST` | `/card/api/wallet/wallet-log` |
| `POST` | `/ucard/wallet/walletLogStat` | `POST` | `/card/api/wallet/wallet-log-stat` |
| `POST` | `/ucard/wallet/walletTopUp` | `POST` | `/card/api/wallet/wallet-top-up` |
| `POST` | `/ucard/wallet/withdrawalNotify` | `POST` | `/card/api/wallet/withdrawal-notify` |

### WalletEntrance（2 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/walletEntrance/findList` | `POST` | `/card/api/wallet-entrance/list` |
| `GET` | `/ucard/walletEntrance/operate` | `GET` | `/card/api/wallet-entrance/operate` |

### WalletNetworkTopUp（3 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/walletNetworkTopUp/add` | `POST` | `/card/api/wallet-network-top-up` |
| `GET` | `/ucard/walletNetworkTopUp/close` | `GET` | `/card/api/wallet-network-top-up/close` |
| `POST` | `/ucard/walletNetworkTopUp/findList` | `POST` | `/card/api/wallet-network-top-up/list` |

### WalletToWebLog（6 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/walletToWebLog/calculateAmount` | `GET` | `/card/api/wallet-to-web-log/calculate-amount` |
| `POST` | `/ucard/walletToWebLog/create` | `POST` | `/card/api/wallet-to-web-log` |
| `POST` | `/ucard/walletToWebLog/findList` | `POST` | `/card/api/wallet-to-web-log/list` |
| `GET` | `/ucard/walletToWebLog/findRates` | `GET` | `/card/api/wallet-to-web-log/rates` |
| `GET` | `/ucard/walletToWebLog/pass` | `GET` | `/card/api/wallet-to-web-log/pass` |
| `GET` | `/ucard/walletToWebLog/reject` | `GET` | `/card/api/wallet-to-web-log/reject` |

### WalletToWebRates（2 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `GET` | `/ucard/withdrawalRates/find` | `GET` | `/card/api/withdrawal-rates` |
| `POST` | `/ucard/withdrawalRates/update` | `PUT` | `/card/api/withdrawal-rates` |

### WalletTransfetRates（3 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/walletTransfetRates/add` | `POST` | `/card/api/wallet-transfet-rates` |
| `GET` | `/ucard/walletTransfetRates/findList` | `GET` | `/card/api/wallet-transfet-rates/list` |
| `POST` | `/ucard/walletTransfetRates/update` | `PUT` | `/card/api/wallet-transfet-rates` |

### Webhook（4 个）

| 旧方法 | 旧接口 | 新方法 | 新接口 |
|---|---|---|---|
| `POST` | `/ucard/webhook/agentNotify` | `POST` | `/card/api/webhook/agent-notify` |
| `GET` | `/ucard/webhook/info` | `GET` | `/card/api/webhook/info` |
| `POST` | `/ucard/webhook/resetAddres` | `POST` | `/card/api/webhook/reset-addres` |
| `GET` | `/ucard/webhook/state` | `GET` | `/card/api/webhook/state` |
