package es.jmoral.dam2.practicaevaluable2;

/**
 * Esta clase sirve para crear y gestionar un fragmento, en él tenemos una TextView que cambiará
 * según el valor que estemos pidiendo para indicarlo, un EditText para recoger el dato y un
 * botón que lo enviará. Este fragmento lo utilizamos para introducir el tipo de dato que nos pide
 * el FragmentoMenu.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentoDetalle extends Fragment implements View.OnClickListener {

    private static final String KEY_BOTON_PULSADO = "KEY_BOTON_PULSADO";

    // declaramos los componentes
    private TextView tvEtiqueta;
    private EditText etDato;
    private Button botonAceptar;

    // variable que almacena el tipo de valor que pedimos según la constante que hayamos introducido
    private String botonPulsado;

    // declaramos el listener para el fragmento
    private FragmentoDetalleListener escuchador;

    // método que infla los elementos
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_detalle, container, false);

        // inicializamos los elementos
        tvEtiqueta = (TextView) v.findViewById(R.id.tvEtiqueta);
        etDato = (EditText) v.findViewById(R.id.etDato);
        botonAceptar = (Button) v.findViewById(R.id.botonAceptar);

        // colocamos el focus en el EditText para que siempre se situe el cursor al puslar los botones
        etDato.requestFocus();

        // listener del botón
        botonAceptar.setOnClickListener(this);

        // si se ha guardado algún valor cuando se giró la pantalla lo volvemos a rellenar
        if(saveInstanceState != null) {
            botonPulsado = saveInstanceState.getString(KEY_BOTON_PULSADO);

            // comprobamos el estado para activar o no el EditText y el botón
            setEnabled(botonPulsado != null);

            // comprobamos si botonPulsado tiene algún valor para rellenar los datos
            if(botonPulsado != null) {
                setFormato(botonPulsado);
                tvEtiqueta.setText(saveInstanceState.getString(ActivityMain.KEY_ETIQUETA));
            }
        } else if(getActivity() instanceof ActivityMain){
            setEnabled(false);
        }

        return v;
    }

    // método que define el comportamiento al pulsar el botón aceptar
    @Override
        public void onClick(View view) {
            if(botonAceptar.getId() == view.getId()) {

                // si no hemos introducido ningún dato nos avisa a través de un Toast y salimos del
                // método para que no entre al switch
                if(etDato.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.introducir_nombre, Toast.LENGTH_SHORT).show();
                    return;
                }

                // con este condicional comprobamos desde qué botón ha sido llamado el fragmento para
                // definir su comportamiento
                switch(botonPulsado) {
                    case ActivityMain.DESDE_NOMBRE:
                        escuchador.onNombreIntroducido(etDato.getText().toString());
                        botonPulsado = null;
                        break;
                    case ActivityMain.DESDE_EDAD:
                        escuchador.onEdadIntroducido(Integer.valueOf(etDato.getText().toString()));
                        botonPulsado = null;
                        break;
                    case ActivityMain.DESDE_TLF:
                        int tlf;

                        // algunos teclados dejan introducir carácteres como * o () si tenemos definido
                        // el teclado como teléfono, por lo que al usar un int para recoger los datos
                        // la app se cierra con un una excepción, la tratamos con este try/catch
                        try {
                            tlf = Integer.valueOf(etDato.getText().toString());
                        } catch (NumberFormatException nfe) {
                            tlf = 0;
                        }

                        botonPulsado = null;
                        escuchador.onTelefonoIntroducido(tlf);
                        break;
                }

                // cuando pulsamos el botón aceptar:
                tvEtiqueta.setText(R.string.elija_opcion); // ponemos la etiqueta por defecto
                etDato.setText(""); // vaciamos el EditText
                setEnabled(false); // ponemos disabled el botón
            }
    }

    // definimos los métodos que necesitarán cuando implementen la interfaz
    public interface FragmentoDetalleListener {
        void onNombreIntroducido(String nombre);
        void onEdadIntroducido(int edad);
        void onTelefonoIntroducido(int telefono);
    }

    // setea el listener
    public void setFragmentoDetalleListener(FragmentoDetalleListener escuchador) {
        this.escuchador = escuchador;
    }

    // según el botón que se haya pulsado desde el FragmentoMenu ponenos una etiqueta u otra
    public void ponEtiqueta(String botonPulsado) {
        this.botonPulsado = botonPulsado;

        switch(botonPulsado) {
            case ActivityMain.DESDE_NOMBRE:
                setFormato(ActivityMain.DESDE_NOMBRE);
                tvEtiqueta.setText(getString(R.string.nombre));
                etDato.setText("");
                break;
            case ActivityMain.DESDE_EDAD:
                setFormato(ActivityMain.DESDE_EDAD);
                tvEtiqueta.setText(getString(R.string.edad));
                etDato.setText("");
                break;
            case ActivityMain.DESDE_TLF:
                setFormato(ActivityMain.DESDE_TLF);
                tvEtiqueta.setText(getString(R.string.telefono));
                etDato.setText("");
                break;
        }
    }

    // definimos el comportamiento del EditText según desde el botón que se haya pulsado
    public void setFormato(String formato) {
        if(formato.equals(ActivityMain.DESDE_NOMBRE))
            etDato.setInputType(InputType.TYPE_CLASS_TEXT);

        if(formato.equals(ActivityMain.DESDE_EDAD))
            etDato.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(formato.equals(ActivityMain.DESDE_TLF))
            etDato.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    // método para poner el EditText y el botón enabled o disabled
    public void setEnabled(boolean isEnabled) {
        etDato.setEnabled(isEnabled);
        botonAceptar.setEnabled(isEnabled);
    }

    // guardamos los datos que contiene la etiqueta para recuperarlos al girar la pantalla
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(ActivityMain.KEY_ETIQUETA, tvEtiqueta.getText().toString());
        savedInstanceState.putString(KEY_BOTON_PULSADO, botonPulsado);

        super.onSaveInstanceState(savedInstanceState);
    }

    // utilizado para evitar fugas de memoria (memory leaks)
    @Override
    public void onDetach() {
        super.onDetach();
        escuchador = null;
    }
}
