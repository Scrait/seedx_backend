package ru.scrait.seedx.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final TelegramLongPollingBot bot;

    public MessageService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        sendMessage(message);
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(String.valueOf(chatId), text);
    }

    public void sendMessage(SendMessage message) {
        try {
            bot.execute(message); // Метод отправки сообщения
        } catch (TelegramApiException e) {
            e.fillInStackTrace(); // Обработка исключений при отправке
        }
    }

    public void updateKeyboard(EditMessageReplyMarkup editMessageReplyMarkup) {
        try {
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.fillInStackTrace();
        }
    }

    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId));
        deleteMessage.setMessageId(messageId);

        deleteMessage(deleteMessage);
    }

    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
            bot.execute(deleteMessage);  // Этот метод отправляет команду боту на удаление сообщения
        } catch (TelegramApiException e) {
            e.fillInStackTrace();  // Обработка ошибок
        }
    }

    public void sendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(keyboard);  // Attach the keyboard

        // Validate keyboard before sending
        if (keyboard == null || keyboard.getKeyboard().isEmpty()) {
            throw new IllegalArgumentException("Keyboard is empty or invalid.");
        }

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
