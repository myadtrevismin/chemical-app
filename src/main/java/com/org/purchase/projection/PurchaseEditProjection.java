package com.org.purchase.projection;

import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestProjection;
import com.org.purchase.Purchase;

@Projection(name = "purchases_edit", types = { Purchase.class })
public interface PurchaseEditProjection {

	public Long getId();

	public String getCode();

	public String getContactName();

	public String getDescription();

	public String getEmail();

	public String getStatus();

	public String getPhone();

	public String getSource();

	public Date getEnquiryDate();
	
	public int getQuantity();

	public double getAmount();

	public Long getOrder();

	public CompanyAutoSuggestProjection getCompany();

	public List<PurchaseProductProjection> getProducts();
	
	String getStatusNotes();
}
