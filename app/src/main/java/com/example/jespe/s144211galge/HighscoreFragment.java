package com.example.jespe.s144211galge;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class HighscoreFragment extends Fragment {

    SharedPreferences settings;
    ListView lvhl;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return i.inflate(R.layout.fragment_highscore, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        settings = getActivity().getSharedPreferences("game_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        lvhl = (ListView) getActivity().findViewById(R.id.lvhl);
        ArrayList<String> al = new ArrayList<String>();

        al.add(0,settings.getString("firstplace", "None: 0"));
        al.add(1,settings.getString("secondplace", "None: 0"));
        al.add(2,settings.getString("thirdplace", "None: 0"));

        //Run this method to reset highscores.
        //and slide to highscore list before commenting again.
        //resetScore();

        lvhl.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al));
    }

    //Alternative update method for highscore list
    //This will run every  time fragment is in "focus"
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public void resetScore(){
        settings.edit().remove("temp").commit();
        settings.edit().remove("firstplace").commit();
        settings.edit().remove("secondplace").commit();
        settings.edit().remove("thirdplace").commit();
    }
}
