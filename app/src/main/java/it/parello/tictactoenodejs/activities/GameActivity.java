package it.parello.tictactoenodejs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.service.Cpu;
import it.parello.tictactoenodejs.service.MyClickListener;

public class GameActivity extends AppCompatActivity {

    public Button restart;
    public static int mark[][];
    public static int i, j = 0;
    public static Button b[][];
    public static Cpu cpu;
    public static TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setBoard();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            setBoard();
        });
    }

    private void setBoard() {
        cpu = new Cpu();
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
                b[i][j].setOnClickListener(new MyClickListener(i, j));
                if (!b[i][j].isEnabled()) {
                    b[i][j].setText(" ");
                    b[i][j].setEnabled(true);
                }
            }
        }
    }
}



