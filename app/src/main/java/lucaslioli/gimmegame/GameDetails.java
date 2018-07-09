package lucaslioli.gimmegame;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

public class GameDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

//        Toast.makeText(getApplicationContext(), "Jogo adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
    }
}
