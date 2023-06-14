package webscanner.adapters;

import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;

/*
Класс реализует взаимодействие с браузером.
 */
public class WebDriverAdapter {

    @Step("Поиск элемента по Xpath")
    public SelenideElement findElementByXpath(String xpath) {
        return element(By.xpath(xpath));
    }

    @Step("Поиск коллекции элементов по Xpath")
    public ElementsCollection findElementsCollectionByXpath(String xpath) {
        return elements(By.xpath(xpath));
    }

    @Step("Открыть страницу в браузере")
    public void openPage(String url) {
        Selenide.open(url);
    }

    @BeforeSuite(description = "Установка конфигурации браузера")
    public void beforeSuite() {
        Configuration.browser = Browsers.CHROME;
        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = true;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
    }

    @AfterMethod(description = "Окончание работы с веб-драйвером")
    public void afterTest() {
        Selenide.closeWebDriver();
    }
}
