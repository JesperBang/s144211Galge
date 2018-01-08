package com.example.jespe.s144211galge;

import android.content.Context;
import android.content.SharedPreferences;
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
        playbtn = (Button) getView().findViewById(R.id.Play);
        playvsbtn = (Button) getView().findViewById(R.id.PlayVs);

        //Listeners
        playbtn.setOnClickListener(this);
        playvsbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Play:
                frag = new GameFragment();
                ft = getFragmentManager().beginTransaction();
                ft.add(R.id.con_play, frag, "PlaySolo");
                ft.addToBackStack("main");
                ft.commit();
                break;
            case R.id.PlayVs:
                frag = new PlayVsFragment();
                ft = getFragmentManager().beginTransaction();
                ft.add(R.id.con_play, frag, "PlayVs");
                ft.addToBackStack("main");
                ft.commit();
                break;
        default:break;
        }
    }
}