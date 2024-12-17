package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.scrait.seedx.keyboards.CurrencySettingsKeyboard;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.services.KeyService;
import ru.scrait.seedx.services.MessageService;

@Component
public class CoinsCommand extends Command {

    private final KeyService keyService;
    private final CurrencySettingsKeyboard currencySettingsKeyboard;

    public CoinsCommand(KeyService keyService, CurrencySettingsKeyboard currencySettingsKeyboard) {
        super("/coins", "Изменение бч ключа.");
        this.keyService = keyService;
        this.currencySettingsKeyboard = currencySettingsKeyboard;
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        // Extract the message text from the update and remove the command part
        String messageText = update.getMessage().getText().trim();
        String[] messageParts = messageText.split(" ");  // Split the command and key ID

        if (messageParts.length < 2) {
            // If there is no key ID in the command, inform the user
            messageService.sendMessage(update.getMessage().getChatId(), "Please provide a key ID. Example: /coins <keyId>");
            return;
        }

        String keyId = messageParts[1];  // Extract the key ID from the message

        // Fetch key by ID using the KeyService
        Key key = keyService.getKeyById(keyId);

        if (key == null) {
            // If the key is not found, send an error message
            messageService.sendMessage(update.getMessage().getChatId(), "Invalid key. Please make sure the key is correct.");
        } else {
            // If the key is found, generate the keyboard for the user
            InlineKeyboardMarkup keyboard = currencySettingsKeyboard.getCurrencySettingsKeyboard(key);

            // Send a message with the generated keyboard
            messageService.sendMessageWithKeyboard(update.getMessage().getChatId(), "Here are your currency settings:", keyboard);
        }
    }
}
