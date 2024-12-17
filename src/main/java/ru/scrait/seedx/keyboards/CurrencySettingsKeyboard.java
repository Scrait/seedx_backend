package ru.scrait.seedx.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.utils.ButtonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CurrencySettingsKeyboard {

    /**
     * Генерация клавиатуры для настройки валют.
     *
     * @param key Текущие настройки пользователя (модель Key)
     * @return InlineKeyboardMarkup для настройки валют
     */
    public InlineKeyboardMarkup getCurrencySettingsKeyboard(Key key) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Получаем список валют, которые включены для пользователя
        Set<Key.CryptoCurrency> selectedCurrencies = key.getCurrencies();

        Key.CryptoCurrency[] currencies = Key.CryptoCurrency.values();
        for (Key.CryptoCurrency currency : currencies) {
            boolean isSelected = selectedCurrencies.contains(currency);  // Валюта включена или нет

            // Создаем строку с кнопкой для валюты
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(createCurrencyButton(currency, isSelected, key.getId()));  // Добавляем keyId в колбек

            keyboard.add(row);
        }

        // Устанавливаем клавиатуру без кнопки "Назад"
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    /**
     * Создает кнопку для валюты с индикацией включения/выключения.
     *
     * @param currency Валюта
     * @param isSelected Включена ли валюта
     * @param keyId ID ключа пользователя
     * @return InlineKeyboardButton
     */
    private InlineKeyboardButton createCurrencyButton(Key.CryptoCurrency currency, boolean isSelected, String keyId) {
        String buttonText = (isSelected ? "✅ " : "☐ ") + currency.name();  // Отображаем галочку, если валюта включена
        // Добавляем keyId в callbackData
        String callbackData = "t_" + currency.name() + "_" + keyId;  // Формируем callbackData с ID ключа
        return ButtonUtil.createInlineButton(buttonText, callbackData);
    }

}
