package server.commands;

import common.User;
import common.data.Flat;
import exceptions.IncorrectValueException;
import server.utility.ResponseCreator;

public class ServerExitCommand extends AbstractCommand {
    public ServerExitCommand() {
        super("server_exit", "завершить работу сервера");
    }
    @Override
    public boolean execute(String stringArgument, Flat flat, User user) {
        try {
            if (!stringArgument.isEmpty() || flat != null) throw new IncorrectValueException();
            ResponseCreator.appendln("Работа сервера успешно завершена!");
            return true;
        } catch (IncorrectValueException exception) {
            ResponseCreator.appendln("У этой команды нет параметров! Необходимо ввести: server_exit");
        }
        return false;
    }
}
