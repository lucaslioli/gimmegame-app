package lucaslioli.gimmegame;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FavoritesFragment extends Fragment {

    private TextView semFavoritos;
    private ListView listGames;
    private SQLiteDatabase database;

    private ArrayList<String> favoriteGames;

    private static final String PREFERENCES_FILE = "GimmeGamePreferences";

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        getActivity().setTitle("GimmeGame - Lista de Favoritos");

        // Abre o banco de dados
        database = getActivity().openOrCreateDatabase("gimmegame", MODE_PRIVATE, null);

        // Elemento listView que ira mostrar todos os jogos favoritos
        listGames = view.findViewById(R.id.listViewFavorites);

        // Elemento textView para exibir mensagem caso não tenha favoritos
        semFavoritos = view.findViewById(R.id.semFavoritosId);

        recuperarFavoritos();

        return view;
    }

    private void recuperarFavoritos(){
        try{
            ArrayAdapter<String> favoritesAdapter;
            favoriteGames = new ArrayList<String>();

            // Busca todas as tarefas
            Cursor cursor = database.rawQuery("SELECT * FROM jogo " +
                    "WHERE favorito = 1 ORDER BY nome ASC", null);

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                favoriteGames.add(cursor.getString(cursor.getColumnIndex("nome")));
                cursor.moveToNext();
            }

            cursor.close();

            // Verifica se existem jogos favoritos para ocultar listView
            if(favoriteGames == null || favoriteGames.isEmpty()){
                listGames.setVisibility(View.INVISIBLE);
                semFavoritos.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Você não possui nenhum favorito!", Toast.LENGTH_SHORT).show();
            }else{
                listGames.setVisibility(View.VISIBLE);
                semFavoritos.setVisibility(View.INVISIBLE);
            }

            // Criação do adapter
            favoritesAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    favoriteGames);

            listGames.setAdapter(favoritesAdapter);

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
