package com.core.sms.template;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.user.entity.UserMiniProjection;

@Projection(name = "template_details", types = { Template.class })
public interface TemplateProjection {
	long getId();

	String getName();

	String getMessage();

	Date getCreationDate();

	UserMiniProjection getUser();
}
