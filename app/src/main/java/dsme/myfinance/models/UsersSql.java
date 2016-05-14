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

                String userID = cursor.getString(id_userID);
                String userEmail = cursor.getString(id_userEmail);
                return new User(userID, userEmail);
            }
        }
        return null;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " TEXT PRIMARY KEY," +
                USER_EMAIL + " TEXT" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_USER + ";");
    }
}

