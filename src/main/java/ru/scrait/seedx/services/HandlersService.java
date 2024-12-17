package ru.scrait.seedx.services;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.scrait.seedx.handlers.IHandler;

import java.util.List;

@Service
public class HandlersService {

    private final List<IHandler> handlers;
    private final KeyService keyService;
    private final MessageService messageService;

    public HandlersService(List<IHandler> handlers, KeyService keyService, @Lazy MessageService messageService) {
        this.handlers = handlers;
        this.keyService = keyService;
        this.messageService = messageService;
    }

    @Transactional
    public void handleKeyboards(Update update) {
        handlers.forEach(iHandler -> iHandler.execute(update, messageService, keyService));
    }

}
