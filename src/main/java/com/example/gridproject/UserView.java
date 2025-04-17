package com.example.gridproject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.notification.Notification;

import java.util.List;
import java.util.Comparator;

@Route("")
public class UserView extends VerticalLayout {

    private final Grid<User> grid;

    @Autowired
    public UserView(UserService userService) {
        grid = new Grid<>(User.class, false);

        grid.addColumn(User::getId).setHeader("ID");
        grid.addColumn(User::getFirstName).setHeader("First Name").setKey("firstName");;
        grid.addColumn(User::getLastName).setHeader("Last Name");
        grid.addColumn(User::getEmail).setHeader("Email").setKey("email");
        grid.addColumn(User::getPhone).setHeader("Phone");

        CallbackDataProvider<User, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return userService.fetchUsers(offset, limit).stream();
                },
                query -> userService.fetchUserCount()
        );

        Button nextUserButton = new Button("Show next User", e -> showNextUser());

        grid.setDataProvider(dataProvider);
        add(grid, nextUserButton);
    }

    private void showNextUser() {
        User selectedUser = grid.asSingleSelect().getValue();
        if (selectedUser == null) {
            Notification.show("No user selected");
            return;
        }

        //Get current visual sort order
        Comparator<User> comparator = null;
        for (GridSortOrder<User> sortOrder : grid.getSortOrder()) {
            Grid.Column<User> column = sortOrder.getSorted();
            com.vaadin.flow.data.provider.SortDirection direction = sortOrder.getDirection();

            if ("firstName".equals(column.getKey())) {
                Comparator<User> comp = Comparator.comparing(User::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase));
                comparator = direction == SortDirection.ASCENDING ? comp : comp.reversed();
            }
        }

        var visibleSortedUsers = comparator != null
                ? grid.getLazyDataView().getItems().sorted(comparator).toList()
                : grid.getLazyDataView().getItems().toList();

        int index = visibleSortedUsers.indexOf(selectedUser);
        if(index >= 0 && index < visibleSortedUsers.size() - 1) {
            User nextUser = visibleSortedUsers.get(index + 1);
            grid.asSingleSelect().setValue(nextUser);
            grid.scrollToIndex(index + 1);
        } else {
            Notification.show("This is the last visible user.");
        }
    }

}
