package lucaslioli.gimmegame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class GamesFragment extends Fragment {

    private ListView listGames;
    private SQLiteDatabase database;

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        getActivity().setTitle("GimmeGame - Lista de Jogos");

        // Abre o banco de dados
        database = getActivity().openOrCreateDatabase("gimmegame", MODE_PRIVATE, null);

        // Elemento listView que ira mostrar todos os jogos
        listGames = view.findViewById(R.id.listViewGames);

        // Guarda em args os parâmetros passados
        Bundle args = getArguments();

        // Busca e exibe os jogos na listView
        recuperarJogos(geraQuery(args));

        return view;
    }

    private void recuperarJogos(String query){
        try{
            ArrayAdapter<String> gamesAdapter;
            ArrayList<String> games = new ArrayList<String>();

            // Busca todas as tarefas
            Cursor cursor;

            if(query != null && !query.equals("")){
                cursor = database.rawQuery(query, null);
            }else{
                cursor = database.rawQuery("SELECT * FROM jogo ORDER BY nome ASC", null);
            }

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                games.add(cursor.getString(cursor.getColumnIndex("nome")));
                cursor.moveToNext();
            }

            cursor.close();

            // Criação do adapter
            gamesAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    games);

            listGames.setAdapter(gamesAdapter);

            listGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), GameDetails.class);

                    intent.putExtra("nomeJogo", listGames.getItemAtPosition(index).toString());

                    getActivity().startActivity(intent);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String geraQuery(Bundle args){
        String query = "SELECT j.* FROM jogo as j ";
        String where = " WHERE j.nome IS NOT NULL ";
        Boolean flag = false;

        if(args == null){
            return null;
        }

        String categoria = args.getString("categoria");
        String material = args.getString("material");

        String minJogadores = args.getString("maxJogadores");
        String maxJogadores = args.getString("maxJogadores");

        String minIdade = args.getString("maxIdade");

        String detalhes = args.getString("detalhes");

        if(!categoria.equals(getResources().getStringArray(R.array.respostas_categorias)[0])){
            query += " JOIN jogo_categoria AS jc ON j.id = jc.id_jogo " +
                     " JOIN categoria AS c ON jc.id_categoria = c.id ";
            where += " AND c.nome = '" + categoria + "' ";
            flag = true;
        }

        if(!material.equals(getResources().getStringArray(R.array.respostas_materiais)[0])){
            query += " JOIN jogo_material AS jm ON j.id = jm.id_jogo " +
                     " JOIN material AS m ON jm.id_material = m.id ";
            where += " AND m.nome = '" + material+ "' ";
            flag = true;
        }

        if(!minJogadores.equals(getResources().getStringArray(R.array.respostas_numericas)[0])){
            where += " AND j.min_jogadores >= " + minJogadores;

            flag = true;
        }

        if(!maxJogadores.equals(getResources().getStringArray(R.array.respostas_numericas)[0])){
            where += " AND j.max_jogadores <= " + minJogadores;

            flag = true;
        }

        if(!minIdade.equals(getResources().getStringArray(R.array.respostas_numericas)[0])){
            where += " AND j.min_idade <= " + minIdade;

            flag = true;
        }

        if(!detalhes.equals("") && !detalhes.isEmpty()){
            where += " AND j.descricao LIKE '%" + detalhes + "%' ";

            flag = true;
        }

        query += where + " ORDER BY nome ASC";

        if(!flag)
            return null;

        return query;
    }

}
