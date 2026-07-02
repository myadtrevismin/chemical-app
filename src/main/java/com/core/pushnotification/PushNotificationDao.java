package com.core.pushnotification;

import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.core.trans.CollectionEntity;
import com.core.trans.CollectionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("pushDao")
public class PushNotificationDao extends PushNotificationSender {
	@PersistenceContext
	private EntityManager em;

	@Value("${app.company.fcm_server_key}")
	private String fcmServerKey;

	public CollectionEntity<PushNotification> getNotifications(CollectionFilter filter) {
		List<PushNotification> users = null;
		Long totalResults = Long
				.parseLong(em.createNativeQuery("select count(1) from push_notification").getSingleResult().toString());

		if (totalResults > 0 && ((filter.getOffset() - 1) * filter.getLimit()) < totalResults) {
			TypedQuery<PushNotification> tq = em.createQuery(
					"select t from PushNotification t order by t.postingDate desc, t.active desc",
					PushNotification.class);
			tq.setFirstResult((filter.getOffset() - 1) * filter.getLimit()).setMaxResults(filter.getLimit());

			users = tq.getResultList();
		}

		return new CollectionEntity<PushNotification>(users, totalResults);
	}

	public PushNotification getNotificationById(long notificationId) {
		return em.find(PushNotification.class, notificationId);
	}

	@Transactional
	public void createNotification(PushNotification notification) {
		if (notification.getPostingDate() == null) {
			notification.setPostingDate(new Date());
		}

		notification.setActive(true);

		em.persist(notification);
	}

	@Transactional
	public void updateNotification(PushNotification notification) {

		PushNotification emNotification = getNotificationById(notification.getId());

		if (emNotification.isPosted() && emNotification.getPostingDate().compareTo(notification.getPostingDate()) < 0) {
			emNotification.setPosted(false);
		}

		emNotification.setName(notification.getName());
		emNotification.setDescription(notification.getDescription());
		emNotification.setPostingDate(notification.getPostingDate());

		em.merge(notification);
	}

	@Transactional
	public void patchNotification(long id) {
		PushNotification notification = getNotificationById(id);

		notification.setActive(!notification.isActive());

		em.merge(notification);
	}

	@Transactional
	public void deleteNotification(long id) {

	}

	@Transactional
	public void postNotification(long id) {
		PushNotification notification = getNotificationById(id);

		if (notification.isActive()) {
			try {
				sendFcmTopicNotifications("ALL", new NotificationEntity(notification));

				notification.setPosted(true);
				em.merge(notification);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendFcmTopicNotifications(String topic, NotificationEntity notification) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonStr = om.writeValueAsString(notification);

		JSONObject jp = new JSONObject();
		jp.put("notification", new JSONObject(jsonStr));
		jp.put("to", "/topics/" + topic);

		JSONObject data = constructPushData(notification);
		if (data != null) {
			jp.put("data", data);
		}

		sendFcmNotification(jp.toString());
	}

}
