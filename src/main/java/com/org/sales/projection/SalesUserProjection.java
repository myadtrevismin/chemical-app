package com.org.sales.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.user.entity.UserMiniProjection;
import com.org.sales.SalesUser;

@Projection(name = "sales-user", types = { SalesUser.class })
public interface SalesUserProjection {
	Long getId();

	UserMiniProjection getUser();
}
