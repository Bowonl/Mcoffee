package net.elephenapp.mcoffee;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailUserFragment extends Fragment {

    private String midString, nameString,sernameString,emailString,balanceString;
    private ImageView avataImageView;
    private EditText nameEdiText, surnameEditText, emailEditText;
    private TextView balanceTextView;
    private Button editButton;


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


    }//Main Method

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
