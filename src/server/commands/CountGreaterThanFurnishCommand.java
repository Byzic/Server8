package server.commands;

import common.User;
import common.data.Flat;
import exceptions.EmptyArgumentException;
import server.utility.CollectionManager;
import server.utility.ResponseCreator;


/**
 * Класс для команды "count_greater_than_furnish"
 */
public class CountGreaterThanFurnishCommand extends AbstractCommand {
    CollectionManager collectionManager;
    public CountGreaterThanFurnishCommand(CollectionManager collectionManager){
        super("count_greater_than_furnish furnish","вывести количество элементов, значение поля furnish которых больше заданного");
        this.collectionManager=collectionManager;
    }

    /**
     * Выполнение команды
     * @param argument аргумент
     * @return состояние выполнения команды
     */
    @Override
    public boolean execute(String argument, Flat flat, User user) {
        int count=0;
        try{
            if (argument.isEmpty()) throw new EmptyArgumentException();
            count=collectionManager.countFurnish(argument);
            ResponseCreator.appendln(count+"\u001B[37m"+"\u001B[33m"+"-столько элементов имеет значение furnish болше заданного"+"\u001B[33m"+"\u001B[37m");
            return true;
        }catch (EmptyArgumentException e){
            ResponseCreator.error("У этой команды должен быть аргумент(значение furnish)");
        }catch (IllegalArgumentException e){
            ResponseCreator.error("Такого значения нет в furnish");
        }
        return false;
    }
}
