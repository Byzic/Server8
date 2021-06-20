package server.utility;

import common.User;
import exceptions.DatabaseHandlingException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//Класс, который работает с пользователями ии бд()
public class DatabaseUserManager {
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseManager.USER_TABLE + " (" +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN +
            ", "+ DatabaseManager.USER_TABLE_COLOR_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_ONLINE_COLUMN="UPDATE "+DatabaseManager.USER_TABLE+" SET "+DatabaseManager.USER_TABLE_ONLINE_COLUMN+" WHERE "+DatabaseManager.USER_TABLE_USERNAME_COLUMN+ " = ? AND "+DatabaseManager.USER_TABLE_PASSWORD_COLUMN+ " = ?";



    private DatabaseManager databaseManager;
    public DatabaseUserManager(DatabaseManager databaseManager){
        this.databaseManager=databaseManager;
    }
    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getLogin());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
    }
    public void setOnlineColumnTrue(User user){
        PreparedStatement preparedOnlineColumnStatement=null;
        try{
            preparedOnlineColumnStatement=databaseManager.getPreparedStatement(UPDATE_ONLINE_COLUMN,false);
            preparedOnlineColumnStatement.setString(2,user.getLogin());
            preparedOnlineColumnStatement.setString(3, user.getPassword());
            preparedOnlineColumnStatement.setBoolean(1, true);
        }catch (SQLException exception){

        } finally {
        databaseManager.closePreparedStatement(preparedOnlineColumnStatement);
        }
    }
    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement =
                    databaseManager.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getLogin());
            preparedInsertUserStatement.setString(2, user.getPassword());
            preparedInsertUserStatement.setString(3, user.getColor());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedInsertUserStatement);
        }
    }


    public int getUserIdByUsername(User user) throws DatabaseHandlingException {
        int userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getLogin());
            preparedSelectUserByUsernameStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt(DatabaseManager.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
    }

    public User getUserById(long userID) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseManager.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseManager.USER_TABLE_PASSWORD_COLUMN),
                        resultSet.getString(DatabaseManager.USER_TABLE_COLOR_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return user;
    }

}
