package com.core.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.Ack;
import com.core.common.CommonUtils;
import com.core.notification.NotificationService;
import com.core.service.AdminService;

@RestController
@PreAuthorize("isFullyAuthenticated()")
public class PrivateController {
	@Autowired
	AdminService adminService;
	
	@Autowired
	NotificationService notificaionService;

	@ResponseBody
	@RequestMapping(value = "invoices/{id}.pdf", method = RequestMethod.GET)
	public void downloadQuote(@PathVariable("id") long id, HttpServletResponse response) {
		try {
			adminService.downloadInvoice(id, response);
			response.setContentType("application/pdf");
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@RequestMapping(value = "invoices/send/{id}.pdf", method = RequestMethod.GET)
	public void sendInvoice(@PathVariable("id") long id, HttpServletResponse response) {
		adminService.sendInvoice(id);
		response.setContentType("application/pdf");
	}

	@RequestMapping(value = "quotations/{id}/products/{pid}", method = RequestMethod.PATCH)
	public void sendQuotation(@PathVariable("id") long id, @PathVariable("pid") long pid) {
		adminService.sendQuotation(id, pid);
	}
	@RequestMapping(value = "purchase-enquiry/{id}/products/{pid}", method = RequestMethod.PATCH)
	public void sendPurchaseEnquiry(@PathVariable("id") long id, @PathVariable("pid") long pid) {
		adminService.sendPurchaseEnquiry(id, pid);
	}

	@RequestMapping(value = "inventory/{prodId}/{salesId}")
	public List<InventoryDetails> getInventory(@PathVariable("prodId") Long prodId,
			@PathVariable("salesId") Long salesId) {

		return adminService.loadInventory(prodId, salesId);
	}
	@RequestMapping(value = "inventory-update/{prodId}/{salesId}", method = RequestMethod.POST)
	public Ack saveSalesInventory(@RequestBody List<InventoryDetails> inventoryDetails,@PathVariable("prodId") Long prodId,
			@PathVariable("salesId") Long salesId) {
		return adminService.saveInventory(inventoryDetails,prodId, salesId);
		
	}
	@RequestMapping(value = "notification-cnt")
	public Ack getNotificationCount() {
		long cnt=notificaionService.getCount(CommonUtils.getUserIdFromSession());
		Ack ack=new Ack();
		ack.setStatus(200);
		ack.setMessage(cnt+"");
		return ack;
	}
	@RequestMapping(value = "notification-read")
	public Ack readNotification() {
		 notificaionService.markAsRead(CommonUtils.getUserIdFromSession());
		Ack ack=new Ack();
		ack.setStatus(200);
		return ack;
	}

}
