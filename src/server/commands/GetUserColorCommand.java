package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseManagerException;
import exceptions.IncorrectValueException;
import server.utility.DatabaseUserManager;
import server.utility.ResponseCreator;

public class GetUserColorCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public GetUserColorCommand(DatabaseUserManager databaseUserManager) {
        super("get_user_color", "служебная команда");
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String argument, Flat flat, User user) {
        try {
            if (!argument.isEmpty() || flat != null) throw new IncorrectValueException();
            String color = databaseUserManager.getColorByUsername(user);
            ResponseCreator.append(color);
            return true;
        } catch (IncorrectValueException e) {
           // ResponseCreator.append("У этой команды должен быть только один параметр: 'user'\n");
        } catch (DatabaseManagerException e) {
            ResponseCreator.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (ClassCastException e) {
            //ResponseCreator.append("Переданный клиентом объект неверен!\n");
        }
        return false;
    }
}
