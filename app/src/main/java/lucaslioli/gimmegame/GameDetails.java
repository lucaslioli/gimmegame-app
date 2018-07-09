package lucaslioli.gimmegame;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class GameDetails extends Activity {

    private TextView textDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        textDescricao = findViewById(R.id.textDescricao);

        Bundle extra = getIntent().getExtras();

        if(extra != null){
            String nomeJogo = extra.getString("nomeJogo");
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

            textDescricao.setText(dados);
        }

        // Toast.makeText(getApplicationContext(), "Jogo adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
    }
}
