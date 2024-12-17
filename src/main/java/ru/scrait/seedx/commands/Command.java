package ru.scrait.seedx.commands;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.scrait.seedx.services.MessageService;

public abstract class Command extends BotCommand implements ICommand {

    public Command(@NonNull String command, @NonNull String description) {
        super(command, description);
    }

    @Override
    public abstract void execute(Update update, MessageService messageService);

}
