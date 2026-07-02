package com.org.order.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestProjection;
import com.org.order.Inventory;

@Projection(name = "order_inventory_edit", types = { Inventory.class })
public interface InventoryEditProjection {
	public Long getId();

	public String getInvoiceNo();

	public String getType();

	public String getPhase();

	public String getBatchNo();

	public String getOrigin();

	public String getDestination();

	public String getEta();

	public String getLastLocation();

	public String getPackagingType();

	public int getPackageCount();

	public String getTransporter();

	public Date getLrDate();

	public String getLrDetails();

	public double getTransportationCharges();

	public double getLoadingUnloadingCharges();

	public String getProofOfDelivery();

	public String getStatus();

	public CompanyAutoSuggestProjection getCompany();

	public OrderAutoSuggestProjection getOrder();
}
