package com.core.auth.dao;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.core.auth.AuthStatConstats;
import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionLog;
import com.core.auth.trans.UserSessionStore;
import com.core.auth.trans.UserToken;
import com.core.common.CommonData.JWTAttrs;

@Repository
public class AuthDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	@PersistenceContext
	private EntityManager em;

	@Autowired
	UserDao userDao;

	private Algorithm macAuth = null;

	@PostConstruct
	public void init() {
		try {
			macAuth = Algorithm.HMAC256(JWTAttrs.SECRET.toString());
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public UserToken getValidToken(String token, long studentId) {
		try {
			TypedQuery<UserToken> qry = em.createQuery(
					"select t from UserToken t where t.uid=:uid and t.token=:token and t.active = true",
					UserToken.class);
			qry.setParameter("uid", studentId);
			qry.setParameter("token", token);
			List<UserToken> lis = qry.getResultList();
			if (lis.size() > 0) {
				UserToken u = lis.get(0);
				Date d1 = u.getValidTill();
				Date d2 = new Date();
				if (d1.compareTo(d2) > 0) {
					return u;
				} else {
					u.setActive(false);
					em.merge(u);
				}
			}
		} catch (Exception e) {
		} finally {

		}

		return null;
	}

	@Transactional
	public void storeUserToken(long uid, String token, long expiry, String browserName) {
		UserToken us = new UserToken();

		us.setUid(uid);
		us.setActive(true);
		us.setToken(token);
		us.setValidTill(new Date(expiry));
		us.setUserAgent(browserName);

		em.persist(us);
	}

	public TokenMetaData constructTokenMetadata(UserSessionStore uss, UserToken userToken) {
		TokenMetaData metaData = new TokenMetaData();

		metaData.setLastAccessTime(new Date());
		metaData.setAuthorities(uss.getAuthorities());

		if (userToken != null) {
			metaData.setValidTill(userToken.getValidTill());
			metaData.setToken(userToken.getToken());

			AuthStatConstats.addTokenForUser(userToken.getUid(), metaData);
		} else {
			metaData.setValidTill(new Date(uss.getTokenExpiry()));
			metaData.setToken(uss.getToken());

			AuthStatConstats.addTokenForUser(uss.getId(), metaData);
		}

		userDao.loadUserSessionStore(uss);

		return metaData;
	}

	@Transactional
	public long addStudentSessionLog(long uid, String token) {
		UserSessionLog sessionLog = new UserSessionLog();
		sessionLog.setStartTime(new Date());
		sessionLog.setUid(uid);
		sessionLog.setToken(token);

		em.persist(sessionLog);
		em.flush();

		return sessionLog.getId();
	}

	private void buildToken(UserSessionStore uss, Date expiry)
			throws IllegalArgumentException, UnsupportedEncodingException {
		String token = JWT.create().withSubject(uss.getUsername()).withClaim(JWTAttrs.CLAIM.toString(), uss.getId())
				.withExpiresAt(expiry).withIssuedAt(new Date()).withIssuer(JWTAttrs.ISSUER.toString()).sign(macAuth);
		
		uss.setToken(token);
		uss.setTokenExpiry(expiry.getTime());
	}

	@Transactional
	public void buildToken(UserSessionStore uss)
			throws IllegalArgumentException, UnsupportedEncodingException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		buildToken(uss, cal.getTime());
	}

}
