package uz.pdp.simpletelegrambot.payload;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class InLineKeyboards {

    public InlineKeyboardButton button(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);
        return button;
    }

    public List<InlineKeyboardButton> row(InlineKeyboardButton... buttons) {
        return new LinkedList<>(Arrays.asList(buttons));
    }

    public List<List<InlineKeyboardButton>> rowList(List<InlineKeyboardButton>... rows) {
        return new LinkedList<>(Arrays.asList(rows));
    }

    public InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup menuButton() {
        InlineKeyboardButton button = button("Go to Menu", "menu");
        List<InlineKeyboardButton> row = row(button);
        List<List<InlineKeyboardButton>> lists = rowList(row);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        return keyboard;
    }

    public void menuListButtonsForUser(SendMessage sendMessage) {
        sendMessage.setText("Menu bo'limi");
        sendMessage.setParseMode("HTML");

        InlineKeyboardButton button = button("\uD83D\uDCC3 Bo'limlar", "categories");
        InlineKeyboardButton button2 = button("\uD83D\uDED2 Savat", "orders");
        InlineKeyboardButton button3 = button("☎ Admin bilan aloqa", "admin");

        List<InlineKeyboardButton> row = row(button, button2);
        List<InlineKeyboardButton> row2 = row(button3);

        List<List<InlineKeyboardButton>> lists = rowList(row, row2);
        InlineKeyboardMarkup keyboard = keyboard(lists);

        sendMessage.setReplyMarkup(keyboard);
    }

    public void menuListButtonsForAdmin(SendMessage sendMessage) {
        sendMessage.setText("Menu bo'limi");
        sendMessage.setParseMode("HTML");

        InlineKeyboardButton button = button("\uD83D\uDCC3 Bo'limlar", "categories");
        InlineKeyboardButton button2 = button("\uD83D\uDD8A Mahsulot qo'shish", "add_product");
//        InlineKeyboardButton button3 = button("Delete Product", "delete_product");
//        InlineKeyboardButton button4 = button("Update Product", "update_product");

        List<InlineKeyboardButton> row = row(button, button2);
//        List<InlineKeyboardButton> row2 = row(button3, button4);

        List<List<InlineKeyboardButton>> lists = rowList(row);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        sendMessage.setReplyMarkup(keyboard);
    }

    public void categoryListButtonsForAdmin(SendMessage sendMessage) {
        sendMessage.setText("Bo'limlar");
        sendMessage.setParseMode("HTML");

        InlineKeyboardButton button = button("\uD83D\uDC55 Hoodies", "product_list_Hoodies");
        InlineKeyboardButton button2 = button("\uD83D\uDCD3 Blaknot", "product_list_Blaknot");
        InlineKeyboardButton button3 = button("\uD83D\uDD8A Ruchka", "product_list_Ruchka");
        InlineKeyboardButton button4 = button("\uD83E\uDDE3 Sharf", "product_list_Sharf");
        InlineKeyboardButton button5 = button("\uD83D\uDCF1 Powerpank", "product_list_Powerbank");
        InlineKeyboardButton button6 = button("\uD83D\uDCBC Sumka", "product_list_Sumka");

        List<InlineKeyboardButton> row = row(button, button2, button3);
        List<InlineKeyboardButton> row2 = row(button4, button5, button6);

        List<List<InlineKeyboardButton>> lists = rowList(row, row2);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        sendMessage.setReplyMarkup(keyboard);
    }

    public void categoryListButtonsForUser(SendMessage sendMessage) {
        sendMessage.setText("Bo'limlar");
        sendMessage.setParseMode("HTML");

        InlineKeyboardButton button = button("\uD83D\uDC55 Hoodies", "product_list_Hoodies");
        InlineKeyboardButton button2 = button("\uD83D\uDCD3 Blaknot", "product_list_Blaknot");
        InlineKeyboardButton button3 = button("\uD83D\uDD8A Ruchka", "product_list_Ruchka");
        InlineKeyboardButton button4 = button("\uD83E\uDDE3 Sharf", "product_list_Sharf");
        InlineKeyboardButton button5 = button("\uD83D\uDCF1 Powerpank", "product_list_Powerbank");
        InlineKeyboardButton button6 = button("\uD83D\uDCBC Sumka", "product_list_Sumka");
        InlineKeyboardButton button7 = button("Basket", "orders");

        List<InlineKeyboardButton> row = row(button, button2, button3);
        List<InlineKeyboardButton> row2 = row(button4, button5, button6);
        List<InlineKeyboardButton> row3 = row(button7);

        List<List<InlineKeyboardButton>> lists = rowList(row, row2,row3);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        sendMessage.setReplyMarkup(keyboard);
    }

    public InlineKeyboardMarkup basketKeyboard(Integer id) {

        InlineKeyboardButton button = button("\uD83D\uDED2 Buyurtma berish", "buyurtma+" + id.toString());


        List<InlineKeyboardButton> row = row(button);

        List<List<InlineKeyboardButton>> lists = rowList(row);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        return keyboard;
    }

    public InlineKeyboardMarkup buyurtmalar() {

        InlineKeyboardButton button = button("\uD83D\uDDD1 Buyurtmalar", "orders");


        List<InlineKeyboardButton> row = row(button);

        List<List<InlineKeyboardButton>> lists = rowList(row);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        return keyboard;
    }

    public InlineKeyboardMarkup contactWithAdminandDeleteButton(Integer pId, Integer bId) {

        InlineKeyboardButton button = button(" ☎ Admin bilan bog'lanish", "admin");
        InlineKeyboardButton button2 = button(" ❌ Buyurtmani bekor qilish", "bekor_qilish"+pId.toString()+bId.toString());


        List<InlineKeyboardButton> row = row(button);
        List<InlineKeyboardButton> row2 = row(button2);

        List<List<InlineKeyboardButton>> lists = rowList(row,row2);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        return keyboard;
    }

    public InlineKeyboardMarkup productListKeyboardFromAdmin(Integer id) {
        InlineKeyboardButton button1 = button("⚙ Mahsulotni o'zgartirish", "_update_product+" + id);
        InlineKeyboardButton button = button("❌ Mahsulotni o'chirish", "_delete_product+" + id);

        List<InlineKeyboardButton> row = row(button1);
        List<InlineKeyboardButton> row2 = row(button);

        List<List<InlineKeyboardButton>> lists = rowList(row, row2);
        InlineKeyboardMarkup keyboard = keyboard(lists);
        return keyboard;
    }
//    public InlineKeyboardMarkup productListKeyboardFromAdmin() {
//        InlineKeyboardButton button = button("Delete Product", "_delete_product");
//        InlineKeyboardButton button1 = button("Update Product", "_update_product");
//
//        List<InlineKeyboardButton> row = row(button,button1);
//
//        List<List<InlineKeyboardButton>> lists = rowList(row);
//        InlineKeyboardMarkup keyboard = keyboard(lists);
//        return keyboard;
//    }
}
