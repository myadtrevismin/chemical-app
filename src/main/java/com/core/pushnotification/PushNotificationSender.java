package com.core.pushnotification;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.core.common.CommonUtils;

@Repository("pushNotificationSender")
public class PushNotificationSender {
	
	@Value("${app.company.fcm_server_key}")
	private String fcmServerKey;

	protected JSONObject constructPushData(NotificationEntity notification) {
		if (CommonUtils.isNotNullOrEmpty(notification.getPage())
				|| CommonUtils.isNotNullOrEmpty(notification.getImage())) {
			JSONObject data = new JSONObject();

			if (CommonUtils.isNotNullOrEmpty(notification.getImage())) {
				data.put("image", notification.getImage());
			}

			if (CommonUtils.isNotNullOrEmpty(notification.getPage())) {
				data.put("page", notification.getPage());
			}

			if (CommonUtils.isNotNullOrEmpty(notification.getSound())) {
				data.put("sound", notification.getSound());
			}

			return data;
		}

		return null;
	}

	protected String sendFcmNotification(String msg) throws Exception {
		StringEntity requestEntity = new StringEntity(msg, ContentType.APPLICATION_JSON);

		HttpPost postMethod = new HttpPost("https://fcm.googleapis.com/fcm/send");
		postMethod.setHeader("Content-Type", "application/json");
		postMethod.setHeader("Authorization", "key=" + fcmServerKey);
		postMethod.setEntity(requestEntity);

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse rawResponse = httpClient.execute(postMethod);
		return IOUtils.toString(rawResponse.getEntity().getContent(), StandardCharsets.UTF_8.name());
	}

}
