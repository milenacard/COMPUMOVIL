package co.edu.udea.compumovil.gr08_20171.Lab3.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;
import co.edu.udea.compumovil.gr08_20171.Lab3.activities.EventDetail;

/**
 * Created by CristianCamilo on 13/03/2017.
 */

public class AdapterEvents extends RecyclerView.Adapter<EventViewHolder> {

    private static List<Event> events;

    public AdapterEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {

        Event event = events.get(position);

        holder.eventName.setText(event.getName());
        holder.eventDescription.setHint(event.getDescription());
        //Log.d("valort",events.get(position).getDescription());
        holder.eventRating.setRating(event.getScore());

        final Context context = holder.eventPhoto.getContext();

        String foto = events.get(position).getPhoto();
        if (!foto.equals("") && foto.charAt(0) == 'R') {
            int resourceId = context.getResources().getIdentifier(foto.substring(11), "drawable", context.getPackageName());
            Picasso.with(context).load(resourceId).fit().centerCrop().into(holder.eventPhoto);
        } else {
            Uri path = Uri.fromFile(new File(foto));
            Picasso.with(context).load(path).fit().centerCrop().into(holder.eventPhoto);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Click on element number : " + position, Toast.LENGTH_SHORT).show();
                Intent infoActivity = new Intent(context, EventDetail.class);
                Bundle b = new Bundle();
                b.putSerializable("EVENT", events.get(position));
                infoActivity.putExtras(b);
                context.startActivity(infoActivity);

            }
        });
    }

        @Override
        public int getItemCount () {
            return events.size();
        }


    public static Event getEvent(int position) {

        return events.get(position);
    }

    ;


}
