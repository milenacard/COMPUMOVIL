package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.DetalleLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.MiLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Interes;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;


public class FragmentLibrosDeInteres extends Fragment {

    private RecyclerView recyclerViewLibros;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refIntereses;

    boolean completado;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    public FragmentLibrosDeInteres() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Libros de Interés");
        View view = inflater.inflate(R.layout.fragment_libros_de_interes, container, false);


        recyclerViewLibros = (RecyclerView) view.findViewById(R.id.list_libros_interes);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLibros.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewLibros.setHasFixedSize(true);

        refIntereses = db.getReference().child("Intereses");

        String correo = getArguments().getString("email");


        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Interes, BookViewHolder>
                (Interes.class, R.layout.book_item_card, BookViewHolder.class, refIntereses.orderByChild("email").equalTo(correo)){


            @Override
            protected void populateViewHolder(final BookViewHolder viewHolder, final Interes model, final int position) {
                DatabaseReference myRef = db.getReference().child("Libros");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String libroKey = singleSnapshot.getKey();
                            if (libroKey.equals(model.getLibro())){
                                viewHolder.nombreLibroTV.setText((String)singleSnapshot.child("nombre").getValue());

                                Glide.with(getContext()).load((String)singleSnapshot.child("foto").getValue()).listener(new RequestListener<Drawable>() {
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


                                final Libro libro = new Libro();
                                libro.setFoto((String)singleSnapshot.child("foto").getValue());
                                libro.setNombre((String)singleSnapshot.child("nombre").getValue());
                                libro.setAutor((String)singleSnapshot.child("autor").getValue());
                                libro.setEstado(Float.valueOf(String.valueOf(singleSnapshot.child("estado").getValue())));
                                libro.setGenero((String) singleSnapshot.child("genero").getValue());
                                libro.setIdioma((String) singleSnapshot.child("idioma").getValue());
                                libro.setPaginas((String) singleSnapshot.child("paginas").getValue());
                                libro.setSinopsis((String) singleSnapshot.child("sinopsis").getValue());
                                libro.setPropietario((String) singleSnapshot.child("propietario").getValue());

                                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent infoActivity = new Intent(getContext(), DetalleLibroActivity.class);

                                        infoActivity.putExtra("libroKey", getRef(position).getKey());

                                        int color = Color.BLACK;
                                        Drawable background = viewHolder.nombreLibroTV.getBackground();
                                        if (background instanceof ColorDrawable){
                                            color = ((ColorDrawable)background).getColor();
                                        }
                                        infoActivity.putExtra("color fondo", color);

                                        infoActivity.putExtra("libro", libro);
                                        infoActivity.putExtra("interes", true);
                                        startActivity(infoActivity);

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerViewLibros.setAdapter(firebaseRecyclerAdapter);



        return view;
    }


    public void ponerFondoTexto(final BookViewHolder view) {
        final int[] bgColor = {0};
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                completado = false;
                while (!completado) {

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) view.imagenLibroIV.getDrawable();
                    if (bitmapDrawable != null) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();

                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                bgColor[0] = getResources().getColor(R.color.colorPrimary);
                                bgColor[0] = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                                if (bgColor[0] != getResources().getColor(R.color.colorPrimary)) {
                                    Log.d(" COLOR NOMBRE", "Se usara el muted.");
                                    view.nombreLibroTV.setBackgroundColor(bgColor[0]);
                                    completado = true;
                                } else {
                                    bgColor[0] = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                                    if (bgColor[0] != getResources().getColor(R.color.colorPrimary)) {
                                        Log.d(" COLOR NOMBRE", "Se usara el vibrant.");
                                        view.nombreLibroTV.setBackgroundColor(bgColor[0]);
                                        completado = true;
                                    } else {
                                        bgColor[0] = palette.getDominantColor(getResources().getColor(R.color.colorPrimary));
                                        Log.d(" COLOR NOMBRE", "Se usara el dominant.");
                                        view.nombreLibroTV.setBackgroundColor(bgColor[0]);
                                        completado = true;
                                    }
                                }
                                System.out.println("COLOR: "+ bgColor[0]);

                            }
                        });
                        completado = true;
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        hilo.start();
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
