package com.core.pushnotification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.Ack;
import com.core.common.CommonUtils;
import com.core.trans.CollectionEntity;
import com.core.trans.CollectionFilter;

/**
 * Handles requests for the Push Notifications page.
 */
@RestController
@RequestMapping("admin/notifications")
@Secured("ROLE_ADMIN")
public class AdminPushNotificationController {
	@Autowired
	PushNotificationDao pushDao;

	@RequestMapping(method = RequestMethod.GET)
	public CollectionEntity<PushNotification> getNotification(HttpServletRequest request) {
		CollectionFilter filter = CommonUtils.getCollectionFilter(request);

		return pushDao.getNotifications(filter);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Ack postNotification(@RequestBody PushNotification notification) {
		Ack ack = new Ack();

		notification.setId(0);
		pushDao.createNotification(notification);

		ack.setEntityId(notification.getId());

		return ack;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public PushNotification getNotification(Model model, @PathVariable long id) {
		return pushDao.getNotificationById(id);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public Ack updateNotification(@RequestBody PushNotification notification, @PathVariable long id) {
		Ack ack = null;

		notification.setId(id);
		pushDao.updateNotification(notification);

		return ack;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PATCH)
	public Ack patchNotification(@PathVariable long id) {
		Ack ack = null;

		pushDao.patchNotification(id);

		return ack;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public Ack deleteNotification(@PathVariable long id) {
		Ack ack = null;

		pushDao.deleteNotification(id);

		return ack;
	}

	@RequestMapping(value = "{id}/post", method = RequestMethod.POST)
	public Ack postNotification(@PathVariable long id) {
		Ack ack = null;

		pushDao.postNotification(id);

		return ack;
	}
}
