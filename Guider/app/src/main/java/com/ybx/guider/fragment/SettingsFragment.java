package com.ybx.guider.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ybx.guider.R;
import com.ybx.guider.activity.ChangePasswordActivity;
import com.ybx.guider.activity.LoginActivity;
import com.ybx.guider.adapters.TeamLogListAdapter;

/**
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private TeamLogListAdapter mAdapter;

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

//        LayoutInflater inflater = LayoutInflater.from(this.getContext());
//        this.getListView().addHeaderView(inflater.inflate(R.layout.real_name_list_header, null));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

//    public void onClickAddDepartment(View view){
//
//    }
//
//    public void onClickApplyProvider(View view){
//
//    }
//
//    public void onClickChangeUserInfo(View view){
//
//    }
//
//    public void onClickChangePassword(View view){
//
//    }
//
//    public void onClickLogout(View view){
//
//    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.add_department:
                break;
            case R.id.apply_provider:
                break;
            case R.id.change_user_info:
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
}
