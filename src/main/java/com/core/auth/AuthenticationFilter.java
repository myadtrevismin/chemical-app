package com.core.auth;

import java.io.IOException;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.auth.dao.AuthDao;
import com.core.auth.service.UserLoginService;
import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionStore;
import com.core.auth.trans.UserToken;
import com.core.common.CommonData.JWTAttrs;

@Component("jwtAuth")
public class AuthenticationFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private Algorithm macAuth = null;

	private JWTVerifier verifier = null;

	 @Autowired
	 AuthDao authDao;

	@Autowired
	private UserLoginService userDetailsService;

	public UserLoginService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserLoginService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	public void init() {
		try {
			macAuth = Algorithm.HMAC256(JWTAttrs.SECRET.toString());
			verifier = JWT.require(macAuth).withIssuer(JWTAttrs.ISSUER.toString()).build();
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} 
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;

		if (!request.getServletPath().startsWith("/pages/app/payment.html")) {
			if (request.getServletPath() != null && request.getServletPath().startsWith("/web-socket")) {
				token = request.getParameter("token");
			} else {
				token = request.getHeader("authorization");
				// Token start with Bearer
				if (token != null) {
					token = token.substring(7);
				}
			}
		}

		if (token != null) {
			try {
				DecodedJWT jwt = verifier.verify(token);

				long studentId = jwt.getClaim(JWTAttrs.CLAIM.toString()).asLong();
				UserSessionStore uss = AuthStatConstats.getUserSessionStore(studentId);

				if (uss == null) {
					synchronized (this) {
						uss = AuthStatConstats.getUserSessionStore(studentId);
						if (uss == null) {
							UserToken validToken = authDao.getValidToken(token, studentId);

							if (validToken != null) {
								uss = (UserSessionStore) userDetailsService.loadUserByUsername(jwt.getSubject());
								uss.setToken(token);
								uss.setTokenExpiry(validToken.getValidTill().getTime());
								AuthStatConstats.setUserSessionStrore(studentId, uss);
								authDao.constructTokenMetadata(uss, validToken);
								authDao.addStudentSessionLog(validToken.getUid(), validToken.getToken());
							} else {

								throw new RuntimeException();
							}
						}
					}

				} else {
					if (!AuthStatConstats.isTokenExists(studentId, token)) {
						UserToken validToken = authDao.getValidToken(token, studentId);
						if (validToken != null) {
							authDao.constructTokenMetadata(uss, validToken);
						} else {
							throw new RuntimeException();
						}
					}
				}

				AuthStatConstats.updateTime(studentId, token);
				Optional<TokenMetaData> tmd = AuthStatConstats.findTokenMetadata(studentId, token);
 
				Authentication authentication = new UsernamePasswordAuthenticationToken(uss, "",
						tmd.isPresent() ? tmd.get().getAuthorities() : uss.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				e.printStackTrace();
				// logger.error(e.getMessage(), e);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
