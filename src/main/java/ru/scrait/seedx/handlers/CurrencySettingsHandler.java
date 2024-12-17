package ru.scrait.seedx.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.keyboards.CurrencySettingsKeyboard;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.services.MessageService;
import ru.scrait.seedx.services.KeyService;

import java.util.Set;

@Component
public class CurrencySettingsHandler implements IHandler {

    private final CurrencySettingsKeyboard currencySettingsKeyboard;
    private final KeyService keyService;

    public CurrencySettingsHandler(CurrencySettingsKeyboard currencySettingsKeyboard, KeyService keyService) {
        this.currencySettingsKeyboard = currencySettingsKeyboard;
        this.keyService = keyService;
    }

    @Override
    public void execute(Update update, MessageService messageService, KeyService keyService) {
        String callbackData = update.getCallbackQuery().getData();

        // Check if the callback data starts with "toggle_" or "settings_currencies"
        if (!callbackData.startsWith("t_")) return;

        // Extract the keyId from callbackData (it's the part after the currency name)
        String[] callbackParts = callbackData.split("_");
        if (callbackParts.length < 3) {
            // Invalid callback data format
            return;
        }

        String currencyName = callbackParts[1];  // Currency name, e.g., BTC, ETH
        String keyId = callbackParts[2];         // keyId from callback data, e.g., 12345

        // Retrieve the Key from the database using the extracted keyId
        Key key = keyService.getKeyById(keyId);  // Use keyId to fetch the Key entity

        if (key == null) {
            // Handle the case where the key wasn't found (optional)
            return;
        }

        // Process the toggle action for the selected currency
        Key.CryptoCurrency currency = Key.CryptoCurrency.valueOf(currencyName);
        toggleCurrency(key, currency);  // Toggle the currency setting for the user

        // Update the keyboard after the change
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        editMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMarkup.setReplyMarkup(currencySettingsKeyboard.getCurrencySettingsKeyboard(key));

        // Send the updated keyboard
        messageService.updateKeyboard(editMarkup);
    }

    /**
     * Toggle the currency in the user's settings and save changes to the database.
     *
     * @param key      The current user's settings
     * @param currency The currency to toggle on/off
     */
    private void toggleCurrency(Key key, Key.CryptoCurrency currency) {
        Set<Key.CryptoCurrency> currencies = key.getCurrencies();

        // Toggle the currency: remove it if already selected, add it if not
        if (currencies.contains(currency)) {
            currencies.remove(currency);
        } else {
            currencies.add(currency);
        }

        // Save the updated key to the database
        keyService.saveKey(key);  // Save the updated key entity to the database
    }
}
