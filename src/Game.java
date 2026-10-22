//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Ishaan Srichitturi (Ishaan07)

//LLM Statement:
//
//During the preparation of this assignment, I, Ishaan Srichitturi used claude
//for designing and programming the Game class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

import java.util.Random;
import java.util.Scanner;

/**
 * Runs the two-player PokeQuest main loop: reads commands from the user and
 * calls the Player, Pokemon, Room and Battle classes. Players take turns.
 * A turn is one whole encounter: the player explores, then either catches
 * the wild Pokemon or fights it, and the turn only passes to the other
 * player once the encounter is over (caught, won, lost or ran away). The
 * game is either exploring (battle is null) or in a battle (battle is not
 * null). The game ends when one player's Pokemon have all fainted; the
 * other player wins.
 *
 * Time complexities below use p = number of Pokemon the player owns,
 * b = number of item stacks in the bag (at most 3: Poke Balls, Potions and
 * X Attacks, so b terms are really O(1)), and t = number of commands typed.
 * Looping over the collection with getPokemon(i) is O(p^2), because
 * LinkedChain.get(i) walks i nodes each time: 0 + 1 + ... + (p - 1).
 */
public class Game {

    public static final String POKE_BALL = "Poke Ball";
    public static final String POTION = "Potion";
    public static final String X_ATTACK = "X Attack";
    public static final int STARTING_BALLS = 5;
    public static final int STARTING_POTIONS = 2;
    public static final int STARTING_X_ATTACKS = 1;
    // A wild Pokemon can only be caught once its HP is below this
    public static final int CATCH_HP_THRESHOLD = 20;

    private static final String BATTLE_HINT =
        "Type attack, special, buff, heal, power, catch or run.";

    // The fixed list of wild Pokemon that "explore" picks from
    private static final String[] WILD_NAMES =
        {"Bulbasaur", "Charmander", "Squirtle", "Pidgey", "Rattata", "Geodude"};
    private static final String[] WILD_TYPES =
        {"Grass", "Fire", "Water", "Normal", "Normal", "Rock"};
    private static final int[] WILD_HP = {45, 39, 44, 40, 30, 40};
    private static final int[] WILD_ATTACK = {49, 52, 48, 45, 56, 80};

    private static final int PLAYER_COUNT = 2;

    // The two players, and the index (0 or 1) of the one whose turn it is
    private Player[] players;
    private int current;
    private Player winner;
    private LinkedChain<Room> rooms;
    // The room each player is standing in (null until they first explore)
    private Room[] currentRooms;
    // Only the current player ever has a wild Pokemon; it is cleared when
    // the turn ends
    private Pokemon wildPokemon;
    private Pokemon fighter;
    private Battle battle;
    private boolean gameOver;
    private Scanner input;
    private Random random;

    /**
     * Creates a game that reads from the keyboard.
     * Time Complexity: O(1)
     */
    public Game()
    {
        this(new Scanner(System.in), new Random());
    }

    /**
     * Sets up both players (each with a starter Pokemon and a bag) and the
     * map. Player 1 (Ash) goes first.
     * Time Complexity: O(1), a fixed number of adds (6 items, 3 rooms)
     *
     * @param input where commands are read from
     * @param random used to pick rooms and wild Pokemon
     * @throws IllegalArgumentException if input or random is null
     */
    public Game(Scanner input, Random random)
    {
        if (input == null || random == null)
        {
            throw new IllegalArgumentException("Input and random cannot be null");
        }

        this.input = input;
        this.random = random;
        players = new Player[PLAYER_COUNT];
        players[0] = new Player("Ash");
        players[0].addPokemon(
            new Pokemon("common", "Pikachu", "Electric", 35, 55, 50));
        players[1] = new Player("Misty");
        players[1].addPokemon(
            new Pokemon("common", "Charmander", "Fire", 39, 52, 52));
        for (int i = 0; i < PLAYER_COUNT; i++)
        {
            players[i].addItem(new Item(POKE_BALL, "catch", STARTING_BALLS));
            players[i].addItem(new Item(POTION, "heal", STARTING_POTIONS));
            players[i].addItem(new Item(X_ATTACK, "buff", STARTING_X_ATTACKS));
        }
        current = 0;
        winner = null;

        rooms = new LinkedChain<Room>();
        rooms.add(new Room("Forest", "Tall trees block out most of the sun."));
        rooms.add(new Room("Cave", "It is dark and you hear rocks falling."));
        rooms.add(new Room("Lake", "Water splashes against the shore."));

        currentRooms = new Room[PLAYER_COUNT];
        wildPokemon = null;
        fighter = null;
        battle = null;
        gameOver = false;
    }

    /**
     * Starts the game.
     * Time Complexity: O(t * p^2), same as run(). The spec estimated O(t);
     * it is higher because a single command can cost O(p^2).
     */
    public static void main(String[] args)
    {
        new Game().run();
    }

    /**
     * Reads and runs commands until the game ends or input runs out.
     * Time Complexity: O(t * p^2), t commands that each cost at most O(p^2)
     * (see processCommandString). The spec estimated O(t).
     */
    public void run()
    {
        System.out.println("Welcome to PokeQuest!");
        printHelp();
        announceTurn();

        while (!gameOver && input.hasNextLine())
        {
            System.out.print(getCurrentPlayer().getName() + "> ");
            processCommandString(input.nextLine());
        }

        System.out.println("Thanks for playing!");
    }

    /**
     * Runs one command for the current player. Bad commands print a message
     * and never crash. Commands that finish an encounter (catch, run, or the
     * end of a fight) also pass the turn to the other player.
     * Time Complexity: O(p^2) worst case. Matching the command word is
     * O(1) (a fixed number of if/else checks; the spec estimated O(n) for
     * searching a command list). The work after that depends on the
     * command: explore O(1), catch O(b^2), fight O(p), attack O(p^2),
     * special/buff O(p^2), heal/power O(b^2 + p^2), view O(p^2 + b^2),
     * run/help/quit O(1).
     *
     * @param command what the user typed
     */
    public void processCommandString(String command)
    {
        if (command == null || command.trim().isEmpty())
        {
            System.out.println("What do you want to do? Type help for commands.");
            return;
        }
        if (gameOver)
        {
            System.out.println("The game is over.");
            return;
        }

        String[] words = command.trim().toLowerCase().split("\\s+", 2);
        String word = words[0];
        String target = "";
        if (words.length > 1)
        {
            target = words[1].trim();
        }

        if (word.equals("explore"))
        {
            explore();
        }
        else if (word.equals("catch"))
        {
            catchPokemon();
        }
        else if (word.equals("view"))
        {
            view();
        }
        else if (word.equals("fight"))
        {
            fight(target);
        }
        else if (word.equals("attack"))
        {
            attack();
        }
        else if (word.equals("special"))
        {
            special();
        }
        else if (word.equals("buff"))
        {
            buff();
        }
        else if (word.equals("heal"))
        {
            heal();
        }
        else if (word.equals("power"))
        {
            power();
        }
        else if (word.equals("run"))
        {
            runAway();
        }
        else if (word.equals("help"))
        {
            printHelp();
        }
        else if (word.equals("quit"))
        {
            gameOver = true;
        }
        else
        {
            System.out.println("I don't know \"" + command.trim()
                + "\". Type help for commands.");
        }
    }

    /**
     * Returns one of the two players.
     * Time Complexity: O(1)
     *
     * @param number 1 or 2
     * @throws IllegalArgumentException if number is not 1 or 2
     */
    public Player getPlayer(int number)
    {
        if (number < 1 || number > PLAYER_COUNT)
        {
            throw new IllegalArgumentException("Player number must be 1 or 2");
        }
        return players[number - 1];
    }

    /**
     * Returns the player whose turn it is.
     * Time Complexity: O(1)
     */
    public Player getCurrentPlayer()
    {
        return players[current];
    }

    /**
     * Returns the number (1 or 2) of the player whose turn it is.
     * Time Complexity: O(1)
     */
    public int getCurrentPlayerNumber()
    {
        return current + 1;
    }

    /**
     * Returns the winner, or null if the game is still going or was quit.
     * Time Complexity: O(1)
     */
    public Player getWinner()
    {
        return winner;
    }

    /**
     * Returns the wild Pokemon being faced, or null if there is none.
     * Time Complexity: O(1)
     */
    public Pokemon getWildPokemon()
    {
        return wildPokemon;
    }

    /**
     * Returns the room the current player is in, or null if they have not
     * explored yet.
     * Time Complexity: O(1)
     */
    public Room getCurrentRoom()
    {
        return currentRooms[current];
    }

    /**
     * Tells whether a fight is going on.
     * Time Complexity: O(1)
     */
    public boolean isInBattle()
    {
        return battle != null;
    }

    /**
     * Tells whether the game has ended.
     * Time Complexity: O(1)
     */
    public boolean isGameOver()
    {
        return gameOver;
    }

    // Moves to a random room and shows a random wild Pokemon there
    // Time Complexity: O(1): rooms.get(i) on 3 rooms
    private void explore()
    {
        if (isInBattle())
        {
            System.out.println("You cannot explore during a fight. "
                + BATTLE_HINT);
            return;
        }

        Room room = rooms.get(random.nextInt(rooms.size()));
        currentRooms[current] = room;
        System.out.println("You walk into the " + room.getName() + ". "
            + room.getDescription());

        int i = random.nextInt(WILD_NAMES.length);
        wildPokemon = new Pokemon("common", WILD_NAMES[i], WILD_TYPES[i],
            WILD_HP[i], WILD_ATTACK[i], WILD_ATTACK[i]);
        System.out.println("A wild " + wildPokemon.getName() + " appeared! (Type: "
            + wildPokemon.getType() + ", HP: " + wildPokemon.getHealth() + ")");
    }

    // Uses one Poke Ball and adds the wild Pokemon to the collection.
    // Works while exploring or during a fight (it ends the fight), but only
    // if the wild Pokemon's HP is below CATCH_HP_THRESHOLD; a refused catch
    // uses no Poke Ball and does not end the turn. Catching ends the turn.
    // Time Complexity: O(b^2): useItem searches the bag with get(i)
    private void catchPokemon()
    {
        if (wildPokemon == null)
        {
            System.out.println("Catch what? Explore to find a Pokemon first.");
            return;
        }

        if (wildPokemon.getHealth() >= CATCH_HP_THRESHOLD)
        {
            System.out.println(wildPokemon.getName() + " is too healthy to "
                + "catch (HP: " + wildPokemon.getHealth() + "). Weaken it "
                + "below " + CATCH_HP_THRESHOLD + " HP first.");
            return;
        }

        if (!getCurrentPlayer().useItem(POKE_BALL))
        {
            System.out.println("You're out of Poke Balls!");
            return;
        }

        getCurrentPlayer().addPokemon(wildPokemon);
        System.out.println("You caught " + wildPokemon.getName() + "!");
        endTurn();
    }

    // Starts a battle between the chosen Pokemon (number from view) and
    // the wild Pokemon
    // Time Complexity: O(p): getPokemon(n) walks the chain
    private void fight(String target)
    {
        if (isInBattle())
        {
            System.out.println("You are already fighting. " + BATTLE_HINT);
            return;
        }
        if (wildPokemon == null)
        {
            System.out.println("Fight what? Explore to find a Pokemon first.");
            return;
        }
        if (target.isEmpty())
        {
            System.out.println("Fight with which Pokemon? "
                + "Type fight and a number from view, like fight 1.");
            return;
        }

        Pokemon chosen = pickPokemon(target);
        if (chosen == null)
        {
            System.out.println("You cannot fight with \"" + target
                + "\". Type view to see your Pokemon.");
            return;
        }
        if (chosen.isFainted())
        {
            System.out.println("You cannot fight with " + chosen.getName()
                + ". It has fainted.");
            return;
        }

        fighter = chosen;
        battle = new Battle(fighter, wildPokemon);
        System.out.println(fighter.getName() + ", I choose you! "
            + fighter.getName() + " (HP: " + fighter.getHealth() + ") vs "
            + wildPokemon.getName() + " (HP: " + wildPokemon.getHealth() + ")");
        System.out.println(BATTLE_HINT);
    }

    // One turn: your Pokemon attacks, then the wild Pokemon attacks back
    // Time Complexity: O(p^2): the game-over check
    private void attack()
    {
        if (!isInBattle())
        {
            System.out.println("Attack what? Type fight and a number first.");
            return;
        }

        battle.runTurn();
        printBattleStatus();
        checkBattleOver();
    }

    // Special attack (once per fight), then the wild Pokemon attacks back
    // Time Complexity: O(p^2): the game-over check
    private void special()
    {
        if (!isInBattle())
        {
            System.out.println("Special attack what? Type fight and a number first.");
            return;
        }
        if (battle.hasUsedSpecial(fighter))
        {
            System.out.println("You cannot use " + fighter.getName()
                + "'s special attack again this fight.");
            return;
        }

        int damage = battle.specialAttack(fighter, wildPokemon);
        System.out.println(fighter.getName() + " used its special attack for "
            + damage + " damage!");
        wildTurn();
    }

    // Support move: boosts your Pokemon's attack for a few turns, then the
    // wild Pokemon attacks
    // Time Complexity: O(p^2): the game-over check
    private void buff()
    {
        if (!isInBattle())
        {
            System.out.println("You can only buff during a fight.");
            return;
        }
        if (!battle.boost(fighter))
        {
            System.out.println("You cannot buff " + fighter.getName()
                + ". It is already powered up.");
            return;
        }

        System.out.println(fighter.getName() + " powered up! Attack +"
            + Battle.BOOST_AMOUNT + " for its next " + Battle.BOOST_TURNS
            + " attacks.");
        wildTurn();
    }

    // Uses a Potion on your Pokemon, then the wild Pokemon attacks
    // Time Complexity: O(b^2 + p^2): useItem plus the game-over check
    private void heal()
    {
        if (!isInBattle())
        {
            System.out.println("You can only heal during a fight.");
            return;
        }
        if (!getCurrentPlayer().useItem(POTION))
        {
            System.out.println("You're out of Potions!");
            return;
        }

        // The bag already used one Potion; itemHeal only needs its effect
        fighter.itemHeal(new Item(POTION, "heal", 1));
        System.out.println(fighter.getName() + " healed to "
            + fighter.getHealth() + " HP.");
        wildTurn();
    }

    // Uses an X Attack: raises your Pokemon's attack for good, then the
    // wild Pokemon attacks
    // Time Complexity: O(b^2 + p^2): useItem plus the game-over check
    private void power()
    {
        if (!isInBattle())
        {
            System.out.println("You can only use an X Attack during a fight.");
            return;
        }
        if (!getCurrentPlayer().useItem(X_ATTACK))
        {
            System.out.println("You're out of X Attacks!");
            return;
        }

        // The bag already used one X Attack; itemBuff only needs its effect
        fighter.itemBuff(new Item(X_ATTACK, "buff", 1));
        System.out.println(fighter.getName() + "'s attack rose to "
            + fighter.getAttack() + "!");
        wildTurn();
    }

    // The wild Pokemon attacks back (if it is still standing)
    // Time Complexity: O(p^2): the game-over check
    private void wildTurn()
    {
        battle.attack(wildPokemon, fighter);
        printBattleStatus();
        checkBattleOver();
    }

    // Time Complexity: O(1)
    private void runAway()
    {
        if (!isInBattle())
        {
            System.out.println("Run from what? You are not in a fight.");
            return;
        }

        System.out.println("You ran away from " + wildPokemon.getName() + ".");
        endTurn();
    }

    // Time Complexity: O(1)
    private void printBattleStatus()
    {
        String boost = "";
        if (battle.getBoostTurns(fighter) > 0)
        {
            boost = " (powered up: " + battle.getBoostTurns(fighter) + " left)";
        }
        System.out.println(fighter.getName() + " HP: " + fighter.getHealth()
            + boost + " | " + wildPokemon.getName() + " HP: "
            + wildPokemon.getHealth());
    }

    // Ends the encounter if someone fainted. A won or lost fight passes the
    // turn; if the fight was lost and all of the current player's Pokemon
    // have fainted, the game ends and the other player wins.
    // Time Complexity: O(p^2): countStanding
    private void checkBattleOver()
    {
        if (wildPokemon.isFainted())
        {
            System.out.println(wildPokemon.getName() + " fainted! You won.");
            endTurn();
        }
        else if (fighter.isFainted())
        {
            System.out.println(fighter.getName() + " fainted!");

            if (countStanding(getCurrentPlayer()) == 0)
            {
                finishGame();
            }
            else
            {
                System.out.println("You lost the fight.");
                endTurn();
            }
        }
    }

    // The current player has no Pokemon left, so the other player wins
    // Time Complexity: O(1)
    private void finishGame()
    {
        winner = players[1 - current];
        gameOver = true;
        clearEncounter();
        System.out.println("All of " + getCurrentPlayer().getName()
            + "'s Pokemon have fainted. " + winner.getName() + " wins!");
    }

    // Clears the wild Pokemon and any fight, then hands the turn to the
    // other player
    // Time Complexity: O(1)
    private void endTurn()
    {
        clearEncounter();
        current = 1 - current;
        announceTurn();
    }

    // Time Complexity: O(1)
    private void announceTurn()
    {
        System.out.println("--- " + getCurrentPlayer().getName() + "'s turn ---");
    }

    // Time Complexity: O(1)
    private void clearEncounter()
    {
        wildPokemon = null;
        battle = null;
        fighter = null;
    }

    // How many of the player's Pokemon have not fainted
    // Time Complexity: O(p^2), getPokemon(i) walks the chain each time
    private int countStanding(Player who)
    {
        int standing = 0;
        for (int i = 0; i < who.getPokemonCount(); i++)
        {
            if (!who.getPokemon(i).isFainted())
            {
                standing++;
            }
        }
        return standing;
    }

    // Turns "1", "2", ... into a Pokemon, or null if it is not a valid number
    // Time Complexity: O(p): getPokemon(n) walks the chain
    private Pokemon pickPokemon(String target)
    {
        int number;
        try
        {
            number = Integer.parseInt(target);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (number < 1 || number > getCurrentPlayer().getPokemonCount())
        {
            return null;
        }
        return getCurrentPlayer().getPokemon(number - 1);
    }

    // Shows the current player's numbered Pokemon and bag, and how many of
    // the opponent's Pokemon are still standing
    // Time Complexity: O(p^2 + b^2): getPokemon(i) in a loop, then getItemAmount
    private void view()
    {
        Player me = getCurrentPlayer();
        Player opponent = players[1 - current];

        System.out.println(me.getName() + "'s Pokemon:");
        for (int i = 0; i < me.getPokemonCount(); i++)
        {
            Pokemon p = me.getPokemon(i);
            String status = "";
            if (p.isFainted())
            {
                status = " - fainted";
            }
            System.out.println("  " + (i + 1) + ". " + p.getName() + " (Type: "
                + p.getType() + ", HP: " + p.getHealth() + ", Attack: "
                + p.getAttack() + ")" + status);
        }
        System.out.println("Poke Balls left: " + me.getItemAmount(POKE_BALL));
        System.out.println("Potions left: " + me.getItemAmount(POTION));
        System.out.println("X Attacks left: " + me.getItemAmount(X_ATTACK));
        System.out.println(opponent.getName() + " has "
            + countStanding(opponent) + " of " + opponent.getPokemonCount()
            + " Pokemon standing.");
    }

    // Time Complexity: O(1)
    private void printHelp()
    {
        if (isInBattle())
        {
            System.out.println("Battle commands: attack, special, buff, heal, "
                + "power, catch, run, view, help, quit");
        }
        else
        {
            System.out.println("Commands: explore, catch, view, fight <number>, "
                + "help, quit");
            System.out.println("Your turn ends when you catch, win, lose or "
                + "run from a wild Pokemon.");
        }
    }
}