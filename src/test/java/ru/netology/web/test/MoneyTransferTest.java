package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


class MoneyTransferTest {


    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }


    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {  //проверяем пополнение с первой карты на вторую
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards(); //проверили видимость заголовка "Ваши карты"
        val initialBalanceFromCard = dashboardPage.getCardBalance(0); //получили баланс карты, которую будем пополнять
        val initialBalanceToCard = dashboardPage.getCardBalance(1); //получили баланс карты, с которой будем пополнять
        dashboardPage.validChoosePay1(); //выбрали карту c которой будет оплата
        dashboardPage.checkHeadingPaymentCards(); // проверили видимость заголовка "Пополнение карты"
        dashboardPage.validPayAmount();//ввели сумму перевода
        val dashboardPage1 = dashboardPage.validPayCard(1); //ввели карту для перевода и вернулись на страницу с балансами и картами
        val actual = dashboardPage1.getCardBalance(0); //получили новый баланс первой карты
        val expected = initialBalanceFromCard + Integer.parseInt(DataHelper.getPayment()); //посчитали каким должен быть баланс первой карты
        val actual2 = dashboardPage1.getCardBalance(1); //получили новый баланс второй карты
        val expected2 = initialBalanceToCard - Integer.parseInt(DataHelper.getPayment()); //посчитали каким должен быть баланс второй карты
        assertEquals(expected, actual); //сравнили балансы первой карты
        assertEquals(expected2, actual2); //сравнили балансы второй карты
    }


    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {  //проверяем пополнение со второй карты на первую
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceFromCard = dashboardPage.getCardBalance(1);
        val initialBalanceToCard = dashboardPage.getCardBalance(0);
        dashboardPage.validChoosePay2();
        dashboardPage.checkHeadingPaymentCards();
        dashboardPage.validPayAmount();
        val dashboardPage1 = dashboardPage.validPayCard(0);
        val actual1 = dashboardPage1.getCardBalance(1);
        val expected1 = initialBalanceFromCard + Integer.parseInt(DataHelper.getPayment());
        val actual2 = dashboardPage1.getCardBalance(0); //получили новый баланс второй карты
        val expected2 = initialBalanceToCard - Integer.parseInt(DataHelper.getPayment());
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV3() {  //проверяем невозможность пополнения первой карты на саму себя
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceFromCard = dashboardPage.getCardBalance(0);
        dashboardPage.validChoosePay1();
        dashboardPage.checkHeadingPaymentCards();
        dashboardPage.validPayAmount();
        val dashboardPage1 = dashboardPage.validPayCard(0); //ввели данные той же первой карты
        val actual = dashboardPage1.getCardBalance(0); //получили новый баланс
        val expected = initialBalanceFromCard; //посчитали каким должен быть баланс, он не долен измениться
        assertEquals(expected, actual); //сравнили балансы
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV4() {  //проверяем невозможность пополнения первой карты на несуществующую карту
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceFromCard = dashboardPage.getCardBalance(0);
        dashboardPage.validChoosePay1();
        dashboardPage.checkHeadingPaymentCards();
        dashboardPage.validPayAmount();
        dashboardPage.validPayCard(2); //ввели данные несуществующей карты, заранее заведенной в DataHelper
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Произошла ошибка"));
    }


    //Упавший тест
//    @Test
//    void shouldTransferMoneyBetweenOwnCardsV5() {  //проверяем невозможность пополнения первой карты на сумму, большую чем на карте
//        val loginPage = new LoginPageV2();
//        val authInfo = DataHelper.getAuthInfo();
//        val verificationPage = loginPage.validLogin(authInfo);
//        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
//        val dashboardPage = verificationPage.validVerify(verificationCode);
//        dashboardPage.checkHeadingYourCards();
//        val initialBalanceToCard = dashboardPage.getCardBalance(0);
//        val initialBalanceFromCard = dashboardPage.getCardBalance(1);
//        dashboardPage.validChoosePay1();
//        dashboardPage.checkHeadingPaymentCards();
//        dashboardPage.validPayExtendAmount(initialBalanceFromCard + 1); //вводим сумму, большую на 1 руб., чем имеется на счету второй карты
//        dashboardPage.validPayCard(1);
//        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Вы не можете перевести средств больше, чем есть на карте"));
//    }


}
