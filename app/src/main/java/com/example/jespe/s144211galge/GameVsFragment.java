package com.example.jespe.s144211galge;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class GameVsFragment extends Fragment implements View.OnClickListener {

    FragmentTransaction ft;
    Fragment fm;
    String wordunderscore = "", guess = "", guesslist = "", misslist = "", scoreNumber, chosenword;
    TextView wordfield, guessfield, missfield, score, info;
    long tStart, tEnd, tDelta;
    double elapsedSeconds, temp1, temp2, temp3;
    ImageView hangmanimg;
    EditText inputField;
    ProgressBar spinner;
    int numofguess = 0;
    int[] imageArray;
    Boolean scoreBoolean;
    DecimalFormat twoDForm;
    Fragment frag;
    Bundle bundle;

    //"importing" galgelogic into gameactivity
    Galgelogik spil = new Galgelogik();

    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_game_vs, container, false);

        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        twoDForm= new DecimalFormat("#.##");



        //Spinner and progressbar
        spinner = (ProgressBar) getView().findViewById(R.id.progressBar1);

        //ImageView
        hangmanimg = (ImageView) getView().findViewById(R.id.hangman);

        //TextView
        wordfield = (TextView) getView().findViewById(R.id.wordfield);
        guessfield = (TextView) getView().findViewById(R.id.guessfield);
        missfield = (TextView) getView().findViewById(R.id.missfield);
        score = (TextView) getView().findViewById(R.id.score);
        info = (TextView) getView().findViewById(R.id.infoText);

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

        bundle = this.getArguments();

        new GameVsFragment.AsyncTaskRunnerVsG().execute();

        //Handle Back button press ingame.
        //Returning til main menu
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){return true; }
                return false;
            }
        });
    }

    private class AsyncTaskRunnerVsG extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Delay so player1 can give player2 the phone without spending time
            //which would affect the score.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Removing loading/spinner...
            //and info text.
            spinner.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
            inputField.setVisibility(View.VISIBLE);

            //Start game with chosen word.
            if (bundle != null) {
                chosenword = bundle.getString("sendword", "error");
            }
            spil.vsNulstil(chosenword);

            //Setting word with underscores
            wordunderscore = spil.getSynligtOrd().replaceAll("\\*", "_ ");

            //Fixing post fence problem
            wordunderscore = wordunderscore.substring(0,wordunderscore.length() - 1);
            wordfield.setText(wordunderscore);

            //starting timer
            tStart = System.currentTimeMillis();

            //Start thread for score calculating and showing in fragment
            score();
        }

        @Override
        protected void onPreExecute() {     }

        @Override
        protected void onProgressUpdate(String... text) {      }
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

    public void getScore(){
        //Grabbing time and converting to seconds.
        tEnd = System.currentTimeMillis();
        tDelta = tEnd- tStart;
        elapsedSeconds = tDelta / 1000.0;

        //Random "algorithm" i came up with
        //to calculate score relative to word length,
        //time spend and amount of misses.
        temp1 = elapsedSeconds*(numofguess+1);
        temp2 = temp1/(wordunderscore.length()+1);
        temp3 = (100/temp2)*100;
    }

    //Score Thread for calculating and updating textview with score
    public void score(){
        final Handler handler = new Handler();

        scoreBoolean = true;

        Runnable runnable = new Runnable() {
            public void run() {
                while (scoreBoolean) {
                    //Updating score every 1000 ms.
                    try { Thread.sleep(1000); } catch (InterruptedException e) {e.printStackTrace();}
                    handler.post(new Runnable(){
                        public void run() {
                            getScore();

                            //Formatting to get rid of a lot of decimals and updating textview
                            scoreNumber = twoDForm.format(temp3);
                            score.setText("Score: "+scoreNumber);

                            //Fail safe to stop thread from working
                            //after 500 seconds in case game isn't completed.
                            //This will still be enough to lower score close to 0
                            if (elapsedSeconds > 500){
                                scoreBoolean = false;
                            }
                        }});
                }
            };
        };
        new Thread(runnable).start();
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
        //Stopping and grabbing newest score result.
        scoreBoolean = false;
        getScore();

        //Method for hiding keyboard / Auto close keyboard after game.
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //"Sending" data to score fragment
        Bundle bundle = new Bundle();
        bundle.putString("result", r);
        bundle.putString("scoreSolo", scoreNumber);
        bundle.putString("word", spil.getOrdet());
        bundle.putInt("tries", spil.getAntalForkerteBogstaver());

        //Creating fragment and attaching bundle
        frag = new ScoreFragment();
        ft = getFragmentManager().beginTransaction();
        frag.setArguments(bundle);

        //Adding fragment to transaction and terminating GameFragment
        ft.add(R.id.con_play, frag, "ScoreScreen");
        fm = getFragmentManager().findFragmentByTag("PlayVsScreen");
        if(fm!=null) ft.remove(fm);
        ft.commit();
    }

}
