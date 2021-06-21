package server.commands;

import common.User;
import common.data.Flat;
import exceptions.*;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseCreator;

import java.util.List;


/**
 * Команда "remove_lower_key". Удаляет эл-ты с меньшим ключом
 */
public class RemoveLowerKeyCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public RemoveLowerKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_lower_key","удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        this.collectionManager=collectionManager;
        this.databaseCollectionManager=databaseCollectionManager;
    }
    /**
     * Выполнение команды
     * @param argument аргумент
     * @return состояние выполнения команды
     */
    @Override
    public boolean execute(String argument, Flat flat, User user){
        try{int count=0;
            if (argument.isEmpty() ) throw new EmptyArgumentException();
            Integer key=Integer.parseInt(argument);
            List<Integer> keys = collectionManager.getLowerKey(key);
            //int i=collectionManager.removeLowerKey(key);
            if (!keys.isEmpty()) {
                count=0;
                for (int i : keys) {
                    Flat f=collectionManager.getCollectionWithKey(i);
                    boolean b=(f.getOwner().equals(user));
                    if (!(b)) continue;
                    if (!databaseCollectionManager.checkFlatByIdAndUserId(collectionManager.getCollectionWithKey(i).getID(), user))
                        throw new IllegalDatabaseEditException();
                    databaseCollectionManager.deleteFlatById(collectionManager.getCollectionWithKey(i).getID());
                    collectionManager.removeKey(i);
                    count++;

                }
            }
            ResponseCreator.appendln("\u001B[37m"+"\u001B[33m"+"Было удалено "+count+" квартир с ключом меньше "+key+"\u001B[33m"+"\u001B[37m");
            return true;
        }catch (EmptyArgumentException e) {
            ResponseCreator.error("У этой команды должен быть аргумент(ключ для удаления элементов)" );
        }catch (NumberFormatException e){
            ResponseCreator.error("Формат введенного аргумента неверен. Он должен быть целым.");
        }catch (NullPointerException e){
            ResponseCreator.error("Элемента с таким ключом не существует");
        } catch (DatabaseManagerException e) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
        } catch (IllegalDatabaseEditException e) {
            ResponseCreator.error("Произошло нелегальное изменение объекта в базе данных!");
            ResponseCreator.error("Перезапустите клиент для избежания ошибок!");}

        return false;
    }
}
