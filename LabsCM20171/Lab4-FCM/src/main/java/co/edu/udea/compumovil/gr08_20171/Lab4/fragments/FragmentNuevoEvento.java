package co.edu.udea.compumovil.gr08_20171.Lab4.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import co.edu.udea.compumovil.gr08_20171.Lab4.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;
import co.edu.udea.compumovil.gr08_20171.Lab4.Volley.VolleyMultipartRequest;
import co.edu.udea.compumovil.gr08_20171.Lab4.Volley.VolleySingleton;
import co.edu.udea.compumovil.gr08_20171.Lab4.activities.Login;
import co.edu.udea.compumovil.gr08_20171.Lab4.activities.MenuPrincipal;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNuevoEvento extends Fragment {

    private final int OPEN_GALERIA = 200;
    private crearEventoInterfaz mListener;
    private EditText newNameEvent;
    private EditText newResponsibleEvent;
    private TextView newDateEvent;
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
    Uri path_image;


    FirebaseStorage storage;
    StorageReference storageRef;
    UploadTask uploadTask;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;



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
        newDateEvent = (TextView) view.findViewById(R.id.newevent_fecha_event);
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

        newDateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        myRef = database.getReference().child("Eventos") ;

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://lab4fcm-5259d.appspot.com/Eventos");

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

    private void sendRequest(){
        Event evento = new Event();
        evento.setPhoto("");
        evento.setName(nameEvent);
        evento.setDescription(description);
        evento.setResponsible(responsable);
        evento.setScore(Float.valueOf(score));
        evento.setDate(date);
        evento.setGeneralInformation(infoEvent);

        Calendar cal = new GregorianCalendar();

        if (path_image != null){
            StorageReference img = storageRef.child(String.valueOf(cal.getTimeInMillis()));
            uploadTask = img.putFile(path_image);
            evento.setPhoto(img.getPath());
        } else {
            evento.setPhoto("/Eventos/sportlogotipo.9.png");
        }

        myRef.child(String.valueOf(cal.getTimeInMillis())).setValue(evento);
        Toast.makeText(getContext(), "El evento ha sido creado correctamente " , Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(getContext(), MenuPrincipal.class);
        startActivity(intent);
    }

    public void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                newDateEvent.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, dia, mes, ano);

        datePickerDialog.getDatePicker().init(ano, mes, dia, null);
        datePickerDialog.show();

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
                    path_image = data.getData();
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
