package model;

import java.util.ArrayList;

public class Model {
    public int pointCount;
    public ArrayList<Point>p;

    public Model(){
    this.pointCount = pointCount;
    this.p = new ArrayList<Point>();
    }

    public int getPointCount() {
        return p.size();
    }

    public void addPoint(Point point) {
        p.add(point);
    }

    public void removePoint(int x, int y) {
        for (int tempX = x - 3; tempX < x + 4; tempX++) {
            for (int tempY = y - 3; tempY < y + 4; tempY++) {
                int finalTempX = tempX;
                int finalTempY = tempY;
                p.removeIf(point -> point.getX() == finalTempX && point.getY() == finalTempY);
            }
        }
    }

    public Point getPoint(int i){
        return this.p.get(i);
    }

    public  void  deleteArray()
    {
        p.clear();
    }

    public int searchPoint(int x, int y){
        int index = -1;
        for (int i = 0; i < this.p.size(); i++){
            if (this.p.get(i).x == x && this.p.get(i).y == y) index = i;
        }
        return index;
    }
}
