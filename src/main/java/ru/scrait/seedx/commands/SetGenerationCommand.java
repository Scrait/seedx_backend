package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.controllers.KeyWebSocketHandler;
import ru.scrait.seedx.models.Key;
import ru.scrait.seedx.services.KeyService;
import ru.scrait.seedx.services.MessageService;

@Component
public class SetGenerationCommand extends Command {

    private final KeyService keyService;
    private final KeyWebSocketHandler webSocketHandler;

    public SetGenerationCommand(KeyService keyService, KeyWebSocketHandler webSocketHandler) {
        super("/setgeneration", "Изменить скорость генерации (чем больше, тем медленнее).");
        this.keyService = keyService;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        String messageText = update.getMessage().getText().trim();
        String[] messageParts = messageText.split(" ");

        if (messageParts.length < 3) {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Использование: /setgeneration <keyId> <speed>\nПример: /setgeneration 2bae4d 400");
            return;
        }

        String keyId = messageParts[1];
        int speed;

        try {
            speed = Integer.parseInt(messageParts[2]);
            if (speed < 1) {
                messageService.sendMessage(update.getMessage().getChatId(),
                        "Скорость должна быть положительным числом больше 0.");
                return;
            }
        } catch (NumberFormatException e) {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Неверный формат скорости. Введите целое число, например: /setgeneration 2bae4d 400");
            return;
        }

        Key key = keyService.getKeyById(keyId);
        if (key == null) {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Неверный keyId. Проверьте и попробуйте снова.");
            return;
        }

        key.setSpeed(speed);
        keyService.saveKey(key);
        webSocketHandler.broadcastUpdatedSpeed(key);

        messageService.sendMessage(update.getMessage().getChatId(),
                "Скорость генерации для ключа " + keyId + " обновлена до " + speed + ".");
    }
}
