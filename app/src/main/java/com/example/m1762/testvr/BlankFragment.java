package com.example.m1762.testvr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m1762.testvr.vrplayer.Definition;
import com.example.m1762.testvr.vrplayer.PlayActivity;
import com.example.m1762.testvr.vrplayer.utils.SPUtils;


public class BlankFragment extends Fragment {


    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        textView = (TextView) view.findViewById(R.id.paly);
//        String data = getArguments().getString("Data");
//        textView.setText(data);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"进入播放器",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(),PlayActivity.class);
                i.putExtra(Definition.KEY_PLAY_URL, "rtmp://9250.liveplay.myqcloud.com/live/9250_1111112111");
                SPUtils.put(getContext(),Definition.HISTORY_URL,"rtmp://9250.liveplay.myqcloud.com/live/9250_1111112111");
                getContext().startActivity(i);
            }
        });

        return view;
    }

}
