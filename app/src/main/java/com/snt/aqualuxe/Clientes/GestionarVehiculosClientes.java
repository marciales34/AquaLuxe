package com.snt.aqualuxe.Clientes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;

import java.io.IOException;
import java.io.InputStream;

public class GestionarVehiculosClientes extends BarraDeNavegacion {

        private static final int PICK_IMAGE_REQUEST = 1;
        private static final int TAKE_PHOTO_REQUEST = 2;
        private static final int REQUEST_CAMERA_PERMISSION = 100;
        private static final int REQUEST_STORAGE_PERMISSION = 101;

        private ImageView fotosVehiculo1;
        private ImageView fotosVehiculo2;
        private Button selectImageButton;
        private int selectedImageView;  // Para identificar cuál ImageView se va a actualizar

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            getLayoutInflater().inflate(R.layout.activity_gestionar_vehiculos_clientes, findViewById(R.id.frameLayout));

            fotosVehiculo1 = findViewById(R.id.fotos_vehiculo1);
            fotosVehiculo2 = findViewById(R.id.fotos_vehiculo2);
            selectImageButton = findViewById(R.id.selectImageButton);

            // Listener para seleccionar una imagen
            selectImageButton.setOnClickListener(v -> {
                showImageTargetDialog();  // Diálogo para elegir cuál ImageView actualizar
            });
        }

        private void showImageTargetDialog() {
            String[] options = {"Actualizar foto 1", "Actualizar foto 2", "Cancelar"};
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Selecciona una opción")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                selectedImageView = R.id.fotos_vehiculo1;  // Asigna a fotos_vehiculo1
                                checkPermissionsAndShowImageSourceDialog();
                                break;
                            case 1:
                                selectedImageView = R.id.fotos_vehiculo2;  // Asigna a fotos_vehiculo2
                                checkPermissionsAndShowImageSourceDialog();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    })
                    .show();
        }

        private void checkPermissionsAndShowImageSourceDialog() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                showImageSourceDialog();
            }
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
                if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = rotateBitmapIfRequired(bitmap, imageUri);
                        setImageViewBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == TAKE_PHOTO_REQUEST && data.getExtras() != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    setImageViewBitmap(thumbnail);
                }
            }
        }

        private void setImageViewBitmap(Bitmap bitmap) {
            // Asigna la imagen seleccionada al ImageView correcto
            if (selectedImageView == R.id.fotos_vehiculo1) {
                fotosVehiculo1.setImageBitmap(bitmap);
            } else if (selectedImageView == R.id.fotos_vehiculo2) {
                fotosVehiculo2.setImageBitmap(bitmap);
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
                    takePhoto();
                }
            } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhotoFromGallery();
                }
            }
        }
    }
