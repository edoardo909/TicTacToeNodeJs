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

import it.parello.tictactoenodejs.service.AsyncResponse;

/**
 * Created by edoar on 23/04/2017.
 */

public class SendOnlineGameRequest extends AsyncTask<String, Integer, String> {

    private static final String TAG = "SendOnlineGameRequest";
    public AsyncResponse responseListener;

    public SendOnlineGameRequest(AsyncResponse listener){
        this.responseListener = listener;
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
                wr.writeBytes("GameRequest=" + params[1]);
                wr.flush();
                wr.close();
                Log.d(TAG, "sending gamerequest");
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
        Log.e(TAG , result);
        responseListener.onProcessFinished(result);
    }


}
