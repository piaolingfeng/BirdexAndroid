package com.birdex.bird.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends BaseFragment {
    @Bind(R.id.iv_welcome_bg)
    public ImageView iv_welcome;
    private int imgID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgID = getArguments().getInt("imgid", 0);
    }

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_welcom_layout;
    }

    @Override
    public void initializeContentViews() {
        if (imgID != 0) {
            iv_welcome.setImageResource(imgID);
        }
    }

    @Override
    protected void lazyLoad() {

    }
}
