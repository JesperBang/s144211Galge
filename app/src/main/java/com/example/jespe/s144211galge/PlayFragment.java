package com.example.jespe.s144211galge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class PlayFragment extends Fragment implements View.OnClickListener {

    TextView highscore;
    Button playbtn, playvsbtn;
    Fragment frag;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_play, container, false);

        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Can't call getView() before after onCreateView
        highscore = (TextView) getView().findViewById(R.id.highscore);

        playbtn = (Button) getView().findViewById(R.id.Play);
        playvsbtn = (Button) getView().findViewById(R.id.PlayVs);

        //Listeners
        playbtn.setOnClickListener(this);
        playvsbtn.setOnClickListener(this);

        //Setting highscore on mainpage
        SharedPreferences settings = this.getActivity().getSharedPreferences("game_data", Context.MODE_PRIVATE);
        String highscorenum = settings.getString("high_score", null);

        if (highscorenum == null){ highscore.setText("No highscore yet!"); }
        else{ highscore.setText(highscorenum+" Seconds"); }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Play:
                frag = new GameFragment();
                ft = getFragmentManager().beginTransaction();
                ft.add(R.id.con_play, frag, "PlaySolo");
                ft.commit();
                break;
            case R.id.PlayVs:
                frag = new GameFragment();
                ft = getFragmentManager().beginTransaction();
                ft.add(R.id.con_play, frag, "PlayVs");
                ft.commit();
                break;
        default:break;
        }
    }
}
