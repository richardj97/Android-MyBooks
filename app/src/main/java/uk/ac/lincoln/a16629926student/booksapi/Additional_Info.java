package uk.ac.lincoln.a16629926student.booksapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Additional_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);
        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String descriptionAll = intent.getStringExtra("descriptionAll");
        String price = intent.getStringExtra("price");
        final String buyLink = intent.getStringExtra("buyLink");
        final String previewLink = intent.getStringExtra("previewLink");

        /*
        Button BuyNowBtn = (Button)findViewById(R.id.BuyBookBtn);
        Button PreviewBookBtn = (Button)findViewById(R.id.PreviewBookBtn);

        Log.e(HttpConnect.TAG, buyLink);

        if (buyLink.isEmpty()){
            BuyNowBtn.setVisibility(View.GONE);
        }
        else{
            String tempStr = price.replace("0.0 ", "");
            Log.e(HttpConnect.TAG, tempStr);
            if (tempStr.isEmpty())
                price = "FREE";

            String buttonText = "Buy Now (" + price + ")";
            BuyNowBtn.setText(buttonText);
            BuyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(buyLink));
                    startActivity(web);
                }
            });
        }

        if (previewLink.isEmpty()){
            PreviewBookBtn.setVisibility(View.GONE);
        }
        else {
            PreviewBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(previewLink));
                    startActivity(web);
                }
            });
        } */

        ArrayList<String> items = new ArrayList<String>();
        String[] separateData = descriptionAll.split("/split/"); //

        for (int i = 0; i < separateData.length; i++){
            items.add(separateData[i]);
        }

        ListView list = (ListView)findViewById(R.id.bookList);
        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(Additional_Info.this, android.R.layout.simple_expandable_list_item_1, items);
        list.setAdapter(ArrayAdapter);
    }
}

