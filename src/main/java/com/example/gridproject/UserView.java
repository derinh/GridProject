package com.example.gridproject;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class UserView extends VerticalLayout {

    private final Grid<User> grid;

    @Autowired
    public UserView(UserService userService) {
        grid = new Grid<>(User.class, false);

        grid.addColumn(User::getId).setHeader("ID");
        grid.addColumn(User::getFirstName).setHeader("First Name");
        grid.addColumn(User::getLastName).setHeader("Last Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getPhone).setHeader("Phone");

        List<User> users = userService.fetchUsers();
        grid.setItems(users);

        add(grid);
    }
}
