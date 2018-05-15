package net.elephenapp.mcoffee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Bowon on 10/7/2560.
 */

public class MainFragment extends Fragment{
    private ProgressDialog progressDialog;

    public static MainFragment mainInstant(){
        MainFragment mainFragment = new MainFragment();
        Bundle bubdle = new Bundle();
        mainFragment.setArguments(bubdle);
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        Check Mid
        checkMid();

        //New Regis Controller
        newRegisterController();


//        Login Controller
        loginController();


    }

    private void checkMid() {
        try {

            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences("MyCoffee", Context.MODE_PRIVATE);
            String midString = sharedPreferences.getString("mid", "");

            if(midString.length() != 0){
                myIntent(midString);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Wait Few Minus");
                progressDialog.setMessage("Please wait Few Minus Process Working ...");
                progressDialog.show();

                EditText userEditText = getView().findViewById(R.id.edtuser);
                EditText passwordEditText = getView().findViewById(R.id.edtpassword);

                String usreString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                MyAleart myAleart = new MyAleart(getActivity());

                if (usreString.isEmpty() || passwordString.isEmpty()) {
                    myAleart.myDialog(getString(R.string.nave_space),
                            getString(R.string.message_space));
                    progressDialog.dismiss();
                } else {

                    try {

                        MyConstant myConstant = new MyConstant();
                     /*   GetAllData getAllData = new GetAllData(getActivity());
                        getAllData.execute(myConstant.getUrlGetAllUser());
                      */


                     GetUserWhereUserAnPass getUserWhereUserAnPass = new GetUserWhereUserAnPass(getActivity());
                     getUserWhereUserAnPass.execute(usreString,passwordString,myConstant.getUrlGetUserWhwerWserAnPass());


                        String jsonString = getUserWhereUserAnPass.get();
                        Log.d("15MayV1", "JSON ==>" + jsonString);

                        if(jsonString.trim().length() ==  4){
                            Log.d("15MayV1", "JSON ==> Null");
                            myAleart.myDialog("User And Password False",
                                    "Please Try Again User or Password False");
                            progressDialog.dismiss();
                        }else{

                            JSONArray jsonArray = new JSONArray(jsonString);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String midString = jsonObject.getString("mid");
                            String statusString = jsonObject.getString("status");
                            Log.d("15MayV1","mid ==>" + midString);
                            Log.d("15MayV1","status ==>" + statusString);

                            if(Integer.parseInt(statusString) == 1){

                                progressDialog.dismiss();

//                                Save User An Pass
                                CheckBox checkBox = getView().findViewById(R.id.checkboxSave);
                                if(checkBox.isChecked()){
                                    saveSharePerference(midString);
                                }

                                myIntent(midString);


                            }else{
                                myAleart.myDialog("Expired Date", "Your Account Expired");
                                progressDialog.dismiss();
                            }

                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }//if



            }
        });
    }

    private void myIntent(String midString) {
        Intent intent = new Intent(getActivity(),ServiceActivity.class);
        intent.putExtra("mid", midString);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveSharePerference(String midString) {

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("MyCoffee", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mid", midString);
        editor.commit();

    }

    private void newRegisterController() {
        TextView textView = (TextView) getView().findViewById(R.id.txtNewRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.maincontent, RegisterFragment.registerInstant())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}   //Main Class
