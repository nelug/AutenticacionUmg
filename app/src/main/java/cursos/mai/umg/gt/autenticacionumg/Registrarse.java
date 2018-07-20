package cursos.mai.umg.gt.autenticacionumg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Registrarse extends AppCompatActivity {

    EditText correo,nombre,apellido,password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        setTitle("Registra tus datos");
        session = new SessionManager(this);

        correo = (EditText) findViewById(R.id.txtCorreo);
        nombre = (EditText) findViewById(R.id.txtNombre);
        apellido = (EditText) findViewById(R.id.txtApellido);
        password = (EditText) findViewById(R.id.txtPassword);
    }

    public void GuardarDatos(View view){
        GuardarUsuario();
    }

    private void GuardarUsuario(){
    try
    {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", correo.getText()+ "");
        usuario.put("apellido", apellido.getText()+"");
        usuario.put("nombre", nombre.getText()+"");
        usuario.put("nombre_completo", nombre.getText() + " " + apellido.getText());
        usuario.put("red", "Firebase");
        usuario.put("token_cloud_message", FirebaseCloudMessage.getToken());
        usuario.put("password", md5(password.getText().toString()));


        db.collection("usuarios").document(correo.getText().toString())
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        session.createLoginSession(nombre.getText().toString(), correo.getText().toString() );
                        Toast.makeText(Registrarse.this, "Datos Guardados con exito", Toast.LENGTH_SHORT).show();
                        mostrarMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registrarse.this, "No se pudieron guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    catch (Exception ex)
    {

    }

    }

    private  void  mostrarMain(){
        Intent principal;
        principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}


