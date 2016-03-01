package com.ybx.guider.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.responses.XMLResponse;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

public class SignUpInfoFragment extends Fragment implements Response.Listener<XMLResponse>, Response.ErrorListener {
    private static final String ARG_VERIFY_CODE = "verify_code";
    private static final String ARG_PHONE_NUMBER = "phone_number";

    private OnFragmentInteractionListener mListener;
    private Button mBtnSubmit;
    private EditText mUserName;
    private EditText mUserId;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mFirstLanguage;
    private EditText mSecondLanguage;
    private String mVerifyCode;
    private String mPhoneNumber;
    private ProgressDialog mProgressDialog;

    public SignUpInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpInfoFragment.
     */
    public static SignUpInfoFragment newInstance(String phoneNumber, String verifyCode) {
        SignUpInfoFragment fragment = new SignUpInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_VERIFY_CODE, verifyCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVerifyCode = getArguments().getString(ARG_VERIFY_CODE);
            mPhoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserName = (EditText) this.getView().findViewById(R.id.userName);
        mUserId = (EditText) this.getView().findViewById(R.id.userId);
        mPassword = (EditText) this.getView().findViewById(R.id.password);
        mConfirmPassword = (EditText) this.getView().findViewById(R.id.confirmPassword);
        mFirstLanguage = (EditText) this.getView().findViewById(R.id.firstLanguage);
        mSecondLanguage = (EditText) this.getView().findViewById(R.id.secondLanguage);

        mBtnSubmit = (Button) this.getView().findViewById(R.id.submit);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCheck()) {
                    requestSignUp();
                }
            }
        });
    }

    public boolean inputCheck() {
        if (mUserName == null || mUserName.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "姓名不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mUserId == null || mUserId.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "导游证号不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPassword == null || mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "密码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPassword.getText().toString().length() < Utils.MIN_LENGTH_OF_PASSWORD) {
            Toast.makeText(this.getContext(), "密码长度不能小于6！", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utils.checkPassword(mPassword.getText().toString())) {
            Toast.makeText(this.getContext(), "密码必须同时包含字母和数字！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mConfirmPassword == null || mConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "确认密码不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
            Toast.makeText(this.getContext(), "确认密码必须和密码相同！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mFirstLanguage == null || mFirstLanguage.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "主要语种不能为空！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
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
    public void onResume() {
        super.onResume();
//        TextView tv =(TextView)this.getActivity().findViewById(R.id.licenseLink);
//        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSubmit(Uri uri);
    }

    public void requestSignUp() {
        mProgressDialog = ProgressDialog.show(this.getContext(), "正在登录", "请稍等...", true, false);

        Param param = new Param(ParamUtils.PAGE_GUIDER_REGISTER);
        param.removeParam(ParamUtils.KEY_SIGN_TYPE);

        param.setVerifyCode(mVerifyCode);
        param.setGuiderNumber(PreferencesUtils.getGuiderNumber(this.getContext()));
        param.setGuiderName(mUserName.getText().toString());
        param.setGuiderIdentity(mUserId.getText().toString());
        param.setPhoneNumber(mPhoneNumber);
        param.setFistLanguage(mFirstLanguage.getText().toString());
        param.setSecondLanguage(mSecondLanguage.getText().toString());
        String hash = EncryptUtils.md5(mPassword.getText().toString());
        param.setPassword(hash);
        param.setPhoto(PreferencesUtils.getGuiderNumber(this.getContext()) + ".jpg");

//        String orderParams = param.getParamStringInOrder();
//        String sign = EncryptUtils.generateSign(orderParams, password);
//        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        XMLRequest<XMLResponse> request = new XMLRequest<XMLResponse>(url, this, this, new XMLResponse());

        VolleyRequestQueue.getInstance(this.getContext()).add(request);
    }

    @Override
    public void onResponse(XMLResponse response) {
        mProgressDialog.dismiss();
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            Toast.makeText(this.getContext(), "注册成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this.getContext(), "注册失败！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        Toast.makeText(this.getContext(), "注册失败！", Toast.LENGTH_LONG).show();
    }

}
