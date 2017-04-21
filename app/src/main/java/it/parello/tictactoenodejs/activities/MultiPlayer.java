package it.parello.tictactoenodejs.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;

public class MultiPlayer extends AppCompatActivity {

    Button restart;
    int mark[][];
    int i, j = 0;
    Button b[][];
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            setBoard();
        });
    }

    private void setBoard() {
        //TODO waitForOtherPlayer()
        b = new Button[4][4];
        mark = new int[4][4];
        b[0][2] = (Button) findViewById(R.id.b1);
        b[0][1] = (Button) findViewById(R.id.b2);
        b[0][0] = (Button) findViewById(R.id.b3);
        b[1][2] = (Button) findViewById(R.id.b4);
        b[1][1] = (Button) findViewById(R.id.b5);
        b[1][0] = (Button) findViewById(R.id.b6);
        b[2][2] = (Button) findViewById(R.id.b7);
        b[2][1] = (Button) findViewById(R.id.b8);
        b[2][0] = (Button) findViewById(R.id.b9);

        textView = (TextView) findViewById(R.id.dialogue);

        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++)
                mark[i][j] = 2;

        }

        textView.setText("Click a button to start.");
        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++) {
                b[i][j].setOnClickListener(l->{
                    //TODO
                });
                if (!b[i][j].isEnabled()) {
                    b[i][j].setText(" ");
                    b[i][j].setEnabled(true);
                }
            }
        }
    }

    private void waitForOpponentToConnect(){
        //TODO
    }

    private void listenForIncomingGameData(){
        //TODO
    }

    private void waitForOpponentMove(){
        //TODO
    }

    private boolean isGameOver(){
        return false;
    }

}
