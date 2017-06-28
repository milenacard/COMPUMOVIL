package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments;

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
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.IllegalFormatCodePointException;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.DetalleLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.MiLibroActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Token;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLibreriaPublica.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentLibreriaPublica extends Fragment {

    private RecyclerView recyclerViewLibros;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    public DatabaseReference refLibros;

    private OnFragmentInteractionListener mListener;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private String email;

    public boolean completado;

    public FragmentLibreriaPublica() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Librería Pública");
        View view = inflater.inflate(R.layout.fragment_libreria_publica, container, false);

        recyclerViewLibros = (RecyclerView) view.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLibros.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewLibros.setHasFixedSize(true);

        email = getArguments().getString("email");

        refLibros = db.getReference().child("Libros");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Libro, BookViewHolder>
                (Libro.class, R.layout.book_item_card, BookViewHolder.class, refLibros) {
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

                        if(!libro.getPropietario().equals(email)) {

                            final Intent infoActivity = new Intent(getContext(), DetalleLibroActivity.class);

                            int color = Color.BLACK;
                            Drawable background = viewHolder.nombreLibroTV.getBackground();
                            if (background instanceof ColorDrawable) {
                                color = ((ColorDrawable) background).getColor();
                            }
                            infoActivity.putExtra("color fondo", color);
                            System.out.println(color);


                            infoActivity.putExtra("email", email);
                            infoActivity.putExtra("key", firebaseRecyclerAdapter.getRef(position).getKey());

                            infoActivity.putExtra("libro", libro);

                            //Se revisa si ya se marcó el libro como interés
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

                                    if (estaInteresado[0]){
                                        System.out.println("ESTA INTERESADO: "+ estaInteresado[0]);
                                        infoActivity.putExtra("interes", estaInteresado[0]);
                                        startActivity(infoActivity);
                                    } else {

                                        System.out.println("ESTA INTERESADO: "+ estaInteresado[0]);
                                        startActivity(infoActivity);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {

                            Intent infoActivity = new Intent(getContext(), MiLibroActivity.class);

                            infoActivity.putExtra("libroKey", getRef(position).getKey());

                            int color = Color.BLACK;
                            Drawable background = viewHolder.nombreLibroTV.getBackground();
                            if (background instanceof ColorDrawable){
                                color = ((ColorDrawable)background).getColor();
                            }
                            infoActivity.putExtra("color fondo", color);

                            infoActivity.putExtra("email", email);
                            infoActivity.putExtra("libro", libro);
                            startActivity(infoActivity);
                        }
                    }
                });
            }

        };
        recyclerViewLibros.setAdapter(firebaseRecyclerAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
