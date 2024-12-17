package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.services.KeyService;
import ru.scrait.seedx.services.MessageService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class SetDateCommand extends Command {

    private final KeyService keyService;

    public SetDateCommand(KeyService keyService) {
        super("/setdate", "Изменить дату окончания подписки.");
        this.keyService = keyService;
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        // Extract the message text from the update and remove the command part
        String messageText = update.getMessage().getText().trim();
        String[] messageParts = messageText.split(" ");  // Split the command, keyId, and date

        if (messageParts.length < 3) {
            // If there is no key ID or date in the command, inform the user
            messageService.sendMessage(update.getMessage().getChatId(),
                "Please provide a key ID and a date in the format YYYY-MM-DD. Example: /setdate <keyId> 2024-12-31");
            return;
        }

        String keyId = messageParts[1];  // Extract the key ID from the message
        String dateString = messageParts[2];  // Extract the date from the message

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Parse the date from the string
            LocalDate newExpirationDate = LocalDate.parse(dateString, formatter);

            // Retrieve the user's Key object by keyId using the KeyService
            Key key = keyService.getKeyById(keyId);

            if (key == null) {
                // If the key is not found, send an error message
                messageService.sendMessage(update.getMessage().getChatId(), "Invalid key ID. Please make sure the key is correct.");
            } else {
                // Update the expiration date and save it to the database
                key.setSubscriptionExpirationDate(newExpirationDate);
                keyService.saveKey(key);

                // Confirm the change
                messageService.sendMessage(update.getMessage().getChatId(),
                    "The subscription expiration date for key " + keyId + " has been updated to: " + newExpirationDate);
            }

        } catch (DateTimeParseException e) {
            // If the date format is invalid, send an error message
            messageService.sendMessage(update.getMessage().getChatId(),
                "Invalid date format. Please use the format YYYY-MM-DD. Example: /setdate <keyId> 2024-12-31");
        }
    }
}
