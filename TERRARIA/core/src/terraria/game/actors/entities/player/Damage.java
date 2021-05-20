package terraria.game.actors.entities.player;

public enum Damage {

    MINIMAL_DAMAGE (0.25),
    SMALL_DAMAGE  (0.5),
    LOT_OF_DAMAGE (1),
    MAXIMUM_DAMAGE (2);

    private double numberOfdamage;

    Damage(double damage){
        this.numberOfdamage = damage;
    }

    public double getNumberOfdamage() {
        return numberOfdamage;
    }
}
