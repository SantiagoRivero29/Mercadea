package com.example.mercadea.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercadea.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class MLKitFragment extends Fragment {

    private ImageView imgPreview;
    private TextView txtResults;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        imgPreview.setImageBitmap(bitmap);
                        processImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mlkit, container, false);
        imgPreview = view.findViewById(R.id.imgPreview);
        txtResults = view.findViewById(R.id.txtResults);
        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);

        btnChooseImage.setOnClickListener(v -> chooseImage());
        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void processImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Usar el nuevo ImageLabelerOptions
        var labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(labels -> {
                    StringBuilder result = new StringBuilder("Etiquetas detectadas:\n");
                    for (ImageLabel label : labels) {
                        result.append(label.getText())
                                .append(" (Confianza: ")
                                .append(String.format("%.2f", label.getConfidence() * 100))
                                .append("%)\n");
                    }
                    txtResults.setText(result.toString());
                })
                .addOnFailureListener(e -> txtResults.setText("Error: " + e.getMessage()));
    }
}
