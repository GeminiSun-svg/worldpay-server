package com.worldpay.api.response;

import java.util.List;
import java.util.Map;


import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "物流轨迹查询响应")
public class TrackingInfoEntity {

	@Schema(description = "响应状态码")
	private int code;
	@Schema(description = "响应数据")
	private DataInner data;

	@Data
	public static class DataInner {
		@Schema(description = "已接受的运单列表")
		private List<AcceptedItem> accepted;
	}

	@Data
	public static class AcceptedItem {
		@Schema(description = "承运商编码")
		private int carrier;
		@Schema(description = "运单号")
		private String number;
		@Schema(description = "运单标签")
		private String tag;
		@Schema(description = "物流轨迹信息")
		private TrackInfo track_info;
	}

	@Data
	public static class TrackInfo {
		@Schema(description = "最新物流事件")
		private Event latest_event;
		@Schema(description = "最新物流状态")
		private Status latest_status;
		@Schema(description = "物流里程碑列表")
		private List<Milestone> milestone;
		@Schema(description = "附加信息")
		private MiscInfo misc_info;
		@Schema(description = "收发货地址信息")
		private ShippingInfo shipping_info;
		@Schema(description = "时效指标")
		private TimeMetrics time_metrics;
		@Schema(description = "承运商轨迹")
		private Tracking tracking;
	}

	@Data
	public static class Event {
		@Schema(description = "事件发生地址")
		private Address address;
		@Schema(description = "事件描述")
		private String description;
		@Schema(description = "事件地点")
		private String location;
		@Schema(description = "ISO格式时间")
		private String time_iso;
		@Schema(description = "原始时间")
		private TimeRaw time_raw;
		@Schema(description = "UTC时间")
		private String time_utc;
		@Schema(description = "子状态编码")
		private String sub_status;
		@Schema(description = "子状态ID")
		private Integer sub_statusId;
		@Schema(description = "物流阶段")
		private String stage;
	}

	@Data
	public static class Status {
		@Schema(description = "物流状态")
		private String status;
		@Schema(description = "物流子状态")
		private String sub_status;
	}

	@Data
	public static class Milestone {
		@Schema(description = "关键阶段")
		private String key_stage;
		@Schema(description = "ISO格式时间")
		private String time_iso;
		@Schema(description = "原始时间")
		private TimeRaw time_raw;
		@Schema(description = "UTC时间")
		private String time_utc;
	}

	@Data
	public static class MiscInfo {
		@Schema(description = "本地索引键")
		private int local_key;
		@Schema(description = "风险系数")
		private int risk_factor;
	}

	@Data
	public static class ShippingInfo {
		@Schema(description = "收件人地址")
		private SimpleAddress recipient_address;
		@Schema(description = "发货人地址")
		private SimpleAddress shipper_address;
	}

	@Data
	public static class SimpleAddress {
		@Schema(description = "经纬度坐标")
		private Map<String, Object> coordinates;
		@Schema(description = "国家")
		private String country;
	}

	@Data
	public static class TimeMetrics {
		@Schema(description = "距上次更新天数")
		private int days_after_last_update;
		@Schema(description = "下单后天数")
		private int days_after_order;
		@Schema(description = "运输总天数")
		private int days_of_transit;
		@Schema(description = "已运输天数")
		private int days_of_transit_done;
		@Schema(description = "预计送达日期")
		private Map<String, Object> estimated_delivery_date;
	}

	@Data
	public static class Tracking {
		@Schema(description = "承运商列表")
		private List<Provider> providers;
	}

	@Data
	public static class Provider {
		@Schema(description = "物流事件列表")
		private List<Event> events;
	}

	@Data
	public static class Address {
		@Schema(description = "城市")
		private String city;
		@Schema(description = "经纬度坐标")
		private Map<String, Object> coordinates;
		@Schema(description = "邮政编码")
		private String postal_code;
	}

	@Data
	public static class TimeRaw {
		@Schema(description = "日期")
		private String date;
		@Schema(description = "时间")
		private String time;
	}

}
