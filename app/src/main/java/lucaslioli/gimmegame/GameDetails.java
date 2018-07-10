package lucaslioli.gimmegame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameDetails extends Activity {

    private String nomeJogo, isFavorite;
    private TextView toolbarTitle;
    private TextView textDescription;
    private FloatingActionButton btnFavorite;

    private static final String PREFERENCES_FILE = "GimmeGamePreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        toolbarTitle = findViewById(R.id.toolbarTitleId);
        textDescription = findViewById(R.id.textDescricao);
        btnFavorite = findViewById(R.id.btnFavorite);

        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeJogo = extra.getString("nomeJogo");
            String minJogadores = extra.getString("minJogadores");
            String maxJogadores = extra.getString("maxJogadores");
            String minIdade = extra.getString("minIdade");
            String maxIdade = extra.getString("maxIdade");
            String categoria = extra.getString("categoria");
            String material = extra.getString("material");
            String detalhes = extra.getString("detalhes");

            String dados = "Jogo: " + nomeJogo + "\n" +
                "Min. Jogadores: " + minJogadores + "\n" +
                "Max. Jogadores: " + maxJogadores + "\n" +
                "Min. Idade: " + minIdade  + "\n" +
                "Max. Idade: " + maxIdade  + "\n" +
                "Categoria: " + categoria  + "\n" +
                "Material: " + material  + "\n" +
                "Detalhes: " + detalhes;

            textDescription.setText(dados);

            if(nomeJogo != null){
                toolbarTitle.setText(nomeJogo);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0);
        if(sharedPreferences.contains(nomeJogo)){
            isFavorite = sharedPreferences.getString(nomeJogo, "not");

            if(isFavorite.equals("yes")){
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_starred));
            }else{
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
            }
        }else{
            isFavorite = "not";
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(isFavorite.equals("not")) {
                    editor.putString(nomeJogo, "yes");
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Jogo adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_starred));

                }else{
                    editor.remove(nomeJogo);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Jogo removido dos favoritos!", Toast.LENGTH_SHORT).show();
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_star));
                }
            }
        });
    }
}
