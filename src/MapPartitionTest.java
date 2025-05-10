import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//size is radius

//TODO: check if something clamps to MAP_SIZE, it clamps to MAP_SIZE - 1
// so when /MAP_PARTITIONS, doesn't give out of bounds error

public class MapPartitionTest {

    final int MAP_SIZE = 256;
    final int MAP_PARTITIONS = 16;
    final int COLLISION_BUFFER = 20; //size of largest minion radius
    Set<DummyEntity>[][] entities = new HashSet[MAP_PARTITIONS][MAP_PARTITIONS];
    ArrayList<Minion> minions = new ArrayList<Minion>();
    Champion player;

    MapPartitionTest() {
        for (int i = 0; i < MAP_PARTITIONS; i++) {
            for (int j = 0; j < MAP_PARTITIONS; j++) {
                entities[i][j] = new HashSet<DummyEntity>();
            }
        }
        player = new Champion(this, 40, MAP_SIZE - 40);
    }

    //y is row, x is col
    //does not check that minion is spawned "in bounds" (0 to MAP_SIZE - 1)
    public Minion spawnMinion(int x, int y) {
        Minion m = new Minion(this, x, y);
        minions.add(m);
        entities[y * MAP_PARTITIONS / MAP_SIZE][x * MAP_PARTITIONS / MAP_SIZE].add(m);
        return m;
    }

    public void removeMinion(Minion m) {
        minions.remove(m);
        entities[m.y * MAP_PARTITIONS / MAP_SIZE][m.x * MAP_PARTITIONS / MAP_SIZE].remove(m);
    }

    public static int intSqrt(int s) {
        //source: https://en.wikipedia.org/wiki/Integer_square_root#Example_implementation_in_C
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
        //how close are we to a boundary?... do we have to check surrounding cells?
        //this could break if the circle is more than 2x the size of the map lol

        int xMinCell = (a.x - a.size - COLLISION_BUFFER) * MAP_PARTITIONS / MAP_SIZE;
        xMinCell = (xMinCell < 0) ? 0 : xMinCell;
        int xMaxCell = (a.x + a.size + COLLISION_BUFFER) * MAP_PARTITIONS / MAP_SIZE;
        xMaxCell = (xMaxCell > MAP_PARTITIONS - 1) ? MAP_PARTITIONS - 1 : xMaxCell;
        int yMinCell = (a.y - a.size - COLLISION_BUFFER) * MAP_PARTITIONS / MAP_SIZE;
        yMinCell = (yMinCell < 0) ? 0 : yMinCell;
        int yMaxCell = (a.y + a.size + COLLISION_BUFFER) * MAP_PARTITIONS / MAP_SIZE;
        yMaxCell = (yMaxCell > MAP_PARTITIONS - 1) ? MAP_PARTITIONS - 1 : yMaxCell;

        //check in this range of cells
        for (int i = xMinCell; i <= xMaxCell; i++) {
            for (int j = yMinCell; j <= yMaxCell; j++) {
                for (DummyEntity e : entities[j][i]) { //j is y, i is x
                    if (dist(a, e) < a.size + e.size && a != e) {
                        hit.add(e);
                    }
                }
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

        public String toString() {
            return ("[" + x + "," + y + "]");
        }
    }

    public static class Minion extends DummyEntity {
        int health;

        public Minion(MapPartitionTest map, int x, int y) {
            super(map, x, y, 5);
            health = 100;
        }

        public void damage(int dmg) {
            health -= dmg;
            if (health < 0) map.removeMinion(this);
        }
    }

    public static class Champion extends Minion {
        public Champion(MapPartitionTest map, int x, int y) {
            super(map, x, y);
            size = 10;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MapPartitionTest map = new MapPartitionTest();
        CameraTest view = new CameraTest(map);

        map.spawnMinion(220, 50);

        TimeUnit.SECONDS.sleep(1);

        //Move the player across the screen, redrawing with CameraTest centered on player
        int i = 0;
        while (i < 50) {
            view.update();
            map.player.x += 3;
            map.player.y -= 1;
            TimeUnit.MILLISECONDS.sleep(50);
            i++;
        }
//        i = 8;
//        while (i > 0) {
//            view.setSCALE(i);
//            TimeUnit.MILLISECONDS.sleep(1000);
//            i--;
//        }
        i = 0;
        while (i < 50) {
            view.update();
            map.player.x -= 3;
            map.player.y -= 1;
            TimeUnit.MILLISECONDS.sleep(20);

            i++;
        }

    }
}