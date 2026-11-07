package com.worldpay.api.response;

import java.util.List;


import lombok.Data;

@Data
public class TrackingRegisterEntity {
	
	private int code;
    private DataField data;

    @Data
    public static class DataField {
        private List<AcceptedItem> accepted;
        private List<RejectedItem> rejected;
    }

    @Data
    public static class AcceptedItem {
        private int carrier;
        private String number;
        private int origin;
    }

    @Data
    public static class RejectedItem {
        private String number;
        private ErrorDetail error;
    }

    @Data
    public static class ErrorDetail {
        private int code;
        private String message;
    }

}
