package com.snt.aqualuxe.Clientes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.exifinterface.media.ExifInterface;

import com.snt.aqualuxe.R;

import java.io.InputStream;
import java.io.IOException;

public class CambioFotoPerfil extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;

    private ImageView profileImageView;
    private Button selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambio_foto_perfil);

        profileImageView = findViewById(R.id.profileImageView);
        selectImageButton = findViewById(R.id.selectImageButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectImageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                showImageSourceDialog();
            }
        });
    }

    private void showImageSourceDialog() {
        String[] options = {"Tomar Foto", "Seleccionar de Galería", "Cancelar"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Selecciona una opción")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            takePhoto();
                            break;
                        case 1:
                            selectPhotoFromGallery();
                            break;
                        case 2:
                            dialog.dismiss();
                            break;
                    }
                })
                .show();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

    private void selectPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = rotateBitmapIfRequired(bitmap, imageUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap thumbnail = (Bitmap) extras.get("data");
                    profileImageView.setImageBitmap(thumbnail);
                }
            }
        }
    }

    private Bitmap rotateBitmapIfRequired(Bitmap img, Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateBitmap(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateBitmap(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateBitmap(img, 270);
                default:
                    return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }
    }

    private Bitmap rotateBitmap(Bitmap img, float degree) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso para la cámara concedido
                selectPhotoFromGallery();
            } else {
                // Permiso para la cámara denegado
                // Puedes mostrar un mensaje al usuario aquí
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso para almacenamiento concedido
                showImageSourceDialog();
            } else {
                // Permiso para almacenamiento denegado
                // Puedes mostrar un mensaje al usuario aquí
            }
        }
    }
}

