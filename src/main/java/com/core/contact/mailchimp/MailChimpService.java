package com.core.contact.mailchimp;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MailChimpService {
	
	@Value("${app.company.mailchimp.username}")
	private String username;
	
	@Value("${app.company.mailchimp.password}")
	private String password;
	
	
	public String updateContact(MailChimpMember member,String id) {

		ObjectMapper om = new ObjectMapper();

		CloseableHttpClient client = HttpClientBuilder.create().build();
		try {
			String json = om.writeValueAsString(member);

			HttpPatch httpPost = new HttpPatch("https://us5.api.mailchimp.com/3.0/lists/07b398141e/members/id");

			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Authorization","Basic c2Z0cHJvcGVydGllczplNzgyMDdhN2RjMmM4NjI2MWZjMTMxMDhhMmNjODI0My11czU=");
			CloseableHttpResponse response = client.execute(httpPost);
			String jsonResponse = IOUtils.toString(response.getEntity().getContent());
			System.out.println(jsonResponse);
			response.close();
			return om.readTree(jsonResponse).get("id").asText();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

	}

	public String createContact(MailChimpMember member) {
		ObjectMapper om = new ObjectMapper();
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		try {
			String json = om.writeValueAsString(member);

			HttpPost httpPost = new HttpPost("https://us5.api.mailchimp.com/3.0/lists/07b398141e/members");

			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Authorization","Basic c2Z0cHJvcGVydGllczplNzgyMDdhN2RjMmM4NjI2MWZjMTMxMDhhMmNjODI0My11czU=");
			CloseableHttpResponse response = client.execute(httpPost);
			String jsonResponse = IOUtils.toString(response.getEntity().getContent());
			System.out.println(jsonResponse);
			response.close();
			return om.readTree(jsonResponse).get("id").asText();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
}
