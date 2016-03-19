package com.ybx.guider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.ybx.guider.R;
import com.ybx.guider.responses.BindingDeptItem;

/**
 * Created by chenl on 2016/3/15.
 */
public class AddDeptDialog extends DialogFragment {
    public static String ARG_DEPT_ITEM = "item";
    BindingDeptItem mItem;
    DeptItemDialogListener mListener;
    CheckBox mCBAppo;
    CheckBox mCBAssign;

    public interface DeptItemDialogListener {
        public void onDeptAdd(BindingDeptItem item);

        public void onDeptCancel(BindingDeptItem item);
    }

    public static AddDeptDialog newInstance(BindingDeptItem item) {
        AddDeptDialog f = new AddDeptDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_DEPT_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (BindingDeptItem) getArguments().getSerializable(ARG_DEPT_ITEM);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DeptItemDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DeptItemDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_dept_item, null);
        mCBAppo = (CheckBox) view.findViewById(R.id.cbAppo);
        mCBAssign = (CheckBox) view.findViewById(R.id.cbAssign);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage(mItem.customername)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mItem.setAllowAppointment(mCBAppo.isChecked());
                        mItem.setAllowAssignTeam(mCBAssign.isChecked());
                        mListener.onDeptAdd(mItem);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDeptCancel(mItem);
                    }
                });
        return builder.create();
    }
}
