import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//Nathan Zimet
//no functions have any error checking.
//the file must exactly match expected input

//Sources NOT ChatGPT wowa wowa I'm using Liang - Intro to Java

public class TerrainMap {

    public String terrainFile;
    public static final int MAP_SIZE = 256;
    public static final int TERRAIN_MAP_SIZE = 64;
    public static int[][] terrainMap = new int[TERRAIN_MAP_SIZE][TERRAIN_MAP_SIZE];

    public static ArrayList<Line> lines = new ArrayList<>();
    public static ArrayList<Point> points = new ArrayList<>();

    public TerrainMap(String terrainFile) {
        this.terrainFile = terrainFile;
        try {
            this.loadMap();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMap() throws FileNotFoundException {
        java.io.File file = new java.io.File(terrainFile);
        Scanner input = new Scanner(file);
        for (int i = 0; i < TERRAIN_MAP_SIZE; i++) {
            for (int j = 0; j < TERRAIN_MAP_SIZE; j++) {
                terrainMap[i][j] = input.nextInt();
            }
        }
    }

    /*–––––––––––––utilities–––––––––––––––––––*/
    public static double dist(Point a, Point b) {
        double temp = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        return Math.sqrt(temp);
    }

    public static class Point {
        double x = 0;
        double y = 0;

        public Point(double x, double y) {
            this.x = x < 0 ? 0 : (x >= TerrainMap.MAP_SIZE ? TerrainMap.MAP_SIZE - 1 : x);
            this.y = y < 0 ? 0 : (y >= TerrainMap.MAP_SIZE ? TerrainMap.MAP_SIZE - 1 : y);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public Boolean inWall() {
            return TerrainMap.terrainMap[(int) (y * TERRAIN_MAP_SIZE / MAP_SIZE)][(int) (x * TERRAIN_MAP_SIZE / MAP_SIZE)] == 1;
        }
    }


    //Defines start and end points with parametric linear equation
    //tstep is 1 terrain unit
    public static class Line {
        Point p1;
        Point p2;
        double t = 0;
        double tstep;
        boolean drawn = false;

        public Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            tstep = (1.0 / (dist(p1, p2) * TERRAIN_MAP_SIZE / MAP_SIZE));
        }

        public Point generateNextPoint() {
            if (t > 1)
                drawn = true;
            Point p = new Point((p1.x + ((p2.x - p1.x) * t)), (p1.y + ((p2.y - p1.y) * t)));
            t += tstep;
            return p;
        }

        public Point getOppositePoint() {
            double tPrime = t * -1;
            return new Point((p1.x + ((p2.x - p1.x) * tPrime)), (p1.y + ((p2.y - p1.y) * tPrime)));
        }

        public void reset() {
            t = 0;
            drawn = false;
        }


        public static Line makeLeftBisector(Point A, Point B) {
            double Px = (B.x + A.x) / 2.0;
            double Py = (B.y + A.y) / 2.0;
            Point P = new Point(Px, Py);
            double Qx = P.x - P.y + A.y;
            double Qy = P.x - A.x + P.y;
            Point Q = new Point(Qx, Qy);
            //System.out.println(P);
            //System.out.println(Q);
            return new Line(P, Q);
        }

        public static Line makeRightBisector(Point A, Point B) {
            double Px = (B.x + A.x) / 2.0;
            double Py = (B.y + A.y) / 2.0;
            Point P = new Point(Px, Py);
            double Qx = P.y - A.y + P.x;
            double Qy = P.y - P.x + A.y;
            Point Q = new Point(Qx, Qy);
            //System.out.println(P);
            //System.out.println(Q);
            return new Line(P, Q);
        }

        public ArrayList<Point> getPath() {
            Path path = new Path(p1, p2);
            return path.path;
        }
    }

    //player does NOT ignore terrain
    //TODO: A MUST not be in a wall, add if statement
    // if A is in a wall, just return A -> B
    //TODO: B MUST not be in a wall, add if statement
    // if B is in a wall, find nearest point and call
    // makePath on those two points
    public static class Path {
        ArrayList<Point> path = new ArrayList<>();
        Point A;
        Point B;
        int rounding = MAP_SIZE / TERRAIN_MAP_SIZE;
        Line l;
        boolean hasLine = false;

        public Path(Point A, Point B) {
            this.A = A;
            this.B = B;
            makePath();
        }

        public void makePath() {
            path.add(A);
            makePath(A, B, 0, 0);
        }

        //direction:
        // 1 = left, 0 = none, -1 = right
        public void makePath(Point P, Point Q, int direction, int attempts) {

            if (attempts == 20) return;

            Line PQ = new Line(P, Q);
            Point T = PQ.generateNextPoint();

            while (!T.inWall()) {
                T = PQ.generateNextPoint();
                if (PQ.drawn) {
                    if (Math.abs(Q.x - path.get(path.size() - 1).x) < 2
                            || Math.abs(Q.y - path.get(path.size() - 1).y) < 2)
                        path.set(path.size() - 1, Q);
                    else path.add(Q);
                    return;
                }
            }

            Point p = T;
            while (T.inWall()) T = PQ.generateNextPoint(); //B must not be in wall
            Point q = T;

            Line pqBisectorLeft = Line.makeLeftBisector(p, q);
            Point t = pqBisectorLeft.generateNextPoint();

            //initially, no direction
            if (direction == 0) {
                direction = 1;
                Point tOpposite;
                while (t.inWall()) {
                    tOpposite = pqBisectorLeft.getOppositePoint();
                    if (!(tOpposite.inWall())) {
                        t = tOpposite;
                        direction = -1;
                        break;
                    }
                    t = pqBisectorLeft.generateNextPoint();
                }

                //going left
            } else if (direction == 1) {
                while (t.inWall()) {
                    t = pqBisectorLeft.generateNextPoint();
                }

                //going right
            } else { //direction == -1
                Line pqBisectorRight = Line.makeRightBisector(p, q);
                t = pqBisectorRight.generateNextPoint();
                while (t.inWall()) {
                    t = pqBisectorRight.generateNextPoint();
                }
            }

            t.x = Math.floor(t.x);
            t.y = Math.floor(t.y);

            makePath(P, t, direction, attempts + 1);
            makePath(t, Q, direction, attempts + 1);

        }

        public Point generateNextPoint() {
            if (path.size() == 1) {
                return B;
            }
            if (!hasLine) {
                l = new Line(path.get(0), path.get(1));
                l.tstep /= 2;
                hasLine = true;
            }

            if (l.t < 1) {
                return l.generateNextPoint();
            } else {
                path.remove(0);
                l.reset();
                hasLine = false;
            }

            return this.generateNextPoint();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        TerrainMap map = new TerrainMap("src/terrain2.txt");
        PathfindingDrawer view = new PathfindingDrawer(map);

        Point A = new Point(120, 200);
        Point B = new Point(150, 20);

        Line l1 = new Line(A, B);
        map.lines.add(l1);

        view.update();

        Path p = new Path(A, B);

        System.out.println(p.path.toString());

        points.add(A);

        for (int i = 0; i < 5000; i++) {
            points.set(0, p.generateNextPoint());
            view.update();
            TimeUnit.MILLISECONDS.sleep(17);
        }

    }
}
