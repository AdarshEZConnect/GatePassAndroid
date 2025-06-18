package com.ranjit.gatepass.models;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> userContact = new MutableLiveData<>();
    public MutableLiveData<Integer> departmentId = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> parentContact = new MutableLiveData<>();
    public MutableLiveData<Uri> profileImageUri = new MutableLiveData<>();

}
