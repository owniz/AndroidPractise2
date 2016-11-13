package es.jmoral.dam2.practicaevaluable2;

/**
 * Esta clase contiene el FragmentoDetalle cuando se visualiza desde un móvil. Cuando es una tablet
 * no la utilizamos ya que los dos fragmentos se pintan en la actividad principal.
 */

    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;

    public class ActivitySecond extends AppCompatActivity implements FragmentoDetalle.FragmentoDetalleListener {

        // declaramos el fragmento
        private FragmentoDetalle fragmentoDetalle;

        // método que define los elementos que aparecerán en pantalla a la hora de crear la actividad
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_second);

            // declaramos el fragmento
            fragmentoDetalle = (FragmentoDetalle) getFragmentManager().findFragmentById(R.id.fragmentDetalle);

            // listener del fragmento
            fragmentoDetalle.setFragmentoDetalleListener(this);

            // llamamos al método que rellena la etiqueta
            setEtiqueta(getIntent().getStringExtra(ActivityMain.KEY_ETIQUETA));
        }

        // método que pinta la etiqueta según el botón que se pulsó en el FragmentoMenu
        public void setEtiqueta(String botonPulsado) {
            fragmentoDetalle.ponEtiqueta(botonPulsado);
        }

        /*
         * Los 3 siguientes métodos indican qué tipo de dato se está enviando desde el EditText
         * (llaman a otro método que se encarga de enviar el Intent)
         */
        @Override
        public void onNombreIntroducido(String nombre) {
            sendIntent(ActivityMain.KEY_NOMBRE, nombre);
        }

        @Override
        public void onEdadIntroducido(int edad) {
            sendIntent(ActivityMain.KEY_EDAD, edad);
        }

        @Override
        public void onTelefonoIntroducido(int telefono) {
            sendIntent(ActivityMain.KEY_TLF, telefono);
        }

        // método que envía el dato introducido del EditText
        public void sendIntent(String clave, Object dato) {
            Intent intent = new Intent(this, ActivitySecond.class);

            // comprobamos si es un String o un int para indicar qué tipo de valor enviamos
            if(dato instanceof String)
                intent.putExtra(clave, (String) dato);
            else
                intent.putExtra(clave, (int) dato);

            setResult(RESULT_OK, intent);
            finish();
        }
}
