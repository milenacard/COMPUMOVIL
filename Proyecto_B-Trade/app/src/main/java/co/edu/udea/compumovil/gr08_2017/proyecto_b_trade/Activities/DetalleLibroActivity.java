package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Chat;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Interes;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Mensaje;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class DetalleLibroActivity extends AppCompatActivity {

    private LinearLayout mRevealView;
    private LinearLayout nombreHolder;
    private ImageView imageView;
    private TextView datosLibro;
    private TextView nombreLibro;
    private RatingBar estadoLibro;
    private TextView sinopsisLibro;
    private EditText mensaje;
    private FloatingActionButton mFloatingActionButton;

    private String email;
    private String key;
    private String interesIniciador;

    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference myRef;

    Libro libro;
    Bitmap imagen;

    String id;

    private boolean isEditTextVisible;
    private boolean esInteres;
    private boolean intercambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

        mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.btn_add);
        nombreHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
        estadoLibro = (RatingBar) findViewById(R.id.detail_libro_estado);
        sinopsisLibro = (TextView) findViewById(R.id.sinopsis_libro);
        datosLibro = (TextView) findViewById(R.id.datos_libro);
        nombreLibro = (TextView) findViewById(R.id.nombre_libro);
        imageView = (ImageView) findViewById(R.id.placeImage);
        mensaje = (EditText) findViewById(R.id.mensaje_interes_ET);

        libro = (Libro) getIntent().getSerializableExtra("libro");
        esInteres = getIntent().getBooleanExtra("interes", false);
        intercambio = getIntent().getBooleanExtra("intercambio", false);

        Glide.with(getApplicationContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(imageView);

        int color = getIntent().getIntExtra("color fondo", Color.BLACK);
        email = getIntent().getStringExtra("email");
        System.out.println("EMAIL: " + email);
        key = getIntent().getStringExtra("key");
        interesIniciador = getIntent().getStringExtra("interesIniciador");

        nombreLibro.setBackgroundColor(color);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextVisible) {

                    Calendar cal = new GregorianCalendar();
                    id = String.valueOf(cal.getTimeInMillis());

                    crearInteresFirebase();
                    crearMensajeFirebase();
                    if (intercambio) {
                        crearChatFirebase();
                    }
                    finish();
                } else {
                    revealEditText(mRevealView);
                }
            }
        });
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

        if (esInteres) {
            mFloatingActionButton.setVisibility(View.GONE);
        }

        String datos = "Autor: " + libro.getAutor() + "\nGénero: "
                + libro.getGenero() + "\nIdioma: " + libro.getIdioma() + "\nNúmero de Páginas: " +
                libro.getPaginas();

        sinopsisLibro.setText(libro.getSinopsis());
        estadoLibro.setRating(libro.getEstado());
        System.out.println("El flotante es: " + libro.getEstado());
        datosLibro.setText(datos);
        nombreLibro.setText(libro.getNombre());
    }

    private void crearChatFirebase() {
        //TODO: Crear chat si agrega interes desde librería publica
        Chat chat = new Chat(libro.getPropietario(), interesIniciador, email, key);
        myRef = db.getReference().child("Chats");

        myRef.child(id).setValue(chat);
    }

    private void crearMensajeFirebase() {
        if (!TextUtils.isEmpty(mensaje.getText().toString())) {
            myRef = db.getReference().child("Mensajes");
            Mensaje msg = new Mensaje(email, id, mensaje.getText().toString());

            myRef.child(id).setValue(msg);
        }
    }

    private void crearInteresFirebase() {
        myRef = db.getReference().child("Intereses");

        Interes interes = new Interes(email, key);

        myRef.child(id).setValue(interes);
    }

    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = null;
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            anim.start();
        }
        mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
    }
}
