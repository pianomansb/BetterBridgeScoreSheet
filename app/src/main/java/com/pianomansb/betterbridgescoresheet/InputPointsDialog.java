package com.pianomansb.betterbridgescoresheet;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by pianomansb on 2/2/16.
 */
public class InputPointsDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_points, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final MainActivity main = (MainActivity) getActivity();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(main,
                R.array.levels, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner levelSpinner = (Spinner) main.findViewById(R.id.level_spinner);
        levelSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(main,
                R.array.suits, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner suitSpinner = (Spinner) main.findViewById(R.id.suit_spinner);
        suitSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(main,
                R.array.doubled, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner doubledSpinner = (Spinner) main.findViewById(R.id.doubled_spinner);
        doubledSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(main,
                R.array.seats, R.layout.spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner declarerSpinner = (Spinner) main.findViewById(R.id.declarer_spinner);
        declarerSpinner.setAdapter(adapter);

        main.findViewById(R.id.points_cancel_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.killDialog();
                    }
                }
        );

        main.findViewById(R.id.points_accept_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Contract c = new Contract();
                        c.setLevel(levelSpinner.getSelectedItemPosition() + 1);
                        int suit;
                        switch (suitSpinner.getSelectedItemPosition()) {
                            case 0:
                                suit = Contract.CLUBS;
                                break;
                            case 1:
                                suit = Contract.DIAMONDS;
                                break;
                            case 2:
                                suit = Contract.HEARTS;
                                break;
                            case 3:
                                suit = Contract.SPADES;
                                break;
                            default:
                                suit = Contract.NOTRUMP;
                                break;
                        }
                        c.setSuit(suit);
                        int doubled;
                        switch (doubledSpinner.getSelectedItemPosition()) {
                            case 0:
                                doubled = Contract.NOT_DOUBLED;
                                break;
                            case 1:
                                doubled = Contract.DOUBLED;
                                break;
                            default:
                                doubled = Contract.REDOUBLED;
                                break;
                        }
                        c.setDoubled(doubled);
                        int declarer;
                        switch (declarerSpinner.getSelectedItemPosition()) {
                            case 0:
                                declarer = Contract.NORTH;
                                break;
                            case 1:
                                declarer = Contract.EAST;
                                break;
                            case 2:
                                declarer = Contract.SOUTH;
                                break;
                            default:
                                declarer = Contract.WEST;
                                break;
                        }
                        c.setDeclarer(declarer);

                        main.setContract(c);
                    }
                }
        );
    }
}
