package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr08_20171.Lab2.R;

/**
 * Created by CristianCamilo on 13/03/2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {



     CardView cardView;
     ImageView eventPhoto;
     TextView eventName;
     RatingBar eventRating;
     TextView eventDescription;


    EventViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        eventPhoto = (ImageView) itemView.findViewById(R.id.event_photo);
        eventName = (TextView) itemView.findViewById(R.id.event_name);
        eventRating = (RatingBar) itemView.findViewById(R.id.event_rating);
        eventDescription = (TextView) itemView.findViewById(R.id.event_description);
    }


}
