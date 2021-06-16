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




