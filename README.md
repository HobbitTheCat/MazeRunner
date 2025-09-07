# Labyrinth Game in Java

## 📌 About the Project
This project is a **Java-based labyrinth game**, developed as part of the 1st-year computer science course.  
The main goal was to design the game in a **fully object-oriented style**, with modular classes for the map, player, cells, items, and game loop.

Key features:
- Procedural **map generation** with probability-based distribution of mines and tools.
- **Player mechanics**: movement, collecting items, using tools, throwing grenades.
- **Game objects**: energy sources, grenade boxes, mines, tools.
- **Visualization**:
  - Console rendering of the labyrinth.
  - Export to Notepad++ with Unicode characters.
  - Animations for actions (breaking walls, explosions, discoveries).
- **Score system** with saving and loading past results.

---

## 🏗 Object-Oriented Architecture

The game is structured into classes, each with clear responsibilities:

- **`Map`** – generates the labyrinth, places objects with probabilities.  
- **`Cell`** – represents each tile (walls, visibility, contents, neighbors).  
- **`Player`** – manages position, energy, grenades, actions, victory/defeat state.  
- **`ExtraTools` (and subclasses)** – tools such as:
  - Discharge Unit, Excavator, Long-range detectors, Mine detector, etc.  
- **`Mine`** – mines with defusing logic.  
- **`EnergySource` / `GrenadeSource`** – collectible items.  
- **`Game`** – the main loop, handles gameplay logic.  
- **`Output`** – visualization and animations, score saving/loading.

A more detailed description of the project is available in this document: [Rapport.pdf](https://github.com/HobbitTheCat/MazeRunner/blob/main/Docs/Rapport.pdf)

---

## 🎮 Gameplay
- The player explores a randomly generated labyrinth.  
- Mines are more likely to appear near the edges, tools closer to the center.  
- The player can:
  - Move in four directions,
  - Break walls,
  - Pick up and use tools,
  - Throw grenades to clear paths.  
- The game ends in **victory** (exit reached) or **defeat** (mine explosion).

---

## 🚀 Installation & Running
### Requirements
- Java 17+
- Notepad ++ (Optional)
- Any IDE

### Compile & Run
```bash
javac *.java
java Main
```

### For output in Notepad (with animations)
```java Main.java
public static void forPresentation() {
  Map m1 = nwe Map(height, width, numberOfMine, numberOfGrenadeSource, numberOfEnergySource, amountEnergy, amountGrenade);
  Player p1 = new Player(m1, maxEnergy, maxGrenade);
  Game g1 = new Game(p1, m1, PATH_OF_OUTPUT_FILE.TXT);
  g1.play(true);
}
```

---

## ⚠️ Known Limitations
- Some error handling is done via return strings instead of exceptions.
- No dedicated Direction class (directions are represented as strings).

---

## 📈 Possible Improvements
- Implement proper exception handling (try/catch).
- Extend visualization (e.g., JavaFX GUI).
- Expand the game's functionality
- Add multiplayer

---

## 👥 Author
[Semenov Egor](https://github.com/HobbitTheCat) - Design, implementation, documentation.

---

## 📂 Documentation
[Rapport](https://github.com/HobbitTheCat/MazeRunner/blob/main/Docs/Rapport.pdf)
[Class diagram](https://github.com/HobbitTheCat/MazeRunner/blob/main/Docs/Diagramme%20de%20classes.pdf)
[Test batch in console mode](https://github.com/HobbitTheCat/MazeRunner/blob/main/Docs/Jeux%20d'essais.txt)
