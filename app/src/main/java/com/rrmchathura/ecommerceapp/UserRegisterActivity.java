package com.rrmchathura.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrmchathura.ecommerceapp.User.UserHomeActivity;
import com.rrmchathura.ecommerceapp.databinding.ActivityUserRegisterBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRegisterActivity extends AppCompatActivity {

    ActivityUserRegisterBinding binding;

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    ArrayList<String> arrayList;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait........");
        progressDialog.setCanceledOnTouchOutside(false);

        arrayList = new ArrayList<>();

        arrayList.add("Ampara");
        arrayList.add("Anuradhapura");
        arrayList.add("Badulla");
        arrayList.add("Batticaloa");
        arrayList.add("Colombo");
        arrayList.add("Galle");
        arrayList.add("Gampaha");
        arrayList.add("Hambantota");
        arrayList.add("Jaffna");
        arrayList.add("Kalutara");
        arrayList.add("Kandy");
        arrayList.add("Kegalle");
        arrayList.add("Kilinochchi");
        arrayList.add("Kurunegala");
        arrayList.add("Mannar");
        arrayList.add("Matale");
        arrayList.add("Matara");
        arrayList.add("Monaragala");
        arrayList.add("Mullaitivu");
        arrayList.add("Nuwara Eliya");
        arrayList.add("Polonnaruwa");
        arrayList.add("Puttalam");
        arrayList.add("Ratnapura");
        arrayList.add("Trincomalee");
        arrayList.add("Vavuniya");


        binding.txtcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadCityDialog();
            }
        });

        binding.btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData();
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterActivity.this, LoginActivity.class));
            }
        });

        binding.seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterActivity.this, SellerRegisterActivity.class));
            }
        });
    }
    private String username = "", email = "", mobile ="", city ="", password = "",con_password ="";
    private void ValidateData() {

        username = binding.txtusername.getText().toString().trim();
        email = binding.txtemail.getText().toString().trim();
        mobile = binding.txtmobile.getText().toString().trim();
        city = binding.txtcity.getText().toString().trim();
        password = binding.txtpassword.getText().toString().trim();
        con_password = binding.txtconpass.getText().toString().trim();


        if (TextUtils.isEmpty(username)){
            binding.txtusername.setError("Username Required");
        }
        else if (TextUtils.isEmpty(email)){
            binding.txtemail.setError("Email Required");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.txtemail.setError("Invalid Email Pattern");
        }
        else if (TextUtils.isEmpty(mobile)){
            binding.txtmobile.setError("Mobile Number Required");
        }
        else if (mobile.length() < 10){
            binding.txtmobile.setError("Invalid Number");
        }
        else if (TextUtils.isEmpty(city)){
            binding.txtcity.setError("City Required");
        }
        else if (TextUtils.isEmpty(password)){
            binding.txtpassword.setError("Password Required");
        }
        else if (TextUtils.isEmpty(con_password)){
            binding.txtconpass.setError("Confirm Password Required");
        }
        else if (!password.equals(con_password)){

            Toast.makeText(UserRegisterActivity.this,"Password doesn't match..!",Toast.LENGTH_SHORT).show();
        }
        else {
            createUserAccount();
        }

    }

    private void createUserAccount() {
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UserRegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving user info....");

        long timestamp = System.currentTimeMillis();

        String uid = mAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("email",email);
        hashMap.put("mobile",mobile);
        hashMap.put("city",city);
        hashMap.put("password",password);
        hashMap.put("timestamp",String.valueOf(timestamp));
        hashMap.put("address_main","Empty");
        hashMap.put("address_second","Empty");
        hashMap.put("userType","user");


        DatabaseReference reference = database.getReference("Users");
        reference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(UserRegisterActivity.this,"Account Created....",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserRegisterActivity.this, UserHomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UserRegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadCityDialog() {
        dialog = new Dialog(UserRegisterActivity.this);
        dialog.setContentView(R.layout.dialog_searchable_city);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserRegisterActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.txtcity.setText(adapter.getItem(i));
                dialog.dismiss();
            }
        });


    }

}