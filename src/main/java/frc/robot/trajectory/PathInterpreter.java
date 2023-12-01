package frc.robot.trajectory;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.drive.Vector2d;

import java.util.List;

public class PathInterpreter {

    private double percentageOnCurrentPath;

    private Path path;

    public Vector2d getMotorValues() {
        Point point = path.getPath().get(0);
        double x = point.getX();
        double y = point.getY();
        return new Vector2d(x, y);
    }

    public void generatePath() {
        List<Point> path = this.path.getPath();
        Point startingPoint = path.get(0);
        Point endingPoint = path.get(1);

    }
}
