package server.commands;

import common.User;
import common.data.Flat;
import exceptions.DatabaseHandlingException;
import exceptions.IncorrectValueException;
import exceptions.RecurringUserException;
import exceptions.UserIsNotFoundException;
import server.App;
import server.utility.DatabaseUserManager;
import server.utility.ResponseCreator;


public class LoginCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;
    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login",  "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }
    @Override
    public boolean execute(String stringArgument, Flat flat, User user) {
        try {
            if (!stringArgument.isEmpty() || flat != null) throw new IncorrectValueException();
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) {
                if (App.user_ID.contains(databaseUserManager.getUserIdByUsername(user))){
                    throw new RecurringUserException();
                }else{
                    //App.user_ID.add(databaseUserManager.getUserIdByUsername(user));
               // databaseUserManager.setOnlineColumn(user);
                ResponseCreator.appendln("Пользователь " +
                    user.getLogin() + " авторизован.");}}
            else throw new UserIsNotFoundException();
            return true;
        } catch (IncorrectValueException exception) {
            ResponseCreator.appendln("Эммм...эээ.это внутренняя команда...(Неправильные аргументы)");
        } catch (ClassCastException exception) {
            ResponseCreator.error("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseCreator.error("Произошла ошибка при обращении к базе данных!");
        } catch (UserIsNotFoundException exception) {
            ResponseCreator.appendln("UserIsNotFoundException");
        }catch (RecurringUserException e){
            ResponseCreator.error("Данный пользователь уже авторизован.");
        }
        return false;
    }
}
