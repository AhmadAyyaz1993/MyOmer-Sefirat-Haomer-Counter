package com.myomer.myomer.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.myomer.myomer.R;
import com.myomer.myomer.event_bus.Events;
import com.myomer.myomer.event_bus.GlobalBus;
import com.myomer.myomer.models.RecordBlessing;
import com.myomer.myomer.plist_parser.PListDict;
import com.myomer.myomer.plist_parser.PListException;
import com.myomer.myomer.plist_parser.PListParser;
import com.myomer.myomer.realm.RealmController;
import com.myomer.myomer.utilty.Utilty;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlessingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlessingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlessingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tvBlessings,tvQuote, tvSefirah,tvLocale,tvBlessingRecord;
    TextView wvBlessings;
    SwitchCompat recordBlessing;
    boolean changeSwitch = true;
    String selectedLocale = "en";
    int day;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String color;

    public BlessingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlessingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlessingsFragment newInstance(String param1, String param2) {
        BlessingsFragment fragment = new BlessingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blessings, container, false);

        wvBlessings = (TextView) view.findViewById(R.id.wvBlessings);
        tvQuote = (TextView) view.findViewById(R.id.tvQuote);
        tvSefirah = (TextView) view.findViewById(R.id.tvSefirah);
        recordBlessing = (SwitchCompat) view.findViewById(R.id.simpleSwitch);
        tvLocale = (TextView) view.findViewById(R.id.tvLocale);
        tvBlessingRecord = (TextView) view.findViewById(R.id.tvBlessingRecord);
        GlobalBus.getBus().register(this);
        wvBlessings.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return(event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        wvBlessings.setScrollContainer(false);
        recordBlessing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && changeSwitch == true){
                    Realm realm = RealmController.with(BlessingsFragment.this).getRealm();
                    realm.beginTransaction();
                    RecordBlessing rb = new RecordBlessing();
                    rb.setId(day);
                    rb.setRecorded(b);
                    rb.setYear(Utilty.getYear(new Date()));
                    realm.copyToRealmOrUpdate(rb);
                    realm.commitTransaction();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    recordBlessing.getTrackDrawable().setColorFilter(b ? Color.parseColor("#"+color) : Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    recordBlessing.getThumbDrawable().setColorFilter(b ? Color.LTGRAY : Color.LTGRAY, PorterDuff.Mode.MULTIPLY);


                }
            }
        });

        tvLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Language");

//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
//                arrayAdapter.add("Hebrew");
//                arrayAdapter.add("English");
//                arrayAdapter.add("Russian");
//                arrayAdapter.add("Spanish");
//                arrayAdapter.add("French");

                final String[] arrayAdapter = {"Hebrew",
                "English",
                "Russian",
                "Spanish",
                "French"};

                int itemToBeSelected = 0;

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                if (selectedLocale == "en"){
                    itemToBeSelected = 1;
                }else if (selectedLocale == "hb"){
                    itemToBeSelected = 0;
                }
                else if (selectedLocale == "ru"){
                    itemToBeSelected = 2;
                }
                else if (selectedLocale == "es"){
                    itemToBeSelected = 3;
                }
                else if (selectedLocale == "fr"){
                    itemToBeSelected = 4;
                }
                builderSingle.setSingleChoiceItems(arrayAdapter,itemToBeSelected ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter[which];
                        if (strName.equals("English")){
                            selectedLocale = "en";
                        }else if (strName.equals("Hebrew")){
                            selectedLocale = "hb";
                        }else if (strName.equals("Russian")){
                            selectedLocale = "ru";
                        }else if (strName.equals("Spanish")){
                            selectedLocale = "es";
                        }else if (strName.equals("French")){
                            selectedLocale = "fr";
                        }
                        tvLocale.setText(selectedLocale);
                        populateViews(day);
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                builderSingle.show();
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDayChangeEvent(Events.DayChangeEvent event) {
        day = event.getDayChangeEvent();
        RecordBlessing recordedBlessing = RealmController.with(BlessingsFragment.this).getRecorderBlessing(day);
        if (recordedBlessing != null && recordedBlessing.isRecorded() == true) {
            changeSwitch = false;
            recordBlessing.setChecked(recordedBlessing.isRecorded());
        }else {
            recordBlessing.setChecked(false);
            changeSwitch = true;
        }
        populateViews(event.getDayChangeEvent());
    }
    private void populateViews(int dayCount){
        AssetManager assetManager = getActivity().getAssets();
        try {
            InputStream stream = null;

            stream = assetManager.open("days/day" + dayCount + ".plist");

            PListDict dict = PListParser.parse(stream);
            int week = dict.getInt("week");
            PListDict quote = dict.getPListDict("quote");
            String quoteString = quote.getString(selectedLocale);
            tvQuote.setText(quoteString);
            PListDict sefirah = dict.getPListDict("sefirah");
            String sefirahText = sefirah.getString(selectedLocale);
            tvSefirah.setText(sefirahText);

            InputStream is = assetManager.open("blessings/blessings."+selectedLocale+".html");

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String str = new String(buffer);
            str = str.replace("%%DYNAMIC1%%", quoteString);
            str = str.replace("%%DYNAMIC2%%", sefirahText);

            //Now instead of webview.loadURL(""), I needed to do something like -
            //wvBlessings.loadDataWithBaseURL("file:///android_asset/", str, "text/html", "UTF-8",null);
            wvBlessings.setText(android.text.Html.fromHtml(str).toString());
            InputStream inputStream = null;

            inputStream = assetManager.open("weeks/week" + week + ".plist");

            PListDict weekDict = PListParser.parse(inputStream);
            String color = weekDict.getString("color");
            tvLocale.setBackgroundColor(Color.parseColor("#"+color));
            tvBlessingRecord.setTextColor(Color.parseColor("#"+color));
            tvQuote.setTextColor(Color.parseColor("#"+color));
            tvSefirah.setTextColor(Color.parseColor("#"+color));
            this.color = color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                recordBlessing.getTrackDrawable().setColorFilter(recordBlessing.isChecked() ? Color.parseColor("#"+color) : Color.GRAY, PorterDuff.Mode.MULTIPLY);
                recordBlessing.getThumbDrawable().setColorFilter(recordBlessing.isChecked() ? Color.LTGRAY : Color.LTGRAY, PorterDuff.Mode.MULTIPLY);


            }
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Biko_Bold.otf");
            tvQuote.setTypeface(type);
            tvSefirah.setTypeface(type);
            tvBlessingRecord.setTypeface(type);
            Typeface type1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Biko_Regular.otf");
            wvBlessings.setTypeface(type1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PListException e) {
            e.printStackTrace();
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
