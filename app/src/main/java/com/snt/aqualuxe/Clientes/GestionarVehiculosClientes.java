package com.snt.aqualuxe.Clientes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.snt.aqualuxe.BarraDeNavegacion;
import com.snt.aqualuxe.R;
import com.snt.aqualuxe.RetrofitClient;
import com.snt.aqualuxe.serviciosVehiculo.RegistrarVehiculo;
import com.snt.aqualuxe.serviciosVehiculo.Vehiculo;
import com.snt.aqualuxe.serviciosVehiculo.VehiculoApi;
import com.snt.aqualuxe.serviciosVehiculo.VehiculoResponseAdapter;
import com.snt.aqualuxe.serviciosVehiculo.VehiculosResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GestionarVehiculosClientes extends BarraDeNavegacion {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;

    private ImageView fotosVehiculo;
    private Button selectImageButton;
    private EditText editTextMarca;
    private EditText editTextTipo;
    private EditText editTextModelo;
    private EditText editTextPlaca;
    private EditText editTextColor;

    private Button btnRegistrar;

    private int userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_gestionar_vehiculos_clientes, findViewById(R.id.frameLayout));

        fotosVehiculo = findViewById(R.id.fotos_vehiculo1);  // Solo un ImageView
        selectImageButton = findViewById(R.id.selectImageButton);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = Integer.parseInt(sharedPreferences.getString("userID", "0"));

        // Listener para seleccionar una imagen
        selectImageButton.setOnClickListener(v -> {
            showImageTargetDialog();  // Diálogo para elegir cuál ImageView actualizar
        });

        // Inicializar los EditText
        editTextMarca = findViewById(R.id.editTextMarca);
        editTextTipo = findViewById(R.id.editTextTipo);
        editTextModelo = findViewById(R.id.editTextModelo);
        editTextPlaca = findViewById(R.id.editTextPlaca);
        editTextColor = findViewById(R.id.editTextColor);

        // Inicializar el botón de registrar vehículo
        btnRegistrar = findViewById(R.id.btn_registrar_vehiculo);
        btnRegistrar.setOnClickListener(v -> registrarVehiculo());

        // Inicializar el botón para seleccionar la imagen del vehículo
        fotosVehiculo.setOnClickListener(v -> seleccionarImagen());
    }

    // Método para seleccionar la imagen
    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();  // Obtener el URI de la imagen seleccionada

                // Obtener la ruta real del archivo
                String realPath = getRealPathFromURI(this, imageUri);
                Log.d("ImagenURL", "Ruta de la imagen seleccionada: " + realPath);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap = rotateBitmapIfRequired(bitmap, imageUri); // Rotar la imagen si es necesario

                    // Convertir la imagen a JPEG y guardar
                    String savedPath = saveBitmapAsJPEG(bitmap); // Guarda la imagen como JPEG

                    // Establecer la imagen en el ImageView (si deseas mostrarla también)
                    setImageViewBitmap(bitmap, imageUri);  // Pasa la URI junto con la imagen

                    // Aquí deberías guardar la ruta de la imagen en un campo que luego será enviado
                    // al servidor
                    fotosVehiculo.setTag(savedPath);  // Guardar la ruta de la imagen en el tag
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String saveBitmapAsJPEG(Bitmap bitmap) {
        try {
            // Crear el archivo donde se guardará la imagen
            File outputFile = new File(getExternalFilesDir(null), "converted_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // Comprimir el Bitmap a JPEG
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // 100 es la calidad máxima
            outputStream.flush();
            outputStream.close();

            // Log para verificar que la imagen se guardó correctamente
            Log.d("Image Conversion", "Imagen convertida y guardada en: " + outputFile.getAbsolutePath());
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void setImageViewBitmap(Bitmap bitmap, Uri imageUri) {
        fotosVehiculo.setImageBitmap(bitmap);
        fotosVehiculo.setTag(imageUri);  // Guardar la URI de la imagen
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

    private void showImageTargetDialog() {
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return null;
        }
    }

    public String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName(); // Esto devuelve el nombre del archivo, no la ruta completa
    }

    private void registrarVehiculo() {
        String marca = editTextMarca.getText().toString().trim();
        String tipo = editTextTipo.getText().toString().trim();
        String modelo = editTextModelo.getText().toString().trim();
        String placa = editTextPlaca.getText().toString().trim();
        String color = editTextColor.getText().toString().trim();
        String usuarioId = String.valueOf(userId); // Cambia esto según cómo obtienes el ID del usuario

        // Validar que los campos no estén vacíos
        if (marca.isEmpty() || tipo.isEmpty() || modelo.isEmpty() || placa.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la URL de la imagen cargada en el ImageView
        String imagenUrl = fotosVehiculo.getTag() != null ? fotosVehiculo.getTag().toString() : null;

        // Si la URL de la imagen no es nula, extraer solo el nombre del archivo
        if (imagenUrl != null) {
            // Extraer solo el nombre del archivo
            File imagenFile = new File(imagenUrl);
            String nombreImagen = imagenFile.getName(); // Esto extrae solo el nombre del archivo, por ejemplo "converted_image.jpg"
            imagenUrl = nombreImagen;
        }

        // Log para verificar los datos antes de enviarlos
        Log.d("RegistrarVehiculo", "Marca: " + marca);
        Log.d("RegistrarVehiculo", "Tipo: " + tipo);
        Log.d("RegistrarVehiculo", "Modelo: " + modelo);
        Log.d("RegistrarVehiculo", "Placa: " + placa);
        Log.d("RegistrarVehiculo", "Color: " + color);
        Log.d("RegistrarVehiculo", "Usuario ID: " + usuarioId);
        Log.d("RegistrarVehiculo", "Imagen URL: " + imagenUrl);

        // Crear el objeto Vehiculo
        RegistrarVehiculo vehiculo = new RegistrarVehiculo(marca, tipo, modelo, placa, color, usuarioId, imagenUrl);

        // Crear Retrofit y la llamada
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        VehiculoApi api = retrofit.create(VehiculoApi.class);

// Llamada a la API usando la nueva clase adaptadora
        Call<VehiculoResponseAdapter> call = api.registrarVehiculo(vehiculo);

        call.enqueue(new Callback<VehiculoResponseAdapter>() {
            @Override
            public void onResponse(Call<VehiculoResponseAdapter> call, Response<VehiculoResponseAdapter> response) {
                if (response.isSuccessful()) {
                    // Aquí accedes a la respuesta
                    VehiculoResponseAdapter vehiculoResponse = response.body();

                    if (vehiculoResponse != null) {
                        Object data = vehiculoResponse.getData();

                        // Si la respuesta es una lista, la puedes convertir
                        if (data instanceof List) {
                            List<Vehiculo> vehiculos = (List<Vehiculo>) data;

                            if (!vehiculos.isEmpty()) {
                                Vehiculo vehiculo = vehiculos.get(0); // Si esperas solo el primer vehículo
                                Log.d("RegistrarVehiculo", "Vehículo registrado: " + vehiculo.getMarca());
                                Toast.makeText(GestionarVehiculosClientes.this, "Vehículo registrado exitosamente", Toast.LENGTH_SHORT).show();

                                // Redirigir a la actividad RegistrarAutosClientes
                                Intent intent = new Intent(GestionarVehiculosClientes.this, RegistrarVehiculosClientes.class);
                                startActivity(intent);
                                finish(); // Cerrar la actividad actual si no la necesitas más
                            } else {
                                Log.e("ErrorRegistroVehiculo", "No se recibieron vehículos en la respuesta");
                                Toast.makeText(GestionarVehiculosClientes.this, "No se recibieron vehículos", Toast.LENGTH_SHORT).show();
                            }
                        } else if (data instanceof Vehiculo) {
                            // Si la respuesta es un solo objeto Vehiculo
                            Vehiculo vehiculo = (Vehiculo) data;
                            Log.d("RegistrarVehiculo", "Vehículo registrado: " + vehiculo.getMarca());
                            Toast.makeText(GestionarVehiculosClientes.this, "Vehículo registrado exitosamente", Toast.LENGTH_SHORT).show();

                            // Redirigir a la actividad RegistrarAutosClientes
                            Intent intent = new Intent(GestionarVehiculosClientes.this, RegistrarVehiculosClientes.class);
                            startActivity(intent);
                            finish(); // Cerrar la actividad actual si no la necesitas más
                        }
                    } else {
                        Log.e("ErrorRegistroVehiculo", "Respuesta vacía");
                        Toast.makeText(GestionarVehiculosClientes.this, "Error al registrar vehículo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("ErrorRegistroVehiculo", "Error al registrar vehículo: " + errorMessage);
                        Toast.makeText(GestionarVehiculosClientes.this, "Error al registrar vehículo", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<VehiculoResponseAdapter> call, Throwable t) {
                Log.e("ErrorRegistroVehiculo", "Error de conexión: " + t.getMessage());
                Toast.makeText(GestionarVehiculosClientes.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}