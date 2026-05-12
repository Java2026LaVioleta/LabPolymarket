package com.polymarket.polymarket_backend;

import com.polymarket.polymarket_backend.model.User;
import com.polymarket.polymarket_backend.repository.UserRepository;
import com.polymarket.polymarket_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	void testAddFavoriteMarket() {
		User user = new User("fav@example.com", "password123", "favuser");
		User savedUser = userRepository.save(user);

		User result = userService.addFavoriteMarket(savedUser.getId(), "605431");

		assertEquals(1, result.getFavoriteMarketIds().size());
		assertTrue(result.getFavoriteMarketIds().contains("605431"));
	}

	@Test
	void testAddAndRemoveFavoriteMarket() {
		User user = new User("fav2@example.com", "password123", "favuser2");
		User savedUser = userRepository.save(user);

		userService.addFavoriteMarket(savedUser.getId(), "123");
		userService.addFavoriteMarket(savedUser.getId(), "456");
		User afterAdd = userService.addFavoriteMarket(savedUser.getId(), "789");

		assertEquals(3, afterAdd.getFavoriteMarketIds().size());

		User afterRemove = userService.removeFavoriteMarket(savedUser.getId(), "456");

		assertEquals(2, afterRemove.getFavoriteMarketIds().size());
		assertTrue(afterRemove.getFavoriteMarketIds().containsAll(Set.of("123", "789")));
	}

	@Test
	void testAddDuplicateFavoriteMarket() {
		User user = new User("fav3@example.com", "password123", "favuser3");
		User savedUser = userRepository.save(user);

		userService.addFavoriteMarket(savedUser.getId(), "111");
		User result = userService.addFavoriteMarket(savedUser.getId(), "111");

		assertEquals(1, result.getFavoriteMarketIds().size());
	}

	@Test
	void testRemoveNonExistentFavoriteMarket() {
		User user = new User("fav4@example.com", "password123", "favuser4");
		User savedUser = userRepository.save(user);

		User result = userService.removeFavoriteMarket(savedUser.getId(), "999");

		assertTrue(result.getFavoriteMarketIds().isEmpty());
	}

	@Test
	void testSaveUserToDatabase() {
		User user = new User("test@example.com", "password123", "testuser");

		User savedUser = userRepository.save(user);

		assertNotNull(savedUser.getId());
		assertEquals("test@example.com", savedUser.getEmail());
		assertEquals("password123", savedUser.getPassword());
		assertEquals("testuser", savedUser.getUsername());

		User fetchedUser = userRepository.findById(savedUser.getId()).orElse(null);
		assertNotNull(fetchedUser);
		assertEquals("test@example.com", fetchedUser.getEmail());
		assertEquals("testuser", fetchedUser.getUsername());
	}

	@Test
	void testDeleteUser() {
		User user = new User("delete@example.com", "password123", "deleteuser");
		User savedUser = userRepository.save(user);
		Long id = savedUser.getId();

		userRepository.deleteById(id);

		assertNull(userRepository.findById(id).orElse(null));
	}

	@Test
	void testUpdateUser() {
		User user = new User("update@example.com", "password123", "updateuser");
		User savedUser = userRepository.save(user);

		savedUser.setUsername("updateduser");
		savedUser.setEmail("updated@example.com");
		User updatedUser = userRepository.save(savedUser);

		assertEquals("updateduser", updatedUser.getUsername());
		assertEquals("updated@example.com", updatedUser.getEmail());
	}
}
