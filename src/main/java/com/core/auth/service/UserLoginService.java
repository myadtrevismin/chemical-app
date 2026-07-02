package com.core.auth.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.core.auth.dao.UserDao;
import com.core.auth.trans.TokenMetaData;
import com.core.auth.trans.UserSessionStore;
import com.core.common.CommonUtils;
import com.core.user.entity.User;

@Service("userDetailsService")
public class UserLoginService implements UserDetailsService {

	@Autowired
	UserDao userDao;

	public void updateUserLastSeen(List<TokenMetaData> lis) {
		userDao.updateUserLastSeen(lis);
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException("Username or password not matching");
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRole().getCode());

		UserSessionStore uss = new UserSessionStore(user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
				true, authorities);
		uss.setId(user.getId());
		uss.setName(user.getName());
		uss.setEmail(user.getEmail());
		uss.setGoogleid(user.getGoogleid());
		uss.setFacebookid(user.getFacebookid());
		uss.setDob(user.getDob());
		uss.setEnrolledDate(user.getCreationDate());
		uss.setReferralCode(user.getReferralCode());
		uss.setOnboarding(user.isOnboarding());

		if (!CommonUtils.isNotNullOrEmpty(user.getGoogleid()) && !CommonUtils.isNotNullOrEmpty(user.getFacebookid())
				&& !"Confirmed".equals(user.getConfirmToken())) {
			uss.setEmailPending(true);
		}

		if (CommonUtils.isNotNullOrEmpty(authorities)) {
			uss.setRole(authorities.get(0).getAuthority());
		}
		uss.setPermissions(user.getRole().getPermissions().stream().map(g->g.getPermission().getFlowCode()).collect(Collectors.toList()));
		 

		return uss;
	}

	private List<GrantedAuthority> buildUserAuthority(String userRole) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		setAuths.add(new SimpleGrantedAuthority(userRole));

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}

}
