package it.parello.tictactoenodejs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;

import static it.parello.tictactoenodejs.service.Cpu.getSPDrawCounter;
import static it.parello.tictactoenodejs.service.Cpu.getSPLoseCounter;
import static it.parello.tictactoenodejs.service.Cpu.getSPWinCounter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Statistics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {

    private TextView spWinCount;
    private TextView spLoseCount;
    private TextView spDrawCount;
    private TextView mpWinCount;
    private TextView mpLoseCount;
    private TextView mpDrawCount;

    SharedPreferences sp ;

    private OnFragmentInteractionListener mListener;

    public Statistics() {}


    public static Statistics newInstance() {
        Statistics fragment = new Statistics();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_statistics, container, false);
        spWinCount = (TextView) layout.findViewById(R.id.sp_win_counter);
        spLoseCount = (TextView) layout.findViewById(R.id.sp_lose_counter);
        spDrawCount = (TextView) layout.findViewById(R.id.sp_draw_counter);
        mpWinCount = (TextView) layout.findViewById(R.id.mp_win_counter);
        mpLoseCount = (TextView) layout.findViewById(R.id.mp_lose_counter);
        mpDrawCount = (TextView) layout.findViewById(R.id.mp_draw_counter);
        int spWinCounter = getSPWinCounter();
        int spLoseCounter = getSPLoseCounter();
        int spDrawCounter = getSPDrawCounter();
        spWinCount.setText(String.valueOf(spWinCounter));
        spLoseCount.setText(String.valueOf(spLoseCounter));
        spDrawCount.setText(String.valueOf(spDrawCounter));
        mpWinCount.setText(String.valueOf(sp.getInt("mpWinCounter", 0)));
        mpLoseCount.setText(String.valueOf(sp.getInt("mpLoseCounter", 0)));
        mpDrawCount.setText(String.valueOf(sp.getInt("mpDrawCounter", 0)));
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
