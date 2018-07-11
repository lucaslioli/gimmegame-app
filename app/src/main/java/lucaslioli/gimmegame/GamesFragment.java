package lucaslioli.gimmegame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

        // Busca e exibe os jogos na listView
        recuperarJogos();

        return view;
    }

    private void recuperarJogos(){
        try{
            ArrayAdapter<String> gamesAdapter;
            ArrayList<String> games = new ArrayList<String>();

            // Busca todas as tarefas
            Cursor cursor = database.rawQuery("SELECT * FROM jogo ORDER BY nome ASC", null);

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

}
