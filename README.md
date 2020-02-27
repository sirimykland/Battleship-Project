# Battleship-Project
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

### Version Control: Git
You'll need to familiarie yourself with the basics in order to work with, and contribute to this project.

- Overall Introductions:
  * [Great overall explanation](https://betterexplained.com/articles/aha-moments-when-learning-git/)
  * [In-browser interactive tutorial](https://try.github.io/levels/1/challenges/1) (from GitHub)
  * [Git's official 'get started' docs](https://git-scm.com/documentation) (surprisingly good!)

- Relevant Specific Deeper Dives:
  * [Remotes](https://git-scm.com/book/en/v2/Git-Basics-Working-with-Remotes)
  * [Remote Branches](https://git-scm.com/book/en/v2/Git-Branching-Remote-Branches) 

### Git Strategy: Git Flow

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

##### Creation: 

Here's an example of creating a new `feature_headerbar` branch that tracks the branch `origin/master`:

```sh
> git checkout -b feature_headerbar origin/master
Switched to a new branch "feature_headerbar"
```

##### Merging: 

Push your feature branch (`feature_headerbar`) up to Github.

```sh
> git push origin feature_headerbar
```

Then open a pull request on GitHub, attempting to merge your `feature` branch into `master`. **Note: Feature branches always merge into `master`.**

Once successfully merged into `master`, you can safely delete the feature branch on GitHub as well as from your local branches.

```sh
> git branch -D feature-headerbar
Deleted branch feature-headerbar (was 05e9557).
```

#### Hotfix Branches: `hotfix_*`

Hotfix branches arise from the necessity to act immediately upon an undesired state of a live production version. When a critical bug in a production version must be resolved immediately, a `hotfix` branch must be branched from the current `master`.

- Naming convention: `hotfix_*`
- Branches from: `master`
- Must merge back into: first `master`

##### Creation:

Here's an example of creating a new `hotfix_button_bug` branch that tracks the branch `origin/master`:

```sh
> git checkout -b hotfix_button_bug origin/master
Switched to a new branch "hotfix_button_bug"
```

##### Merging:

Push your feature branch (`hotfix_button_bug`) up to Github.

```sh
> git push origin hotfix_button_bug
```

Then open a pull request on GitHub, attempting to merge your hotfix branch (`hotfix_button_bug`) into `master`.

Once successfully merged into `master`, you can safely delete your `hotfix_button_bug` branch on GitHub as well as from your local branches.

### <a name="pulls"></a>Merging Code: Pull Requests

All merges from `feature` and `hotfix` branches into the `master` branch need to be made through pull requests. Doing this accomplishes several important goals:

- Information dissemination across a larger swath of the team.
- Increased familiarity with the expanding codebase.
- Ensuring code quality / consistency.

For these reasons we ask that all merges be made by opening a new pull request and tagging someone for review! Thanks in advance!

### Set up Android Studio for linting
In order to be able to increase consistency among the code base, kotlin linting rules are enforced when creating a pull request. To setup enforcement of these rules on your local machine, follow the steps below.

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

### Description of the architectural phase

The educational requirements of this project is for the group to learn all the phases of
developing a software architecture, this includes design, evaluation, implementation, testing,
and iterations. This document will describe some of these phases along with the
architectural description of the product.
The group has decided to create a turn based online multiplayer game with a focus on the
quality attributes modifiability and usability. Well known architectural principles will be utilized
to achieve this goal. The architectures and patterns that will be used in this project is
described below. How they are implemented will however be explained at a later phase.

### Game concept

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
[Link to draw.io](https://www.draw.io/#G1Q8YDrOI8EXfJwTWOScuIR0LGAb1X6k4Z)

![Logical View ](https://i.imgur.com/XQyIilb.png)

### Development View
[Link to draw.io](https://www.draw.io/#G11CwfNnBh6LyNHOcgmTlTsBnQNeapD7c8)

![Development View ](https://i.imgur.com/ENkQcNF.png)
