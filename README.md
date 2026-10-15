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
5. Use commands like explore, catch, view, help, and quit.

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
All our main classes remained the same, however we left out some features 
like the full battle system and multiplayer for now. We also added methods 
like getPokemon() and getItemAmount() to help the classes work together. 
Each class handles its own input validation, and we added test cases. This 
helped us create the basic game working. We decided to keep our basic classes 
the same based on group discussion and we added methods with the 
suggestion from Chat GPT. 
 
What changed in the LinkedChain? 

Anything you added to or modified in the provided class and why your 
application needed it, or that you used it unchanged. The only thing that 
changed in Linked Chain was the addition of a toString method that allows us 
to correctly display information to the user by properly iterating through 
each node within a room whether it be a pokemon or item node.

What happened to your unknowns? 

Of the things you listed in your scope as not knowing how to do, which did 
you figure out, and which are still open? Some of our unknowns were completely 
solved. Specifically implementing the exploration aspect of the game.
A random pokemon can appear in any room which was a major aspect to the 
realism of a pokemon game. This gave us a hint as to what we should do for 
the full implementation of the exploration game element. Furthermore, 
implementing most of our MVP allows us to see the more intricate parts that 
were missed in the spec. We still need to implement two-player mode and 
connect the battle system to the game. 

What doesn't work yet? 

MVP features, bad-input cases, or tests that are missing or failing in this 
prototype. Most, if not all of the bad input is being taken care of at least 
at an incremental level. The missing element to our MVP is the movement 
aspect in the fact that the player cannot move from room to room yet and can 
only encounter wild pokemon. Players can currently catch Pokémon, but the 
battle system is not fully implemented yet. 

NEXT STEPS

What is left to finish the MVP? 

The remaining features and fixes, in the order you will do them. We still 
need to finish the basic battle system so players can fight Pokemon found in 
the wild and the game can end when all their Pokemon faint. After that, we’ll 
test the full game to make sure exploring, catching, battling, and viewing 
collections all work together. Finally, we’ll go through the entire program 
and fix any remaining bugs and ensure all our JUnit tests pass. 

Which stretch goals will you attempt? 

The ones from your scope you are committing to, and the ones you are dropping. 
Advanced combat and local multiplayer become more realistic with each day of 
implementation. Our team will be attempting both of these stretch goals.
What new tests will you need? The methods and bad-input cases the added work 
must cover. Future tests will mainly have to do with combat. As this is 
implemented, more tests will need to be added to existing classes to keep 
up with the growing logic. This includes things like pokemon being able to 
take and deal damage, reading when a pokemon can or cannot be captured using 
pokeballs, and how exactly pokemon and items will interact with each other.

Who does what? 

Which team member owns each remaining item. Each of us will keep working on 
our assigned classes. Ishaan will handle Game and integrating the battle 
system, Rushil will work on Player and inventory, Richard will work on 
Pokémon and Room, and Gowri will handle Item and related tests. We will all 
help test the final game and fix any remaining issues.

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
| LinkedChain<T>     | Rushil         | Wrote the generic LinkedChain class with methods for adding, removing, searching, and retrieving elements. |

