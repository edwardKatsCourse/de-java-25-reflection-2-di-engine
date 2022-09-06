package org.example.project.repository;

import org.example.engine.annotations.FileContent;
import org.example.engine.annotations.Service;
import org.example.engine.annotations.Value;
import org.example.project.model.User;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserFileRepository {

    @Value("users.file.path")
    private String filePath;

    @FileContent(path = "C:\\Users\\Edward\\Desktop\\dependency-injection-engine\\users.json")
    private String fileContent;


    private Map<String, User> users = new HashMap<>() {{
        put(
                "1",
                User.builder()
                        .id("1")
                        .firstName("John")
                        .lastName("Smith")
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .build()
        );
    }};

    public Collection<User> findAll() {
        return this.users.values();
    }

    public User save(User user) {

        var id = UUID.randomUUID().toString();
        user.setId(id);
        this.users.put(user.getId(), user);
        return user;
    }

    public User findById(String id) {
        return users.get(id);
    }


}
