package com.core.pushnotification;

public class NotificationEntity {
	String title;
	String body;
	String icon;
	String image;
	String page;
	String sound;

	public NotificationEntity() {

	}

	public NotificationEntity(PushNotification notification) {
		this.title = notification.getName();
		this.body = notification.getDescription();
		this.image = notification.getImage();
		this.page = notification.getPage();
//		this.icon = "https://sftproperties.com/resources/images/icon.png";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}
}
