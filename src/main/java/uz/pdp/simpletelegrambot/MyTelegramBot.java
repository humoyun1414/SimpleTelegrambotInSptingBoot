package uz.pdp.simpletelegrambot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.simpletelegrambot.controller.MainController;
import uz.pdp.simpletelegrambot.controller.ProductController;
import uz.pdp.simpletelegrambot.entity.UserEntity;
import uz.pdp.simpletelegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class MyTelegramBot extends TelegramLongPollingBot {
    @Lazy
    @Autowired
    private MainController mainController;
    @Lazy
    @Autowired
    private ProductController productController;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return "@pdp_Walmartbot";
    }

    @Override
    public String getBotToken() {
        return "5700891541:AAFqiaDFJ0npaRclUt0Hulgcj57j7v9Y4Ns";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
//        System.out.println(update.getMessage().getFrom());
        try {
            if (update.hasMessage()) {
                User user = update.getMessage().getFrom();
                UserEntity entity = registerUser(user);
                Message message = update.getMessage();
                if (update.getMessage().hasText()) {
                    mainController.handleText(entity, message);
                } else if (message.hasPhoto()) {
                    productController.handleText(entity, message);
                }
            } else if (update.hasCallbackQuery()) {
                Message message = update.getCallbackQuery().getMessage();
                User user = update.getCallbackQuery().getFrom();
                UserEntity entity = registerUser(user);
                String text = update.getCallbackQuery().getData();
                mainController.handleCallBack(entity, message, text);

            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    public UserEntity registerUser(User user) {
        Optional<UserEntity> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) return optionalUser.get();
        UserEntity entity = new UserEntity();
        entity.setChatId(user.getId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        userRepository.save(entity);
        return entity;
    }

    public void sdm(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TypeNotPresentException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sdm(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TypeNotPresentException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sdm(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TypeNotPresentException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void log(UserEntity user, String text) {
        String str = String.format(LocalDateTime.now() + ",  userId: %d, firstName: %s, lastName: %s, text: %s",
                user.getChatId(), user.getFirstName(), user.getLastName(), text);
        System.out.println(str);
    }
}
