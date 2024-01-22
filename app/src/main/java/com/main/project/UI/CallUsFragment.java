package com.main.project.UI;

import static com.main.project.UI.AdminDashboard.animateImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.project.Database.DataBaseHelper;
import com.main.project.R;
import com.main.project.Screens.Utilities.CarUtility;
import com.main.project.databinding.FragmentHomeBinding;

public class CallUsFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_call_us, container, false);

        // Add animation to the ImageView
        ImageView infoBackgroundImage = root.findViewById(R.id.infoBackgroundImage);
        animateImageView(infoBackgroundImage);

        // Call Dealer Button
        root.findViewById(R.id.buttonCallDealer).setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:0599000000"));
            startActivity(callIntent);
        });

        // Open Maps Button
        root.findViewById(R.id.buttonOpenMaps).setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=car+dealer");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        // Open Gmail Button
        root.findViewById(R.id.buttonOpenGmail).setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:CarDealer@cars.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
