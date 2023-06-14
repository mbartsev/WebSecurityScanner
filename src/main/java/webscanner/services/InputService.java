package webscanner.services;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import webscanner.adapters.WebDriverAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputService {

    private final WebDriverAdapter webDriverAdapter;

    public InputService(WebDriverAdapter webDriverAdapter) {
        this.webDriverAdapter = webDriverAdapter;
    }

    @Step("Получить список всех элементов с типом 'Input'")
    public List<SelenideElement> getInputListForScan() {
        ElementsCollection inputList = webDriverAdapter.findElementsCollectionByXpath("//input");

        List<SelenideElement> visibleInputList = filterInputList(inputList);
        System.out.println("Отобрано input " + visibleInputList.size() + " из " + inputList.size());
        return visibleInputList;
    }

    @Step("Отфильтровать коллекцию элементов")
    private List<SelenideElement> filterInputList(List<SelenideElement> inputList) {
        List<SelenideElement> visibleInputList = new ArrayList<>();
        for (SelenideElement inputElement : inputList) {
            if (inputElement.is(Condition.visible)) {
                if (inputElement.getAttribute("id") != null) {
                    if (!Objects.equals(inputElement.getAttribute("id"), "button")) {
                        visibleInputList.add(inputElement);
                    }
                } else {
                    visibleInputList.add(inputElement);
                }
            }
        }
        return visibleInputList;
    }
}

