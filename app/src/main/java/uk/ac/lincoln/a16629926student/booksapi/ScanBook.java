package uk.ac.lincoln.a16629926student.booksapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ScanBook extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.scan_book, container, false);

        Button button = (Button)rootView.findViewById(R.id.scanBookBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(rootView.getContext(), Scanner.class));
            }
        });

        return rootView;
    }
}
