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
 * Runs the PokeQuest main loop: reads commands from the user and calls the
 * Player, Pokemon, Room and Battle classes. The game is either exploring
 * (battle is null) or in a battle (battle is not null).
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

    private static final String BATTLE_HINT =
        "Type attack, special, buff, heal, power, catch or run.";

    // The fixed list of wild Pokemon that "explore" picks from
    private static final String[] WILD_NAMES =
        {"Bulbasaur", "Charmander", "Squirtle", "Pidgey", "Rattata", "Geodude"};
    private static final String[] WILD_TYPES =
        {"Grass", "Fire", "Water", "Normal", "Normal", "Rock"};
    private static final int[] WILD_HP = {45, 39, 44, 40, 30, 40};
    private static final int[] WILD_ATTACK = {49, 52, 48, 45, 56, 80};

    private Player player;
    private LinkedChain<Room> rooms;
    private Room currentRoom;
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
     * Sets up the player, starter Pokemon, bag and map.
     * Time Complexity: O(1), a fixed number of adds (3 items, 3 rooms)
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
        player = new Player("Ash");
        player.addPokemon(new Pokemon("common", "Pikachu", "Electric", 35, 55, 50));
        player.addItem(new Item(POKE_BALL, "catch", STARTING_BALLS));
        player.addItem(new Item(POTION, "heal", STARTING_POTIONS));
        player.addItem(new Item(X_ATTACK, "buff", STARTING_X_ATTACKS));

        rooms = new LinkedChain<Room>();
        rooms.add(new Room("Forest", "Tall trees block out most of the sun."));
        rooms.add(new Room("Cave", "It is dark and you hear rocks falling."));
        rooms.add(new Room("Lake", "Water splashes against the shore."));

        currentRoom = null;
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

        while (!gameOver && input.hasNextLine())
        {
            System.out.print("> ");
            processCommandString(input.nextLine());
        }

        System.out.println("Thanks for playing!");
    }

    /**
     * Runs one command. Bad commands print a message and never crash.
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
     * Returns the player.
     * Time Complexity: O(1)
     */
    public Player getPlayer()
    {
        return player;
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
     * Returns the room the player is in, or null before the first explore.
     * Time Complexity: O(1)
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
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

        currentRoom = rooms.get(random.nextInt(rooms.size()));
        System.out.println("You walk into the " + currentRoom.getName() + ". "
            + currentRoom.getDescription());

        int i = random.nextInt(WILD_NAMES.length);
        wildPokemon = new Pokemon("common", WILD_NAMES[i], WILD_TYPES[i],
            WILD_HP[i], WILD_ATTACK[i], WILD_ATTACK[i]);
        System.out.println("A wild " + wildPokemon.getName() + " appeared! (Type: "
            + wildPokemon.getType() + ", HP: " + wildPokemon.getHealth() + ")");
    }

    // Uses one Poke Ball and adds the wild Pokemon to the collection.
    // Works while exploring or during a fight (it ends the fight).
    // Time Complexity: O(b^2): useItem searches the bag with get(i)
    private void catchPokemon()
    {
        if (wildPokemon == null)
        {
            System.out.println("Catch what? Explore to find a Pokemon first.");
            return;
        }

        if (!player.useItem(POKE_BALL))
        {
            System.out.println("You're out of Poke Balls!");
            return;
        }

        player.addPokemon(wildPokemon);
        System.out.println("You caught " + wildPokemon.getName() + "!");
        wildPokemon = null;
        endBattle();
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
        if (!player.useItem(POTION))
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
        if (!player.useItem(X_ATTACK))
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
        wildPokemon = null;
        endBattle();
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

    // Ends the battle if someone fainted, and ends the game if all of the
    // player's Pokemon have fainted
    // Time Complexity: O(p^2): allFainted
    private void checkBattleOver()
    {
        if (wildPokemon.isFainted())
        {
            System.out.println(wildPokemon.getName() + " fainted! You won.");
            wildPokemon = null;
            endBattle();
        }
        else if (fighter.isFainted())
        {
            System.out.println(fighter.getName() + " fainted!");
            endBattle();

            if (allFainted())
            {
                System.out.println("All your Pokemon have fainted. Game over!");
                gameOver = true;
            }
            else
            {
                System.out.println("Pick another Pokemon with fight and a "
                    + "number, or run.");
            }
        }
    }

    // Time Complexity: O(1)
    private void endBattle()
    {
        battle = null;
        fighter = null;
    }

    // Time Complexity: O(p^2), getPokemon(i) walks the chain each time
    private boolean allFainted()
    {
        for (int i = 0; i < player.getPokemonCount(); i++)
        {
            if (!player.getPokemon(i).isFainted())
            {
                return false;
            }
        }
        return true;
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

        if (number < 1 || number > player.getPokemonCount())
        {
            return null;
        }
        return player.getPokemon(number - 1);
    }

    // Shows the player's numbered Pokemon and what is left in the bag
    // Time Complexity: O(p^2 + b^2): getPokemon(i) in a loop, then getItemAmount
    private void view()
    {
        System.out.println("Your Pokemon:");
        for (int i = 0; i < player.getPokemonCount(); i++)
        {
            Pokemon p = player.getPokemon(i);
            String status = "";
            if (p.isFainted())
            {
                status = " - fainted";
            }
            System.out.println("  " + (i + 1) + ". " + p.getName() + " (Type: "
                + p.getType() + ", HP: " + p.getHealth() + ", Attack: "
                + p.getAttack() + ")" + status);
        }
        System.out.println("Poke Balls left: " + player.getItemAmount(POKE_BALL));
        System.out.println("Potions left: " + player.getItemAmount(POTION));
        System.out.println("X Attacks left: " + player.getItemAmount(X_ATTACK));
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
        }
    }
}
