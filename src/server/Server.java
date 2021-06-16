package server;

import common.Request;
import common.Response;
import common.ResponseCode;
import server.utility.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {
    private int port;
    private CommandManager commandManager;
    ServerSocket serverSocket;
    Scanner scanner;
    Socket clientSocket;
    private boolean isStopped = false;
    private RequestManager requestManager;
    Request userRequest = null;
    ObjectOutputStream oos;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public Server(int port, RequestManager requestManager) {
        this.port = port;
        this.requestManager = requestManager;
    }

    public void run() {
        do_CTRL_C_Thread();
        openServerSocket();
        while (true) {
            Socket clientSocket = connectToClient();
            try {
                if (!cachedThreadPool.submit(() -> {

                    Response responseToUser = null;
                    boolean stopFlag = false;
                    try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());) {

                        ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
                        userRequest = (Request) clientReader.readObject();
                        oos=clientWriter;
                        System.out.println("Получена команда " + userRequest.getCommandName());
                        return true;
                    } catch (ClassNotFoundException exception) {
                        System.out.println("Произошла ошибка при чтении полученных данных!");
                    }
                    return false;
                }).get()) break;                 //чтение запроса
                //new RequestProcessingThread(requestManager, userRequest, oos).start();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("При чтении запроса произошла ошибка многопоточности!");
            }

        }


    }


    private void openServerSocket() {
        try {
            System.out.println("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер успешно запущен");
        } catch (IOException e) {
            System.err.println("Произошла ошибка при попытке использовать порт '" + port + "'!");
        }
    }

    private Socket connectToClient() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Соединение с клиентом успешно установлено");
            return clientSocket;
        } catch (IOException e) {
            System.err.println("Ошибка при соединении с клиентом");
        }
        //убрать следующую строчку
        return clientSocket;

    }
    private void do_CTRL_C_Thread() {
        scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Завершаю программу.");
        }));
    }
}
