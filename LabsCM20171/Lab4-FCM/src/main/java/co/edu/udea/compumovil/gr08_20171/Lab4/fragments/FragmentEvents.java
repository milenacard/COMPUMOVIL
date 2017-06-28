package co.edu.udea.compumovil.gr08_20171.Lab4.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab4.activities.EventDetail;
import co.edu.udea.compumovil.gr08_20171.Lab4.adapters.AdapterEvents;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;

public class FragmentEvents extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    public RecyclerView recyclerViewEvents;
    public List<Event> eventList;
    public DbHelper dbHelper;
    public StorageReference storageRef;
    public FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerViewEvents = (RecyclerView) rootView.findViewById(R.id.recycler_view_events);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        recyclerViewEvents.setLayoutManager(llm);
        recyclerViewEvents.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference().child("Eventos");

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReferenceFromUrl("gs://lab4fcm-5259d.appspot.com");


        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>
                (Event.class, R.layout.card_event, EventViewHolder.class, myRef){
            @Override
            protected void populateViewHolder(final EventViewHolder holder, final Event event, final int position) {
                holder.eventName.setText(event.getName());
                holder.eventDescription.setHint(event.getDescription());
                //Log.d("valort",events.get(position).getDescription());
                holder.eventRating.setRating(event.getScore());

                Log.d("Foto", storageRef.child(event.getPhoto()).toString());

                storageRef.child(event.getPhoto()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Foto", "Va a procesar la foto");
                        Picasso.with(getContext()).load(uri.toString()).fit().centerCrop().into(holder.eventPhoto);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Foto", "Hubo un error con la foto");
                        e.printStackTrace();
                    }
                });




                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, "Click on element number : " + position, Toast.LENGTH_SHORT).show();
                        Intent infoActivity = new Intent(getContext(), EventDetail.class);
                        Bundle b = new Bundle();
                        b.putParcelable("EVENT", event);
                        infoActivity.putExtras(b);
                        getContext().startActivity(infoActivity);

                    }
                });
            }

        };

        recyclerViewEvents.setAdapter(firebaseRecyclerAdapter);


        return rootView;
    }


    public void initializeData() {
        eventList = dbHelper.getAllEvents();
    }

    public void initializeAdapter() {
        AdapterEvents adapter = new AdapterEvents(eventList);
        recyclerViewEvents.setAdapter(adapter);
    }

    public void update() {
        initializeData();
        initializeAdapter();
    }

    public class rvAdapter{
        void update(){
            FragmentEvents.this.update();

        }

    };

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView eventPhoto;
        public TextView eventName;
        public RatingBar eventRating;
        public TextView eventDescription;

        public EventViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            eventPhoto = (ImageView) itemView.findViewById(R.id.event_photo);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventRating = (RatingBar) itemView.findViewById(R.id.event_rating);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
        }


    }
}
