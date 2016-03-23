package com.ybx.guider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ybx.guider.responses.RelatedProviderItem;

/**
 * Created by chenl on 2016/3/15.
 */
public class ProviderSyncDialog extends DialogFragment {
    public static String ARG_ITEM = "item";
    RelatedProviderItem mItem;
    ProviderSyncDialogListener mListener;

    public interface ProviderSyncDialogListener {
        public void onSync(RelatedProviderItem item);
    }

    public static ProviderSyncDialog newInstance(RelatedProviderItem item) {
        ProviderSyncDialog f = new ProviderSyncDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (RelatedProviderItem) getArguments().getSerializable(ARG_ITEM);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProviderSyncDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProviderSyncDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("同步供应商信息?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSync(mItem);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
