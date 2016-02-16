package com.ybx.guider.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ybx.guider.FileImageUpload;
import com.ybx.guider.R;
import com.ybx.guider.Utils;

import java.io.File;

public class UploadPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PHOTO = 3;
    private static final int CROP_OUTPUT_X = 300;
    private static final int CROP_OUTPUT_Y = 300;
    private static final String IMAGE_FILE_LOCATION = "file:////sdcard/test.jpg";//temp file
    //    private Uri mImageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private Uri mImageUri;
    private ImageView mImageView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UploadPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadPhotoFragment newInstance(String param1, String param2) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_photo, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPhotoUploaded(uri);
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
        void onPhotoUploaded(Uri uri);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView = (ImageView) this.getActivity().findViewById(R.id.photo);
        this.getActivity().findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                new Thread() {
                    public void run() {
//                        File file = new File(IMAGE_FILE_LOCATION);
//                        File file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");


//                        File file = new File(getRealPathFromURI(mImageUri));
//                        if( file.exists() ) {
                        FileImageUpload.uploadFile(UploadPhotoFragment.this.getActivity().getApplicationContext(), mImageUri, "http://10.0.2.2:8080/", "1234.jpg");
//                        }
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

    private String getFilePath(Uri uri) {
        String path = "";
        ContentResolver cr = this.getActivity().getContentResolver();
        Cursor c = cr.query(uri, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            path = c.getString(c.getColumnIndex("_data"));
        }

        return path;
    }

    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        CursorLoader loader = new CursorLoader(this.getContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);

        String path="";
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path =cursor.getString(column_index);
            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealPathFromURI1(Uri contentUri) {
        String filePath = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndex("_data");
        if (cursor.moveToFirst()) {
            ;
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            filePath = cursor.getString(column_index);
            filePath = cursor.getString(column_index);
        }
        cursor.close();
        return filePath;


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
