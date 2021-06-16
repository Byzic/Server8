package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseManagerException;
import exceptions.IllegalDatabaseEditException;
import exceptions.IncorrectValueException;
import exceptions.PermissionDeniedException;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseCreator;

import java.util.Hashtable;
import java.util.Map;

/**
 * команда "clear". Удаляет все элементы коллекции
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("clear","очистить коллекцию");
        this.collectionManager=collectionManager;
        this.databaseCollectionManager=databaseCollectionManager;
    }

    /**
     * Выполнение команды
     * @param argument аргумент
     * @return Статус выполнения программы
     */
    @Override
    public boolean execute(String argument, Flat flat, User user){
        try {
            int count=0;
            if (!argument.isEmpty() || flat!= null)throw new IncorrectValueException();

            for (Map.Entry<Integer, Flat> e : collectionManager.getCollection().entrySet()) {
                Flat f=e.getValue();

                boolean b=(f.getOwner().equals(user));
                if (!(b)) continue;
                if (!databaseCollectionManager.checkFlatByIdAndUserId(f.getID(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteFlatById(f.getID());
                collectionManager.removeKey(collectionManager.getKeyById(f.getID()));
                count++;
                
            }



            ResponseCreator.appendln("\u001B[37m"+"\u001B[33m"+"Все элементы, принадлежащие данному пользователю удалены("+count+" элементов)"+"\u001B[33m"+"\u001B[37m");
            return true;
        }catch (IncorrectValueException e) {
            ResponseCreator.error("У этой команды нет параметров! Необходимо ввести: clear");
        } catch (DatabaseManagerException e) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
        } catch (IllegalDatabaseEditException e) {
            ResponseCreator.error("Произошло нелегальное изменение объекта в базе данных!");
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }


}
