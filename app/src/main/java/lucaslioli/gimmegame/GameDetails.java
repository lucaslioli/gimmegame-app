package lucaslioli.gimmegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameDetails extends Activity {

    private String nomeJogo = null;
    private Integer isFavorite = 0, idJogo = 0;
    private FloatingActionButton btnFavorite;

    private SQLiteDatabase database;

    private static final String PREFERENCES_FILE = "GimmeGamePreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // Atribui às variáveis, os elementos da view
        TextView toolbarTitle = findViewById(R.id.toolbarTitleId);

        TextView tvNJogadores = findViewById(R.id.textNJogadoresId);
        TextView tvFxEtaria = findViewById(R.id.textFxEtariaId);
        TextView tvCategoria = findViewById(R.id.textCategoriaId);
        TextView tvMaterial = findViewById(R.id.textMaterialId);
        TextView tvDescricao = findViewById(R.id.textDescricaoId);

        btnFavorite = findViewById(R.id.btnFavorite);

        // Abre o banco de dados
        database = openOrCreateDatabase("gimmegame", MODE_PRIVATE, null);

        // Guarda em extra os valores extras passados pela intent
        Bundle extra = getIntent().getExtras();

        // Cria variável de cursor para pegar os dados do banco
        Cursor cursor;

        // Verifica se extra não é null e guarda os valores em string para exibição
        if(extra != null) {
            nomeJogo = extra.getString("nomeJogo");
        }

        if(nomeJogo != null)
            cursor = database.rawQuery("SELECT * FROM jogo WHERE nome = '" + nomeJogo + "'", null);
        else
            cursor = database.rawQuery("SELECT * FROM jogo ORDER BY RANDOM() ", null);

        cursor.moveToFirst();

        // Guardo o nome do jogo e dado sobre favorito
        nomeJogo = cursor.getString(cursor.getColumnIndex("nome"));
        idJogo = cursor.getInt(cursor.getColumnIndex("id"));
        isFavorite = cursor.getInt(cursor.getColumnIndex("favorito"));

        // Atualiza toolbar com nome do jogo
        toolbarTitle.setText(nomeJogo);

        // Verifica o máximo para evitar o null
        String sAux = cursor.getString(cursor.getColumnIndex("max_jogadores"));
        sAux = (sAux == null)?"livre":sAux;

        // Atualiza informações que estão na tabela do jogo
        String sNJogadores = cursor.getString(cursor.getColumnIndex("min_jogadores")) +
                " .. " + sAux;
        tvNJogadores.setText(sNJogadores);

        // Verifica o máximo para evitar o null
        sAux = cursor.getString(cursor.getColumnIndex("max_idade"));
        sAux = (sAux == null)?"livre":sAux;

        String sFxEtaria = cursor.getString(cursor.getColumnIndex("min_idade")) +
                " .. " + sAux;
        tvFxEtaria.setText(sFxEtaria);

        // Solução alternativa para inserir espaçamento ao final, pois parte do texto era cortado
        sAux = cursor.getString(cursor.getColumnIndex("descricao")) + "\n\n\n\n";
        tvDescricao.setText(sAux);

        // Busca por todas as categorias do jogo
        cursor = database.rawQuery("SELECT group_concat(c.nome, '\n ') FROM categoria AS c " +
                "JOIN jogo_categoria AS j ON c.id = j.id_categoria " +
                "WHERE j.id_jogo = "+idJogo, null);
        cursor.moveToFirst();

        sAux = cursor.getString(0);
        sAux = (sAux == null || sAux == "")?"Outro":sAux;
        tvCategoria.setText(sAux);

        // Busca por todos os materiais do jogo
        cursor = database.rawQuery("SELECT group_concat(m.nome, '\n ') FROM material AS m " +
                "JOIN jogo_material AS j ON m.id = j.id_material " +
                "WHERE j.id_jogo = "+idJogo, null);
        cursor.moveToFirst();

        sAux = cursor.getString(0);
        sAux = (sAux == null || sAux == "")?"nenhum material necessário":sAux;
        tvMaterial.setText(sAux);

        cursor.close();

        // Verifica se o jogo está salvo nas SharedPreferences pra modificar o ícone
        if(isFavorite == 1){
            btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_starred));
        }else{
            btnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
        }

        // Onclick para adicionar ou remover dos favoritos
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    isFavorite = (isFavorite == 1)?0:1;

                    database.execSQL("UPDATE jogo SET favorito = " + isFavorite + " WHERE id = " + idJogo);

                    if (isFavorite == 1) {
                        Toast.makeText(getApplicationContext(), "Jogo adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                        btnFavorite.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_starred));

                    } else {
                        Toast.makeText(getApplicationContext(), "Jogo removido dos favoritos!", Toast.LENGTH_SHORT).show();
                        btnFavorite.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_star));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
