package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
	@Autowired
	private CartController cartController;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private CartRepository cartRepository;
	@MockBean
	private ItemRepository itemRepository;

	@Before
	public void setUp() {
		User user = new User();
		Cart cart = new Cart();
		user.setId(0);
		user.setUsername("user_name");
		user.setPassword("password123");
		user.setCart(cart);

		Item item = new Item();
		item.setId(1L);
		item.setName("Item");
		BigDecimal price = BigDecimal.valueOf(100);
		item.setPrice(price);
		item.setDescription("Item description");

		when(userRepository.findByUsername("user_name")).thenReturn(user);
		when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

	}

	@Test
	public void test_addToCart() {
		ModifyCartRequest r = new ModifyCartRequest();
		r.setItemId(1L);
		r.setQuantity(1);
		r.setUsername("user_name");
		var response = cartController.addTocart(r);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		var cart = response.getBody();
		assertNotNull(cart);
		assertEquals(BigDecimal.valueOf(100), cart.getTotal());

	}
	@Test
	public void test_addToCart_invalidUser() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1L);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("user_name2");
		var response = cartController.addTocart(modifyCartRequest);

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void test_addToCart_invalidItem() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(2L);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("user_name");
		var response = cartController.addTocart(modifyCartRequest);

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void test_deleteFromCart() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1L);
		modifyCartRequest.setQuantity(2);
		modifyCartRequest.setUsername("user_name");
		var response = cartController.addTocart(modifyCartRequest);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());

		modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1L);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("user_name");
		response = cartController.removeFromcart(modifyCartRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Cart cart = response.getBody();
		assertNotNull(cart);
		assertEquals(BigDecimal.valueOf(100), cart.getTotal());

	}

	@Test
	public void test_deleteFromCart_invalidUser() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1L);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("user_name2");
		var response = cartController.removeFromcart(modifyCartRequest);

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void test_deleteFromCart_invalidItem() {
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(2L);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("user_name");
		var response = cartController.removeFromcart(modifyCartRequest);

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

}
