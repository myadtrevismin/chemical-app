package com.core.notification;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.notification.repo.NotificationRepo;

@Service
public class NotificationService {
	@Autowired
	NotificationRepo notificationRepo;

	@Autowired
	EntityManager em;

	@Transactional
	public Notification saveNotification(Notification notification) {
		return notificationRepo.save(notification);
	}

	public long  getCount(long uid) {
		List lis=em.createQuery("select count(t.id) from Notification t where uid=:uid and status=false ").setParameter("uid", uid).getResultList();
		if(lis.size()>0) {
			return (Long)lis.get(0);
		}
		return 0;
	}
@Transactional
	public void markAsRead(long uid) {
		em.createQuery("update Notification set status=true where uid=:uid").setParameter("uid", uid).executeUpdate();
	}
}
