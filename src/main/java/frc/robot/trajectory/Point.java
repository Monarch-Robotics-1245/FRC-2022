package frc.robot.trajectory;

public class Point {
    private double x;
    private double y;
    private double entranceDirection;
    private double exitDirection;

    public Point(double x, double y, double entranceDirection, double exitDirection) {
        this.x = x;
        this.y = y;
        this.entranceDirection = entranceDirection;
        this.exitDirection = exitDirection;
    }

    public Point(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.exitDirection = direction;
        this.entranceDirection = direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getEntranceDirection() {
        return entranceDirection;
    }

    public double getExitDirection() {
        return exitDirection;
    }
}
