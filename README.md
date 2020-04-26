# Treasure Hunt (Battleship-Project)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](https://github.com/sirimykland/Battleship-Project/blob/master/LICENCE)

The educational requirements of this project is for the group to learn all the phases of
developing a software architecture, this includes design, evaluation, implementation, testing,
and iterations.

The group has decided to create a turn based online multiplayer game with a focus on the
quality attributes modifiability and usability. Well known architectural principles will be utilized
to achieve this goal. 

## Game concept

The game is a special take on Battleship. The original game, played with pencil and paper,
dates back to World War I. Since then, it has appeared in several different formats, such as
pencil and paper, boardgame, and as a computer game released in 1979.
Battleship is played with two players. Each player has two grids, usually 10x10 in size. The
first grid is used to place the fleet consisting of ships of different sizes. The second grid is
used for taking shots on the opposing player’s fleet. The players take turns taking shots, and
the shots are marked on the second grid either as a “hit” or “miss”, based on the positioning
of the opponent’s fleet. When all the squares of a ship are shot, the shooter gets informed
that the ship has been eliminated. The first player to eliminate all the opposing ships wins
the game.

![Your board in-game](https://i.imgur.com/K6zs2lP.png)
![Opponent's board in-game](https://i.imgur.com/yvsQeFR.png)


### Our take on the concept
The group has chosen to change the theme of the game, but keep all the rules from the original Battleship game. The new theme will be treasure hunt, which will see ships replaced with buried treasure and weapons replaced with equipment used to dig up these treasures. To add something new to the game, there will be different types of equipment available for looking for your opponents treasure. Special features will be added to the different types of equipment. A suggestion for one such feature is equipment that hit more than one square or equipment that will search an area for treasure without digging it up, like a metal detector. In other words, there are a lot of possibilities for what types of equipment that can be added to the game. The pictures above show screenshots of Treasure Hunt, where the treasures are revealed when all squares are hit and missed attempts are market with red. 

## Development

### Version Control: Git
We use our own version of the [Git Flow](http://nvie.com/posts/a-successful-git-branching-model/) strategy for handling branches and releases. We follow this strictly to avoid odd bugs and unknown state in our production code.

#### Master Branch: `master`

We consider `master` to be the main branch where the source code of `HEAD` **always reflects a stable application**. This is the branch from which all `feature` and `hotfix` branches are forked, and into which all `feature` and `hotfix` branches are merged.

Changes to `master` should always come through a pull request.

#### Feature Branches: `feature_*`

Feature branches are used to develop new features. The essence of a feature branch is that it exists as long as the feature is in development, but will eventually be merged back into master (to definitely add the new feature to the upcoming release) or discarded (in case of a disappointing experiment). Basically, all feature branches are eventually pruned.

Feature branches should not exist in `origin` for very long. They should be merged into `master`, via pull request, as quickly as possible and then cleaned up.

- Naming convention: `feature_*`
- Branches from: `master`
- Must merge back into: `master`

#### Hotfix Branches: `hotfix_*`

Hotfix branches arise from the necessity to act immediately upon an undesired state of the master branch. When a critical bug must be resolved immediately, a `hotfix` branch must be branched from the current `master`.

- Naming convention: `hotfix_*`
- Branches from: `master`
- Must merge back into: first `master`

### Linting: Set up Android Studio for linting
In order to be able to increase consistency among the code base, kotlin linting rules are enforced when creating a pull request. 

#### Setup rules in Android Studio/IntelliJ
Go to <kbd>File</kbd> -> <kbd>Settings...</kbd> -> <kbd>Editor</kbd>
- <kbd>General</kbd> -> <kbd>Auto Import</kbd>
  - check `Optimize imports on the fly (for current project)`.
- <kbd>Code Style</kbd> -> <kbd>Kotlin</kbd>
  - <kbd>Set from...</kbd> -> <kbd>Predefined style</kbd> -> <kbd>Kotlin style guide</kbd> (Kotlin plugin 1.2.20+).
  - open <kbd>Code Generation</kbd> tab
    - uncheck `Line comment at first column`;
    - select `Add a space at comment start`.
  - open <kbd>Imports</kbd> tab
    - select `Use single name import` (all of them);
    - remove `import java.util.*` from `Packages to Use Import with '*'`.
  - open <kbd>Blank Lines</kbd> tab
    - change `Keep Maximum Blank Lines` / `In declarations` & `In code` to 1 and `Before '}'` to 0.
  - (optional but recommended) open <kbd>Wrapping and Braces</kbd> tab
    - uncheck `Method declaration parameters` / `Align when multiline`.     
  - (optional but recommended) open <kbd>Tabs and Indents</kbd> tab
    - change `Continuation indent` to the same value as `Indent` (4 by default).   
- <kbd>Inspections</kbd> 
  - change `Severity` level of `Unused import directive` and `Redundant semicolon` to `ERROR`.

#### Ktlint command line tool
Another option is to install [ktlint](https://ktlint.github.io/) and run ```ktlint -F``` from the command line.

### Database
The project uses [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore) to store imporant game data which is structured in the following manner: 

#### Games collection
This collection will contain one document for each game with a randomly generated ID. Each document created using the application currently contain the following fields: 
- **player1Id:** The ID of player 1.
- **player1Name:** The name of player 1.
- **player2Id:** The ID of player 2. 
- **player2Name:** The name of player 2.
- **playerLeft:** An ID of a player who has left the game before it ended. 
- **winner:** The winner of the game.
- **treasures:** A map with the userId as key and a list of treasures as value:
    - **rotate:** Boolean signalling if the treasure is rotated or not in the view.
    - **type:** Which type of treasure this is.
    - **x:** The x-coordinate where the treasure is located.
    - **y:** The y-coordinate where the treasure is located.
- **moves:** A list of maps, where each map contain the following information about the move: 
    - **playerId:** The ID of player who made the move
    - **x:** The x-coordinate of the move
    - **y:** The y-coordinate of the move

##### Example data from a test game:

![Firebase example data overall](https://i.imgur.com/M3pwjP1.png)

*Treasures*:

![Firebase example data treasures](https://i.imgur.com/NKaW0fT.png)

*Moves*:

![Firebase example data moves](https://i.imgur.com/mPaTFkr.png)

### Overall logical view

![Overall logical view](https://i.imgur.com/4Lrrxsw.png)

### GUI Entity Component System logical view

![GUI Entity Component System logical view](https://i.imgur.com/P056rXg.png)

### Development View

![Development View ](https://i.imgur.com/WHCXbO0h.png)
