package server.commands;


import common.User;
import common.data.Flat;
import exceptions.IncorrectValueException;
import server.utility.CollectionManager;
import server.utility.DatabaseUserManager;
import server.utility.ResponseCreator;

/**
* Класс для команды "exit". Проверяет аргумент и дальше ничего не делает
 **/
public class ExitCommand extends AbstractCommand {
    CollectionManager collectionManager;
    DatabaseUserManager databaseUserManager;
        public ExitCommand(CollectionManager collectionManager,DatabaseUserManager databaseUserManager){
            super("exit","завершить программу ")  ;
            this.collectionManager=collectionManager;
            this.databaseUserManager=databaseUserManager;

        }
        /**
         * Выполнение команды
         * @param argument аргумент
         * @return состояние выполнения команды
         */
        @Override
        public boolean execute(String argument, Flat flat, User user){
            try {
                if (!argument.isEmpty())throw new IncorrectValueException();
                //App.user_ID.remove(App.user_ID.indexOf(databaseUserManager.getUserIdByUsername(user)));
                ResponseCreator.appendln("Клиент "+user.getLogin()+" завершил свою работу");

                return true;
            }
            catch (IncorrectValueException  e) {
                ResponseCreator.error("ExitError");

            }/*catch(DatabaseHandlingException e){
                ResponseCreator.error("Возникла проблема с обращением к бд");
            }*/
            return false;
        }
    }

