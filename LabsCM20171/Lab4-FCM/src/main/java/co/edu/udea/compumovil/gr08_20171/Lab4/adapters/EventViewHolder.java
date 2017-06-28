package co.edu.udea.compumovil.gr08_20171.Lab4.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;

/**
 * Created by CristianCamilo on 13/03/2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {



     public CardView cardView;
    public ImageView eventPhoto;
    public TextView eventName;
    public RatingBar eventRating;
    public TextView eventDescription;



    EventViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        eventPhoto = (ImageView) itemView.findViewById(R.id.event_photo);
        eventName = (TextView) itemView.findViewById(R.id.event_name);
        eventRating = (RatingBar) itemView.findViewById(R.id.event_rating);
        eventDescription = (TextView) itemView.findViewById(R.id.event_description);
    }


}
