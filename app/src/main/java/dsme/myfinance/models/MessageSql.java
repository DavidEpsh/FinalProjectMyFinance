package dsme.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class MessageSql {

    private static final String TABLE_MESSAGES = "MESSAGES";
    private static final String MESSAGE_ID = "MESSAGE_ID";
    private static final String DATE = "DATE";
    private static final String SENDER_ID = "SENDER_ID";
    private static final String RECEPIENT_ID = "RECEPIENT_ID";
    private static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";

    public static void addMessage(ModelSql.MyOpenHelper dbHelper, MessageLocal message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (!isMessageExists(dbHelper, message.getMessageId())) {

            ContentValues values = new ContentValues();
            values.put(MESSAGE_ID, message.getMessageId());
            values.put(RECEPIENT_ID, message.getRecepeintId());
            values.put(SENDER_ID, message.getSenderId());
            values.put(MESSAGE_CONTENT, message.getMessageContent());
            values.put(DATE, message.getDate());

            db.insert(TABLE_MESSAGES, null, values);
        }
    }

    public static List<MessageLocal> getMessages(ModelSql.MyOpenHelper dbHelper) {
        List<MessageLocal> data = new LinkedList<MessageLocal>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MESSAGES +
                " ORDER BY " + DATE + " ASC";;
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_message = cursor.getColumnIndex(MESSAGE_ID);
                int id_recepient = cursor.getColumnIndex(RECEPIENT_ID);
                int id_content = cursor.getColumnIndex(MESSAGE_CONTENT);
                int id_sender= cursor.getColumnIndex(SENDER_ID);
                int id_date = cursor.getColumnIndex(DATE);

                do {
                    String messageId = cursor.getString(id_message);
                    String senderId = cursor.getString(id_sender);
                    String chatContent = cursor.getString(id_content);
                    String recepientId = cursor.getString(id_recepient);
                    String date = cursor.getString(id_date);

                    MessageLocal message = new MessageLocal(messageId,senderId, recepientId, chatContent, date);
                    data.add(message);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static boolean isMessageExists(ModelSql.MyOpenHelper dbHelper, String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + MESSAGE_ID + " = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" +
                MESSAGE_ID + " TEXT PRIMARY KEY," +
                DATE + " TEXT," +
                SENDER_ID + " TEXT, " +
                RECEPIENT_ID + " TEXT, " +
                MESSAGE_CONTENT + " TEXT" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_MESSAGES + ";");
    }
}
