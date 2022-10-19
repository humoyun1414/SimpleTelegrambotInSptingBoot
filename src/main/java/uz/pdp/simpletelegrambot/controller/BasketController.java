package uz.pdp.simpletelegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.simpletelegrambot.MyTelegramBot;
import uz.pdp.simpletelegrambot.entity.BasketEntity;
import uz.pdp.simpletelegrambot.entity.ProductEntity;
import uz.pdp.simpletelegrambot.entity.UserEntity;
import uz.pdp.simpletelegrambot.payload.InLineKeyboards;
import uz.pdp.simpletelegrambot.repository.BasketRepository;
import uz.pdp.simpletelegrambot.repository.ProductRepository;
import uz.pdp.simpletelegrambot.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class BasketController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InLineKeyboards inLineKeyboards;
    @Autowired
    private MyTelegramBot telegramBot;

    public void handleCallBack(UserEntity entity, Message message, String text) {
        try {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(entity.getChatId());
            if (text.startsWith("buyurtma")) {
                String Id = text.substring(text.length() - 1);
                int id = Integer.parseInt(Id);
                Optional<ProductEntity> entityOptional = productRepository.findById(id);
                if (entityOptional.isPresent()) {
                    ProductEntity product = entityOptional.get();
                    if (entity.getBasket() == null) {
                        BasketEntity basket = new BasketEntity();
                        basket.setProducts(Arrays.asList(product));
                        basket.setUserChatId(entity.getChatId());
                        BasketEntity saveBasket = basketRepository.save(basket);
//                        product.setBasket(Arrays.asList(saveBasket));
                        entity.setBasket(saveBasket);
//                        productRepository.save(product);
                    } else {
                        BasketEntity basket = entity.getBasket();

                        List<ProductEntity> products = basket.getProducts();
                        products.add(product);
                        basket.setProducts(products);
//                        product.setBasket(Arrays.asList(basket));
//                        productRepository.save(product);
                        basketRepository.save(basket);
                    }
                }
//                userRepository.save(entity);
                sendMessage.setText("Buyurtma savatchaga joylandi ! ");
                sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
                telegramBot.sdm(sendMessage);
            }
//            else if (text.startsWith("orders")) {
//                if (entity.getBasket() == null || entity.getBasket().getProducts().isEmpty()) {
//                    sendMessage.setText("Sizda hozrda buyurtma mavjud emas !");
//                    sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
//                    telegramBot.sdm(sendMessage);
//                } else {
//                    List<ProductEntity> productList = productRepository.findAll();
//
//                    for (ProductEntity product : productList) {
//                        if (product.getBasket() == null) {
//                            continue;
//                        } else {
//                            if (product.getBasket().equals(entity.getBasket().getId())) {
//                                StringBuilder builder = new StringBuilder();
//                                SendPhoto sendPhoto = new SendPhoto();
//                                sendPhoto.setChatId(String.valueOf(entity.getChatId()));
//
//                                sendPhoto.setPhoto(new InputFile(product.getPhoto().getFileId()));
//
//                                builder.append("Nomi: ");
//                                builder.append(product.getName());
//                                builder.append("\n");
//                                builder.append("Narxi: ");
//                                builder.append(product.getPrice());
//                                builder.append("\n");
//                                builder.append("Status: ");
//                                builder.append(product.getStatus());
//                                builder.append("\n");
//                                builder.append("Created: ");
//                                builder.append(product.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                                builder.append("\n-------------------------------\n");
//
//                                SendMessage editMessageText = new SendMessage();
//                                editMessageText.setChatId(message.getChatId().toString());
//                                editMessageText.setText(builder.toString());
//
//                                telegramBot.sdm(sendPhoto);
//
//                                editMessageText.setReplyMarkup(inLineKeyboards.contactWithAdminandDeleteButton(product.getId(), entity.getBasket().getId()));
//                                telegramBot.sdm(editMessageText);
//
//                            }
//                        }
//                    }
//                }
//            }

            else if (text.startsWith("orders")) {
                List<BasketEntity> basketlist = basketRepository.findAll();
                if (entity.getBasket() == null || entity.getBasket().getProducts().isEmpty()) {
                    sendMessage.setText("Hozirda sizda buyurtmalar mavjud emas !!");
                    sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
                    telegramBot.sdm(sendMessage);
                }
                for (BasketEntity basket : basketlist) {

                    for (ProductEntity product : basket.getProducts()) {
                        if (basket.getUserChatId().equals(entity.getChatId())) {

                            StringBuilder builder = new StringBuilder();
                            SendPhoto sendPhoto = new SendPhoto();
                            sendPhoto.setChatId(String.valueOf(entity.getChatId()));

                            sendPhoto.setPhoto(new InputFile(product.getPhoto().getFileId()));

                            builder.append("Nomi: ");
                            builder.append(product.getName());
                            builder.append("\n");
                            builder.append("Narxi: ");
                            builder.append(product.getPrice());
                            builder.append("\n");
                            builder.append("Status: ");
                            builder.append(product.getStatus());
                            builder.append("\n");
                            builder.append("Created: ");
                            builder.append(product.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            builder.append("\n-------------------------------\n");

                            SendMessage editMessageText = new SendMessage();
                            editMessageText.setChatId(message.getChatId().toString());
                            editMessageText.setText(builder.toString());

                            telegramBot.sdm(sendPhoto);

                            editMessageText.setReplyMarkup(inLineKeyboards.contactWithAdminandDeleteButton(product.getId(), basket.getId()));
                            telegramBot.sdm(editMessageText);
                        }
//                        else if (entity.getBasket() == null) {
//                            sendMessage.setText("Sizda hozrda buyurtma mavjud emas !");
//                            sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
//                            telegramBot.sdm(sendMessage);
//                        }
                    }
                }
            } else if (text.startsWith("bekor_qilish")) {
                String pId = text.substring(12, 13);
                int pid = Integer.parseInt(pId);

                String bId = text.substring(13);
                Integer bid = Integer.valueOf(bId);

//                Optional<ProductEntity> optionalProduct = productRepository.findById(pid);
                basketRepository.deleteByIdNative(pid);
                sendMessage.setChatId(entity.getChatId());
                sendMessage.setText("Buyurtma bekor qilindi !");
                sendMessage.setReplyMarkup(inLineKeyboards.buyurtmalar());
                telegramBot.sdm(sendMessage);


//                if (optionalProduct.isPresent()) {
//                    ProductEntity product = optionalProduct.get();
//                    product.setBasket(null);
//                    sendMessage.setChatId(entity.getChatId());
//                    sendMessage.setText("Buyurtma bekor qilindi !");
//                    productRepository.save(product);
//                    sendMessage.setReplyMarkup(inLineKeyboards.buyurtmalar());
//                    telegramBot.sdm(sendMessage);
//                }
//                List<Integer> basketIdList = basketRepository.findAllByIdNative(pid);
//                for (Integer integer : basketIdList) {
//                    if (Objects.equals(integer, bid)) {
//                        basketRepository.deleteById(bid);
//                        sendMessage.setChatId(entity.getChatId());
//                        sendMessage.setText("Buyurtma bekor qilindi !");
//                        sendMessage.setReplyMarkup(inLineKeyboards.buyurtmalar());
//                        telegramBot.sdm(sendMessage);
//                    }
//
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public void contactAdmin(UserEntity entity, Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(entity.getChatId());
        sendMessage.setText("Admin : https://t.me/humoyun5656\nNomer : 9999999");
        sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
        telegramBot.sdm(sendMessage);
    }
}
