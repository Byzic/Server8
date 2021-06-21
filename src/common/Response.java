package common;

import common.data.Flat;


import java.io.Serializable;
import java.util.Hashtable;

//Класс для получения ответа от сервера
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private Hashtable<Integer, Flat> collection;
    public Response(ResponseCode responseCode, String responseBody, Hashtable<Integer, Flat> collection) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.collection = collection;
    }

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }
    public String localize() {
        StringBuilder localizeStringBody = new StringBuilder();
        String[] lines = responseBody.split("\n");
        for (String line : lines) {
            if (line.isEmpty()) continue;
            if (line.indexOf(":")!=-1){
                String[] l=line.split(":");System.out.println(l[0]+" "+l[1]);
                //localizeStringBody.append(LocaleBundle.getCurrentBundle().getString(l[0].trim())+l[1]+"\n");

            }else{
                //localizeStringBody.append(LocaleBundle.getCurrentBundle().getString(line)+"\n");
            }


        }return localizeStringBody.toString();
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
    public Hashtable<Integer, Flat> getCollection(){
        return collection;
    }
}


