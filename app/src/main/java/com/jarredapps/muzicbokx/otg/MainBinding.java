// Generated file. Do not modify.
package com.jarredapps.muzicbokx.otg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.*;

public final class MainBinding {
    public final LinearLayout rootView;
    public final LinearLayout linear1;
    public final LinearLayout linear3;
    public final Button openButton;
    public final Button loadBgButton;
    public final HorizontalScrollView hscroll2;
    public final LinearLayout linear6;
    public final ImageButton play;
    public final ImageButton pause;
    public final ImageButton rewind;
    public final ImageButton stop;
    public final ImageButton forward;
	public final CheckBox loopBox;
    public final LinearLayout linear5;
    public final Button aboutButton;
    public final LinearLayout linear2;
    public final LinearLayout linear7;
    public final TextView albumName;
    public final TextView timeCode;
	public final LinearLayout mainBgLayout;
    public final VideoView bg;

    private MainBinding(LinearLayout rootView, LinearLayout linear1, LinearLayout linear3, Button openButton, Button loadBgButton, HorizontalScrollView hscroll2, LinearLayout linear6, ImageButton play, ImageButton pause, ImageButton rewind, ImageButton stop, ImageButton forward, CheckBox loopBox, LinearLayout linear5, Button aboutButton, LinearLayout linear2, LinearLayout linear7, TextView albumName, TextView timeCode, LinearLayout mainBgLayout, VideoView bg) {
        this.rootView = rootView;
        this.linear1 = linear1;
        this.linear3 = linear3;
        this.openButton = openButton;
        this.loadBgButton = loadBgButton;
        this.hscroll2 = hscroll2;
        this.linear6 = linear6;
        this.play = play;
        this.pause = pause;
        this.rewind = rewind;
        this.stop = stop;
        this.forward = forward;
		this.loopBox = loopBox;
        this.linear5 = linear5;
        this.aboutButton = aboutButton;
        this.linear2 = linear2;
        this.linear7 = linear7;
        this.albumName = albumName;
        this.timeCode = timeCode;
		this.mainBgLayout = mainBgLayout;
        this.bg = bg;
    }

    public LinearLayout getRoot() {
        return rootView;
    }

    public static MainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static MainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.main, parent, false);
        if (attachToParent) parent.addView(root);
        return bind(root);
    }

    public static MainBinding bind(View view) {
        LinearLayout rootView = (LinearLayout) view;
        LinearLayout linear1 = findChildViewById(view, R.id.linear1);
        LinearLayout linear3 = findChildViewById(view, R.id.linear3);
        Button openButton = findChildViewById(view, R.id.openButton);
        Button loadBgButton = findChildViewById(view, R.id.loadBgButton);
        HorizontalScrollView hscroll2 = findChildViewById(view, R.id.hscroll2);
        LinearLayout linear6 = findChildViewById(view, R.id.linear6);
        ImageButton play = findChildViewById(view, R.id.play);
        ImageButton pause = findChildViewById(view, R.id.pause);
        ImageButton rewind = findChildViewById(view, R.id.rewind);
        ImageButton stop = findChildViewById(view, R.id.stop);
        ImageButton forward = findChildViewById(view, R.id.forward);
		CheckBox loopBox = findChildViewById(view, R.id.loopBox);
        LinearLayout linear5 = findChildViewById(view, R.id.linear5);
        Button aboutButton = findChildViewById(view, R.id.aboutButton);
        LinearLayout linear2 = findChildViewById(view, R.id.linear2);
        LinearLayout linear7 = findChildViewById(view, R.id.linear7);
        TextView albumName = findChildViewById(view, R.id.albumName);
        TextView timeCode = findChildViewById(view, R.id.timeCode);
		LinearLayout mainBgLayout = findChildViewById(view, R.id.mainBgLayout);
        VideoView bg = findChildViewById(view, R.id.bg);

        if (linear1 == null || linear3 == null || openButton == null || loadBgButton == null || hscroll2 == null || linear6 == null || play == null || pause == null || rewind == null || stop == null || forward == null || linear5 == null || aboutButton == null || linear2 == null || linear7 == null || albumName == null || timeCode == null || bg == null) {
             throw new IllegalStateException("Required views are missing");
        }

        return new MainBinding(rootView, linear1, linear3, openButton, loadBgButton, hscroll2, linear6, play, pause, rewind, stop, forward, loopBox, linear5, aboutButton, linear2, linear7, albumName, timeCode, mainBgLayout, bg);
    }

    private static <T extends View> T findChildViewById(View rootView, int id) {
         if (rootView instanceof ViewGroup) {
              ViewGroup rootViewGroup = (ViewGroup) rootView;
              for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
                   T view = rootViewGroup.getChildAt(i).findViewById(id);
                   if (view != null) return view;
              }
         }
         return null;
    }
}
