package co.edu.udea.compumovil.gr08_20171.Lab4.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class FragmentPerfil extends Fragment {

    private ImageView pPhotoUser;
    private TextView pNameUser;
    private TextView pEmail;
    private TextView pUsername;

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
        pEmail = (TextView) view.findViewById(R.id.perfil_email);
        pUsername = (TextView) view.findViewById(R.id.perfil_username);
        pAge = (TextView) view.findViewById(R.id.perfil_edad_usuario);

        Picasso.with(pPhotoUser.getContext()).load(getArguments().getString(DBInfomation.ColumnUser.PHOTO)).into(pPhotoUser);
       if ((getArguments().getString(DBInfomation.ColumnUser.NAME) != null)){
           pNameUser.setText("Nombre: " + getArguments().getString(DBInfomation.ColumnUser.NAME));
       }else{
           pNameUser.setText("Nombre: " + " ");
       }

       if ((getArguments().getString(DBInfomation.ColumnUser.USERNAME) != null)){
           pUsername.setText("Username: " + getArguments().getString(DBInfomation.ColumnUser.USERNAME));
       }else {
           pUsername.setText("Username: " + " ");
       }

        if ((getArguments().getString(DBInfomation.ColumnUser.EMAIL) != null)){
            pEmail.setText("Email: " + getArguments().getString(DBInfomation.ColumnUser.EMAIL));
        }else {
            pEmail.setText("Email: " + " ");
        }

        if ((getArguments().getString(DBInfomation.ColumnUser.AGE) != null)){
            pAge.setText("Edad: " + getArguments().getString(DBInfomation.ColumnUser.AGE));
        }else {
            pAge.setText("Edad: " + " ");
        }

        return  view;
    }

}
