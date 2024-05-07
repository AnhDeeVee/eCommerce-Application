package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	@Autowired
	private UserController userController;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private CartRepository cartRepository;
	@MockBean
	private BCryptPasswordEncoder encoder;

	@Before
	public void setUp() {
		User user = new User();
		Cart cart = new Cart();
		user.setId(0);
		user.setUsername("user_name");
		user.setPassword("password123");
		user.setCart(cart);
		when(userRepository.findByUsername("user_name")).thenReturn(user);
		when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
		when(userRepository.findByUsername("user_name1")).thenReturn(null);

	}
	@Test
	public void createUser_normalCase() {
		when(encoder.encode("password123")).thenReturn("hashedPassword");
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("user_name");
		r.setPassword("password123");
		r.setConfirmPassword("password123");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
		assertEquals("user_name", u.getUsername());
		assertEquals("hashedPassword", u.getPassword());

	}


	@Test
	public void createUser_abnormalCase01() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("user_name");
		r.setPassword("abc");
		r.setConfirmPassword("abc");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void createUser_abnormalCase02() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("test");
		r.setPassword("abc123455");
		r.setConfirmPassword("abc123456");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void findUserByName_normalCase01() {
		var response = userController.findByUserName("user_name");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals("user_name", u.getUsername());
	}

	@Test
	public void findUserByName_normalCase02() {
		var response = userController.findByUserName("user_name1");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void findUserById_normalCase01() {
		var response = userController.findById(0L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
	}

	@Test
	public void findUserById_normalCase02() {
		var response = userController.findById(1L);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

}
