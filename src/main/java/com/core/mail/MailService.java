package com.core.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.document.service.Document;
import com.core.pdf.PdfLoader.Pdfs;
import com.core.user.entity.User;
import com.org.common.MailObj;
import com.org.company.Company;
import com.org.order.Order;
import com.org.product.Product;
import com.org.purchase.Purchase;
import com.org.purchase.PurchaseProduct;
import com.org.sales.SalesProduct;
import com.org.sales.SalesQuotation;
import com.org.tracking.Tracking;

@Component
@ConfigurationProperties(prefix = "app.company.smtp")
public class MailService {

	private final Log logger = LogFactory.getLog(getClass());

	private enum Templates {
		Welcome("welcome", "Welcome"), ForgotPassword("forgot-password", "Reset password instructions"),
		SalesEnquiry("sales", "Equired about the property"),
		AdminSalesEnquiry("admin-sales", "User equired about the property"),
		ProductExpiry("product-expiry", "Products Expired"), DocumentExpiry("document-expiry", "Documents Expired"),
		FollowupAdmin("followups-admin", "Today followups"), Followup("followups-user", "Today followups"),
		NewCompany("new-company", "Company Created"), NewProduct("new-product", "Product Created"),
		SalesUserAssigned("user-assigned", "Sales Enquiry assigned"),
		PurchaseUserAssigned("user-assigned", "Purchase Enquiry assigned"),
		StockUpdated("product-stock-update", "Product Stock updated"),
		OrderCompleted("order-completed", "Order Completed"), OrderInvoice("order-invoice", "Order Invoice"),
		SalesQuotation("sales-quotation", "Sales Quotation"), Template("template", "Template"),
		PurchaseQuotation("purchase-enquiry", "Purchase Enquiry"),
		Tracking("tracking", "Sample Tracking");

		private String val;
		private String subject;

		Templates(String val, String subject) {
			this.val = val;
			this.subject = subject;
		}

		public String getVal() {
			return this.val;
		}

		public String getSubject() {
			return this.subject;
		}
	}

	@Value("${app.company.smtp.from}")
	String from;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private GSuiteService gSuiteService;

	private void addUrls(final Context ctx) {
		ctx.setVariable("websiteUrl", CommonData.WebSiteUrl);
		ctx.setVariable("adminUrl", CommonData.AdminUrl);
		ctx.setVariable("logoUrl", CommonData.AdminUrl + "img/logo.png?v=1");
		ctx.setVariable("fblogoUrl", CommonData.AdminUrl + "img/email/fb.png?v=1");
		ctx.setVariable("lilogoUrl", CommonData.AdminUrl + "img/email/linkedin.png?v=1");
		ctx.setVariable("twlogoUrl", CommonData.AdminUrl + "img/email/tt.png?v=1");
		ctx.setVariable("inlogoUrl", CommonData.AdminUrl + "img/email/instagram.png?v=1");
	}

	private void sendMail(final Context ctx, String email, String fromEmail, User user, Templates emailTemp) {
		try {
			ctx.setVariable("user", user);
			ctx.setVariable("userName", CommonUtils.getUserName(user));
			addUrls(ctx);

			final String htmlContent = templateEngine.process(emailTemp.getVal(), ctx);

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setSubject(emailTemp.getSubject());
			helper.setText(htmlContent, true);
			helper.setTo(email);
			helper.setFrom(fromEmail);
			// helper.setFrom(from, "company");

			// javaMailSender.send(message);
			gSuiteService.send(fromEmail, message);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void sendAttachmentMail(final Context ctx, String email, String fromEmail, User user, Templates emailTemp,
			Pdfs form, ByteArrayInputStream is) {
		try {
			ctx.setVariable("user", user);
			ctx.setVariable("userName", CommonUtils.getUserName(user));
			addUrls(ctx);

			final String htmlContent = templateEngine.process(emailTemp.getVal(), ctx);

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.addAttachment(form.toString(), new ByteArrayDataSource(is, "application/pdf"));
			helper.setSubject(emailTemp.getSubject());
			helper.setText(htmlContent, true);
			helper.setTo(email);
			helper.setFrom(fromEmail);
			// helper.setFrom(from, "company");

			// javaMailSender.send(message);
			gSuiteService.send(fromEmail, message);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Async
	public void sendWelcomeMail(User user, String password) {
		try {
			Context ctx = new Context(Locale.ENGLISH);
			ctx.setVariable("password", password);

			sendMail(ctx, user.getEmail(), from, user, Templates.Welcome);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Async
	public void sendForgotPasswordToken(User user) {
		try {
			Context ctx = new Context(Locale.ENGLISH);
			ctx.setVariable("token", user.getEmailOtp());

			sendMail(ctx, user.getEmail(), Constants.systemEmail, user, Templates.ForgotPassword);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Async
	public void sendTemplateMail(User user, String subject, String body) {
		Context ctx = new Context(Locale.ENGLISH);

		ctx.setVariable("emailBody", body);

		Templates template = Templates.Template;

		sendMail(ctx, user.getEmail(), from, user, template);
	}

	@Async
	public void sendSalesEnquiry(User user, String propertyName, String referralCode) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.SalesEnquiry;
		ctx.setVariable("propertyName", propertyName);
		ctx.setVariable("referralCode", referralCode);
		sendMail(ctx, user.getEmail(), from, user, template);

		sendAdminSalesEnquiry(user, propertyName, referralCode);
	}

	@Async
	private void sendAdminSalesEnquiry(User user, String propertyName, String referralCode) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.AdminSalesEnquiry;
		ctx.setVariable("propertyName", propertyName);
		ctx.setVariable("referralCode", referralCode);
		sendMail(ctx, Constants.adminEmail, from, user, template);
	}

	@Async
	public void sendNewCompanyMail(Company company, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.NewCompany;
		ctx.setVariable("company", company);
		sendMail(ctx, Constants.adminEmail, from, user, template);
	}

	@Async
	public void sendProductExpirty(List<Product> products) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.ProductExpiry;
		ctx.setVariable("products", products);
		sendMail(ctx, Constants.adminEmail, Constants.systemEmail, null, template);
	}

	@Async
	public void sendDocumentExpirty(List<Document> products) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.DocumentExpiry;
		ctx.setVariable("documents", products);
		ctx.setVariable("name", "Admin");
		ctx.setVariable("msg", "Please take appropriate action");
		sendMail(ctx, Constants.adminEmail, Constants.systemEmail, null, template);
	}

	@Async
	public void sendSalesPurchaseEnquiryAssignedMail(SalesMailObj post, User user) {
		Context ctx = new Context(Locale.ENGLISH);
		ctx.setVariable("obj", post);
		Templates template = null;
		if ("sales".equals(post.getType())) {
			template = Templates.SalesUserAssigned;
		} else {
			template = Templates.PurchaseUserAssigned;
		}

		sendMail(ctx, user.getEmail(), Constants.systemEmail, user, template);
	}

	@Async
	public void sendFollowupsMail(User user, List<MailObj> fmoLis) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.Followup;
		ctx.setVariable("followups", fmoLis);
		sendMail(ctx, user.getEmail(), Constants.systemEmail, user, template);
	}

	@Async
	public void sendTrackingMail(Tracking track) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.Tracking;
		ctx.setVariable("track", track);
		sendMail(ctx, track.getCompany().getEmail(), Constants.systemEmail, new User(track.getCompany()), template);
	}

	@Async
	public void sendAdminFollowupsMail(List<MailObj> adminList) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.FollowupAdmin;
		ctx.setVariable("followups", adminList);
		sendMail(ctx, Constants.adminEmail, Constants.systemEmail, null, template);
	}

	@Async
	public void sendNewProductMail(Product product, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.NewProduct;
		ctx.setVariable("product", product);
		sendMail(ctx, Constants.adminEmail, Constants.systemEmail, null, template);
	}

	@Async
	public void sendStockChangeAlert(MailObj obj, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.StockUpdated;
		ctx.setVariable("obj", obj);
		sendMail(ctx, Constants.adminEmail, Constants.systemEmail, null, template);
	}

	@Async
	public void sendOrderCompleted(MailObj obj, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.OrderCompleted;
		ctx.setVariable("obj", obj);
		sendMail(ctx, Constants.adminEmail, user.getEmail(), user, template);
	}

	@Async
	public void sendInvoiceEmail(ByteArrayOutputStream os, Order order, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.OrderInvoice;
		ctx.setVariable("order", order);

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

		sendAttachmentMail(ctx, order.getCompany().getEmail(), user.getEmail(), user, template, Pdfs.Quotation, is);
	}

	@Async
	public void sendQuotationEmail(SalesQuotation quote, SalesProduct product, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.SalesQuotation;
		ctx.setVariable("quote", quote);
		ctx.setVariable("product", product);
		sendMail(ctx, quote.getEnquiry().getEmail(), user.getEmail(), user, template);
	}

	public void sendDocumentExpirtyForCompany(Document post, String name, String to) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.DocumentExpiry;
		List<Document> products = new ArrayList<>();
		products.add(post);
		ctx.setVariable("name", name);
		ctx.setVariable("documents", products);
		ctx.setVariable("msg", "Please send the document to " + Constants.systemEmail);
		sendMail(ctx, to, Constants.systemEmail, null, template);
	}

	public void sendEnquiryEmail(Purchase enqury, PurchaseProduct product, User user) {
		Context ctx = new Context(Locale.ENGLISH);

		Templates template = Templates.PurchaseQuotation;
		ctx.setVariable("quote", enqury);
		ctx.setVariable("product", product);
		sendMail(ctx, enqury.getEmail(), user.getEmail(), user, template);
		
	}
}