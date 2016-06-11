package dsme.myfinance.models;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dsme.myfinance.api.JSONParser;

public class ModelCloudDB {

    static String Tag = "expenses";
    static String id = "_id";
    static String name = "name";
    static String amount = "amount";
    static String picPath = "picPath";
    static String comments = "comments";
    static String category = "category";
    static String isRecurring = "isRecurring";
    static String created = "created";
    static String updatedAt = "updatedAt";
    static String date = "expenseDate";

    static JSONObject current_user = null;
    static String user = "user";
    static String user_email = "email";
    static String user_display_name = "displayName";
    static String user_phone_number = "phoneNumber";
    static String user_first_name= "firstName";
    static String user_last_name= "lastName";
    static String session_id = "session_id";
    static String user_user_name= "username";
    static String user_password= "password";
    static String roles= "roles";
    static String adviser_description= "description";
    static String adviser_profile_image= "profileImageURL";


    static InputStream is = null;
    static JSONArray jsonArray = null;
    static String json = "";
    static String API_URL ="https://myfinance-mean.herokuapp.com/api/expenses";
    static String API_URL_USERS ="https://myfinance-mean.herokuapp.com/api/users";
    static String API_URL_LOGIN ="https://myfinance-mean.herokuapp.com/api/auth/signin";
    static String API_URL_SIGNUP ="https://myfinance-mean.herokuapp.com/api/auth/signup";
    static String API_URL_USERS_EXPENSES ="https://myfinance-mean.herokuapp.com/api/user-expenses/";
    static String API_URL_ADVISER_PIC ="https://myfinance-mean.herokuapp.com/modules/users/client/img/profile/uploads/";

    List<Expense> expensesArray;

    public class GetAllData2 extends AsyncTask<Void, Void, String > {

        protected String doInBackground(Void... params) {

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

            Model.instance().batchUpdateExpenses(convertToExpenses(json));
            return "Success!";
        }
    }

    public class deleteExpense extends AsyncTask<Expense, Void, String> {

        @Override
        protected String doInBackground(Expense... expenses) {


            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(API_URL);

                httpDelete.setHeader("id", expenses[0].getMongoId());
                HttpResponse httpResponse = httpclient.execute(httpDelete);
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                inputStream, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        inputStream.close();
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }

                    Model.instance().addExpense(convertToSingleExpense(json));
                }

                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
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

//    public class GetAllUsers extends AsyncTask<Void, Void, List<Expense> > {
//
//        protected List<Expense> doInBackground(Void... params) {
//
//            // Making HTTP request
//            try {
//                // defaultHttpClient
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(API_URL_USERS);
//
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(
//                        is, "iso-8859-1"), 8);
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                is.close();
//                json = sb.toString();
//            } catch (Exception e) {
//                Log.e("Buffer Error", "Error converting result " + e.toString());
//            }
//
//            // try parse the string to a JSON object
//            try {
//                jsonArray = new JSONArray(json);
//                //jArray = jsonArray.getJSONArray();
//                expensesArray = new ArrayList<>();
//
//                for (int i = 0; i < jsonArray.length(); i++){
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String mongoId = object.getString(id);
//                    String expenseName = object.getString(name);
//                    int expenseAmount = object.getInt(amount);
//                    String picturePath = object.getString(picPath);
//                    String note = object.getString(comments);
//                    String expenseCategory = object.getString(category);
//                    int isRepeating = object.getInt(isRecurring);
//                    String userName = "Temp";
//
//                    expensesArray.add(new Expense(12121212,
//                            expenseName,
//                            12121212,
//                            isRepeating,
//                            picturePath,
//                            (float)expenseAmount,
//                            category,
//                            note));
//                }
//            } catch (JSONException e) {
//                Log.e("JSON Parser", "Error parsing data " + e.toString());
//            }
//
//            // return JSON String
//            return expensesArray;
//        }
//    }

    public List<Expense> convertToExpenses(String input){
        try {

            jsonArray = new JSONArray(input);
            //jArray = jsonArray.getJSONArray();
            expensesArray = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String mongoId = object.getString(id);
                long expenseDate = object.getLong(date);
                String expenseName = object.getString(name);
                int expenseAmount = object.getInt(amount);
                String picturePath = object.getString(picPath);
                String note = object.getString(comments);
                String expenseCategory = object.getString(category);
                int isRepeating = object.getInt(isRecurring);

                expensesArray.add(new Expense(mongoId,
                        expenseName,
                        expenseDate,
                        isRepeating,
                        picturePath,
                        (float)expenseAmount,
                        expenseCategory,
                        note));
            }
        } catch (JSONException e) {
            Log.e("JSON Parser #1", "Error parsing data " + e.toString());
        }

        // return JSON String
        return expensesArray;
    }

    public User.Customer convertToCustomer(JSONObject input) {

        JSONObject adviser;
        User.Customer currentUser;

        try {
            adviser = input.getJSONObject("advisor");
        }catch (JSONException e) {
            Log.e("JSON Parser #1", "Error parsing data " + e.toString());
            return null;
        }
        try {
            String adviserId = null;
            String adviserName = null;

            if (adviser != null){
                adviserId = adviser.getString(id);
                adviserName = adviser.getString(user_display_name);
            }

            String userId = input.getString(id);
            String phone = input.getString(user_phone_number);
            String email = input.getString(user_email);
            String sessionID = input.getString(session_id);
            String displayName = input.getString(user_display_name);
            String firstName = input.getString(user_first_name);
            String lastName = input.getString(user_last_name);
            String userName = input.getString(user_user_name);

            currentUser = new User.Customer(userId, displayName,firstName, lastName, userName, email, phone, sessionID, null, adviserName, adviserId);

        } catch (JSONException e) {
            Log.e("JSON Parser #1", "Error parsing data " + e.toString());
            return null;
        }

        // return JSON String
        return currentUser;
    }

    public Expense convertToSingleExpense(String input){

        JSONObject object = null;

        try {
            object = new JSONObject(input);

            String mongoId = object.getString(id);
            String expenseName = object.getString(name);
            int expenseAmount = object.getInt(amount);
            long expenseDate = object.getLong(date);
            String picturePath = object.getString(picPath);
            String note = object.getString(comments);
            String expenseCategory = object.getString(category);
            int isRepeating = object.getInt(isRecurring);
            String userName = "Temp";

            return new Expense(mongoId,
                    expenseName,
                    expenseDate,
                    isRepeating,
                    picturePath,
                    (float)expenseAmount,
                    expenseCategory,
                    note);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public class LogIn extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject user = new JSONObject();
                user.put("username", args[0]);
                user.put("password", args[1]);

                JSONObject json = jsonParser.makeHttpRequest(
                        API_URL_LOGIN, "POST", user);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    Model.instance().addCustomer(convertToCustomer(json));
                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class SignUp extends AsyncTask<User, String, User.Customer> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected User.Customer doInBackground(User... users) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(user_first_name, users[0].getFirstName());
                jsonObject.put(user_last_name, users[0].getLastName());
                jsonObject.put(user_email, users[0].getEmail());
                jsonObject.put(user_phone_number, users[0].getPhoneNumber());
                jsonObject.put(user_user_name, users[0].getUserName());
                jsonObject.put(user_password, users[0].getPassword());

                JSONObject json = jsonParser.makeHttpRequestUsingJobj(API_URL_SIGNUP, "POST", jsonObject);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    User.Customer user = convertToCustomer(json);
                    Model.instance().addCustomer(user);
                    return user;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class AddNewExpenseToCloud extends AsyncTask<Expense, Void, String> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected String doInBackground(Expense... expenses) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(isRecurring, expenses[0].isRepeatingExpense);
                jsonObject.put(category, expenses[0].getCategory());
                jsonObject.put(comments, expenses[0].getNote());
                jsonObject.put(date, expenses[0].date);
                jsonObject.put(picPath, expenses[0].getExpenseImage());
                jsonObject.put(amount, expenses[0].getExpenseAmount());
                jsonObject.put(name, expenses[0].getExpenseName());

                JSONObject jObj = jsonParser.makeHttpRequestUsingJobj(API_URL, "POST", jsonObject);

                if (jObj != null) {
                    Log.d("JSON result", jObj.toString());
                    Model.instance().addExpense(convertToSingleExpense(jObj.toString()));
                    return "OK";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class GetAdvisers extends AsyncTask<Void, Void, List<User.Adviser>> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected List<User.Adviser> doInBackground(Void... Void) {

            List<User.Adviser> advisers = new ArrayList<>();

            try {
                JSONArray jArray = jsonParser.makeHttpRequestArray(API_URL_USERS, "GET", null);

                if (jArray != null) {
                    Log.d("JSON result", jArray.toString());

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        JSONArray role = object.getJSONArray(roles);

                        if(role.getString(1).equals("advisor")) {
                            String displayName = object.getString(user_display_name);
                            String userName = object.getString(user_display_name);
                            String firstName = object.getString(user_first_name);
                            String lastName = object.getString(user_last_name);
                            String userId = object.getString(id);
                            String email = object.getString(user_email);
                            String phoneNumber = object.getString(user_phone_number);
                            String description = object.getString(adviser_description);
                            String profileImage = object.getString(adviser_profile_image);

                            advisers.add(new User.Adviser(userId,displayName, userName,firstName, lastName,email, phoneNumber, null, null,description, profileImage));
                        }
                    }
                    return advisers;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class GetAllData extends AsyncTask<Void, Void, String > {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected String doInBackground(Void... Void) {

            try {
                JSONArray jArray = jsonParser.makeHttpRequestArray(API_URL_USERS_EXPENSES + Model.instance().getUser().getId(), "GET", null);

                Model.instance().batchUpdateExpenses(convertToExpenses(jArray.toString()));
                return "Success!";

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}