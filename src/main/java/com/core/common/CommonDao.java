package com.core.common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.core.forms.entity.EmailSubscription;
import com.core.forms.entity.Feedback;
import com.core.mail.MailService;

@Component
public class CommonDao {

	 
	@Autowired
	MailService mailService;
	
	@PersistenceContext
	private EntityManager em;
	
	@Value("${app.company.captchasecret}")
	private String captchasecret;
	
	@Value("${app.company.environment}")
	private String environment;
	 
	@Transactional
	public Ack createFeedBack(Feedback fb) {
		Ack ack = null;
		
		if (environment.equals("dev") || CommonUtils.validateRecaptcha(fb.getRecaptchaResponse(), captchasecret, "")) {
			em.persist(fb);
			ack = new Ack("Saved", "Successfully saved feedback.", 200);
		} else {
			ack = new Ack("Unable to save feedback", 400);
		}
		
		return ack;
	}

	
	
	@Transactional
	public void subscribe(String email) {
		TypedQuery<EmailSubscription> subQuery = em.createQuery("select c from EmailSubscription c where c.email=:email",
				EmailSubscription.class);
		subQuery.setParameter("email", email);

		List<EmailSubscription> lis = subQuery.getResultList();
		if (lis != null && lis.size() > 0) {
			EmailSubscription es = lis.get(0);
			if (es.getSubscribe()) {
				return;
			} else {
				String s = CommonUtils.getRandomAlphaNumericString(16);
				es.setSubscribe(true);
				es.setUqstring(s);
				
				if(!CommonUtils.isAnonymous()) {
					es.setUid(CommonUtils.getUserIdFromSession());
				}
				
				em.persist(es);
				return;
			}
		}
		
		String s = CommonUtils.getRandomNumericString(16);
		EmailSubscription es = new EmailSubscription();
		es.setEmail(email);
		es.setSubscribe(true);
		es.setUqstring(s);
		
		if(!CommonUtils.isAnonymous()) {
			es.setUid(CommonUtils.getUserIdFromSession());
		}
		
		em.persist(es);
	}

	@Transactional
	public boolean unsubscribe(String email, String token) {
		TypedQuery<EmailSubscription> subQuery = em.createQuery("select c from EmailSubscription c where c.email=:email",
				EmailSubscription.class);
		subQuery.setParameter("email", email);
		List<EmailSubscription> lis = subQuery.getResultList();
		if (lis != null && lis.size() > 0) {
			EmailSubscription es = lis.get(0);
			if (token != null && !"".equals(token) && token.equals(es.getUqstring())) {
				es.setSubscribe(false);
				es.setUqstring("UNSUBSCRIBE");
				em.merge(es);
				return true;
			}
		}

		return false;
	}
}
