package it.parello.tictactoenodejs.service;

import java.util.Random;

import static it.parello.tictactoenodejs.activities.SinglePlayer.b;
import static it.parello.tictactoenodejs.activities.SinglePlayer.i;
import static it.parello.tictactoenodejs.activities.SinglePlayer.j;
import static it.parello.tictactoenodejs.activities.SinglePlayer.mark;
import static it.parello.tictactoenodejs.activities.SinglePlayer.textView;

/**
 * Created by Parello on 12/04/2017.
 */

public class Cpu {

    public void takeTurn() {

        if(mark[0][0]==2 &&
                ((mark[0][1]==0 && mark[0][2]==0) ||
                        (mark[1][1]==0 && mark[2][2]==0) ||
                        (mark[1][0]==0 && mark[2][0]==0))) {
            markSquare(0,0);
        } else if (mark[0][1]==2 &&
                ((mark[1][1]==0 && mark[2][1]==0) ||
                        (mark[0][0]==0 && mark[0][2]==0))) {
            markSquare(0,1);
        } else if(mark[0][2]==2 &&
                ((mark[0][0]==0 && mark[0][1]==0) ||
                        (mark[2][0]==0 && mark[1][1]==0) ||
                        (mark[1][2]==0 && mark[2][2]==0))) {
            markSquare(0,2);
        } else if(mark[1][0]==2 &&
                ((mark[1][1]==0 && mark[1][2]==0) ||
                        (mark[0][0]==0 && mark[2][0]==0))){
            markSquare(1,0);
        } else if(mark[1][1]==2 &&
                ((mark[0][0]==0 && mark[2][2]==0) ||
                        (mark[0][1]==0 && mark[2][1]==0) ||
                        (mark[2][0]==0 && mark[0][2]==0) ||
                        (mark[1][0]==0 && mark[1][2]==0))) {
            markSquare(1,1);
        } else if(mark[1][2]==2 &&
                ((mark[1][0]==0 && mark[1][1]==0) ||
                        (mark[0][2]==0 && mark[2][2]==0))) {
            markSquare(1,2);
        } else if(mark[2][0]==2 &&
                ((mark[0][0]==0 && mark[1][0]==0) ||
                        (mark[2][1]==0 && mark[2][2]==0) ||
                        (mark[1][1]==0 && mark[0][2]==0))){
            markSquare(2,0);
        } else if(mark[2][1]==2 &&
                ((mark[0][1]==0 && mark[1][1]==0) ||
                        (mark[2][0]==0 && mark[2][2]==0))) {
            markSquare(2,1);
        }else if( mark[2][2]==2 &&
                ((mark[0][0]==0 && mark[1][1]==0) ||
                        (mark[0][2]==0 && mark[1][2]==0) ||
                        (mark[2][0]==0 && mark[2][1]==0))) {
            markSquare(2,2);
        } else {
            Random rand = new Random();

            int a = rand.nextInt(3);
            int b = rand.nextInt(3);
            while(a==0 || b==0 || mark[a][b]!=2) {
                a = rand.nextInt(3);
                b = rand.nextInt(3);
            }
            markSquare(a,b);
        }
    }


    private void markSquare(int x, int y) {
        b[x][y].setEnabled(false);
        b[x][y].setText("X");//setBackground(getDrawable(R.drawable.empire));
        mark[x][y] = 1;
        isGameOver();
    }

    public boolean isGameOver() {
        boolean gameOver = false;
        if ((mark[0][0] == 0 && mark[1][1] == 0 && mark[2][2] == 0)
                || (mark[0][2] == 0 && mark[1][1] == 0 && mark[2][0] == 0)
                || (mark[0][1] == 0 && mark[1][1] == 0 && mark[2][1] == 0)
                || (mark[0][2] == 0 && mark[1][2] == 0 && mark[2][2] == 0)
                || (mark[0][0] == 0 && mark[0][1] == 0 && mark[0][2] == 0)
                || (mark[1][0] == 0 && mark[1][1] == 0 && mark[1][2] == 0)
                || (mark[2][0] == 0 && mark[2][1] == 0 && mark[2][2] == 0)
                || (mark[0][0] == 0 && mark[1][0] == 0 && mark[2][0] == 0)) {
            textView.setText("Game over. You win!");
            gameOver = true;
            for (i = 0; i <= 2; i++) {
                for (j = 0; j <= 2; j++)
                    b[i][j].setEnabled(false);
            }
        } else if ((mark[0][0] == 1 && mark[1][1] == 1 && mark[2][2] == 1)
                || (mark[0][2] == 1 && mark[1][1] == 1 && mark[2][0] == 1)
                || (mark[0][1] == 1 && mark[1][1] == 1 && mark[2][1] == 1)
                || (mark[0][2] == 1 && mark[1][2] == 1 && mark[2][2] == 1)
                || (mark[0][0] == 1 && mark[0][1] == 1 && mark[0][2] == 1)
                || (mark[1][0] == 1 && mark[1][1] == 1 && mark[1][2] == 1)
                || (mark[2][0] == 1 && mark[2][1] == 1 && mark[2][2] == 1)
                || (mark[0][0] == 1 && mark[1][0] == 1 && mark[2][0] == 1)) {
            textView.setText("Game over. You lost!");
            gameOver = true;
            for (i = 0; i <= 2; i++) {
                for (j = 0; j <= 2; j++)
                    b[i][j].setEnabled(false);
            }
        } else {
            boolean empty = false;
            for(i=0; i<=2; i++) {
                for(j=0; j<=2; j++) {
                    if(mark[i][j]==2) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {
                gameOver = true;
                textView.setText("Game over. It's a draw!");
            }
        }

        return gameOver;
    }
}
