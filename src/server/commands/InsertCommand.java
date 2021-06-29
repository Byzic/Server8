package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseManagerException;
import exceptions.EmptyArgumentException;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseCreator;


/**
 * Команда "insert". Добавляет новый элемент в коллекцию
 */
public class InsertCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public InsertCommand(CollectionManager collectionManager,DatabaseCollectionManager databaseCollectionManager){
        super("insert","добавить новый элемент с заданным ключом");
        this.collectionManager=collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
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
            int key =Integer.parseInt(argument);
            collectionManager.Key(key);
            collectionManager.insertNew(key,databaseCollectionManager.insertFlat(flat, user, key));
            ResponseCreator.appendln("Insert");
            return true;}
        catch (EmptyArgumentException e) {
            //ResponseCreator.error("У этой команды должны быть аргументы(Ключ для добавления нового значения и квартира, которую необходимо добавить) " );
        } catch (DatabaseManagerException e) {
            ResponseCreator.appendln("DBExeption");
        }catch (NumberFormatException e){
            //ResponseCreator.error("Формат введенного аргумента неверен. Он должен быть целым....");
        }catch(NullPointerException e){

            ResponseCreator.appendln("ElemAlreadyExist");
        }
        return false;
    }
}
