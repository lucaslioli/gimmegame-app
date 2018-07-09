package lucaslioli.gimmegame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class GamesFragment extends Fragment {

    private ListView listGames;
    private String[] games = {
            "Beer Pong", "Eu nunca", "Sueca", "Truco",
            "Uno", "Pife", "Dorminhoco", "Jogo da forca",
            "Stop", "Pega-pega", "Queimada", "Esconde-esconde",
            "Dança da cadeira", "Ludo", "Damas", "Xadrez"
    };

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        listGames = view.findViewById(R.id.listViewGames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                games
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
