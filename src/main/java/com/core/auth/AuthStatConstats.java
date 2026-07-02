package com.core.auth;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionStore;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

public class AuthStatConstats {
	private static Map<Long, UserSessionStore> studentMap = new ConcurrentHashMap<Long, UserSessionStore>();
	private static Map<Long, Set<TokenMetaData>> tokenData = new ConcurrentHashMap<Long, Set<TokenMetaData>>();
	private static Map<String,AccessToken> gsuiteAccessToken=new ConcurrentHashMap<>();
	
	
	
	public static boolean isTokenExists(long user, String token) {
		Set<TokenMetaData> lis = tokenData.get(user);
		for (TokenMetaData tmd : lis) {
			if (tmd.getToken().equals(token)) {
				return true;
			}
		}

		return false;
	}
	public static AccessToken getAccessTokenForEmail(String email)throws Exception {
		
		AccessToken token=gsuiteAccessToken.get(email);
		if(token==null || token.getExpirationTime().before(new Date())) {
			Collection<String> c = new ArrayList<>();
			 Resource resource = new ClassPathResource("msc-chemicals-5afe6ad7e2e9.json");
		        InputStream inputStream = resource.getInputStream();
			c.add("https://www.googleapis.com/auth/gmail.send");
			GoogleCredentials credentials = GoogleCredentials
					.fromStream(inputStream)
					.createScoped(c).createDelegated(email);
			
			credentials.refreshIfExpired();
			 token = credentials.getAccessToken();
			 gsuiteAccessToken.put(email, token);

		}

		return token;
		
	}
	public static Set<TokenMetaData> removeUserToken(long user) {
		return tokenData.remove(user);
	}

	public static Map<Long, UserSessionStore> getStudentMap() {
		return studentMap;
	}

	public static Map<Long, Set<TokenMetaData>> getTokenMetaData() {
		return tokenData;
	}

	public static Set<TokenMetaData> getTokensForUser(long userId) {
		return tokenData.get(userId);
	}

	public static void addTokenForUser(long user, TokenMetaData tokenMetadata) {
		Set<TokenMetaData> lis = getTokensForUser(user);

		if (lis == null) {
			lis = new CopyOnWriteArraySet<TokenMetaData>();
			tokenData.put(user, lis);
		}

		lis.add(tokenMetadata);
	}

	public static UserSessionStore getUserSessionStore(long userId) {
		return studentMap.get(userId);
	}

	public static void setUserSessionStrore(long userId, UserSessionStore uss) {
		studentMap.put(userId, uss);
	}

	public static void updateTime(long user, String token) {
		Set<TokenMetaData> lis = tokenData.get(user);
		lis.stream().forEach(s -> {
			if (s.getToken().equals(token)) {
				s.setLastAccessTime(new Date());

			}
		});
	}

	public static Optional<TokenMetaData> findTokenMetadata(long user, String token) {
		Set<TokenMetaData> lis = tokenData.get(user);
		return lis.stream().filter(s -> s.getToken().equals(token)).findFirst();
	}
	public static void removeUserSessionStore(long userId) {
		studentMap.remove(userId);
	}
	public static Set<TokenMetaData> removeToken(long user, String token) {
		Set<TokenMetaData> lis = tokenData.get(user);
		lis.removeIf(s -> !s.getToken().equals(token));

		return lis;
	}
}
