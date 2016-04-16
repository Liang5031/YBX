package com.ybx.guider.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.activity.LoginActivity;
import com.ybx.guider.activity.MainActivity;
import com.ybx.guider.activity.SignUpActivity;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.LoginResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.VolleyRequestQueue;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountVerifyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountVerifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountVerifyFragment extends Fragment implements Response.Listener<LoginResponse>, Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    XMLRequest<LoginResponse> mRequest;
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    public AccountVerifyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountVerifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountVerifyFragment newInstance() {
        AccountVerifyFragment fragment = new AccountVerifyFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_verify, container, false);
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.verify_activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.verify_fragment_refresh:
                reqeustLogin(PreferencesUtils.getGuiderNumber(this.getContext()), PreferencesUtils.getPassword(this.getContext()));
                return true;
            case R.id.verify_fragment_sign_up:
                Intent intent = new Intent(this.getContext(), SignUpActivity.class);
                startActivity(intent);
                this.getActivity().finish();
                return true;
            case R.id.verify_fragment_change_account:
                intent = new Intent(this.getContext(), LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_CHANGE_ACCOUNT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRequest!=null){
            mRequest.cancel();
        }
    }

    private void reqeustLogin(String guiderNumber, String password) {
        if (guiderNumber == null || guiderNumber.isEmpty()) {
            return;
        }
        if (password == null || password.isEmpty()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this.getContext(), "正在刷新", "请稍等...", true, false);

        Param param = new Param(ParamUtils.PAGE_GUIDER_LOGIN);
        param.setUser(guiderNumber);

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, password);
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mRequest = new XMLRequest<LoginResponse>(url, this, this, new LoginResponse());
        mRequest.setShouldCache(false);

        VolleyRequestQueue.getInstance(this.getActivity()).add(mRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        Toast.makeText(this.getContext(), "刷新失败！", Toast.LENGTH_LONG).show();

        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(LoginResponse response) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK) && response.mAccountStatus != null) {
            /* clear cache after login */
            VolleyRequestQueue.getInstance(this.getContext()).clear();

            if (response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_INACTIVE)) {
                Toast.makeText(this.getContext(), "账号已禁用！", Toast.LENGTH_LONG).show();
            } else if (response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_CHECKING)) {
                Toast.makeText(this.getContext(), "账号审核中，请耐心等待！", Toast.LENGTH_LONG).show();
            } else if (response.mAccountStatus.equalsIgnoreCase(ResponseUtils.ACCOUNT_STATUS_ACTIVE)) {
                Intent intent = new Intent(this.getContext(), MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_ACCOUNT_STATUS, response.mAccountStatus);
                startActivity(intent);
                this.getActivity().finish();
            } else {
                Toast.makeText(this.getContext(), "注册审核未通过！", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.getContext(), response.mReturnMSG, Toast.LENGTH_LONG).show();
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }
}
