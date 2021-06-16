package server.utility;

import common.Request;
import common.Response;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ForkJoinPool;

public class RequestProcessingThread extends Thread{
    private RequestManager requestManager;
    private Request request;
    private InetAddress address;
    private int port;
    private Response response;
    private DatagramSocket socket;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private Exchanger<ResponseWriteAction> ex;

    public RequestProcessingThread(RequestManager requestManager, Request request,InetAddress address, int port, DatagramSocket socket,  Exchanger<ResponseWriteAction> ex) {
        this.requestManager = requestManager;
        this.request = request;
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.ex=ex;

    }
    @Override
    public void run() {
        System.out.println("Обрабатывается команда "+request.getCommandName());
        response= requestManager.manage(request);
        System.out.println("Команда "+request.getCommandName()+" обработана");
        try {
            ex.exchange(new ResponseWriteAction(response,address,port,socket));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //forkJoinPool.invoke(new ResponseWriteAction(response, address, port, socket));
        System.out.println("Результат команды "+request.getCommandName()+" отаправлен пользователю");





    }

}
