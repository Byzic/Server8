package server.commands;

import common.User;
import common.data.Flat;
import exceptions.IncorrectValueException;
import server.utility.CollectionManager;
import server.utility.ResponseCreator;


/**
 * Класс команды "show". Выводит коллекцию в строковом представлении
 */
public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager){
        super("show","Показать все элемены коллекции");
        this.collectionManager=collectionManager;
    }
    @Override
    public boolean execute(String argument, Flat flat, User user){
        try {
            if (!argument.isEmpty())throw new IncorrectValueException();
            ResponseCreator.appendln(collectionManager.getStringElements());
        return true;
        }
        catch (IncorrectValueException e) {
            ResponseCreator.error("У этой команды нет параметров! Необходимо ввести: show");
        }
        return false;
    }
}
