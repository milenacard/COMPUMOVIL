package co.edu.udea.compumovil.gr08_20171.Lab3.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class FragmentPerfil extends Fragment {

    private ImageView pPhotoUser;
    private TextView pNameUser;
    private TextView pUsername;
    private TextView pEmail;
    private TextView pAge;


    public FragmentPerfil() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Perfil");
        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);

        pPhotoUser = (ImageView) view.findViewById(R.id.perfil_photo_usuario);
        pNameUser = (TextView) view.findViewById(R.id.perfil_nombre_usuario);
        pUsername = (TextView) view.findViewById(R.id.perfil_username);
        pEmail = (TextView) view.findViewById(R.id.perfil_email);
        pAge = (TextView) view.findViewById(R.id.perfil_edad_usuario);

        //Picasso.with(pPhotoUser.getContext()).load(thisEvent.getPhoto()).into(photo_Detail);
        pNameUser.setText("Nombre: " + getArguments().getString(DBInfomation.ColumnUser.NAME));
        pUsername.setText("Username: " + getArguments().getString(DBInfomation.ColumnUser.USERNAME));
        pEmail.setText("Email: " + getArguments().getString(DBInfomation.ColumnUser.EMAIL));
        pAge.setText("Edad: " + getArguments().getString(DBInfomation.ColumnUser.AGE));
       // pPhotoUser.setText(getArguments().getString(DBInfomation.ColumnUser.PHOTO));
        return  view;
    }

}
