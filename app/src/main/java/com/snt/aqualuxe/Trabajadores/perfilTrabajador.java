package com.snt.aqualuxe.Trabajadores;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.exifinterface.media.ExifInterface;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.Clientes.PerfilClientes;
import com.snt.aqualuxe.R;
import com.snt.aqualuxe.RetrofitClient;
import com.snt.aqualuxe.serviciosClientes.Usuario;
import com.snt.aqualuxe.serviciosClientes.UsuarioApi;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class perfilTrabajador extends BarraDeNavegacion {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;

    private ImageView profileImageView;
    private Button selectImageButton;

    private TextView textViewNombre;
    private TextView textViewCorreo;
    private TextView textViewTelefono;
    private TextView textViewCiudad;
    private TextView textViewDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_perfil_trabajador, findViewById(R.id.frameLayout));

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(sharedPreferences.getString("userID", "0"));

        // Si el ID es válido, realizamos la petición
        if (userId != 0) {
            obtenerDatosUsuario(userId); // Llamamos a la función pasando el ID
        } else {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show();
        }



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

    private void obtenerDatosUsuario(int userId) {
        // Crear la instancia de Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        UsuarioApi usuarioApi = retrofit.create(UsuarioApi.class);

        // Hacer la llamada a la API
        Call<Usuario> call = usuarioApi.obtenerUsuarioPorId(userId);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    // Actualizar los TextViews con los datos del usuario
                    textViewNombre = findViewById(R.id.textView_container_nombre_perfil);
                    textViewCorreo = findViewById(R.id.textView_container_correo_perfil);
                    textViewTelefono = findViewById(R.id.textView_container_telefono_perfil);
                    textViewCiudad = findViewById(R.id.textView_container_ciudad_perfil);
                    textViewDireccion = findViewById(R.id.textView_container_direccion_perfil); // Asegúrate de que este ID sea correcto

                    textViewNombre.setText(usuario.getNombre());
                    textViewCorreo.setText(usuario.getCorreo());
                    textViewTelefono.setText(usuario.getTelefono());
                    textViewCiudad.setText(usuario.getCiudad());
                    textViewDireccion.setText(usuario.getDireccion());
                } else {
                    // Agregar logs para depuración
                    Log.e("PerfilTrabajadores", "Error en la respuesta: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("PerfilTrabajadores", "Cuerpo de la respuesta: " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Toast.makeText(perfilTrabajador.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(perfilTrabajador.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
