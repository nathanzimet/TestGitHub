import java.util.ArrayList;

import static java.lang.Math.sqrt;

//size is radius

public class MapPartitionTest {

    final int MAP_SIZE = 256;
    final int MAP_PARTITIONS = 16;
    final int COLLISION_BUFFER = 20; //size of largest minion radius
    ArrayList<DummyEntity>[][] entities = new ArrayList[MAP_PARTITIONS][MAP_PARTITIONS];
    ArrayList<Minion> minions = new ArrayList<Minion>();

    MapPartitionTest() {
        for (int i = 0; i < MAP_PARTITIONS; i++) {
            for (int j = 0; j < MAP_PARTITIONS; j++) {
                entities[i][j] = new ArrayList<DummyEntity>();
            }
        }
    }

    //y is row, x is col
    public void spawnMinion(int x, int y) {
        Minion m = new Minion(this, x, y);
        minions.add(m);
        entities[y / MAP_PARTITIONS][x / MAP_PARTITIONS].add(m);
    }

    public void removeMinion(Minion m) {
        minions.remove(m);
        entities[m.y / MAP_PARTITIONS][m.x / MAP_PARTITIONS].remove(m);
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
        int temp = (a.x - b.x) * (a.x - b.x) - (a.y - b.y) * (a.y - b.y);
        if (temp < 0) temp *= -1;
        return intSqrt(temp);
    }

    public ArrayList<DummyEntity> collisions(DummyEntity a) {
        ArrayList<DummyEntity> hit = new ArrayList<>();
        //how close are we to a boundary?... do we have to check surrounding cells?
        //this could break if the circle is more than 2x the size of the map lol

        int xMinCell = (a.x - a.size - COLLISION_BUFFER) / 16;
        xMinCell = (xMinCell < 0)? 0 : xMinCell;
        int xMaxCell = (a.x + a.size + COLLISION_BUFFER) / 16;
        xMaxCell = (xMaxCell > 16)? 16 : xMaxCell;
        int yMinCell = (a.y - a.size - COLLISION_BUFFER) / 16;
        yMinCell = (yMinCell < 0)? 0 : yMinCell;
        int yMaxCell = (a.y + a.size + COLLISION_BUFFER) / 16;
        yMaxCell = (yMaxCell > 16)? 16 : yMaxCell;

        //check in this range of cells
        for (int i = xMinCell; i <= xMaxCell; i++) {
            for (int j = yMinCell; j <= yMaxCell; j++) {
                entities[i][j].remove(a);   //TODO: faster to do if() every entity than remove and readd
                for (DummyEntity e : entities[i][j]) {
                    if (dist(a, e) < a.size + e.size) {
                        hit.add(e);
                    }
                }
                entities[i][j].add(a);
            }
        }
        return hit;
    }

    public static class DummyEntity {
        MapPartitionTest map;
        int size;
        int x;
        int y;

        public DummyEntity(MapPartitionTest map, int x, int y, int size) {
            this.map = map;
            this.x = x;
            this.y = y;
            this.size = size;
        }

    }

    public static class Minion extends DummyEntity {
        int health;

        public Minion(MapPartitionTest map, int x, int y) {
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
        MapPartitionTest map = new MapPartitionTest();
        map.spawnMinion(14, 14);
        map.spawnMinion(53, 14);
        System.out.println(map.collisions(map.minions.get(1)).toString());

        //TODO: test by spawning 256 x 256 entities, and check their collisions
        // then, do the same maybe? but without map partitioning
        // time both

    }
}
