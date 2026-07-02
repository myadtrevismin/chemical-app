package com.core.auth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.core.auth.service.UserLoginService;
import com.core.auth.trans.TokenMetaData;

@Component("applicationBackGroundRunner")
public class ApplicationBackGroundRunner {
	Lock userSessionsLock = new ReentrantLock();
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	UserLoginService userLoginService;
	/*
	 * @Autowired
	 * 
	 * @Qualifier("promoSMPPSession") SmppSession promoSMPPSession;
	 */
	boolean isCleanSessionsRunning = false;

	
	@Scheduled(cron = "0 0/15 * 1/1 * ?")
	public void cleanUserSessions() {
		if (isCleanSessionsRunning) {
			return;
		}

		if (!userSessionsLock.tryLock()) {
			return;
		}

		try {
			logger.info("cleaning invalid user sessions");

			isCleanSessionsRunning = true;

			Map<Long, Set<TokenMetaData>> map = AuthStatConstats.getTokenMetaData();

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MILLISECOND, -15 * 60 * 1000);
			Date dt = cal.getTime();
			List<TokenMetaData> deletedMetaData = new ArrayList<TokenMetaData>();
			map.forEach((k, v) -> {
				List<TokenMetaData> lis = v.stream().filter(s -> s.getLastAccessTime().compareTo(dt) < 0)
						.collect(Collectors.toList());
				if (lis.size() > 0) {
					if (v.size() == lis.size()) {
						Set<TokenMetaData> metaData = AuthStatConstats.removeUserToken(k);
						deletedMetaData.addAll(metaData);
						AuthStatConstats.removeUserSessionStore(k);
					} else {
						v.removeAll(lis);
						deletedMetaData.addAll(lis);
					}
				}

				if (v.size() == lis.size()) {
					Set<TokenMetaData> metaData = AuthStatConstats.removeUserToken(k);
					if (metaData != null) {
						deletedMetaData.addAll(metaData);
					}
					AuthStatConstats.removeUserSessionStore(k);
				}
			});

			userLoginService.updateUserLastSeen(deletedMetaData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isCleanSessionsRunning = false;
			userSessionsLock.unlock();
		}
	}
}
