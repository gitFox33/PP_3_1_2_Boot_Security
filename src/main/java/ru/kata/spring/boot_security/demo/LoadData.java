package ru.kata.spring.boot_security.demo;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;


@Component
public class LoadData {

    private final UserService userService;

    public LoadData(UserService userService) {
        this.userService = userService;
    }


    @PostConstruct
    public void init() {

        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        userService.createRole(adminRole);
        userService.createRole(userRole);


        User alex = new User("Alex", "Alex@gmail", 24, "user", "user");
        alex.addRole(userRole);

        User boris = new User("Boris", "Boris@gmail", 31, "admin", "admin");
        boris.addRole(adminRole);

        User natalia = new User("Natalia", "Natalia@gmail", 43, "natalia", "natalia");
        natalia.addRole(userRole);

        User oleg = new User("Oleg", "Oleg@gmail", 14, "oleg", "oleg");
        oleg.addRole(adminRole);

        userService.createUser(alex);
        userService.createUser(boris);
        userService.createUser(natalia);
        userService.createUser(oleg);


    }
}

