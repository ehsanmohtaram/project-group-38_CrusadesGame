package model;

import java.util.ArrayList;

public class Map implements Cloneable {
    public static ArrayList<Map> Maps = new ArrayList<>();
    public static ArrayList<Map> DEFAULT_MAPS = new ArrayList<>(3);
    private ArrayList<Kingdom> players;
    private String mapName;
    private MapBlock[][] map;
    private Boolean[][] access;
    private int mapWidth;
    private int mapHeight;

    {
        createDefaultMaps();
    }

    public Map(Integer mapWidth, Integer mapHeight, String mapName) {
        this.mapName = mapName;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        map = new MapBlock[mapWidth][mapHeight];
        access = new Boolean[mapWidth * 2][mapHeight * 2];
        for (int i = 0; i < mapWidth * 2; i++)
            for (int j = 0; j < mapHeight * 2; j++)
                access[i][j] = true;
        for (int i = 0; i < mapWidth; i++)
            for (int j = 0; j < mapHeight; j++)
                map[i][j] = new MapBlock(i , j);
        Maps.add(this);
//        for (MapBlock[] mapBlockHeight : map)
//            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith = new MapBlock();
    }

    public MapBlock GetMapBlockByLocation(int x , int y){
        return map[x][y];
    }
    private void createDefaultMaps(){
        Map defaultMap1 = new Map(60 , 60 , "jungle");
        Map defaultMap2 = new Map(60 , 60 , "graveyard");
        DEFAULT_MAPS.add(defaultMap1);
        DEFAULT_MAPS.add(defaultMap2);
        for (MapBlock[] mapBlockHeight : defaultMap1.map)
            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith.setMapBlockType(MapBlockType.GRASSLAND);

        //toDo: design two default maps
    }

    public String getMapName() {
        return mapName;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void addPlayer(Kingdom kingdom){
        players.add(kingdom);
    }

    public Kingdom getKingdomByOwner(User owner){
        for (Kingdom find: players) {
            if(find.getOwner().equals(owner))
                return find;
        }
        return null;
    }

    public static Map getDefaultMap(int index) throws CloneNotSupportedException {
        if(index >= DEFAULT_MAPS.size())
            return null;
        return (Map)(DEFAULT_MAPS.get(index).clone());
    }

    public void changeType(int x , int y, MapBlockType type){
        map[x][y].setMapBlockType(type);
    }

    public void changeType(int x1 , int y1, int x2, int y2, MapBlockType type){
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                map[i][j].setMapBlockType(type);
    }

}
