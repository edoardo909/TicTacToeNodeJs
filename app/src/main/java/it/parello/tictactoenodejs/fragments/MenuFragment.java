package it.parello.tictactoenodejs.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.activities.MultiPlayer;
import it.parello.tictactoenodejs.activities.SinglePlayer;
import it.parello.tictactoenodejs.async.SendOnlineGameRequest;
import it.parello.tictactoenodejs.service.AsyncResponse;

import static it.parello.tictactoenodejs.service.Cpu.getDrawCounter;
import static it.parello.tictactoenodejs.service.Cpu.getLoseCounter;
import static it.parello.tictactoenodejs.service.Cpu.getWinCounter;

/**
 * Created by Parello on 02/05/2017.
 */

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";


    public interface OnMenuFragmentListener {
        void playAlone();
        void playWithFriends();
        void exit();
        void statistics();
    }

    OnMenuFragmentListener mListener;
    Button playAlone,playOnline, statistics,exit;

    public MenuFragment(){}

    private void findViews(View layout){
        playAlone = (Button) layout.findViewById(R.id.play_single_player);
        playOnline = (Button) layout.findViewById(R.id.play_multi_player);
        statistics = (Button) layout.findViewById(R.id.statistics);
        exit = (Button) layout.findViewById(R.id.exit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnMenuFragmentListener) {
            mListener = (OnMenuFragmentListener) context;
        }else {
                throw new RuntimeException(context.toString()
                        + " must implement OnMenuFragmentListener");
            }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnMenuFragmentListener) {
            mListener = (OnMenuFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(layout);


        playAlone.setOnClickListener(e -> mListener.playAlone());
        playOnline.setOnClickListener(e -> mListener.playWithFriends());
        statistics.setOnClickListener(e -> mListener.statistics());
        exit.setOnClickListener(e -> mListener.exit());

        return layout;
    }
}
