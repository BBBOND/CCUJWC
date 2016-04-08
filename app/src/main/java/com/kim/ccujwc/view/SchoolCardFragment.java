package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.SchoolCard;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.MySharedPreferences;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SchoolCardFragment extends BaseFragment {


    private static final String TAG = "SchoolCardFragment";

    private ImageView ivHead;
    private AppCompatTextView tvFullName;
    private AppCompatTextView tvSex;
    private AppCompatTextView tvNation;
    private AppCompatTextView tvDateOfBirth;
    private AppCompatTextView tvHometown;
    private AppCompatTextView tvFamily;
    private AppCompatTextView tvPoliticalLandscape;
    private AppCompatTextView tvPhone;
    private AppCompatTextView tvDepartment;
    private AppCompatTextView tvMajor;
    private AppCompatTextView tvClass;
    private AppCompatTextView tvLengthOfSchooling;
    private AppCompatTextView tvAdmissionDate;
    private AppCompatTextView tvGraduationDate;
    private AppCompatTextView tvProfessionalDirection;
    private AppCompatTextView tvStudentID;
    private AppCompatTextView tvLearningForm;
    private AppCompatTextView tvLearningLevel;
    private AppCompatTextView tvJoinLeagueTimeAndPlace;
    private AppCompatTextView tvPreSchoolEducation;
    private AppCompatTextView tvForeignLanguageTypes;
    private AppCompatTextView tvPreWorkUnit;
    private AppCompatTextView tvPosition;
    private AppCompatTextView tvAddress;
    private AppCompatTextView tvStationGetOff;
    private AppCompatTextView tvPostcode;
    private AppCompatTextView tvHomePhone;
    private AppCompatTextView tvContacts;
    private AppCompatTextView tvEntranceExam;
    private AppCompatTextView tvIdCardNum;
    private AppCompatTextView tvStudentIDCardNum;
    private AppCompatTextView tvGraduationCertificateNum;
    private AppCompatTextView tvGraduationCardNum;
    private LoadingView loadView;

    private TextView tvSaveTime;
    private LinearLayout llRefresh;

    private int requestCount = 0;

    Handler GetSchoolCardErrorHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (requestCount <= 3) {
                    new GetSchoolCard().execute();
                    requestCount++;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("警告");
                    builder.setMessage("获取课表失败，是否重试？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestCount = 0;
                            new GetSchoolCard().execute();
                        }
                    });
                    builder.create().show();
                }
            }
            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_school_card, null);
        initView(view);
        initEvent();
        MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
        if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
            new GetLocalSchoolCard().execute();
        else
            new GetSchoolCard().execute();

        new GetStudentImage().execute();
        return view;
    }

    private void initEvent() {
        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.clearLocalSchoolCard();
                new GetSchoolCard().execute();
            }
        });
    }

    private void initView(View view) {
        loadView = (LoadingView) view.findViewById(R.id.loadView);
        ivHead = (ImageView) view.findViewById(R.id.iv_head);
        tvFullName = (AppCompatTextView) view.findViewById(R.id.tv_fullName);
        tvSex = (AppCompatTextView) view.findViewById(R.id.tv_sex);
        tvNation = (AppCompatTextView) view.findViewById(R.id.tv_nation);
        tvDateOfBirth = (AppCompatTextView) view.findViewById(R.id.tv_dateOfBirth);
        tvHometown = (AppCompatTextView) view.findViewById(R.id.tv_hometown);
        tvFamily = (AppCompatTextView) view.findViewById(R.id.tv_family);
        tvPoliticalLandscape = (AppCompatTextView) view.findViewById(R.id.tv_politicalLandscape);
        tvPhone = (AppCompatTextView) view.findViewById(R.id.tv_phone);
        tvDepartment = (AppCompatTextView) view.findViewById(R.id.tv_department);
        tvMajor = (AppCompatTextView) view.findViewById(R.id.tv_major);
        tvClass = (AppCompatTextView) view.findViewById(R.id.tv_class);
        tvLengthOfSchooling = (AppCompatTextView) view.findViewById(R.id.tv_lengthOfSchooling);
        tvAdmissionDate = (AppCompatTextView) view.findViewById(R.id.tv_admissionDate);
        tvGraduationDate = (AppCompatTextView) view.findViewById(R.id.tv_graduationDate);
        tvProfessionalDirection = (AppCompatTextView) view.findViewById(R.id.tv_professionalDirection);
        tvStudentID = (AppCompatTextView) view.findViewById(R.id.tv_studentID);
        tvLearningForm = (AppCompatTextView) view.findViewById(R.id.tv_learningForm);
        tvLearningLevel = (AppCompatTextView) view.findViewById(R.id.tv_learningLevel);
        tvJoinLeagueTimeAndPlace = (AppCompatTextView) view.findViewById(R.id.tv_joinLeagueTimeAndPlace);
        tvPreSchoolEducation = (AppCompatTextView) view.findViewById(R.id.tv_preSchoolEducation);
        tvForeignLanguageTypes = (AppCompatTextView) view.findViewById(R.id.tv_foreignLanguageTypes);
        tvPreWorkUnit = (AppCompatTextView) view.findViewById(R.id.tv_preWorkUnit);
        tvPosition = (AppCompatTextView) view.findViewById(R.id.tv_position);
        tvAddress = (AppCompatTextView) view.findViewById(R.id.tv_address);
        tvStationGetOff = (AppCompatTextView) view.findViewById(R.id.tv_stationGetOff);
        tvPostcode = (AppCompatTextView) view.findViewById(R.id.tv_postcode);
        tvHomePhone = (AppCompatTextView) view.findViewById(R.id.tv_homePhone);
        tvContacts = (AppCompatTextView) view.findViewById(R.id.tv_contacts);
        tvEntranceExam = (AppCompatTextView) view.findViewById(R.id.tv_entranceExam);
        tvIdCardNum = (AppCompatTextView) view.findViewById(R.id.tv_idCardNum);
        tvStudentIDCardNum = (AppCompatTextView) view.findViewById(R.id.tv_studentIDCardNum);
        tvGraduationCertificateNum = (AppCompatTextView) view.findViewById(R.id.tv_graduationCertificateNum);
        tvGraduationCardNum = (AppCompatTextView) view.findViewById(R.id.tv_graduationCardNum);
        tvSaveTime = (TextView) view.findViewById(R.id.tv_saveTime);
        llRefresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
    }

    class GetLocalSchoolCard extends AsyncTask<Void, Void, SchoolCard> {

        @Override
        protected SchoolCard doInBackground(Void... params) {
            try {
                SchoolCard schoolCard = null;
                MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                Map<String, Object> map = msp.readSchoolCard();
                schoolCard = (SchoolCard) map.get("schoolcard");
                if (schoolCard.getFullName() != null && !schoolCard.getFullName().equals(""))
                    return schoolCard;
                else
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                loadView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(SchoolCard schoolCard) {
            try {
                if (schoolCard != null) {
                    Log.d(TAG, "获取本地学籍卡片!");
                    setSchoolCardView(schoolCard);
                    MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                    String saveTime = (String) msp.readSchoolCard().get("savetime");
                    tvSaveTime.setText(saveTime);
                    loadView.setVisibility(View.GONE);
                } else {
                    new GetSchoolCard().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new GetSchoolCard().execute();
            }
            super.onPostExecute(schoolCard);
        }
    }

    class GetSchoolCard extends AsyncTask<Void, Void, SchoolCard> {

        @Override
        protected SchoolCard doInBackground(Void... params) {
            if (App.getLocalSchoolCard() != null) {
                return (SchoolCard) App.getLocalSchoolCard().get("schoolcard");
            }

            try {
                return MyHttpUtil.getSchoolCard();
            } catch (Exception e) {
                Message message = GetSchoolCardErrorHandler.obtainMessage();
                message.what = 1;
                message.sendToTarget();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                loadView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(SchoolCard schoolCard) {
            try {
                if (schoolCard != null) {
                    MySharedPreferences msp = MySharedPreferences.getInstance(getContext());
                    if ((boolean) msp.readLoginInfo().get("isAutoLogin"))
                        msp.saveSchoolCard(schoolCard);
                    else
                        App.setLocalSchoolCard(schoolCard);
                    setSchoolCardView(schoolCard);
                    if (App.getLocalSchoolCard() != null) {
                        tvSaveTime.setText((String) App.getLocalSchoolCard().get("savetime"));
                    } else {
                        tvSaveTime.setText("刚刚");
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示");
                    builder.setMessage("没有获取到数据！");
                    builder.setPositiveButton("知道了", null);
                    builder.create().show();
                }
                loadView.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(schoolCard);
        }
    }

    private void setSchoolCardView(SchoolCard schoolCard) {
        tvFullName.setText(schoolCard.getFullName());
        tvSex.setText(schoolCard.getSex());
        tvNation.setText(schoolCard.getNation());
        tvDateOfBirth.setText(schoolCard.getDateOfBirth());
        tvHometown.setText(schoolCard.getHometown());
        tvFamily.setText(schoolCard.getFamily());
        tvPoliticalLandscape.setText(schoolCard.getPoliticalLandscape());
        tvPhone.setText(schoolCard.getPhone());
        tvDepartment.setText(schoolCard.getDepartment());
        tvMajor.setText(schoolCard.getMajor());
        tvClass.setText(schoolCard.getClas());
        tvLengthOfSchooling.setText(schoolCard.getLengthOfSchooling());
        tvAdmissionDate.setText(schoolCard.getAdmissionDate());
        tvGraduationDate.setText(schoolCard.getGraduationDate());
        tvProfessionalDirection.setText(schoolCard.getProfessionalDirection());
        tvStudentID.setText(schoolCard.getStudentID());
        tvLearningForm.setText(schoolCard.getLearningForm());
        tvLearningLevel.setText(schoolCard.getLearningLevel());
        tvJoinLeagueTimeAndPlace.setText(schoolCard.getJoinLeagueTimeAndPlace());
        tvPreSchoolEducation.setText(schoolCard.getPreSchoolEducation());
        tvForeignLanguageTypes.setText(schoolCard.getForeignLanguageTypes());
        tvPreWorkUnit.setText(schoolCard.getPreWorkUnit());
        tvPosition.setText(schoolCard.getPosition());
        tvAddress.setText(schoolCard.getAddress());
        tvStationGetOff.setText(schoolCard.getStationGetOff());
        tvPostcode.setText(schoolCard.getPostcode());
        tvHomePhone.setText(schoolCard.getHomePhone());
        tvContacts.setText(schoolCard.getContacts());
        tvEntranceExam.setText(schoolCard.getEntranceExam());
        tvIdCardNum.setText(schoolCard.getIdCardNum());
        tvStudentIDCardNum.setText(schoolCard.getStudentIDCardNum());
        tvGraduationCertificateNum.setText(schoolCard.getGraduationCertificateNum());
        tvGraduationCardNum.setText(schoolCard.getGraduationCardNum());
    }

    class GetStudentImage extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            File file = null;
            try {
                file = new File(getContext().getFilesDir().getPath() + "/" + App.Account + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (file.exists()) {
                return true;
            } else {
                try {
                    return MyHttpUtil.getStudentImage(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (result) {
                    ivHead.setImageDrawable(Drawable.createFromPath(getContext().getFilesDir().getPath() + "/" + App.Account + ".jpg"));
                } else {
                    ivHead.setImageDrawable(getResources().getDrawable(R.drawable.ic_main));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
