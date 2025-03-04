package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.controllers.KeyWebSocketHandler;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.services.KeyService;
import ru.scrait.seedx.services.MessageService;

@Component
public class ClearCommand extends Command {

    private final KeyService keyService;
    private final KeyWebSocketHandler webSocketHandler;

    public ClearCommand(KeyService keyService, KeyWebSocketHandler webSocketHandler) {
        super("/clear", "Очистить найденные фразы.");
        this.keyService = keyService;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        String messageText = update.getMessage().getText().trim();
        String[] messageParts = messageText.split(" ");

        if (messageParts.length < 2) {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Использование: /clear <keyId> \nПример: /clear 2bae4d");
            return;
        }

        String keyId = messageParts[1];

        Key key = keyService.getKeyById(keyId);
        if (key == null) {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Неверный keyId. Проверьте и попробуйте снова.");
            return;
        }

        webSocketHandler.broadcastClearAll(key);

        messageService.sendMessage(update.getMessage().getChatId(),
                "Фразы очищены для ключа " + keyId + ".");
    }
}
