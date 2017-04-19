package it.parello.tictactoenodejs.service;

import android.os.Handler;
import android.view.View;

import static it.parello.tictactoenodejs.activities.GameActivity.b;
import static it.parello.tictactoenodejs.activities.GameActivity.cpu;
import static it.parello.tictactoenodejs.activities.GameActivity.mark;
import static it.parello.tictactoenodejs.activities.GameActivity.textView;

/**
 * Created by Parello on 12/04/2017.
 */

public class MyClickListener implements View.OnClickListener {
    int x;
    int y;


    public MyClickListener(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void onClick(View view) {
        if (b[x][y].isEnabled()) {
            b[x][y].setEnabled(false);
            b[x][y].setText("O");//setBackground(getDrawable(R.drawable.rebels));
            mark[x][y] = 0;
            textView.setText("");
            if (!cpu.isGameOver()) {
                final Handler handler = new Handler();
                handler.postDelayed(()-> {
                    cpu.takeTurn();
                }, 300);

            }
        }
    }
}
