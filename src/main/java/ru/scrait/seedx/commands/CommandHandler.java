package ru.scrait.seedx.commands;

import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.services.MessageService;

import java.util.List;

@Component
public class CommandHandler {

    @Getter
    private final List<Command> commands;
    private final MessageService messageService;

    public CommandHandler(@Lazy MessageService messageService, List<Command> commands) {
        this.messageService = messageService;
        this.commands = commands;
    }

    public void handleCommand(Update update) {
        String userMessage = update.getMessage().getText().trim();
        Long userId = update.getMessage().getChatId();

        ICommand command = commands.stream().filter(command1 -> command1.getCommand().equals(userMessage.split(" ")[0])).toList().getFirst();
        if (command != null) {
            command.execute(update, messageService);
        } else {
            // Обработка неизвестных команд
            messageService.sendMessage(userId.toString(), "Неизвестная команда. Напишите /start.");
        }
    }
}
