package it.parello.tictactoenodejs.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import it.parello.tictactoenodejs.R;


public class LoadingFragment extends Fragment {

    private static final String TAG = "LoadingFragment";
    ProgressBar progressBar;

    public LoadingFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.loading_page, container, false);
        progressBar = (ProgressBar) layout.findViewById(R.id.progresss_bar);
        return layout;
    }

}
