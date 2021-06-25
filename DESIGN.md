# Architecture

## UML

![image](https://user-images.githubusercontent.com/72931362/123392369-43435500-d59d-11eb-8286-d0fffb3423de.png)

![UML](/TERRARIA/core/assets/icons/icon_128.png)

## Packages structure

The first class called is TerrariaGame wich setup the window and directly call the MainMenuScreen class in the Screens package,

the project is then structured in 2 main packages:

* Actors : 
  * Entities    (Handle the player and all mobs)
  * Inventory   (handle the inventory and craft system)
  * World       (handle the map generator, define every tiles and define every operation from the player on the map)

* Screens       (handle each screens of the game and their inputs)

    
## Libraries
We are using Gradle to manage dependencies
