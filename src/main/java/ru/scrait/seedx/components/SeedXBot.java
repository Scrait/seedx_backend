package ru.scrait.seedx.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.commands.CommandHandler;
import ru.scrait.seedx.services.HandlersService;

@Component
public class SeedXBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;
    private final HandlersService handlersService;

    public SeedXBot(CommandHandler commandHandler, HandlersService handlersService) {
        this.commandHandler = commandHandler;
        this.handlersService = handlersService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handlersService.handleKeyboards(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            commandHandler.handleCommand(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}