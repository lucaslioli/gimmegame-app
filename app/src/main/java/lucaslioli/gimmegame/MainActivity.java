package lucaslioli.gimmegame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.navigation_home:
                transaction.replace(R.id.ScrollViewId, new GimmoFragment()).commit();
                return true;
            case R.id.navigation_games:
                transaction.replace(R.id.ScrollViewId, new GamesFragment()).commit();
                return true;
            case R.id.navigation_favorites:
                transaction.replace(R.id.ScrollViewId, new FavoritesFragment()).commit();
                return true;
        }
        return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Base de Dados
        database = openOrCreateDatabase("gimmegame", MODE_PRIVATE, null);

        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS jogo (" +
                    "id INTEGER PRIMARY KEY," +
                    "nome TEXT NOT NULL UNIQUE," +
                    "descricao TEXT NOT NULL," +
                    "min_idade INTEGER," +
                    "max_idade INTEGER," +
                    "min_jogadores INTEGER," +
                    "max_jogadores INTEGER," +
                    "favorito INTEGER NOT NULL DEFAULT 0);");

            database.execSQL("CREATE TABLE IF NOT EXISTS categoria (" +
                    "id INTEGER PRIMARY KEY," +
                    "nome TEXT NOT NULL UNIQUE);");

            database.execSQL("CREATE TABLE IF NOT EXISTS jogo_categoria (" +
                    "id_jogo INTEGER NOT NULL," +
                    "id_categoria INTEGER NOT NULL," +
                    "PRIMARY KEY (id_jogo, id_categoria)," +
                    "FOREIGN KEY (id_jogo) REFERENCES jogo (id) ON DELETE CASCADE," +
                    "FOREIGN KEY (id_categoria) REFERENCES categoria (id) ON DELETE CASCADE);");

            database.execSQL("CREATE TABLE IF NOT EXISTS material (" +
                    "id INTEGER PRIMARY KEY," +
                    "nome TEXT NOT NULL UNIQUE," +
                    "descricao TEXT);");

            database.execSQL("CREATE TABLE IF NOT EXISTS jogo_material (" +
                    "id_jogo INTEGER NOT NULL," +
                    "id_material INTEGER NOT NULL," +
                    "PRIMARY KEY (id_jogo, id_material)," +
                    "FOREIGN KEY (id_jogo) REFERENCES jogo (id) ON DELETE CASCADE," +
                    "FOREIGN KEY (id_material) REFERENCES material (id) ON DELETE CASCADE);");

            // Busca quantas categorias já estão cadastrados
            Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM categoria", null);
            cursor.moveToFirst();
            // Se não houver categorias cadastradas, insere todos elas
            // (Não deve ser a melhor forma de fazer, mas por hora, ficará assim)
            if(cursor.getInt(0) == 0) {
                database.execSQL("INSERT INTO categoria (id, nome) VALUES" +
                        "(1, 'Alcoólico'),(2, 'Cartas'),(3, 'Educativo'),(4, 'Esportes'),(5, 'Estratégia')," +
                        "(6, 'Lógica'),(7, 'Música'),(8, 'Palavras'),(9, 'Tabuleiro'),(10, 'Outros')");
            }

            cursor = database.rawQuery("SELECT COUNT(*) FROM material", null);
            cursor.moveToFirst();

            if(cursor.getInt(0) == 0) {
                database.execSQL("INSERT INTO material (id, nome) VALUES" +
                        "(1, 'Baralho'),(2, 'Bola'),(3, 'Bolinha de Ping-Pong'),(4, 'Copos'),(5, 'Cadeira'),(6, 'Dados')," +
                        "(7, 'Peças'),(8, 'Papel e Caneta'),(9, 'Tabuleiro'),(10, 'Venda')");
            }

            cursor = database.rawQuery("SELECT COUNT(*) FROM jogo", null);
            cursor.moveToFirst();

            if(cursor.getInt(0) == 0) {
                // BEER PONG
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(1, 'Beer Pong', 18, NULL, 2, NULL, 'Deve-se posicionar seis copos em cada ponta da mesa, como se fossem pinos de boliche, e com uma bolinha de ping-pong os jogadores devem" +
                        "tentar acertar o maior número de copos do seu oponente (lembrando que a bolinha tem que cair dentro do copo, senão não vale o ponto). A cada" +
                        "copo que você acertar, seu oponente tem que virar a cerveja presente dentro dele.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (1,1)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (1,3), (1,4)");

                // EU NUNCA
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(2, 'Eu Nunca', 18, NULL, 3, NULL, 'Todos os participantes devem se reunir em uma roda com o copo de bebida nas mãos. A brincadeira acontece no sentido horário com a primeira \n" +
                        "pessoa dizendo: “Eu nunca...” (complete com algo que quiser). Todos jogadores que já fizeram o que foi dito devem tomar um gole da bebida" +
                        "alcoólica. Ex: Eu nunca quebrei nenhum osso do corpo. Quem já tiver quebrado, deve tomar um gole de sua bebida.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (2,1)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (2,4)");

                // SUECA
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(3, 'Sueca', 18, NULL, 4, NULL, 'Em sentido horário cada jogador tira uma carta por vez do baralho seguindo a regra referente aquela carta. As regras são:\n" +
                        "A - Escolhe uma pessoa para beber uma dose;\n" +
                        "2 - Escolhe duas pessoas para beber uma dose ou uma pessoa para beber duas doses;\n" +
                        "3 - Quem tirou escolhe três pessoas para beber uma dose;\n" +
                        "4 - Toma dose dupla;\n" +
                        "5 - Não pode ir ao banheiro até outra pessoa tirar o 5, se não aguentar bebe dose dupla quando voltar.\n" +
                        "6 - Mão na mesa. O último que colocar a palma da mão na mesa bebe.\n" +
                        "7 - História. O primeiro fala tipo (era uma vez), o próximo (era uma vez um cara bêbado), e assim por diante. Quem errar bebe.\n" +
                        "8 - Palavra não dita. Quem tirar a carta escolhe uma palavra que não pode ser dita. Quem falar bebe.\n" +
                        "9 - PI - Cada um fala um número, múltiplos de 3 e os que tem um 3 são substituídos por PI, quem errar bebe. Ex.: 1, 2, PI, 4, 5, PI, 7, 8, PI, 10, 11, PI, PI, 14, ...\n" +
                        "10 - Regra. Escolhe-se uma regra para o jogo. Ex.: Quem mexer no cabelo bebe (para sacanear as mulheres).\n" +
                        "J - Todos bebem.\n" +
                        "Q - Mulheres bebem.\n" +
                        "K - Homens bebem.\n" +
                        "PS: As regras podem ser alteradas de acordo com a criatividade dos participantes antes de iniciar o jogo.\n" +
                        "Fonte: https://www.obaoba.com.br e https://www.einerd.com.br')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (3,1)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (3,1)");

                // TRUCO
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(4, 'Truco', 10, NULL, 2, 6, 'No jogo com dois participantes, um jogará contra o outro e a contagem de pontos é individual. Quando há quatro ou seis participantes, " +
                        "duplas são formadas, uma jogando contra a outra (ou seja, a contagem de pontos é por dupla) e os oponentes devem sentar de forma alternada, de modo que o seu parceiro esteja a sua frente.\n" +
                        "Cada jogador recebe 3 cartas, e a equipe que alcançar primeiro o maior número de pontos (12 ou 24) é a vencedora. Além do pedido de Truco, que pode aumentar o valor da mão, também existem os " +
                        "pedidos de Envido e Flor, que são disputas paralelas e também contabilizam pontos no placar geral.\n" +
                        "- Manilhas: São as cartas mais fortes do jogo. 1 de espada, 1 de paus, 7 de espada e 7 de ouro, nessa sequência.\n" +
                        "Ordem de força das cartas restantes: 3 (mais forte), 2, 1 (copas e ouro), K, Q, J, 10, 7 (paus e copas), 6, 5, 4 (mais fraca). \n" +
                        "- Envido: O envido pode ser chamado ao início de cada rodada. É a soma do valor de duas cartas do mesmo naipe na mão de cada jogador, sendo que cada uma vale o número que corresponde, " +
                        "com exceção do K (12), J (10) e do Q (11) acrescidas do valor de 20 pontos. Ex: 2, 4, K. Correspondem a 26 pontos (2 + 4 + 20). Quando um jogador chama o envido, o adversário tem a opção de " +
                        "recusar e então quem chamou ganha 1 ponto, ou então aceitar e quem tiver o maior envido ganha 2 pontos.\n" +
                        "- Flor: Deve ser chamado sempre que um jogador tem as 3 cartas do mesmo naipe. Se o adversário recusar, quem chamou ganha 3 pontos, mas se o adversário também pode dizer “Contra flor” se ele também " +
                        "possuir flor, então quem tiver a maior ganha 6 pontos.\n" +
                        "- Truco: Aumenta o valor da mão para 2 pontos. O adversário pode recusar dizendo que “corre” e quem chamou o truco, ganha 1 ponto, ou ele pode aceitar e a mão valerá 2 pontos. Mas ele também pode " +
                        "responder “Re-truco” para aumentar o valor da mão para 3 pontos. Então o adversário pode responder “correndo” ou aumentando a mão para 4 pontos se dizer “Vale 4”.\n" +
                        "PS: Utiliza-se geralmente o Baralho Espanhol, sem as cartas 8, 9 e Coringas.\n" +
                        "Fonte: https://www.jogatina.com')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (4,2)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (4,1)");

                // UNO
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(5, 'Uno', 4, NULL, 2, 10, 'O objetivo do jogo é ser o primeiro a conseguir descartar todas as cartas que tem na mão, seguindo as regras do jogo e realizando as ações de cada carta. " +
                        "O jogo começa com um jogador embaralhando as cartas e distribuindo 7 cartas pelos demais jogadores. Depois, coloca-se a carta do topo do baralho para ficar como a primeira carta no topo " +
                        "da pilha de descarte. O primeiro jogador a jogar é o jogador à esquerda do distribuidor e o jogo segue no sentido horário. Cada jogador, na sua vez, pode descartar uma carta de acordo com a " +
                        "carta no topo da pilha de descarte. Você pode descartar as cartas da mesma cor, do mesmo número ou com o mesmo desenho.\n" +
                        "Sempre que não conseguir descartar uma carta na sua vez, você terá de comprar uma carta. Se não conseguir jogar a carta que comprou, você tem de passar a vez. Quando você descartar a penúltima " +
                        "carta, tem que dizer alto UNO. Se esquecer e outro jogador reparar, você terá de comprar duas cartas.\n" +
                        "- Carta +2: obriga o próximo jogador a pescar 2 cartas e ficar essa rodada sem jogar.\n" +
                        "- Carta +4: obriga o próximo jogador a pescar 4 cartas e ficar essa rodada sem jogar. Além disso, quem jogou a carta deve escolher a cor das cartas daquele turno.\n" +
                        "- Coringa: é usada para mudar a cor do naipe para o turno seguinte.\n" +
                        "- Pular: quando esta carta é jogada, o jogador seguinte perde a vez.\n" +
                        "- Inverter: ela inverte o sentido do jogo.\n" +
                        "Fonte: http://www.jogos360.com.br')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (5,2)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (5,1)");

                // DORMINHOCO
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(6, 'Dorminhoco', 4, NULL, 3, 12, 'Separa-se de um baralho normal, uma trinca de cartas do mesmo naipe para cada jogador. Ou seja, se forem 4 os jogadores, separa-se 4 " +
                        "trincas. Se forem 5, cinco trincas, e assim por diante. Deve-se separar também um coringa. As cartas são embaralhadas e distribuídas entre " +
                        "os jogadores, sendo que um ficará com uma carta a mais que os outros jogadores e iniciará o jogo. Este jogador escolhe uma de suas cartas e " +
                        "passa a mesma para o jogador a sua esquerda. O jogo prossegue, sempre com um jogador passando uma carta para o jogador seguinte. Quem " +
                        "receber o coringa passado pelo jogador anterior, deve ficar com ele por uma rodada, devendo passar outra carta. Quando um jogador formar uma " +
                        "trinca, ou seja, ficar com três cartas iguais na mão, deve baixá-las, mas com os valores para baixo, de maneira discreta. Os demais " +
                        "jogadores devem fazer o mesmo. O último a baixar suas cartas será o “Dorminhoco”. O objetivo o jogo, portanto, não é formar a trinca mas não " +
                        "ser o último a baixar as suas cartas. Aquele que for o dorminhoco deve receber uma rolhada no rosto, para deixar uma marca, ganha no final, " +
                        "quem tiver menos marcas.\n" +
                        "Fonte: http://www.xalingo.com.br')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (6,2)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (6,1)");

                // PIFE
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(7, 'Pife', 10, NULL, 2, 8, 'Deve distribuir 9 cartas para cada jogador e o objetivo é formar trincas de naipes diferentes ou sequências de naipes iguais. O primeiro a " +
                        "jogar deve pescar uma carta do baralho e se ela lhe servir, deve jogar uma carta que está em sua mão no monte de descarte. Caso a carta não lhe sirva, deve fazer o mesmo. " +
                        "O próximo jogador, em sentido horário, poderá comprar do baralho ou comprar a última carta descartada no monte. A partida termina quando alguém bate (formando 3 trincas ou sequências). " +
                        "Um jogador pode bater com 9 ou 10 cartas. Quando bate com 9 ele descarta uma carta, e quando bate com 10 não descarta nada.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (7,2)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (7,1)");

                // JOGO DA FORCA
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(8, 'Jogo da Forca', 6, NULL, 2, NULL, 'Uma pessoa deve pensar em uma palavra ou em uma frase e desenhar em uma folha traços em sequência, referentes as letras daquela palavra e " +
                        "também desenhar uma forca. Cada jogador deve dizer uma letra em sua vez, se essa letra estiver na palavra ela deve ser escrita no lugar " +
                        "dela, se essa letra não estiver na palavra, deve-se desenhar uma parte do boneco da forca. Quando o boneco estiver completo (cabeça, corpo, " +
                        "braços e pernas) ele foi enforcado, ou seja, quem pensou na palavra ganha. Mas se alguém adivinhar a palavra ela é a vencedora.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (8,3),(8,8)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (8,8)");

                // STOP
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(9, 'Stop', 10, NULL, 2, NULL, 'Para jogar stop as pessoas se reúnem, determinam e escrevem em uma folha diferentes categorias como Nome, CEP (cidade, estado e país), Ator/" +
                        "Atriz, PHC (parte do corpo humano) etc. e sorteiam uma letra do alfabeto. Sorteada a letra, o jogo começa. Para cada tema, os jogadores " +
                        "devem encontrar uma palavra que comece com a letra sorteada. Quem preenche primeiro todas as categorias fala “stop” e todos os outros " +
                        "jogadores param o jogo. Vence quem somar mais pontos. (Nenhuma palavra é zero, palavras iguais são 5 pontos e palavras diferentes são 10 " +
                        "pontos).')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (9,3),(9,8)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (9,8)");

                // Pega-Pega
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(10, 'Pega-Pega', 4, NULL, 3, NULL, 'Deve-se sortear quem começará sendo o que “pega”, após o sorteio este jogador deve começar a correr com o intuito de pegar os outros " +
                        "jogadores enquanto eles fogem dele, quem ele conseguir pegar, passa a ser pegador. ')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (10,4)");

                // QUEIMADA
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(11, 'Queimada', 6, NULL, 8, NULL, 'Deve-se sinalizar 2 campos opostos no ambiente do jogo. Cada time se coloca num campo, sendo que apenas um jogador de cada lado deverá se " +
                        "colocar atrás da linha de fundo do campo adversário sendo denominado de \"cruzar\", que não pode \"ser queimado\" enquanto está nesta função. O objetivo tentar acertar alguém do " +
                        "grupo adversário, que passará a ser seu prisioneiro. Ganha quem fizer o maior número possível de prisioneiros.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (11,4)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (11,2)");

                // ESCONDE-ESCONDE
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(12, 'Esconde-Esconde', 4, NULL, 5, NULL, 'O objetivo é esconder e não ser encontrado pela pessoa que vai procurar. O que vai procurar deve ficar com os olhos fechados, " +
                        "até que termine uma contagem, previamente combinada com todos os participantes. Quando termina a contagem, os outros já devem estar escondidos. Então a pessoa abre os olhos " +
                        "e vai procurá-los. O encarregado de procurar os escondidos será vencedor apenas se encontrar todos os participantes, antes que algum retorne ao ponto de partida. O primeiro dos " +
                        "escondidos que voltar para o ponto de partida, vence a brincadeira.\n" +
                        "Fonte: https://www.obrasileirinho.com.br')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (12,4)");

                // DANÇA DA CADEIRA
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(13, 'Dança da Cadeira', 6, NULL, 5, NULL, 'Deve-se organizar as cadeiras em um círculo (um número a menos que o de pessoas), as pessoas devem andar em círculos em frente as " +
                        "cadeiras enquanto toca uma música que deverá ser controlado por alguém. Quando a música parar todos devem sentar nas cadeiras, aquele que sobrar, perde. Então deve-se tirar uma " +
                        "cadeira para a próxima rodada, e seguir assim respectivamente até que sobre apenas uma pessoa na brincadeira, que será o vencedor.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (13,7)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (13,5)");

                // LUDO
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(14, 'Ludo', 8, NULL, 2, 4, 'Cada jogador tem 4 peças que move pelas casas do tabuleiro até dar uma volta inteira e chegar no centro. Cada peça só pode entrar no jogo quando o " +
                        "jogador tira um 6. Se uma peça cair na mesma casa que outra pela do adversário, a peça do adversário é enviada de volta para o início e não pode ser usada até tirar um 6. Ganha quem " +
                        "chega todas as suas peças ao centro primeiro.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (14,9)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (14,9),(14,7)");

                // DAMAS
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(15, 'Damas', 8, NULL, 2, 2, 'O jogo de damas é praticado em um tabuleiro de 64 casas, claras e escuras. Com 12 peças brancas de uma lado e 12 peças pretas do outro lado. " +
                        "As peças se movimentam apenas nas diagonais. O objetivo do jogo é imobilizar ou capturar todas as peças do adversário. ')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (15,9),(15,5)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (15,9),(15,7)");

                // XADREZ
                database.execSQL("INSERT INTO jogo (id, nome, min_idade, max_idade, min_jogadores, max_jogadores, descricao) VALUES" +
                        "(16, 'Xadrez', 12, NULL, 2, 2, 'Cada jogador move um conjunto de peças brancas ou pretas no tabuleiro, um de cada vez. Peças diferentes podem se mover de maneiras diferentes " +
                        "(para a frente, para trás, na diagonal…). Objetivo é mover suas peças para capturar as peças do adversário, sem que ele capture as suas. Ganha quem capturar o rei do adversário. " +
                        "A movimentação de cada peça é:\n" +
                        "- Torre: somente de forma horizontal ou vertical.\n" +
                        "- Bispo: somente nas diagonais.\n" +
                        "- Dama – horizontal, vertical e diagonais.\n" +
                        "- Rei: qualquer direção mas com limitação quanto ao número de casas (1 por lance).\n" +
                        "- Peão: somente 1 lance para frente e não pode retroceder. Na sua posição inicial, este pode dar um salto de 2 casas à frente.\n" +
                        "- Cavalo:  feita em forma de L.')");

                database.execSQL("INSERT INTO jogo_categoria (id_jogo, id_categoria) VALUES (16,9),(16,5)");
                database.execSQL("INSERT INTO jogo_material (id_jogo, id_material) VALUES (16,9),(16,7)");
            }

            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        // Inicializa o listener da bottom navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Chama o fragment do Gimmo ao abrir o app
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.ScrollViewId, new GimmoFragment()).commit();
    }

}
