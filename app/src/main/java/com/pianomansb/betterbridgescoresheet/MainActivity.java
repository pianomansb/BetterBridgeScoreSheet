package com.pianomansb.betterbridgescoresheet;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Contract contract = null;
    private Fragment dialog = null;
    //init as West so deal advances to North for first deal
    private int dealer = Contract.WEST;
    private int weCurrentGamePoints =0, theyCurrentGamePoints =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.this.dialog == null) { //no dialog showing

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    if (contract == null) { //no contract yet -- open set contract dialog
                        MainActivity.this.dialog = new InputPointsDialog();
                    } else { //finished play -- open dialog to input results for scoring
                        MainActivity.this.dialog = new InputResultsDialog();
                    }
                    ft.add(R.id.main, dialog);
                    ft.commit();
                }
            }
        };
        findViewById(R.id.main).setOnClickListener(listener);

        beginAuction();
    }

    public void killDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(dialog);
        ft.commit();
        dialog=null;
    }

    private void beginAuction() {
        dealer = Contract.getNextSeat(dealer);
        String title = "Bidding (";
        title += Contract.toString(dealer);
        title += " dealt)";
        getSupportActionBar().setTitle(title);
    }

    public void setContract(Contract contract) {
        this.contract = contract;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(dialog);
        ft.commit();
        dialog = null;

        String titleString = "";
        titleString += Contract.toString(contract.getDeclarer()) + "'s ";
        titleString += Integer.toString(contract.getLevel());
        titleString += Contract.toString(contract.getSuit());
        if( contract.getDoubled() != Contract.NOT_DOUBLED ){
            titleString += Contract.toString(contract.getDoubled());
        }
        titleString += " (" + Contract.toString(dealer) + " dealt)";
        getSupportActionBar().setTitle(titleString);
    }

    private TextView getNewScoreView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        TextView ret = new TextView(this);
        ret.setLayoutParams(params);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){ // API 23
            ret.setTextAppearance(R.style.PointsFont);
        } else {
            ret.setTextAppearance(this, R.style.PointsFont);
        }
        return  ret;
    }

    private void createScoreText(int declarerGame, int declarerBonus, int defenderBonus) {

        int declarer = contract.getDeclarer();

        if( declarerGame > 0 ){
            TextView declarerGameText = getNewScoreView();
            declarerGameText.setText(Integer.toString(declarerGame));
            LinearLayout declarerGameLayout;
            if( declarer == Contract.NORTH || declarer == Contract.SOUTH ){
                declarerGameLayout = (LinearLayout) findViewById(R.id.we_game);
            } else {
                declarerGameLayout = (LinearLayout) findViewById(R.id.they_game);
            }
            declarerGameLayout.addView(declarerGameText, -1);
        }

        if( declarerBonus > 0 ){
            TextView declarerBonusText = getNewScoreView();
            declarerBonusText.setText(Integer.toString(declarerBonus));
            LinearLayout declarerBonusLayout;
            if( declarer == Contract.NORTH || declarer == Contract.SOUTH ){
                declarerBonusLayout = (LinearLayout) findViewById(R.id.we_bonus);
            } else {
                declarerBonusLayout = (LinearLayout) findViewById(R.id.they_bonus);
            }
            declarerBonusLayout.addView(declarerBonusText, 0);
        }

        if( defenderBonus > 0 ){
            TextView defenderBonusText = getNewScoreView();
            defenderBonusText.setText(Integer.toString(defenderBonus));
            LinearLayout defenderBonusLayout;
            if( declarer == Contract.NORTH || declarer == Contract.SOUTH ){
                defenderBonusLayout = (LinearLayout) findViewById(R.id.they_bonus);
            } else {
                defenderBonusLayout = (LinearLayout) findViewById(R.id.we_bonus);
            }
            defenderBonusLayout.addView(defenderBonusText, 0);
        }
    }

    //TODO need to add rubber bonuses
    public void scoreContract(int tricksMade) {
        int declarerGame=0, declarerBonus=0, defenderBonus=0;
        int doubled = contract.getDoubled();
        boolean vulnerable =false; //TODO

        //score game tricks
        int bonusAdditions =0; //use for bonuses found when adding game points
        int tricksNeeded = contract.getLevel() + 6;
        if (tricksMade >= tricksNeeded) {
            declarerGame = contract.getLevel();

            switch (contract.getSuit()) {
                case Contract.CLUBS:
                case Contract.DIAMONDS:
                    declarerGame *= 20;
                    break;
                case Contract.HEARTS:
                case Contract.SPADES:
                    declarerGame *= 30;
                    break;
                case Contract.NOTRUMP:
                    declarerGame *= 30;
                    declarerGame += 10; //40  for first trick
                    break;
            }

            if( doubled == Contract.DOUBLED ){
                declarerGame *= 2;
                bonusAdditions += 50;
            } else if( doubled == Contract.REDOUBLED ) {
                declarerGame *= 4;
                bonusAdditions += 100;
            }

            //slam bonus
            if( contract.getLevel() >= 6 ){
                int slamBonus;
                if( vulnerable ) slamBonus = 750;
                else slamBonus = 500;
                if( contract.getLevel() == 7 )
                    slamBonus *= 2;
                bonusAdditions += slamBonus;
            }

            //add score to local game counts
            if( contract.getDeclarer() == Contract.NORTH ||
                    contract.getDeclarer() == Contract.SOUTH )
                weCurrentGamePoints += declarerGame;
            else
                theyCurrentGamePoints += declarerGame;
        }

        //score over tricks
        if (tricksMade > tricksNeeded) {
            declarerBonus = tricksMade - tricksNeeded;

            if( doubled == Contract.NOT_DOUBLED ){
                switch( contract.getSuit() ){
                    case Contract.CLUBS:
                    case Contract.DIAMONDS:
                        declarerBonus *= 20;
                        break;
                    default:
                        declarerBonus *= 30;
                        break;
                }
            } else {
                declarerBonus *= 100; //base score for doubled/not vulnerable
                if( doubled == Contract.REDOUBLED )
                    declarerBonus *= 2;
                if( vulnerable )
                    declarerBonus *= 2;
            }
        }
        declarerBonus += bonusAdditions; //add this last

        //score under tricks
        if (tricksMade < tricksNeeded) {
            defenderBonus = tricksNeeded - tricksMade;
            if( doubled == Contract.NOT_DOUBLED ){
                if( vulnerable ) defenderBonus *= 100;
                else defenderBonus *= 50;
            } else {
                int bonus =0;
                for( int i=1; i <= defenderBonus; ++i ){
                    //first trick
                    if( i == 1 ){
                        if( vulnerable ) bonus = 200;
                        else bonus = 100;
                    }
                    //second and third tricks
                    else if( i == 2 || i == 3 ){
                        if( vulnerable ) bonus += 300;
                        else bonus += 200;
                    }
                    //subsequent tricks
                    else {
                        bonus += 300;
                    }
                }
                if( doubled == Contract.REDOUBLED )
                    bonus *= 2;

                defenderBonus = bonus;
            }
        }

        createScoreText(declarerGame, declarerBonus, defenderBonus);

        contract = null;
        killDialog();
        findGames();
        beginAuction();
    }

    private void findGames(){
        if( weCurrentGamePoints < 100 && theyCurrentGamePoints < 100 )
            return;
        final LinearLayout weGameLayout = (LinearLayout) findViewById(R.id.we_game);
        final LinearLayout theyGameLayout = (LinearLayout) findViewById(R.id.they_game);
        int weChildCount = weGameLayout.getChildCount();
        int theyChildCount = theyGameLayout.getChildCount();
        if( weChildCount != theyChildCount ){
            if( weChildCount < theyChildCount )
                addTextViewsToPad(weGameLayout, theyChildCount-weChildCount);
            else
                addTextViewsToPad(theyGameLayout, weChildCount-theyChildCount);
        }

        weGameLayout.addView(new GameLine(this, GameLine.GAME_LINE), -1);

        /*findViewById(R.id.main).getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //remove listener
                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                            findViewById(R.id.main).getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        else
                            findViewById(R.id.main).getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);

                        TextView bottomTextView = (TextView) weGameLayout
                                .getChildAt(weGameLayout.getChildCount() - 1);
                        int[] pos = new int [2];
                        bottomTextView.getLocationOnScreen(pos);
                        int gameLinePosition = pos[1];
                        int height = bottomTextView.getHeight();
                        int topdiff = findViewById(R.id.background_lines).getTop()
                                + findViewById(R.id.main).getTop();
                        ((BackgroundLines)findViewById(R.id.background_lines))
                                .addGameLine(gameLinePosition + height - topdiff);
                    }
                }
        );*/
        /*
        //find position under bottommost textview
        //doesn't matter if we or they, since after padding they have the same
        //number of views
        TextView bottomTextView = (TextView) weGameLayout
                .getChildAt(weGameLayout.getChildCount()-1);
        int[] pos = new int [2];
        bottomTextView.getLocationInWindow(pos);
        int gameLinePosition = pos[1];// + bottomTextView.getHeight();
        ((BackgroundLines)findViewById(R.id.background_lines))
                .addGameLine(gameLinePosition);
        ((BackgroundLines)findViewById(R.id.background_lines))
                .addGameLine(gameLinePosition + bottomTextView.getMeasuredHeight());*/
    }

    private void addTextViewsToPad(LinearLayout view, int numToAdd) {
        for( int i=0; i < numToAdd; ++i ){
            TextView pad = getNewScoreView();
            pad.setText("  ");
            view.addView(pad, -1);
        }
    }

    public Contract getContract() {
        return contract;
    }
}
