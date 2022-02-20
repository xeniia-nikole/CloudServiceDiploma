package com.diploma.init;

import com.diploma.exceptions.Errors;
import com.diploma.model.Role;
import com.diploma.model.UserEntity;
import com.diploma.properties.StorageProperties;
import com.diploma.repository.RoleRepository;
import com.diploma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InsertUser implements ApplicationRunner {

    private static final String USER1LOGIN = "user";
    private static final String USER1PASSWORD = "qwerty";
    private static final String  ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Path rootLocation;

    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public InsertUser(UserRepository userRepository,
                      RoleRepository roleRepository, StorageProperties storageProperties, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role roleUser = new Role(1L, ROLE_USER);
        roleRepository.save(roleUser);

        UserEntity user = new UserEntity();
        user.setLogin(USER1LOGIN);
        user.setPassword(bcryptEncoder.encode(USER1PASSWORD));
        user.setRole(roleUser);

        userRepository.save(user);

        // creating user directories
        userRepository.findAll().forEach(userPDO -> {
            try {
                Files.createDirectories(rootLocation.resolve(userPDO.getLogin()));
            } catch (IOException e) {
                throw new RuntimeException(Errors.COULD_NOT_CREATE_USER_DIRECTORIES.value() + e.getMessage());
            }
            System.out.println(userPDO);
        });
    }
}
