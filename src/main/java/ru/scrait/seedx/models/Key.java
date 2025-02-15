package ru.scrait.seedx.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "keys")
@Data
public class Key {

    @Id
    private String id;

    @Column(name = "subscription_expiration_date")
    private LocalDate subscriptionExpirationDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "key_currencies", joinColumns = @JoinColumn(name = "key_id"))
    @Column(name = "currency")
    private Set<CryptoCurrency> currencies = new HashSet<>(Collections.singleton(CryptoCurrency.TRON));

    private int speed = 300;

    // Check if subscription is active
    public boolean isSub() {
        return subscriptionExpirationDate != null && subscriptionExpirationDate.isAfter(LocalDate.now());
    }

    public enum CryptoCurrency {
        BTC, BNB, SOL, AVAX, OP, ETH, TRON, LTC, POLYGON, DOGE
    }

}
