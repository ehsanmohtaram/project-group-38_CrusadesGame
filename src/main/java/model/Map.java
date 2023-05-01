package model;

import model.building.Building;

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
        Map defaultMap1 = new Map(60 , 50 , "jungle");
        Map defaultMap2 = new Map(60 , 60 , "graveyard");
        DEFAULT_MAPS.add(defaultMap1);
        DEFAULT_MAPS.add(defaultMap2);
        for (MapBlock[] mapBlockHeight : defaultMap1.map)
            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith.setMapBlockType(MapBlockType.GRASSLAND);

        for (MapBlock b: defaultMap1.map[0]) b.setMapBlockType(MapBlockType.SEA);
        for (MapBlock b: defaultMap1.map[1]) b.setMapBlockType(MapBlockType.BEACH);
        for (MapBlock b: defaultMap1.map[defaultMap1.mapWidth - 1]) b.setMapBlockType(MapBlockType.SEA);
        for (MapBlock b: defaultMap1.map[defaultMap1.mapWidth - 2]) b.setMapBlockType(MapBlockType.BEACH);
        for (int i = 5; i < 45; i++) {
            defaultMap1.map[25][i].setMapBlockType(MapBlockType.SHALLOW_WATER);
            for (int j = 20; j < 22; j++)
                defaultMap1.map[i][j].setMapBlockType(MapBlockType.RIVER);
        }
        for (int j = 10; j < 40; j++)
            defaultMap1.map[20][j].setMapBlockType(MapBlockType.SLATE);

        for (int i = 20; i < 35; i++) {
            for (int j = 4; j < 12; j++) {
                defaultMap1.map[i][j].setMapBlockType(MapBlockType.SLATE);
            }
            defaultMap1.map[i][30].setMapBlockType(MapBlockType.IRON);
        }
        for (MapBlock[] mapBlockHeight : defaultMap1.map)
            for (MapBlock mapBlockWith : mapBlockHeight) mapBlockWith.addTree(Tree.OLIVE);

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
        if(xPosition < mapWidth && yPosition < mapHeight && xPosition >= 0 && yPosition >= 0)
            return map[xPosition][yPosition];
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

    public String getPartOfMap (int xPosition , int yPosition){
        String result = "map: in " + xPosition + "," + yPosition + '\n';
        String resetColor = "\033[0m";
        for (int j = yPosition - 4; j <= yPosition + 4; j++) {
            for (int fill = 0; fill < 3; fill++){
                for (int i = xPosition - 10; i <= (xPosition + 10); i++) {
                    MapBlock showedBlock;
                    if ((showedBlock = getMapBlockByLocation(i, j)) != null) {
                        if(fill == 1){
                            result += showedBlock.getMapBlockType().getColor() + "  "
                                    + showedBlock.getLatestDetails() + "  " + resetColor + " ";
                        }else {
                            result += showedBlock.getMapBlockType().getColor() + "     " + resetColor + " ";
                        }
                    }else
                        result += "\u001B[48;5;237m" + "XXXXX" + resetColor + " ";

                }
                result += '\n';
            }
            result += '\n';
        }
        return result.substring(0, result.length() - 2);
    }


}
