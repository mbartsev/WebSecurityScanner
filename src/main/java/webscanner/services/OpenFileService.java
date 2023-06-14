package webscanner.services;

import io.qameta.allure.Step;
import webscanner.setup.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenFileService {

    private String documentName;

    public OpenFileService(String documentName) {
        this.documentName = documentName;
    }

    @Step("Проверить открыт ли документ")
    public Boolean documentIsExist() throws IOException {
        String robotsUrlStr = Config.BASE_URL + documentName;
        URL robotsUrl = new URL(robotsUrlStr);
        HttpURLConnection conn = (HttpURLConnection) robotsUrl.openConnection();
        conn.setRequestMethod("GET");

        return checkResponse(conn.getResponseCode());
    }

    @Step("Проверить код ответа от GET-запроса")
    private boolean checkResponse(int responseCode) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Файл Robots.txt доступен.");
            return true;
        } else {
            System.out.println("Файл Robots.txt не существует.");
            return false;
        }
    }
}

