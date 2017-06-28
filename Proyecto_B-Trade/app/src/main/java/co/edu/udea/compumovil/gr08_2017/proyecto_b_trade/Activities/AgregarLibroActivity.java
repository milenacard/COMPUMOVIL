package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class AgregarLibroActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_PHOTO_FOR_AVATAR = 2;

    private Button tomarFoto;
    private Button elegirFoto;
    private Button agregar;
    private ImageView imageView;
    private TextView textoImgView;
    private EditText nombre;
    private EditText autor;
    private EditText genero;
    private EditText paginas;
    private EditText idioma;
    private EditText sinopsis;
    private RatingBar estado;
    private Uri uriFoto;

    String propietario;


    private FirebaseStorage storage;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_libro);

        Intent intent = getIntent();
        propietario = intent.getStringExtra("propietario");

        tomarFoto = (Button) findViewById(R.id.tomar_foto_btn);
        elegirFoto = (Button) findViewById(R.id.elegir_foto_btn);
        agregar = (Button) findViewById(R.id.agregar_libro_btn);
        imageView = (ImageView) findViewById(R.id.foto_libro_imgview);
        textoImgView = (TextView) findViewById(R.id.texto_imgview);

        nombre = (EditText) findViewById(R.id.nombre_libro_ET);
        autor = (EditText) findViewById(R.id.autor_libro_ET);
        genero = (EditText) findViewById(R.id.genero_libro_ET);
        paginas = (EditText) findViewById(R.id.paginas_libro_ET);
        idioma = (EditText) findViewById(R.id.idioma_libro_ET);
        sinopsis = (EditText) findViewById(R.id.sinopsis_libro_ET);

        estado = (RatingBar) findViewById(R.id.estado_RB);

        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        elegirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarFormulario(v)){
                    subirAFirebase();
                    finish();
                }
            }
        });
    }

    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == PICK_PHOTO_FOR_AVATAR)&& resultCode == RESULT_OK) {
            uriFoto = data.getData();
//            Glide.with(getApplicationContext()).load(uriFoto).apply(RequestOptions.fitCenterTransform()).into(imageView);
            Picasso.with(getApplicationContext()).load(uriFoto).into(imageView);
            textoImgView.setVisibility(View.GONE);
        }

    }

    private boolean validarFormulario(View v){
        boolean valido = true;
        String texto;

        nombre.setError(null);
        autor.setError(null);
        genero.setError(null);
        idioma.setError(null);
        paginas.setError(null);
        sinopsis.setError(null);

        texto = nombre.getText().toString();
        if (TextUtils.isEmpty(texto)){
            nombre.setError("Este campo es requerido");
            valido = false;
        }

        texto = autor.getText().toString();
        if (TextUtils.isEmpty(texto)){
            autor.setError("Este campo es requerido");
            valido = false;
        }

        texto = genero.getText().toString();
        if (TextUtils.isEmpty(texto)){
            genero.setError("Este campo es requerido");
            valido = false;
        }

        texto = paginas.getText().toString();
        if (TextUtils.isEmpty(texto)){
            paginas.setError("Este campo es requerido");
            valido = false;
        }

        texto = idioma.getText().toString();
        if (TextUtils.isEmpty(texto)){
            idioma.setError("Este campo es requerido");
            valido = false;
        }

        texto = sinopsis.getText().toString();
        if (TextUtils.isEmpty(texto)){
            sinopsis.setError("Este campo es requerido");
            valido = false;
        }

        if (uriFoto == null){
            Snackbar.make(v, "Tienes que subir una foto del libro.", Snackbar.LENGTH_LONG).show();
            valido = false;
        }

        if (estado.getRating() == 0){
            Snackbar.make(v, "Debes seleccionar el estado del libro.", Snackbar.LENGTH_LONG).show();
            valido = false;
        }

        return valido;
    }

    public void subirAFirebase(){
        Calendar cal = new GregorianCalendar();
        final String nombreFirebase = String.valueOf(cal.getTimeInMillis());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://b-trade-bde54.appspot.com/libros");


        final Libro libro = new Libro();

        libro.setPropietario(propietario);
        libro.setNombre(nombre.getText().toString());
        libro.setAutor(autor.getText().toString());
        libro.setGenero(genero.getText().toString());
        libro.setIdioma(idioma.getText().toString());
        libro.setSinopsis(sinopsis.getText().toString());
        libro.setPaginas(paginas.getText().toString());
        libro.setEstado(estado.getRating());

        final StorageReference img = storageRef.child(nombreFirebase);
        uploadTask = img.putFile(uriFoto);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //noinspection VisibleForTests

                libro.setFoto(taskSnapshot.getDownloadUrl().toString());
                myRef = database.getReference().child("Libros");
                myRef.child(String.valueOf(nombreFirebase)).setValue(libro);
            }
        });

    }

}
