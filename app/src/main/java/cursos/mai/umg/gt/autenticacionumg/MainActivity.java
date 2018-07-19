package cursos.mai.umg.gt.autenticacionumg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            guardarFacebook();
        }
        loginFacebook();
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

    public void LoginBasic(View view) {
        mostrarPrincipal();
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
}
