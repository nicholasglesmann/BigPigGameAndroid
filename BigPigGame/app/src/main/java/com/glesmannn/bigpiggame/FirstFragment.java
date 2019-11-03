package com.glesmannn.bigpiggame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FirstFragment extends Fragment implements OnClickListener {

    private static final String PLAYER_1_NAME = "player1name";
    private static final String PLAYER_2_NAME = "player2name";

    private FirstActivity activity;
    private boolean twoPaneLayout;
    private EditText player1EditText;
    private EditText player2EditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.first_fragment, container, false);

        // set this fragment to listen for the new game click event
        Button b = (Button) view.findViewById(R.id.newGameButton2);
        b.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (FirstActivity)getActivity();
        player1EditText = (EditText) activity.findViewById(R.id.player1NameEditText);
        player2EditText = (EditText) activity.findViewById(R.id.player2NameEditText);


        twoPaneLayout = activity.findViewById(R.id.second_fragment) != null;

        Toast.makeText(getActivity(), String.valueOf(twoPaneLayout), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(View v) {
        String player1Name = player1EditText.getText().toString();
        String player2Name = player2EditText.getText().toString();

        if (v.getId() == R.id.newGameButton2) {
            if (twoPaneLayout) {
                Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
                activity.setPlayerNamesFromFirstActivity(player1Name, player2Name);
            } else {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra(PLAYER_1_NAME, player1Name);
                intent.putExtra(PLAYER_2_NAME, player2Name);
                startActivity(intent);
            }
        }
    }
}
