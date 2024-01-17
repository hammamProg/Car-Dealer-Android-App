package com.main.project.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.project.R;
import com.main.project.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

//    private ViewFlipper viewFlipper;
//    private Button btnMoveLeft;
//    private Button btnMoveRight;

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        viewFlipper = view.findViewById(R.id.viewFlipper);  // Initialize the ViewFlipper

//        btnMoveLeft = view.findViewById(R.id.btnMoveLeft);
//        btnMoveRight = view.findViewById(R.id.btnMoveRight);
//
//        btnMoveLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moveLeft();
//            }
//        });
//
//        btnMoveRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moveRight();
//            }
//        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //    // Method to move ViewFlipper to the left
//    public void moveLeft() {
//        viewFlipper.showPrevious();
//    }
//
//    // Method to move ViewFlipper to the right
//    public void moveRight() {
//        viewFlipper.showNext();
//    }
}
