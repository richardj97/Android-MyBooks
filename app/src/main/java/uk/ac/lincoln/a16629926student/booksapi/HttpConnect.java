package uk.ac.lincoln.a16629926student.booksapi;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnect {
    public static final String TAG = "JsonParser.java";
    public static final String TAG2 = "GoogleAuthException";
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String API_KEY = "AIzaSyBGuxffcbeElIYVhkH2y3NFozKnyKVU1xo";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";
    private static final String KEY_Prefix = "key";
    private static String json = "";
    public static String[] prefix = {"intitle:", "inauthor:", "inpublisher:", "isbn:"};
    public static int status;

    protected static String fetchToken(Activity mActivity, String mScope, String mEmail) throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (GoogleAuthException fatalException) {
            Log.e(TAG2, "Exception: ", fatalException);
        }
        return null;
    }

    public static String getUserBookcaseJSONFromUrl(Activity mActivity, String mScope, String mEmail) {
        try {
            URL u = new URL("https://www.googleapis.com/books/v1/mylibrary/bookshelves?key=" + API_KEY);
            HttpURLConnection restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestProperty("Authorization", "Bearer " + fetchToken(mActivity, mScope, mEmail));
            restConnection.setRequestMethod("GET");
            restConnection.setRequestProperty("Content-length", "0");
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);
            restConnection.setConnectTimeout(10000);
            restConnection.setReadTimeout(10000);
            restConnection.connect();
            status = restConnection.getResponseCode();

            // switch statement to catch HTTP 200 and 201 errors
            switch (status) {
                case 200: // Ok
                case 201: // Created
                    // live connection to your REST service is established here using getInputStream() method
                    BufferedReader br = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));

                    // create a new string builder to store json data returned from the REST service
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // loop through returned data line by line and append to stringbuilder 'sb' variable
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    // remember, you are storing the json as a stringy
                    try {
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "error parsing data " + e.toString());
                    }
                    // return JSON String containing data to Tweet activity (or whatever your activity is called!)
                    return json;
                case 400: // Bad Request
                case 401: // Unauthorized
                case 403: // Forbidden
                case 404: // Not found
                case 405: // Method not allowed
                case 406: // Not acceptable
                case 412: // Precondition failed
                case 415: // Unsupported media type
                case 500: // Internal server error
                case 501: // Not implemented
                    Log.e(TAG, "Error code: " + status);
                    return null;
            }
            // HTTP 200 and 201 error handling from switch statement
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Malformed URL ");
        } catch (IOException ex) {
            Log.e(TAG, "IO Exception ");
        }
        return null;
    }

    public static String getJSONFromUrl(String query, String prefix) {
        try {
            Uri uri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, prefix + query)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .appendQueryParameter(KEY_Prefix, API_KEY)
                    .build();

            URL u = new URL(uri.toString());
            HttpURLConnection restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestMethod("GET");
            restConnection.setRequestProperty("Content-length", "0");
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);
            restConnection.setConnectTimeout(10000);
            restConnection.setReadTimeout(10000);
            restConnection.connect();
            status = restConnection.getResponseCode();

            // switch statement to catch HTTP 200 and 201 errors
            switch (status) {
                case 200: // Ok
                case 201: // Created
                    // live connection to your REST service is established here using getInputStream() method
                    BufferedReader br = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));

                    // create a new string builder to store json data returned from the REST service
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // loop through returned data line by line and append to stringbuilder 'sb' variable
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    // remember, you are storing the json as a stringy
                    try {
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "error parsing data " + e.toString());
                    }
                    // return JSON String containing data to Tweet activity (or whatever your activity is called!)
                    return json;
                case 400: // Bad Request
                case 401: // Unauthorized
                case 403: // Forbidden
                case 404: // Not found
                case 405: // Method not allowed
                case 406: // Not acceptable
                case 412: // Precondition failed
                case 415: // Unsupported media type
                case 500: // Internal server error
                case 501: // Not implemented
                    Log.e(TAG, "Error code: " + status);
                    return null;
            }
            // HTTP 200 and 201 error handling from switch statement
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Malformed URL ");
        } catch (IOException ex) {
            Log.e(TAG, "IO Exception ");
        }
        return null;
    }

    public static String GetResponse(int status) {
        String response = "";

        switch (status) {
            case 400: // Bad Request
                response = "Bad Request";
            case 401: // Unauthorized
                response = "Unauthorized request";
            case 403: // Forbidden
                response = "Access is forbidden";
            case 404: // Not found
                response = "Request was not found";
            case 405: // Method not allowed
                response = "Method was not allowed";
            case 406: // Not acceptable
                response = "Request is not acceptable";
            case 412: // Precondition failed
                response = "The precondition failed";
            case 415: // Unsupported media type
                response = "Unsupported media type";
            case 500: // Internal server error
                response = "Internal server error";
            case 501: // Not implemented
                response = "Request not available";
                return response;
        }
        return response;
    }
}