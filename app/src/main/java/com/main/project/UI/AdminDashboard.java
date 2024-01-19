package com.main.project.UI;

import android.animation.ObjectAnimator;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.R;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class AdminDashboard extends Fragment {
    DataBaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
        dbHelper = new DataBaseHelper(requireContext());
        // Add animation to the ImageView
        ImageView adminBackgroundImage = root.findViewById(R.id.adminBackgroundImage);
        animateImageView(adminBackgroundImage);

        root.findViewById(R.id.btnDeleteCustomers).setOnClickListener(v -> {
            showMaterialAlertDialog(getContext());

        });
        


        return root;
    }

    private void animateImageView(ImageView imageView) {
        // Create an ObjectAnimator for scaling animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 10f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 10f, 1f);

        // Set the duration of the animation in milliseconds
        int duration = 1000;

        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        // Start the scaling animation
        scaleX.start();
        scaleY.start();
    }
    public void showMaterialAlertDialog(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getResources().getString(R.string.title))
                .setMessage(context.getResources().getString(R.string.supporting_text))
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    // Respond to neutral button press
                })
                .setPositiveButton(context.getResources().getString(R.string.accept), (dialog, which) -> {
                    // Respond to positive button press
                    if (dbHelper.deleteAllCustomers() > 0) {
                        // Deletion successful, show success message
                        Toast.makeText(context, "All customers deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // No customers to delete, show a message
                        Toast.makeText(context, "No customers to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}