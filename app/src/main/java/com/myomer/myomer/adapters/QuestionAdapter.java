package com.sefirah.myomer.adapters;


        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.RelativeLayout;
        import android.widget.TextView;


        import com.sefirah.myomer.R;
        import com.sefirah.myomer.models.JournalQuestionModel;
        import com.sefirah.myomer.plist_parser.PListArray;
        import com.sefirah.myomer.plist_parser.PListDict;
        import com.sefirah.myomer.plist_parser.PListException;
        import com.sefirah.myomer.realm.RealmController;
        import com.sefirah.myomer.utilty.Utilty;

        import java.util.Date;
        import java.util.Timer;
        import java.util.TimerTask;

        import io.realm.Realm;


/**
 * Created by Ahmad on 3/16/17.
 */

public class QuestionAdapter extends android.support.v4.view.PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    private PListArray questions;
    int day;
    String color;
    boolean addToDB = false;
    public QuestionAdapter(Context context, PListArray questions,int day, String color) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.questions = questions;
        this.day = day;
        this.color = color;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.question_item_view, container, false);

        TextView tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
        EditText etAnswer = (EditText) itemView.findViewById(R.id.etAnsuwer);
        try {
            PListDict question = questions.getPListDict(position);
            final int questionId = question.getInt("id");
            String quest = question.getString("question");
            if (questions.size() != 0)
                tvQuestion.setText(TextUtils.isEmpty(quest) ? "Please check back tomorrow for more journal questions.":quest);
            else
                tvQuestion.setText("Please check back tomorrow for more journal questions.");
            tvQuestion.setTextColor(Color.parseColor("#"+color));
            final JournalQuestionModel journalQuestionModel = RealmController.with((Activity) mContext).getAnswer(day,questionId);
            if (journalQuestionModel != null)
            etAnswer.setText(journalQuestionModel.getAnswer());
            Typeface type = Typeface.createFromAsset(mContext.getAssets(),"fonts/Biko_Bold.otf");
            tvQuestion.setTypeface(type);
            Typeface type1 = Typeface.createFromAsset(mContext.getAssets(),"fonts/Biko_Regular.otf");
            etAnswer.setTypeface(type1);
            etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(final Editable editable) {

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // TODO: do what you need here (refresh list)
                            // you will probably need to use
                            // runOnUiThread(Runnable action) for some specific
                            // actions

                            addToDB = true;
                        }

                    }, 500);
                    if (addToDB == true) {
                        Realm realm = RealmController.with((Activity) mContext).getRealm();
                        realm.beginTransaction();
                        JournalQuestionModel rb = new JournalQuestionModel();

                        rb.setUniqueId((long) (System.currentTimeMillis()+Math.random()));
                        rb.setId(day);
                        rb.setAnswer(editable.toString());
                        rb.setQuestionId(questionId);
                        rb.setYear(Utilty.getYear(new Date()));
                        if (journalQuestionModel != null && journalQuestionModel.getId() == day && journalQuestionModel.getQuestionId() == questionId)
                            realm.copyToRealmOrUpdate(rb);
                        else
                            realm.copyToRealm(rb);
                        realm.commitTransaction();
                    }

                }
            });
        } catch (PListException e) {
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

