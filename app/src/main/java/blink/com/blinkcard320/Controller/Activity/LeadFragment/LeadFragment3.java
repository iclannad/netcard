package blink.com.blinkcard320.Controller.Activity.LeadFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import blink.com.blinkcard320.Controller.Activity.Login;
import blink.com.blinkcard320.Controller.Activity.SplashActivity;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Administrator on 2017/4/5.
 */
public class LeadFragment3 extends Fragment {

    private TextView fragment3Text = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leadfragment3, container, false);

        fragment3Text = (TextView) view.findViewById(R.id.fragment3Text);

        fragment3Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtils.setBooleanPreference(getActivity(), SplashActivity.SHAREDPREFERENCES_NAME, false);
                Intent intent = new Intent(getActivity(), Login.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
