package cursos.mai.umg.gt.autenticacionumg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.facebook.Profile;
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

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Menu Principal | MAESTRIA UMG");

        guardarFacebook();

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
