import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//no functions have any error checking.
//the file must exactly match expected input

//Sources NOT ChatGPT wowa wowa I'm using Liang - Intro to Java

public class TerrainMap {

    public String terrainFile;
    public static final int MAP_SIZE = 256;
    public static final int TERRAIN_MAP_SIZE = 8;
    public static int[][] terrainMap = new int[TERRAIN_MAP_SIZE][TERRAIN_MAP_SIZE];

    public ArrayList<Line> lines = new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();

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

    public static class Line {
        Point p1;
        Point p2;
        double t = 0;
        double tstep;
        boolean drawn = false;

        public Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            tstep = 1.0 / (dist(p1, p2) * TERRAIN_MAP_SIZE / MAP_SIZE);
        }

        public Point generateNextPoint() {
            if (t > 1)
                drawn = true;
            Point p = new Point((p1.x + ((p2.x - p1.x) * t)), (p1.y + ((p2.y - p1.y) * t)));
            t += tstep;
            return p;
        }

        public Point getPointAtT(double tPrime) {
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

        public Point getNearestGroundPoint() {
            this.reset();
            Point T = this.generateNextPoint();
            while (T.inWall()) {
                T = this.generateNextPoint();
            }
            return T;
        }

        public ArrayList<Point> getPath(Point A, Point B) {


            return null;
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

        public Path(Point A, Point B) {
            this.A = A;
            this.B = B;
        }

        public void makePath(Point P, Point Q) {
            makePath(P, Q, 0);
        }

        public void makePath(Point P, Point Q, int direction) {

            path.add(P);
            Line PQ = new Line(P, Q);
            Point T = PQ.generateNextPoint();
            while (!T.inWall()) T = PQ.generateNextPoint();
            if (PQ.drawn) {
                path.add(Q);
                return;
            }
            Point p = T;
            while (T.inWall()) T = PQ.generateNextPoint(); //B must not be in wall
            Point q = T;

            Line pqBisectorLeft = Line.makeLeftBisector(p, q);
            Line pqBisectorRight = Line.makeRightBisector(p, q);

            Point t = pqBisectorLeft.generateNextPoint();



        }

    }

    public static void main(String args[]) {
        TerrainMap map = new TerrainMap("src/terrainsmall.txt");
        PathfindingDrawer view = new PathfindingDrawer(map);

        Point A = new Point(50, 230);
        Point B = new Point(120, 30);

        Line l1 = new Line(A, B);
        map.lines.add(l1);
        map.lines.add(Line.makeLeftBisector(A, B));

        for (Line l : map.lines) {
            int i = 0;
            while (l.drawn == false) {
                Point p = l.generateNextPoint();
                if (!p.inWall()) map.points.add(p);
                i++;
            }
            System.out.println("drew " + i + " points");
        }

        view.update();

    }
}
