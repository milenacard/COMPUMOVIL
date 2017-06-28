package co.edu.udea.compumovil.gr08_20171.Lab3.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class EventDetail extends AppCompatActivity {

    private ImageView photo_Detail;
    private TextView name_Detail;
    private RatingBar rating_Detail;
    private TextView responsible_Detail;
    private TextView date_Detail;
    private ImageButton ubication_Detail;
    private TextView gralInfo_Detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);
        Bundle extras = getIntent().getExtras();
        final Event thisEvent;

        name_Detail = (TextView) findViewById(R.id.detail_event_name);
        photo_Detail = (ImageView) findViewById(R.id.detail_event_photo);
        rating_Detail = (RatingBar) findViewById(R.id.detail_event_rating);
        responsible_Detail = (TextView) findViewById(R.id.detail_event_responsible);
        date_Detail = (TextView) findViewById(R.id.detail_event_date);
        ubication_Detail = (ImageButton) findViewById(R.id.detail_event_ubication);
        gralInfo_Detail = (TextView) findViewById(R.id.detail_event_generalinformation);

        if (extras != null) {
            thisEvent = (Event) extras.getSerializable("EVENT");
        } else {
            thisEvent = null;
        }
        ;
        if (thisEvent != null) {
            //Toast.makeText(getBaseContext(), "Encontro el evento:".concat(thisEvent.getName()).concat("\n Con la descripcion: ".concat(thisEvent.getDescription())), Toast.LENGTH_SHORT).show();
            this.setTitle(thisEvent.getName());

            String foto = thisEvent.getPhoto();
            if (!foto.equals("") && foto.charAt(0) == 'R') {
                int resourceId = getResources().getIdentifier(foto.substring(11), "drawable", getPackageName());
                Picasso.with(photo_Detail.getContext()).load(resourceId).fit().centerCrop().into(photo_Detail);

            } else {
                Uri path = Uri.fromFile(new File(foto));
                Picasso.with(photo_Detail.getContext()).load(path).fit().centerCrop().into(photo_Detail);
            }

            name_Detail.setText(thisEvent.getName());
            rating_Detail.setRating(thisEvent.getScore());
            responsible_Detail.setText(thisEvent.getResponsible());
            //Log.d("responsable_Detail",thisEvent.getResponsible() );
            date_Detail.setText(thisEvent.getDate());
            //ubication_Detail
            gralInfo_Detail.setText(thisEvent.getGeneralInformation());
        }

        ubication_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = ubication_Detail.getContext();
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", thisEvent.getLatitude(), thisEvent.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });


    }

}