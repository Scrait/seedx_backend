package ru.scrait.seedx.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.services.MessageService;

@Component
public class StartCommand extends Command {

    public StartCommand() {
        super("/start", "Информация как пользоваться ботом.");
    }

    @Override
    public void execute(Update update, MessageService messageService) {
        messageService.sendMessage(update.getMessage().getChatId(),
                                   "Добро пожаловать в бота!\n" +
                                           "\n" +
                                           "Вот несколько команд, которые помогут вам настроить ваш аккаунт:\n" +
                                           "\n" +
                                           "1. `/coins <keyId>` - Показать или изменить валютные настройки вашего ключа.\n" +
                                           "   Пример использования: /coins 2bae4db60fdb667f97760806298a8279\n" +
                                           "\n" +
                                           "2. `/setdate <keyId> <date>` - Установить дату окончания подписки для ключа. Укажите ключ и дату в формате YYYY-MM-DD.\n" +
                                           "   Пример использования: /setdate 2bae4db60fdb667f97760806298a8279 2024-12-31\n" +
                                           "\n" +
                                           "3. `/setaddress <address>` - Установить ваш криптовалютный адрес. Укажите адрес, который вы хотите использовать.\n" +
                                           "   Пример использования: /setaddress 1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa\n");
    }

}