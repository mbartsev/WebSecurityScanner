import org.testng.annotations.Test;
import webscanner.adapters.WebDriverAdapter;
import websecurityscanner.WebSecurityScanner;

public class KazanPosadTest {

    private WebSecurityScanner webSecurityScanner = new WebSecurityScanner(new WebDriverAdapter(),
            new int[]{21, 22, 23, 25, 53, 80, 110, 143, 443, 465, 993, 995},
            "robots.txt");

    @Test(description = "Поиск уязвимостей 'XSS'")
    public void checkXss() {
        webSecurityScanner.scanXss();
    }

    @Test(description = "Поиск уязвимости с открытым файлом 'Robots.txt'")
    public void checkRobotsFile() {
        webSecurityScanner.scanOpenDocument();
    }

    @Test(description = "Поиск уязвимостей с открытыми портами")
    public void checkOpenPorts() {
        webSecurityScanner.scanOpenPorts();
    }
}
