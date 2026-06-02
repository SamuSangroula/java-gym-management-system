# Gym Membership Manager

A small Java project demonstrating basic object-oriented design and a simple graphical user interface for managing gym members.

Overview

- Purpose: Demonstrates inheritance and GUI interaction by modeling gym members (regular and premium) and displaying them via a simple GUI.
- Language: Java (no external libraries required).

Project structure

- `GymGUI.java`: Simple graphical interface and entry point. Displays member information and provides basic controls.
- `GymMember.java`: Base class representing a gym member with common attributes and methods.
- `RegularMember.java`: Subclass representing a regular gym member with any specific behavior or fields.
- `PremiumMember.java`: Subclass representing a premium gym member with additional properties or benefits.

Prerequisites

- Java JDK 11 or later installed and available on your `PATH`.

Build and run

1. Compile the sources:

```powershell
javac *.java
```

2. Run the GUI:

```powershell
java GymGUI
```
