package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nick.myfirstapp.R;

/**
 * Created by nick on 2017/10/09.
 */

public class TodoFrag extends Fragment {

    private static final String TAG = "TodoFrag";
    private View rootView;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_todo,null);
        return rootView;
    }

}
