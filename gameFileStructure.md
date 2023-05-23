
<h1>Game directory</h1>
<h3>Must contain:</h3>

- gamedirectory/game.info
- gamedirectory/*.difficulties
- gamedirectory/*.goals
- gamedirectory/*.paths
- gamedirectory/*.player

<h3>Can contain:</h3>

- gamedirectory/*.enemies
- gamedirectory/*.items
- gamedirectory/*.sprites
- Image files

<h1>game.info</h1>
<p>Contains file paths of every file that's part of the game.<br/>
File paths are relative from the game directory</p>
<h3>Must contain:</h3>

- (...).difficulties
- (...).goals
- (...).paths
- (...).player

<h3>Can contain:</h3>

- (...).enemies
- (...).items
- (...).sprites

<h1>actions</h1>
<p>Actions don't have their own file, but are defined within various other files for various reasons<br/>
Items can be referenced here by their name<br/>
There are 4 types of actions, each can be defined as follows:</p>
- {Gold: [int]}
- {Health: [int]}
- {Inventory: [Item]}
- {Score: [int]}


<h1>difficulties</h1>
<p>Contains a list of every difficulty in the game.<br/>
Difficulties are separated by each line, and can be any text string.<br/>
Blank lines are ignored.<br/>
Must have at least 1 difficulty.</p>

<h1>enemies</h1>
<p>Contains a list of every enemy in the game.<br/>
Items can be referenced here by their name<br/>
Enemies are separated by a blank line, and are defined as follows:</p>

- !Enemy Name (Required)
- -["Basic" | "Vampire"] (Required, without quotes)
- Health: [int] (Required)
- Score: [int]
- Gold: [int]
- Inventory: [Item, Item, ...]
- Weapon: [Item] (Must be a weapon) (Will be added to inventory upon enemy creation)
- Drop Chance: [double]
- Drop Weapon: [boolean]
- Escape Chance: [double]

<h1>goals</h1>
<p>Contains a list of goals for every difficulty in the game.<br/>
If a difficulty has no goals, it will not be playable.<br/>
Items & difficulties can be referenced here by their name<br/>
Goals are defined for each difficulty, with each difficulty being separated by a blank line.<br/>
There are 4 types of goals, each can be defined as follows:</p>

- #Difficulty
- Gold: [int]
- Health: [int]
- Inventory: [Item]
- Score: [int]

<h1>items</h1>
<p>Contains a list of every item in the game.<br/>
Items are separated by a blank line, and are defined as follows:</p>

- #Item Name (Required)
- -["Basic" | "Weapon" | "Usable"] (Required, without quotes)
- Description: [int] (Required)
- Damage: [int] (Required) (Only for "Weapon")
- On Use: [Action] (Required) (Only for "Usable")

<h1>paths</h1>
<p>Contains the story of the game<br/>
Items & enemies can be referenced here by their name.<br/>
The story is defined as a list of passages, each separated by a blank line<br/>
The first passage will be the opening passage, and at least 1 passage is required.<br/>
The complete file structure is as follows:</p>

- Story title (Required)
-
- ::Opening passage name (Required)
- Passage content (Required)
- more passage content
- [Link Text] (Link reference) {Action}
- [Another link Text] (Another link reference) {Another action}
- !Enemy name
- !Another enemy name
-
- ::Another passage name
- Passage content
- more passage content
- [Link Text] (Link reference) {Action}
- [Another link Text] (Another link reference) {Another action}
- !Enemy name
- !Another enemy name
- 
- ...

<h1>goals</h1>
<p>Contains a player for every difficulty in the game.<br/>
If a difficulty has no player, it will not be playable.<br/>
Items & difficulties can be referenced here by their name<br/>
A player is defined for each difficulty, with each difficulty being separated by a blank line.<br/>
The player can be defined as follows:</p>

- #Difficulty
- Health: [int] (Required)
- Score: [int]
- Gold: [int]
- Inventory: [Item, Item, ...]
- Weapon: [Item] (Must be a weapon) (Will be added to inventory upon player creation)

<h1>sprites</h1>
<p>Contains a the file path for the sprite of every object in the game that has one<br/>
Sprites are separated by each line. Blank lines are ignored.<br/>
Any object can be referenced here by their name.<br/>
This only does something for enemies & passages.<br/>
Paths are relative to the game directory.<br/>
The sprites can be defined as follows</p>

- [Object name] : [Sprite path]