package com.ybx.guider.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.GetVerifyCodeResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

public class PhoneVerifyFragment extends Fragment {
    private EditText mETPhoneNumber;
    private EditText mVerifyCode;
    private OnFragmentInteractionListener mListener;
    private String mPhoneNumber;
    XMLRequest<GetVerifyCodeResponse> mRequest;

    public PhoneVerifyFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mETPhoneNumber = (EditText) this.getActivity().findViewById(R.id.phoneNumber);
        mVerifyCode = (EditText) this.getActivity().findViewById(R.id.verifyCode);
        mVerifyCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mListener != null) {
                    mListener.onPhoneVerified(mPhoneNumber, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.getActivity().findViewById(R.id.btnGetVerifyCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mETPhoneNumber.getText() == null || mETPhoneNumber.getText().length() == 0) {
                    Toast.makeText(PhoneVerifyFragment.this.getContext(), "手机号不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    mPhoneNumber = mETPhoneNumber.getText().toString();
                    GetVerifyCode();
                }
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhoneVerifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneVerifyFragment newInstance() {
        PhoneVerifyFragment fragment = new PhoneVerifyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_verify, container, false);
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
        void onPhoneVerified(String phoneNumber, String verifyCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRequest!=null){
            mRequest.cancel();
        }
    }

    public void GetVerifyCode() {
        Param param = new Param(ParamUtils.PAGE_GET_CHECK_CODE);
        param.removeParam(ParamUtils.KEY_SIGN_TYPE);

        param.setPhoneNumber(mPhoneNumber);
//        param.setGuiderNumber(PreferencesUtils.getGuiderNumber(this.getContext()));

        Response.Listener<GetVerifyCodeResponse> listener = new Response.Listener<GetVerifyCodeResponse>() {
            @Override
            public void onResponse(GetVerifyCodeResponse response) {
                if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
                    Toast.makeText(PhoneVerifyFragment.this.getContext(), "验证码已发送至您的手机!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PhoneVerifyFragment.this.getContext(), response.mReturnMSG, Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PhoneVerifyFragment.this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<GetVerifyCodeResponse>(url, listener, errorListener, new GetVerifyCodeResponse());
        mRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(PhoneVerifyFragment.this.getContext()).add(mRequest);
    }
}
