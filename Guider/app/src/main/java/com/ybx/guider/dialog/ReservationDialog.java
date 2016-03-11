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
import com.ybx.guider.responses.TeamScheduleItem;

/**
 * Created by chenl on 2016/2/18.
 */
public class ReservationDialog extends DialogFragment {

    private TeamScheduleItem mItem;
    private static String ARG_TEAM_SCHEDULE_ITEM = "item";

    public static ReservationDialog newInstance(TeamScheduleItem item) {
        ReservationDialog f = new ReservationDialog();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_SCHEDULE_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = (TeamScheduleItem)getArguments().getSerializable(ARG_TEAM_SCHEDULE_ITEM);
        }

    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ReservationListener {
        public void onReservationClickOk(DialogFragment dialog);

        public void onReservationClickCancel(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ReservationListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ReservationListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement FinishTeamDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reservation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onReservationClickOk(ReservationDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onReservationClickCancel(ReservationDialog.this);
                    }
                });
        return builder.create();
    }
}
