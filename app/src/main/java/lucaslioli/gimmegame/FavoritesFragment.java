package lucaslioli.gimmegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {

    private ListView listGames;
    private ArrayList<String> favoriteGames;
    private String[] games = {
            "Beer Pong", "Eu nunca", "Sueca", "Truco",
            "Uno", "Pife", "Dorminhoco", "Jogo da forca",
            "Stop", "Pega-pega", "Queimada", "Esconde-esconde",
            "Dança da cadeira", "Ludo", "Damas", "Xadrez"
    };

    private static final String PREFERENCES_FILE = "GimmeGamePreferences";

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        getActivity().setTitle("GimmeGame - Lista de Favoritos");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_FILE, 0);
        favoriteGames = new ArrayList<String>();

        // Removing games that are not favorite
        for (String game : games){
            if(sharedPreferences.contains(game)){
                favoriteGames.add(game);
            }
        }

        listGames = view.findViewById(R.id.listViewFavorites);

        // Check if there are favorite games
        if(favoriteGames == null || favoriteGames.isEmpty()){
            listGames.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.semFavoritosId).setVisibility(View.VISIBLE);
            Toast.makeText(view.getContext(), "Você não possui nenhum favorito!", Toast.LENGTH_SHORT).show();
            return view;
        }else{
            listGames.setVisibility(View.VISIBLE);
            view.findViewById(R.id.semFavoritosId).setVisibility(View.INVISIBLE);
        }

        // Creating the listView for favorite games
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                favoriteGames
        );

        listGames.setAdapter(adapter);

        listGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GameDetails.class);

                intent.putExtra("nomeJogo", listGames.getItemAtPosition(index).toString());

                getActivity().startActivity(intent);
            }
        });

        return view;
    }



}
