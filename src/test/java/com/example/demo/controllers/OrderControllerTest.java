package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
	@Autowired
	private OrderController orderController;
	@MockBean
	private OrderRepository orderRepository;
	@MockBean
	private UserRepository userRepository;

	@Before
	public void setUp() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Pen");
		BigDecimal price = BigDecimal.valueOf(9.99);
		item.setPrice(price);
		item.setDescription("Item description: Pen");
		List<Item> items = new ArrayList<>();
		items.add(item);

		User user = new User();
		Cart cart = new Cart();
		user.setId(0);
		user.setUsername("user_name");
		user.setPassword("password123");
		cart.setId(0L);
		cart.setUser(user);
		cart.setItems(items);
		BigDecimal total = BigDecimal.valueOf(2.99);
		cart.setTotal(total);
		user.setCart(cart);
		when(userRepository.findByUsername("user_name")).thenReturn(user);
		when(userRepository.findByUsername("user_name2")).thenReturn(null);

	}
	
	@Test
	public void test_submitOrder() {
		var response = orderController.submit("user_name");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		UserOrder order = response.getBody();
		assertNotNull(order);
		assertEquals(1, order.getItems().size());
	}
	
	@Test
	public void test_submitOrder_abnormalCase() {
		var response = orderController.submit("user_name1");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void test_getOrder() {
		var ordersForUser = orderController.getOrdersForUser("user_name");
		assertNotNull(ordersForUser);
		assertEquals(200, ordersForUser.getStatusCodeValue());
		List<UserOrder> orders = ordersForUser.getBody();
		assertNotNull(orders);

	}

	@Test
	public void test_getOrderForUser_abnormalCase() {
		var ordersForUser = orderController.getOrdersForUser("user_name1");
		assertNotNull(ordersForUser);
		assertEquals(404, ordersForUser.getStatusCodeValue());

	}

}
