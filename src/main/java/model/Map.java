package model;

import java.util.ArrayList;

public class Map {
    public static ArrayList<Map> Maps = new ArrayList<>();
    private MapBlock[][] map;
    public Map(Integer mapWidth, Integer mapHeight) {
        map = new MapBlock[mapWidth][mapHeight];
        for (MapBlock[] mapBlockHeight : map)
            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith = new MapBlock();
    }

    public MapBlock GetMapBlockByLocation(int x , int y){
        return map[x][y];
    }

}
