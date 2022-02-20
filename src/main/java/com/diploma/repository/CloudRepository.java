package com.diploma.repository;

import com.diploma.entity.FileToCloud;
import com.diploma.exceptions.StorageException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;


@Data
@Transactional
@Repository
public class CloudRepository {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public CloudRepository(UserRepository userRepository,
                           FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    private Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public void login(String authToken, UserDetails userPrincipal) {
        tokenStorage.put(authToken, userPrincipal);
    }

    public Optional<UserDetails> logout(String authToken) {
        return ofNullable(tokenStorage.remove(authToken));
    }

    public Optional<FileToCloud> uploadFile(FileToCloud cloudFile, String authToken) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            cloudFile.setUserId(userId.get());
            return Optional.of(fileRepository.save(cloudFile));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public void deleteFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            fileRepository.removeByUserIdAndName(userId.get(), fileName);
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public Optional<FileToCloud> downloadFile(String authToken, String fileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            return fileRepository.findByUserIdAndName(userId.get(), fileName);
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    public Optional<FileToCloud> renameFile(String authToken, String fileName, String newFileName) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            Optional<FileToCloud> cloudFile = fileRepository.findByUserIdAndName(userId.get(), fileName);
            cloudFile.ifPresent(file -> file.setName(newFileName));
            return Optional.of(fileRepository.save(Objects.requireNonNull(cloudFile.orElse(null))));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }


    public Optional<List<FileToCloud>> getFiles(String authToken, int limit) {
        Optional<Long> userId = getUserId(authToken);
        if (userId.isPresent()) {
            return ofNullable(fileRepository.findAllByUserIdWithLimit(userId.get(), limit));
        } else {
            throw new StorageException("Invalid auth-token");
        }
    }

    private Optional<Long> getUserId(String authToken) {
        UserDetails user = tokenStorage.get(authToken.substring(7));
        return user != null ? ofNullable(Objects.requireNonNull(userRepository.findByLogin(user.getUsername()).orElse(null)).getId()) : Optional.empty();
    }
}
