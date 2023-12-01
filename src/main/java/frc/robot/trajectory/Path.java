package frc.robot.trajectory;

import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Point> path = new ArrayList<>();

    public void addPointToEnd(Point point) {
        path.add(point);
    }

    public void addPoint(int index, Point point) {
        path.add(index, point);
    }

    public void removeReachedPoint() {
        path.remove(0);
    }

    public List<Point> getPath() {

        return path;
    }

}
