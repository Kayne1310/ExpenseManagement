package com.example.expensemng.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.expensemng.Activity.ChangePassword;
import com.example.expensemng.Activity.LoginActivity;
import com.example.expensemng.Activity.MainActivity;
import com.example.expensemng.Activity.ViewProfile;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.OnUserCallBack;
import com.example.expensemng.Models.User;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout changePassword;
    private CardView viewProfile, cardLogout;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView tvName, tvEmail;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        changePassword = view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

        viewProfile = view.findViewById(R.id.cardProfile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(getActivity(), ViewProfile.class);
                startActivity(itent);


            }
        });
        cardLogout = view.findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //method get Infor user
        IgetInfor igetInfor = new firebaseService(db);
        String userId = user.getUid();
        igetInfor.getInforUser(userId, new OnUserCallBack() {
            @Override
            public void onSuccess(User user) {
                tvName = view.findViewById(R.id.tvName);
                tvName.setText(user.getUserName());
                Log.d("User Info", "Name: " + user.getUserName() + ", Email: " + user.getEmail());

                tvEmail = view.findViewById(R.id.tvEmail);
                tvEmail.setText(user.getEmail());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Error","Faild",e);
            }
        });


        return view;

    }

}