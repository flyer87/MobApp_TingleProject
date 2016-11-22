package com.bignerdranch.android.tinlge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;

import com.bignerdranch.android.tinlge.database.ThingDbSchema;

import java.util.List;

public class TingleFragment extends Fragment {
    private Button addThing, searchThing,  mListActivities, mScanner;
    private TextView lastAdded, newWhat, newWhere;
    private CheckBox importnat;
    private static ThingLab sThingLab;

    private final int REQUEST_SCAN_BARCODE = 0;
    private final int MAX_SEARCH_RESULTS = 3;
    private final String DEFAULT_ORDER_ASC = " asc";
    private final String SEARCH_RESULT_PRE = "MORE THAN ";
    private final String SEARCH_RESULT_POST = " RESULTS FOUND!";
    private final String ZXING_CLIENT_SCAN = "com.google.zxing.client.android.SCAN";

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
//    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static String sPref = null;

    // The BroadcastReceiver that tracks network connectivity changes.
    // private NetworkReceiver receiver = new NetworkReceiver();


    public interface ToActivityOnDataStateChanged {public  void  stateChange();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Registers BroadcastReceiver to track network connection changes.
        // IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        // receiver = new NetworkReceiver();
        // getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, container, false);

        sThingLab = ThingLab.get(getActivity());
        lastAdded = (TextView) v.findViewById(R.id.last_thing);
        updateUI();

        newWhat = (EditText) v.findViewById(R.id.what_text);
        newWhat.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            // nothing
                        }

                        return false;
                    }
                }
        );

        newWhere = (EditText)v.findViewById(R.id.where_text);
        importnat = (CheckBox) v.findViewById(R.id.thing_important);

        addThing = (Button) v.findViewById(R.id.add_button);
        addThing.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newWhat.getText().toString().trim().length() > 0
                                && newWhere.getText().toString().trim().length() > 0) {
                            Integer isImportant = 0;
                            if (importnat.isChecked()) isImportant = 1;
                            sThingLab.addThing(
                                    new Thing(newWhat.getText().toString().trim(), newWhere.getText().toString().trim(), isImportant)
                            );

                            int orientation = getResources().getConfiguration().orientation;
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                ((ToActivityOnDataStateChanged) getActivity()).stateChange();
                             }

                            updateUI();
                        }

                        newWhat.setText("");
                        newWhere.setText("");
                        importnat.setChecked(false);


                    }
                }
        );

        searchThing = (Button) v.findViewById(R.id.search_button);
        searchThing.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!newWhat.getText().toString().isEmpty()) {
                            String searchString = newWhat.getText().toString().trim();
                            new SearchThingTask().execute(searchString);
                        }
                    }
                }
        );

        int orientation = getResources().getConfiguration().orientation;
        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mListActivities = (Button) v.findViewById(R.id.list_button);
            mListActivities.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TingleFragment.this.getContext(), ListActivity.class);
                            startActivity(intent);
                        }
                    }
            );
        }
        else {
            mListActivities = (Button) v.findViewById(R.id.list_button);
            mListActivities.setVisibility(View.GONE);
        }

        // Scan for a barcode
        mScanner = (Button) v.findViewById(R.id.scan_button);
        try {
            mScanner.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ZXING_CLIENT_SCAN);
                            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                            startActivityForResult(intent, REQUEST_SCAN_BARCODE);
                        }
                    }
            );
        } catch (Exception e) {
            Log.i("onCreate", "Scanner Not Found", e);
        }

        return  v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_BARCODE){
            if (resultCode == Activity.RESULT_OK) {
                String content = data.getStringExtra("SCAN_RESULT");

                if (hasConnectivity()){ // check for new netw
                    //Log.i("async task call", "before");
                    new FetchOutpanTask().execute(content);
                    //Log.i("async task call", "after");
                }
                else {
                    Toast.makeText(getActivity(), "Problem occurred with the Internet connection! \n" + "Check your connectivity and your settings." , Toast.LENGTH_LONG).show();
                }

                //newWhat.setText(content);
                newWhere.requestFocus();
            }
        }
    }

    private boolean hasConnectivity() {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        return (networkInfo != null && networkInfo.isConnected());

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("pref_key_connection_type_list", "WIFI");

        updateConnectedFlags();

        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            return  true;
        }

        return  false;
    }

    // new netw
    public void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    private void updateUI() {
        Thing lastThing = ThingLab.getLastThing();

        if (lastThing != null) {
            lastAdded.setText(lastThing.toString());
        }

        //int size = sThingLab.size();
        //if (size > 0){
            //Thing lastThing = ThingLab.getLastThing();
            //lastAdded.setText(ThingLab.getLastThing().toString());
        //}
    }

    private class FetchOutpanTask extends AsyncTask<String,Void,String>{

        @Override
        // Object -> Void
        protected String doInBackground(String... params) {
            String result = new OutpanFetcher().getProductInfo((String)params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("onPostExecute input", s);
            newWhat.setText(s);
        }
    }

    private class SearchThingTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String pref_old = sharedPref.getString("pref_order_old", DEFAULT_ORDER_ASC);
            Boolean pref_important = sharedPref.getBoolean("pref_order_important", true);

            String orderClause = "";
            if (pref_important) orderClause += ThingDbSchema.ThingTable.Cols.IMPORTANT + " desc , " ;
            orderClause += ThingDbSchema.ThingTable.Cols.ID + " " + pref_old;
            Log.i("order ", orderClause);

            String searchString = params[0];
            List<Thing> searchResult = sThingLab.searchThingsByNameAndOrder(searchString, orderClause);

            String resultStr = "";
            int resultCnt = 0;
            for (Thing thing : searchResult) {
                resultCnt++;
                if (resultCnt <= MAX_SEARCH_RESULTS)
                    resultStr += thing.toString() + "\n";
            }

            if (resultCnt > MAX_SEARCH_RESULTS)
                resultStr += SEARCH_RESULT_PRE + " " + MAX_SEARCH_RESULTS + " " + SEARCH_RESULT_POST;

            if (resultCnt > 0) return resultStr;
                else return "";
        }

        @Override
        protected void onPostExecute(String searchResult) {
            if (!searchResult.isEmpty()){
                Toast.makeText(getActivity(), searchResult, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.app_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_settings:
                Intent intent = new Intent(TingleFragment.this.getContext(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // new netw
    @Override
    public void onResume() {
        super.onResume();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        // check new Network settings
        //Toast.makeText(getActivity(), "Resumed!", Toast.LENGTH_LONG).show();
    }
}
