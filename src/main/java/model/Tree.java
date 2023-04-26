package model;

public enum Tree {
    CHERRY, OLIVE, COCONUT, DATE, DESSERT;

    public static Tree findEnumByName(String TreeName) {
        for (Tree searchForType : Tree.values())
            if (searchForType.name().toLowerCase().equals(TreeName))
                return searchForType;
        return null;
    }
}
