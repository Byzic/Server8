package server.commands;

import common.User;
import common.data.Flat;
import exceptions.*;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseCreator;


/**
 * Класс команды "replace_if_greater". Удаляет, если элемент меньше
 */
public class ReplaceIfGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public ReplaceIfGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("replace_if_greater","заменить значение по ключу, если новое значение больше старого");
        this.collectionManager=collectionManager;
        this.databaseCollectionManager=databaseCollectionManager;
    }
    /**
     * Выполнение команды
     * @param argument аргумент
     * @return состояние выполнения команды
     */
    @Override
    public boolean execute(String argument, Flat flat, User user) {
        try{
            if (argument.isEmpty() || flat == null) throw new EmptyArgumentException();
            Integer key=Integer.parseInt(argument);
            Flat  oldFlat= collectionManager.getCollectionWithKey(key);
            int id= oldFlat.getID();
            flat.setID(id);
            boolean b=(oldFlat.getOwner().equals(user));
            if (!(b)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkFlatByIdAndUserId(oldFlat.getID(), user)) throw new IllegalDatabaseEditException();
            collectionManager.checkKey(key);
            databaseCollectionManager.updateFlatByID(id,flat);
            flat.setID(oldFlat.getID());
            collectionManager.replaceIfGreater(key,flat);
            return true;
        }catch (EmptyArgumentException e) {
            ResponseCreator.error("У этой команды должен быть аргумент(ключ для удаления элементов)" );
        }catch (NumberFormatException e){
            ResponseCreator.error("Формат введенного аргумента неверен. Он должен быть целым.");
        }catch (NullPointerException e){
            ResponseCreator.error("Элемента с таким ключом не существует");
        }catch (СomparisonExeption e){
            ResponseCreator.appendln("\u001B[37m"+"\u001B[33m"+"Квартира c данным ключом не была заменена, так как меньше или равна уже существующей"+"\u001B[33m"+"\u001B[37m");
        }catch (PermissionDeniedException e) {
            ResponseCreator.error("Принадлежащие другим пользователям объекты доступны только для чтения!");
        } catch (DatabaseManagerException e) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
        } catch (IllegalDatabaseEditException e) {
            ResponseCreator.error("Произошло нелегальное изменение объекта в базе данных!");
            ResponseCreator.error("Перезапустите клиент для избежания ошибок!");}

        return false;
    }
}
