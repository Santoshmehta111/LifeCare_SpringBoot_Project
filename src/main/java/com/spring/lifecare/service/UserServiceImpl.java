package com.spring.lifecare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.lifecare.entites.User;
import com.spring.lifecare.exception.ServiceException;
import com.spring.lifecare.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public User createUserDetail(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			return userRepository.save(user);
		} catch (Exception e) {

			throw new ServiceException("Failed to create User Details");
		}
	}

	@Override
	public User findUserByEmail(String email) {
		try {

			return userRepository.findByEmail(email);
		} catch (Exception e) {

			throw new ServiceException("Failed to find User by email");
		}
	}


	@Override
	public User getUserByuserId(int userId) throws ServiceException {
		return userRepository.getUserByuserId(userId);
	}
}