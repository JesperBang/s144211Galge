package com.example.jespe.s144211galge;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


public class PlayVsFragment extends Fragment {

    ProgressBar pb;
    ListView lv;
    Object o;
    String chosenWord;
    Fragment frag;
    FragmentTransaction ft;
    Fragment fm;

    //"importing" galgelogic into gameactivity
    Galgelogik spil = new Galgelogik();

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_play_vs, container, false);

        return v;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        pb = (ProgressBar) getActivity().findViewById(R.id.pb);
        lv = (ListView) getActivity().findViewById(R.id.lv);

        //Listener and onclick method
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenWord = spil.getMuligListOrd().get(position);
                //spil.vsNulstil(chosenWord);
                startVsGame();
            }
        });

        //Get more possible words if available.
        new AsyncTaskRunnerVs().execute();
    }

    private class AsyncTaskRunnerVs extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Fetching words
            getWordListFromDr();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Removing loading/spinner...
            pb.setVisibility(View.GONE);

            lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spil.getMuligListOrd()));


        }

        @Override
        protected void onPreExecute() {     }

        @Override
        protected void onProgressUpdate(String... text) {      }
    }

    public void startVsGame(){
        //Creating fragment and attaching bundle
        Bundle bundle = new Bundle();
        bundle.putString("sendword", chosenWord);
        frag = new GameVsFragment();
        frag.setArguments(bundle);
        ft = getFragmentManager().beginTransaction();

        //Adding fragment to transaction and terminating GameFragment
        ft.add(R.id.con_play, frag, "PlayVsScreen");
        fm = getFragmentManager().findFragmentByTag("PlayVs");
        if(fm!=null) ft.remove(fm);
        ft.commit();
    }

    //Grabbing words from dr.dk
    public void getWordListFromDr(){
        //Fixing policy for grabbing words from dr.dk
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Trying to fecth word list from dr.dk
        try {
            spil.hentMuligeOrdFraDr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}