package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.ScoreAdapter;
import com.kim.ccujwc.view.utils.ShapeLoadingView;

import org.apache.commons.httpclient.HttpClient;

public class ScoreFragment extends BaseFragment {

    private LoadingView loadView;
    private ListView lvScore;
    private TextView tvTotalCredits;
    private TextView tvCompulsoryCredits;
    private TextView tvLimitCredits;
    private TextView tvProfessionalElectiveCredits;
    private TextView tvOptionalCredits;
    private TextView tvGradePointAverage;
    private TextView tvTotalStudyHours;
    private TextView tvTotalCoursesNumber;
    private TextView tvFailedCoursesNumber;

    private int connCount = 0;

    Handler getScoreErrorHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (connCount <= 3) {
                    connCount++;
                    new GetScore().execute();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("警告");
                    builder.setMessage("获取成绩失败，是否重试？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connCount = 0;
                            new GetScore().execute();
                        }
                    });
                    builder.create().show();
                }
            }
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_score, null);

        initView(view);

        new GetScore().execute();

        return view;
    }

    private void initView(View view) {
        loadView = (LoadingView) view.findViewById(R.id.loadView);
        lvScore = (ListView) view.findViewById(R.id.lv_score);

        tvTotalCredits = (TextView) view.findViewById(R.id.tv_totalCredits);
        tvCompulsoryCredits = (TextView) view.findViewById(R.id.tv_compulsoryCredits);
        tvLimitCredits = (TextView) view.findViewById(R.id.tv_limitCredits);
        tvProfessionalElectiveCredits = (TextView) view.findViewById(R.id.tv_professionalElectiveCredits);
        tvOptionalCredits = (TextView) view.findViewById(R.id.tv_optionalCredits);
        tvGradePointAverage = (TextView) view.findViewById(R.id.tv_gradePointAverage);
        tvTotalStudyHours = (TextView) view.findViewById(R.id.tv_totalStudyHours);
        tvTotalCoursesNumber = (TextView) view.findViewById(R.id.tv_totalCoursesNumber);
        tvFailedCoursesNumber = (TextView) view.findViewById(R.id.tv_failedCoursesNumber);
    }

    class GetScore extends AsyncTask<Void, Void, PersonGrade> {

        @Override
        protected PersonGrade doInBackground(Void... params) {
            if (App.personGrade != null) {
                return App.personGrade;
            }
            HttpClient client = new HttpClient();
            try {
                return MyHttpUtil.getGrade(client);
            } catch (Exception e) {
                e.printStackTrace();
                Message message = getScoreErrorHandler.obtainMessage();
                message.what = 1;
                message.sendToTarget();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            loadView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(PersonGrade result) {
            try {
                if (result != null) {
                    App.personGrade = result;
                    tvTotalCredits.setText(result.getTotalCredits());
                    tvCompulsoryCredits.setText(result.getCompulsoryCredits());
                    tvLimitCredits.setText(result.getLimitCredits());
                    tvProfessionalElectiveCredits.setText(result.getProfessionalElectiveCredits());
                    tvOptionalCredits.setText(result.getOptionalCredits());
                    tvGradePointAverage.setText(result.getGradePointAverage());
                    tvTotalStudyHours.setText(result.getTotalStudyHours());
                    tvTotalCoursesNumber.setText(result.getTotalCoursesNumber());
                    tvFailedCoursesNumber.setText(result.getFailedCoursesNumber());

                    ScoreAdapter adapter = new ScoreAdapter(getContext(), R.layout.score_list_item, result.getGradeList());
                    lvScore.setAdapter(adapter);
                    loadView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
