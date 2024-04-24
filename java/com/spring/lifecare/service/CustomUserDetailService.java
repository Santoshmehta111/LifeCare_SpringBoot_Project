package com.spring.lifecare.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.lifecare.entites.User;
import com.spring.lifecare.exception.ServiceException;
import com.spring.lifecare.repository.UserRepository;


/**
 * CustomUserDetailsService is an implementation of the Spring Security
 * UserDetailsService interface. It provides functionality to load user details
 * by username, specifically designed for user authentication.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Attempts to load user details by the provided username.
	 *
	 * @param username The username (email) of the user to be loaded.
	 * @return An implementation of UserDetails representing the loaded user.
	 * @throws UsernameNotFoundException If the user with the given username is not
	 *                                   found.
	 * @throws ServiceException          If an unexpected service error occurs
	 *                                   during user loading.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, ServiceException {
		User user = userRepository.findByEmail(username);

		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetail(user);
	}
}