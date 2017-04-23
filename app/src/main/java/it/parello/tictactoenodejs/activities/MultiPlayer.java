package it.parello.tictactoenodejs.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.listeners.MPClickListener;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MultiPlayer extends MyAppActivity {

    Button restart;
    public static int mpMark[][];
    public static int i, j = 0;
    public static Button mpButtons[][];
    public static TextView mpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        setBoard();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            setBoard();
        });
    }

    private void setBoard() {
        mpButtons = new Button[4][4];
        mpMark = new int[4][4];
        mpButtons[0][2] = (Button) findViewById(R.id.b1);
        mpButtons[0][1] = (Button) findViewById(R.id.b2);
        mpButtons[0][0] = (Button) findViewById(R.id.b3);
        mpButtons[1][2] = (Button) findViewById(R.id.b4);
        mpButtons[1][1] = (Button) findViewById(R.id.b5);
        mpButtons[1][0] = (Button) findViewById(R.id.b6);
        mpButtons[2][2] = (Button) findViewById(R.id.b7);
        mpButtons[2][1] = (Button) findViewById(R.id.b8);
        mpButtons[2][0] = (Button) findViewById(R.id.b9);

        mpTextView = (TextView) findViewById(R.id.dialogue);

        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++)
                mpMark[i][j] = 2;

        }

        mpTextView.setText("Click a button to start.");
        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++) {
                mpButtons[i][j].setOnClickListener(new MPClickListener(i, j));
                if (!mpButtons[i][j].isEnabled()) {
                    mpButtons[i][j].setText(" ");
                    mpButtons[i][j].setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            //TODO notifica che vince l'altro
        }else {
            super.onBackPressed();
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
