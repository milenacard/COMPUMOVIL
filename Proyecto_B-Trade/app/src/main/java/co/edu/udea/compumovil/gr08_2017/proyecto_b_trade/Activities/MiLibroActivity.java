package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Interes;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Usuario;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class MiLibroActivity extends AppCompatActivity {

    private LinearLayout nombreHolder;
    private ImageView imageView;
    private TextView nombreLibro;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView interesados;

    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refInteresados;

    public String email;
    public String key;

    Libro libro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_libro);


        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.btn_editar_mi_libro);
        nombreHolder = (LinearLayout) findViewById(R.id.nombre_mi_libro_holder);
        nombreLibro = (TextView) findViewById(R.id.nombre_mi_libro);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        interesados = (RecyclerView) findViewById(R.id.list_interesados);
        interesados.setLayoutManager(llm);

        imageView = (ImageView) findViewById(R.id.image_mi_libro);

        libro = (Libro) getIntent().getSerializableExtra("libro");
        email = getIntent().getStringExtra("email");
        Glide.with(getApplicationContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(imageView);


        int color = getIntent().getIntExtra("color fondo", 0);
        nombreLibro.setBackgroundColor(color);

        key = getIntent().getStringExtra("libroKey");

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditarLibroActivity.class);
                intent.putExtra("libroKey", key);
               // startActivity(intent);
            }
        });

        nombreLibro.setText(libro.getNombre());
        refInteresados = db.getReference().child("Intereses");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Interes, InteresadoHolder>
                (Interes.class, R.layout.interesado_item_card, InteresadoHolder.class, refInteresados.orderByChild("libro").equalTo(key)){
            @Override
            protected void populateViewHolder(final InteresadoHolder viewHolder, final Interes model, int position) {

                DatabaseReference myRef = db.getReference().child("Usuarios");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String emailDB = (String) singleSnapshot.child("email").getValue();
                            if (emailDB.equals(model.getEmail())){
                                viewHolder.nombreInteresado.setText((String)singleSnapshot.child("nombre").getValue());
                                Glide.with(getApplicationContext()).load((String)singleSnapshot.child("foto").getValue()).apply(RequestOptions.fitCenterTransform()).into(viewHolder.fotoInteresado);
                                //TODO:Agregar el mensaje
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*

                DatabaseReference chatsRef = db.getReference().child("Usuarios");

                System.out.println("Entro" + model);
                chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String emailDB = (String) singleSnapshot.child("email").getValue();
                            if (emailDB.equals(model)){
                                viewHolder.nombreInteresado.setText((String)singleSnapshot.child("nombre").getValue());
                                Picasso.with(getApplicationContext()).load((String)singleSnapshot.child("foto").getValue()).into(viewHolder.fotoInteresado);

                            }
                        }
                    }





                 */


                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentLibreriaInteresado = new Intent(getApplicationContext(), LibreriaInteresado.class);

                        intentLibreriaInteresado.putExtra("nombre", viewHolder.nombreInteresado.getText());
                        intentLibreriaInteresado.putExtra("email", email);
                        intentLibreriaInteresado.putExtra("interesIniciador", key);
                        intentLibreriaInteresado.putExtra("interesado", model.getEmail());

                        startActivity(intentLibreriaInteresado);
                    }
                });

            }
        };

        interesados.setAdapter(firebaseRecyclerAdapter);


    }

    public static class InteresadoHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fotoInteresado;
        TextView nombreInteresado;
        TextView mensaje;

        public InteresadoHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.interesado_card);
            fotoInteresado = (ImageView) itemView.findViewById(R.id.foto_interesado);
            nombreInteresado = (TextView) itemView.findViewById(R.id.nombre_interesado);
            mensaje = (TextView) itemView.findViewById(R.id.mensaje_interesado);

        }
    }
}
