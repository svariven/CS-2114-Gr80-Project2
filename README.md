# CS-2114-Gr80-Project2

PokeQuest

PokeQuest is a Pokemon fighting/Pokedex text based game where players can 
explore, catch Pokemon, and battle others. 

How to Compile and Run

1. Open the PokeQuest project in Eclipse. 
2. Make sure the project has a Java JRE and JUnit 4 configured.
3. Eclipse should automatically compile the Java files when Build
Automatically is enabled under Project.
4. Right-click Game.java and select Run As → Java Application.
5. Use commands like explore, catch, view, fight <number> (number from
view), help, and quit. During a fight: attack, special (once per fight),
buff (attack boost for 3 attacks), heal (uses a Potion), power (uses an
X Attack), catch, and run.

How to Run Tests

1. In Eclipse, expand src folder. 
2. Right-click each test class, such as ItemTest.java.
3. Select Run As → JUnit Test.
4. Repeat for every test class.
5. Verify that all tests pass with no failures or errors.

REFLECTION

What changed from your specification? Classes, method signatures, where 
validation lives, test cases — what did the spec say, what did you 
build instead, and why? 

We simplified our original spec. document to create a prototype. 
We kept our main classes and implemented basic battling, healing, catching, 
and room exploration. We left out multiplayer and some advanced combat 
features for now.. We also added methods like getPokemon() and getItemAmount() 
to help the classes work together. Each class handles its own input validation,
and we added test cases. This helped us create the basic game. 
We decided to keep our basic classes the same based on group discussion 
and we added methods with the suggestion from Chat GPT. 
 
What changed in the LinkedChain? Anything you added to or modified in the 
provided class and why your application needed it, or that you used it 
unchanged. 

The only thing that changed in Linked Chain was the addition of a toString 
method that allows us to correctly display information to the user by properly 
iterating through each node within a room whether it be a Pokemon or item node.

What happened to your unknowns? Of the things you listed in your scope as not 
knowing how to do, which did you figure out, and which are still open? 

Some of our unknowns were completely solved. Specifically implementing the 
exploration aspect of the game. A random Pokemon can appear in any room which 
was a major aspect to the realism of a Pokemon game. This gave us a hint as to 
what we should do for the full implementation of the exploration game element. 
Furthermore, implementing most of our MVP allows us to see the more intricate 
parts that were missed in the spec. We figured out how to implement basic 
battles and exploration, but still need to finish multiplayer and some advanced 
combat features.

What doesn't work yet? MVP features, bad-input cases, or tests that are 
missing or failing in this prototype. 

Most, if not all of the bad input is being taken care of at least 
at an incremental level. Players can currently explore random rooms, catch 
Pokémon, and battle wild Pokémon. However, players cannot choose which room 
to enter, and some advanced combat features are still unfinished. 

NEXT STEPS

What is left to finish the MVP? The remaining features and fixes, in the 
order you will do them.

We have implemented basic battling, catching, healing, and exploration.
Next, we will improve movement between rooms, finish testing the full game, 
and fix any remaining bugs. 

Which stretch goals will you attempt? The ones from your scope you are 
committing to, and the ones you are dropping. 

Advanced combat and local multiplayer become more realistic with each day of 
implementation. Our team will be attempting both of these stretch goals.

What new tests will you need? The methods and bad-input cases the added work 
must cover.

Future tests will focus on expanding our existing combat and game logic. 
Since the Battle class has already been implemented and integrated with Game, 
we will add more tests for Pokémon damage, Poké Ball capture conditions, and 
interactions between Pokémon and items. We will also test local multiplayer, 
including switching between players, keeping their Pokémon and inventories 
separate, and determining the winner of a two-player battle.

Who does what? Which team member owns each remaining item. 

Each of us will continue working on our assigned classes. First, Richard will 
handle any remaining Pokémon bugs and validation tests, while everyone checks 
their existing JUnit tests. Next, Ishaan will work on integrating advanced 
combat and local multiplayer into Game, Rushil will handle separate player 
inventories and Pokémon collections, Richard will work on Pokémon battle 
behavior, and Gowri will handle item interactions and related tests. Finally, 
we will all test the completed game and fix any remaining bugs.

ATTRIBUTION TABLE

|       Section      | Team Member(s) |                                             Contribution (how)                                             |
|:------------------:|:--------------:|:----------------------------------------------------------------------------------------------------------:|
| Pokemon Class      | Richard        | Wrote stubs and implemented all methods, revised with Chat GPT.                                            |
| Pokemon Test Class | Richard        | Wrote all test methods                                                                                     |
| Item Class         | Gowri          | Wrote the Item class and its methods, and used ChatGPT to help with the code and fix errors.               |
| Item Test Class    | Gowri          | Drafted initial test methods and used Chat GPT to review and revise test cases.                            |
| Player Class       | Rushil         | Wrote the Player class and methods and reviewed with claude                                                |
| Player Test Class  | Rushil         | Ideated with claude and then implemented code                                                              |
| Room Class         | Richard        | Implemented first, added what was missing/looked over with Chat GPT                                        |
| Room Test Class    | Richard        | Implemented first, ensured good coverage with Chat GPT                                                     |
| Game Class         | Ishaan         | Iterated and planned with Claude before implementing, verifying, and ensuring fit with existing            |
| Game Test Class    | Ishaan         | Iterated and planned with Claude before implementing, verifying, and ensuring fit with existing            |
| Battle Class       | Ishaan         | Iterated and planned with Claude before implementing, verifying, and ensuring fit with existing            |
| Battle Test Class  | Ishaan         | Iterated and planned with Claude before implementing, verifying, and ensuring fit with existing            |
| LinkedChain<T>     | Rushil         | Wrote the generic LinkedChain class with methods for adding, removing, searching, and retrieving elements. |


## Big-O: Game and Battle

p = number of Pokemon the player owns, b = number of item stacks in the bag (only Poke Balls, Potions and X Attacks, so b ≤ 3), t = number of commands typed.

| Method | Big-O | Spec estimate | Matches? |
|---|---|---|---|
| `Game(Scanner, Random)` | O(1) | not given | — |
| `main` / `run()` | O(t · p²) | O(t) | No: each command can cost up to O(p²), not O(1) |
| `processCommandString` – matching the word | O(1) | O(n) | Better: fixed number of commands, checked with if/else |
| ↳ `explore` | O(1) (3 rooms, `rooms.get(i)`) | — | — |
| ↳ `catch` | O(b²) = O(1) | — | — |
| ↳ `fight <n>` | O(p) (`getPokemon(n)`) | — | — |
| ↳ `attack` | O(p²) (checks if all Pokemon fainted) | — | — |
| ↳ `special`, `buff` | O(p²) (checks if all Pokemon fainted) | — | — |
| ↳ `heal`, `power` | O(b² + p²) | — | — |
| ↳ `view` | O(p² + b²) | — | — |
| ↳ `run`, `help`, `quit` | O(1) | — | — |
| `getPlayer`, `getWildPokemon`, `getCurrentRoom`, `isInBattle`, `isGameOver` | O(1) | — | — |
| `Battle(Pokemon, Pokemon)` | O(1) | O(1) | Yes |
| `Battle.attack` | O(1) | O(1) | Yes |
| `Battle.isOver` | O(1) | O(1) | Yes |
| `Battle.runTurn` | O(1) | O(1) | Yes |
| `Battle.specialAttack` | O(1) | not in spec | — |
| `Battle.boost`, `getBoostTurns`, `hasUsedSpecial` | O(1) | not in spec | — |

**Why O(p²)?** Looping over the collection with `getPokemon(i)` calls `LinkedChain.get(i)`, which walks i nodes, so 0 + 1 + … + (p−1) = O(p²). The spec assumed each command was cheap, but `view` and the game-over check both loop like this.
