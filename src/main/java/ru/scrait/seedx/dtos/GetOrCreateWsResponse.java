package ru.scrait.seedx.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.scrait.seedx.models.Key;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetOrCreateWsResponse {

    private boolean isSub;
    private Set<Key.CryptoCurrency> coins;
    private int[] subscriptionExpirationDate;

}
