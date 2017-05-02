package it.parello.tictactoenodejs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;

import static it.parello.tictactoenodejs.service.Cpu.getDrawCounter;
import static it.parello.tictactoenodejs.service.Cpu.getLoseCounter;
import static it.parello.tictactoenodejs.service.Cpu.getWinCounter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Statistics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {

    private TextView winCount;
    private TextView loseCount;
    private TextView drawCount;

    SharedPreferences sp ;

    private OnFragmentInteractionListener mListener;

    public Statistics() {
        // Required empty public constructor
    }


    public static Statistics newInstance() {
        Statistics fragment = new Statistics();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("my_shared_prefs", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_statistics, container, false);
        winCount = (TextView) layout.findViewById(R.id.sp_win_counter);
        loseCount = (TextView) layout.findViewById(R.id.sp_lose_counter);
        drawCount = (TextView) layout.findViewById(R.id.sp_draw_counter);
        int winCounter = getWinCounter();
        int loseCounter = getLoseCounter();
        int drawCounter = getDrawCounter();
        winCount.setText(String.valueOf(winCounter));
        loseCount.setText(String.valueOf(loseCounter));
        drawCount.setText(String.valueOf(drawCounter));
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
