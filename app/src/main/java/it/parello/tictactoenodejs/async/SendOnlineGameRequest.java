package it.parello.tictactoenodejs.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.parello.tictactoenodejs.service.AsyncResponse;

/**
 * Created by edoar on 23/04/2017.
 */

public class SendOnlineGameRequest extends AsyncTask<String, Integer, String> {

    private static final String TAG = "SendOnlineGameRequest";
    public AsyncResponse responseListener;
//    ProgressBar progressBar;
//    int progressStatus = 0;
    public SendOnlineGameRequest(AsyncResponse listener){
        this.responseListener = listener;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        HttpURLConnection httpURLConnection = null;
//        while(progressStatus < 100) {
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("GameRequest=" + params[1]);
                wr.flush();
                wr.close();
                Log.d(TAG, "sending gamerequest");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
//                try {
//                    Thread.sleep(1000);
//                    publishProgress(progressStatus);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
//        }
        return data;
    }

//    @Override
//    protected void onProgressUpdate(Integer...values){
//        progressBar.setProgress(values[0]);
//    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e(TAG , result);
        responseListener.onProcessFinished(result);
//        progressBar.setVisibility(View.GONE);
    }


}
