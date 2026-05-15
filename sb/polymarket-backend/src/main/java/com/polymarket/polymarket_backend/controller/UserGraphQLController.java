package com.polymarket.polymarket_backend.controller;

import com.polymarket.polymarket_backend.model.User;
import com.polymarket.polymarket_backend.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<User> users() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public User user(@Argument Long id) {
        return userService.getUserById(id).orElse(null);
    }

    @MutationMapping
    public User createUser(@Argument String email, @Argument String password, @Argument String username) {
        return userService.createUser(new User(email, password, username));
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String email, @Argument String password, @Argument String username) {
        return userService.updateUser(id, new User(email, password, username));
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        try {
            userService.deleteUser(id);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @MutationMapping
    public User addFavoriteMarket(@Argument Long userId, @Argument String marketId) {
        return userService.addFavoriteMarket(userId, marketId);
    }

    @MutationMapping
    public User removeFavoriteMarket(@Argument Long userId, @Argument String marketId) {
        return userService.removeFavoriteMarket(userId, marketId);
    }
}
