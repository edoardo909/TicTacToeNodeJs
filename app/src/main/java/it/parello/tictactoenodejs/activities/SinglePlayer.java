package it.parello.tictactoenodejs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.service.Cpu;
import it.parello.tictactoenodejs.listeners.SPClickListener;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class SinglePlayer extends MyAppActivity {

    public Button restart;
    public static int spMark[][];
    public static int i, j = 0;
    public static Button spButtons[][];
    public static Cpu cpu;
    public static TextView spTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        setBoard();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            setBoard();
        });
    }

    private void setBoard() {
        cpu = new Cpu();
        spButtons = new Button[4][4];
        spMark = new int[4][4];
        spButtons[0][0] = (Button) findViewById(R.id.b1);
        spButtons[0][1] = (Button) findViewById(R.id.b2);
        spButtons[0][2] = (Button) findViewById(R.id.b3);
        spButtons[1][0] = (Button) findViewById(R.id.b4);
        spButtons[1][1] = (Button) findViewById(R.id.b5);
        spButtons[1][2] = (Button) findViewById(R.id.b6);
        spButtons[2][0] = (Button) findViewById(R.id.b7);
        spButtons[2][1] = (Button) findViewById(R.id.b8);
        spButtons[2][2] = (Button) findViewById(R.id.b9);

        spTextView = (TextView) findViewById(R.id.dialogue);

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++)
                spMark[i][j] = 2;

        }

        spTextView.setText("Click a button to start.");
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                spButtons[i][j].setOnClickListener(new SPClickListener(i, j));
                if (!spButtons[i][j].isEnabled()) {
                    spButtons[i][j].setText(" ");
                    spButtons[i][j].setEnabled(true);
                }
            }
        }
    }


}



