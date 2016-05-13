package dsme.myfinance.models;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModelCloudDB {

    static String Tag = "expenses";
    static String name = "name";
    static String amount = "amount";
    static String picPath = "picPath";
    static String comments = "comments";
    static String category = "category";
    static String isRecurring = "isRecurring";
    static String created = "created";
    static String updatedAt = "updatedAt";
    static String user = "displayName";

    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";
    static JSONArray jArray = null;
    static String API_URL ="https://myfinance-mean.herokuapp.com/api/expenses";
    List<Expense> expensesArray;

    public class GetAllData extends AsyncTask<Void, Void, List<Expense> > {

        protected List<Expense> doInBackground(Void... params) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(API_URL);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONArray(json);
                //jArray = jObj.getJSONArray();
                expensesArray = new ArrayList<>();

                for (int i = 0; i < jObj.length(); i++){
                    JSONObject object = jObj.getJSONObject(i);
                    String expenseName = object.getString(name);
                    int expenseAmount = object.getInt(amount);
                    String picturePath = object.getString(picPath);
                    String note = object.getString(comments);
                    String expenseCategory = object.getString(category);
                    int isRepeating = object.getInt(isRecurring);
                    String userName = "Temp";

                    expensesArray.add(new Expense(12121212,
                            expenseName,
                            12121212,
                            isRepeating,
                            picturePath,
                            (float)expenseAmount,
                            category,
                            note));
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // return JSON String
            return expensesArray;
        }
    }

//    public boolean isConnected(){
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }

    public class addNewExpenseToCloud extends AsyncTask<Expense, Void, String> {
        @Override
        protected String doInBackground(Expense... expenses) {

            InputStream inputStream = null;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(API_URL);

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate(name, expenses[0].getExpenseName());
                jsonObject.accumulate(amount, expenses[0].getExpenseAmount());
                jsonObject.accumulate(picPath, expenses[0].getExpenseImage());
                jsonObject.accumulate(comments, expenses[0].getNote());
                jsonObject.accumulate(category, expenses[0].getCategory());
                jsonObject.accumulate(isRecurring, expenses[0].isRepeatingExpense);
                jsonObject.accumulate(user, "Moshe_a_Totah");

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 6. set httpPost Entity
                httpPost.setEntity(se);

                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            // 11. return result
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}


