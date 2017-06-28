package co.edu.udea.compumovil.gr08_20171.Lab3.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;

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

    int id = 0;

    private final int MY_PERMISSIONS = 100;
    private final int OPEN_CAMERA = 200;
    private final int OPEN_GALLERY = 300;
    private String url;
    private String urlPOST;


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

        name.setText(getArguments().getString(DBInfomation.ColumnUser.NAME));
        user.setText(getArguments().getString(DBInfomation.ColumnUser.USERNAME));
        email.setText(getArguments().getString(DBInfomation.ColumnUser.EMAIL));
        age.setText(getArguments().getString(DBInfomation.ColumnUser.AGE));

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

            System.out.println("hola");
            nombre = name.getText().toString();
            usuario = user.getText().toString();
            contra = password.getText().toString();
            edad = age.getText().toString();
            correo = email.getText().toString();
            System.out.println("hola");

            url = "http://" + IpDispositivoServicio.ip + ":3000/api/users/findOne?filter[where][username]=";
            url = url + usuario;
            getIDUser();


        }

    }

    private void getIDUser() {
        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(getContext());

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET,
                                url,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(url);

                                        System.out.println(response.toString());
                                        try {
                                            id = response.getInt("id");
                                            sendRequest();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Datos Login", "Error: " + error.toString());

                            }
                        }
                        );
                queue.add(jsObjRequest);


            }

        });
        hilo.start();
    }

    private void sendRequest() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                urlPOST = "http://" + IpDispositivoServicio.ip + ":3000/api/users/" + id;
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest stringRequest = new StringRequest
                        (Request.Method.PUT,
                                urlPOST,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        Log.d("eDITAR USUARIO", "Se ha editado el usuario");

                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ErrorPeticion", "Error: " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("foto", null);
                        params.put("username", usuario);
                        params.put("nombre", nombre);
                        params.put("password", contra);
                        params.put("email", correo);
                        return params;
                    }
                };
                queue.add(stringRequest);

            }
        });
        hilo.start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_CAMERA && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.d("Editar Perfil", "Tengo la foto desde la camara");

            foto.setImageBitmap(imageBitmap);
        } else if (requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK) {

            Log.d("Editar Perfil", "Tengo la foto desde la galería");
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
