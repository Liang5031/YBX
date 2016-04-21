package com.ybx.guider.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.ybx.guider.R;
import com.ybx.guider.utils.FileImageUpload;
import com.ybx.guider.utils.PreferencesUtils;
import com.ybx.guider.utils.UserPicture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    public static final String IMAGE_TYPE = "image/*";
    private Uri mTakePhotoImageUri;
    private Uri mPickPhotoImageUri;
    private Uri mUploadImageUri;
    private ImageView mImageView;
    private Button mBtnUpload;
    private ProgressDialog mProgressDialog;
    private EditText mETGuiderNumber;
    private String mGuiderNumber;
    private OnFragmentInteractionListener mListener;
    private int mActionType;

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
                    mActionType = TAKE_PHOTO;
                    if (mImageView != null && mTakePhotoImageUri !=null) {
                        try {
                            mImageView.setImageBitmap(new UserPicture(mTakePhotoImageUri, getActivity().getContentResolver()).getBitmap());
                        } catch (IOException e) {
                        }
                    }
                    break;

                case PICK_PHOTO:
                    mActionType = PICK_PHOTO;
                    if (mImageView != null) {
                        mPickPhotoImageUri = data.getData();
                        try {
                            mImageView.setImageBitmap(new UserPicture(mPickPhotoImageUri, getActivity().getContentResolver()).getBitmap());
                        } catch (IOException e) {
                        }
                    }
                    break;

                case CROP_PHOTO:
//                    if (mImageView != null) {
//                        mPickPhotoImageUri = data.getData();
//                        mImageView.setImageURI(mPickPhotoImageUri);
//                    }
                    break;
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            String retMsg = (String) msg.obj;
            Toast.makeText(UploadPhotoFragment.this.getContext(), retMsg, Toast.LENGTH_LONG).show();
            PreferencesUtils.setGuiderNumber(UploadPhotoFragment.this.getContext(), mETGuiderNumber.getText().toString());
            boolean ret = false;
            if(retMsg.equals("上传成功")){
                ret = true;
                if (mListener != null) {
                    mListener.onPhotoUploaded(ret);
                }
            }

            super.handleMessage(msg);

        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mETGuiderNumber = (EditText) this.getActivity().findViewById(R.id.guiderNumber);
        if (mGuiderNumber != null && !mGuiderNumber.isEmpty()) {
            mETGuiderNumber.setText(mGuiderNumber);
        }
        mImageView = (ImageView) this.getActivity().findViewById(R.id.photo);
        mBtnUpload = (Button)this.getActivity().findViewById(R.id.upload);
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = mETGuiderNumber.getText().toString();
                if (number.isEmpty()) {
                    Toast.makeText(UploadPhotoFragment.this.getContext(), "导游证号不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mActionType == TAKE_PHOTO) {
                    mUploadImageUri = mTakePhotoImageUri;
                } else if (mActionType == PICK_PHOTO) {
                    mUploadImageUri = mPickPhotoImageUri;
                }

                if (mUploadImageUri == null) {
                    Toast.makeText(UploadPhotoFragment.this.getContext(), "请先选择照片！", Toast.LENGTH_LONG).show();
                    return;
                }

                long size = getFileSize(mUploadImageUri);
                if (size > 2 * 1024 * 1024) {
                    Toast.makeText(UploadPhotoFragment.this.getContext(), "照片大小不能超过2M", Toast.LENGTH_LONG).show();
                    return;
                }

                mProgressDialog = ProgressDialog.show(UploadPhotoFragment.this.getContext(), "上传照片", "请稍等...", true, false);
                new Thread() {
                    @Override
                    public void run() {
                        String ret = FileImageUpload.callWebService(UploadPhotoFragment.this.getActivity().getApplicationContext()
                                , mUploadImageUri, number);

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

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }

        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here, thanks to the answer from @mad indicating this is needed for
        // working code based on images selected using other file managers
        return uri.getPath();
    }

    /**
     * helper to retrieve the size of an image URI
     */
    public long getFileSize(Uri uri) {
        if( uri == null ) {
            return 0;
        }

        long size = 0;
        if(uri.toString().startsWith("content://")) {
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = {MediaStore.Images.Media.SIZE};
            Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                cursor.moveToFirst();
                size = cursor.getLong(column_index);
            }
        } else if(uri.toString().startsWith("file:///")){
            File file = new File(uri.getPath());
            size = file.length();
//            if (file.exists()) {
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(file);
//                    size = fis.available();
////                    fis.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        // this is our fallback here, thanks to the answer from @mad indicating this is needed for
        // working code based on images selected using other file managers
        return size;
    }



    private void getImageFromAlbum() {
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择照片"), PICK_PHOTO);
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTakePhotoImageUri = getmOutputImageUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    Uri getmOutputImageUri(){
        File file = new File(Environment.getExternalStorageDirectory() + "/", "temp.jpg");
        if(file.exists()){
            file.delete();
        }
        return Uri.fromFile(file);
    }

    private void cropImage(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP_OUTPUT_X);
        intent.putExtra("outputY", CROP_OUTPUT_Y);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PHOTO);
    }

    private void cropImageUri(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP_OUTPUT_X);
        intent.putExtra("outputY", CROP_OUTPUT_Y);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PHOTO);
    }
}
