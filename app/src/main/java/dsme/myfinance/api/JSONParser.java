package dsme.myfinance.api;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dsme.myfinance.models.Model;

public class JSONParser {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    JSONArray jArray = null;
    StringBuilder sbParams;
    String paramsString;
    String sessionId = null;

    public JSONObject makeHttpRequest(String url, String method,JSONObject user) {

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Accept-Charset", charset);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();
                paramsString = user.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){
            // request method is GET

            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }

            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setConnectTimeout(15000);
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            //Receive the response from the server
            String response = conn.getResponseMessage();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String headerName;
            String headerValue = "FAIL";
            for (int j = 0;; j++) {
                headerName = conn.getHeaderFieldKey(j);
                if (headerName != null && headerName.equals("Set-Cookie")) {
                    // found the Set-Cookie header (code assudavemes only one cookie is
                    // being set)
                    headerValue = conn.getHeaderField(j);
                    sessionId = headerValue; //headerValue.split("=")[1].split(";")[0];
                    break;
                }
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
            if (sessionId != null){
                jObj.put("session_id", sessionId);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jObj;
    }

    public JSONObject makeHttpRequestUsingJobj(String url, String method,JSONObject jobj) {
        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                if(Model.instance().getCustomer().getSessionId() != null) {
                    String temp1 = Model.instance().getCustomer().getSessionId();
                    String temp = Model.instance().getCustomer().getSessionIdTrimmed();
                    conn.setRequestProperty("Cookie", Model.instance().getCustomer().getSessionId());
                }
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                paramsString = jobj.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){
            // request method is GET

            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }

            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-Type", Model.instance().getCustomer().getSessionIdTrimmed());
                conn.setConnectTimeout(15000);
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            //Receive the response from the server
            String response = conn.getResponseMessage();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String headerName;
            String headerValue = "FAIL";
            for (int j = 0; j<50; j++) {
                headerName = conn.getHeaderFieldKey(j);
                if (headerName != null && headerName.equals("Set-Cookie")) {
                    headerValue = conn.getHeaderField(j);
                    sessionId = headerValue; //headerValue.split("=")[1].split(";")[0];
                    break;
                }
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        try {
            jObj = new JSONObject(result.toString());
            if (sessionId != null){
                jObj.put("session_id", sessionId);
            }

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jObj;
    }

    public JSONArray makeHttpRequestArray(String url, String method, JSONObject jobj) {

        if(method.equals("GET")){
            // request method is GET

            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("Content-Type", Model.instance().getCustomer().getSessionIdTrimmed());
                conn.setConnectTimeout(15000);
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            //Receive the response from the server
            String response = conn.getResponseMessage();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jArray = new JSONArray(result.toString());
            if (sessionId != null){
                jObj.put("session_id", sessionId);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jArray;
    }

}
