-- ============================================================
-- 理财模块测试数据：finance_product_type / finance_product / finance_product_order
-- 仅用于本地测试环境，请勿在生产库执行
-- 生成时间：2026-11-09
--
-- 取值说明：
--   whether_current：1=活期，2=定期（代码以 PublicEnums.TOW(2) 判定为定期）
--   product_state_id：1=已上架，2=已下架
--   order_state：posted=持有中，finish=已结束
--   interest_rate 存小数（0.0450 = 4.50%），与 ProductServiceImpl 入库逻辑一致
--   收益公式：本金 * 年利率 / 365 * 天数，保留 6 位小数
-- ============================================================

SET NAMES utf8mb3;

-- ---------------- 1. 产品类型 ----------------
INSERT INTO `finance_product_type` (`id`, `language`, `name`, `whether_current`, `setTime`, `gmtModified`) VALUES
(1, 'zh', '活期理财', 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(2, 'zh', '定期理财', 2, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(3, 'en', 'Current Plan', 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(4, 'en', 'Fixed-term Plan', 2, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(5, 'tw', '活期理財', 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(6, 'tw', '定期理財', 2, '2026-11-09 12:00:00', '2026-11-09 12:00:00');

-- ---------------- 2. 理财产品 ----------------
INSERT INTO `finance_product` (`id`, `language`, `cover_img`, `product_name`, `product_info`, `product_type_id`, `product_type`, `funds_type`, `product_state_id`, `product_state`, `initial_monery`, `product_time`, `interest_rate`, `whether_current`, `delete_state`, `setTime`, `gmtModified`) VALUES
(1, 'zh', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily.png', '天天盈活期理财', '随存随取，按日计息，年化收益稳定，适合短期闲置资金。', 1, '活期理财', 'USD', 1, '已上架', 100.000, 0, 0.0350, 1, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(2, 'zh', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90.png', '稳盈90天定期', '90天封闭期，到期一次性还本付息，收益稳健。', 2, '定期理财', 'USD', 1, '已上架', 1000.000, 90, 0.0450, 2, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(3, 'zh', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-180.png', '稳盈180天定期', '180天封闭期，适合中期闲置资金，年化收益更高。', 2, '定期理财', 'USDT', 1, '已上架', 500.000, 180, 0.0520, 2, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(4, 'zh', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-365.png', '尊享365天定期', '365天长期持有，锁定全年收益，该产品已下架。', 2, '定期理财', 'USD', 2, '已下架', 5000.000, 365, 0.0650, 2, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(5, 'en', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily-en.png', 'Daily Yield Current', 'Deposit and withdraw anytime, interest accrued daily.', 3, 'Current Plan', 'USD', 1, '已上架', 100.000, 0, 0.0350, 1, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(6, 'en', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90-en.png', 'Steady Yield 90D', '90-day fixed term with principal and interest repaid at maturity.', 4, 'Fixed-term Plan', 'USD', 1, '已上架', 1000.000, 90, 0.0450, 2, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(7, 'tw', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily-tw.png', '天天盈活期理財', '隨存隨取，按日計息，適合短期閒置資金。', 5, '活期理財', 'USD', 1, '已上架', 100.000, 0, 0.0350, 1, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00'),
(8, 'tw', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-180-tw.png', '穩盈180天定期', '180天封閉期，到期還本付息，收益穩健。', 6, '定期理財', 'USD', 1, '已上架', 1000.000, 180, 0.0520, 2, 1, '2026-11-09 12:00:00', '2026-11-09 12:00:00');

-- ---------------- 3. 理财订单 ----------------
INSERT INTO `finance_product_order` (`id`, `uid`, `product_id`, `order_num`, `order_state`, `order_monery`, `product_name`, `product_img`, `product_type`, `funds_type`, `product_time`, `interest_rate`, `whether_current`, `estimate_income`, `actual_income`, `pay_time`, `expire_time`, `finish_time`, `setTime`, `gmtModified`, `pay_num`) VALUES
(1, '150130', 1, '1202608011000001951727', 'posted', 500.000, '天天盈活期理财', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily.png', '活期理财', 'USD', 0, 0.0350, 1, NULL, NULL, '2026-08-01 10:00:00', NULL, NULL, '2026-08-01 10:00:00', '2026-08-01 10:00:00', 5),
(2, '150365', 2, '1202608051420001954819', 'posted', 10000.000, '稳盈90天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90.png', '定期理财', 'USD', 90, 0.0450, 2, 110.958904, NULL, '2026-08-05 14:20:00', '2026-11-03 14:20:00', NULL, '2026-08-05 14:20:00', '2026-08-05 14:20:00', 100),
(3, '152176', 3, '1202606010930001978399', 'posted', 2000.000, '稳盈180天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-180.png', '定期理财', 'USDT', 180, 0.0520, 2, 51.287671, NULL, '2026-06-01 09:30:00', '2026-11-28 09:30:00', NULL, '2026-06-01 09:30:00', '2026-06-01 09:30:00', 20),
(4, '152200', 1, '1202609101645001978748', 'posted', 300.000, '天天盈活期理财', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily.png', '活期理财', 'USD', 0, 0.0350, 1, NULL, NULL, '2026-09-10 16:45:00', NULL, NULL, '2026-09-10 16:45:00', '2026-09-10 16:45:00', 3),
(5, '152403', 2, '1202605011100001981424', 'finish', 5000.000, '稳盈90天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90.png', '定期理财', 'USD', 90, 0.0450, 2, 55.479452, 55.479452, '2026-05-01 11:00:00', '2026-07-30 11:00:00', '2026-07-30 11:00:00', '2026-05-01 11:00:00', '2026-07-30 11:00:00', 50),
(6, '152748', 3, '1202604011015001985946', 'finish', 8000.000, '稳盈180天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-180.png', '定期理财', 'USDT', 180, 0.0520, 2, 205.150685, 205.150685, '2026-04-01 10:15:00', '2026-09-28 10:15:00', '2026-09-28 10:15:00', '2026-04-01 10:15:00', '2026-09-28 10:15:00', 80),
(7, '152749', 1, '1202607151305001985996', 'finish', 1000.000, '天天盈活期理财', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily.png', '活期理财', 'USD', 0, 0.0350, 1, NULL, 5.753425, '2026-07-15 13:05:00', NULL, '2026-09-13 13:05:00', '2026-07-15 13:05:00', '2026-09-13 13:05:00', 10),
(8, '153548', 4, '1202510010900001996420', 'finish', 20000.000, '尊享365天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-365.png', '定期理财', 'USD', 365, 0.0650, 2, 1300.000000, 1300.000000, '2025-10-01 09:00:00', '2026-10-01 09:00:00', '2026-10-01 09:00:00', '2025-10-01 09:00:00', '2026-10-01 09:00:00', 200),
(9, '150130', 5, '1202609011000001952023', 'posted', 200.000, 'Daily Yield Current', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily-en.png', 'Current Plan', 'USD', 0, 0.0350, 1, NULL, NULL, '2026-09-01 10:00:00', NULL, NULL, '2026-09-01 10:00:00', '2026-09-01 10:00:00', 2),
(10, '150365', 6, '1202609211530001955115', 'posted', 1500.000, 'Steady Yield 90D', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90-en.png', 'Fixed-term Plan', 'USD', 90, 0.0450, 2, 16.643836, NULL, '2026-09-21 15:30:00', '2026-12-20 15:30:00', NULL, '2026-09-21 15:30:00', '2026-09-21 15:30:00', 15),
(11, '152176', 2, '1202610101040001978695', 'posted', 3000.000, '稳盈90天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90.png', '定期理财', 'USD', 90, 0.0450, 2, 33.287671, NULL, '2026-10-10 10:40:00', '2027-01-08 10:40:00', NULL, '2026-10-10 10:40:00', '2026-10-10 10:40:00', 30),
(12, '152200', 3, '1202601050910001979044', 'finish', 1200.000, '稳盈180天定期', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-180.png', '定期理财', 'USDT', 180, 0.0520, 2, 30.772603, 30.772603, '2026-01-05 09:10:00', '2026-07-04 09:10:00', '2026-07-04 09:10:00', '2026-01-05 09:10:00', '2026-07-04 09:10:00', 12),
(13, '152403', 1, '1202610201820001981720', 'posted', 800.000, '天天盈活期理财', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/current-daily.png', '活期理财', 'USD', 0, 0.0350, 1, NULL, NULL, '2026-10-20 18:20:00', NULL, NULL, '2026-10-20 18:20:00', '2026-10-20 18:20:00', 8),
(14, '152748', 6, '1202609011150001986242', 'posted', 2000.000, 'Steady Yield 90D', 'https://onetokenapp.s3.ap-east-1.amazonaws.com/product/fixed-90-en.png', 'Fixed-term Plan', 'USD', 90, 0.0450, 2, 22.191781, NULL, '2026-09-01 11:50:00', '2026-11-30 11:50:00', NULL, '2026-09-01 11:50:00', '2026-09-01 11:50:00', 20);

-- ---------------- 校验 ----------------
SELECT 'product_type' AS tbl, COUNT(*) AS cnt FROM finance_product_type
UNION ALL SELECT 'product', COUNT(*) FROM finance_product
UNION ALL SELECT 'product_order', COUNT(*) FROM finance_product_order;
