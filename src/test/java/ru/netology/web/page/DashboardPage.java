package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement heading1 = $(byText("Ваши карты"));
    private SelenideElement heading2 = $(byText("Пополнение карты"));
    private SelenideElement buttonTransfer1 = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button__text");
    private SelenideElement buttonTransfer2 = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button__text");
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement fromCard = $("[data-test-id=from] input");
    private SelenideElement toCard = $("[data-test-id=to]");
    private SelenideElement buttonPay = $("[data-test-id=action-transfer]");


    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public int getCardBalance(int id) {
        val text = cards.get(id).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }


    public DashboardPage validChoosePay1() {
        buttonTransfer1.click();
        return new DashboardPage();
    }

    public DashboardPage validChoosePay2() {
        buttonTransfer2.click();
        return new DashboardPage();
    }

    public void checkHeadingYourCards() {   //проверяем видимость заголовка "Ваши карты"
        heading1.shouldBe(Condition.visible);
    }

    public void checkHeadingPaymentCards() {   //проверяем видимость заголовка "Пополнение карты"
        heading2.shouldBe(Condition.visible);
    }

    public void validPayAmount() {   //открывается окно пополнение карты, нужно ввести сумму пополнения
        amount.setValue(DataHelper.getPayment());
    }

    public void validPayExtendAmount(int payment) {   //дополнительный метод для пополнения на сумму большую, чем есть на карте
        amount.setValue(String.valueOf(payment));
    }

    public DashboardPage validPayCard(int id) {   // ввести карту с которой осуществляется оплата и нажать "пополнить"
        fromCard.setValue(DataHelper.getCardInfo(id));
        buttonPay.click();
        return new DashboardPage();
    }

}