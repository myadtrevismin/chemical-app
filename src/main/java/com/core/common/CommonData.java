package com.core.common;

import java.util.regex.Pattern;

public class CommonData {
	public static final int DEFAULT_LIMIT = 10;
	public static final String NON_ASCII_CHECK = "[^\\x00-\\xFF]+";
	public static final String WebSiteUrl = "http://mscgroup.co.in/admin/";
	public static final String AdminUrl = "http://167.86.69.182/msc/";
	
	public static String imagePath = "";

	public enum SMSStatus {
		Accepted, Queued, Sent, Delivered, Undelivered, Failed, InValidNumber
	}

	public static final Pattern nonAscii = Pattern.compile(NON_ASCII_CHECK);

	public enum JWTAttrs {
		SECRET("AskPanditKundali"), ISSUER("Askpandi"), CLAIM("USERD");

		private String value;

		private JWTAttrs(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	public enum SocialType {
		facebook, google
	}

	public enum Status {
		NEW, ASSIGNED, REPLIED, RATED
	}

	public enum PaginationParams {
		limit, offset
	}
	public enum TypeRepoEnum{
		sales("sales"),
		companies("companies"),
		purchases("purchases"),
		leads("leads"),
		orders("orders");
		private String repo;
		TypeRepoEnum(String str){
			this.repo=str;
		}
	}
}
