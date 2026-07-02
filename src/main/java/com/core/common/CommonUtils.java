package com.core.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.core.auth.trans.UserSessionStore;
import com.core.common.CommonData.PaginationParams;
import com.core.trans.CollectionFilter;
import com.core.user.entity.HUser;
import com.core.user.entity.User;
import com.core.user.entity.UserImage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CommonUtils {
	private static Pattern emailPattern;
	private static Pattern phonePattern;
	private static Pattern ytPattern;

	private static Matcher matcher;

	public static final HttpClientBuilder clientBulder = HttpClientBuilder.create();

	public static void setResponseStatus(int status) {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse().setStatus(status);
	}

	public static final String VALID_DATE = "dd-MM-yyyy";

	public static final String VALID_DATE_TIME = "dd-MM-yyyy HH:mm";

	public static final String MYSQL_DEFAULT_DATE = "yyyy-MM-dd HH:mm:ss.S";

	public static final String NO_AVATAR_URL = "resources/images/noavatar.jpg";

	public static final String VALID_PHONE_NUMBER = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

	protected static final String ANONYMOUS_USER = "anonymousUser";
	private static final String EMAIL_PATTERN = "^[A-Za-z][A-Za-z0-9!#$%&'*+-/=?^_`{|}~\\.]{7,63}@(?:gmail|yahoo|ymail|rediffmail|hotmail|)+\\.(?:com)$";
	private static final String YT_PATTERN = "^http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)$";
	static {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		phonePattern = Pattern.compile(VALID_PHONE_NUMBER);
		ytPattern = Pattern.compile(YT_PATTERN, Pattern.CASE_INSENSITIVE);
	}

	public static String getUserCreateException(User user, Exception ex) {
		String msg = "";

		Throwable cause = ex.getCause();

		while (cause != null) {
			if (cause.getMessage().contains("email_UNIQUE")) {
				msg = "Email already exists for " + user.getEmail();
				break;
			} else if (cause.getMessage().contains("username_UNIQUE")) {
				msg = "Username already exists for " + user.getEmail();
				break;
			} else if (cause.getMessage().contains("mobile_UNIQUE")) {
				msg = "Phone number already exists for " + user.getMobile();
				break;
			} else {
				cause = cause.getCause();
			}
		}

		if (msg.isEmpty()) {
			msg = "Unknown error.";
		}

		return msg;
	}

	public static final String JSON_VALUE = "{\"%s\": \"%s\"}";

	public static boolean validateEmail(final String hex) {
		matcher = emailPattern.matcher(hex);
		return matcher.matches();
	}

	public static String convertPhoneToRequiredFormat(String phone) {

		phone = phone.replaceAll(" ", "");
		phone = phone.replaceAll("-", "");
		if (phone.length() == 10) {
			return "+91" + phone;
		}
		if (phone.length() == 11 && phone.charAt(0) == '0') {// starts with zero
			return "+91" + phone.substring(1);
		}
		if (phone.length() == 12 && phone.startsWith("+91")) {// starts with zero
			return "+" + phone.substring(1);
		}
		if (phone.length() == 14 && phone.startsWith("0091")) {// starts with zero
			return "+" + phone.substring(2);
		}

		if (phone.length() == 13 && phone.startsWith("+91")) {
			return phone;
		}

		return phone;
	}

	public static boolean validPhone(final String phone) {
		matcher = phonePattern.matcher(phone);
		return matcher.matches();
	}

	public static boolean isAnonymous() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth == null || ANONYMOUS_USER.equals(auth.getPrincipal());
	}

	public static String convertTitleToStringURL(String title) {
		return title.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll(" +", " ").toLowerCase().trim().replace(" ", "-");
	}

	public static UserSessionStore getUserSessionStore() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserSessionStore user = null;
		if (auth != null && !ANONYMOUS_USER.equals(auth.getPrincipal())) {
			user = (UserSessionStore) auth.getPrincipal();
		}
		return user;
	}

	public static User getUserFromSession() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		if (auth != null && !ANONYMOUS_USER.equals(auth.getPrincipal())) {
			UserSessionStore uss = (UserSessionStore) auth.getPrincipal();

			user = new User(uss.getId(), uss.getName(), uss.getEmail());
		}
		return user;
	}
	
	public static HUser getHUserFromSession() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HUser user = null;
		if (auth != null && !ANONYMOUS_USER.equals(auth.getPrincipal())) {
			UserSessionStore uss = (UserSessionStore) auth.getPrincipal();

			user = new HUser(uss.getId(), uss.getName(), uss.getEmail());
		}
		return user;
	}

	public static long parseYTTimeToSeconds(String hourFormat) {
		long seconds = 0;
		String[] split = hourFormat.split(":");

		try {
			seconds += Long.parseLong(split[0]) * 60;
			seconds += Long.parseLong(split[1]);
			return seconds;

		} catch (Exception e) {
			return -1;
		}
	}

	public static long parseClockToSeconds(String hourFormat) {
		long seconds = 0;
		String[] split = hourFormat.split(":");

		try {
			seconds += Long.parseLong(split[0]) * 60 * 60;
			seconds += Long.parseLong(split[1]) * 60;
			seconds += Long.parseLong(split[2]);

			return seconds;

		} catch (Exception e) {
			return -1;
		}
	}

	public static int getRandomNumber(int maxNo) {
		return RandomUtils.nextInt(0, maxNo) + 1;
	}

	public static String getRandomString(int maxNo) {
		return RandomStringUtils.randomAlphabetic(maxNo).toLowerCase();
	}

	public static String getRandomAlphaNumericString(int maxNo) {
		return RandomStringUtils.randomAlphanumeric(maxNo).toLowerCase();
	}

	public static String getRandomString() {
		return getRandomString(16);
	}

	public static String getRandomNumericString(int maxNo) {// Dont chnage this
		return RandomStringUtils.randomNumeric(maxNo, maxNo);
	}

	public static String getRandomNumericString() {// Dont chnage this
		return getRandomAlphaNumericString(32);
	}

	public static int getRandomInt(int max) {
		return (int) Math.floor(Math.random() * Math.floor(max));
	}

	public static boolean isNotNullOrEmpty(Object obj) {

		if (obj != null) {
			if (obj instanceof String && ((String) obj).trim().isEmpty()) {
				return false;
			} else if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
				return false;
			} else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
				return false;
			} else if ((obj instanceof Long && ((Long) obj).equals(Long.valueOf(0)))
					|| (obj instanceof Double && ((Double) obj).equals(Double.valueOf(0)))) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean convertStringToBoolean(String flag) {
		if (flag.equals("Y") || flag.equalsIgnoreCase(Boolean.TRUE.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getBooleanFromString(String object) {
		boolean val = false;

		if (object != null && object.equalsIgnoreCase(Boolean.TRUE.toString())) {
			val = true;
		}

		return val;
	}

	public static List<Long> getListLong(String ids) {
		List<Long> qbIds = new ArrayList<Long>();

		if (isNotNullOrEmpty(ids)) {
			String[] lis = ids.split(",");

			for (String str : lis) {
				qbIds.add(Long.parseLong(str));
			}
		}

		return qbIds;
	}

	public static Date getISTEndInUtc() {
		Instant instant = Instant.now();
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);

		ZonedDateTime zdtStart = zdt.toLocalDate().atStartOfDay(zoneId);
		zdtStart.withZoneSameInstant(ZoneId.of("UTC")).toInstant();

		return Date.from(zdtStart.withZoneSameInstant(ZoneId.of("UTC")).toInstant());
	}

	public static long differenceIndays(Date d1, Date d2) {
		LocalDateTime ld1 = LocalDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault());
		LocalDateTime ld2 = LocalDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault());

		return ChronoUnit.DAYS.between(ld1, ld2);
	}

	public static Date getISTStartInUtc() {
		Instant instant = Instant.now();
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);

		ZonedDateTime zdtStart = zdt.toLocalDate().atStartOfDay(zoneId);
		zdtStart.withZoneSameInstant(ZoneId.of("UTC")).toInstant();

		return Date.from(zdtStart.withZoneSameInstant(ZoneId.of("UTC")).toInstant());
	}

	public static Long getUserIdFromSession() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !ANONYMOUS_USER.equals(auth.getPrincipal())) {
			UserSessionStore uss = (UserSessionStore) auth.getPrincipal();
			return uss.getId();
		}

		return null;
	}

	public static String getGooglePlusProfileImage(String googleId, String googlePlusApiKey) {
		String url = "https://www.googleapis.com/plus/v1/people/" + googleId + "?fields=image&key=" + googlePlusApiKey;

		try {
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", CommonData.WebSiteUrl);
			conn.connect();

			if (conn.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				JSONObject json = new JSONObject(in.toString());

				return json.getJSONObject("image").getString("url") + "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getUserImage(String fbId, String googleId, UserImage image, String googlePlusApiKey) {
		String imgurl = null;

		if (image != null && image.getImage() != null) {
			imgurl = "msc-api/user-images/" + image.getUid() + ".jpg?v=" + image.getImageVersion();
		} else if (fbId != null) {
			imgurl = "https://graph.facebook.com/" + fbId + "/picture?type=large";
		} else if (googleId != null) {
			imgurl = getGooglePlusProfileImage(googleId, googlePlusApiKey);
		}

		return imgurl;
	}

	public static String getUserImage(User user, UserImage image, String googlePlusApiKey) {
		return getUserImage(user.getFacebookid(), user.getGoogleid(), image, googlePlusApiKey);
	}

	public static String getUserImage(UserSessionStore user, UserImage image, String googlePlusApiKey) {
		return getUserImage(user.getFacebookid(), user.getGoogleid(), image, googlePlusApiKey);
	}

	public static int getPageOffset(HttpServletRequest request) {
		int pageNo = 1;
		String limit = request.getParameter(PaginationParams.offset.toString());
		if (limit != null) {
			try {
				pageNo = Integer.parseInt(limit);
			} catch (Exception e) {
				pageNo = 1;
			}
		}
		return pageNo;
	}

	public static int getPageLimit(HttpServletRequest request) {
		int maxResults = CommonData.DEFAULT_LIMIT;
		String offset = request.getParameter(PaginationParams.limit.toString());
		if (offset != null) {
			try {
				maxResults = Integer.parseInt(offset);
			} catch (Exception e) {
				maxResults = CommonData.DEFAULT_LIMIT;
			}
		}
		return maxResults;
	}

	public static void sendJsonResponse(HttpServletResponse response, String key, Object obj, boolean cacheFlag) {
		response.setContentType("application/json;charset=UTF-8");
		if (cacheFlag) {
			response.setHeader("Cache-Control", "public,max-age=348000"); // HTTP 1.1.
			response.setHeader("Pragma", "public,max-age=348000"); // HTTP 1.0.
		} else {
			response.setHeader("Cache-Control", "no-cache");
		}

		try {
			if (obj instanceof String) {
				response.getWriter().write(String.format(JSON_VALUE, key, obj));
			} else if (obj instanceof SystemException) {
				response.getWriter().write(String.format(JSON_VALUE, key, ((SystemException) obj).getMessage()));
			}

			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendImageResponse(HttpServletResponse response, InputStream in) {
		try {
			response.setContentType("image/jpeg");
			response.setHeader("Cache-Control", "public,max-age=31536000"); // HTTP 1.1.
			response.setHeader("Pragma", "public,max-age=31536000"); // HTTP 1.0.
			IOUtils.copy(in, response.getOutputStream());
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	public static CollectionFilter getCollectionFilter(HttpServletRequest request) {
		CollectionFilter filter = new CollectionFilter();
		filter.setOffset(getPageOffset(request));
		filter.setLimit(getPageLimit(request));

		return filter;
	}

	public static Timestamp getCurrentTimeStamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());
	}

	public static String htmlToText(String html) {
		if (isNotNullOrEmpty(html)) {
			return Jsoup.parse(html).text();
		} else {
			return "";
		}
	}

	public static String getDescriptionFromPost(String content) {
		String text = CommonUtils.htmlToText(content);

		if (text.length() > 200) {
			text = text.substring(0, 200);
		}

		return text;
	}

	public static String updateYoutubeEmbed(String videoUrl) {
		System.out.println(videoUrl);
		if (isNotNullOrEmpty(videoUrl)) {
			Matcher matcher = ytPattern.matcher(videoUrl);
			if (matcher.matches()) {
				return "https://www.youtube.com/embed/" + matcher.group(1).trim();
			}
		}

		return null;
	}

	public static boolean validateRecaptcha(String gresponse, String secret, String ipaddr) {
		try {
			URL url = new URL(
					"https://www.google.com/recaptcha/api/siteverify?secret=" + secret + "&response=" + gresponse);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			JSONObject json = new JSONObject(IOUtils.toString(in));

			return json.getBoolean("success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static class NumericBooleanSerializer extends JsonSerializer<Boolean> {

		@Override
		public void serialize(Boolean bool, JsonGenerator generator, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			generator.writeString(bool ? "1" : "0");
		}
	}

	public static class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {

		@Override
		public Boolean deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			return !"0".equals(parser.getText());
		}
	}

	public static String getUserName(User user) {
		if(user==null) {
			return "";
		}
		if (CommonUtils.isNotNullOrEmpty(user.getName())) {
			return user.getName();
		} else {
			return user.getEmail();
		}
	}

	public static String getUserName(UserSessionStore user) {
		if (CommonUtils.isNotNullOrEmpty(user.getName())) {
			return user.getName();
		} else {
			return user.getEmail();
		}
	}

//	public static void main(String[] args) {
//		System.out.println(getRandomNumericString(4));
//	}
}
