package com.ybx.guider.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ybx.guider.R;
import com.ybx.guider.activity.ChangePasswordActivity;
import com.ybx.guider.activity.ChangeUserInfoActivity;
import com.ybx.guider.activity.DeptManageActivity;
import com.ybx.guider.activity.LoginActivity;
import com.ybx.guider.activity.RelatedProviderActivity;
import com.ybx.guider.adapters.TeamLogListAdapter;
import com.ybx.guider.parameters.Param;
import com.ybx.guider.parameters.ParamUtils;
import com.ybx.guider.requests.XMLRequest;
import com.ybx.guider.responses.GuiderInfoResponse;
import com.ybx.guider.responses.ResponseUtils;
import com.ybx.guider.utils.EncryptUtils;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.URLUtils;
import com.ybx.guider.utils.Utils;
import com.ybx.guider.utils.VolleyRequestQueue;

import org.w3c.dom.Text;

/**
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, Response.Listener<GuiderInfoResponse>, Response.ErrorListener {
    private TeamLogListAdapter mAdapter;
    XMLRequest<GuiderInfoResponse> mQueryGuiderInfo;
    TextView mGuiderName;
    TextView mGuiderNumber;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamLogListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getView().findViewById(R.id.add_department).setOnClickListener(this);
        this.getView().findViewById(R.id.apply_provider).setOnClickListener(this);
        this.getView().findViewById(R.id.change_user_info).setOnClickListener(this);
        this.getView().findViewById(R.id.change_password).setOnClickListener(this);
        this.getView().findViewById(R.id.logout).setOnClickListener(this);

        mGuiderName = (TextView)this.getView().findViewById(R.id.guiderName);
        mGuiderNumber = (TextView)this.getView().findViewById(R.id.guiderNumber);
        TextView version = (TextView)this.getView().findViewById(R.id.versionName);
        version.setText(Utils.getVersionName(this.getContext()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        reqeustQuery();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.main_refresh);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.add_department:
                intent = new Intent(this.getContext(), DeptManageActivity.class);
                startActivity(intent);
                break;
            case R.id.apply_provider:
                intent = new Intent(this.getContext(), RelatedProviderActivity.class);
                startActivity(intent);
                break;
            case R.id.change_user_info:
                intent = new Intent(this.getContext(), ChangeUserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.change_password:
                intent = new Intent(this.getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                intent = new Intent(this.getContext(), LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_START_TYPE, LoginActivity.START_TYPE_LOGOUT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.getActivity().finish();
                break;
            default:
                break;
        }
    }

    void reqeustQuery(){
        Param param = new Param(ParamUtils.PAGE_GUIDER_QUERY);
        param.setUser(PreferencesUtils.getGuiderNumber(this.getContext()));

        String orderParams = param.getParamStringInOrder();
        String sign = EncryptUtils.generateSign(orderParams, PreferencesUtils.getPassword(this.getContext()));
        param.setSign(sign);

        String url = URLUtils.generateURL(param);
        mQueryGuiderInfo = new XMLRequest<GuiderInfoResponse>(url, this, this, new GuiderInfoResponse());
        mQueryGuiderInfo.setShouldCache(false);
        VolleyRequestQueue.getInstance(this.getContext()).add(mQueryGuiderInfo);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (URLUtils.isDebug) {
            Log.d(URLUtils.TAG_DEBUG, "Volly error: " + error.toString());
        }
    }

    @Override
    public void onResponse(GuiderInfoResponse response) {
        if (response.mReturnCode.equals(ResponseUtils.RESULT_OK)) {
            mGuiderName.setText(response.Name);
            mGuiderNumber.setText(PreferencesUtils.getGuiderNumber(this.getContext()));
        } else {
            if (URLUtils.isDebug) {
                Log.d(URLUtils.TAG_DEBUG, "retcode: " + response.mReturnCode);
                Log.d(URLUtils.TAG_DEBUG, "retmsg: " + response.mReturnMSG);
            }
        }
    }

}
