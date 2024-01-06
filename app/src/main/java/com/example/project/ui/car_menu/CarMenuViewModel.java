package com.example.project.ui.car_menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarMenuViewModel extends ViewModel {

        private final MutableLiveData<String> mText;

        public CarMenuViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is CarMenu fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }