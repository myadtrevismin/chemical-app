package com.core.sms;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.core.auth.trans.UserSessionStore;
import com.core.common.Ack;
import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.contact.Contact;
import com.core.contact.ContactGroup;
import com.core.contact.ContactGroupRepo;
import com.core.contact.ContactRepo;

@Service
public class MessageService {

	@Autowired
	ContactGroupRepo groupRepo;

	@Autowired
	ContactRepo contactRepo;

	@Value("${app.company.environment}")
	private String environment;

	@Value("${app.company.sms.server}")
	private String server;

	@Value("${app.company.sms.username}")
	private String username;

	@Value("${app.company.sms.password}")
	private String password;

	private boolean isValidPhoneNumber(String phoneNumber) {
		String numberStr = phoneNumber;
		if (phoneNumber.startsWith("+91")) {
			if (phoneNumber.length() != 13) {
				return false;
			}
			numberStr = phoneNumber.substring(1);
		} else {
			if (phoneNumber.length() != 10) {
				return false;
			}
		}
		Pattern p = Pattern.compile("[0-9]{1,13}");// . represents single character
		Matcher m = p.matcher(numberStr);
		return m.matches();
	}

	public MessageStatus submitRequest(Message request) {
		MessageStatus messageStatus = new MessageStatus();
		messageStatus.setStatus(true);
		if (request.getGroup() != null) {
			List<String> to = new ArrayList<String>();

			Optional<ContactGroup> opt = groupRepo.findById(request.getGroup());
			if (opt.isPresent()) {
				List<Contact> contacts = contactRepo.findByGroup(request.getGroup());

				if (CommonUtils.isNotNullOrEmpty(contacts)) {
					contacts.forEach(c -> {
						to.add(c.getMobile());
					});
				}
			}

			request.setTo(to);
		}

		Set<String> numSet = new HashSet<>();

		for (String number : request.getTo()) {

			number = number.trim();

			if (!number.startsWith("+91")) {
				number = "+91" + number;
			}

			numSet.add(number);

			boolean validPhNo = isValidPhoneNumber(number);
			if (validPhNo) {

				boolean success = sendSMS(request, number);
				if (!success) {
					messageStatus.setStatus(false);
				}
			}

		}

		return messageStatus;

	}

	public Ack previewRequest(Message msg) throws Exception {
		UserSessionStore user = CommonUtils.getUserSessionStore();
		Ack ack = new Ack();

//		MessageStatus messageStatus = new MessageStatus();
		if (msg.getGroup() != null) {
			List<String> to = new ArrayList<String>();

			Optional<ContactGroup> opt = groupRepo.findById(msg.getGroup());
			if (opt.isPresent()) {
				List<Contact> contacts = contactRepo.findByGroup(msg.getGroup());

				if (CommonUtils.isNotNullOrEmpty(contacts)) {
					contacts.forEach(c -> {
						to.add(c.getMobile());
					});
				}
			}

			msg.setTo(to);
		}

		if (CommonUtils.isNotNullOrEmpty(msg.getTo())) {
			int credits = 0;

			// msg.setReports(new ArrayList<SMSReport>());
			msg.setUid(user.getId());
			msg.setCreationDate(new Date());

			if (msg.getMessages() == null) {
				double devisor = 160.0;

				Matcher m = CommonData.nonAscii.matcher(msg.getMessage());
				if (m.find()) {
					devisor = 70.0;
				}

				credits = (int) (msg.getTo().size() * Math.ceil(msg.getMessage().length() / devisor));
			} else {
				credits = msg.getCredits();
			}

			int i = 0;
			Set<String> numSet = new HashSet<>();
			List<SMSRequest> lis = new ArrayList<>();
			for (String number : msg.getTo()) {

				String message = msg.getMessage();

				number = number.trim();

				if (!number.startsWith("+91")) {
					number = "+91" + number;
				}
				if (!msg.isDuplicatedAllowed() && numSet.contains(number)) {
					continue;
				}
				numSet.add(number);

				boolean validPhNo = isValidPhoneNumber(number);

				if (validPhNo) {
					SMSRequest smsRequest = new SMSRequest();

					if (msg.getMessages() != null) {
						message = msg.getMessages().get(i);
					}
					double devisor = 160.0;

					Matcher m = CommonData.nonAscii.matcher(message);
					if (m.find()) {
						devisor = 70.0;
					}
					smsRequest.setCredits((int) Math.ceil((double) message.length() / devisor));
					smsRequest.setMessage(message);
					smsRequest.setSender(msg.getSender());
					smsRequest.setType(msg.getType());
					smsRequest.setNumbers(Arrays.asList(number));
					smsRequest.setUnicode(msg.isUnicode());

					smsRequest.setFalsh(msg.isFlash());
					if (msg.getScheduledTime() != null) {
						DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
						formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to
																						// be
						smsRequest.setScheduledDate(formatter.format(msg.getScheduledTime()));
					}
					lis.add(smsRequest);
				}
				i++;
			}
			ack.setList(lis);
			ack.setCredits(credits);
			msg.setCredits(credits);

		}

		ack.setStatus(200);
		return ack;
	}

	public boolean sendSMS(Message request, String phoneNumber) {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			String numbers = phoneNumber.replace("+91", "");

			String unicode = "";
			if (request.isUnicode()) {
				unicode = "&msgtype=unicode";
			}

			String sendOnDate = "";
			if (request.getScheduledTime() != null) {
				DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
				formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to
																				// be

				sendOnDate = "&sendondate=" + formatter.format(request.getScheduledTime());
			}
			HttpGet get = new HttpGet("http://sms.pencaptech.co.in/api.php?username=" + username + "&password="
					+ password + "&from=" + request.getSender() + "&to=" + numbers + "" + "&message="
					+ URLEncoder.encode(request.getMessage(), "UTF-8") + unicode + sendOnDate);
			HttpResponse response = client.execute(get);
			String responseJSON = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			if (responseJSON.trim().length() > 0) {

				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private void sendSMS(String str, String to) {
		if (environment.equals("prod")) {
			Message message = new Message();

			message.setSender("SFTPRP");
			message.setMessage(str);

			sendSMS(message, to);
		}
	}

	@Async
	public void sendOTPSMS(String mobile, String passwordResetToken) {
		sendSMS("Dear Customer, " + passwordResetToken
				+ " is the OTP to login SFTProperties. Do not share it with anyone.", mobile);
	}

	@Async
	public void sendSignUp(String mobile, String username, String password) {
		sendSMS("Welcome to SFT Properties. Thank you for choosing SFT services. Your login ID: " + username + " PWD: "
				+ password, mobile);
	}

	@Async
	public void vendorSignup(String mobile) {
		sendSMS("Welcome to SFT Properties. Thank you for registering with us. Our agent will connect you shortly for your account activation.",
				mobile);
	}

	@Async
	public void vendorActivation(String mobile) {
		sendSMS("Welcome to SFT Properties. Your account is activated as vendor.", mobile);
	}

	@Async
	public void propertyBookmarkAlert(String name, String propname, String userPhone, String email, String to) {
		if (propname != null) {
			sendSMS(name + " interested in " + propname + " " + " Contact Number: " + userPhone + " Email ID: " + email,
					to);
		} else {
			sendSMS(name + " enqured about property " + " Contact Number: " + userPhone + " Email ID: " + email, to);
		}
	}

	@Async
	public void vendorRegToAdmin(String vendorName, String mobile) {
		sendSMS(vendorName
				+ " has registered on website to post a new property on portal. Verify and activate the account. ",
				mobile);
	}

	@Async
	public void sendSiteVisitAlert(String username, String propName, String mobile) {
		sendSMS("Dear " + username + ", your site visit to property " + propName + " is scheduled on (date). Thank you",
				mobile);
	}

	@Async
	public void propertyBoughtOption(String mobile, String username, String propName) {
		sendSMS(username + " has purchased " + propName + " on SFT Properties Portal. Convey your best wishes.",
				mobile);
	}

}
