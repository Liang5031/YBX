package com.ybx.guider.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ybx.guider.utils.FileImageUpload;
import com.ybx.guider.R;
import com.ybx.guider.utils.URLUtils;

public class UploadPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GUIDER_NUMBER = "guider_number";
//    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PHOTO = 3;
    private static final int CROP_OUTPUT_X = 300;
    private static final int CROP_OUTPUT_Y = 240;
    private static final String IMAGE_FILE_LOCATION = "file:////sdcard/test.jpg";//temp file
    //    private Uri mImageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private Uri mImageUri;
    private ImageView mImageView;
    private ProgressDialog mProgressDialog;
    private EditText mETGuiderNumber;
    private String mGuiderNumber;
    private OnFragmentInteractionListener mListener;

    public UploadPhotoFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UploadPhotoFragment.
     */
    public static UploadPhotoFragment newInstance(String number) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GUIDER_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGuiderNumber = getArguments().getString(ARG_GUIDER_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_photo, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(boolean isUploaded) {
        if (mListener != null) {
            mListener.onPhotoUploaded(isUploaded);
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
        void onPhotoUploaded(boolean isUploaded);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    cropImage(data.getData(), CROP_OUTPUT_X, CROP_OUTPUT_Y);
                    break;

                case PICK_PHOTO:
                    if (mImageView != null) {
                        mImageUri = data.getData();
                        mImageView.setImageURI(mImageUri);
                    }
                    break;

                case CROP_PHOTO:
                    if (mImageView != null) {
                        mImageUri = data.getData();
                        mImageView.setImageURI(mImageUri);
                    }
                    break;
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            boolean ret = (boolean)msg.obj;
            if(ret){
                Toast.makeText(UploadPhotoFragment.this.getContext(), "照片上传完成！", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(UploadPhotoFragment.this.getContext(), "照片上传失败！", Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);

            if (mListener != null) {
                mListener.onPhotoUploaded(ret);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mETGuiderNumber = (EditText)this.getActivity().findViewById(R.id.guiderNumber);
        if(mGuiderNumber!=null && !mGuiderNumber.isEmpty()) {
            mETGuiderNumber.setText(mGuiderNumber);
        }
        mImageView = (ImageView) this.getActivity().findViewById(R.id.photo);
        this.getActivity().findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = mETGuiderNumber.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(UploadPhotoFragment.this.getContext(), "导游证号不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }

                mProgressDialog = ProgressDialog.show(UploadPhotoFragment.this.getContext(), "上传照片", "请稍等...", true, false);
                new Thread() {
                    @Override
                    public void run() {
                        boolean ret = FileImageUpload.callWebService(UploadPhotoFragment.this.getActivity().getApplicationContext()
                                , mImageUri, number);

                        Message message = new Message();
                        message.obj = ret;
                        mHandler.sendMessage(message);
                    }
                }.start();
            }
        });

        Button btn = (Button) this.getActivity().findViewById(R.id.pickPhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        btn = (Button) this.getActivity().findViewById(R.id.takePhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String path = "";
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void getImageFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP_OUTPUT_X);
        intent.putExtra("outputY", CROP_OUTPUT_Y);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_PHOTO);
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void cropImage(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        startActivityForResult(intent, CROP_PHOTO);
    }
}
