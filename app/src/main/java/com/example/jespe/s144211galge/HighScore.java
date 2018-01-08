package com.example.jespe.s144211galge;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.ArrayList;

public class HighScore {

HighscoreFragment hlf = new HighscoreFragment();

    public void saveHighScore(String Score, Activity activity){
        SharedPreferences settings = activity.getSharedPreferences("game_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("temp", String.valueOf(Score));
        editor.commit();
        sortHighScore(activity);
    }

    public void sortHighScore(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences("game_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        String temp = settings.getString("temp", "None: 0");
        Double dtemp = Double.parseDouble(temp.split(":")[1]);;

        String first = settings.getString("firstplace", "None: 0");
        Double dfirst = Double.parseDouble(first.split(":")[1]);

        String second = settings.getString("secondplace", "None: 0");
        Double dsecond = Double.parseDouble(second.split(":")[1]);

        String third = settings.getString("thirdplace", "None: 0");
        Double dthird = Double.parseDouble(third.split(":")[1]);

        System.out.println(first+"  "+second+"  "+third);

        if (dtemp >= dfirst){
            third = second;
            second = first;
            first = temp;
        }else if (dtemp > dsecond && dtemp < dfirst){
            third = second;
            second = temp;
        }else if(dtemp < dsecond && dtemp > dthird){
            third = temp;
        }

        System.out.println(first);
        System.out.println(second);
        System.out.println(third);

        editor.putString("firstplace", first);
        editor.putString("secondplace", second);
        editor.putString("thirdplace", third);
        editor.commit();

        ArrayList<String> al = new ArrayList<String>();

        al.add(0,first);
        al.add(1,second);
        al.add(2,third);
    }
}


