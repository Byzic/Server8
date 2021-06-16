package server.commands;

import common.User;
import common.data.Flat;
import exceptions.EmptyArgumentException;
import server.utility.CollectionManager;
import server.utility.ResponseCreator;


/**
 * Команда "filter_starts_with_name". Выводит элементы с именем, начинающимся на подстроку
 */
public class FilterNameCommand extends AbstractCommand {
    CollectionManager collectionManager;
    public FilterNameCommand(CollectionManager collectionManager){
        super("filter_starts_with_name name","вывести элементы, значение поля name которых начинается с заданной подстроки");
        this.collectionManager=collectionManager;
    }
    /**
     * Выполнение команды
     * @param argument аргумент
     * @return состояние выполнения команды
     */

    @Override
    public boolean execute(String argument, Flat flat, User user) {
        try {
            if (argument.isEmpty()) throw new EmptyArgumentException();
            collectionManager.filterName(argument);
            return true;
        } catch (
                EmptyArgumentException e) {
            ResponseCreator.error("У этой команды должен быть аргумент(строка, с которой будет начинаться name)");

        }
        return false;
    }
}
