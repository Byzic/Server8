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
 * Класс команды "update". Заменяет элемент по ключу
 */
public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("update id {element}","обновить значение элемента коллекции, id которого равен заданному");
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
        try{
            if (argument.isEmpty() || flat == null) throw new EmptyArgumentException();
            Integer id =Integer.parseInt(argument);
            collectionManager.checkId(id);
            int key = collectionManager.getKeyById(id);
            Flat  oldFlat= collectionManager.getCollectionWithKey(key);
            boolean b=(oldFlat.getOwner().equals(user));
            if (!(b))  throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkFlatByIdAndUserId(oldFlat.getID(), user)) throw new IllegalDatabaseEditException();
            databaseCollectionManager.updateFlatByID(id, flat);
            collectionManager.update(key,flat);

            ResponseCreator.appendln("Элемент коллекции с id равным "+argument+" был успешно заменен");
            return true;
        }catch (EmptyArgumentException e) {
            ResponseCreator.error("У этой команды должен быть аргумент(ключ для удаления элементов)" );
        }catch (NumberFormatException e){
            ResponseCreator.error("Формат введенного аргумента неверен . Он должен быть целым.");
        } catch (NullPointerException e){
            ResponseCreator.error("Элемента с таким id не существует. Невозможно обновить значение несуществующего элемента коллекции");
        } catch (PermissionDeniedException e) {
        ResponseCreator.error("Принадлежащие другим пользователям объекты доступны только для чтения!");
        } catch (DatabaseManagerException e) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
          } catch (IllegalDatabaseEditException e) {
        ResponseCreator.error("Произошло нелегальное изменение объекта в базе данных!");
        ResponseCreator.error("Перезапустите клиент для избежания ошибок!");}
        return false;}
}
