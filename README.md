# Battleship-Project

## Set up Android Studio for linting

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

### Description of the project and this phase (architectural phase)

The educational requirements of this project is for the group to learn all the phases of
developing a software architecture, this includes design, evaluation, implementation, testing,
and iterations. This document will describe some of these phases along with the
architectural description of the product.
The group has decided to create a turn based online multiplayer game with a focus on the
quality attributes modifiability and usability. Well known architectural principles will be utilized
to achieve this goal. The architectures and patterns that will be used in this project is
described below. How they are implemented will however be explained at a later phase.

### Description of the game concept

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
![Screenshot from http://en.battleship-game.org/](https://i.imgur.com/VNyLuF0.png)


To add something new to the game, power-ups will be added to both ships and shots.
Suggestions for these power-ups are shots that hit more than one square, or specific
squares on a ship that automatically eliminates it. An economic system where the players
add ships and weaponry based on a budget is also being considered.

### Logical View

![Logical View ](https://i.imgur.com/XQyIilb.png)

### Development View

![Development View ](https://i.imgur.com/ENkQcNF.png)
