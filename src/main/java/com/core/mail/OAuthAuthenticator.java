package com.core.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;

import com.core.common.CommonData;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPTransport;

public class OAuthAuthenticator {

	@Value("${app.company.images}")
	String imagePath;

	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2", "com.core.mail.OAuth2SaslClientFactory");
		}
	}

	/**
	 * Installs the OAuth2 SASL provider. This must be called exactly once before
	 * calling other methods on this class.
	 */
	public static void initialize() {
		Security.addProvider(new OAuth2Provider());
	}

	/**
	 * Connects and authenticates to an IMAP server with OAuth2. You must have
	 * called {@code initialize}.
	 *
	 * @param host       Hostname of the imap server, for example {@code
	 *     imap.googlemail.com}.
	 * @param port       Port of the imap server, for example 993.
	 * @param userEmail  Email address of the user to authenticate, for example
	 *                   {@code oauth@gmail.com}.
	 * @param oauthToken The user's OAuth token.
	 * @param debug      Whether to enable debug logging on the IMAP connection.
	 *
	 * @return An authenticated IMAPStore that can be used for IMAP operations.
	 */
	public static IMAPStore connectToImap(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws Exception {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put("mail.imaps.sasl.mechanisms.oauth2.oauthToken", oauthToken);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		final String emptyPassword = "";
		store.connect(host, port, userEmail, emptyPassword);
		return store;
	}

	/**
	 * Connects and authenticates to an SMTP server with OAuth2. You must have
	 * called {@code initialize}.
	 *
	 * @param host       Hostname of the smtp server, for example {@code
	 *     smtp.googlemail.com}.
	 * @param port       Port of the smtp server, for example 587.
	 * @param userEmail  Email address of the user to authenticate, for example
	 *                   {@code oauth@gmail.com}.
	 * @param oauthToken The user's OAuth token.
	 * @param debug      Whether to enable debug logging on the connection.
	 *
	 * @return An authenticated SMTPTransport that can be used for SMTP operations.
	 */
	public static SMTPTransport connectToSmtp(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put("mail.imaps.sasl.mechanisms.oauth2.oauthToken", oauthToken);
		// props.put("mail.smtp.auth","true");
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
		// If the password is non-null, SMTP tries to do AUTH LOGIN.

		transport.connect(host, port, userEmail, null);

		MimeMessage message = new MimeMessage(session);
		DataHandler handler = new DataHandler(new ByteArrayDataSource("Testing".getBytes(), "text/plain"));
		message.setSender(new InternetAddress(userEmail));
		message.setSubject("Subject");
		message.setDataHandler(handler);

		message.setRecipient(Message.RecipientType.TO, new InternetAddress("abhinayelite@gmail.com"));
		transport.sendMessage(message, message.getAllRecipients());

		return transport;
	}

	/**
	 * oauth2 --generate_oauth2_string --user=abhinayelite@gmail.com
	 * --access_token=AIzaSyDkThPrvHj8YN2rsDT7xuDm305q93z6GVU
	 * 
	 * 
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, AccessToken token)
			throws IOException { // Load client
		// secrets.
		// in =
		InputStream in = OAuthAuthenticator.class.getClass().getResourceAsStream("/msc-chemicals-res.json");
		if (in == null) {
			throw new FileNotFoundException("Resource not found: ");
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request. Collection<String> c =
		Collection<String> c = new ArrayList<>();
		c.add("https://www.googleapis.com/auth/gmail.send");
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, c).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CommonData.imagePath)))
						.setAccessType("offline").build();

		TokenResponse response = new TokenResponse();
		response.setAccessToken(token.getTokenValue());

		return flow.createAndStoreCredential(response, "info@mscgroup.co.in");
	}

	/*
	 * public static void main(String args[]) throws Exception {
	 * 
	 * Collection<String> c = new ArrayList<>();
	 * c.add("https://www.googleapis.com/auth/gmail.send"); GoogleCredentials
	 * credentials = GoogleCredentials
	 * .fromStream(OAuthAuthenticator.class.getClass().getResourceAsStream(
	 * "/msc-chemicals-5afe6ad7e2e9.json"))
	 * .createScoped(c).createDelegated("info@mscgroup.co.in");
	 * 
	 * credentials.refreshIfExpired(); AccessToken token =
	 * credentials.getAccessToken(); credentials.refreshAccessToken();
	 * 
	 * System.out.println(token.getExpirationTime()); final NetHttpTransport
	 * HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	 * 
	 * Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY,
	 * getCredentials(HTTP_TRANSPORT, token))
	 * .setApplicationName("MSC Chemicals").build(); Properties props = new
	 * Properties(); Session session = Session.getDefaultInstance(props, null);
	 * 
	 * MimeMessage email = new MimeMessage(session);
	 * 
	 * email.setFrom(new InternetAddress("info@mscgroup.co.in"));
	 * email.addRecipient(javax.mail.Message.RecipientType.TO, new
	 * InternetAddress("abhinayelite@gmail.com"));
	 * email.setSubject("hello from code as"); email.setText("message 323");
	 * 
	 * ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	 * email.writeTo(buffer); byte[] bytes = buffer.toByteArray(); String
	 * encodedEmail = Base64.encodeBase64URLSafeString(bytes);
	 * com.google.api.services.gmail.model.Message mesage = new
	 * com.google.api.services.gmail.model.Message();
	 * 
	 * mesage.setRaw(encodedEmail);
	 * 
	 * mesage = service.users().messages().send("info@mscgroup.co.in",
	 * mesage).execute(); System.out.println("End " + mesage.getId());
	 * 
	 * 
	 * 
	 * String email = "info@mscgroup.co.in"; String oauthToken =
	 * token.getTokenValue();
	 * 
	 * initialize();
	 * 
	 * 
	 * IMAPStore imapStore = connectToImap("imap.gmail.com", 993, email, oauthToken,
	 * true); System.out.println("Successfully authenticated to IMAP.\n");
	 * 
	 * SMTPTransport smtpTransport = connectToSmtp("smtp.gmail.com", 587, email,
	 * oauthToken, true); System.out.println("Successfully authenticated to SMTP.");
	 * 
	 * 
	 * }
	 */
}
