package webscanner.services;

import io.qameta.allure.Step;
import webscanner.setup.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortsService {

    private final int[] ports;
    private final List<Integer> openPorts = new ArrayList<>();

    public PortsService(int[] ports) {
        this.ports = ports;
    }

    @Step("Получить список открытых портов")
    public List<Integer> getOpenPorts() {
        return openPorts;
    }

    @Step("Отсканировать открытые порты")
    public void scanPorts() {
        openPorts.clear();
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(Config.URL.getHost(), port), 2000);
                System.out.println("Порт " + port + " открыт.");
                openPorts.add(port);
                socket.close();
            } catch (IOException ex) {
                System.out.println("Порт " + port + " закрыт.");
            }
        }
    }
}


