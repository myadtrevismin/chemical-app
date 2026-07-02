package com.core.auth.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.core.auth.AuthStatConstats;
import com.core.auth.trans.FacebookLogin;
import com.core.auth.trans.GoogleLogin;
import com.core.auth.trans.RegistrationObj;
import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionLog;
import com.core.auth.trans.UserSessionStore;
import com.core.common.Ack;
import com.core.common.CommonData.SocialType;
import com.core.common.CommonUtils;
import com.core.common.SystemException;
import com.core.mail.MailService;
import com.core.role.UserBaseRole;
import com.core.role.UserBaseRolePermission;
import com.core.trans.Dashboard;
import com.core.trans.NewOperations;
import com.core.user.entity.User;
import com.core.user.entity.UserImage;

@Component
public class UserDao {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Value("${app.company.googleplusapikey}")
	private String googlePlusApiKey;

	@Value("${app.company.googleclientid}")
	String googleclientid;

	@Autowired
	MailService mailService;

	public User findByUsername(String username) {
		List<User> lis = null;
		try {
			TypedQuery<User> tq = em.createQuery(
					"select w from User w where (w.email=:wid or w.mobile=:wid) and w.enabled is true", User.class);

			tq.setParameter("wid", username);
			lis = tq.getResultList();
			if (lis == null || lis.size() == 0) {
				return null;
			}
			User user = lis.get(0);
			List<UserBaseRolePermission> perms=user.getRole().getPermissions();
			perms.forEach(g->g.getPermission());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return null;
	}

	public User getUserDetails(long id) {
		return em.find(User.class, id);
	}

	public Dashboard getUserDashboard() {
		UserSessionStore uss = CommonUtils.getUserSessionStore();

		Dashboard dash = new Dashboard();

		if (uss.getRole().equals("ROLE_ADMIN")) {
			dash.setUsers(Long.parseLong(em.createNativeQuery("select count(1) from user where role!='ROLE_ADMIN'")
					.getSingleResult().toString()));

			dash.setCompanies(
					Long.parseLong(em.createNativeQuery("select count(1) from company").getSingleResult().toString()));

			dash.setContacts(Long.parseLong(
					em.createNativeQuery("select count(1) from company_contact").getSingleResult().toString()));
			
			dash.setProducts(
					Long.parseLong(em.createNativeQuery("select count(1) from product").getSingleResult().toString()));

			dash.setSales(
					Long.parseLong(em.createNativeQuery("select count(1) from sales").getSingleResult().toString()));

			dash.setPurchases(
					Long.parseLong(em.createNativeQuery("select count(1) from purchase").getSingleResult().toString()));

			dash.setOrders(
					Long.parseLong(em.createNativeQuery("select count(1) from orders").getSingleResult().toString()));

			dash.setInvoices(
					Long.parseLong(em.createNativeQuery("select count(1) from orders").getSingleResult().toString()));
		} else if(uss.getPermissions() != null) {
			if(uss.getPermissions().contains("MG_CM")) {
				dash.setCompanies(
						Long.parseLong(em.createNativeQuery("select count(1) from company").getSingleResult().toString()));
				
				dash.setContacts(Long.parseLong(
						em.createNativeQuery("select count(1) from company_contact").getSingleResult().toString()));
			} 
			
			if(uss.getPermissions().contains("MG_PD")) {
				dash.setProducts(
						Long.parseLong(em.createNativeQuery("select count(1) from product").getSingleResult().toString()));
			} 
			
			if(uss.getPermissions().contains("MG_SE")) {
				dash.setSales(
						Long.parseLong(em.createNativeQuery("select count(1) from sales").getSingleResult().toString()));
			} 
			
			if(uss.getPermissions().contains("MG_PR")) {
				dash.setPurchases(
						Long.parseLong(em.createNativeQuery("select count(1) from purchase").getSingleResult().toString()));
			} 
			
			if(uss.getPermissions().contains("MG_OR")) {
				dash.setOrders(
						Long.parseLong(em.createNativeQuery("select count(1) from orders").getSingleResult().toString()));
				
				dash.setInvoices(
						Long.parseLong(em.createNativeQuery("select count(1) from orders").getSingleResult().toString()));
			}
		}

		return dash;
	}

	@Transactional
	public void updateUserLastSeen(List<TokenMetaData> lis) {
		Query qry = em.createQuery("update UserSessionLog set endTime =:endTime where id=:id");
		lis.forEach(s -> {
			qry.setParameter("endTime", s.getLastAccessTime());
			qry.setParameter("id", s.getStudentSessionLogId());
			qry.executeUpdate();
		});

	}

	@Transactional
	public void saveUserWithProfile(User up) {
		UserSessionStore uss = CommonUtils.getUserSessionStore();

		User up1 = em.find(User.class, uss.getId());

		up1.setLocation(up.getLocation());
		up1.setName(up.getName());
		up1.setGender(up.getGender());
		up1.setDob(up.getDob());

		em.persist(up1);

		uss.setName(up.getName());
		uss.setDob(up.getDob());
	}

	private boolean checkReferralCodeAvailability(String code) {
		Query q = em.createNativeQuery("select count(t.id) from user t where t.referral_code=:code");
		q.setParameter("code", code);
		Object obj = q.getSingleResult();

		if (Long.parseLong(obj.toString()) > 0) {
			return false;
		} else {
			return true;
		}
	}

	private String getNewReferralCode() {
		String code = CommonUtils.getRandomAlphaNumericString(6);

		while (!checkReferralCodeAvailability(code)) {
			code = CommonUtils.getRandomAlphaNumericString(6);
		}

		return code;
	}

	@Transactional
	public void createUser(User user) {
		try {

			if (!CommonUtils.isNotNullOrEmpty(user.getConfirmToken())) {
				user.setConfirmToken("Confirmed");
			}

			if (user.getRole() == null) {
				user.setRole(new UserBaseRole(2));
			}

			if (CommonUtils.isNotNullOrEmpty(user.getPassword())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			} else {
				user.setPassword(passwordEncoder.encode(CommonUtils.getRandomString(6)));
			}
			user.setEnabled(true);
			user.setCreationDate(new Date());
			user.setReferralCode(getNewReferralCode());
			em.persist(user);

			em.flush();
		} catch (Exception ex) {
			throw new SystemException(CommonUtils.getUserCreateException(user, ex));
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void createSocialUser(User user) {
		user.setConfirmToken("Confirmed");

		if (user.getId() == 0) {
			createUser(user);
		} else {
			em.merge(user);
		}
	}

	@Transactional
	public void logoutUserToken(Long studentId, String token) {
		em.createQuery("update UserToken set active=false where uid=:uid and token=:token")
				.setParameter("uid", studentId).setParameter("token", token).executeUpdate();
	}

	public User getUser(long id) {
		return em.find(User.class, id);
	}

	public boolean isKYCcompleted(long uid, String table) {
		int count = Integer.parseInt(em.createNativeQuery("select count(1) from " + table + " where uid=:uid")
				.setParameter("uid", uid).getSingleResult().toString());

		return count > 0 ? true : false;
	}

	public void loadUserSessionStore(UserSessionStore uss) {
		UserImage image = getUserImage(uss.getId());

		uss.setImageURL(CommonUtils.getUserImage(uss, image, googlePlusApiKey));
	}

	private String getUserPassword(long userId) {
		Query query = em.createNativeQuery("select password from user where id=:userId");
		query.setParameter("userId", userId);

		return query.getSingleResult().toString();
	}

	private void updateUserPassword(User user, String password) {
		String token = CommonUtils.getRandomNumericString();
		Date da = new Date((new Date().getTime() + (365 * 24 * 60 * 60 * 1000)));

		Query query = em.createQuery("UPDATE User SET password=:password WHERE id=:userId");

		query.setParameter("userId", user.getId()).setParameter("password", passwordEncoder.encode(password))
				.executeUpdate();
	}

	@Transactional
	public Ack changePassword(String oldPassword, String newPassword) {
		User user = CommonUtils.getUserFromSession();
		String password = getUserPassword(user.getId());
		Ack ack = new Ack();
		if (passwordEncoder.matches(oldPassword, password)) {
			updateUserPassword(user, newPassword);

			ack.setStatus(200);
			ack.setMessage("Password updated");
		} else {
			ack.setStatus(400);
			ack.setMessage("Old password didn't match");
		}
		return ack;

	}

	@Transactional
	public String updateUserImage(byte[] b) {
		UserSessionStore uss = CommonUtils.getUserSessionStore();

		UserImage image = getUserImage(uss.getId());
		if (image == null) {
			image = new UserImage();
			image.setUid(uss.getId());
		}

		image.setImageVersion(image.getImageVersion() + 1);
		image.setImage(b);
		em.persist(image);

		uss.setImageURL(CommonUtils.getUserImage(uss, image, googlePlusApiKey));

		return uss.getImageURL();
	}

	public UserImage getUserImage(long userId) {
		TypedQuery<UserImage> tq = em.createQuery("select u from UserImage u where u.uid=:uid", UserImage.class);
		tq.setParameter("uid", userId);
		List<UserImage> list = tq.getResultList();
		if (CommonUtils.isNotNullOrEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public User getUserByEmail(String email) {
		TypedQuery<User> tq = em.createQuery("select w from User w where w.email=:email", User.class);
		tq.setParameter("email", email);

		List<User> lis = tq.getResultList();
		if (lis.size() == 0) {
			return null;
		} else {
			return lis.get(0);
		}
	}

	private String[] updatePasswordResetToken(long userId) {
		String emailToken = CommonUtils.getRandomNumericString(4);
		String mobileOtp = CommonUtils.getRandomNumericString(4);
		String a[] = new String[2];
		em.createQuery("UPDATE User SET emailOtp=:token, mobileOtp=:mobileOtp where id=:userId")
				.setParameter("userId", userId).setParameter("token", emailToken).setParameter("mobileOtp", mobileOtp)
				.executeUpdate();

		a[0] = emailToken;
		a[1] = mobileOtp;

		return a;
	}

	@Transactional
	public User getUserForForgotPassword(String userName) {
		TypedQuery<User> tq = em.createQuery("select u from User u where u.email=:userName", User.class);
		tq.setParameter("userName", userName);

		List<User> lis = tq.getResultList();

		if (lis.isEmpty()) {
			return null;
		} else {
			User user = lis.get(0);
			String ap[] = updatePasswordResetToken(user.getId());
			user.setEmailOtp(ap[0]);
			user.setMobileOtp(ap[1]);

			return user;
		}
	}

	@Transactional
	public User sendOTP(String mobile) {
		TypedQuery<User> tq = em.createQuery("select u from User u where u.mobile=:mobile", User.class);
		tq.setParameter("mobile", mobile);

		List<User> lis = tq.getResultList();

		if (lis.isEmpty()) {
			return null;
		} else {
			User user = lis.get(0);
			String ap[] = updatePasswordResetToken(user.getId());
			user.setEmailOtp(ap[0]);
			user.setMobileOtp(ap[1]);

			return user;
		}
	}

	@Transactional
	public User loginOTP(String mobile, String otp) {
		TypedQuery<User> tq = em.createQuery("select u from User u where u.mobile=:mobile", User.class);
		tq.setParameter("mobile", mobile);

		List<User> lis = tq.getResultList();

		if (lis.isEmpty()) {
			throw new SystemException("User doesn't exists.");
		} else {
			User user = lis.get(0);

			if (user.getMobileOtp().equals(otp)) {
				user.setMobileOtp(null);
				em.persist(user);

				return user;
			} else {
				throw new SystemException("OTP entered is wrong.");
			}
		}
	}

	@Transactional
	public void changePasswordForEmail(String email, String password) {
		User user = getUserByEmail(email);

		updateUserPassword(user, password);
	}

	@Transactional
	public User veryfyEmail(String email, String token) {
		User u = getUserByEmail(email);

		if (token != null && token.equals(u.getConfirmToken())) {
			u.setConfirmToken("Confirmed");
			em.persist(u);

			UserSessionStore uss = AuthStatConstats.getUserSessionStore(u.getId());
			if (uss != null) {
				uss.setEmailPending(false);
			}
			return u;
		}

		return null;
	}

	private User getUserForSocial(String email) {
		return getUserByEmail(email);
	}

	private User checkForNewUser(String appId, SocialType type) {
		String query = "select us from User us where us." + type.toString() + "id=:appId";

		TypedQuery<User> tq = em.createQuery(query, User.class);
		tq.setParameter("appId", appId);

		List<User> ul = tq.getResultList();
		if (ul == null || ul.size() == 0) {
			return null;
		}
		return ul.get(0);

	}

	@Transactional
	public User storeGoogleUser(GoogleLogin googleObj, String reqFrom)
			throws JSONException, ClientProtocolException, IOException {
		User user = null;
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet get = new HttpGet("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + googleObj.getIdToken());

		HttpResponse resp = client.execute(get);
		String jsonStr = EntityUtils.toString(resp.getEntity());
		JSONObject json = new JSONObject(jsonStr);

		if (!(json.has("aud") && json.getString("aud").equals(googleclientid))) {
			throw new SystemException("Invalid Access token / Unable to validate access token");
		}

		String userId = json.getString("sub");

		SocialType type = SocialType.google;

		user = checkForNewUser(userId, type);

		if (user == null) {

			// check user present in registered Users;;

			String email = json.get("email").toString();

			user = getUserForSocial(email);

			if (user == null) {
				throw new SystemException("UnAuthorized User.");
			} else {
				user.setGoogleid(userId);

				try {
					String fname = null;
					if (json.has("given_name")) {
						fname = json.getString("given_name");
					} else if (json.has("name")) {
						fname = json.getString("name");
					}

					user.setName(fname);

					if (json.has("family_name")) {
						user.setLname(json.getString("family_name"));
					}

					if (json.has("picture")) {
						// up.setGoogleImageUrl(json.getString("picture"));
					}

					createSocialUser(user);
				} catch (Exception e) {
					throw new SystemException("Invalid Access token/Unable to validate access token");
				}
			}
		}

		return user;
	}

	@Transactional
	public User storeFacebookuser(FacebookLogin fbObj) throws JSONException, Exception {
		HttpClient client = CommonUtils.clientBulder.build();
		String urlForAccessing = "https://graph.facebook.com/v2.8/me?access_token=" + fbObj.getAccessToken()
				+ "&fields=id,name,email,first_name,last_name,gender,link,languages,"
				+ "education,work,website,birthday,age_range,about,cover,hometown,timezone";

		HttpGet get = new HttpGet(urlForAccessing);

		HttpResponse response = client.execute(get);

		String jsonString = EntityUtils.toString(response.getEntity());

		JSONObject json1 = new JSONObject(jsonString);

		if (json1.has("error")) {
			throw new Exception();
		}

		String userId = json1.getString("id");
		String email = json1.getString("email");

		SocialType type = SocialType.facebook;

		User user = checkForNewUser(userId, type);

		if (user == null) {
			user = getUserForSocial(email);

			if (user == null) {
				user = new User();
				user.setEmail(email);

			}

			user.setFacebookid(userId);

			String fname = json1.getString("first_name");
			if (CommonUtils.isNotNullOrEmpty(fname)) {
				user.setName(fname);
			}

			String lname = json1.getString("last_name");
			if (CommonUtils.isNotNullOrEmpty(fname)) {
				user.setLname(lname);
			}

			createSocialUser(user);

		}

		return user;
	}

	@Transactional
	public Ack changeEmailId(UserSessionStore user, String mailId) {
		Ack ack = new Ack();

		if (user.isEmailPending()) {
			em.createQuery("UPDATE User SET email=:mailId WHERE id=:userId").setParameter("mailId", mailId)
					.setParameter("userId", user.getId()).executeUpdate();

			ack.setStatus(200);
			ack.setMessage("Email updated, please verify your email using link sent to your email");
			user.setEmail(mailId);
		} else {
			ack.setStatus(400);
			ack.setMessage("Email already verified, cann't change email now.");

		}
		return ack;
	}

	@Transactional
	public User saveUser(RegistrationObj regObj) {
		if (!CommonUtils.isNotNullOrEmpty(regObj.getName())) {
			new SystemException("Invalid Name");
		}

		if (!CommonUtils.isNotNullOrEmpty(regObj.getEmail()) || !CommonUtils.validateEmail(regObj.getEmail())) {
			new SystemException("Invalid Email Id");
		}

		User user = new User();
		user.setCountryCode(regObj.getCode());
		user.setMobile(regObj.getPhone());
		user.setEmail(regObj.getEmail());
		user.setPassword(regObj.getPassword());
		user.setName(regObj.getName());
		user.setLname(regObj.getLname());
		user.setPassword(regObj.getPassword());
		user.setConfirmToken(CommonUtils.getRandomString(5));

		mailService.sendWelcomeMail(user, regObj.getPassword());

		createUser(user);

		if (regObj.isVendor()) {
			user.setEnabled(false);
			em.persist(user);
		}

		return user;
	}

	@SuppressWarnings("unchecked")
	public NewOperations getNewOperations() {
		NewOperations operations = new NewOperations();

		UserSessionStore user = CommonUtils.getUserSessionStore();

		Query qry = em
				.createQuery("select t from UserSessionLog t where t.uid=" + user.getId() + " order by startTime desc");
		qry.setMaxResults(2);

		List<UserSessionLog> msg = qry.getResultList();
//		if (msg.size() > 1) {
//			UserSessionLog sl = msg.get(1);
//			
//			Query query = em.createQuery("select count(1) from User t"
//					+ " where t.creationDate>=:creationDate and t.parent=:uid");
//			query.setParameter("uid", user.getId());
//			if (sl.getEndTime() != null) {
//				query.setParameter("creationDate", sl.getEndTime());
//			} else {
//				query.setParameter("creationDate", sl.getStartTime());
//			}
//			
//			int count = Integer.parseInt(query.getSingleResult().toString());
//			operations.setVendors(count);
//			
//			if("ROLE_ADMIN".equals(user.getRole())) {
//				query = em.createQuery("select count(1) from User t"
//						+ " where t.creationDate>=:creationDate");
//				if (sl.getEndTime() != null) {
//					query.setParameter("creationDate", sl.getEndTime());
//				} else {
//					query.setParameter("creationDate", sl.getStartTime());
//				}
//				
//				count = Integer.parseInt(query.getSingleResult().toString());
//				operations.setAgents(count);
//			}
//		}

		return operations;
	}
}
