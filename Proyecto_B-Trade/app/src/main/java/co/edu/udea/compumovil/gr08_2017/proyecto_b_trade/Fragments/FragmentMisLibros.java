package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.AgregarLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.DetalleLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.MiLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMisLibros extends Fragment {

    private RecyclerView recyclerViewLibros;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refLibros;

    String propietario;

    public boolean completado;


    public FragmentMisLibros() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Mis libros");
        View view = inflater.inflate(R.layout.fragment_mis_libros, container, false);

        recyclerViewLibros = (RecyclerView) view.findViewById(R.id.list_mis_libros);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLibros.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewLibros.setHasFixedSize(true);

        refLibros = db.getReference().child("Libros");

        propietario = getArguments().getString("email");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Libro, BookViewHolder>
                (Libro.class, R.layout.book_item_card, BookViewHolder.class, refLibros.orderByChild("propietario").equalTo(propietario)) {
            @Override
            protected void populateViewHolder(final BookViewHolder viewHolder, final Libro libro, final int position) {
                viewHolder.nombreLibroTV.setText(libro.getNombre());

                Glide.with(getContext()).load(libro.getFoto()).listener(new RequestListener<Drawable>() {
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

                        Intent infoActivity = new Intent(getContext(), MiLibroActivity.class);

                        infoActivity.putExtra("libroKey", getRef(position).getKey());

                        int color = Color.BLACK;
                        Drawable background = viewHolder.nombreLibroTV.getBackground();
                        if (background instanceof ColorDrawable){
                            color = ((ColorDrawable)background).getColor();
                        }
                        infoActivity.putExtra("color fondo", color);

                        infoActivity.putExtra("libro", libro);
                        infoActivity.putExtra("email", propietario);
                        startActivity(infoActivity);

                    }
                });
            }

        };
        recyclerViewLibros.setAdapter(firebaseRecyclerAdapter);

        FloatingActionButton agregarLibroFBtn = (FloatingActionButton) view.findViewById(R.id.agregar_libro);
        agregarLibroFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AgregarLibroActivity.class);
                intent.putExtra("propietario", propietario);
                startActivity(intent);
            }
        });


        return view;
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
}
