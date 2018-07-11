package lucaslioli.gimmegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class GimmoFragment extends Fragment {

    private Button botaoBuscar;

    private TextView bemVindo,
            pergNumJogadores,
            pergFxEtaria,
            pergCategoria,
            pergMaterial,
            pergDetalhe;

    private Spinner respMinJogadores,
            respMaxJogadores,
            respMinIdade,
            respMaxIdade,
            respCategoria,
            respMaterial;

    private EditText respDetalhe;

    public GimmoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gimmo, container, false);

        getActivity().setTitle("GimmeGame - Gimmo");

        Random rand= new Random();

        botaoBuscar = view.findViewById(R.id.btnBuscarId);
        bemVindo = view.findViewById(R.id.bemVindoId);

        // Seleciona os elementos de RESPOSTAS
        respMinJogadores = view.findViewById(R.id.respJogadoresMinId);
        respMaxJogadores = view.findViewById(R.id.respJogadoresMaxId);
        respMinIdade = view.findViewById(R.id.respFxEtariaMinId);
        respMaxIdade = view.findViewById(R.id.respFxEtariaMaxId);
        respCategoria = view.findViewById(R.id.respCategoriaId);
        respMaterial = view.findViewById(R.id.respMaterialId);
        respDetalhe = view.findViewById(R.id.respDetalheId);

        // Seleciona o elemento de texto das PERGUNTAS
        pergNumJogadores = view.findViewById(R.id.txtPergunta1);
        pergFxEtaria = view.findViewById(R.id.txtPergunta2);
        pergCategoria = view.findViewById(R.id.txtPergunta3);
        pergMaterial = view.findViewById(R.id.txtPergunta4);
        pergDetalhe = view.findViewById(R.id.txtPergunta5);

        // Atualiza texto das perguntas (e bem vindo) aleatóriamente
        bemVindo.setText(getResources().getStringArray(R.array.bem_vindo)[rand.nextInt(3)]);

        pergNumJogadores.setText(getResources().getStringArray(R.array.pergunta1)[rand.nextInt(3)]);
        pergFxEtaria.setText(getResources().getStringArray(R.array.pergunta2)[rand.nextInt(3)]);
        pergCategoria.setText(getResources().getStringArray(R.array.pergunta3)[rand.nextInt(3)]);
        pergMaterial.setText(getResources().getStringArray(R.array.pergunta4)[rand.nextInt(3)]);
        pergDetalhe.setText(getResources().getStringArray(R.array.pergunta5)[rand.nextInt(3)]);

        // Onclick do botão de recomendar
        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GameDetails.class);

                intent.putExtra("nomeJogo", "Truco"); // Passando manualmente para testes
                intent.putExtra("minJogadores", respMinJogadores.getSelectedItem().toString());
                intent.putExtra("maxJogadores", respMaxJogadores.getSelectedItem().toString());
                intent.putExtra("minIdade", respMinIdade.getSelectedItem().toString());
                intent.putExtra("maxIdade", respMaxIdade.getSelectedItem().toString());
                intent.putExtra("categoria", respCategoria.getSelectedItem().toString());
                intent.putExtra("material", respMaterial.getSelectedItem().toString());
                intent.putExtra("detalhes",respDetalhe.getText().toString());

                getActivity().startActivity(intent);
            }
        });

        return view;
    }

}
