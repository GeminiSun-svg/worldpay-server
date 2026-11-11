# WorldPay 2.0 迁移改造全量变更文档

> 对比基准：旧项目 `/Users/linleiqin/IdeaProjects/worldpay_backend`（`com.ucard`，Spring Boot 2.3 / Java 11）
> 目标项目：`/Users/linleiqin/IdeaProjects/worldpay-server`（`com.worldpay`，Spring Boot 3.5.0 / Java 17）
> 更新日期：2026-11-07

---

## 一、技术栈升级

| 项目 | 旧版 | 新版 |
|---|---|---|
| Spring Boot | 2.3.x | 3.5.0 |
| JDK | 11 | 17（shell 环境需设 `JAVA_HOME` 指向 JDK 17） |
| 包名 | `com.ucard` | `com.worldpay`（全量替换） |
| 命名空间 | `javax.*` | `jakarta.*` |
| MyBatis-Plus | 旧版 | 3.5.7（`mybatis-plus-spring-boot3-starter`） |
| PageHelper | 旧版 | 2.1.0 |
| 数据库连接池 | Druid | Hikari（pom 无 Druid 依赖，参数等价映射） |
| MySQL 驱动 | `com.mysql.jdbc.Driver` | `com.mysql.cj.jdbc.Driver`（9.x 已移除旧类） |
| JWT | jjwt 旧 API | jjwt 0.12.6（`verifyWith`/`parseSignedClaims` 新 API） |
| Spring Security | 5.x（`WebSecurityConfigurerAdapter`） | 6.x（`SecurityFilterChain` Bean + `requestMatchers`） |
| API 文档 | 无 | SpringDoc OpenAPI 2.8.6（`/v3/api-docs`、Swagger UI） |
| Redis 客户端 | Jedis 配置写法 | Lettuce（`spring.data.redis` 命名空间） |

---

## 二、接口层重构（Service 反模式 → 独立 Controller）

### 2.1 架构调整

旧项目「Service 接口兼 Controller」的反模式被拆除：

- 新建 **50 个 Controller**（`com.worldpay.controller`），共 **292 个端点**（含新增 Insights）
- 50 个 Service 接口剥离全部 Web 注解（`@RequestMapping`/`@GetMapping`/`@PostMapping`/`@Operation`），只保留方法声明
- 51 个 ServiceImpl 由 `@RestController` 改为 `@Service`

### 2.2 URL 规范（完全替换，不保留旧 URL）

| 项 | 旧 | 新 |
|---|---|---|
| 前缀 | 无版本概念 | `/card/api` |
| 命名 | 驼峰（`/appUser/findToken`） | kebab-case（`/app-user/token`） |
| 版本路由 | header 区分 v1/v2 | 拆为独立路径（如 `sign-up` 与裸 POST） |
| ID 查询 | `?id=` 参数 | 路径变量 `/{id}`（10 处） |

- 旧 295 个接口 → 新 291 个（v1/v2 header 版本合并），完整对照表见 [api-mapping.md](api-mapping.md)
- HTTP 方法分布：GET 124 / POST 116 / PUT 31 / DELETE 20

### 2.3 已知遗留

- `signUpV2` 占用裸 `POST /card/api/app-user`（语义不明确，建议后续改 `/v2/sign-up`）
- `by-num`/`by-uid` 等字段查询路径保留 kebab 命名，未进一步收敛

---

## 三、安全体系重写

### 3.1 Spring Security 6

- 新建 `WebSecurityConfig`：`SecurityFilterChain` Bean、`AuthenticationManager` 经 `AuthenticationConfiguration` 获取、白名单全部使用 `/card/api/**` 新路径
- 修复 Spring Security 6 的 PathPattern 限制：`/**/*.html` 非法 → 改为 `/*.html`、`/*.css`、`/*.js`（此前导致所有请求 500）

### 3.2 JWT 密钥修复（⚠️ 破坏性变更）

- **存量 Bug**：旧 `SIGNING_KEY` 仅 18 字节，jjwt 0.12 强制 HS256 密钥 ≥ 32 字节，导致**所有需登录接口验签必失败**
- 修复：密钥扩至 64 字节，签发/校验统一收口到 `Constants.jwtKey()`
- **副作用：修复前签发的所有旧 token 全部失效，用户需重新登录**

### 3.3 白名单判定方式

- `JWTAuthenticationFilter`、`GlobalDecryptRequestAdvice` 的放行判断由 `getRequestURI()`（含 context-path）改为 `getServletPath()`，context-path 变更不再导致白名单失效

---

## 四、加解密机制变更（⚠️ 接口行为变更）

| 项 | 旧 | 新 |
|---|---|---|
| 响应 | 整体 AES 加密为 `{"data":"<Base64>"}` | **明文** `ResponseBase` |
| 请求 | 必须加密 | 推荐明文；兼容加密提交 |
| 组件 | `GlobalEncryptResponseAdvice` + `ResponseWrapper` | **均已删除**；`GlobalDecryptRequestAdvice` 保留 |
| 过滤器异常输出 | AES 加密 JSON | 明文 JSON |

- AES 参数不变：`AES/ECB/PKCS5Padding`，key `Ej0c8VSW9l2sP7kM`
- 7 个不支持加密的接口白名单保留（webhook、文件上传、sign 等）

---

## 五、配置体系重构

### 5.1 单文件化 + 密钥外置

- 删除 `application-dev/test/prod.yml`，只保留 [application.yml](../src/main/resources/application.yml)
- **密钥全部占位符化**（28 处 `${ENV:}`）：MySQL 密码、银行 appId/appSecret/RSA 私钥/AES Key、nationTransfer、yunmail、百度、极光、个推、monitor、telegram、stripe、EMS、S3、forecast
- 真实值存放于 [application-local.yml](../src/main/resources/application-local.yml)（**已 gitignore，不入库**），经 `spring.config.import: optional:classpath:/application-local.yml` 加载，优先级高于主 yml
- 修正旧 yml 的 `worldpay.*` 前缀与代码 `@Value("bank.*")` 不匹配问题（原配置实际未生效）
- 曾因密钥泄露被 GitHub Push Protection 拦截；已重建 git 历史为单一干净提交后推送成功

### 5.2 其他配置调整

| 项 | 说明 |
|---|---|
| context-path | 代码内白名单改用 `getServletPath()`，与 context-path 解耦 |
| i18n | 文案合并至 `resources/i18n/`（每语言 119 key），`I18nConfig` basename 同步；语言 Header 实际取值为 **`lang`**（非 `language`） |
| Actuator/Prometheus | Boot 3 属性名适配 |

---

## 六、数据层与实体修复

- [AppUserEntity](../src/main/java/com/worldpay/entity/appuser/AppUserEntity.java) 重写：金额字段统一 `Double`（`walletBalance`/`freezeBalance`/`openFreezeBalance`/`withdrawableBalance`），时间字段 `Date`，`walletState` 改 `Boolean`，继承 `PageQueryHelperEntity` —— 一举消除 346 个 BigDecimal/Double 混用编译错误中的绝大多数
- 批量为 18 个文件补充 `com.worldpay.utils.api.*` 显式 import（通配 import 不覆盖子包，共 27 条）
- 批量为全部 `@Value("${xxx}")` 补默认值 `${xxx:}`，避免缺配置启动失败

---

## 七、新增功能

### 7.1 卡片消费洞察（Insights）

| 项 | 内容 |
|---|---|
| 端点 | `GET /card/api/insights`（单接口） |
| 参数 | `startDate`、`endDate`（yyyy-MM-dd，缺省近 30 天）、`dimension=day/week/month/quarter`（默认 week）、`userBankcardId`（可选） |
| 跨度限制 | 90 天（quarter 维度 370 天） |
| 返回 | 区间合计（消费/充值/退款/笔数）+ 上一周期环比 + 按维度聚合列表 |
| 统计口径 | 仅当前登录用户（JWT 解析 uid）、`order_state='posted'`、金额取 `local_currency_amt` 绝对值；AUTH=消费、TOPUP=充值、REFUND=退款 |
| 涉及文件 | `InsightsController`、`InsightsService(Impl)`、`CardTransactionDao`（4 条聚合 SQL）、`CardInsightEntity` |
| 专项文档 | [api-insights.md](api-insights.md) |

### 7.2 接口描述注解

291+1 个端点的 `@Operation(summary)` 全部补齐中文描述（关键业务接口人工精写约 90 条），Swagger UI 同步可见。

---

## 八、代码清理

### 8.1 A 组（7 个文件 + 1 空目录）

`GmtFastJsonConfig`（空类）、`HeardUtil`（空类）、`ResponseWrapper`（随响应加密删除）、`ExcelAnnotation`（0 引用）、resources 根目录 3 份旧 `messages_*.properties`（已合并入 i18n/）、空 `resources/mapper/` 目录

### 8.2 B 组（31 个零引用文件）

- 未使用 DTO 14 个：AgentNotifyEntity、ForecastCallBackEntity、ForecastTopUpAndWithdrawEntity、KycBindingEntity、MerchantRecharge、QueryCardOrderInfoEntity、TopUpEntity、CardCloseApiResEntity、CardRechargeEntity、CardStatusChangeEntity、CardTransactionApiEntity、OpenCardResponseEntity、TransactionVerificationCodeEntity、UserCardInfoEntity
- 未使用枚举 5 个：ApiStateCodeEnums、CardLanguageEnums、FundsTypeEnums、ProductTypeEnums、SysBrokerageRatesEnums
- 未使用查询对象 2 个：SignUpRequest、UserInfoResponse
- 未使用实体 2 个：IdCardInfo、TotalAccounts
- 未使用 DAO 3 个：AppUserMapper、MerchantTradeFeeDao、NationDataDao
- 异常基类 2 个：BusinessException、SystemException
- 未生效拦截器 2 个：InterceptorConfig、ApiIdempotentInterceptor
- 旧版三方客户端 1 个：UCardApiOld

**清理后 Java 文件 523 → 488，对外接口零变化（OpenAPI 路径数前后一致）。**

---

## 九、新增基础设施

| 文件 | 用途 |
|---|---|
| `RedisConfig` | 提供 `RedisTemplate<String,Object>` Bean（Jackson 序列化） |
| `AmazonS3Config` | 提供 `AmazonS3` Bean |
| `JPushUtils` / `AddressExtractor` | 空壳占位（三方未接入） |
| `GeTuiPushUtils.pushAll` | 补充缺失方法 |
| `LanguageInterceptor` | 加 `@Component`，`set`→`setLanguage` |
| `BaiduAuthApiUtil` | 配置为空时 init 直接返回（防空 URL 启动异常） |
| `TelegramNotificationUtil` | token 缺省值兜底 |

---

## 十、交付文档清单（docs/）

| 文档 | 内容 |
|---|---|
| [api-doc-app.md](api-doc-app.md) | 全量接口文档（292 端点 / 51 模块） |
| [api-doc-app-only.md](api-doc-app-only.md) | 仅 App 端接口（220+ 个） |
| [api-doc-app.html](api-doc-app.html) | 网页版（搜索/筛选） |
| [api-mapping.md](api-mapping.md) | 新旧接口对照表（295 → 291） |
| [api-insights.md](api-insights.md) | 洞察接口专项文档（手工维护） |
| migration-changes.md | 本文档 |

---

## 十一、遗留事项（非阻塞）

1. **三方 SDK 空壳**：UCard/Stripe/AWS S3/极光/个推等仅结构就位，密钥在 local yml 已就位但联调未做
2. **i18n 部分 key 缺失**：个别新接口提示语未翻译
3. **`signUpV2` 裸路径**：建议改 `/v2/sign-up`
4. **Insights 待联调**：开发期曾出现 500（手工构造会话数据不完整所致），需真实登录态验证
5. **AWS 密钥轮换建议**：`AKIARG6IBZE2...` 曾在 git 历史中出现（未推送成功），建议作废重发
6. **AppUserEntity 金额用 Double**：金融场景建议中期改 BigDecimal（需全链路评估，工作量大）
