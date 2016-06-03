package dsme.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class UsersSql {

    private static final String TABLE_ADVISER = "ADVISER";
    private static final String ADVISER_ID = "ADVISER_ID";


    private static final String TABLE_USER = "USER";
    private static final String USER_ID = "USER_ID";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    private static final String SESSION_ID = "SESSION_ID";
    private static final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";

    public static void addUser(ModelSql.MyOpenHelper dbHelper, User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getId());
        values.put(USER_EMAIL, user.getDisplayName());

        db.execSQL("DELETE FROM "+ TABLE_USER);
        db.insert(TABLE_USER, null, values);
    }

    public static User getUser(ModelSql.MyOpenHelper dbHelper) {
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

                String userID = cursor.getString(id_userID);
                String userEmail = cursor.getString(id_userEmail);
                String userPhone = cursor.getString(id_userPhone);
                String sessionId = cursor.getString(id_sessionId);
                String displayName = cursor.getString(id_displayName);
                return new User(userID, displayName, userEmail, userPhone, sessionId);
            }
        }
        return null;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " TEXT PRIMARY KEY," +
                USER_DISPLAY_NAME + " TEXT," +
                USER_PHONE_NUMBER +" TEXT," +
                SESSION_ID +" TEXT," +
                USER_EMAIL + " TEXT" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_USER + ";");
    }
}

