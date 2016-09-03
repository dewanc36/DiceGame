package com.example.dewan.dicegame;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import java.lang.StringBuilder;
import android.os.Handler;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
    public static int userTotalScore=0;
    public static int userTurnScore=0;
    public static int computerTotalScore=0;
    public static int computerTurnScore=0;

    private static String totalUserScoreMessage="Your score: ";
    private static String totalCompScoreMessage="Computer's score: ";
    private static String turnUserScoreMessage=" Your turn score: ";
    private static String turnCompScoreMessage="Computer's turn score: ";

    private boolean rollAgain=false;

    TextView timerTextView;
    long startTime = 0;

    
    Handler timerHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Random random = new Random();
            int i = random.nextInt(2 - 0 + 1) + 0;

            if(rollAgain==true) {
                computerTurn();;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View view = findViewById(R.id.main);
        Button rollButton = (Button)findViewById(R.id.button_Roll);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   public void onClickRoll(View view){
       roller();
       printUpdate();
   }


    public void onClickHold(View view){
        TextView winnerMessage = (TextView) findViewById(R.id.winner);

        userTotalScore+=userTurnScore;
        userTurnScore=0;
        printUpdate();

        if(userTotalScore>=100){
            winnerMessage.setText("You win!"+"\n"+"Click reset to start a new game.");
            findViewById(R.id.button_Roll).setEnabled(false);
            findViewById(R.id.button_Hold).setEnabled(false);
            //resetHelper();
        }
        else{
            computerTurn();
        }

    }


    public void onClickReset(View view){
        resetHelper();
    }

    public void resetHelper(){
        TextView winnerMessage = (TextView) findViewById(R.id.winner);
        userTotalScore=0;
        userTurnScore=0;
        computerTotalScore=0;
        computerTurnScore=0;
        printUpdate();
        findViewById(R.id.button_Roll).setEnabled(true);
        findViewById(R.id.button_Hold).setEnabled(true);
        winnerMessage.setText("");
        ImageView diceImage =(ImageView) findViewById(R.id.imageSpace);
        diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
        TextView compMessage = (TextView) findViewById(R.id.compRoll);
        compMessage.setText("");
        timerHandler.removeCallbacksAndMessages(null);
    }

    public void computerTurn(){
        findViewById(R.id.button_Roll).setEnabled(false);
        findViewById(R.id.button_Hold).setEnabled(false);
        TextView winnerMessage = (TextView) findViewById(R.id.winner);
        TextView compMessage = (TextView) findViewById(R.id.compRoll);
        int diceValue =roller();


        printUpdate();


        if(diceValue==1) {
            compMessage.setText("Computer rolled a 1");
            findViewById(R.id.button_Roll).setEnabled(true);
            findViewById(R.id.button_Hold).setEnabled(true);
            rollAgain=false;
            timerHandler.removeCallbacksAndMessages(null);

        }else {
            findViewById(R.id.button_Roll).setEnabled(false);
            findViewById(R.id.button_Hold).setEnabled(false);
            if(computerTurnScore>=20){

                computerTotalScore+=computerTurnScore;
                computerTurnScore=0;
                printUpdate();
                compMessage.setText("Computer holds.");
                rollAgain=false;
                //timerHandler.removeCallbacksAndMessages(null);
                timerHandler.removeCallbacks(runnable);
                findViewById(R.id.button_Roll).setEnabled(true);
                findViewById(R.id.button_Hold).setEnabled(true);
            }

            if(computerTotalScore>=100){
                rollAgain=false;
                winnerMessage.setText("Computer wins!"+"\n"+"Click reset to start a new game.");
                findViewById(R.id.button_Roll).setEnabled(false);
                findViewById(R.id.button_Hold).setEnabled(false);
                //timerHandler.removeCallbacksAndMessages(null);
                timerHandler.removeCallbacks(runnable);
                //resetHelper();
            }
            if((!(computerTurnScore>=20 || computerTotalScore>=100)&& !findViewById(R.id.button_Roll).isEnabled())  ){
                rollAgain=true;
                timerHandler.postDelayed(runnable, 2000);
            }
        }

    }


    public int roller(){
        TextView compMessage = (TextView) findViewById(R.id.compRoll);
        compMessage.setText("");
        Button rollButton =   (Button)findViewById(R.id.button_Roll);
        Button holdButton =  (Button)findViewById(R.id.button_Hold);
        Random rand = new Random();
        int diceNum = rand.nextInt(6)+1;
        ImageView diceImage =(ImageView) findViewById(R.id.imageSpace);
        if(diceNum!=1){
            String newDrawableName ="dice"+Integer.toString(diceNum);
            int resId=this.getResources().getIdentifier(newDrawableName, "drawable", getPackageName());
            diceImage.setImageDrawable(getResources().getDrawable(resId));
            if(holdButton.isEnabled()) {
                userTurnScore += diceNum;
            }
            else {
                computerTurnScore+=diceNum;
            }
        }
        if(diceNum==1){
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
            if(holdButton.isEnabled()){
                userTurnScore=0;
                computerTurn();
            }else {
                computerTurnScore=0;
            }
        }
        return diceNum;
    }

    public void printUpdate(){
        StringBuilder sb = new StringBuilder();
        sb.append("      ")
                .append(totalUserScoreMessage).append(userTotalScore).append("               ")
                .append(turnUserScoreMessage).append(userTurnScore).append("\n"+" ")
                .append(totalCompScoreMessage).append(computerTotalScore).append("   ")
                .append(turnCompScoreMessage).append(computerTurnScore);

        //String finalString = getSupportActionBar().toString();
        TextView currentText = (TextView) findViewById(R.id.textView);
        currentText.setText(sb.toString());

    }




}
