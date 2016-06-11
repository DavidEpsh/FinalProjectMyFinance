package dsme.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class UsersSql {

    private static final String TABLE_ADVISER = "ADVISER";
    private static final String ADVISER_ID = "ADVISER_ID";
    private static final String ADVISER_DISPLAY_NAME = "ADVISER_DISPLAY_NAME";

    private static final String TABLE_USER = "CUSTOMER";
    private static final String USER_ID = "USER_ID";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    private static final String SESSION_ID = "SESSION_ID";
    private static final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String USER_FIRST_NAME= "USER_FIRST_NAME";
    private static final String USER_USER_NAME= "USER_USER_NAME";

    public static void addCustomer(ModelSql.MyOpenHelper dbHelper, User.Customer user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ExpenseSql.deleteAll(db);

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getId());
        values.put(USER_DISPLAY_NAME, user.getDisplayName());
        values.put(USER_PHONE_NUMBER, user.getPhoneNumber());
        values.put(USER_EMAIL, user.getEmail());
        values.put(SESSION_ID, user.getSessionId());
        values.put(USER_FIRST_NAME, user.getFirstName());
        values.put(USER_LAST_NAME, user.getLastName());
        values.put(USER_USER_NAME, user.getUserName());
        values.put(ADVISER_DISPLAY_NAME, user.getAdviserName());
        values.put(ADVISER_ID, user.getAdviserId());

        db.insert(TABLE_USER, null, values);
    }

    public static User.Customer getCustomer(ModelSql.MyOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_userID = cursor.getColumnIndex(USER_ID);
                int id_userEmail = cursor.getColumnIndex(USER_EMAIL);
                int id_userPhone = cursor.getColumnIndex(USER_PHONE_NUMBER);
                int id_sessionId = cursor.getColumnIndex(SESSION_ID);
                int id_displayName = cursor.getColumnIndex(USER_DISPLAY_NAME);
                int id_firstName = cursor.getColumnIndex(USER_FIRST_NAME);
                int id_lastName = cursor.getColumnIndex(USER_LAST_NAME);
                int id_userName = cursor.getColumnIndex(USER_USER_NAME);
                int id_adviserName = cursor.getColumnIndex(ADVISER_DISPLAY_NAME);
                int id_adviserId = cursor.getColumnIndex(ADVISER_ID);

                String userID = cursor.getString(id_userID);
                String userEmail = cursor.getString(id_userEmail);
                String userPhone = cursor.getString(id_userPhone);
                String sessionId = cursor.getString(id_sessionId);
                String displayName = cursor.getString(id_displayName);
                String firstName = cursor.getString(id_firstName);
                String lastName = cursor.getString(id_lastName);
                String userName = cursor.getString(id_userName);
                String adviserName = cursor.getString(id_adviserName);
                String adviserId = cursor.getString(id_adviserId);

                return new User.Customer(userID, displayName, firstName, lastName, userName, userEmail, userPhone, sessionId,null, adviserName, adviserId);
            }
        }
        return null;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " TEXT PRIMARY KEY," +
                USER_DISPLAY_NAME + " TEXT," +
                USER_PHONE_NUMBER +" TEXT," +
                USER_FIRST_NAME +" TEXT," +
                USER_LAST_NAME +" TEXT," +
                USER_USER_NAME +" TEXT," +
                SESSION_ID +" TEXT," +
                USER_EMAIL + " TEXT," +
                ADVISER_DISPLAY_NAME +" TEXT," +
                ADVISER_ID + " TEXT" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_USER + ";");
    }

}

