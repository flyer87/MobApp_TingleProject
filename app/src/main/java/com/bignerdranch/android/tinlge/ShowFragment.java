package com.bignerdranch.android.tinlge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Omer on 01.03.2016.
 */
public class ShowFragment extends Fragment {

    private  static ThingLab sThingLab;

    public interface ToActivityOnItemDeleted {public void stateChanged(Context context);}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sThingLab = ThingLab.get(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show, container, false);

        String itemValue = getActivity().getIntent().getStringExtra("itemValue");
        TextView text = (TextView) v.findViewById(R.id.del_text);
        text.setText(itemValue);

        Button mDelItemButton = (Button) v.findViewById(R.id.del_button);
        mDelItemButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemPos = getActivity().getIntent().getIntExtra("itemPos", 0);
                        String what = getActivity().getIntent().getStringExtra("itemName");
                        String where = getActivity().getIntent().getStringExtra("itemPlace");
                        sThingLab.removeAtPos(itemPos, what, where);

                        String deleteText = "Deleted "+ getActivity().getIntent().getStringExtra("itemValue");
                        Toast.makeText(getActivity(), deleteText, Toast.LENGTH_SHORT).show();

                        ((ToActivityOnItemDeleted) getActivity()).stateChanged(ShowFragment.this.getContext());
                    }
                }
        );

        return v;
    }
}
