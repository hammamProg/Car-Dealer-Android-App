package com.example.project.ui.your_reservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class yourReservation_Model extends ViewModel {

        private final MutableLiveData<String> mText;

        public yourReservation_Model() {
            mText = new MutableLiveData<>();
            mText.setValue("This is favor fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }