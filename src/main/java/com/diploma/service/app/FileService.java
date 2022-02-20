package com.diploma.service.app;

import com.diploma.entity.FileToCloud;
import com.diploma.exceptions.StorageException;
import com.diploma.repository.CloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final CloudRepository cloudRepository;

    @Autowired
    public FileService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public FileToCloud downloadFile(String authToken, String fileName) {
        return cloudRepository.downloadFile(authToken, fileName).orElseThrow(() -> new StorageException("Error download file " + fileName));
    }

    public List<FileToCloud> getFiles(String authToken, int limit) {
        return cloudRepository.getFiles(authToken, limit).orElseThrow(() -> new StorageException("Error getting file list"));
    }

    public void uploadFile(String authToken, String fileName, MultipartFile file) throws IOException {
        FileToCloud cloudFilePOJO = new FileToCloud(fileName, file.getContentType(), file.getBytes(), file.getSize());
        cloudRepository.uploadFile(cloudFilePOJO, authToken).orElseThrow(() -> new StorageException("Couldn't save the file " + fileName));
    }

    public void deleteFile(String authToken, String fileName){
        cloudRepository.deleteFile(authToken,fileName);
    }

    public void renameFile(String authToken, String fileName, String newFileName) {
        cloudRepository.renameFile(authToken, fileName, newFileName).orElseThrow(() -> new StorageException("Error edit file " + fileName));
    }

}
