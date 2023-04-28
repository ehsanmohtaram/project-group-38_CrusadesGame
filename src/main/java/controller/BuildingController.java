package controller;

import view.BuildingMenu;

public class BuildingController {
    BuildingMenu buildingMenu;
    public BuildingController() {
        this.buildingMenu = new BuildingMenu(this);

    }

    public void run() {
        buildingMenu.run();
    }
}
