package websecurityscanner;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.AlertNotFoundException;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import webscanner.adapters.WebDriverAdapter;
import webscanner.services.InputService;
import webscanner.services.LinkService;
import webscanner.services.OpenFileService;
import webscanner.services.PortsService;
import webscanner.setup.Config;

import java.io.IOException;
import java.util.List;

public class WebSecurityScanner {

    private final WebDriverAdapter webDriverAdapter;
    private final InputService inputService;
    private final LinkService linkService;
    private final OpenFileService openFileService;
    private final PortsService portsService;

    public WebSecurityScanner(WebDriverAdapter webDriverAdapter,
                              int[] ports,
                              String documentName) {
        this.webDriverAdapter = webDriverAdapter;
        this.inputService = new InputService(webDriverAdapter);
        this.linkService = new LinkService(webDriverAdapter);
        this.openFileService = new OpenFileService(documentName);
        this.portsService = new PortsService(ports);
    }

    @Step("Отсканировать открытые документы")
    public void scanOpenDocument() {
        try {
            Assert.assertFalse(openFileService.documentIsExist(),
                    "Текстовый файл Robots.txt является открытым для поиска уязвимостей");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Отсканировать открытые порты")
    public void scanOpenPorts() {
        portsService.scanPorts();
        List<Integer> openPorts = portsService.getOpenPorts();
        if (!openPorts.isEmpty()) {
            SoftAssert asserts = new SoftAssert();
            for (Integer openPort : openPorts) {
                asserts.fail("Порт " + openPort + " открыт и уязвим для проведения атак");
            }
            asserts.assertAll();
        }
    }

    @Step("Отсканировать XSS-уязвимости")
    public void scanXss() {
        webDriverAdapter.openPage(Config.BASE_URL);
        List<String> linkTextList = linkService.getLinkListForScan();

        // проверка главной страницы
        SoftAssert softAssert = new SoftAssert();
        softAssert = checkInputList(inputService.getInputListForScan(), Config.BASE_URL, softAssert);

        // проверка найденных страниц в DOM
        for (String link : linkTextList) {
            webDriverAdapter.openPage(link);
            System.out.println("\nПоиск input на странице " + link);
            softAssert = checkInputList(inputService.getInputListForScan(), link, softAssert);
        }
        softAssert.assertAll();
    }

    @Step("Проверка списка с инпутами на странице")
    private SoftAssert checkInputList(List<SelenideElement> inputList, String link, SoftAssert softAssert) {
        // изменить на цикл со счетчиком
        for (SelenideElement input : inputList) {
            try {
                input.sendKeys("<script>alert('XSS TEST')</script>");
            } catch (ElementNotFound ignored) {
                continue;
            }
            input.pressEnter();
            try {
                Alert alert = Selenide.switchTo().alert();
                softAssert.assertFalse(
                        alert.getText().contains("XSS TEST"),
                        "Найдена уязвимость:\nurl: " + link);
            } catch (AlertNotFoundException ignored) {
                Selenide.open(link);
            }
        }
        return softAssert;
    }
}
