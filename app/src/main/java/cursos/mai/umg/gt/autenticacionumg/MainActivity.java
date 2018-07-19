package cursos.mai.umg.gt.autenticacionumg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LoginBasic(View view) {
        Intent principal;
        principal = new Intent(this, PrincipalActivity.class);
        startActivity(principal);
    }
}
