package com.org.sales.projection;

import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestProjection;
import com.org.sales.Sales;
import com.org.sales.SalesQuotation;

@Projection(name = "sales_quotation_edit", types = { SalesQuotation.class })
public interface SalesQuotationEditProjection {

	Long getId();

	String getCode();

	String getSpecification();

	String getMake();

	String getTerms();

	double getGst();

	String getPacking();

	double getTransportationCharges();

	Integer getDeliveryPeriod();

	Date getValidTill();
	
	public int getQuantity();

	public double getAmount();

	CompanyAutoSuggestProjection getCompany();

	public List<SalesQuotationProductProjection> getProducts();

	public Date getCreationDate();
	
	SalesEditProjection getEnquiry();
}
