package com.ybx.guider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ybx.guider.R;
import com.ybx.guider.responses.TeamItem;

/**
 * Created by chenl on 2016/2/18.
 */
public class ScheduleDetailDialog extends DialogFragment {
    private TeamItem mItem;
    private static String ARG_TEAM_ITEM = "item";

    public static ScheduleDetailDialog newInstance(TeamItem item) {
        ScheduleDetailDialog f = new ScheduleDetailDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (TeamItem)getArguments().getSerializable(ARG_TEAM_ITEM);
        }

    }

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ScheduleDetailListener {


    }

    // Use this instance of the interface to deliver action events
    ScheduleDetailListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (ScheduleDetailListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement FinishTeamDialogListener");
//        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_schedule_detail, null);
        builder.setView(view);

        return builder.create();
    }

}
