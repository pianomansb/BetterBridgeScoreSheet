package com.pianomansb.betterbridgescoresheet;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by pianomansb on 2/3/16.
 */
public class InputResultsDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_results, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final MainActivity main = (MainActivity)getActivity();
        Contract c = main.getContract();

        TextView spinnerLabel = (TextView)main.findViewById(R.id.spinner_label);
        String s = Contract.toString(c.getDeclarer()) + " made:";
        spinnerLabel.setText(s);

        CharSequence[] arr = createCharSequenceArray(c);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>
                (main, R.layout.spinner_item, arr);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner tricksMadeSpinner = (Spinner) main.findViewById(R.id.tricks_made_spinner);
        tricksMadeSpinner.setAdapter(adapter);
        tricksMadeSpinner.setSelection(c.getLevel() + 6);

        main.findViewById(R.id.results_cancel_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.killDialog();
                    }
                }
        );

        main.findViewById(R.id.results_accept_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.scoreContract(tricksMadeSpinner.getSelectedItemPosition());
                    }
                }
        );
    }

    private CharSequence[] createCharSequenceArray(Contract c) {
        String[] ret = new String[14];
        int needToMake = c.getLevel() + 6;
        for( int i=0; i<=13; ++i ){
            String s = Integer.toString(i);
            s += " tricks (";
            if( i < needToMake ){
                s += "down " + Integer.toString( needToMake-i ) + ")";
            } else if( i == needToMake ){
                s += "contract making)";
            } else if( i > needToMake ){
                s += "contract+" + Integer.toString( i-needToMake ) + ")";
            }
            ret[i] = s;
        }
        return ret;
    }
}
