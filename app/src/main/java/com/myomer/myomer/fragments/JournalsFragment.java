package com.myomer.myomer.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myomer.myomer.R;
import com.myomer.myomer.adapters.QuestionAdapter;
import com.myomer.myomer.event_bus.Events;
import com.myomer.myomer.event_bus.GlobalBus;
import com.myomer.myomer.plist_parser.PListArray;
import com.myomer.myomer.plist_parser.PListDict;
import com.myomer.myomer.plist_parser.PListException;
import com.myomer.myomer.plist_parser.PListParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager mViewPager;
    CircleIndicator indicator;
    QuestionAdapter questionAdapter;
    TextView tvQuest;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public JournalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JournalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JournalsFragment newInstance(String param1, String param2) {
        JournalsFragment fragment = new JournalsFragment();
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
        View view = inflater.inflate(R.layout.fragment_journals, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        tvQuest = (TextView) view.findViewById(R.id.tvQuest);
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

            stream = assetManager.open("days/day" + dayCount + ".plist");

            PListDict dict = PListParser.parse(stream);
            int day = dict.getInt("day");
            int week = dict.getInt("week");
            InputStream inputStream = null;

            inputStream = assetManager.open("weeks/week" + week + ".plist");

            PListDict weekDict = PListParser.parse(inputStream);
            String color = weekDict.getString("color");

            PListArray questions = dict.getPListArray("questions");
            questionAdapter = new QuestionAdapter(this.getContext(), questions, day, color);
            mViewPager.setAdapter(questionAdapter);
            indicator.setViewPager(mViewPager);
            questionAdapter.registerDataSetObserver(indicator.getDataSetObserver());
            if (questions.size() > 0) {
                tvQuest.setVisibility(View.GONE);

            }else {
                tvQuest.setVisibility(View.VISIBLE);

                tvQuest.setText("Please check back tomorrow for more journal questions.");
                tvQuest.setTextColor(Color.parseColor("#"+color));
            }

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
