package ru.scrait.seedx.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.services.KeyService;
import ru.scrait.seedx.services.MessageService;

@FunctionalInterface
public interface IHandler {

    void execute(Update update, MessageService messageService, KeyService keyService);

}
