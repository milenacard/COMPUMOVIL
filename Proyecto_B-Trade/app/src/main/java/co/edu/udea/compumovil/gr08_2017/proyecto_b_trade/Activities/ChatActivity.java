package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Mensaje;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMensajes;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refMensajes;

    String chatID;
    String email;
    String interesado;

    EditText texto;
    ImageView enviarMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.setTitle("Chat");

        chatID = getIntent().getStringExtra("chat");
        email = getIntent().getStringExtra("email");
        interesado = getIntent().getStringExtra("interesado");

        texto = (EditText) findViewById(R.id.mensaje_ET);
        enviarMensaje = (ImageView) findViewById(R.id.enviar_mensaje);

        recyclerViewMensajes = (RecyclerView) findViewById(R.id.lista_mensajes);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewMensajes.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewMensajes.setHasFixedSize(true);

        refMensajes = db.getReference().child("Mensajes");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>
                (Mensaje.class, R.layout.mensaje_item_card, MensajeViewHolder.class, refMensajes.orderByChild("chat").equalTo(chatID)) {
            @Override
            protected void populateViewHolder(final MensajeViewHolder viewHolder, final Mensaje mensaje, int position) {
                viewHolder.textoMensaje.setText(mensaje.getTexto());

                DatabaseReference myRef = db.getReference().child("Usuarios");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                            String userDB = (String) user.child("email").getValue();
                            if (userDB.equals(mensaje.getEmisor())) {
                                String urlFoto = (String) user.child("foto").getValue();
                                Glide.with(getApplicationContext()).load(urlFoto).apply(RequestOptions.fitCenterTransform()).into(viewHolder.fotoMensaje);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        };
        recyclerViewMensajes.setAdapter(firebaseRecyclerAdapter);


        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = texto.getText().toString();
                if (!TextUtils.isEmpty(msg)) {
                    Calendar cal = new GregorianCalendar();
                    Mensaje mensaje = new Mensaje(email, chatID, msg);

                    refMensajes.child(String.valueOf(cal.getTimeInMillis())).setValue(mensaje);

                    texto.setText("");
                }
            }
        });

    }


    public static class MensajeViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView fotoMensaje;
        TextView textoMensaje;

        public MensajeViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.mensaje_card);
            fotoMensaje = (ImageView) itemView.findViewById(R.id.foto_mensaje);
            textoMensaje = (TextView) itemView.findViewById(R.id.texto_mensaje);
        }
    }
}
