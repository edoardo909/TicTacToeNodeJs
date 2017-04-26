package it.parello.tictactoenodejs.listeners;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import static it.parello.tictactoenodejs.activities.SinglePlayer.spButtons;
import static it.parello.tictactoenodejs.activities.SinglePlayer.cpu;
import static it.parello.tictactoenodejs.activities.SinglePlayer.spMark;
import static it.parello.tictactoenodejs.activities.SinglePlayer.spTextView;

/**
 * Created by Parello on 12/04/2017.
 */

public class SPClickListener implements View.OnClickListener {
    int x;
    int y;
    private static final String TAG = "SPClickListener";

    public SPClickListener(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void onClick(View view) {
        Log.d(TAG,"button "+ spButtons[x][y] + " clicked");
        if (spButtons[x][y].isEnabled()) {
            spButtons[x][y].setEnabled(false);
            spButtons[x][y].setText("O");
            spMark[x][y] = 0;
            spTextView.setText("");
            if (!cpu.isGameOver()) {
                final Handler handler = new Handler();
                handler.postDelayed(()-> {
                    cpu.takeTurn();
                }, 300);

            }
        }
    }
}
