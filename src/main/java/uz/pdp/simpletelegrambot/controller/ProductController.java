package uz.pdp.simpletelegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.pdp.simpletelegrambot.MyTelegramBot;
import uz.pdp.simpletelegrambot.entity.PhotoEntity;
import uz.pdp.simpletelegrambot.entity.ProductEntity;
import uz.pdp.simpletelegrambot.entity.UserEntity;
import uz.pdp.simpletelegrambot.enums.ProductStatus;
import uz.pdp.simpletelegrambot.payload.Components;
import uz.pdp.simpletelegrambot.payload.InLineKeyboards;
import uz.pdp.simpletelegrambot.repository.PhotoRepository;
import uz.pdp.simpletelegrambot.repository.ProductRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    boolean result = false;
    @Autowired
    private MyTelegramBot telegramBot;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private InLineKeyboards inLineKeyboards;


    public void handleCallBack(UserEntity entity, Message message, String text) {
        try {
            if (text.equals("add_product")) {

                SendMessage sendMessage = new SendMessage();
                ProductEntity product = new ProductEntity();
                Long chatId = entity.getChatId();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Mahsulotning nomini kiriting :");
                Components.PRODUCT_ENTITY_MAP.put(String.valueOf(entity.getChatId()), product);
                Components.PRODUCT_STATUS_MAP.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_NAME);
                telegramBot.sdm(sendMessage);
            } else if (text.startsWith("_update")) {
                String Id = text.substring(text.length() - 1);
                int id = Integer.parseInt(Id);
                SendMessage sendMessage = new SendMessage();
                Optional<ProductEntity> optionalProduct = productRepository.findById(id);
                if (!optionalProduct.isPresent()) {
                    throw new RuntimeException("Mahsulot topilmadi !");
                }
                ProductEntity product = optionalProduct.get();
                Long chatId = entity.getChatId();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Mahsulotning yangi nomini kiriting :");
                Components.PRODUCT_ENTITY_MAP_UPDATE.put(String.valueOf(entity.getChatId()), product);
                Components.PRODUCT_STATUS_MAP_UPDATE.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_NAME_UPDATE);
                telegramBot.sdm(sendMessage);

            } else if (text.startsWith("_delete")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(entity.getChatId());

                String Id = text.substring(text.length() - 1);
                int id = Integer.parseInt(Id);
                Optional<ProductEntity> entityOptional = productRepository.findById(id);
                if (entityOptional.isPresent()) {
                    entityOptional.get().setStatus("NOT_ACTIVE");
                    productRepository.save(entityOptional.get());
                }
                sendMessage.setText("Mahsulotlar omboridan muvoffaqiyatli o'chirildi !");
                sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
                telegramBot.sdm(sendMessage);
            } else if (text.startsWith("product_list_")) {
                String category = text.substring(13);
                Iterable<ProductEntity> all = productRepository.findAll();
                if (!all.iterator().hasNext()) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(entity.getChatId());
                    sendMessage.setText("Maxsulotlar hali yuklangani yo'q ! ");
                    sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
                    telegramBot.sdm(sendMessage);
                }

                for (ProductEntity product : productRepository.findAll()) {

                    if (entity.getChatId().toString().equals(Components.ADMIN) && category.equals(product.getCategory())) {
                        StringBuilder builder = new StringBuilder();
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(String.valueOf(entity.getChatId()));

                        sendPhoto.setPhoto(new InputFile(product.getPhoto().getFileId()));

                        builder.append("Id: ");
                        builder.append(product.getId());
                        builder.append("\n");
                        builder.append("Nomi: ");
                        builder.append(product.getName());
                        builder.append("\n");
                        builder.append("Narxi: ");
                        builder.append(product.getPrice());
                        builder.append("\n");
                        builder.append("Bo'limi: ");
                        builder.append(product.getCategory());
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

//                editMessageText.setMessageId(message.getMessageId());
//                sendPhoto.setCaption(builder.toString());
                        telegramBot.sdm(sendPhoto);
                        if (entity.getChatId().toString().equals(Components.ADMIN))
                            editMessageText.setReplyMarkup(inLineKeyboards.productListKeyboardFromAdmin(product.getId()));
                        else editMessageText.setReplyMarkup(inLineKeyboards.basketKeyboard(product.getId()));
                        telegramBot.sdm(editMessageText);
                    } else if (product.getStatus().equals("ACTIVE")
                            && !entity.getChatId().toString().equals(Components.ADMIN)
                            && category.equals(product.getCategory())) {
                        result = true;
                        StringBuilder builder = new StringBuilder();
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(String.valueOf(entity.getChatId()));

                        sendPhoto.setPhoto(new InputFile(product.getPhoto().getFileId()));

//                    builder.append("Id: ");
//                    builder.append(product.getId());
//                    builder.append("\n");
                        builder.append("Nomi: ");
                        builder.append(product.getName());
                        builder.append("\n");
                        builder.append("Narxi: ");
                        builder.append(product.getPrice());
                        builder.append("\n");
                        builder.append("Bo'limi: ");
                        builder.append(product.getCategory());
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

//                editMessageText.setMessageId(message.getMessageId());
//                sendPhoto.setCaption(builder.toString());
                        telegramBot.sdm(sendPhoto);
                        if (entity.getChatId().toString().equals(Components.ADMIN))
                            editMessageText.setReplyMarkup(inLineKeyboards.productListKeyboardFromAdmin(product.getId()));
                        else editMessageText.setReplyMarkup(inLineKeyboards.basketKeyboard(product.getId()));
                        telegramBot.sdm(editMessageText);
                    }
//                else {
//                    SendMessage sendMessage = new SendMessage();
//                    sendMessage.setChatId(entity.getChatId());
//                    sendMessage.setText("Ushbu bo'limga mahsulotlarimiz hali yuklangani yo'q");
//                    sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
//                    telegramBot.sdm(sendMessage);
//                }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void handleText(UserEntity entity, Message message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            String text = message.getText();

            ProductEntity product = Components.PRODUCT_ENTITY_MAP.get(entity.getChatId().toString());
            ProductStatus productStatus = Components.PRODUCT_STATUS_MAP.get(String.valueOf(entity.getChatId()));


            if (productStatus.equals(ProductStatus.PRODUCT_NAME)) {
                product.setName(text);
                sendMessage.setText("Nomi: " + text
                        + "\nNarxini kiriting: ");
                Components.PRODUCT_STATUS_MAP.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_PRICE);
                telegramBot.sdm(sendMessage);
            } else if (productStatus.equals(ProductStatus.PRODUCT_PRICE)) {
                product.setPrice(text);
                sendMessage.setText("<b>Mahsulot</b> : \n" +
                        "Nomi: " + product.getName()
                        + "\nNarxi: " + product.getPrice() +
                        "\nMahsulotning bo'limini kiriting! \nMisol uchun: Hoodies , Blaknot\nRuchka , Sharf\nPowerbank , Sumka : "
                );
                sendMessage.setParseMode("HTML");
                product.setStatus("ACTIVE");
                Components.PRODUCT_STATUS_MAP.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_CATEGORY);
                telegramBot.sdm(sendMessage);
            } else if (productStatus.equals(ProductStatus.PRODUCT_CATEGORY)) {
                product.setCategory(text);
                sendMessage.setText("<b>Mahsulot</b> : \n" +
                        "Nomi: " + product.getName()
                        + "\nNarxi: " + product.getPrice() +
                        "\nBo'limi: " + product.getCategory() +
                        "\nMahsulot rasmini yuboring: "
                );
                sendMessage.setParseMode("HTML");
                product.setStatus("ACTIVE");
                Components.PRODUCT_STATUS_MAP.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_PHOTO);
                telegramBot.sdm(sendMessage);
            } else if (productStatus.equals(ProductStatus.PRODUCT_PHOTO)) {
                log_photo(entity, message.getPhoto());
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(Components.ADMIN);
                sendPhoto.setPhoto(new InputFile(message.getPhoto().get(message.getPhoto().size() - 1).getFileId()));

                PhotoEntity photo = new PhotoEntity();
//                byte[] bytes = sendPhoto.getPhoto().getAttachName().getBytes(StandardCharsets.UTF_8);
                String attachName = sendPhoto.getPhoto().getAttachName();

//                photo.setContent(bytes);
                photo.setFileId(attachName);
                PhotoEntity savedPhoto = photoRepository.save(photo);
                product.setPhoto(savedPhoto);
                productRepository.save(product);
                sendPhoto.setCaption(String.valueOf(product.toString()));
//                photo.setContent(sendPhoto.getPhoto().getAttachName().getBytes());
                Components.PRODUCT_ENTITY_MAP.clear();
                Components.PRODUCT_STATUS_MAP.clear();
                telegramBot.sdm(sendPhoto);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void handleTextUpdate(UserEntity entity, Message message) {
        try {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            String text = message.getText();


            ProductEntity productUpdate = Components.PRODUCT_ENTITY_MAP_UPDATE.get(entity.getChatId().toString());
            ProductStatus productStatusUpdate = Components.PRODUCT_STATUS_MAP_UPDATE.get(String.valueOf(entity.getChatId()));

            //////////////////////////////////


            if (productStatusUpdate.equals(ProductStatus.PRODUCT_NAME_UPDATE)) {
                productUpdate.setName(text);
                sendMessage.setText("Name: " + text
                        + "\nMahsulotning yangi narxini kiriting : ");
                Components.PRODUCT_STATUS_MAP_UPDATE.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_PRICE_UPDATE);
                telegramBot.sdm(sendMessage);
            } else if (productStatusUpdate.equals(ProductStatus.PRODUCT_PRICE_UPDATE)) {
                productUpdate.setPrice(text);
                sendMessage.setText("<b>Mahsulot</b> : \n" +
                        "Nomi: " + productUpdate.getName()
                        + "\nNarxi: " + productUpdate.getPrice() +
                        "\nStatusini kiriting: "
                );
                sendMessage.setParseMode("HTML");
//            productUpdate.setStatus("ACTIVE");
                Components.PRODUCT_STATUS_MAP_UPDATE.put(String.valueOf(entity.getChatId()), ProductStatus.PRODUCT_STATUS_UPDATE);
                telegramBot.sdm(sendMessage);
            } else if (productStatusUpdate.equals(ProductStatus.PRODUCT_STATUS_UPDATE)) {
                productUpdate.setStatus(text);
                sendMessage.setText("<b>Mahsulot</b> : \n" +
                        "Nomi: " + productUpdate.getName()
                        + "\nNarxi: " + productUpdate.getPrice() +
                        "\nBo'limi: " + productUpdate.getCategory() +
                        "\nStatus: " + productUpdate.getStatus() +
                        "\nMahsulotning o'zgartitildi ! "
                );
                sendMessage.setParseMode("HTML");
//            productUpdate.setStatus("ACTIVE");
                productRepository.save(productUpdate);
                Components.PRODUCT_ENTITY_MAP_UPDATE.clear();
                Components.PRODUCT_STATUS_MAP_UPDATE.clear();
                sendMessage.setReplyMarkup(inLineKeyboards.menuButton());
                telegramBot.sdm(sendMessage);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void log_photo(UserEntity user, List<PhotoSize> photoList) {
        String str = String.format(LocalDateTime.now() + ",  userId: %d, firstName: %s, lastName: %s",
                user.getChatId(), user.getFirstName(), user.getLastName());
        System.out.println(str);
        for (PhotoSize photo : photoList) {
            System.out.println(photo.getWidth() + " " +
                    photo.getHeight() +
                    " FileId: " + photo.getFileId());
        }
    }
}
