package com.example.project.ui.your_favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class yourFavorites_Model extends ViewModel {

        private final MutableLiveData<String> mText;

        public yourFavorites_Model() {
            mText = new MutableLiveData<>();
            mText.setValue("This is favor fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }