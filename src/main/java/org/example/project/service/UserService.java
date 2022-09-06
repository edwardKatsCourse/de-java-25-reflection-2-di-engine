package org.example.project.service;

import org.example.engine.annotations.Autowired;
import org.example.engine.annotations.Service;
import org.example.project.model.User;
import org.example.project.repository.UserFileRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserFileRepository userFileRepository;


    public Collection<User> findAdultUsers() {
        return userFileRepository.findAll();
    }

    public List<User> findAllByLastname(String lastName) {
        return new ArrayList<>();
    }

    public User saveUser(User user) {
        return userFileRepository.save(user);
    }

    public User findUserById(String id) {
        return userFileRepository.findById(id);
    }


}
