package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Map implements Cloneable {
    public static ArrayList<Map> Maps = new ArrayList<>();
    public static ArrayList<Map> DEFAULT_MAPS = new ArrayList<>(3);
    private ArrayList<Kingdom> players = new ArrayList<>();
    private String mapName;
    private MapBlock[][] map;
    private Boolean[][] accessToRight;
    private Boolean[][] accessToDown;
    private ArrayList<Character> directions = new ArrayList<Character>(List.of('w' , 'e' , 'n' , 's'));
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

//    public void changeType(int x , int y, MapBlockType type){
//        map[x][y].setMapBlockType(type);
//    }

    public void changeType(int x1 , int y1, int x2, int y2, MapBlockType type){
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++) {
                map[i][j].setMapBlockType(type);
                if (type.isAccessible())
                    for (Direction direction : Direction.values())
                        changeAccess(i, j, direction, true);
                else
                    for (Direction direction : Direction.values())
                        changeAccess(i, j, direction, false);

            }

    }

    public void clearBlock(int x , int y){
        map[x][y] = new MapBlock(x , y);
    }

    public void changeAccess(int xPosition , int yPosition , Direction direction, boolean isAccessible){
        switch (direction){
            case WEST:
                if(xPosition != 0)
                    accessToRight[xPosition - 1][yPosition] = isAccessible;
                break;
            case EAST:
                accessToRight[xPosition][yPosition] = isAccessible;
                break;
            case NORTH:
                if(yPosition != 0)
                    accessToDown[xPosition][yPosition - 1] = isAccessible;
                break;
            case SOUTH:
                accessToDown[xPosition][yPosition] = isAccessible;

        }
    }

    public boolean checkAccess(int xPosition , int yPosition , Direction direction){
        switch (direction){
            case WEST:
                if(xPosition != 0)
                    return accessToRight[xPosition - 1][yPosition];
                break;
            case EAST:
                return accessToRight[xPosition][yPosition];
            case NORTH:
                if(yPosition != 0)
                    return accessToDown[xPosition][yPosition - 1];
                break;
            case SOUTH:
                return accessToDown[xPosition][yPosition];
        }
        return false;
    }

    public String getPartOfMap (int xPosition , int yPosition){
        String result = "map: in " + xPosition + "," + yPosition + '\n' + "    ";
        String resetColor = "\033[0m";
        for (int i = xPosition - 10; i <= (xPosition + 10); i++)
            result += String.format(" %3d  " , i);
        result += '\n';
        for (int j = yPosition - 4; j <= yPosition + 4; j++) {
            for (int fill = 0; fill < 3; fill++){
                if(fill == 1)
                    result += String.format("%3d " , j);
                else
                    result += "    ";
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
//            for (int i = xPosition - 10; i <= (xPosition + 10); i++) {
//                result += String.format("%2d,%2d" , i , j) + ' ';
//            }
            result += '\n';
        }
        return result.substring(0, result.length() - 2);
    }


    public MapBlock[][] getSurroundingArea(int xPosition, int yPosition, int range){
        MapBlock[][] output = new MapBlock[2 * range + 1][2 * range + 1];
        for (int j = yPosition - range; j <= yPosition + range; j++) {
            for (int i = xPosition - range; i <= (xPosition + range); i++) {
                output[i - (xPosition - range)][j - (yPosition - range)] = map[i][j];
            }
        }
        return output;
    }

    public Integer getShortestWayLength(int xPosition, int yPosition, int xOfDestination, int yOfDestination, Integer limit){
        boolean[][]mark = new boolean[mapWidth][mapHeight];
        AtomicInteger answer;
        if(limit == null)
            answer = new AtomicInteger(mapWidth * mapHeight);
        else
            answer = new AtomicInteger(limit);
        if(xPosition < xOfDestination && yPosition < yOfDestination)
            getWaysLengthByEast(mark, xPosition, yPosition, 0, xOfDestination, yOfDestination, answer, true);
        else if (xPosition > xOfDestination && yPosition < yOfDestination)
            getWaysLengthByEast(mark, xOfDestination, yOfDestination, 0, xPosition, yPosition, answer, false);
        else if (xPosition >= xOfDestination && yPosition >= yOfDestination)
            getWaysLengthByEast(mark,  xOfDestination, yOfDestination, 0, xPosition, yPosition, answer, true);
        else
            getWaysLengthByEast(mark, xPosition, yPosition, 0, xOfDestination, yOfDestination, answer, false);
        if((limit == null && answer.get() == (mapWidth * mapHeight)) || answer.get() == limit)
            return null;
        return answer.get();
    }

    private void getWaysLengthByEast(boolean[][]mark, int xPosition, int yPosition , int length , int xOfDestination, int yOfDestination , AtomicInteger minimum, boolean southPriority){
        if(length >= minimum.get())
            return;
        if(getMapBlockByLocation(xPosition , yPosition) == null)
            return;
        if(xPosition == xOfDestination && yPosition == yOfDestination) {
            minimum.set(length);
            return;
        }
        if(mark[xPosition][yPosition] == true)
            return;
        mark[xPosition][yPosition] = true;
        if(checkAccess(xPosition, yPosition, Direction.EAST))
            getWaysLengthByEast(mark, xPosition + 1, yPosition , length + 1 , xOfDestination, yOfDestination, minimum, true);
        if(southPriority){
            if (checkAccess(xPosition, yPosition, Direction.SOUTH))
                getWaysLengthByEast(mark, xPosition, yPosition + 1, length + 1, xOfDestination, yOfDestination, minimum, true);
            if (checkAccess(xPosition, yPosition, Direction.WEST))
                getWaysLengthByEast(mark, xPosition - 1, yPosition, length + 1, xOfDestination, yOfDestination, minimum, true);
        }
        if(checkAccess(xPosition, yPosition, Direction.NORTH))
            getWaysLengthByEast(mark, xPosition, yPosition - 1 , length + 1 , xOfDestination, yOfDestination, minimum, true);
        if (!southPriority) {
            if (checkAccess(xPosition, yPosition, Direction.SOUTH))
                getWaysLengthByEast(mark, xPosition, yPosition + 1, length + 1, xOfDestination, yOfDestination, minimum, true);
            if (checkAccess(xPosition, yPosition, Direction.WEST))
                getWaysLengthByEast(mark, xPosition - 1, yPosition, length + 1, xOfDestination, yOfDestination, minimum, true);
        }

        if(mark[xPosition][yPosition] == true) {
            mark[xPosition][yPosition] = false;
        }

    }

}
