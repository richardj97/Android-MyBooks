package uk.ac.lincoln.a16629926student.booksapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetBookDetails extends AppCompatActivity {
    private String data;
    private int prefix;
    private ArrayList<String> descriptionList = new ArrayList<>();
    private ArrayList<String> descriptionAllList = new ArrayList<>();
    private ArrayList<String> pricesList = new ArrayList<>();
    private ArrayList<String> buyLinkList = new ArrayList<>();
    private ArrayList<String> previewLinkList = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private static Context context;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_book_details);
        GetBookDetails.context = getApplicationContext();

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        prefix = intent.getIntExtra("prefix", 0);

        if (CheckInternetConnectivity()){
            new AsyncTaskParseJson().execute(data);
        }
        else {
            Toast.makeText(this, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading... Please wait");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public Boolean CheckInternetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        String[] jsonObjects = {"title", "authors", "categories", "publisher", "publishedDate", "description", "printType",
                "maturityRating", "language", "country", "saleability", "isEbook", "previewLink", "listPrice", "retailPrice",
                "currencyCode", "buyLink", "webReaderLink"};

        String title = "No title",
                authors = "No authors",
                categories = "No categories",
                publisher = "No publisher",
                datePublished = "No date published",
                description = "No description",
                printType = "No print type",
                matureRating = "No mature rating",
                language = "No language",
                country = "No country",
                saleability = "No saleability",
                ebook = "No ebook available",
                currencyCodeR = "",
                currencyCodeL = "",
                previewLink = "",
                buyLink = "",
                webReaderLink = "";

        int pageCount = 0, ratingsCount = 0;
        double averageRating = 0.0, amountR = 0.0, amountL = 0.0;

        @Override
        // Do something before execution
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        // this method is used for...................
        protected String doInBackground(String... arg0) {
            return HttpConnect.getJSONFromUrl(arg0[0], HttpConnect.prefix[prefix]);
        }
        @Override
        // below method will run when service HTTP request is complete, will then bind tweet text in arrayList to ListView
        protected void onPostExecute(String strFromDoInBg) {
            try {
                JSONObject jsonObject = new JSONObject(strFromDoInBg);

                int count = jsonObject.getInt("totalItems");
                String split = "/split/"; // Used to split lines on additional info activity

                if (strFromDoInBg == null) {
                    descriptionList.add("Couldn't get book data\n(" + HttpConnect.status + ")\n" + HttpConnect.GetResponse(HttpConnect.status));
                    urls.add("https://cdn.pixabay.com/photo/2017/02/12/21/29/false-2061132_960_720.png");
                }
                else if (count == 0){
                    Toast.makeText(context, "Couldn't fetch book data", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    JSONArray itemsArray = jsonObject.getJSONArray("items");

                    for (int item = 0; item < itemsArray.length(); item++) {
                        JSONObject book = itemsArray.getJSONObject(item);
                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                        if (volumeInfo.has(jsonObjects[0]))
                            title = volumeInfo.getString("title");
                        if (volumeInfo.has(jsonObjects[1]))
                            authors = Utils.ParseArray(volumeInfo.getString("authors"));
                        if (volumeInfo.has(jsonObjects[2]))
                            categories = Utils.ParseArray(volumeInfo.getString("categories"));
                        if (volumeInfo.has("ratingsCount"))
                            ratingsCount = volumeInfo.getInt("ratingsCount");
                        if (volumeInfo.has("averageRating"))
                            averageRating = volumeInfo.getDouble("averageRating");
                        if (volumeInfo.has(jsonObjects[3]))
                            publisher = volumeInfo.getString("publisher");
                        if (volumeInfo.has(jsonObjects[4]))
                            datePublished = volumeInfo.getString("publishedDate");
                        if (volumeInfo.has(jsonObjects[5]))
                            description = volumeInfo.getString("description");
                        if (volumeInfo.has("pageCount"))
                            pageCount = volumeInfo.getInt("pageCount");
                        if (volumeInfo.has(jsonObjects[6]))
                            printType = volumeInfo.getString("printType");
                        if (volumeInfo.has(jsonObjects[7]))
                            matureRating = volumeInfo.getString("maturityRating");
                        if (volumeInfo.has(jsonObjects[8]))
                            language = volumeInfo.getString("language");

                        if (book.getJSONObject("saleInfo").has(jsonObjects[9]))
                            country = book.getJSONObject("saleInfo").getString("country");
                        if (book.getJSONObject("saleInfo").has(jsonObjects[10]))
                            saleability = book.getJSONObject("saleInfo").getString("saleability");
                        if (book.getJSONObject("saleInfo").has(jsonObjects[11]))
                            ebook = book.getJSONObject("saleInfo").getString("isEbook");
                        if (volumeInfo.has(jsonObjects[12]))
                            previewLink = volumeInfo.getString("previewLink");
                        if (book.getJSONObject("saleInfo").has(jsonObjects[13]))
                            amountL = book.getJSONObject("saleInfo").getJSONObject(jsonObjects[13]).getDouble("amount");
                        if (book.getJSONObject("saleInfo").has(jsonObjects[13]))
                            currencyCodeL = book.getJSONObject("saleInfo").getJSONObject(jsonObjects[13]).getString(jsonObjects[15]);
                        if (book.getJSONObject("saleInfo").has(jsonObjects[14]))
                            amountR = book.getJSONObject("saleInfo").getJSONObject(jsonObjects[14]).getDouble("amount");
                        if (book.getJSONObject("saleInfo").has(jsonObjects[14]))
                            currencyCodeR = book.getJSONObject("saleInfo").getJSONObject(jsonObjects[14]).getString(jsonObjects[15]);
                        if (book.getJSONObject("saleInfo").has(jsonObjects[16]))
                            buyLink = book.getJSONObject("saleInfo").getString(jsonObjects[16]);
                        if (book.getJSONObject("accessInfo").has(jsonObjects[17]))
                            webReaderLink = book.getJSONObject("accessInfo").getString(jsonObjects[17]);

                        try {
                            descriptionList.add(
                                    title + "\n\nAuthors: " +
                                            authors + "\n\nCategories: " +
                                            categories);

                            descriptionAllList.add(
                                    "Title: " + title + split +
                                            "Authors: " + authors + split +
                                            "Categories: " + categories + split +
                                            "Rating: " + ratingsCount + split +
                                            "Average rating: " + averageRating + split +
                                            "Publisher: " + publisher + split +
                                            "Date published: " + datePublished + split +
                                            "Description: " + description + split +
                                            "Page count: " + pageCount + split +
                                            "Print type: " + printType + split +
                                            "Maturity rating: " + matureRating + split +
                                            "Language: " + language + split +
                                            "Country: " + country + split +
                                            "Saleability: " + saleability + split +
                                            "E-book available: " + ebook + split +
                                            "Sale price: " + amountL + " (" + currencyCodeL + ")" + split +
                                            "Retail price: " + amountR + " (" + currencyCodeR + ")"
                            );

                            if (volumeInfo.has("imageLinks"))
                                urls.add(volumeInfo.getJSONObject("imageLinks").getString("thumbnail"));
                            else
                                urls.add("https://cdn.pixabay.com/photo/2017/02/12/21/29/false-2061132_960_720.png");

                            pricesList.add(amountR +" " + currencyCodeR);
                            buyLinkList.add(buyLink);
                            previewLinkList.add(previewLink);

                        } catch (Exception e) {
                            Log.e(HttpConnect.TAG, "Error gathering book data: " + e.toString());
                        }
                    }

                    hideProgressDialog();

                    if (descriptionList.size() > 0){
                        final ListView list = (ListView) findViewById(R.id.bookList);
                        CustomListAdapter CustomListAdapter = new CustomListAdapter(GetBookDetails.this, descriptionList, urls);
                        list.setAdapter(CustomListAdapter);
                        list.setClickable(true);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                Intent intent = new Intent(context, Additional_Info.class);
                                intent.putExtra("descriptionAll", descriptionAllList.get(position));
                                intent.putExtra("price", pricesList.get(position));
                                intent.putExtra("buyLink", buyLinkList.get(position));
                                intent.putExtra("previewLink", previewLinkList.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            catch (Exception e){
                Log.e(HttpConnect.TAG, "Error parsing data: " + e.toString());
            }
        }
    }
    @Override
    public void onDestroy() { // In case the progress dialog is still active when intent is destroyed
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
