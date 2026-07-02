package com.core.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.ClientProtocolException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.core.auth.dao.AuthDao;
import com.core.auth.dao.UserDao;
import com.core.auth.service.UserLoginService;
import com.core.auth.trans.FacebookLogin;
import com.core.auth.trans.GoogleLogin;
import com.core.auth.trans.RegistrationObj;
import com.core.auth.trans.SigninObj;
import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionStore;
import com.core.common.Ack;
import com.core.common.CommonUtils;
import com.core.common.SystemException;
import com.core.mail.MailService;
import com.core.user.entity.User;
import com.core.user.entity.UserImage;
import com.core.user.repo.UserRepo;

@RestController
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	AuthDao authDao;

	@Autowired
	UserDao signinHelper;

	@Autowired
	MailService mailService;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserDao userDao;

	@Autowired
	UserLoginService userDetailsService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Value("${app.company.googleplusapikey}")
	private String googlePlusApiKey;
	
	 

	private UserSessionStore getUserSessionStore(HttpServletRequest request, HttpServletResponse response, String email)
			throws IllegalArgumentException, UnsupportedEncodingException {
		UserSessionStore uss = (UserSessionStore) userDetailsService.loadUserByUsername(email);

		loginBuildToken(request, response, uss);

		return uss;
	}

	private void loginBuildToken(HttpServletRequest request, HttpServletResponse response, UserSessionStore uss)
			throws IllegalArgumentException, UnsupportedEncodingException {
		authDao.buildToken(uss);

		AuthStatConstats.setUserSessionStrore(uss.getId(), uss);

		authDao.storeUserToken(uss.getId(), uss.getToken(), uss.getTokenExpiry(), request.getHeader("User-Agent"));

		TokenMetaData metaData = authDao.constructTokenMetadata(uss, null);
		metaData.setStudentSessionLogId(authDao.addStudentSessionLog(uss.getId(), uss.getToken()));
	}

	@RequestMapping(value = "forgot-password", method = RequestMethod.POST)
	public Ack postForgotPassword(HttpServletRequest request) {
		Ack ack = null;
		String errorMsg = null;

		String userName = request.getParameter("userName");

		try {
			User user = userDao.getUserForForgotPassword(userName);
			
			if (user != null) {
				mailService.sendForgotPasswordToken(user);
			} else {
				errorMsg = "UserName doesn't exists.";
			}
		} catch (Exception ex) {
			errorMsg = "Unable to process forgot password" + ex.getMessage();
		}

		if (errorMsg == null) {
			ack = new Ack(null, "Forgot Password", "Check your email or phone number for password reset instructions", 201);
		} else {
			ack = new Ack(null, User.class.getSimpleName(), errorMsg, 500);
		}

		return ack;
	}
	
	@RequestMapping(value = "send-otp", method = RequestMethod.POST)
	public Ack sendOtp(HttpServletRequest request) {
		Ack ack = null;
		String errorMsg = null;

		String mobile = request.getParameter("mobile");

		try {
			User user = userDao.sendOTP(mobile);
			
			if (user != null) {
			} else {
				errorMsg = "Mobile number doesn't registered.";
			}
		} catch (Exception ex) {
			errorMsg = "Unable to process forgot password" + ex.getMessage();
		}

		if (errorMsg == null) {
			ack = new Ack(null, "OTP generated", "Check your phone number for OTP", 201);
		} else {
			ack = new Ack(null, User.class.getSimpleName(), errorMsg, 500);
		}

		return ack;
	}

	@RequestMapping(value = "save-new-password", method = RequestMethod.POST)
	public Ack saveNewpassword(HttpServletRequest request) {
		Ack ack = null;

		String email = request.getParameter("email");
		String password = request.getParameter("chgPassword");
		String cpassword = request.getParameter("cnfPassword");
		String emailToken = request.getParameter("emailToken");
		String mobileToken = request.getParameter("mobileToken");

		String msg = null;

		if (email == null) {
			msg = "Email id invalid.";
		} else if (emailToken == null ) {
			msg = "Token invalid";
		}

		if (msg == null) {
			User user = userDao.getUserByEmail(email);

			if (user != null) {
				if (!email.equals(user.getEmail())) {
					msg = "Email id invalid.";
				} 
				
				else if (!emailToken.equals(user.getEmailOtp())) {
					msg = "Email Token invalid";
				} /*
					 * else if (!mobileToken.equals(user.getMobileOtp())) { msg =
					 * "Mobile Token invalid"; }
					 */
				
				else {
					if (password == null) {
						msg = "No password provided.";
					} else if (!password.equals(cpassword)) {
						msg = "Passwords didn't matched.";
					} else {
						userDao.changePasswordForEmail(email, password);
					}
				}
			} else {
				msg = "Email id invalid.";
			}
		}

		if (msg == null) {
			ack = new Ack("Password reset", "Successfully changed password.", 201);
		} else {
			ack = new Ack("Password reset", msg, 500);
		}

		return ack;
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public Ack registeruser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RegistrationObj regObj) {
		Ack ack = null;
		String errorMsg = null;

		try {
			User user = userDao.saveUser(regObj);

			// mailService.sendWelcomeMail(user);
		} catch (SystemException ex) {
			errorMsg = "Unable to create user - " + ex.getMessage();
		} catch (Exception ex) {
			errorMsg = "Unable to create user" + ex.getMessage();
		}

		if (errorMsg == null) {
			if(regObj.isVendor()) {
				ack = new Ack(null, User.class.getSimpleName(), "", 200);
			} else {
				ack = login(request, response, regObj);
			}
		} else {
			ack = new Ack(null, User.class.getSimpleName(), errorMsg, 400);
		}

		return ack;
	}

	@RequestMapping(value = "user-login", method = RequestMethod.POST, consumes = "application/json")
	public Ack login(HttpServletRequest request, HttpServletResponse response, @RequestBody SigninObj loginObj) {
		Ack ack = null;
		UserSessionStore uss = null;
		String errorMsg = null;

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				loginObj.getUserName(), loginObj.getPassword());

		try {
			Authentication auth = authenticationManager.authenticate(authRequest);
			uss = (UserSessionStore) auth.getPrincipal();
			loginBuildToken(request, response, uss);
		} catch (BadCredentialsException e) {
			errorMsg = "Invalid Username or password ";
		} catch (org.springframework.security.authentication.DisabledException e) {
			errorMsg = "Account is disabled, Confirm email to enable the account ";
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "Invalid Username or password ";
		}

		if (errorMsg == null) {
			ack = new Ack(null, 200);
			ack.setUser(uss);
		} else {
			ack = new Ack(errorMsg, 400);
		}

		return ack;
	}
	
	@RequestMapping(value = "user-login-otp", method = RequestMethod.POST)
	public Ack loginOTP(HttpServletRequest request, HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String otp = request.getParameter("otp");

		Ack ack = null;
		String errorMsg = null;
		UserSessionStore uss = null;
				
		try {
			User user = userDao.loginOTP(mobile, otp);
			
			try {
				uss = getUserSessionStore(request, response, user.getEmail());
			} catch (JSONException e) {
				errorMsg = "Invalid Access token/Unable to validate access token";
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				errorMsg = "Invalid Access token/Unable to validate access token";
				logger.error(e.getMessage(), e);
			}
		} catch (SystemException ex) {
			errorMsg = "Unable to login - " + ex.getMessage();
		}

		if (errorMsg == null) {
			ack = new Ack(null, 200);
			ack.setUser(uss);
		} else {
			ack = new Ack(null, User.class.getSimpleName(), errorMsg, 400);
		}

		return ack;
	}

	@RequestMapping(value = "login/facebook", method = RequestMethod.POST, consumes = "application/json")
	public Ack loginWithFacebook(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FacebookLogin fbLogin) {
		Ack ack = null;
		UserSessionStore uss = null;
		String errorMsg = null;

		if (fbLogin.getAccessToken() != null) {
			try {
				User user = signinHelper.storeFacebookuser(fbLogin);

				uss = getUserSessionStore(request, response, user.getEmail());
			} catch (JSONException e) {
				errorMsg = "Invalid Access token/Unable to validate access token";
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				errorMsg = "Invalid Access token/Unable to validate access token";
				logger.error(e.getMessage(), e);
			}
		} else {
			errorMsg = "Invalid Access token";
		}

		if (errorMsg == null) {
			ack = new Ack(null, 200);
			ack.setUser(uss);
		} else {
			ack = new Ack(errorMsg, 200);
		}

		return ack;
	}

	@RequestMapping(value = "login/google", method = RequestMethod.POST, consumes = "application/json")
	public Ack loginWithGoogle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody GoogleLogin googleLogin) {
		Ack ack = null;
		UserSessionStore uss = null;
		String errorMsg = null;

		try {
			User user = signinHelper.storeGoogleUser(googleLogin, request.getHeader("x-request-from"));

			uss = getUserSessionStore(request, response, user.getEmail());
		} catch (ClientProtocolException e) {
			errorMsg = "";
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			errorMsg = "";
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			errorMsg = "";
			logger.error(e.getMessage(), e);
		} catch (SystemException e) {
			errorMsg = e.getMessage();
			logger.error(e.getMessage(), e);
		}

		if (errorMsg == null) {
			ack = new Ack(null, 200);
			ack.setUser(uss);
		} else {
			ack = new Ack(errorMsg, 400);
		}

		return ack;
	}

	@RequestMapping(value = "logout", method = RequestMethod.POST)
	@PreAuthorize("isFullyAuthenticated()")
	public void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		User user = CommonUtils.getUserFromSession();

		if (user != null) {
			Long studentId = user.getId();

			// UserSessionStore uss = AuthStatConstats.getUserSessionStore(studentId);

			String token = request.getHeader("authorization");
			// Token start with Bearer
			if (token != null) {
				token = token.substring(7);
			}
			signinHelper.logoutUserToken(studentId, token);
			Set<TokenMetaData> tokenData = AuthStatConstats.removeToken(studentId, token);

			if (tokenData.size() == 0) {
				AuthStatConstats.removeUserSessionStore(studentId);
			}
		}
	}

	@RequestMapping(value = "user-images/{id}", method = RequestMethod.GET)
	public Ack checkUserImage(@PathVariable long id) {
		Ack ack = new Ack();

		User user = userDao.getUser(id);

		if (user != null) {
			UserImage image = userDao.getUserImage(id);

			ack.setUrl(CommonUtils.getUserImage(user, image, googlePlusApiKey));
		}

		return ack;
	}

	@RequestMapping(value = "user-images/{id}.jpg", method = RequestMethod.GET)
	@ResponseBody
	public void getUserImage(HttpServletResponse response, @PathVariable long id) {
		UserImage image = userDao.getUserImage(id);

		try {
			response.setContentType("image/jpeg");

			if (image != null && image.getImage() != null) {
				InputStream in1 = new ByteArrayInputStream(image.getImage());
				IOUtils.copy(in1, response.getOutputStream());
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error(e.getMessage(), e);
		}
	}
}
