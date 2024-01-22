package com.main.project.UI;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
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
import com.main.project.Screens.Auth.Login;


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
            deleteAUser(getContext());

        });
        root.findViewById(R.id.btnAddAdmin).setOnClickListener(v -> {
            addAdmin(getContext());

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

    public void addAdmin(Context context) {
        // Create an AlertDialog builder
        final int[] chosenOption = {0};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the message and title for the dialog (optional)
        builder.setTitle("Choose a User to be an Admin");
        // Get admin emails with the name
        String[] options = dbHelper.getAllUsers().stream() // Get all users
                .filter(user -> !user.isAdmin()) // Filter out admins
                .map(user -> user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")") // Map to name (email)
                .toArray(String[]::new); // Convert to String array
        builder.setItems(options, (dialog, which) -> {
            // Handle option selection
            // 'which' is the index of the selected item
            chosenOption[0] = which;
        });

        // Add Apply button
        builder.setPositiveButton("Apply", (dialog, id) -> {
            //change the user to admin
            // close the current dialog and show an are you sure dialog
            String selectedUser = options[chosenOption[0]];
            // Get the email from the selected user
            String selectedUserEmail = selectedUser.substring(selectedUser.lastIndexOf("(") + 1, selectedUser.lastIndexOf(")"));
            // Make the user an admin
            dbHelper.setAdmin(selectedUserEmail);
            // Show a success message
            Toast.makeText(context, "User " + selectedUserEmail + " is now an admin", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
//            new MaterialAlertDialogBuilder(context)
//                    .setTitle("Are you sure?")
//                    .setMessage("Are you sure you want to make this user "+options[chosenOption[0]]+" an admin?")
//                    .setNeutralButton("Cancel", (dialog1, which) -> {
//                        // Cancel the dialog
//                        dialog1.dismiss();
//                    })
//                    .setPositiveButton("Yes", (dialog1, which) -> {
//                        // Respond to positive button press
//                        // Get the selected user
//                        String selectedUser = options[chosenOption[0]];
//                        // Get the email from the selected user
//                        String selectedUserEmail = selectedUser.substring(selectedUser.lastIndexOf("(") + 1, selectedUser.lastIndexOf(")"));
//                        // Make the user an admin
//                        dbHelper.setAdmin(selectedUserEmail);
//                        // Show a success message
//                        Toast.makeText(context, "User " + selectedUserEmail + " is now an admin", Toast.LENGTH_SHORT).show();
//                    })
//                    .show();

        });

        // Add Cancel button
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // close the dialog
            dialog.dismiss();
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAUser(Context context){
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the message and title for the dialog (optional)
//        builder.setTitle("Choose a User to be an Admin");
        String[] options = dbHelper.getAllUsers().stream() // Get all users
//                .filter(user -> !user.isAdmin()) // Filter out admins
                .filter(user -> !user.getEmail().equals(Login.getUserFromSharedPreferences(context).getEmail()))
                .map(user -> user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")") // Map to name (email)
                .toArray(String[]::new); // Convert to String array

        final int[] chosenOption = {0}; // To hold the selection
        builder.setTitle("Choose a User to delete.")

        // Get admin emails with the name




        // Add Apply button
        .setPositiveButton("Apply", (dialog, id) -> {
            //change the user to admin
            // close the current dialog and show an are you sure dialog
            String selectedUser = options[chosenOption[0]];
            // Get the email from the selected user
            String selectedUserEmail = selectedUser.substring(selectedUser.lastIndexOf("(") + 1, selectedUser.lastIndexOf(")"));

            // Delete the user
            dbHelper.deleteUser(selectedUserEmail);
            // Show a success message
            Toast.makeText(context, "User " + selectedUserEmail + " is now deleted", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();

//            new AlertDialog.Builder(context)
//                    .setTitle("Are you sure?")
////                    .setMessage("Are you sure you want to make this user an admin?")
//                    .setMessage("Are you sure you want to delete this user "+options[chosenOption[0]]+
//                            "?")
//                    .setNegativeButton("Cancel", (dialog1, which) -> {
//                        // Cancel the dialog
////                        dialog1.dismiss();
////                        dialog.dismiss();
//                    })
//                    .setPositiveButton("Yes", (dialog1, which) -> {
//                        // Respond to positive button press
//                        // Get the selected user
//                        String selectedUser = options[chosenOption[0]];
//                        // Get the email from the selected user
//                        String selectedUserEmail = selectedUser.substring(selectedUser.lastIndexOf("(") + 1, selectedUser.lastIndexOf(")"));
//
//                        // Delete the user
//                        dbHelper.deleteUser(selectedUserEmail);
//                        // Show a success message
//                        Toast.makeText(context, "User " + selectedUserEmail + " is now deleted", Toast.LENGTH_SHORT).show();
////                        dialog.dismiss();
//
//                    }).create()
//                    .show();

        })

        // Add Cancel button
        .setNegativeButton("Cancel", (dialog, id) -> {
            // close the dialog
            dialog.dismiss();
        })
                // Add options to the dialog as a single choice list
        .setSingleChoiceItems(options, 0, (dialog, which) -> {
            // 'which' is the index of the selected item
            chosenOption[0] = which;
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}