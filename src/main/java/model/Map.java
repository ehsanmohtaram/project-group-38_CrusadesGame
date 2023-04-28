package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map implements Cloneable {
    public static ArrayList<Map> Maps = new ArrayList<>();
    public static ArrayList<Map> DEFAULT_MAPS = new ArrayList<>(3);
    private ArrayList<Kingdom> players = new ArrayList<>();
    private String mapName;
    private MapBlock[][] map;
    private Boolean[][] accessToRight;
    private Boolean[][] accessToDown;
    private int mapWidth;
    private int mapHeight;

    public Map(Integer mapWidth, Integer mapHeight, String mapName) {
        this.mapName = mapName;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        map = new MapBlock[mapWidth][mapHeight];
        accessToDown = new Boolean[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; i++)
            for (int j = 0; j < mapHeight; j++)
                accessToDown[i][j] = true;
        for (int i = 0; i < mapWidth; i++)
            accessToDown[i][mapHeight - 1] = false;
        accessToRight = new Boolean[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; i++)
            for (int j = 0; j < mapHeight; j++)
                accessToRight[i][j] = true;
        for (int i = 0; i < mapHeight; i++)
            accessToRight[mapWidth - 1][i] = false;

        for (int i = 0; i < mapWidth; i++)
            for (int j = 0; j < mapHeight; j++)
                map[i][j] = new MapBlock(i , j);
        Maps.add(this);
//        for (MapBlock[] mapBlockHeight : map)
//            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith = new MapBlock();
    }

    public static void createDefaultMaps(){
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

    public ArrayList<Kingdom> getPlayers() {
        return players;
    }

    public MapBlock getMapBlockByLocation(int xPosition , int yPosition){
        if(xPosition < mapWidth && yPosition < mapHeight)
            return map[xPosition][yPosition];
        return null;
    }

    public static Map getDefaultMap(int index) throws CloneNotSupportedException {
        if(index >= DEFAULT_MAPS.size())
            return null;
        return (Map)(DEFAULT_MAPS.get(index).clone());
    }

    public Boolean isThereAKingdomHere(int x, int y) {
        for (Kingdom kingdom : players)
            if (kingdom.getHeadquarter().getPosition().equals(getMapBlockByLocation(x,y))) return true;
        return false;
    }

    public void changeType(int x , int y, MapBlockType type){
        map[x][y].setMapBlockType(type);
    }

    public void changeType(int x1 , int y1, int x2, int y2, MapBlockType type){
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                map[i][j].setMapBlockType(type);
    }

    public void clearBlock(int x , int y){
        map[x][y] = new MapBlock(x , y);
    }

    public void restrictAccess(int xPosition ,int yPosition , char direction){
        Random random = new Random();
        ArrayList<Character> directions = new ArrayList<Character>(List.of('w' , 'e' , 'n' , 's'));
        if(direction == 'r')
            direction = directions.get(random.nextInt()%4);
        switch (direction){
            case 'w':
                if(xPosition != 0)
                    accessToRight[xPosition - 1][yPosition] = false;
                break;
            case 'e':
                accessToRight[xPosition][yPosition] = false;
                break;
            case 'n':
                if(yPosition != 0)
                    accessToDown[xPosition][yPosition - 1] = false;
                break;
            case 's':
                accessToDown[xPosition][yPosition] = false;

        }
    }


}
