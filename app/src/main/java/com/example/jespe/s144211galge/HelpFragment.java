package com.example.jespe.s144211galge;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jespe.s144211galge.R;

public class HelpFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = i.inflate(R.layout.fragment_help, container, false);

        return v;
    }
}