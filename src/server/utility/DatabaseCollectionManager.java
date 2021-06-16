package server.utility;

import common.User;
import common.data.*;
import exceptions.DatabaseHandlingException;
import exceptions.DatabaseManagerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Hashtable;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_FLAT = "SELECT * FROM " + DatabaseManager.FLAT_TABLE;
    private final String SELECT_ALL_HOUSE = "SELECT * FROM " + DatabaseManager.HOUSE_TABLE;
    private final String SELECT_HOUSE_BY_ID = SELECT_ALL_HOUSE + " WHERE " + DatabaseManager.HOUSE_TABLE_ID_COLUMN + " =?";
    private final String SELECT_FLAT_BY_ID = SELECT_ALL_FLAT + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_FLAT_BY_ID_AND_USER_ID = SELECT_FLAT_BY_ID + " AND " +
            DatabaseManager.FLAT_TABLE_USER_ID_COLUMN + " = ?";
    private final String DELETE_FLAT_BY_ID = "DELETE FROM " + DatabaseManager.FLAT_TABLE +
            " WHERE " + DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_HOUSE_BY_ID = "DELETE FROM " + DatabaseManager.HOUSE_TABLE+
            " WHERE " + DatabaseManager.HOUSE_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_HOUSE = "INSERT INTO " +
            DatabaseManager.HOUSE_TABLE + " (" +
            DatabaseManager.HOUSE_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.HOUSE_TABLE_YEAR_COLUMN + ", " +
            DatabaseManager.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN+ ") VALUES (?, ?, ?)";
    private final String INSERT_FLAT = "INSERT INTO " +
            DatabaseManager.FLAT_TABLE + " (" +
            DatabaseManager.FLAT_TABLE_KEY_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_COORDINATES_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_AREA_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_FURNISH_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_VIEW_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_USER_ID_COLUMN + ")  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_FLAT_NAME_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATES_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_COORDINATES_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_AREA_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_AREA_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_FURNISH_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_FURNISH_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_VIEW_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_VIEW_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_TRANSPORT_BY_ID="UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_HOUSE_BY_ID="UPDATE "+ DatabaseManager.HOUSE_TABLE+" SET "+DatabaseManager.HOUSE_TABLE_NAME_COLUMN+ " = ?, " +
            DatabaseManager.HOUSE_TABLE_YEAR_COLUMN+ " = ?, " +DatabaseManager.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN+ " = ?" +" WHERE " +
            DatabaseManager.HOUSE_TABLE_ID_COLUMN+" = ?";
    private final String UPDATE_HOUSE_ID="UPDATE "+DatabaseManager.FLAT_TABLE+" SET "+DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN+
            " =? WHERE "+DatabaseManager.HOUSE_TABLE_ID_COLUMN+" =?";




    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;
    public DatabaseCollectionManager(DatabaseManager dm,DatabaseUserManager dum){
        this.databaseManager=dm;
        this.databaseUserManager=dum;
    }
    private Flat returnFlat(ResultSet resultSet, int id) throws SQLException{
        String name = resultSet.getString(DatabaseManager.FLAT_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinates(resultSet.getString(DatabaseManager.FLAT_TABLE_COORDINATES_COLUMN));
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseManager.FLAT_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        Float area=resultSet.getFloat(DatabaseManager.FLAT_TABLE_AREA_COLUMN);
        Integer numberOfRooms=resultSet.getInt(DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN);
        Furnish furnish=Furnish.valueOf(resultSet.getString(DatabaseManager.FLAT_TABLE_FURNISH_COLUMN));
        View view= View.valueOf(resultSet.getString(DatabaseManager.FLAT_TABLE_VIEW_COLUMN));
        Transport transport=Transport.valueOf(resultSet.getString(DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN));
        Integer houseID=resultSet.getInt(DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN);
        House house;
        if(houseID!=0){
           house=getHouseById(houseID);
        }else{
            house=null;
        }

        //Integer houseId=resultSet.getInt(DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN);
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.FLAT_TABLE_USER_ID_COLUMN));
        return new Flat(id,name,coordinates,creationDate,area,numberOfRooms,furnish,view,transport,house,owner);

        

    }
    public void updateFlatByID(int ID, Flat flat) throws DatabaseManagerException {
        PreparedStatement updateFlatName = null;
        PreparedStatement updateFlatCoordinates = null;
        PreparedStatement updateFlatArea = null;
        PreparedStatement updateFlatNumberOfRooms = null;
        PreparedStatement updateFlatFurnish = null;
        PreparedStatement updateFlatView = null;
        PreparedStatement updateFlatTransport = null;
        PreparedStatement updateFlatHouse= null;
        PreparedStatement updateFlatHouse2=null;
        PreparedStatement deleteHouse = null;
        PreparedStatement insertHouse = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            updateFlatName= databaseManager.getPreparedStatement(UPDATE_FLAT_NAME_BY_ID, false);
            updateFlatCoordinates = databaseManager.getPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            updateFlatArea = databaseManager.getPreparedStatement(UPDATE_FLAT_AREA_BY_ID, false);
            updateFlatNumberOfRooms = databaseManager.getPreparedStatement(UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID, false);
            updateFlatFurnish = databaseManager.getPreparedStatement(UPDATE_FLAT_FURNISH_BY_ID, false);
            updateFlatView = databaseManager.getPreparedStatement(UPDATE_FLAT_VIEW_BY_ID, false);
            updateFlatTransport = databaseManager.getPreparedStatement(UPDATE_FLAT_TRANSPORT_BY_ID, false);
            updateFlatHouse = databaseManager.getPreparedStatement(UPDATE_HOUSE_BY_ID, false);
            updateFlatHouse2=databaseManager.getPreparedStatement(UPDATE_HOUSE_ID,false);

            updateFlatName.setString(1, flat.getName());
            updateFlatName.setInt(2, ID);
            if (updateFlatName.executeUpdate() == 0) throw new SQLException();
            updateFlatCoordinates.setString(1,flat.getCoordinates().tostring());
            updateFlatCoordinates.setInt(2, ID);
            if (updateFlatCoordinates.executeUpdate() == 0) throw new SQLException();
            updateFlatArea.setFloat(1,flat.getArea());
            updateFlatArea.setInt(2, ID);
            if (updateFlatArea.executeUpdate() == 0) throw new SQLException();
            updateFlatNumberOfRooms.setInt(1,flat.getNumberOfRooms());
            updateFlatNumberOfRooms.setInt(2, ID);
            if (updateFlatNumberOfRooms.executeUpdate() == 0) throw new SQLException();
            updateFlatFurnish.setString(1,flat.getFurnish().toString());
            updateFlatFurnish.setInt(2, ID);
            if (updateFlatFurnish.executeUpdate() == 0) throw new SQLException();
            updateFlatView.setString(1,flat.getView().toString());
            updateFlatView.setInt(2,ID);
            if (updateFlatView.executeUpdate() == 0) throw new SQLException();
            updateFlatTransport.setString(1,flat.getTransport().toString());
            updateFlatTransport.setInt(2,ID);
            if (Integer.valueOf(getHouseIdByFlatID(flat.getID()))!=0){
                if (flat.getHouse()!=null){

                    updateFlatHouse.setString(1,flat.getHouse().getName());
                    updateFlatHouse.setLong(2,flat.getHouse().getYear());
                    updateFlatHouse.setLong(3,flat.getHouse().getNumberOfFloors());
                    updateFlatHouse.setInt(4,getHouseIdByFlatID(ID));
                    if (updateFlatHouse.executeUpdate() == 0) throw new SQLException();
                }else{

                    deleteHouse = databaseManager.getPreparedStatement(DELETE_HOUSE_BY_ID, false);
                    deleteHouse.setInt(1, getHouseIdByFlatID(flat.getID()));
                    if (deleteHouse.executeUpdate() == 0) throw new SQLException();
                    updateFlatHouse2.setNull(1, java.sql.Types.INTEGER);
                    updateFlatHouse2.setInt(2,flat.getID());
                    if (updateFlatHouse2.executeUpdate() == 0) throw new SQLException();
                }

            }else{
                if (flat.getHouse()!=null){
                    Integer houseID;
                    insertHouse = databaseManager.getPreparedStatement(INSERT_HOUSE, true);
                    insertHouse.setString(1, flat.getHouse().getName());
                    insertHouse.setLong(2, flat.getHouse().getYear());
                    insertHouse.setLong(3, flat.getHouse().getNumberOfFloors());
                    if (insertHouse.executeUpdate() == 0) throw new SQLException();
                    ResultSet resultSetChapter = insertHouse.getGeneratedKeys();
                    if (resultSetChapter.next()) houseID = resultSetChapter.getInt(1);
                    else throw new SQLException();

                    updateFlatHouse2.setInt(1, houseID);
                    updateFlatHouse2.setInt(2,flat.getID());
                    if (updateFlatHouse2.executeUpdate() == 0) throw new SQLException();
                }

            }

            databaseManager.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(updateFlatName);
            databaseManager.closePreparedStatement(updateFlatCoordinates);
            databaseManager.closePreparedStatement(updateFlatArea);
            databaseManager.closePreparedStatement(updateFlatNumberOfRooms);
            databaseManager.closePreparedStatement(updateFlatFurnish);
            databaseManager.closePreparedStatement(updateFlatView);
            databaseManager.closePreparedStatement(updateFlatTransport);
            databaseManager.closePreparedStatement(updateFlatHouse);
            databaseManager.closePreparedStatement(updateFlatHouse2);
            databaseManager.closePreparedStatement(deleteHouse);
            databaseManager.closePreparedStatement(insertHouse);
            databaseManager.setAutoCommit();
        }
    }



    private House getHouseById(int id)throws SQLException{
        House house;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_HOUSE_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                house = new House(
                        resultSet.getString(DatabaseManager.HOUSE_TABLE_NAME_COLUMN),
                        resultSet.getLong(DatabaseManager.HOUSE_TABLE_YEAR_COLUMN),
                        resultSet.getLong(DatabaseManager.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_HOUSE_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return house;
    }


    private Coordinates getCoordinates(String str) throws SQLException {
        String[] xy={"",""};
        xy=str.split(" ",2);
        Coordinates coordinates=new Coordinates( Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
        return coordinates;
    }
    public boolean checkFlatByIdAndUserId(int flatID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_FLAT_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, flatID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | DatabaseHandlingException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_SPACE_MARINE_BY_ID_AND_USER_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }
    public void deleteFlatById(int flatID) throws DatabaseManagerException{
        PreparedStatement deleteFlat = null;
        PreparedStatement deleteHouse = null;
        try {
            int houseID = getHouseIdByFlatID(flatID);
            deleteFlat = databaseManager.getPreparedStatement(DELETE_FLAT_BY_ID, false);
            deleteFlat.setInt(1, flatID);
            if (deleteFlat.executeUpdate() == 0) throw new SQLException();
            if (houseID!=0){
            deleteHouse = databaseManager.getPreparedStatement(DELETE_HOUSE_BY_ID, false);
            deleteHouse.setInt(1, houseID);
            if (deleteHouse.executeUpdate() == 0) throw new SQLException();}
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса DELETE_FLAT_BY_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteFlat);
            databaseManager.closePreparedStatement(deleteHouse);
        }
    }




    public Hashtable<Integer, Flat> getCollection() {
        Hashtable<Integer, Flat> hashtable = new Hashtable<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_ALL_FLAT, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(DatabaseManager.FLAT_TABLE_ID_COLUMN);
                int key = resultSet.getInt(DatabaseManager.FLAT_TABLE_KEY_COLUMN);
                hashtable.put(key, returnFlat(resultSet, id));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return hashtable;
    }
    public boolean checkFlatUserId(int spaceMarineID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_FLAT_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, spaceMarineID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | DatabaseHandlingException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_SPACE_MARINE_BY_ID_AND_USER_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    //удаляет по id квартиру и сопутсвующие ей
    private void deleteSpaceMarineById(int flatId) throws DatabaseManagerException{
        PreparedStatement deleteFlat = null;
        PreparedStatement deleteHouse = null;
        try {
            int houseId = getHouseIdByFlatID(flatId);
            deleteFlat = databaseManager.getPreparedStatement(DELETE_FLAT_BY_ID, false);
            deleteFlat.setInt(1, flatId);
            if (deleteFlat.executeUpdate() == 0) throw new SQLException();
            deleteHouse = databaseManager.getPreparedStatement(DELETE_HOUSE_BY_ID, false);
            deleteHouse.setInt(1, houseId);
            if (deleteHouse.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса DELETE_MARINE_BY_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteFlat);
            databaseManager.closePreparedStatement(deleteHouse);
        }
    }

    //получаем id дома по id квартиры
    private int getHouseIdByFlatID(int flatID) throws SQLException{
        int houseID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.getPreparedStatement(SELECT_FLAT_BY_ID, false);
            preparedStatement.setInt(1, flatID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                houseID = resultSet.getInt(DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_FLAT_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return houseID;

    }

    public Flat insertFlat(Flat flat,User user, int key) throws DatabaseManagerException{
        Flat flatToInsert;
        PreparedStatement insertFlat = null;
        PreparedStatement insertHouse = null;
        try {
            databaseManager.setCommit();//убираем автокомит
            databaseManager.setSavepoint();//сохраняем состояниебд
            Integer houseID=null;
            LocalDateTime localDateTime = LocalDateTime.now();
            if (flat.getHouse()!=null){
            insertHouse = databaseManager.getPreparedStatement(INSERT_HOUSE, true);
            insertHouse.setString(1, flat.getHouse().getName());
            insertHouse.setLong(2, flat.getHouse().getYear());
            insertHouse.setLong(3, flat.getHouse().getNumberOfFloors());
            if (insertHouse.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetChapter = insertHouse.getGeneratedKeys();

            if (resultSetChapter.next()) houseID = resultSetChapter.getInt(1);
            else throw new SQLException();}



            insertFlat = databaseManager.getPreparedStatement(INSERT_FLAT, true);
            insertFlat.setInt(1, key);
            insertFlat.setString(2, flat.getName());
            insertFlat.setString(3, flat.getCoordinates().tostring());
            insertFlat.setTimestamp(4, Timestamp.valueOf(localDateTime));//!в бд этот стобец строка(не помню точно)
            insertFlat.setFloat(5, flat.getArea());
            insertFlat.setInt(6, flat.getNumberOfRooms());
            insertFlat.setString(7, flat.getFurnish().toString());
            insertFlat.setString(8, flat.getView().toString());
            insertFlat.setString(9, flat.getTransport().toString());
            if (houseID==null){
            insertFlat.setNull(10, java.sql.Types.INTEGER);
            }else{insertFlat.setInt(10, houseID);}
            insertFlat.setInt(11, databaseUserManager.getUserIdByUsername(user));
            if (insertFlat.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetFlat = insertFlat.getGeneratedKeys();
            int flatID;
            if (resultSetFlat.next()) flatID = resultSetFlat.getInt(1);
            else throw new SQLException();
            flatToInsert = new Flat(
                    flatID,
                    flat.getName(),
                    flat.getCoordinates(),
                    localDateTime,
                    flat.getArea(),
                    flat.getNumberOfRooms(),
                    flat.getFurnish(),
                    flat.getView(),
                    flat.getTransport(),
                    flat.getHouse(),
                    user
            );
            databaseManager.commit();
            return flatToInsert;
        } catch (SQLException | DatabaseHandlingException exception) {
            System.out.println("Произошла ошибка при добавлении нового объекта в БД!");
            exception.printStackTrace();
            databaseManager.rollback();//вернулись к сохраненному состоянию бд
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(insertFlat);
            databaseManager.closePreparedStatement(insertHouse);
            databaseManager.setAutoCommit();
        }
    }


    public void clearCollection() throws DatabaseManagerException{
        Hashtable<Integer, Flat> hashtable = getCollection();
        for (Flat f : hashtable.values()) {
            deleteSpaceMarineById(f.getID());
        }
    }
}

