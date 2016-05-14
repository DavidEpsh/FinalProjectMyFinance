package dsme.myfinance.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Model {

    interface ModelInterface{
        public void addExpense(Expense expense);
        public void deleteExpense(String expense);
        public Expense getExpense(String id);
        public List<Expense> getExpenses();
        public List<Expense> getExpensesByCategory(String category, long fromDate, long toDate);
        public List<Expense> getExpensesByMonth(long fromDate, long toDate);
        public float getSumByCategory(String category, long fromDate, long toDate);
        public float getSumByMonth(long fromDate, long toDate);
        public List<String> getCategories();
        public void addCategory(String category);
        public List<String> getAllCategories();
        public void updateOrAddExpense(Expense expense);
        public void batchUpdateExpenses(List<Expense> expenses);
        public void changeLastUpdateTime(ChangeTimeListener listener);
        public void getAllDataFromCloud();
        public void addMessage(MessageLocal message);
        public List<MessageLocal> getMessages();

        public void addUser(User user);
        public User getUser();
    }

    private static final Model instance = new Model();
    private ModelInterface modelImpl;
    private ModelCloudDB modelCloud = new ModelCloudDB();
    Context context;

    private Model(){
    }

    public void init(Context applicationContext) {
        this.context = applicationContext;
        modelImpl = new ModelSql(applicationContext);
    }

    public static Model instance(){
        return instance;
    }

    public Expense getExpense(String id){
        return modelImpl.getExpense(id);
    }

    public void addExpense(Expense expense){
        modelImpl.addExpense(expense);
    }

    public List<Expense> getExpenses(){
        return modelImpl.getExpenses();
    }

    public void updateOrAddExpense(Expense expense, boolean doDeleteExpense){
        modelImpl.updateOrAddExpense(expense);
        //modelParse.updateOrDelete(expense, doDeleteExpense);
    }

    public void batchUpdateExpenses(List<Expense> expenses){
        modelImpl.batchUpdateExpenses(expenses);
    }

    public List<String> getCategories(){
        return modelImpl.getCategories();
    }

    public void addCategory(String category){
        modelImpl.addCategory(category);
    }

    public List<String> getAllCategories(){
        return modelImpl.getAllCategories();
    }

    public List<Expense> getExpensesByCategory(String category, long fromDate, long toDate){
        return modelImpl.getExpensesByCategory(category, fromDate, toDate);
    }

    public List<Expense> getExpensesByMonth(long fromDate, long toDate){
        return modelImpl.getExpensesByMonth(fromDate, toDate);
    }

    public float getSumByCategory(String category, long fromDate, long toDate){
        return modelImpl.getSumByCategory(category, fromDate, toDate);
    }

    public float getSumByMonth(long fromDate, long toDate){
        return modelImpl.getSumByMonth(fromDate, toDate);
    }

    public List<MessageLocal> getMessages(){
        return  modelImpl.getMessages();
    }

    public void addMessage(MessageLocal message){
        modelImpl.addMessage(message);
    }

    public void getAllDataFromCloud(){
        ModelCloudDB.GetAllData task = new ModelCloudDB().new GetAllData();
        task.execute();
    }

    public interface ChangeTimeListener{
        public void onResult();
    }

    public interface BatchUpdateListener{
        public void onResult();
    }

    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }


    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to fin the image on the device
//                if (bmp == null) {                                      //if image not found - try downloading it from parse
//                    bmp = modelParse.loadImage(imageName);
//                    if (bmp != null) saveImageToFile(bmp,imageName);    //save the image locally for next time
//                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = context.getExternalFilesDir(null);
            out = new FileOutputStream(new File(dir,imageFileName));
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String fileName){
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = context.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(new File(dir,fileName));
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public void addUser(User user){
        modelImpl.addUser(user);
    }

    public User getUser(){
        return modelImpl.getUser();
    }
}


















