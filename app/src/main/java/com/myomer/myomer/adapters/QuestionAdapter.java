package com.myomer.myomer.adapters;


        import android.app.Activity;
        import android.content.Context;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebView;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;


        import com.myomer.myomer.R;
        import com.myomer.myomer.activities.HomeActivity;
        import com.myomer.myomer.fragments.BlessingsFragment;
        import com.myomer.myomer.models.JournalQuestionModel;
        import com.myomer.myomer.models.RecordBlessing;
        import com.myomer.myomer.plist_parser.PListArray;
        import com.myomer.myomer.plist_parser.PListDict;
        import com.myomer.myomer.plist_parser.PListException;
        import com.myomer.myomer.realm.RealmController;
        import com.myomer.myomer.utilty.Utilty;

        import java.util.Date;
        import java.util.List;
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
    boolean addToDB = false;
    public QuestionAdapter(Context context, PListArray questions,int day) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.questions = questions;
        this.day = day;
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
            tvQuestion.setText(quest);
            JournalQuestionModel journalQuestionModel = RealmController.with((Activity) mContext).getAnswer(day,questionId);
            if (journalQuestionModel != null)
            etAnswer.setText(journalQuestionModel.getAnswer());
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

                    }, 3000);
                    if (addToDB == true) {
                        Realm realm = RealmController.with((Activity) mContext).getRealm();
                        realm.beginTransaction();
                        JournalQuestionModel rb = new JournalQuestionModel();

                        rb.setId(day);
                        rb.setAnswer(editable.toString());
                        rb.setQuestionId(questionId);
                        rb.setYear(Utilty.getYear(new Date()));
                        realm.copyToRealmOrUpdate(rb);
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

