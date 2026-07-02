package com.core.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.core.auth.AuthStatConstats;
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

@Service
public class GSuiteService {

	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	@Autowired
	ResourceLoader resourceLoader;

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, AccessToken token, String from)
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

		return flow.createAndStoreCredential(response, from);
	}

	public void send(String from, MimeMessage message) throws Exception {
		NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		AccessToken token = AuthStatConstats.getAccessTokenForEmail(from);
		Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, token, from))
				.setApplicationName("MSC Chemicals").build();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		message.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		com.google.api.services.gmail.model.Message mesage = new com.google.api.services.gmail.model.Message();

		mesage.setRaw(encodedEmail);

		mesage = service.users().messages().send(from, mesage).execute();

		System.out.println("Email Sent with with id " + mesage.getId());

	}

}
