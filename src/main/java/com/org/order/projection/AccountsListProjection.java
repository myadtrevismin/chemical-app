package com.org.order.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.projection.CompanyAutoSuggestProjection;
import com.org.order.Accounts;

@Projection(name = "order_accounts_list", types = { Accounts.class })
public interface AccountsListProjection {
	public Long getId();

	public String getInvoiceNo();

	public String getType();

	public String getBankName();

	public long getAccountNo();

	public String getPaymentType();

	public String getReferenceNo();

	public String getPaymentTerm();

	public double getAmountPaid();

	public Date getDueDate();

	public String getStatus();

	public String getDescription();

	public CompanyAutoSuggestProjection getCompany();

	public OrderAutoSuggestProjection getOrder();

	public Date getCreationDate();
}
