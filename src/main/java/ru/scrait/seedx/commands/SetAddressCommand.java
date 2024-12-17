package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.components.AddressComponent;
import ru.scrait.seedx.services.MessageService;

@Component
public class SetAddressCommand extends Command {

    private final AddressComponent addressComponent;

    public SetAddressCommand(AddressComponent addressComponent) {
        super("/setaddress", "Установить крипто-адрес.");
        this.addressComponent = addressComponent;
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        // Извлекаем текст сообщения и делим его на части
        String messageText = update.getMessage().getText().trim();
        String[] messageParts = messageText.split(" ");  // Разделяем команду и адрес

        if (messageParts.length < 2) {
            // Если не указаны адрес, информируем пользователя
            messageService.sendMessage(update.getMessage().getChatId(),
                "Please provide a crypto address. Example: /setaddress <cryptoAddress>");
            return;
        }

        String cryptoAddress = messageParts[1];  // Извлекаем крипто-адрес

        // Устанавливаем новый адрес
        addressComponent.setAddress(cryptoAddress);

        // Подтверждаем обновление
        messageService.sendMessage(update.getMessage().getChatId(),
            "Your crypto address has been updated to: " + cryptoAddress);
    }
}
