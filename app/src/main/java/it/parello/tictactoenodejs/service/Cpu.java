package it.parello.tictactoenodejs.service;

import android.content.SharedPreferences;

import java.util.Random;

import it.parello.tictactoenodejs.activities.MainMenu;

import static it.parello.tictactoenodejs.activities.SinglePlayer.spButtons;
import static it.parello.tictactoenodejs.activities.SinglePlayer.i;
import static it.parello.tictactoenodejs.activities.SinglePlayer.j;
import static it.parello.tictactoenodejs.activities.SinglePlayer.spMark;
import static it.parello.tictactoenodejs.activities.SinglePlayer.spTextView;

/**
 * Created by Parello on 12/04/2017.
 */

public class Cpu extends MainMenu{

    public static int winCounter;
    public static int loseCounter;
    public static int drawCounter;

    public static int getWinCounter() {
        return winCounter;
    }

    public static int getLoseCounter() {
        return loseCounter;
    }

    public static int getDrawCounter() {
        return drawCounter;
    }
    public void takeTurn() {

        if(spMark[0][0]==2 &&
                ((spMark[0][1]==0 && spMark[0][2]==0) ||
                        (spMark[1][1]==0 && spMark[2][2]==0) ||
                        (spMark[1][0]==0 && spMark[2][0]==0))) {
            markSquare(0,0);
        } else if (spMark[0][1]==2 &&
                ((spMark[1][1]==0 && spMark[2][1]==0) ||
                        (spMark[0][0]==0 && spMark[0][2]==0))) {
            markSquare(0,1);
        } else if(spMark[0][2]==2 &&
                ((spMark[0][0]==0 && spMark[0][1]==0) ||
                        (spMark[2][0]==0 && spMark[1][1]==0) ||
                        (spMark[1][2]==0 && spMark[2][2]==0))) {
            markSquare(0,2);
        } else if(spMark[1][0]==2 &&
                ((spMark[1][1]==0 && spMark[1][2]==0) ||
                        (spMark[0][0]==0 && spMark[2][0]==0))){
            markSquare(1,0);
        } else if(spMark[1][1]==2 &&
                ((spMark[0][0]==0 && spMark[2][2]==0) ||
                        (spMark[0][1]==0 && spMark[2][1]==0) ||
                        (spMark[2][0]==0 && spMark[0][2]==0) ||
                        (spMark[1][0]==0 && spMark[1][2]==0))) {
            markSquare(1,1);
        } else if(spMark[1][2]==2 &&
                ((spMark[1][0]==0 && spMark[1][1]==0) ||
                        (spMark[0][2]==0 && spMark[2][2]==0))) {
            markSquare(1,2);
        } else if(spMark[2][0]==2 &&
                ((spMark[0][0]==0 && spMark[1][0]==0) ||
                        (spMark[2][1]==0 && spMark[2][2]==0) ||
                        (spMark[1][1]==0 && spMark[0][2]==0))){
            markSquare(2,0);
        } else if(spMark[2][1]==2 &&
                ((spMark[0][1]==0 && spMark[1][1]==0) ||
                        (spMark[2][0]==0 && spMark[2][2]==0))) {
            markSquare(2,1);
        }else if( spMark[2][2]==2 &&
                ((spMark[0][0]==0 && spMark[1][1]==0) ||
                        (spMark[0][2]==0 && spMark[1][2]==0) ||
                        (spMark[2][0]==0 && spMark[2][1]==0))) {
            markSquare(2,2);
        } else {
            Random rand = new Random();

            int a = rand.nextInt(3);
            int b = rand.nextInt(3);
            while(a==0 || b==0 || spMark[a][b]!=2) {
                a = rand.nextInt(3);
                b = rand.nextInt(3);
            }
            markSquare(a,b);
        }
    }


    private void markSquare(int x, int y) {
        spButtons[x][y].setEnabled(false);
        spButtons[x][y].setText("X");//setBackground(getDrawable(R.drawable.empire));
        spMark[x][y] = 1;
        isGameOver();
    }

    public boolean isGameOver() {
        boolean gameOver = false;


        if ((spMark[0][0] == 0 && spMark[1][1] == 0 && spMark[2][2] == 0)
                || (spMark[0][2] == 0 && spMark[1][1] == 0 && spMark[2][0] == 0)
                || (spMark[0][1] == 0 && spMark[1][1] == 0 && spMark[2][1] == 0)
                || (spMark[0][2] == 0 && spMark[1][2] == 0 && spMark[2][2] == 0)
                || (spMark[0][0] == 0 && spMark[0][1] == 0 && spMark[0][2] == 0)
                || (spMark[1][0] == 0 && spMark[1][1] == 0 && spMark[1][2] == 0)
                || (spMark[2][0] == 0 && spMark[2][1] == 0 && spMark[2][2] == 0)
                || (spMark[0][0] == 0 && spMark[1][0] == 0 && spMark[2][0] == 0)) {
            spTextView.setText("Game over. You win!");
            gameOver = true;
            winCounter++;
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++)
                    spButtons[i][j].setEnabled(false);
            }
        } else if ((spMark[0][0] == 1 && spMark[1][1] == 1 && spMark[2][2] == 1)
                || (spMark[0][2] == 1 && spMark[1][1] == 1 && spMark[2][0] == 1)
                || (spMark[0][1] == 1 && spMark[1][1] == 1 && spMark[2][1] == 1)
                || (spMark[0][2] == 1 && spMark[1][2] == 1 && spMark[2][2] == 1)
                || (spMark[0][0] == 1 && spMark[0][1] == 1 && spMark[0][2] == 1)
                || (spMark[1][0] == 1 && spMark[1][1] == 1 && spMark[1][2] == 1)
                || (spMark[2][0] == 1 && spMark[2][1] == 1 && spMark[2][2] == 1)
                || (spMark[0][0] == 1 && spMark[1][0] == 1 && spMark[2][0] == 1)) {
            spTextView.setText("Game over. You lost!");
            gameOver = true;
            loseCounter++;
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++)
                    spButtons[i][j].setEnabled(false);
            }
        } else {
            boolean empty = false;
            for(i=0; i<=2; i++) {
                for(j=0; j<=2; j++) {
                    if(spMark[i][j]==2) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {
                gameOver = true;
                drawCounter++;
                spTextView.setText("Game over. It's a draw!");
            }
        }

        return gameOver;
    }
}
