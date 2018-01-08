package com.example.jespe.s144211galge;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Set;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class ScoreFragment extends Fragment implements View.OnClickListener {

    ImageView resultimg;
    String resultin, wordpass, highscorenum;
    TextView yourscore, rightword;
    double timer, hsnum;
    Button returnbtn;
    int tries;
    Bundle bundle;
    Fragment fm;
    FragmentTransaction ft;
    EditText uName;
    KonfettiView conf;

    HighScore Hs = new HighScore();

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_score, container, false);

        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //ImageView Animation
        resultimg = (ImageView) getActivity().findViewById(R.id.resultimg);
        resultimg.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));

        yourscore = (TextView) getActivity().findViewById(R.id.yourscore);
        returnbtn = (Button) getActivity().findViewById(R.id.returnbtn);
        rightword = (TextView) getActivity().findViewById(R.id.word);

        uName = (EditText) getActivity().findViewById(R.id.UserName);

        conf = (KonfettiView) getActivity().findViewById(R.id.confetti);

        returnbtn.setOnClickListener(this);

        //Grabbing info from prev fragment
        bundle = this.getArguments();
        if (bundle != null) {
            highscorenum = bundle.getString("scoreSolo", "1");
            tries        = bundle.getInt("tries", 0);
            resultin     = bundle.getString("result", "error");
            wordpass     = bundle.getString("word");
        }

        //Showing either win or loss image
        setImage();

        //Makes no sense to allow back stack here
        //since that would result in the player getting
        //back to the playscreen. Might aswell submit and get to main menu.
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.returnbtn){
            //Method for hiding keyboard / Auto close keyboard before going back to main menu.
            View view = getView();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            //submit highscore
            if (resultin.contains("win")){Hs.saveHighScore(uName.getText()+": "+highscorenum, getActivity()); }

            //Close Score Fragment "returning" to tabbar fragments
            ft = getFragmentManager().beginTransaction();
            fm = getFragmentManager().findFragmentByTag("ScoreScreen");
            if(fm!=null) ft.remove(fm);
            ft.commit();
        }
    }

    public void setImage(){
        //Deciding which image to use based on win or loss
        if(resultin.contains("loss")){
            final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.loss);
            mp.start();

            resultimg.setImageResource(R.drawable.llost);
            yourscore.setText("Out of lives");
            rightword.setText("The word was: "+wordpass);
            returnbtn.setText("  Return to main menu  ");
        }else if (resultin.contains("win")){
            final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.win);
            mp.start();

            //Followed tutorial for the 3rd party lib
            //https://www.youtube.com/watch?v=9ElwO8MwUF4
            conf.build()
                    .addColors(Color.GREEN, Color.WHITE, Color.MAGENTA, Color.YELLOW, Color.BLUE)
                    .setDirection(0.0,359.0)
                    .setSpeed(1f, 8f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(1500L)
                    .addShapes(Shape.CIRCLE,Shape.RECT)
                    .addSizes(new Size(14, 5f))
                    .setPosition(-50f, conf.getWidth() + 50f, -50f, -50f)
                    .stream(200,3000L);

            uName.setVisibility(View.VISIBLE);
            resultimg.setImageResource(R.drawable.winner);
            yourscore.setText(highscorenum+" with "+tries+" misses.");
        }else if(resultin.contains("error")){
            yourscore.setText("An error occurred");
        }
    }
}