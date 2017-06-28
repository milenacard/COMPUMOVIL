package co.edu.udea.compumovil.gr08_20171.Lab3.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;
import co.edu.udea.compumovil.gr08_20171.Lab3.Volley.VolleyMultipartRequest;
import co.edu.udea.compumovil.gr08_20171.Lab3.Volley.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNuevoEvento extends Fragment {
    private final int OPEN_GALERIA = 200;



    private crearEventoInterfaz mListener;


    private EditText newNameEvent;
    private EditText newResponsibleEvent;
    private EditText newDateEvent;
    private EditText newScoreEvent;
    private EditText newUbicationEvent;
    private EditText newGralInfoEvent;
    private EditText newDescriptionEvent;
    private Button createEvent;
    private Button  uploadImage;
    private ImageView newPhotoEvent;
    public FragmentManager fragmentManager;
    private String urlPost;
    private String nameEvent;
    private String infoEvent;
    private String ubication;
    private String score;
    private String date;
    private String responsable;
    private String description;
    private byte[] imagenSeleccionada = null;


    public FragmentNuevoEvento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_evento, container, false);

        getActivity().setTitle("Nuevo Evento");

        newNameEvent = (EditText) view.findViewById(R.id.newevent_name_event);
        newResponsibleEvent = (EditText) view.findViewById(R.id.newevent_responsible);
        newDateEvent = (EditText) view.findViewById(R.id.newevent_fecha_event);
        newScoreEvent = (EditText) view.findViewById(R.id.newevent_score);
        newUbicationEvent = (EditText) view.findViewById(R.id.newevent_ubication_event);
        newGralInfoEvent = (EditText) view.findViewById(R.id.newevent_infogeneral);
        newDescriptionEvent = (EditText) view.findViewById(R.id.newevent_description_event);
        createEvent = (Button) view.findViewById(R.id.btn_save);
        uploadImage = (Button) view.findViewById(R.id.btn_subir);
        newPhotoEvent = (ImageView) view.findViewById(R.id.photo_new_event);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent(view);
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });
        return view;
    }

    private void saveEvent(View view) {
        nameEvent = newNameEvent.getText().toString();
        responsable = newResponsibleEvent.getText().toString();
        date = newDateEvent.getText().toString();
        score = newScoreEvent.getText().toString();
        ubication = newUbicationEvent.getText().toString();
        infoEvent = newGralInfoEvent.getText().toString();
        description = newDescriptionEvent.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(nameEvent)) {
            newNameEvent.setError(getString(R.string.error_field_required));
            focusView = newNameEvent;
            cancel = true;

        } else if (TextUtils.isEmpty(responsable)) {
            newResponsibleEvent.setError(getString(R.string.error_field_required));
            focusView = newResponsibleEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(date)) {
            newDateEvent.setError(getString(R.string.error_field_required));
            focusView = newDateEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(ubication)) {
            newUbicationEvent.setError(getString(R.string.error_field_required));
            focusView = newUbicationEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(infoEvent)) {
            newGralInfoEvent.setError(getString(R.string.error_field_required));
            focusView = newGralInfoEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(description)) {
            newDescriptionEvent.setError(getString(R.string.error_field_required));
            focusView = newDescriptionEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(score)) {
            newScoreEvent.setError(getString(R.string.error_field_required));
            focusView = newScoreEvent;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            sendRequest();

        }
    }

    private void sendRequest() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                urlPost = "http://" + IpDispositivoServicio.ip + ":3000/api/events";
                final String urlMultipart = "http://" + IpDispositivoServicio.ip + ":3000/api/Containers/all/upload";
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST,
                                urlPost,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        Event evento = new Event();
                                        evento.setPhoto("");
                                        evento.setName(nameEvent);
                                        evento.setDescription(description);
                                        evento.setResponsible(responsable);
                                        evento.setScore(Float.valueOf(score));
                                        evento.setDate(date);
                                        evento.setGeneralInformation(infoEvent);

                                        DbHelper dbHelper = new DbHelper(getActivity());
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                                        dbHelper.addSportEvent(evento);

                                        Log.d("Agregar Evento", "Se ha agregado Evento en la DB y en el servidor");

                                        redireccionarAEventos();

                                        sendImage(urlMultipart,"E".concat(evento.getName()).concat("img.jpg"));

                                        Toast.makeText(getContext(),"Evento agregado",Toast.LENGTH_LONG).show();
                                        getActivity().onBackPressed();
                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ErrorPeticion", "Error: " + error.toString());
                                Toast.makeText(getContext(),"Error agregando el  evento",Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("foto", "img.jpg");
                        params.put("descripcion", description);
                        params.put("nombre", nameEvent);
                        params.put("responsable", responsable);
                        params.put("puntuacion", score);
                        params.put("fecha", date);
                        params.put("latitud", "0");
                        params.put("longitud", "0");
                        params.put("informacionGeneral", infoEvent);

                        return params;
                    }
                };
                queue.add(stringRequest);

            }
        });
        hilo.start();
    }

    public void redireccionarAEventos() {
        if (mListener != null) {
            mListener.crearEventoInteraccion();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof crearEventoInterfaz) {
            mListener = (crearEventoInterfaz) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement crearEventoInterfaz");
        }
    }

    public interface crearEventoInterfaz {
        void crearEventoInteraccion();
    }
    private void sendImage(String url, final String nameImage) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error subiendo la imagen", Toast.LENGTH_SHORT).show();
                //Log.d("nada3", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image", new DataPart(nameImage, imagenSeleccionada, "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("SessionId", mSessionId);
                return headers;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(multipartRequest);
    }


    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), OPEN_GALERIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_GALERIA:
                    Uri path_image = data.getData();
                    newPhotoEvent.setImageURI(path_image);
                    Bitmap bitM = ((BitmapDrawable) newPhotoEvent.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitM.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    imagenSeleccionada = stream.toByteArray();
                    newPhotoEvent.setImageBitmap(bitM);

                    break;
            }

        }
    }


}
