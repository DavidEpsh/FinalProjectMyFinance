package dsme.myfinance.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;

public class ModelSql implements Model.ModelInterface {
    private MyOpenHelper dbHelper;

    public ModelSql(Context applicationContext) {
        dbHelper = new MyOpenHelper(applicationContext);
    }

    @Override
    public void addExpense(Expense expense) {
        ExpenseSql.addExpense(dbHelper, expense);
    }

    @Override
    public void deleteExpense(String expense) {
        ExpenseSql.deleteExpense(dbHelper, expense);
    }

    @Override
    public Expense getExpense(long id) {
        return  ExpenseSql.getExpense(dbHelper, id);
    }

    @Override
    public void updateOrAddExpense(Expense expense) {
        ExpenseSql.updateOrAddExpense(dbHelper, expense);
    }

    @Override
    public void batchUpdateExpenses(List<Expense> expenses, Model.BatchUpdateListener listener) {
        ExpenseSql.batchUpdateExpense(dbHelper, expenses, listener);
    }

    @Override
    public String getExistingUsersSheet(String id, String userName) {
        return null;
    }

    @Override
    public List<Expense> getExpenses() {
        return ExpenseSql.getExpenses(dbHelper);
    }

    @Override
    public List<String> getCategories() {
        return ExpenseSql.getCategories(dbHelper);
    }

    @Override
    public List<String> getAllCategories() {
        return ExpenseSql.getAllCategories(dbHelper);
    }
    @Override
    public void addCategory(String category){
        ExpenseSql.addCategory(dbHelper, category);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category, long fromDate, long toDate) {
        return ExpenseSql.getExpensesByCategory(dbHelper, category, fromDate, toDate);
    }

    public List<Expense> getExpensesByMonth(long fromDate, long toDate) {
        return ExpenseSql.getExpensesByMonth(dbHelper, fromDate, toDate);
    }

    @Override
    public float getSumByCategory(String category, long fromDate, long toDate) {
        return ExpenseSql.getSumByCategory(dbHelper, category, fromDate, toDate);
    }

    @Override
    public float getSumByMonth(long fromDate, long toDate) {
        return ExpenseSql.getSumByMonth(dbHelper, fromDate, toDate);
    }

    @Override
    public void changeLastUpdateTime(Model.ChangeTimeListener listener) {
        changeLastUpdateTime(listener);
    }

    @Override
    public HashMap<String, String> returnMySheets() {
        return null;
    }

    class MyOpenHelper extends SQLiteOpenHelper {
        final static String dbName = "database.db";
        final static int version = 1;

        public MyOpenHelper(Context context) {
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ExpenseSql.create(db);
            //ModelUsersAndAccountsSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ExpenseSql.drop(db);
            //ModelUsersAndAccountsSql.drop(db);
            onCreate(db);
        }
    }
}
