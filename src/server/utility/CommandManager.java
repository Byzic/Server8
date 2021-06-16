package server.utility;


import common.User;
import common.data.Flat;
import server.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Класс для запуска команд
 */
public class CommandManager {
    private final int maxCommandSize = 14;

    private List<AbstractCommand> commands = new ArrayList<>();//массив с командами
    private AbstractCommand helpCommand;
    private AbstractCommand infoCommand;
    private AbstractCommand showCommand;
    private AbstractCommand insertCommand;
    private AbstractCommand updateIdCommand;
    private AbstractCommand removeKeyCommand;
    private AbstractCommand executeScriptCommand;
    private AbstractCommand clearCommand;
    private AbstractCommand replaceIfGreaterCommand;
    private AbstractCommand replaceIfLowerCommand;
    private AbstractCommand removeLowerKeyCommand;
    private AbstractCommand removeAllByNumberOfRoomsCommand;
    private AbstractCommand countFurnishCommand;
    private AbstractCommand filterNameCommand;
    private AbstractCommand exitCommand;
    private AbstractCommand serverExitCommand;
    private AbstractCommand loginCommand;
    private AbstractCommand registerCommand;
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();


    public CommandManager(AbstractCommand exitCommand,AbstractCommand helpCommand, AbstractCommand infoCommand, AbstractCommand showCommand, AbstractCommand insertCommand, AbstractCommand updateIdCommand,
                          AbstractCommand removeKeyCommand, AbstractCommand clearCommand, AbstractCommand executeScriptCommand,
                          AbstractCommand replaceIfGreaterCommand, AbstractCommand replaceIfLowerCommand, AbstractCommand removeLowerKeyCommand, AbstractCommand removeAllByNumberOfRoomsCommand,
                          AbstractCommand countFurnishCommand, AbstractCommand filterNameCommand,AbstractCommand serverExitCommand,AbstractCommand loginCommand,AbstractCommand registerCommand) {
        this.exitCommand=exitCommand;
        commands.add(exitCommand);
        this.helpCommand = helpCommand;
        commands.add(helpCommand);
        this.infoCommand = infoCommand;
        commands.add(infoCommand);
        this.showCommand = showCommand;
        commands.add(showCommand);
        this.insertCommand = insertCommand;
        commands.add(insertCommand);
        this.updateIdCommand = updateIdCommand;
        commands.add(updateIdCommand);
        this.removeKeyCommand = removeKeyCommand;
        commands.add(removeKeyCommand);

        this.clearCommand = clearCommand;
        commands.add(clearCommand);
        this.executeScriptCommand = executeScriptCommand;
        commands.add(executeScriptCommand);
        this.replaceIfGreaterCommand = replaceIfGreaterCommand;
        commands.add(replaceIfGreaterCommand);
        this.replaceIfLowerCommand = replaceIfLowerCommand;
        commands.add(replaceIfLowerCommand);
        this.removeLowerKeyCommand = removeLowerKeyCommand;
        commands.add(removeLowerKeyCommand);
        this.removeAllByNumberOfRoomsCommand = removeAllByNumberOfRoomsCommand;
        commands.add(removeAllByNumberOfRoomsCommand);
        this.countFurnishCommand = countFurnishCommand;
        commands.add(countFurnishCommand);
        this.filterNameCommand = filterNameCommand;
        commands.add(filterNameCommand);
        this.serverExitCommand=serverExitCommand;
        this.loginCommand=loginCommand;
        this.registerCommand=registerCommand;
    }

    /**
     * Выводит все доступные команды с описанием
     * @param argument это аргумент
     * @return Состояние работы команды
     */

    public boolean help (String argument, Flat flat, User user){
        if (helpCommand.execute(argument,flat,user)) {
            for (AbstractCommand command : commands) {
                ResponseCreator.appendln(command.getName().toUpperCase()+ ": " + command.getDescription());
            }
            return true;
        } else return false;
    }
    public boolean exit(String argument, Flat flat, User user){
        return exitCommand.execute(argument, flat, user);
    }

    /**
     * Запускает команду информации о коллекции
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean info(String argument, Flat flat,User user){

        return infoCommand.execute(argument, flat,user);

    }


    /**
     * Запускает команду показа всех элементов коллекции
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean show(String argument, Flat flat,User user){

        return showCommand.execute(argument,flat,user);

    }
    /**
     * Запускает команду очистки коллекции
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean clear(String argument, Flat flat,User user){

            return clearCommand.execute(argument, flat, user);

    }
    /**
     * Запускает команду добавления нового элемента
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean insert(String argument, Flat flat,User user){

            return insertCommand.execute(argument, flat, user);

    }
    /**
     * Запускает команду замены элемента по ключу
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean update(String argument, Flat flat,User user){

            return updateIdCommand.execute(argument, flat,user);

    }
    /**
     * Запускает команду удаления элемента по ключу
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean removeKey(String argument, Flat flat,User user){

            return removeKeyCommand.execute(argument, flat,user);

    }
    /**
     * Запускает команду удаления элементов по количеству комнат
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean removeAllByNumber(String argument, Flat flat,User user){

             return removeAllByNumberOfRoomsCommand.execute(argument,  flat,user);

    }
    /**
     * Запускает команду удаления элементов с ключом меньшим чем заданный
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean removeLowerKey(String argument, Flat flat,User user){

            return removeLowerKeyCommand.execute(argument, flat,user);

    }
    /**
     * Запускает команду, которая выводит элементы по имени
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean filterName(String argument, Flat flat,User user){

            return filterNameCommand.execute(argument, flat, user);

    }
    /**
     * Запускает команду выполнения скрипта
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean executeScript(String argument, Flat flat,User user){
        return executeScriptCommand.execute(argument,  flat,user);
    }
    /**
     * Запускает команду замены элемента, если он больше
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean replaceIfGreater(String argument, Flat flat,User user){

        return replaceIfGreaterCommand.execute(argument,  flat,user);

    }
    /**
     * Запускает команду замены элемента, если он меньше
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean replaceIfLower(String argument, Flat flat,User user){

            return replaceIfLowerCommand.execute(argument, flat,user);

    }
    /**
     * Запускает команду подсчета кол-ва элементов с определенной отделкой
     * @param argument это переданный аргумент
     * @return состояние работы программы
     */
    public boolean countFurnish(String argument, Flat flat,User user){

            return countFurnishCommand.execute(argument,  flat,user);

    }
    public boolean serverExit(String argument, Flat flat, User user) {
        return serverExitCommand.execute(argument, flat, user);
    }
    public boolean login(String stringArgument, Flat flat, User user) {
        return loginCommand.execute(stringArgument, flat, user);
    }
    public boolean register(String stringArgument, Flat flat, User user) {
        return registerCommand.execute(stringArgument, flat, user);
    }





}
