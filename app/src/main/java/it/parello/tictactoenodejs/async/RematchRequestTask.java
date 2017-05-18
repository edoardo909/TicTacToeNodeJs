package it.parello.tictactoenodejs.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.activities.MultiPlayer;

/**
 * Created by edoar on 12-May-17.
 */

public class RematchRequestTask extends AsyncTask <String, Void, String> {

    private static final String TAG = "RematchRequestTask";

    MultiPlayer context;
    ProgressBar progressBar;

    public RematchRequestTask(MultiPlayer context){
        this.context = context;
    }

    protected void onPreExecute(){
        progressBar = (ProgressBar) context.findViewById(R.id.progress_bar_multiplayer);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("RematchRequest=" + params[1]);
            wr.flush();
            wr.close();
            Log.d(TAG, "sending rematch request");
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e(TAG, result);
        context.runOnUiThread(()->{
            progressBar.setVisibility(View.GONE);
        });
    }
}
