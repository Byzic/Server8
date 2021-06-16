package server.utility;

import common.data.Flat;
import common.data.Furnish;
import exceptions.СomparisonExeption;
import exceptions.KeyException;

import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Управляет коллекцией
 */
//работа с коллекцией: создание нового id, добавление элемента, удаление и тд
public class CollectionManager {
    private Hashtable<Integer, Flat> hashtable=new Hashtable<>();
    private FileManager fileManager;
    private LocalDateTime lastInitTime;
    DatabaseCollectionManager databaseCollectionManager;


    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    /**
     * Записывает коллекцию в файл
     */


    /**
     * Читает коллекцию из файла
     */
    public void loadCollection() {
        synchronized (hashtable){
            hashtable = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            System.out.println("Коллекция загружена.");
        }

    }
    public Hashtable<Integer,Flat> getCollection(){
        synchronized (hashtable){
            return hashtable;}
    }

    /**
     * Представляет все элементы коллекции в виде строки
     * @return строковое представление коллекции
     */
    public String getStringElements(){
        synchronized (hashtable){
            String strElem="";
            if (hashtable.isEmpty()) return "Коллекция пуста!!!";
            for (String str: hashtable.entrySet().stream().map(x->"\u001B[37m"+"\u001B[33m"+"КЛЮЧ:"+x.getKey()+
                    "\n"+"\u001B[33m"+"\u001B[37m"+x.getValue().toString()+"\n").collect(Collectors.toList())){
                strElem+=str;
            }
            return strElem;
        }


    }

    /**
     * Чистит коллекцию
     */

    public void clear(){
        synchronized (hashtable){
            hashtable.clear();
        }
    }
    /**
     * Определяет класс коллекции
     * @return имя класса коллекции
     */
    public String collectionType(){
        synchronized (hashtable){

            return hashtable.getClass().getName();
        }


    }
    /**
     * Определяет размер коллекции
     * @return размер коллекции
     */
    public int collectionSize(){
        synchronized (hashtable) {
            return hashtable.size();
        }

    }
    public LocalDateTime getLastInitTime(){
        return lastInitTime;
    }



    /**
     * Добавляет новый элемент в коллекцию
     * @param key ключ
     * @param flat значение
     */
    public void insertNew(Integer key, Flat flat){
        synchronized (hashtable) {
            long start= System.nanoTime();
            long time=0;

            while (time<10){
                long finish= System.nanoTime();
                time=(finish-start)/ 1000000000;}
            hashtable.put(key, flat);
        }

    }

    /**
     * Находит ключ элемента по его ID
     * @param id id
     * @return ключ
     */
    public Integer getKeyById(Integer id){
        synchronized (hashtable) {

            List list = hashtable.entrySet().stream().filter(x -> x.getValue().getID().equals(id)).map(x -> x.getKey()).collect(Collectors.toList());
            if (list.isEmpty()) return null;
            else return Integer.parseInt(list.get(0).toString());
        }



    }

    /**
     * Заменяет элемент по ключу
     * @param key ключ
     * @param flat значение
     */
    public void update(Integer key, Flat flat){
        synchronized (hashtable) {
            long start= System.nanoTime();
            long time=0;

            while (time<1){
                long finish= System.nanoTime();
                time=(finish-start)/ 1000000000;
            }
            hashtable.remove(key);
            hashtable.put(key, flat);
        }


    }

    /**
     * Удаляет элемент по ключу
     * @param key ключ
     */
    public void removeKey(int key){

        try{synchronized (hashtable){
            if (!hashtable.containsKey(key)) throw new KeyException();
            hashtable.remove(key);}
            ResponseCreator.appendln("\u001B[37m"+"\u001B[33m"+"Элемент с ключом "+ key+" успешно удален"+"\u001B[33m"+"\u001B[37m");
        }catch (KeyException e){

        }
    }
    public List<Integer> getKeysLikeNumber(Integer number){
        synchronized (hashtable){
            List<Integer> a;
            a=hashtable.entrySet().stream().filter(x-> x.getValue().getNumberOfRooms().equals(number)).map(x->x.getKey()).collect(Collectors.toList());
            return a;
        }
    }

    /**
     * Удаляет все элементы с заданным числом комнат
     * @param number число комнат
     * @return количество удаленных элементов
     */
    public int removeAllByNumber(Integer number){
        synchronized (hashtable) {
            List<Integer> a;
            a = hashtable.entrySet().stream().filter(x -> x.getValue().getNumberOfRooms().equals(number)).map(x -> x.getKey()).collect(Collectors.toList());
            for (Integer i : a) {
                hashtable.remove(i);
            }
            return a.size();
        }

    }
    public List<Integer> getLowerKey(Integer key){
        synchronized (hashtable) {
            List<Integer> a;
            a = hashtable.entrySet().stream().filter(x -> x.getKey().intValue() < key.intValue()).map(x -> x.getKey()).collect(Collectors.toList());
            return a;
        }
    }

    /**
     * Удаляет все элементы с наименьшими ключами
     * @param key ключ
     * @return количество удаленных элементов
     */
    public int removeLowerKey(Integer key){
        synchronized (hashtable) {
            List<Integer> a;
            a = hashtable.entrySet().stream().filter(x -> x.getKey().intValue() < key.intValue()).map(x -> x.getKey()).collect(Collectors.toList());
            for (Integer i : a) {
                hashtable.remove(i);
            }
            return a.size();
        }

    }

    /**
     * Находит элементы, имя которых начинается с заданной подстроки
     * @param string подстрока
     *
     */
    public void filterName(String string){
        synchronized (hashtable){
        hashtable.entrySet().stream().filter(x->x.getValue().getName().indexOf(string)==0).map(x->x.getValue().toString()).forEach(ResponseCreator::appendln);
        if (ResponseCreator.get().isEmpty()){ResponseCreator.appendln("\u001B[37m"+"\u001B[33m"+"Нет элементов, начинающихся на такую строку"+"\u001B[33m"+"\u001B[37m");}
        }
    }

    /**
     * Заменяет значение по ключу, если оно больше
     * @param key ключ
     */
    public void replaceIfGreater(Integer key,Flat flat) throws СomparisonExeption {
        try{synchronized (hashtable){
            if (hashtable.get(key).compareTo(flat)<0) {
                hashtable.put(key, flat);
                ResponseCreator.appendln("\u001B[37m" + "\u001B[33m" + "Квартира с ключом " + key + " была успешно заменена" + "\u001B[33m" + "\u001B[37m");
                }

            else {throw new СomparisonExeption(); }}
        }catch(NullPointerException e){
            ResponseCreator.error("Элемента с таким ключом не существует");
        }


    }

    /**
     * Проверяет, есть ли в коллекции элемент с данным ключом
     * @param key ключ
     * @throws NullPointerException если ключа нет
     */
    public void checkKey(Integer key){
        synchronized (hashtable) {
            if (!hashtable.containsKey(key)) {
                throw new NullPointerException();
            }
        }

    }

    public Flat getCollectionWithKey(int key){
        synchronized (hashtable) {
            return hashtable.get(key);
        }
    }
    /**
     * Проверяет, есть ли в коллекции элемент с данным ключом
     * @param key ключ
     * @throws NullPointerException если ключ есть
     */
    public void Key(Integer key) throws NullPointerException{
        synchronized (hashtable) {
            if (hashtable.containsKey(key)) {
                throw new NullPointerException();
            }
        }
    }

    /**
     * Проверяет, есть ли в коллекции элемент с таким ID
     * @param id id
     * @throws NullPointerException если нет эл-та с таким ID
     */
    public void checkId(Integer id){
        synchronized (hashtable) {
            List a;
            boolean checker = false;
            checker = !(hashtable.entrySet().stream().filter(x -> x.getValue().getID().equals(id)).collect(Collectors.toList())).isEmpty();

            if (checker = false) throw new NullPointerException();
        }

    }
    /**
     * Заменяет значение по ключу, если оно меньше
     * @param key ключ
     */
    public void replaceIfLower(Integer key,Flat flat) throws СomparisonExeption {
        synchronized (hashtable) {
            if (hashtable.get(key).compareTo(flat) > 0) {
                hashtable.put(key, flat);
                ResponseCreator.appendln("\u001B[30m" + "\u001B[33m" + "Квартира с ключом " + key + " была успешно заменена" + "\u001B[33m" + "\u001B[30m");
            } else {
                throw new СomparisonExeption();
            }
        }
    }

    /**
     * Генерирует ID
     * @return ID

    public Flat newId(Flat flat){
        int id;
        if (hashtable.isEmpty()) id=1;
        int lastId = 0;
        for (Flat f : hashtable.values()) {
            lastId = Math.max(lastId, f.getID());
        }
        id= lastId + 1;
        return new Flat(id, flat.getName(), flat.getCoordinates(), flat.getCreationDate(),flat.getArea(),flat.getNumberOfRooms(),flat.getFurnish(),flat.getView(),flat.getTransport(),flat.getHouse(),user);
    }*/

    /**
     * Считает кол-во элементов с определенной отделкой
     * @param furnish определенная отделка
     * @return количество эл-тов
     */
    public int countFurnish(String furnish){
        synchronized (hashtable) {
            int count = (int) hashtable.entrySet().stream().filter(x -> x.getValue().getFurnish().compareTo(Furnish.valueOf(furnish.toUpperCase())) > 0).count();
            return count;
        }
    }

}
