package co.edu.udea.compumovil.gr08_20171.Lab4.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import co.edu.udea.compumovil.gr08_20171.Lab4.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;
import co.edu.udea.compumovil.gr08_20171.Lab4.Volley.VolleyMultipartRequest;
import co.edu.udea.compumovil.gr08_20171.Lab4.activities.MenuPrincipal;
import co.edu.udea.compumovil.gr08_20171.Lab4.activities.Register;

/**
 * Created by Milena Cardenas on 29-mar-17.
 */
public class FragmentEditPerfil extends Fragment implements View.OnClickListener {

    EditText name;
    EditText user;
    EditText email;
    EditText age;
    EditText password;
    Button subir;
    Button guardar;
    ImageView foto;
    RadioGroup radioGroup;

    String nombre;
    String usuario;
    String correo;
    String edad;
    String contra;
    Uri path_image;

    int id = 0;

    private final int MY_PERMISSIONS = 100;
    private final int OPEN_CAMERA = 200;
    private final int OPEN_GALLERY = 300;
    private String url;
    private String urlPOST;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private EditText mAge;
    private EditText mName;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myref;
    UploadTask uploadTask;
    private byte[] imagenSeleccionada = null;


    public FragmentEditPerfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Editar perfil");
        View view = inflater.inflate(R.layout.fragment_edit_perfil, container, false);

        name = (EditText) view.findViewById(R.id.edit_perfil_name);
        user = (EditText) view.findViewById(R.id.edit_perfil_username);
        email = (EditText) view.findViewById(R.id.edit_perfil_email);
        age = (EditText) view.findViewById(R.id.edit_perfil_age);
        password = (EditText) view.findViewById(R.id.edit_perfil_password);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg_radio_group);
        foto = (ImageView) view.findViewById(R.id.edit_perfil_photo_usuario);
        subir = (Button) view.findViewById(R.id.btn_subir);
        guardar = (Button) view.findViewById(R.id.btn_guardar_edit);


        guardar.setOnClickListener(this);
        subir.setOnClickListener(this);
        Picasso.with(foto.getContext()).load(getArguments().getString(DBInfomation.ColumnUser.PHOTO)).into(foto);
        name.setText(getArguments().getString(DBInfomation.ColumnUser.NAME));
        user.setText(getArguments().getString(DBInfomation.ColumnUser.USERNAME));
        email.setText(getArguments().getString(DBInfomation.ColumnUser.EMAIL));
        age.setText(getArguments().getString(DBInfomation.ColumnUser.AGE));

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://lab4fcm-5259d.appspot.com/Usuarios");

        myref = database.getReference().child("Usuarios");

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_subir) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rb_camara) {
                if (verificarPermisoCamara()) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, OPEN_CAMERA);
                    }

                } else {
                    requestPermissions(new String[]{getContext().CAMERA_SERVICE}, MY_PERMISSIONS);
                }

            } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_galeria) {
                if (verificarPermisoGaleria()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(intent, OPEN_GALLERY);
                    }

                } else {
                    requestPermissions(new String[]{getContext().STORAGE_SERVICE}, MY_PERMISSIONS);
                }
            }
        } else if (view.getId() == R.id.btn_guardar_edit) {

            saveInfo(view);
        }

    }

    //Guarda la informacion en la base de datos
    public void saveInfo(View v) {
        user.setError(null);
        password.setError(null);
        email.setError(null);
        age.setError(null);
        name.setError(null);

        nombre = name.getText().toString();
        usuario = user.getText().toString();
        contra = password.getText().toString();
        edad = age.getText().toString();
        correo = email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(usuario)) {
            user.setError(getString(R.string.error_field_required));
            focusView = user;
            cancel = true;
        } else if (!TextUtils.isEmpty(usuario) && (usuario.length() < 4)) {
            user.setError(getString(R.string.error_invalid_username));
            focusView = user;
            cancel = true;
        } else if (TextUtils.isEmpty(nombre)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        } else if (!TextUtils.isEmpty(nombre) && !validarNombre(nombre, 5)) {
            name.setError(getString(R.string.error_name));
            focusView = name;
            cancel = true;
        } else if (TextUtils.isEmpty(correo)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!TextUtils.isEmpty(correo) && !esCorreoValido(correo)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        } else if (TextUtils.isEmpty(edad)) {
            age.setError(getString(R.string.error_field_required));
            focusView = age;
            cancel = true;
        } else if (!TextUtils.isEmpty(edad) && !validarEdad(edad, 3)) {
            age.setError(getString(R.string.error_age));
            focusView = age;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = "";

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        String emailDB = (String) singleSnapshot.child("email").getValue();

                        Log.d("correoEditar", correo);
                        if (emailDB.equals(correo)) {
                            key = singleSnapshot.getKey();
                        }
                    }

                    if (!key.equals("")) {
                        myref = myref.child(key);
                        DatabaseReference refAux;

                        Calendar cal = new GregorianCalendar();

                        refAux = myref.child("username");
                        refAux.setValue(usuario);

                        refAux = myref.child("name");
                        refAux.setValue(nombre);

                        refAux = myref.child("email");
                        refAux.setValue(correo);

                        refAux = myref.child("age");
                        refAux.setValue(edad);

                        if (path_image != null) {
                            StorageReference img = storageRef.child(String.valueOf(cal.getTimeInMillis()));
                            uploadTask = img.putFile(path_image);
                            refAux = myref.child("photo");
                            refAux.setValue(img.getPath());
                        } //else {
                        //refAux = myref.child("photo");
                        //refAux.setValue("/Usuarios/deafult.png");
                        //}
                    }

                    Toast.makeText(getContext(), "Datos Guardados ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MenuPrincipal.class);
                    intent.putExtra(DBInfomation.ColumnUser.NAME, nombre);
                    intent.putExtra(DBInfomation.ColumnUser.USERNAME, usuario);
                    intent.putExtra(DBInfomation.ColumnUser.EMAIL, correo);
                    intent.putExtra(DBInfomation.ColumnUser.AGE, edad);
                    if (path_image != null) {
                        intent.putExtra(DBInfomation.ColumnUser.PHOTO, path_image);
                    }
                    startActivity(intent);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(">>> Error:" + "find onCancelled:" + databaseError);
                }
            });
        }
    }


    public boolean validarNombre(String name, int limit) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        return patron.matcher(name).matches() && name.length() > limit;
    }

    public boolean esCorreoValido(String correo) {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    private boolean validarEdad(String age, int i) {
        return (age.length() < i);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_CAMERA && resultCode == Activity.RESULT_OK) {
            path_image = data.getData();
            foto.setImageURI(path_image);
            Bitmap bitM = ((BitmapDrawable) foto.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitM.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagenSeleccionada = stream.toByteArray();
            foto.setImageBitmap(bitM);

        } else if (requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
            path_image = data.getData();
            foto.setImageURI(path_image);
            Bitmap bitM = ((BitmapDrawable) foto.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitM.compress(Bitmap.CompressFormat.PNG, 100, stream);

            imagenSeleccionada = stream.toByteArray();
            foto.setImageBitmap(bitM);
        }
    }

    public boolean verificarPermisoCamara() {

        //Si android es < 6.0 tiene los permisos
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        //Verifica los permisos en android >= 6.0
        if (getContext().checkSelfPermission(Context.CAMERA_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    public boolean verificarPermisoGaleria() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (getContext().checkSelfPermission(Context.STORAGE_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }


}
