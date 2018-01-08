package com.example.jespe.s144211galge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class GameFragment extends Fragment implements View.OnClickListener {

    FragmentTransaction ft;
    Fragment fm;
    String wordunderscore = "", guess = "", guesslist = "", misslist = "";
    TextView wordfield, guessfield, missfield;
    long tStart, tEnd, tDelta;
    double elapsedSeconds;
    ImageView hangmanimg;
    EditText inputField;
    ProgressBar spinner;
    int numofguess = 0;
    int[] imageArray;

    //"importing" galgelogic into gameactivity
    Galgelogik spil = new Galgelogik();

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_game, container, false);

        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spil.nulstil();

        //Get more possible words if available.
        new AsyncTaskRunner().execute();

        //Spinner and progressbar
        spinner = (ProgressBar) getView().findViewById(R.id.progressBar1);

        //ImageView
        hangmanimg = (ImageView) getView().findViewById(R.id.hangman);

        //TextView
        wordfield = (TextView) getView().findViewById(R.id.wordfield);
        guessfield = (TextView) getView().findViewById(R.id.guessfield);
        missfield = (TextView) getView().findViewById(R.id.missfield);

        //TextInput
        inputField = (EditText) getView().findViewById(R.id.inputField);
        inputField.setOnClickListener(this);

        //Drawable resources
        imageArray = new int[8];
        imageArray[0] = R.drawable.forkert1;
        imageArray[1] = R.drawable.forkert2;
        imageArray[2] = R.drawable.forkert3;
        imageArray[3] = R.drawable.forkert4;
        imageArray[4] = R.drawable.forkert5;
        imageArray[5] = R.drawable.forkert6;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Fetching words
            getWordsFromDr();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Removing loading/spinner...
            spinner.setVisibility(View.GONE);

            //Setting word with underscores
            wordunderscore = spil.getSynligtOrd().replaceAll("\\*", "_ ");

            //Fixing post fence problem
            wordunderscore = wordunderscore.substring(0,wordunderscore.length() - 1);
            wordfield.setText(wordunderscore);

            //starting timer
            tStart = System.currentTimeMillis();
        }

        @Override
        protected void onPreExecute() {     }

        @Override
        protected void onProgressUpdate(String... text) {      }
    }

    //Grabbing words from dr.dk
    public void getWordsFromDr(){
        //Fixing policy for grabbing words from dr.dk
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Trying to fecth words from dr.dk
        try {
            spil.hentOrdFraDr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Updating correct and wrong guesses
    public void updateTexts(){
        System.out.println(spil.getSynligtOrd().contains(guess));
        if (spil.getSynligtOrd().contains(guess) == true) {
            guesslist += guess;
            guessfield.setText(guesslist.replaceAll(".(?!$)", "$0 ").toUpperCase());

        } else if (spil.getSynligtOrd().contains(guess) == false) {
            misslist += guess;
            missfield.setText(misslist.replaceAll(".(?!$)", "$0 ").toUpperCase());
            if(numofguess < 6) {
                numofguess = spil.getAntalForkerteBogstaver();
                hangmanimg.setImageResource(imageArray[numofguess-1]);
            }
        }
    }

    //Toast method
    public void toasts(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void result(String r){
        //Ending timer
        tEnd = System.currentTimeMillis();
        //Calculating result in seconds
        tDelta = tEnd - tStart;
        elapsedSeconds = tDelta / 1000.0;
        System.out.println("Tiden var "+elapsedSeconds);

        //Grabbing extra info and starting activity
        //Intent i = new Intent(GameFragment.this, ScoreFragment.class);
        //i.putExtra("timer", elapsedSeconds);
        //i.putExtra("result", r);
        //i.putExtra("word", spil.getOrdet());
        //i.putExtra("tries", spil.getAntalForkerteBogstaver());
        //System.out.println(spil.getAntalForkerteBogstaver());
        //startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if (inputField.getText().length() != 1){
            toasts("Please only guess one word at a time.");
        }else {

            guess = inputField.getText().toString();

            //Checking if guessed word is correct and resetting field
            if (spil.getBrugteBogstaver().contains(guess)) {
                toasts("You already guessed that!");
            } else {
                //Submit guess to logic
                spil.gætBogstav(guess);
                inputField.setText("");

                //Updating wordfield
                System.out.println(spil.getSynligtOrd().replaceAll("\\*", "_").replaceAll(".(?!$)", "$0 ").toUpperCase());
                wordfield.setText(spil.getSynligtOrd().replaceAll("\\*", "_").replaceAll(".(?!$)", "$0 ").toUpperCase());

                //Updating guessed characters
                updateTexts();
            }
        }

        //Game Status
        spil.logStatus();
        if(spil.erSpilletTabt()){
            result("loss");
        }else if(spil.erSpilletVundet()){
            result("win");
        }
    }
}