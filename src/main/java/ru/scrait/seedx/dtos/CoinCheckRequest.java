package ru.scrait.seedx.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.scrait.seedx.models.Key;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoinCheckRequest extends IdRequest {

    private Key.CryptoCurrency coin;

}
