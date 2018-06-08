package com.sefirah.myomer.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sefirah.myomer.R;
import com.sefirah.myomer.event_bus.Events;
import com.sefirah.myomer.event_bus.GlobalBus;
import com.sefirah.myomer.plist_parser.PListDict;
import com.sefirah.myomer.plist_parser.PListException;
import com.sefirah.myomer.plist_parser.PListParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tvTopHeading, tvQuote,tvTitleOne,tvTitleTwo,tvContent;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        tvTitleOne = (TextView) view.findViewById(R.id.tvTitleOne);
        tvTitleTwo = (TextView) view.findViewById(R.id.tvTitleTwo);
        tvContent =(TextView) view.findViewById(R.id.tvContent);
        GlobalBus.getBus().register(this);

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

        populateViews(event.getDayChangeEvent());
    }
    private void populateViews(int dayCount){
        AssetManager assetManager = getActivity().getAssets();
        try {
            InputStream stream = null;

            stream = assetManager.open("weeks/week" + dayCount + ".plist");

            PListDict dict = PListParser.parse(stream);

            String title = dict.getString("title");
            String subTitle = dict.getString("subtitle");
            String content = dict.getString("content");
            tvTitleOne.setText(title);
            tvTitleTwo.setText(subTitle);
            tvContent.setText(content);


            String color = dict.getString("color");
            tvTitleOne.setTextColor(Color.parseColor("#"+color));
            tvTitleTwo.setTextColor(Color.parseColor("#"+color));
            Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Biko_Bold.otf");
            tvTitleOne.setTypeface(type);
            tvTitleTwo.setTypeface(type);
            Typeface type1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Biko-Regular.otf");
            tvContent.setTypeface(type1);
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
