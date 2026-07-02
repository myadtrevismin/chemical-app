package com.org.tracking.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestProjection;
import com.org.product.projection.ProductAutoSuggestProjection;
import com.org.tracking.Tracking;

@Projection(name = "tracking_edit", types = { Tracking.class })
public interface TrackingEditProjection {

	public Long getId();
	
	public String getAssignedTo();

	public String getRemarks();

	public String getDocketNo();
	
	public String getCourierCompany();

	public Date getDispatchDate();
	
	public Date getReceivedDate();

	public ProductAutoSuggestProjection getProduct();
	
	public CompanyAutoSuggestProjection getCompany();
}
