package webscanner.services;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import webscanner.adapters.WebDriverAdapter;
import webscanner.setup.Config;

import java.util.ArrayList;
import java.util.List;

public class LinkService {

    private final WebDriverAdapter webDriverAdapter;

    public LinkService(WebDriverAdapter webDriverAdapter) {
        this.webDriverAdapter = webDriverAdapter;
    }

    @Step("Получить список всех элементов с атрибутами 'href' или 'src'")
    public List<String> getLinkListForScan() {
        ElementsCollection linkList = webDriverAdapter.findElementsCollectionByXpath("//*[@href or @src]");

        List<String> filteredLinkList = filterLinkList(linkList);
        System.out.println("Отобрано link " + filteredLinkList.size() + " из " + linkList.size());
        return filteredLinkList;
    }

    @Step("Отфильтровать коллекцию элементов 'Link' и получить из них список с ссылками")
    private List<String> filterLinkList(ElementsCollection linkList) {
        List<String> filteredLinkList = new ArrayList<>();
        for (SelenideElement link : linkList) {
            String linkText = link.getAttribute("href");
            if (linkText != null) {
                if (linkText.contains(Config.BASE_URL) && !linkText.equals(Config.BASE_URL)) {
                    if (!linkText.substring(Config.BASE_URL.length()).contains(".")) {
                        filteredLinkList.add(linkText);
                    }
                }
            }
        }
        return filteredLinkList;
    }
}

