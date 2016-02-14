package com.ybx.guider.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ybx.guider.R;

import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadPhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PHOTO = 3;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    private Uri mImageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
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

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    private Intent getPickPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch(requestCode){
                case TAKE_PHOTO:
                    cropImageUri(mImageUri, 300, 300, CROP_PHOTO);
                    break;
                case PICK_PHOTO:
                    if(mImageView != null) {
                        Bitmap bmp = data.getParcelableExtra("data");
                        mImageView.setImageBitmap(bmp);
                    }
                    break;
                case CROP_PHOTO:
                    if(mImageView != null){
                        Bitmap bmp = decodeUriAsBitmap(mImageUri);
                        mImageView.setImageBitmap(bmp);
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView = (ImageView)this.getActivity().findViewById(R.id.photo);
        Button btn = (Button) this.getActivity().findViewById(R.id.pickPhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickPhotoIntent(), PICK_PHOTO);
            }
        });

        btn = (Button) this.getActivity().findViewById(R.id.takePhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
