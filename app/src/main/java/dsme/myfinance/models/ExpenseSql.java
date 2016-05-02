package dsme.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExpenseSql {
    private static final String TABLE = "EXPENSES";
    private static final String TABLE_CATEGORIES = "CATEGORIES";
    private static final String CATEGORY_ENTRY = "CATEGORY";
    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    private static final String REPEATING = "REPEATING";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
    private static final String IS_SAVED =  "IS_SAVED";
    private static final String NOTE = "NOTE";
    private static final String DATE = "DATE";

    public static void addExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, expense.getTimestamp());
        values.put(DATE, expense.getDate());
        values.put(NAME, expense.getExpenseName());
        values.put(CATEGORY, expense.getCategory());
        values.put(REPEATING, expense.getIsRepeatingExpense());
        values.put(IMAGE_PATH, expense.getExpenseImage());
        values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());
        values.put(NOTE, expense.getNote());
        values.put(IS_SAVED, 1);

        db.insert(TABLE, null, values);
    }

    public static void deleteExpense(ModelSql.MyOpenHelper dbHelper, String timeStamp) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SAVED, 0);
        db.update(TABLE, values, TIMESTAMP + " = '" + timeStamp + "'", null);
    }

    public static Expense getExpense(ModelSql.MyOpenHelper dbHelper, long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE + " WHERE " + TIMESTAMP + " = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String expenseName = cursor.getString(cursor.getColumnIndex(NAME));
            String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
            String imagePath = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
            long timeStamp = cursor.getLong(cursor.getColumnIndex(TIMESTAMP));
            long date = cursor.getLong(cursor.getColumnIndex(DATE));
            int repeating = cursor.getInt(cursor.getColumnIndex(REPEATING));
            float amount = cursor.getFloat(cursor.getColumnIndex(EXPENSE_AMOUNT));
            String note = cursor.getString(cursor.getColumnIndex(NOTE));

            Expense expense = new Expense(timeStamp, expenseName, date, repeating, imagePath, amount, category, note);

            cursor.close();
            return expense;
        }else{
            return null;
        }
    }

    public static void updateOrAddExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE + " WHERE " + TIMESTAMP + " = " + expense.getTimestamp();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put(NAME, expense.getExpenseName());
            values.put(CATEGORY, expense.getCategory());
            values.put(REPEATING, expense.getIsRepeatingExpense());
            values.put(TIMESTAMP, expense.getTimestamp());
            values.put(DATE, expense.getDate());
            values.put(IMAGE_PATH, expense.getExpenseImage());
            values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());
            values.put(NOTE, expense.getNote());

            db.update(TABLE, values, TIMESTAMP + " = '" + expense.getTimestamp() + "'", null);
        }else{
            addExpense(dbHelper, expense);
        }
    }

    public static void batchUpdateExpense(ModelSql.MyOpenHelper dbHelper, List<Expense> expenses, Model.BatchUpdateListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for(Expense expense : expenses) {
            if (getExpense(dbHelper, expense.getTimestamp()) == null) {
                addExpense(dbHelper, expense);
            } else {
                ContentValues values = new ContentValues();
                values.put(NAME, expense.getExpenseName());
                values.put(CATEGORY, expense.getCategory());
                values.put(REPEATING, expense.getIsRepeatingExpense());
                values.put(TIMESTAMP, expense.getTimestamp());
                values.put(DATE, expense.getDate());
                values.put(IMAGE_PATH, expense.getExpenseImage());
                values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());
                values.put(NOTE, expense.getNote());

                db.update(TABLE, values, TIMESTAMP + " = '" + expense.getTimestamp() + "'", null);
            }
        }
        listener.onResult();
    }

    public static List<Expense> getExpenses(ModelSql.MyOpenHelper dbHelper) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE +
                " WHERE " + IS_SAVED + " = " + " 1 " +
                " ORDER BY " + DATE + " DESC";;
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_timestamp = cursor.getColumnIndex(TIMESTAMP);
                int date_index = cursor.getColumnIndex(DATE);
                int name_index = cursor.getColumnIndex(NAME);
                int category_index = cursor.getColumnIndex(CATEGORY);
                int repeating_index = cursor.getColumnIndex(REPEATING);
                int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
                int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);
                int note_index = cursor.getColumnIndex(NOTE);

                do {
                    long timeStamp = cursor.getLong(id_timestamp);
                    String expenseName = cursor.getString(name_index);
                    long date = cursor.getLong(date_index);
                    String category = cursor.getString(category_index);
                    String imagePath = cursor.getString(image_path_index);
                    int repeating = cursor.getInt(repeating_index);
                    float amount = cursor.getFloat(amount_index);
                    String note = cursor.getString(note_index);

                    Expense expense = new Expense(timeStamp, expenseName, date, repeating, imagePath, amount, category, note);
                    data.add(expense);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static List<Expense> getExpensesByCategory(ModelSql.MyOpenHelper dbHelper, String selectedCategory,long fromDate, long toDate) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        if(selectedCategory == null && fromDate == 0 && toDate == 0){
            String query = "SELECT * FROM " + TABLE +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE + " DESC ";

            cursor = db.rawQuery(query, null);

        }else if(selectedCategory == null && toDate == 0){
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE +" DESC ";

            cursor = db.rawQuery(query, null);

        }else if(fromDate == 0 && toDate == 0) {
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                    " AND " + IS_SAVED + " = " + " 1 ";

            cursor = db.rawQuery(query, null);

        }else {
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                    " AND " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE +" DESC ";

            cursor = db.rawQuery(query, null);
        }

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_index = cursor.getColumnIndex(TIMESTAMP);
                int date_index = cursor.getColumnIndex(DATE);
                int name_index = cursor.getColumnIndex(NAME);
                int category_index = cursor.getColumnIndex(CATEGORY);
                int repeating_index = cursor.getColumnIndex(REPEATING);
                int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
                int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);
                int note_index = cursor.getColumnIndex(NOTE);

                do {
                    long timeStamp = cursor.getLong(id_index);
                    long date = cursor.getLong(date_index);
                    String expenseName = cursor.getString(name_index);
                    String category = cursor.getString(category_index);
                    String imagePath = cursor.getString(image_path_index);
                    int repeating = cursor.getInt(repeating_index);
                    float amount = cursor.getFloat(amount_index);
                    String note = cursor.getString(note_index);

                    Expense expense = new Expense(timeStamp, expenseName, date, repeating, imagePath, amount, category, note);
                    data.add(expense);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static List<Expense> getExpensesByMonth(ModelSql.MyOpenHelper dbHelper, long fromDate, long toDate) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String query;

        if(toDate == 0){
            query = "SELECT * FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE + " DESC ";

            cursor = db.rawQuery(query, null);

        }else {
            query = "SELECT * FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + DATE + " < " + "'" + toDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE + " DESC ";

            cursor = db.rawQuery(query, null);
        }

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_index = cursor.getColumnIndex(TIMESTAMP);
                int date_index = cursor.getColumnIndex(DATE);
                int name_index = cursor.getColumnIndex(NAME);
                int category_index = cursor.getColumnIndex(CATEGORY);
                int repeating_index = cursor.getColumnIndex(REPEATING);
                int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
                int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);
                int note_index = cursor.getColumnIndex(NOTE);

                do {
                    long timeStamp = cursor.getLong(id_index);
                    long date = cursor.getLong(date_index);
                    String expenseName = cursor.getString(name_index);
                    String category = cursor.getString(category_index);
                    String imagePath = cursor.getString(image_path_index);
                    int repeating = cursor.getInt(repeating_index);
                    float amount = cursor.getFloat(amount_index);
                    String note = cursor.getString(note_index);

                    Expense expense = new Expense(timeStamp, expenseName, date, repeating, imagePath, amount, category, note);
                    data.add(expense);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static List<String> getCategories(ModelSql.MyOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> allCategories = new ArrayList<>();

        String query = "SELECT DISTINCT " + CATEGORY +
                " FROM " + TABLE +
                " WHERE " + IS_SAVED + " = " + " 1 ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int category_index = cursor.getColumnIndex(CATEGORY);

            do {
                String category = cursor.getString(category_index);
                allCategories.add(category);
            } while (cursor.moveToNext());
        }

        return allCategories;
    }

    public static float getSumByCategory(ModelSql.MyOpenHelper dbHelper, String selectedCategory, long fromDate, long toDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        String query = "SELECT SUM(" + EXPENSE_AMOUNT + ")" + " FROM " + TABLE +
                " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                " AND " + DATE + " > " + "'" + fromDate + "'" +
                " AND " + IS_SAVED + " = " + " 1 ";

        cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            return cursor.getFloat(0);
        }else {
            return 0;
        }
    }

    public static float getSumByMonth(ModelSql.MyOpenHelper dbHelper, long fromDate, long toDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;
        Cursor cursor;

        if (toDate > 0 ) {
            query = "SELECT SUM(" + EXPENSE_AMOUNT + ")" + " FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + DATE + " < " + "'" + toDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1 ";
        }else {
            query = "SELECT SUM(" + EXPENSE_AMOUNT + ")" + " FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + IS_SAVED + " = " + " 1";
        }

        cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            return cursor.getFloat(0);
        }else {
            return 0;
        }
    }

    public static void addCategory(ModelSql.MyOpenHelper dbHelper, String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ENTRY, category);

        db.insert(TABLE_CATEGORIES, null, values);
    }

    public static List<String> getAllCategories(ModelSql.MyOpenHelper dbHelper) {
        List<String> data = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATEGORIES
                + " ORDER BY " + CATEGORY_ENTRY + " COLLATE NOCASE";
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_category = cursor.getColumnIndex(CATEGORY_ENTRY);

                do {
                    String category = cursor.getString(id_category);
                    data.add(category);
                } while (cursor.moveToNext());
            }
            return data;
        }else{
            addCategory(dbHelper, "Travel");
            addCategory(dbHelper, "Transportation");
            addCategory(dbHelper, "Food");
            addCategory(dbHelper, "Bills");

            return getAllCategories(dbHelper);
        }
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                TIMESTAMP + " LONG PRIMARY KEY," +
                DATE + " LONG," +
                NAME + " TEXT," +
                CATEGORY + " TEXT, " +
                IMAGE_PATH + " TEXT," +
                REPEATING + " INTEGER, " +
                NOTE + " TEXT, " +
                IS_SAVED + " INTEGER, " +
                EXPENSE_AMOUNT + " FLOAT" + ")");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                CATEGORY_ENTRY + " TEXT PRIMARY KEY" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }
}
