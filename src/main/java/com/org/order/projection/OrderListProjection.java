package com.org.order.projection;

import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestOrderProjection;
import com.org.order.Order;

@Projection(name = "order_list", types = { Order.class })
public interface OrderListProjection {

	public Long getId();

	public String getCode();

	public String getType();

	public Long getEnquiryId();

	public String getEmail();

	public String getStatus();

	public String getPhone();

	public String getContactName();

	public String getDescription();

	public String getTerms();

	public String getBillingAddress();

	public int getQuantity();

	public double getAmount();

	public CompanyAutoSuggestOrderProjection getCompany();

	public List<OrderProductProjection> getProducts();

	public Date getCreationDate();
}
