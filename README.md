
# 🎲 Lucky 9 - Java CLI Card Game

Lucky 9 is a fully interactive, terminal-based card game written in Java. It's inspired by traditional Filipino gambling mechanics and built with a focus on clean OOP design, modular logic, and a polished CLI experience.

---

## 📦 Features

- 🂠 Dynamic deck creation using enums for suits and ranks  
- 👤 Dealer and player logic with balance tracking and bet deduction  
- 💵 Auto-generated, randomized betting per round  
- 🧮 Game resolution with Lucky 9 detection and pot distribution  
- 🏆 Leaderboard ranking based on dealer take-outs  
- ♻️ Replayable game loop with player elimination  
- 🚫 Input validation and robust exception handling  
- ✅ Uses Java Streams, predicates, and collectors where it matters  

---

## 📁 Project Structure

   ```bash
   project/Lucky9/
   ├── Card.java               → Represents a single card (suit, rank, value)
   ├── Deck.java               → Manages the deck and shuffling
   ├── Player.java             → Player structure and hand logic
   ├── Dealer.java             → Inherits Player, handles cash flow and rankings
   ├── LeaderBoard.java        → Displays ranked dealer earnings
   ├── PlayerController.java   → Game rules and card value computations
   ├── GameManager.java        → Controls game flow, bets, and player interactions
   └── Test.java               → Entry point for the game loop

   ```

## 🚀 How to Run

1. Clone this repo or copy the source files.  
2. Compile everything via terminal:
   ```bash
   javac project/Lucky9/*.java

3. Run the game:

   ```bash
   java project.Lucky9.Test
   ```
4. Play straight from your console. Bet, draw cards, challenge the dealer, and top the leaderboard.

---

## 🎖️ Game Rules Summary

* All players and the dealer start with a fixed amount of money.
* Each round, players draw up to three cards — total card value is calculated modulo 10.
* The best score is **9**, also called a “Lucky 9”.
* If multiple players hit 9, it results in a draw.
* The dealer collects money from players who lose and distributes winnings if they lose.
* Broke players are automatically removed in the next round.

---

## 🧠 Technologies Used

* Java 17+
* Object-Oriented Programming
* Java Stream API
* Functional Interfaces
* Defensive programming and exception handling

---

## About Me :)

Hey, I’m **Jam** — an aspiring dev. 👋

---

## 📌 Notes

This game was built for a CLI-based academic requirement. Future updates might include:

* GUI or REST-based version
* Persistent leaderboard tracking
* Thematic or visual upgrades

---
