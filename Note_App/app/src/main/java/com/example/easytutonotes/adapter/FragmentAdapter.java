package com.example.easytutonotes.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.easytutonotes.fragment.NoteFragment;
import com.example.easytutonotes.fragment.ToDoFragment;


public class FragmentAdapter extends FragmentStatePagerAdapter {

    private int numPage = 2;

    public FragmentAdapter(@NonNull FragmentManager fm, int numPage) {
        super(fm,numPage);
        this.numPage = numPage;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new NoteFragment();
            case 1: return new ToDoFragment();
            default: return new NoteFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position){
            case 0:
                title = "Note";
                break;
            case 1:
                title = "To do";
                break;
        }
        return title;
    }
}
