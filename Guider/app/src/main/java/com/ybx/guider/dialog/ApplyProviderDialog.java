package com.ybx.guider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ybx.guider.responses.ProviderItem;

/**
 * Created by chenl on 2016/3/15.
 */
public class ApplyProviderDialog extends DialogFragment {
    public static String ARG_ITEM = "item";
    ProviderItem mItem;
    ApplyProviderDialogListener mListener;

    public interface ApplyProviderDialogListener {
        public void onApply(ProviderItem item);
    }

    public static ApplyProviderDialog newInstance(ProviderItem item) {
        ApplyProviderDialog f = new ApplyProviderDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (ProviderItem) getArguments().getSerializable(ARG_ITEM);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ApplyProviderDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ApplyProviderDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("申请开通供应商预约?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onApply(mItem);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
