package model;

import java.util.ArrayList;

public class Map {
    public static ArrayList<Map> Maps = new ArrayList<>();
    private MapBlock[][] map;
    public Map(Integer mapWidth, Integer height) {
        map = new MapBlock[height][mapWidth];
        for (MapBlock[] mapBlockHeight : map)
            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith = new MapBlock();
    }
}
