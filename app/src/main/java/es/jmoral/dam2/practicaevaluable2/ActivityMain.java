package es.jmoral.dam2.practicaevaluable2;

/**
 * Esta clase contiene el FragmentoMenu cuando se visualiza desde un móvil y FragmentoMenu y
 * FragmentoDetalle desde tablet. En el primer caso sirve de puente para enviar qué botón se ha
 * pulsado desde el fragmento para enviar el dato a la actividad ActivitySecond. En el caso de
 * la tablet sirve de puente entre los dos fragmentos ya que no se carga la segunda actividad.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ActivityMain extends AppCompatActivity implements FragmentoMenu.FragmentoMenuListener, FragmentoDetalle.FragmentoDetalleListener {

    // constantes definidas para usar como claves
    public static final String KEY_NOMBRE = "KEY_NOMBRE";
    public static final String KEY_EDAD = "KEY_EDAD";
    public static final String KEY_TLF = "KEY_TLF";
    public static final String KEY_ETIQUETA = "KEY_ETIQUETA";

    // constante para el requestCode
    public static final int REQUEST_CODE = 1234;

    // constantes definidas para controlar que botón se ha pulsado
    public static final String DESDE_NOMBRE = "DESDE_NOMBRE";
    public static final String DESDE_EDAD = "DESDE_EDAD";
    public static final String DESDE_TLF = "DESDE_TLF";

    // variables que contienen los fragmentos
    private FragmentoMenu fragmentoMenu;
    private FragmentoDetalle fragmentoDetalle;

    // variable que guarda si está ejecutándose en una tablet o móvil
    private boolean isTablet;

    // método que define los elementos que aparecerán en pantalla a la hora de crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicializamos los fragmentos
        fragmentoMenu = (FragmentoMenu) getFragmentManager().findFragmentById(R.id.fragmentMenu);
        fragmentoDetalle = (FragmentoDetalle) getFragmentManager().findFragmentById(R.id.fragmentDetalle);

        // listener del fragmento
        fragmentoMenu.setFragmentoMenuListener(this);

        // guardamos si el FragmentoDetalle está inicializado en un boolean
        isTablet = fragmentoDetalle != null;

        // si es true (es tablet), seteamos el listener
        if(isTablet)
            fragmentoDetalle.setFragmentoDetalleListener(this);
    }

    // método que recibe los datos de la actividad secundaria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // comprobamos si hemos recibido los datos correctamente
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            // para cada clave comprobamos que el intent contiene ese extra para mostrarlo
            if(data.hasExtra(KEY_NOMBRE))

                // para cada clave llamamos al método implementado del FragmentoMenu para poner el valor
                fragmentoMenu.ponNombre(data.getStringExtra(KEY_NOMBRE));
            if(data.hasExtra(KEY_EDAD))
                fragmentoMenu.ponEdad(data.getIntExtra(KEY_EDAD, -1));
            if(data.hasExtra(KEY_TLF))
                fragmentoMenu.ponTelefono(data.getIntExtra(KEY_TLF, -1));

        // si el usuario apreta el boton back devolveremos un Toast avisándole
        } else if(requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, R.string.usuario_back, Toast.LENGTH_SHORT).show();
        }
    }

    // método que llamaremos a la hora de mandar los datos a la segunda actividad para decir qué
    // botón hemos pulsado
    private Intent getIntentToActivitySecond(String botonPulsado) {
        Intent intent = new Intent(this, ActivitySecond.class);
        intent.putExtra(KEY_ETIQUETA, botonPulsado);
        return intent;
    }

    /*
     * los siguientes 3 métodos sirven para llamar a la segunda actividad según que botón pulsemos
     * en el FragmentoMenu
     */
    @Override
    public void onPideNombre() {
        // si es tablet llamamos al método que pone el nombre de la etiqueta y ademas activamos
        // boton y el EditText
        if(isTablet) {
            fragmentoDetalle.ponEtiqueta(DESDE_NOMBRE);
            fragmentoDetalle.setEnabled(true);

        // si no es tablet mandamos los datos a la segunda actividad
        } else {
            startActivityForResult(getIntentToActivitySecond(DESDE_NOMBRE), REQUEST_CODE);
        }
    }

    @Override
    public void onPideEdad() {
        if(isTablet) {
            fragmentoDetalle.ponEtiqueta(DESDE_EDAD);
            fragmentoDetalle.setEnabled(true);
        } else {
            startActivityForResult(getIntentToActivitySecond(DESDE_EDAD), REQUEST_CODE);
        }
    }

    @Override
    public void onPideTelefono() {
        if(isTablet) {
            fragmentoDetalle.ponEtiqueta(DESDE_TLF);
            fragmentoDetalle.setEnabled(true);
        } else {
            startActivityForResult(getIntentToActivitySecond(DESDE_TLF), REQUEST_CODE);
        }
    }

    /*
     * Los siguientes 3 métodos definen el comportamiento de ellos cuando sean llamados, pasando
     * como parámetro el valor que se colocará en el EditText
     */
    @Override
    public void onNombreIntroducido(String nombre) {
        fragmentoMenu.ponNombre(nombre);
    }

    @Override
    public void onEdadIntroducido(int edad) {
        fragmentoMenu.ponEdad(edad);
    }

    @Override
    public void onTelefonoIntroducido(int telefono) {
        fragmentoMenu.ponTelefono(telefono);
    }
}
