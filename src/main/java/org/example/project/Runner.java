package org.example.project;

import lombok.SneakyThrows;
import org.example.engine.ClassPathDIEngine;
import org.example.project.model.User;
import org.example.project.service.UserService;

import java.time.LocalDate;
import java.util.Scanner;

public class Runner {

    @SneakyThrows
    public static void main(String[] args) {


        // 1. new instance
        // 2. @Autowired
        // 3. @Value -> properties file

        ClassPathDIEngine engine = new ClassPathDIEngine("org.example.project");
        engine.start();


        UserService userService = engine.getService(UserService.class);

        User user = User.builder()
                .lastName("Doe")
                .firstName("Sarah")
                .dateOfBirth(LocalDate.of(1980, 2, 2))
                .build();

        System.out.println(userService.saveUser(user));

        System.out.println("Find user by id");
        System.out.println(userService.findUserById(new Scanner(System.in).nextLine()));

    }
}
