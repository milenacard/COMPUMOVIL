package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.sql.SQLOutput;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.ChatActivity;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Chat;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Libro;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChat extends Fragment {

    private RecyclerView recyclerViewChats;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    public DatabaseReference chatsRef;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private String email;

    public FragmentChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Chat");
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        email = getArguments().getString("email");

        recyclerViewChats = (RecyclerView) view.findViewById(R.id.lista_chats);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewChats.setLayoutManager(mStaggeredLayoutManager);
        recyclerViewChats.setHasFixedSize(true);

        chatsRef = db.getReference().child("Chats");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>

                (Chat.class, R.layout.chat_item_card, ChatViewHolder.class, chatsRef) {
            @Override
            protected void populateViewHolder(final ChatViewHolder viewHolder, final Chat chat, final int position) {


                System.out.println(email);
                System.out.println(chat.getIniciador());
                System.out.println(chat.getReceptor());
                if (chat.getInteresIniciador() != null) {
                    //Se buscaran los datos de los libros para ponerlos
                    DatabaseReference libroIniciadorRef = db.getReference().child("Libros").child(chat.getInteresIniciador());

                    libroIniciadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Libro libro = dataSnapshot.getValue(Libro.class);

                            if (email.equals(libro.getPropietario())) {
                                viewHolder.nombreMiLibro.setText(libro.getNombre());
                                Glide.with(getContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(viewHolder.imagenMiLibro);
                            } else {
                                viewHolder.nombreLibroInteres.setText(libro.getNombre());
                                Glide.with(getContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(viewHolder.imagenLibroInteres);

                                DatabaseReference usuariosRef = db.getReference().child("Usuarios");
                                usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            String emailDB = (String) singleSnapshot.child("email").getValue();
                                            if (emailDB.equals(libro.getPropietario())) {
                                                viewHolder.propietarioLibro.setText((String) singleSnapshot.child("nombre").getValue());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            DatabaseReference libroReceptorRef = db.getReference().child("Libros").child(chat.getInteresReceptor());
                            libroReceptorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Libro libro = dataSnapshot.getValue(Libro.class);
                                    if (email.equals(libro.getPropietario())) {
                                        viewHolder.nombreMiLibro.setText(libro.getNombre());
                                        Glide.with(getContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(viewHolder.imagenMiLibro);
                                    } else {
                                        viewHolder.nombreLibroInteres.setText(libro.getNombre());
                                        Glide.with(getContext()).load(libro.getFoto()).apply(RequestOptions.fitCenterTransform()).into(viewHolder.imagenLibroInteres);

                                        DatabaseReference usuariosRef = db.getReference().child("Usuarios");
                                        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                    String emailDB = (String) singleSnapshot.child("email").getValue();
                                                    if (emailDB.equals(libro.getPropietario())) {
                                                        viewHolder.propietarioLibro.setText((String) singleSnapshot.child("nombre").getValue());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);

                            intent.putExtra("chat", getRef(position).getKey());
                            intent.putExtra("email", email);
                            if (email.equals(chat.getIniciador())) {
                                intent.putExtra("interesado", chat.getReceptor());
                            } else {
                                intent.putExtra("interesado", chat.getIniciador());
                            }
                            startActivity(intent);

                        }
                    });
                }


                if (!chat.getIniciador().equals(email) && !chat.getReceptor().equals(email)) {
                    viewHolder.cardView.setVisibility(View.GONE);
                    System.out.println("No se muestra: " + getRef(position));
                }
            }
        };

        recyclerViewChats.setAdapter(firebaseRecyclerAdapter);
        return view;
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imagenLibroInteres;
        ImageView imagenMiLibro;
        TextView nombreLibroInteres;
        TextView nombreMiLibro;
        TextView propietarioLibro;

        public ChatViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.chat_card);
            imagenLibroInteres = (ImageView) itemView.findViewById(R.id.libro_de_interes);
            imagenMiLibro = (ImageView) itemView.findViewById(R.id.mi_libro);
            nombreLibroInteres = (TextView) itemView.findViewById(R.id.nombre_libro_de_interes);
            nombreMiLibro = (TextView) itemView.findViewById(R.id.nombre_mi_libro);
            propietarioLibro = (TextView) itemView.findViewById(R.id.propietario_libro);
        }
    }

}
