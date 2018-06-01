package net.elephenapp.mcoffee;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;

public class DetailUserFragment extends Fragment {

    private String midString, nameString,sernameString,emailString,balanceString;
    private ImageView avataImageView;
    private EditText nameEdiText, surnameEditText, emailEditText;
    private TextView balanceTextView;
    private Button editButton;
    private Uri uri;
    private boolean photoABoolean = false;


    public static DetailUserFragment detailUserInstance(String midString) {

        DetailUserFragment detailUserFragment = new DetailUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mid", midString);
        detailUserFragment.setArguments(bundle);

        return detailUserFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        midString = getArguments().getString("mid");
        Log.d("1JuneV1", "midString Recive ==> " + midString);


//        Initial View
        initialView();


//        Show View
        showView();

//        change avata
        changeAvata();

//        Edit Controler

        editControler();


    }//Main Method

    private void editControler() {
        editButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

//                     Photo Manage
                     if(photoABoolean){
                         uploadPhotoToServer();
                     }

                  }
        });
    }

    private void uploadPhotoToServer() {
//        Find Path Photo
        String pathPhotoString = null;
        String[] strings = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver()
                .query(uri, strings, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            int indexInt = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            pathPhotoString = cursor.getString(indexInt);

        }else{
            pathPhotoString = uri.getPath();
        }
        Log.d("1JuneV1","PhatPhoto ==> " + pathPhotoString);

        try{

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                    .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com",21,"dru@swiftcodingthai.com" ,"Abc12345");
            simpleFTP.bin();
            simpleFTP.cwd("member");
            simpleFTP.stor(new FileInputStream(new File(pathPhotoString)),midString + ".jpg");
            simpleFTP.disconnect();

        }catch (Exception e){
            Log.d("1JuneV1","e ftp ==> " + e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK){
            photoABoolean = true;
            uri = data.getData();
            try{

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                avataImageView.setImageBitmap(bitmap);


            }catch (Exception e){
                e.printStackTrace();
            }


        }else {
            Toast.makeText(getActivity(),"Plase Choose Image One mere...",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void changeAvata() {
        avataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Please Choose App"),1);

            }
        });
    }

    private void showView() {
        try {
            MyConstant myConstant = new MyConstant();
            GetUserWhareMid getUserWhareMid = new GetUserWhareMid(getActivity());
            getUserWhareMid.execute(midString, myConstant.getUrlGetUserWhereMid());
            String jsonString = getUserWhareMid.get();
            Log.d("1JuneV1",  "JSON ==> " + jsonString );
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            nameString = jsonObject.getString("name");
            sernameString = jsonObject.getString("sername");
            emailString = jsonObject.getString("email");
            balanceString = jsonObject.getString("balance");

            nameEdiText.setText(nameString);
            surnameEditText.setText(sernameString);
            emailEditText.setText(emailString);
            balanceTextView.setText(balanceString);


        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private void initialView() {
        avataImageView = getView().findViewById(R.id.imvAvata);
        nameEdiText = getView().findViewById(R.id.edtName);
        surnameEditText = getView().findViewById(R.id.edtSName);
        emailEditText = getView().findViewById(R.id.edtEmail);
        balanceTextView = getView().findViewById(R.id.txtBalance);
        editButton = getView().findViewById(R.id.bynEditValue);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_user, container, false);
        return view;
    }
}
