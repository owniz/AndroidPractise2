package es.jmoral.dam2.practicaevaluable2;

/**
 * Esta clase sirve para crear y gestionar un fragmento, en él tenemos 3 botones que mandarán qué
 * tipo de valor quieren recibir y un EditText que mostrará ese valor una vez nos lo devuelvan
 */

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentoMenu extends Fragment implements  View.OnClickListener {

    // declaramos los botones
    private Button botonNombre;
    private Button botonEdad;
    private Button botonTelefono;

    // dclaramos el TextView
    private TextView tvDatos;

    // declaramos el listener para el fragemento
    private FragmentoMenuListener escuchador;

    // método que infla los elementos
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_menu, container, false);

        // instanciamos los botones
        botonNombre = (Button) v.findViewById(R.id.botonNombre);
        botonEdad = (Button) v.findViewById(R.id.botonEdad);
        botonTelefono = (Button) v.findViewById(R.id.botonTelefono);
        tvDatos = (TextView) v.findViewById(R.id.tvDatos);

        // listeners de los botones
        botonNombre.setOnClickListener(this);
        botonEdad.setOnClickListener(this);
        botonTelefono.setOnClickListener(this);

        // si se ha guardado algún valor cuando se giró la pantalla lo volvemos a pintar
        if(saveInstanceState != null) {
            tvDatos.setText(saveInstanceState.getString(ActivityMain.KEY_ETIQUETA));
        }

        return v;
    }

    // llamamos al listener cada vez que pulsamos un botón
    @Override
    public void onClick(View view) {
        if(botonNombre.getId() == view.getId())
            escuchador.onPideNombre();

        if(botonEdad.getId() == view.getId())
            escuchador.onPideEdad();

        if(botonTelefono.getId() == view.getId())
            escuchador.onPideTelefono();
    }

    // definimos los métodos que necesitarán cuando implementen la interfaz
    public interface FragmentoMenuListener {
        void onPideNombre();
        void onPideEdad();
        void onPideTelefono();
    }

    // setea el listener
    public void setFragmentoMenuListener(FragmentoMenuListener escuchador) {
        this.escuchador = escuchador;
    }

    /*
     * Los siguientes 3 métodos son los que llamará la actividad principal con su método onActivityResult
     * cuando rellene los datos en el TextView
     */
    @SuppressLint("SetTextI18n")
    public void ponNombre(String nombre) {
        tvDatos.setText(getString(R.string.nombre) + nombre);
    }

    @SuppressLint("SetTextI18n")
    public void ponEdad(int edad) {
        tvDatos.setText(getString(R.string.edad) + edad);
    }

    @SuppressLint("SetTextI18n")
    public void ponTelefono(int telefono) {
        tvDatos.setText(getString(R.string.telefono) + telefono);
    }

    // guardamos los datos que contiene la etiqueta para recuperarlos al girar la pantalla
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(ActivityMain.KEY_ETIQUETA, tvDatos.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    // utilizado para evitar fugas de memoria (memory leaks)
    @Override
    public void onDetach() {
        super.onDetach();
        escuchador = null;
    }
}
