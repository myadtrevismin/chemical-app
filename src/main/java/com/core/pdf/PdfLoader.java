package com.core.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.core.auth.dao.UserDao;
import com.core.auth.trans.UserSessionStore;
import com.core.mail.MailService;

@Component
public class PdfLoader {
	@Autowired
	UserDao userDao;

	@Autowired
	HtmlToPdfService pdfService;

	@Autowired
	MailService mailService;

	@Autowired
	private SpringTemplateEngine pdfsEngine;

	public enum Pdfs {
		Invoice("invoice", "Order Invoice"),
		Quotation("quotation", "Sales Quotation");

		private String val;
		private String subject;

		Pdfs(String val, String subject) {
			this.val = val;
			this.subject = subject;
		}

		public String getVal() {
			return this.val;
		}

		public String getSubject() {
			return this.subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}
	}

	public void loadPdf(Object obj, Pdfs form, OutputStream os) {
		final Context ctx = new Context(Locale.ENGLISH);
		ctx.setVariable(form.toString(), obj);
		final String htmlContent = pdfsEngine.process(form.getVal(), ctx);

		try {
			pdfService.htmlToPdf(htmlContent, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void sendAttachmentMail(UserSessionStore uss, Object obj, Pdfs form) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		loadPdf(obj, form, os);

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

//		mailService.sendAttachmentMail(new Context(Locale.ENGLISH), uss, is, form);
	}
}
