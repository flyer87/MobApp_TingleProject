package com.bignerdranch.android.tinlge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Omer on 01.03.2016.
 */
public class ListFragment extends Fragment {

    private static ThingLab sThingLab;
    private final String LIST_ITEM_PRETEXT = Thing.preText;
    private final String LIST_ITEM_POSTTEXT = Thing.postText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list, container, false);

        sThingLab = ThingLab.get(getActivity());
        List<Thing> thingList = sThingLab.getThings();

        ArrayAdapter<Thing> adapter =
                new ArrayAdapter<Thing>(getActivity(), R.layout.list_item, R.id.textViewItem, thingList );

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // new
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ListFragment.this.getContext(), Show.class);

                        String itemValue = ((TextView) view.findViewById(R.id.textViewItem)).getText().toString();
                        intent.putExtra("itemValue", itemValue);
                        intent.putExtra("itemPos", position);

                        String itemName = getWhat(itemValue);
                        intent.putExtra("itemName", itemName);

                        String itemPlace = getWhere(itemValue);
                        intent.putExtra("itemPlace", itemPlace);
                        Log.i("place", itemPlace);
                        startActivity(intent);
                    }
                }
        );

        return  v;
    }

    private String getWhat(String itemValue){
        int fromIndex = itemValue.indexOf(LIST_ITEM_PRETEXT) + LIST_ITEM_PRETEXT.length();
        int toIndex = itemValue.indexOf(LIST_ITEM_POSTTEXT) -1;
        String itemName = itemValue.substring(fromIndex, toIndex);

        return  itemName;
    }

    private String getWhere(String itemValue){
        int fromIndex = itemValue.indexOf(LIST_ITEM_POSTTEXT) +  LIST_ITEM_POSTTEXT.length();
        String itemPlace = itemValue.substring(fromIndex);

        return  itemPlace;
    }

}

