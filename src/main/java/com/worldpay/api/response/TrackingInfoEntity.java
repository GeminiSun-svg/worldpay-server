package com.worldpay.api.response;

import java.util.List;
import java.util.Map;


import lombok.Data;

@Data
public class TrackingInfoEntity {
	
	private int code;
	private DataInner data;

	@Data
	public static class DataInner {
		private List<AcceptedItem> accepted;
	}

	@Data
	public static class AcceptedItem {
		private int carrier;
		private String number;
		private String tag;
		private TrackInfo track_info;
	}

	@Data
	public static class TrackInfo {
		private Event latest_event;
		private Status latest_status;
		private List<Milestone> milestone;
		private MiscInfo misc_info;
		private ShippingInfo shipping_info;
		private TimeMetrics time_metrics;
		private Tracking tracking;
	}

	@Data
	public static class Event {
		private Address address;
		private String description;
		private String location;
		private String time_iso;
		private TimeRaw time_raw;
		private String time_utc;
		private String sub_status;
		private Integer sub_statusId;
		private String stage;
	}

	@Data
	public static class Status {
		private String status;
		private String sub_status;
	}

	@Data
	public static class Milestone {
		private String key_stage;
		private String time_iso;
		private TimeRaw time_raw;
		private String time_utc;
	}

	@Data
	public static class MiscInfo {
		private int local_key;
		private int risk_factor;
	}

	@Data
	public static class ShippingInfo {
		private SimpleAddress recipient_address;
		private SimpleAddress shipper_address;
	}

	@Data
	public static class SimpleAddress {
		private Map<String, Object> coordinates;
		private String country;
	}

	@Data
	public static class TimeMetrics {
		private int days_after_last_update;
		private int days_after_order;
		private int days_of_transit;
		private int days_of_transit_done;
		private Map<String, Object> estimated_delivery_date;
	}

	@Data
	public static class Tracking {
		private List<Provider> providers;
	}

	@Data
	public static class Provider {
		private List<Event> events;
	}

	@Data
	public static class Address {
		private String city;
		private Map<String, Object> coordinates;
		private String postal_code;
	}

	@Data
	public static class TimeRaw {
		private String date;
		private String time;
	}

}
