package cursos.mai.umg.gt.autenticacionumg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManager session;
    EditText correoElectronico;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        correoElectronico = (EditText) findViewById(R.id.txtUsuario);
        password = (EditText) findViewById(R.id.txtPasswordMain);
        session = new SessionManager(this);

        if(session.isLoggedIn()){
            mostrarPrincipal();
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            guardarFacebook();
        }

        loginFacebook();
    }

    private void ComprobarRegistro(String correo){
        final DocumentReference docRef = db.collection("usuarios").document(correo.toString());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    showCredencialesInvalidas();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String pass = (String) snapshot.getData().get("password");

                    if(pass.equals(md5(password.getText().toString()))){
                        mostrarPrincipal();
                    }else {
                        showCredencialesInvalidas();
                    }
                } else {
                    showCredencialesInvalidas();
                }
            }
        });
    }

    private  void showCredencialesInvalidas(){
        Toast.makeText(this, "Credenciales Invalidas", Toast.LENGTH_SHORT).show();
    }

    private  void loginFacebook() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                guardarFacebook();
            }
            public void onCancel() { }

            @Override
            public void onError(FacebookException exception) {  }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void mostrarPrincipal(){
        Intent principal;
        principal = new Intent(this, PrincipalActivity.class);
        startActivity(principal);
    }

    private  void mostrarRegistrarse(){
        Intent registro;
        registro = new Intent(this, Registrarse.class);
        startActivity(registro);
    }


    public void LoginBasic(View view) {
        ComprobarRegistro(correoElectronico.getText().toString());
    }

    public void Registrarse(View view){
        mostrarRegistrarse();
    }


    private void  guardarFacebook(){
        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            Map<String, Object> usuario = new HashMap<>();

            usuario.put("id", profile.getId());
            usuario.put("apellido", profile.getLastName());
            usuario.put("nombre", profile.getFirstName());
            usuario.put("nombre_completo", profile.getName());
            usuario.put("red", "Facebook");
            usuario.put("token_cloud_message", FirebaseCloudMessage.getToken());


            db.collection("usuarios").document("fb" + profile.getId())
                    .set(usuario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }

        mostrarPrincipal();
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
