package net.elephenapp.mcoffee;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Bowon on 10/7/2560.
 */

public class RegisterFragment extends Fragment{

    public static RegisterFragment registerInstant() {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Back Controller
        backController();

        //Save Controller
        saveController();

    }

    private void saveController() {

        //Initial View
        ImageView imageView = (ImageView) getView().findViewById(R.id.imvsave);
        final EditText nameEditText = (EditText) getView().findViewById(R.id.edtName);
        final EditText namesEditText = (EditText) getView().findViewById(R.id.edtSName);
        final EditText userEditText = (EditText) getView().findViewById(R.id.edtuser);
        final EditText passwordEditText = (EditText) getView().findViewById(R.id.edtpassword);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get value Form Edit text

                String strName = nameEditText.getText().toString().trim();
                String strSName = namesEditText.getText().toString().trim();
                String strUser = userEditText.getText().toString().trim();
                String strPassword = passwordEditText.getText().toString().trim();


                //Check space
                if (strName.equals("") || strSName.equals("") ||strUser.equals("") || strPassword.equals("")) {
                    //Have Space

                    MyAleart myAlert = new MyAleart(getActivity());
                    myAlert.myDialog(getString(R.string.nave_space),
                            getString(R.string.message_space));


                } else {
                    //No Space
                    regisMenber(strName, strSName, strUser, strPassword);
                }

            }
        });


    }//End Save Controller

    private void regisMenber(final String strName, String strSName, String strUser, String strPassword){
        String strURL = "http://infobizplus.com/R2P/php/appMemberRegis.php";
        OkHttpClient OkHttpClient = new OkHttpClient();
        RequestBody RequestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("name", strName)
                .add("sername",strSName)
                .add("user", strUser)
                .add("password",strPassword)
                .build();
        Request.Builder builder = new  Request.Builder();
        Request request = builder.url(strURL).post(RequestBody).build();
        Call call = OkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String resualtString = response.body().string();
                    Log.d("M COFFEE", "Response ==>"+ resualtString);

                    getActivity().getSupportFragmentManager().popBackStack();

                    /*

                    if(Boolean.parseBoolean(resualtString)){

                    }else{
                        MyAleart myAleart  = new MyAleart(getActivity());
                        myAleart.myDialog("Can not Upload",
                                "Plases Try again Network False");
                    }
                    */
                    //getActivity().finish();
                } catch (Exception e){
                    Log.d("M COFFEE","error " + e.toString());
                }
            }
        });


    } //End regisMember

    private void backController() {
        ImageView imageView = (ImageView) getView().findViewById(R.id.imvback);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.maincontent, MainFragment.mainInstant())
                        .commit();
            }
        });
    }//End Back Controller

}//Main Class
