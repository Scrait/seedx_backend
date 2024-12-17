package ru.scrait.seedx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.repositories.KeyRepository;

@Service
public class KeyService {

    private final KeyRepository keyRepository;

    @Autowired
    public KeyService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    // Method to get Key by keyId
    public Key getKeyById(String keyId) {
        return keyRepository.findById(keyId).orElse(null);  // Fetch by keyId, return null if not found
    }

    // Method to save the updated Key
    public Key saveKey(Key key) {
        return keyRepository.save(key);  // Save the updated Key entity to the database
    }
}
