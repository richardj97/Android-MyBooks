package uk.ac.lincoln.a16629926student.booksapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class SearchBooks extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search_books, container, false);

        Button button = rootView.findViewById(R.id.getBookBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                RadioButton bookTitleRb = rootView.findViewById(R.id.bookTitleRb);
                RadioButton isbmRb = rootView.findViewById(R.id.bookIsbmRb);
                RadioButton authorRb = rootView.findViewById(R.id.authorRb);
                RadioButton publisherRb = rootView.findViewById(R.id.publisherRb);
                EditText searchBookTb = rootView.findViewById(R.id.searchBookTb);

                Intent intent = new Intent(rootView.getContext(), GetBookDetails.class);
                intent.putExtra("data", searchBookTb.getText().toString());

                if (bookTitleRb.isChecked()){
                    intent.putExtra("prefix", 0);
                }
                else if (authorRb.isChecked()){
                    intent.putExtra("prefix", 1);
                }
                else if (publisherRb.isChecked()){
                    intent.putExtra("prefix", 2);
                }
                else if (isbmRb.isChecked()){
                    intent.putExtra("prefix", 3);
                }
                startActivity(intent);
            }
        });

        return rootView;
    }
}
