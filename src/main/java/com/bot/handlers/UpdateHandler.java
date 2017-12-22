package com.bot.handlers;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

public interface UpdateHandler {

    void handle(Update update) throws TelegramApiException;

    void loadProperties() throws IOException;
}
