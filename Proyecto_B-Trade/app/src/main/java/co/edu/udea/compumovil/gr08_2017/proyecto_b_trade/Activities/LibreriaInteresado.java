package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class LibreriaInteresado extends AppCompatActivity {

    private RecyclerView recyclerViewLibros;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refLibros;

    String email;
    String interesado;
    String interesIniciador;

    boolean isListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libreria_interesado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Librería de " + getIntent().getExtras().getString("nombre"));

        recyclerViewLibros = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLibros.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewLibros.setHasFixedSize(true);

        isListview = true;
        refLibros = db.getReference().child("Libros");

        email = getIntent().getStringExtra("email");
        interesado = getIntent().getStringExtra("interesado");
        interesIniciador = getIntent().getStringExtra("interesIniciador");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Libro, BookViewHolder>
                (Libro.class, R.layout.book_item_card, BookViewHolder.class, refLibros.orderByChild("propietario").equalTo(interesado)) {
            @Override
            protected void populateViewHolder(final BookViewHolder viewHolder, final Libro libro, final int position) {
                viewHolder.nombreLibroTV.setText(libro.getNombre());

                Glide.with(getApplicationContext()).load(libro.getFoto()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        Palette.generateAsync(((BitmapDrawable) resource).getBitmap(), new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int bgColor/* = getResources().getColor(R.color.black)*/;
                                bgColor = palette.getMutedColor(getResources().getColor(R.color.black));
                                if (bgColor != getResources().getColor(R.color.black)) {
                                    Log.d(" COLOR NOMBRE", "Se usara el muted.");
                                } else {
                                    bgColor = palette.getVibrantColor(getResources().getColor(R.color.black));
                                    if (bgColor != getResources().getColor(R.color.black)) {
                                        Log.d(" COLOR NOMBRE", "Se usara el vibrant.");
                                    } else {
                                        bgColor = palette.getDominantColor(getResources().getColor(R.color.black));
                                        Log.d(" COLOR NOMBRE", "Se usara el dominant.");
                                    }
                                }

                                System.out.println(bgColor);
                                viewHolder.nombreLibroTV.setBackgroundColor(bgColor);
                            }
                        });
                        return false;
                    }
                }).apply(RequestOptions.fitCenterTransform()).into(viewHolder.imagenLibroIV);

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Intent infoActivity = new Intent(getApplicationContext(), DetalleLibroActivity.class);

                        infoActivity.putExtra("key", getRef(position).getKey());
                        int color = Color.BLACK;
                        Drawable background = viewHolder.nombreLibroTV.getBackground();
                        if (background instanceof ColorDrawable){
                            color = ((ColorDrawable)background).getColor();
                        }
                        infoActivity.putExtra("color fondo", color);

                        infoActivity.putExtra("libro", libro);
                        infoActivity.putExtra("email", email);
                        infoActivity.putExtra("intercambio", true);
                        infoActivity.putExtra("interesIniciador", interesIniciador);



                        final boolean[] estaInteresado = {false};
                        DatabaseReference refIntereses;
                        refIntereses = db.getReference().child("Intereses");
                        refIntereses.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    String emailDB = (String) singleSnapshot.child("email").getValue();
                                    String libroDB = (String) singleSnapshot.child("libro").getValue();

                                    if (libroDB.equals(getRef(position).getKey()) && emailDB.equals(email)){
                                        estaInteresado[0] = true;
                                    }
                                }
                                infoActivity.putExtra("interes", estaInteresado[0]);
                                startActivity(infoActivity);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }
        };

        recyclerViewLibros.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.btn_cambiar_modo) {
            isListview = cambiarModo(isListview, item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean cambiarModo(boolean isListView, MenuItem item) {

        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_view_agenda_black_24dp);
            item.setTitle("Show as list");
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isListView = true;
        }

        return isListView;

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imagenLibroIV;
        TextView nombreLibroTV;

        public BookViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.placeCard);
            imagenLibroIV = (ImageView) itemView.findViewById(R.id.cover_photo_card_view);
            nombreLibroTV = (TextView) itemView.findViewById(R.id.book_name_card_view);
        }
    }

}
