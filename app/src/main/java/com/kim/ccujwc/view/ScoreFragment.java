package com.kim.ccujwc.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.Grade;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.MySharedPreferences;
import com.kim.ccujwc.view.utils.ScoreAdapter;

import java.util.List;
import java.util.Map;

public class ScoreFragment extends BaseFragment {

    private static final String TAG = "ScoreFragment";

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
    private TextView tvSaveTime;
    private LinearLayout llRefresh;

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
        initEvent();
        MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
        if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
            new GetLocalScore().execute();
        else
            new GetScore().execute();

        return view;
    }

    private void initEvent() {
        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.clearLocalPersonGrade();
                new GetScore().execute();
            }
        });
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
        tvSaveTime = (TextView) view.findViewById(R.id.tv_saveTime);
        llRefresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
    }

    class GetLocalScore extends AsyncTask<Void, Void, PersonGrade> {

        @Override
        protected PersonGrade doInBackground(Void... params) {
            try {
                PersonGrade personGrade = null;
                MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                Map<String, Object> map = msp.readPersonGrade();
                personGrade = (PersonGrade) map.get("persongrade");
                if (personGrade.getGradeList().size() > 0) {
                    return personGrade;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            loadView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(PersonGrade personGrade) {
            try {
                if (personGrade != null) {
                    Log.d(TAG, "获取本地成绩!");
                    setPersonGradeView(personGrade);
                    MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                    Map<String, Object> map = msp.readPersonGrade();
                    String saveTime = (String) map.get("savetime");
                    tvSaveTime.setText(saveTime);
                    loadView.setVisibility(View.GONE);
                } else {
                    new GetScore().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new GetScore().execute();
            }
            super.onPostExecute(personGrade);
        }
    }

    class GetScore extends AsyncTask<Void, Void, PersonGrade> {

        @Override
        protected PersonGrade doInBackground(Void... params) {
            if (App.getLocalPersonGrade() != null) {
                return (PersonGrade) App.getLocalPersonGrade().get("persongrade");
            }
            try {
                return MyHttpUtil.getGrade();
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
                    MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                    if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
                        msp.savePersonGrade(result);
                    else
                        App.setLocalPersonGrade(result);
                    setPersonGradeView(result);
                    if (App.getLocalPersonGrade() != null) {
                        tvSaveTime.setText((String) App.getLocalPersonGrade().get("savetime"));
                    } else {
                        tvSaveTime.setText("刚刚");
                    }
                    loadView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    private void setPersonGradeView(PersonGrade result) {
        tvTotalCredits.setText(result.getTotalCredits());
        tvCompulsoryCredits.setText(result.getCompulsoryCredits());
        tvLimitCredits.setText(result.getLimitCredits());
        tvProfessionalElectiveCredits.setText(result.getProfessionalElectiveCredits());
        tvOptionalCredits.setText(result.getOptionalCredits());
        tvGradePointAverage.setText(result.getGradePointAverage());
        tvTotalStudyHours.setText(result.getTotalStudyHours());
        tvTotalCoursesNumber.setText(result.getTotalCoursesNumber());
        tvFailedCoursesNumber.setText(result.getFailedCoursesNumber());

        final List<Grade> grades = result.getGradeList();

        ScoreAdapter adapter = new ScoreAdapter(getContext(), R.layout.score_list_item, grades);
        lvScore.setAdapter(adapter);

        lvScore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grade grade = grades.get(position);
                String str = "";
                str = "课程编号:" + grade.getCourseNumber() +
                    "\n课程名称:" + grade.getCourseName() +
                    "\n学分:" + grade.getCredit() +
                    "\n学时:" + grade.getPeriod() +
                    "\n课程性质:" + grade.getCourseNature() +
                    "\n课程类别:" + grade.getCourseType() +
                    "\n考试性质:" + grade.getExamNature() +
                    "\n考核方式:" + grade.getExamMethod() +
                    "\n开课学期:" + grade.getSemester() +
                    "\n分数:" + grade.getSocre()+
                    "\n标志:" + grade.getMark();
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
            }
        });

        lvScore.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Grade grade = grades.get(position);
                String str = "";
                str = "课程编号:" + grade.getCourseNumber() +
                        "\n课程名称:" + grade.getCourseName() +
                        "\n学分:" + grade.getCredit() +
                        "\n学时:" + grade.getPeriod() +
                        "\n课程性质:" + grade.getCourseNature() +
                        "\n课程类别:" + grade.getCourseType() +
                        "\n考试性质:" + grade.getExamNature() +
                        "\n考核方式:" + grade.getExamMethod() +
                        "\n开课学期:" + grade.getSemester() +
                        "\n分数:" + grade.getSocre()+
                        "\n标志:" + grade.getMark();
                try {
                    ClipboardManager c = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                    c.setPrimaryClip(ClipData.newPlainText("成绩详情", str));
                    Toast.makeText(getContext(),"复制成绩详情成功!",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(),"复制成绩详情失败!",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}
