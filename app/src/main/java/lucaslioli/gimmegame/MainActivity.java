package lucaslioli.gimmegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button botaoBuscar;
    private TextView bemVindo;
    private TextView perg1, perg2, perg3, perg4, perg5;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Random rand= new Random();
                    bemVindo.setText(getResources().getStringArray(R.array.bem_vindo)[rand.nextInt(3)]);
                    return true;
                case R.id.navigation_games:
                    bemVindo.setText(R.string.title_games);
                    return true;
                case R.id.navigation_favorites:
                    bemVindo.setText(R.string.title_favorites);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random rand= new Random();

        botaoBuscar = findViewById(R.id.btnBuscarId);
        bemVindo = findViewById(R.id.bemVindoId);

        perg1 = findViewById(R.id.txtPergunta1);
        perg2 = findViewById(R.id.txtPergunta2);
        perg3 = findViewById(R.id.txtPergunta3);
        perg4 = findViewById(R.id.txtPergunta4);
        perg5 = findViewById(R.id.txtPergunta5);

        bemVindo.setText(getResources().getStringArray(R.array.bem_vindo)[rand.nextInt(3)]);

        perg1.setText(getResources().getStringArray(R.array.pergunta1)[rand.nextInt(3)]);
        perg2.setText(getResources().getStringArray(R.array.pergunta2)[rand.nextInt(3)]);
        perg3.setText(getResources().getStringArray(R.array.pergunta3)[rand.nextInt(3)]);
        perg4.setText(getResources().getStringArray(R.array.pergunta4)[rand.nextInt(3)]);
        perg5.setText(getResources().getStringArray(R.array.pergunta5)[rand.nextInt(3)]);

        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameDetails.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
