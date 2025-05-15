import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.sqrt;

//Nathan Zimet

//size is radius

public class MapWithoutPartitionTest {

    final int MAP_SIZE = 256;
    ArrayList<Minion> minions = new ArrayList<Minion>();

    MapWithoutPartitionTest() {
    }

    //y is row, x is col
    //does not check that minion is spawned "in bounds" (0 to MAP_SIZE - 1)
    public void spawnMinion(int x, int y) {
        Minion m = new Minion(this, x, y);
        minions.add(m);
    }

    public void removeMinion(Minion m) {
        minions.remove(m);
    }

    //source: https://en.wikipedia.org/wiki/Integer_square_root#Example_implementation_in_C
    public int intSqrt(int s) {
        if (s <= 1)
            return s;
        int x0 = s / 2;
        int x1 = (x0 + s / x0) / 2;
        while (x1 < x0) {
            x0 = x1;
            x1 = (x0 + s / x0) / 2;
        }
        return x0;
    }

    public int dist(DummyEntity a, DummyEntity b) {
        int temp = (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        if (temp < 0) temp *= -1;
        return intSqrt(temp);
    }

    public ArrayList<DummyEntity> collisions(DummyEntity a) {
        ArrayList<DummyEntity> hit = new ArrayList<>();

        for (DummyEntity e : minions) {
            if (dist(a, e) < a.size + e.size && a != e) {
                hit.add(e);
            }
        }

        return hit;
    }

    public static class DummyEntity {
        MapWithoutPartitionTest map;
        int size;
        int x;
        int y;

        public DummyEntity(MapWithoutPartitionTest map, int x, int y, int size) {
            this.map = map;
            this.x = x;
            this.y = y;
            this.size = size;
        }

    }

    public static class Minion extends DummyEntity {
        int health;

        public Minion(MapWithoutPartitionTest map, int x, int y) {
            super(map, x, y, 20);
            health = 100;
        }

        public void damage(int dmg) {
            health -= dmg;
            if (health < 0) map.removeMinion(this);
        }
        public String toString() {
            return ("[x: " + x + ", y: " + y + ", hp: " + health + "]");
        }
    }

    public static void main(String[] args) {
        MapWithoutPartitionTest map = new MapWithoutPartitionTest();

        for (int i = 0; i < map.MAP_SIZE; i+=1) {
            for (int j = 0; j < map.MAP_SIZE; j+=1) {
                map.spawnMinion(i, j);
            }
        }

        System.out.println("num of minions:  " + map.minions.size());
        long startTime = System.currentTimeMillis();
        for (Minion m : map.minions) {
            //System.out.println("A new minion: " + map.collisions(m).toString());
            map.collisions(m);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
