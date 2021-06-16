package server.utility;

import common.Request;
import common.Response;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ForkJoinPool;

public class ResponseWriterTread extends Thread{
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private InetAddress address;
    private int port;
    private ResponseWriteAction responseWriteAction;
    private DatagramSocket socket;
    Exchanger<ResponseWriteAction> exResponse;
    public ResponseWriterTread(  Exchanger<ResponseWriteAction> ex){
        this.exResponse=ex;



    }
    @Override
    public void run(){
        try {
            while(true){
            responseWriteAction=exResponse.exchange(null);

            if (responseWriteAction!=null) {
                forkJoinPool.invoke(responseWriteAction);
            }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
