package cursos.mai.umg.gt.autenticacionumg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> userList = new ArrayList<>();
    SessionManager session;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Menu Principal | MAESTRIA UMG");
        session = new SessionManager(this);
        guardarFacebook();

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://appmoviles-89341.firebaseapp.com");
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.layout.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            session.logoutUser();
            LoginManager.getInstance().logOut();
        }

        return super.onOptionsItemSelected(item);
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
    }
}
