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
 * Player and Pokemon classes. Battling will be added later.
 */
public class Game {

    public static final String POKE_BALL = "Poke Ball";
    public static final int STARTING_BALLS = 5;

    // The fixed list of wild Pokemon that "explore" picks from
    private static final String[] WILD_NAMES =
        {"Bulbasaur", "Charmander", "Squirtle", "Pidgey", "Rattata", "Geodude"};
    private static final String[] WILD_TYPES =
        {"Grass", "Fire", "Water", "Normal", "Normal", "Rock"};
    private static final int[] WILD_HP = {45, 39, 44, 40, 30, 40};
    private static final int[] WILD_ATTACK = {49, 52, 48, 45, 56, 80};

    private Player player;
    private Pokemon wildPokemon;
    private boolean gameOver;
    private Scanner input;
    private Random random;

    public Game()
    {
        this(new Scanner(System.in), new Random());
    }

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
        wildPokemon = null;
        gameOver = false;
    }

    public static void main(String[] args)
    {
        new Game().run();
    }

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

    public void processCommandString(String command)
    {
        if (command == null || command.trim().isEmpty())
        {
            System.out.println("What do you want to do? Type help for commands.");
            return;
        }

        String word = command.trim().toLowerCase();

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
            System.out.println("Battling isn't ready yet.");
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

    public Player getPlayer()
    {
        return player;
    }

    public Pokemon getWildPokemon()
    {
        return wildPokemon;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    // Shows a random wild Pokemon from the fixed list
    private void explore()
    {
        int i = random.nextInt(WILD_NAMES.length);
        wildPokemon = new Pokemon("common", WILD_NAMES[i], WILD_TYPES[i],
            WILD_HP[i], WILD_ATTACK[i], WILD_ATTACK[i]);
        System.out.println("A wild " + wildPokemon.getName() + " appeared! (Type: "
            + wildPokemon.getType() + ", HP: " + wildPokemon.getHealth() + ")");
    }

    // Uses one Poke Ball and adds the wild Pokemon to the collection
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
    }

    // Shows the player's Pokemon and how many Poke Balls are left
    private void view()
    {
        System.out.println("Your Pokemon:");
        for (int i = 0; i < player.getPokemonCount(); i++)
        {
            Pokemon p = player.getPokemon(i);
            System.out.println("  " + p.getName() + " (Type: " + p.getType()
                + ", HP: " + p.getHealth() + ", Attack: " + p.getAttack() + ")");
        }
        System.out.println("Poke Balls left: " + player.getItemAmount(POKE_BALL));
    }

    private void printHelp()
    {
        System.out.println("Commands: explore, catch, view, fight, help, quit");
    }
}
