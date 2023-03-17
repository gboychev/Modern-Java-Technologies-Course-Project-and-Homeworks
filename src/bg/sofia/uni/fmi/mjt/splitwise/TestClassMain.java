package bg.sofia.uni.fmi.mjt.splitwise;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestClassMain {
    public static void main(String[] args) {
        final int port = 7777;
        Storage storage = new Storage();
        ServerCommandExecutor sce = new ServerCommandExecutor(storage);
        Server server = new Server(port, sce);
        Thread servTh = new Thread(server);
        servTh.start();
        System.out.println("The server is ready to accept commands");
    }
}
