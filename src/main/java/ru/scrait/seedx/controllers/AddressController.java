package ru.scrait.seedx.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.scrait.seedx.components.AddressComponent;
import ru.scrait.seedx.dtos.AddressResponse;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressComponent addressComponent;

    public AddressController(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }

    @GetMapping("/get")
    public ResponseEntity<AddressResponse> getAddress() {
        return ResponseEntity.ok(new AddressResponse(addressComponent.getAddress()));
    }

}
