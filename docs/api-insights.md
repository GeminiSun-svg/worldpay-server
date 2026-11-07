# 卡片消费洞察接口文档

> 模块：Insights · 消费洞察与月账单
> 适用端：App
> 更新时间：2026-11-07

---

## 1. 接口概述

一个接口完成卡片消费的全维度统计查询：**区间合计（消费 / 充值 / 退款 / 笔数）+ 上一周期环比 + 按时间维度聚合的明细列表**。

数据来源：`card_transaction` 表，仅统计当前登录用户（`uid` 由 JWT 解析，不接受传参）且交易状态为成功（`order_state = posted`）的记录。

| 项目 | 内容 |
|---|---|
| 请求方法 | `GET` |
| 接口路径 | `/card/api/insights` |
| 完整地址 | `http://{host}:8023/worldpay/card/api/insights` |
| 是否需要登录 | 是（Header 携带 `token`） |
| Content-Type | 无需请求体（Query 参数） |

### 请求头

| 请求头 | 必填 | 说明 |
|---|---|---|
| `token` | 是 | `Bearer <jwt>`，登录/注册接口签发 |
| `lang` | 否 | 语言：`en`（默认）/ `zh` / `tw` |

---

## 2. 请求参数（Query）

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `startDate` | string | 否 | 区间开始日期，格式 `yyyy-MM-dd`。缺省时 = `endDate` 往前推 29 天 |
| `endDate` | string | 否 | 区间结束日期，格式 `yyyy-MM-dd`。缺省时 = 当天 |
| `dimension` | string | 否 | 聚合维度：`day` 每天 / `week` 每周 / `month` 每月 / `quarter` 每三个月（自然季度）。默认 `week`，大小写不敏感 |
| `userBankcardId` | int | 否 | 卡 ID（`user_bankcard` 主键）。不传统计该用户全部卡片 |

**区间规则**

- 缺省区间：近 30 天（含当天）
- 最大跨度：`day` / `week` / `month` → **90 天**；`quarter` → **370 天**
- 起始晚于结束、格式非法、跨度超限 → 返回错误

**统计口径**

- 消费：`trans_type = AUTH`，金额取绝对值
- 充值：`trans_type = TOPUP`，金额取绝对值
- 退款：`trans_type = REFUND`，金额取绝对值
- 金额字段：`local_currency_amt`（本币金额）
- 周聚合以**当周周一**作为标签

---

## 3. 响应说明

统一响应体（明文，无加密）：

```json
{
  "rtncode": 200,
  "msg": "操作成功",
  "data": { }
}
```

### `data` 字段（区间合计 + 环比）

| 字段 | 类型 | 说明 |
|---|---|---|
| `consumeTotal` | double | 区间内消费合计（AUTH 绝对值） |
| `topupTotal` | double | 区间内充值合计（TOPUP） |
| `refundTotal` | double | 区间内退款合计（REFUND） |
| `totalCount` | int | 区间内交易总笔数 |
| `prevConsumeTotal` | double | 上一周期消费合计（与当前区间等长、紧邻其前，用于环比） |
| `prevTopupTotal` | double | 上一周期充值合计 |
| `prevRefundTotal` | double | 上一周期退款合计 |
| `prevTotalCount` | int | 上一周期交易笔数 |
| `startDate` | string | 实际生效的区间开始日期 |
| `endDate` | string | 实际生效的区间结束日期 |
| `list` | array | 按 `dimension` 聚合的明细列表（见下） |

### `list` 元素字段（时间维度明细）

| 字段 | 类型 | 说明 |
|---|---|---|
| `dateLabel` | string | 维度标签：`day`→`2026-11-07`；`week`→`2026-11-02`（周一）；`month`→`2026-11`；`quarter`→`2026-Q4` |
| `consumeTotal` | double | 该周期消费合计 |
| `topupTotal` | double | 该周期充值合计 |
| `refundTotal` | double | 该周期退款合计 |
| `totalCount` | int | 该周期交易笔数 |

> 无数据的周期不出现在列表中；`list` 按时间倒序排列。

---

## 4. 请求示例

### 4.1 每周消费（默认）

```bash
curl -X GET "http://localhost:8023/worldpay/card/api/insights" \
  -H "token: Bearer eyJhbGciOi..."
```

### 4.2 指定区间 + 按月 + 指定卡片

```bash
curl -X GET "http://localhost:8023/worldpay/card/api/insights?startDate=2026-08-01&endDate=2026-10-31&dimension=month&userBankcardId=153792" \
  -H "token: Bearer eyJhbGciOi..."
```

### 4.3 季度账单（近一年）

```bash
curl -X GET "http://localhost:8023/worldpay/card/api/insights?dimension=quarter&startDate=2025-11-01&endDate=2026-11-07" \
  -H "token: Bearer eyJhbGciOi..."
```

---

## 5. 响应示例

```json
{
  "rtncode": 200,
  "msg": "操作成功",
  "data": {
    "consumeTotal": 1250.75,
    "topupTotal": 3000.00,
    "refundTotal": 68.20,
    "totalCount": 45,
    "prevConsumeTotal": 980.00,
    "prevTopupTotal": 2500.00,
    "prevRefundTotal": 0.00,
    "prevTotalCount": 30,
    "startDate": "2026-10-09",
    "endDate": "2026-11-07",
    "list": [
      {
        "dateLabel": "2026-11-02",
        "consumeTotal": 320.50,
        "topupTotal": 1000.00,
        "refundTotal": 0.00,
        "totalCount": 8
      },
      {
        "dateLabel": "2026-10-26",
        "consumeTotal": 930.25,
        "topupTotal": 2000.00,
        "refundTotal": 68.20,
        "totalCount": 12
      }
    ]
  }
}
```

无流水时（列表为空、合计为 0，不报错）：

```json
{
  "rtncode": 200,
  "msg": "操作成功",
  "data": {
    "consumeTotal": 0, "topupTotal": 0, "refundTotal": 0, "totalCount": 0,
    "startDate": "2026-10-09", "endDate": "2026-11-07",
    "list": []
  }
}
```

---

## 6. 错误场景

| 场景 | rtncode | msg |
|---|---|---|
| 未携带 / 失效 token | 401 | `token不合法` |
| 未登录（Redis 会话不存在） | 403 | `请登录！！` |
| `dimension` 非法 | 500 | `dimension 仅支持 day/week/month/quarter` |
| 日期格式错误 / 跨度超限 | 500 | `日期格式应为 yyyy-MM-dd，且跨度不能超过 N 天` |

---

## 7. 前端使用建议

- **月账单页**：`dimension=month`，`list` 直接渲染月度卡片；`data` 顶部合计作为当前选中月汇总
- **消费趋势图**：`dimension=day`，`list` 作为折线图数据源（按 `dateLabel` 升序重排）
- **环比箭头**：`(consumeTotal - prevConsumeTotal) / prevConsumeTotal`，注意 `prevConsumeTotal = 0` 时不展示百分比
