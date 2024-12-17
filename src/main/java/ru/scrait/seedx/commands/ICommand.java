package ru.scrait.seedx.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.services.MessageService;

@FunctionalInterface
public interface ICommand {

    void execute(Update update, MessageService messageService);

}
