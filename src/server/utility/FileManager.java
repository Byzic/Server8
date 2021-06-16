package server.utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import common.data.Flat;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Класс загрузки/чтения коллекции из файла
 */
public class FileManager {
    private Gson gson = new Gson();
    private String envVariable;
    File file;

    public FileManager(String fileName) {
        try{
        this.envVariable = fileName;
        this.file=new File(System.getenv(envVariable));
        }catch(NullPointerException e){
            System.out.println("\u001B[37m"+"\u001B[31m"+"Вам необходимо задать переменную окружения!!!"+"\u001B[31m"+"\u001B[37m");
        }
    }

    /**
     * Запись коллекции в файл
     * @param collection -коллекция, которую нужно записать
     */
    public void writeCollection(Hashtable collection) {
        if (System.getenv().get(envVariable) != null) {
            if (!file.canWrite()) {
                System.out.println("\u001B[37m"+"\u001B[31m"+"Недостаточно прав для записи в файл. Добавьте права на запись "+"\u001B[31m"+"\u001B[37m");
                try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File("/home/s311701/prog5/lib/file2")))){
                    out.write(gson.toJson(collection));
                    System.out.println("Не переживайте. Мы записали вашу коллекцию в новый файл: "+"/home/s311701/prog5/lib/file2");
                }catch(Exception e){

                }

            }else{
                try ( OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(System.getenv().get(envVariable)))){
                    File file=new File(System.getenv().get(envVariable));

                    pw.write(gson.toJson(collection));
                    System.out.println("Коллекция успешно сохранена в файл!");

                } catch (Exception e) {
                    System.out.println();

                }}
        } else System.out.println("Системная переменная с загрузочным файлом не найдена!");
    }

    /**
     *Чтение коллекции из файла
     * @return коллекция, которая была считана из файла
     */
    public Hashtable<Integer, Flat> readCollection()  {
        if (System.getenv().get(envVariable) != null) {
            if (file.exists() & !file.canRead()) {
                System.out.println("\u001B[37m"+"\u001B[31m"+"Недостаточно прав для чтения данных из файла. Добавьте права на чтение и запустите программу вновь"+"\u001B[31m"+"\u001B[37m");
                System.exit(0);
            }
            try (FileReader fileScanner = new FileReader(file)){

                Type collectionType = new TypeToken<Hashtable<Integer,Flat>>() {}.getType();
                Hashtable<Integer,Flat> collection = gson.fromJson(fileScanner,collectionType);
                System.out.println("\u001B[37m"+"\u001B[33m"+"Коллекция успешно загружена!"+"\u001B[33m"+"\u001B[37m");
                if (collection==null) return new Hashtable<>();
                parsingCheck(collection);
                return collection;
            } catch (FileNotFoundException e) {
                System.err.println("Файл с таким именем не найден :(");

            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода");

            }catch(JsonSyntaxException e){
                System.err.println("Формат файла не удовлетворяет условию");
            }
            }else System.out.println("\u001B[37m"+"\u001B[31m"+"Системная переменная с загрузочным файлом не найдена!"+"\u001B[31m"+"\u001B[37m");
            return new Hashtable<>();

    }

    private void parsingCheck(Hashtable<Integer,Flat> collection){
        List<Integer> keys= new ArrayList<>();
        for (Map.Entry<Integer, Flat> e : collection.entrySet()) {
            if (e.getValue().getID()==null || e.getValue().getCoordinates().getX()==null || e.getValue().getCoordinates().getY()==null || e.getValue().getName()==null || e.getValue().getArea()==null || e.getValue().getCoordinates()==null || e.getValue().getNumberOfRooms()==null || e.getValue().getFurnish()==null || e.getValue().getView()==null || e.getValue().getTransport()==null ||  e.getValue().getHouse()!=null &&(e.getValue().getHouse().getName()==null || e.getValue().getHouse().getYear()==null || e.getValue().getHouse().getNumberOfFloors()==null )){
                keys.add(e.getKey());

            }
        }
        for ( Integer k:keys){
            collection.remove(k);
        }

    }
    @Override
    public String toString(){
        String string = "FileManager (класс для работы с загрузочным файлом)";
        return string;
    }
}
