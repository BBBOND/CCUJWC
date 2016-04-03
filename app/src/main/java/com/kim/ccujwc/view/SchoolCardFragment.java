package com.kim.ccujwc.view;

import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kim.ccujwc.R;
import com.kim.ccujwc.common.App;
import com.kim.ccujwc.common.MyHttpUtil;
import com.kim.ccujwc.model.SchoolCard;
import com.kim.ccujwc.view.utils.LoadingView;
import com.kim.ccujwc.view.utils.ShapeLoadingView;

import org.apache.commons.httpclient.HttpClient;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SchoolCardFragment extends BaseFragment {


    private static final String TAG = "SchoolCardFragment";

    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_fullName)
    AppCompatTextView tvFullName;
    @Bind(R.id.tv_sex)
    AppCompatTextView tvSex;
    @Bind(R.id.tv_nation)
    AppCompatTextView tvNation;
    @Bind(R.id.tv_dateOfBirth)
    AppCompatTextView tvDateOfBirth;
    @Bind(R.id.tv_hometown)
    AppCompatTextView tvHometown;
    @Bind(R.id.tv_family)
    AppCompatTextView tvFamily;
    @Bind(R.id.tv_politicalLandscape)
    AppCompatTextView tvPoliticalLandscape;
    @Bind(R.id.tv_phone)
    AppCompatTextView tvPhone;
    @Bind(R.id.tv_department)
    AppCompatTextView tvDepartment;
    @Bind(R.id.tv_major)
    AppCompatTextView tvMajor;
    @Bind(R.id.tv_class)
    AppCompatTextView tvClass;
    @Bind(R.id.tv_lengthOfSchooling)
    AppCompatTextView tvLengthOfSchooling;
    @Bind(R.id.tv_admissionDate)
    AppCompatTextView tvAdmissionDate;
    @Bind(R.id.tv_graduationDate)
    AppCompatTextView tvGraduationDate;
    @Bind(R.id.tv_professionalDirection)
    AppCompatTextView tvProfessionalDirection;
    @Bind(R.id.tv_studentID)
    AppCompatTextView tvStudentID;
    @Bind(R.id.tv_learningForm)
    AppCompatTextView tvLearningForm;
    @Bind(R.id.tv_learningLevel)
    AppCompatTextView tvLearningLevel;
    @Bind(R.id.tv_joinLeagueTimeAndPlace)
    AppCompatTextView tvJoinLeagueTimeAndPlace;
    @Bind(R.id.tv_preSchoolEducation)
    AppCompatTextView tvPreSchoolEducation;
    @Bind(R.id.tv_foreignLanguageTypes)
    AppCompatTextView tvForeignLanguageTypes;
    @Bind(R.id.tv_preWorkUnit)
    AppCompatTextView tvPreWorkUnit;
    @Bind(R.id.tv_position)
    AppCompatTextView tvPosition;
    @Bind(R.id.tv_address)
    AppCompatTextView tvAddress;
    @Bind(R.id.tv_stationGetOff)
    AppCompatTextView tvStationGetOff;
    @Bind(R.id.tv_postcode)
    AppCompatTextView tvPostcode;
    @Bind(R.id.tv_homePhone)
    AppCompatTextView tvHomePhone;
    @Bind(R.id.tv_contacts)
    AppCompatTextView tvContacts;
    @Bind(R.id.tv_entranceExam)
    AppCompatTextView tvEntranceExam;
    @Bind(R.id.tv_idCardNum)
    AppCompatTextView tvIdCardNum;
    @Bind(R.id.tv_studentIDCardNum)
    AppCompatTextView tvStudentIDCardNum;
    @Bind(R.id.tv_graduationCertificateNum)
    AppCompatTextView tvGraduationCertificateNum;
    @Bind(R.id.tv_graduationCardNum)
    AppCompatTextView tvGraduationCardNum;
    @Bind(R.id.loadView)
    LoadingView loadView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_school_card, null);
        ButterKnife.bind(this, view);
        new GetSchoolCard().execute();
        new GetStudentImage().execute();
        return view;
    }

    class GetSchoolCard extends AsyncTask<Void, Void, SchoolCard> {

        @Override
        protected SchoolCard doInBackground(Void... params) {
            HttpClient client = null;
            try {
                client = new HttpClient();
                return MyHttpUtil.getSchoolCard(client);
            } catch (Exception e) {
                Message message = GetSchoolCardErrorHandler.obtainMessage();
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
        protected void onPostExecute(SchoolCard schoolCard) {
            try {
                if (schoolCard != null) {
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
                    HttpClient client = new HttpClient();
                    return MyHttpUtil.getStudentImage(client, getContext());
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
        ButterKnife.unbind(this);
    }
}
