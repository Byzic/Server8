package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseManagerException;
import exceptions.EmptyArgumentException;
import exceptions.IllegalDatabaseEditException;
import exceptions.PermissionDeniedException;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseCreator;


/**
 * Команда "remove_key". Удаляет эл-т по ключу
 */
public class RemoveKeyCommand extends AbstractCommand {
    CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public RemoveKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_key","удалить элемент из коллекции по его ключу");
        this.collectionManager=collectionManager;
        this.databaseCollectionManager=databaseCollectionManager;

    }
    /**
     * Выполнение команды
     * @param argument аргумент
     * @return состояние выполнения команды
     */
    @Override
    public boolean execute(String argument, Flat flat, User user){// проверить ключ на наличие
        try{
            if (argument.isEmpty() || flat != null) throw new EmptyArgumentException();
            Integer key=Integer.parseInt(argument);

            collectionManager.checkKey(key);
            Flat f=collectionManager.getCollectionWithKey(key);
            boolean b=(f.getOwner().equals(user));
            if (!(b))throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkFlatByIdAndUserId(f.getID(), user)) throw new IllegalDatabaseEditException();
            databaseCollectionManager.deleteFlatById(f.getID());
            collectionManager.removeKey(key);
            return true;
        }catch (EmptyArgumentException e) {
           // ResponseCreator.error("У этой команды должен быть аргумент(ключ для удаления элемента)" );
        }catch (NumberFormatException e){
            //ResponseCreator.error("Формат введенного аргумента неверен. Он должен быть целым.");
        }catch (NullPointerException e){
            ResponseCreator.appendln("notExist");
        }catch (DatabaseManagerException e) {
            ResponseCreator.appendln("DBExeption");
        }catch (PermissionDeniedException e){
            ResponseCreator.appendln("anotherUser");
        }catch (IllegalDatabaseEditException exception) {
            //ResponseCreator.error("Произошло нелегальное изменение объекта в базе данных!\n");
            //ResponseCreator.error("Перезапустите клиент для избежания ошибок!\n");
        }
        return false;
    }
}
