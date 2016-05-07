package dsme.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class MessageSql {

    private static final String TABLE_MESSAGES = "MESSAGES";
    private static final String MESSAGE_ID = "MESSAGE_ID";
    private static final String INCREMENT = "INCREMENT";
    private static final String SENDER = "SENDER";
    private static final String CHAR_ID= "CHAT_ID";
    private static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";

    public static void addMessage(ModelSql.MyOpenHelper dbHelper, Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, message.getMessageId());
        values.put(CHAR_ID, message.getChatId());
        values.put(SENDER, message.getSender());
        values.put(MESSAGE_CONTENT, message.getMessageContent());

        db.insert(TABLE_MESSAGES, null, values);
    }

    public static List<Message> getMessages(ModelSql.MyOpenHelper dbHelper) {
        List<Message> data = new LinkedList<Message>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_MESSAGES +
                " ORDER BY " + INCREMENT + " ASC";;
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_message = cursor.getColumnIndex(MESSAGE_ID);
                int id_chat = cursor.getColumnIndex(CHAR_ID);
                int content_index = cursor.getColumnIndex(MESSAGE_CONTENT);
                int sender_index= cursor.getColumnIndex(SENDER);

                do {
                    long messageId = cursor.getLong(id_message);
                    long chatId = cursor.getLong(id_chat);
                    String chatContent = cursor.getString(content_index);
                    String senderEmail = cursor.getString(sender_index);

                    Message message = new Message(messageId, chatId, chatContent, senderEmail);
                    data.add(message);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" +
                INCREMENT + " INTEGER PRIMARY KEY," +
                MESSAGE_ID + " LONG," +
                SENDER + " TEXT, " +
                MESSAGE_CONTENT + " TEXT" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_MESSAGES + ";");
    }
}
