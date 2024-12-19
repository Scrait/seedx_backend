package ru.scrait.seedx.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.scrait.seedx.dtos.CoinCheckRequest;
import ru.scrait.seedx.dtos.GetOrCreateResponse;
import ru.scrait.seedx.dtos.IdRequest;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.repositories.KeyRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/api/keys")
public class KeyController {

    private final KeyRepository keyRepository;

    @Autowired
    public KeyController(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @PostMapping("/get-or-create")
    public ResponseEntity<GetOrCreateResponse> getOrCreateKey(@RequestBody IdRequest id) {
        Key key = keyRepository.findById(id.getId()).orElse(null);

        if (key == null) {
            key = new Key();
            key.setId(id.getId());
            keyRepository.save(key);
        }

        return ResponseEntity.ok(new GetOrCreateResponse(key.isSub(),
                key.isSub() ? key.getCurrencies()
                        : new HashSet<>(Collections.singleton(Key.CryptoCurrency.TRON)),
                key.getSubscriptionExpirationDate()));
    }

    @PostMapping("/check-coin")
    public ResponseEntity<Map<String, Boolean>> checkCoinAvailability(@RequestBody CoinCheckRequest request) {
        Key key = keyRepository.findById(request.getId()).orElse(null);

        if (key == null) {
            return ResponseEntity.ok(Map.of("allowed", false));
        }

        if (!key.isSub()) {
            return ResponseEntity.ok(Map.of("allowed", request.getCoin().equals(Key.CryptoCurrency.TRON)));
        }

        boolean isAllowed = key.getCurrencies().contains(request.getCoin());

        return ResponseEntity.ok(Map.of("allowed", isAllowed));
    }
}