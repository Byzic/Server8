package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseHandlingException;
import exceptions.IncorrectValueException;
import exceptions.UserAlreadyExists;
import server.utility.DatabaseUserManager;
import server.utility.ResponseCreator;

public class RegisterCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;
    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }
    public boolean execute(String stringArgument, Flat flat, User user) {
        try {
            //System.out.println(user.getColor());
            //System.exit(0);
            if (!stringArgument.isEmpty() || flat != null) throw new IncorrectValueException();
            if (databaseUserManager.insertUser(user)) {
                //App.user_ID.add(databaseUserManager.getUserIdByUsername(user));
                ResponseCreator.appendln("Пользователь " +
                    user.getLogin() + " зарегистрирован.");}
            else throw new UserAlreadyExists();
            return true;
        } catch (IncorrectValueException exception) {
            ResponseCreator.appendln("Эммм...эээ.это внутренняя команда...(Неправильные аргументы)");
        } catch (ClassCastException exception) {
            ResponseCreator.error("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExists exception) {
            ResponseCreator.error("Пользователь " + user.getLogin() + " уже существует!");
        }
        return false;
    }
}
