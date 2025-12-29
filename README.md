# Hangman Game 
## Description
This project is a multi-level cinematic Hangman game developed in JavaFX, designed to combine suspenseful gameplay, storytelling, and interactive learning. The game begins by asking the user to enter the names of three beloved ones, then launches a cinematic video to immerse the player. The gameplay is divided into three levels: Level One challenges the user to guess a word within a limited number of tries while navigating true and false hints; Level Two introduces a creepy video and sound effects to intimidate the player while they guess under a strict timer; Level Three misleads the user with distracting words and a false countdown, testing both knowledge and focus. Each level ends with dramatic feedback reflecting the outcome of the game, making this an immersive and psychological experience beyond standard Hangman.

---

## Game Overview & Levels
Introduction:
- User enters the names of three loved ones.
- A cinematic video plays, showing happy children before a sudden dramatic shift in tone (screams, suspense).

Level One:
- Guess a word with 6 hidden chances (unknown to the user).
- A 50-second timer counts down.
- Every few seconds, two hints appear: one correct, one misleading.
- Upon losing: a dramatic message shames the user.
- Upon winning: a teasing message indicates the journey is not over.

Level Two:
- A creepy video and sound effects play in the background, creating tension.
- Unlimited guesses: the user must identify the word before time runs out.
- Dramatic win/lose message is displayed after the timer ends.

Level Three:
- Narrative warning: “Welcome to the place where your own brain betrays you.”
- The word to guess is biomedical, but distracting math and physics words appear randomly on screen.
- Timer shows 90 seconds counting down, but in reality, the user has only 45 seconds to guess the word.
- After the level, a final cinematic message plays, delivering the reflective conclusion.

---

## Features
- Cinematic storytelling with videos and sound effects
- Multi-level Hangman game with suspenseful twists
- Dynamic hints: true and false hints to challenge the user
- Timers with psychological effects (intimidation, false countdowns, dramatic alerts)
- Immersive experience combining gameplay, narrative, and reflection
- Personalized gameplay with names of loved ones
- Final reflective message on knowledge, learning, and responsibility

---

## Technologies Used
- Java
- JavaFX (GUI, animations, media playback)
- MediaPlayer / MediaView for video integration
- Timeline, FadeTransition, and other animations for effects

---

## Project Structure
Hangman-Game/
│
├─ src/                 # Java source files for each level and main game
│  ├─ LevelOne.java
│  ├─ LevelTwo.java
│  ├─ LevelThree.java
│  ├─ Ending.java
│  └─ MyJavaFX.java      # Main launcher
│
├─ media/               # All video and audio assets
│  ├─ g1.mp4
│  ├─ g2.mp4
│  ├─ p2.mp4
│  └─ p3.mp4
│
└─ README.md            # Project documentation

---

## Learning Outcomes
- Building multi-level interactive games using JavaFX
- Implementing timers, animations, and media playback
- Designing suspenseful gameplay mechanics (hints, intimidation, false timers)
- Creating a personalized and immersive user experience
- Applying concepts of GUI design, event handling, and game logic

---

## Optional Features / Future Improvements:
- Additional levels with more complex word challenges
- Adaptive difficulty based on user performance
- Enhanced video and sound effects integration
- Graphical representation of lives, hints, and score

---

## Contact:
- GitHub: [github.com/christinakhoury](https://github.com/christinakhoury)


