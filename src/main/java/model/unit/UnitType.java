package model.unit;

import javafx.scene.image.Image;
import model.Weapons;

public enum UnitType {
    ARCHER(20, 5, 4, 5, 7,0, Weapons.BOW, null, true, new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    CROSSBOWMEN(30, 8, 2, 3, 7,0, Weapons.CROSSBOW, Weapons.LEATHER_ARMOUR, true,new Image(UnitType.class.getResource("/images/unit/menu/CROSSBOWMEN.png").toExternalForm())),
    SPEAR_MAN(20, 8, 3, 1, 5,0,Weapons.SPEAR,null, false,new Image(UnitType.class.getResource("/images/unit/menu/SPEAR_MAN.png").toExternalForm())),
    PIKE_MAN(40, 8, 2, 2, 10, 0, Weapons.SPEAR, Weapons.METAL_ARMOUR, false,new Image(UnitType.class.getResource("/images/unit/menu/PIKE_MAN.png").toExternalForm())),
    MACE_MAN(30,12,3, 1, 13,0,Weapons.MACE, Weapons.LEATHER_ARMOUR, false,new Image(UnitType.class.getResource("/images/unit/menu/MACE_MAN.png").toExternalForm())),
    SWORDSMEN(30, 12, 1,1,8,0, Weapons.SWORDS, Weapons.METAL_ARMOUR, false,new Image(UnitType.class.getResource("/images/unit/menu/SWORDSMEN.png").toExternalForm())),
    KNIGHT(40, 12, 5,2,15,0, Weapons.SWORDS, Weapons.METAL_ARMOUR, false,new Image(UnitType.class.getResource("/images/unit/menu/KNIGHT.png").toExternalForm())),
    TUNNELER(15, 8, 4 , 1 ,5, -1, null, null, false,new Image(UnitType.class.getResource("/images/unit/menu/TUNNELER.png").toExternalForm())),
    LADDER_MAN(10, 0, 4,1,4, -1,Weapons.LADDER,null, false,new Image(UnitType.class.getResource("/images/unit/menu/LADDER_MAN.png").toExternalForm())),
    ENGINEER(10 , 15 , 3, 1,4, -1, null, null, null,new Image(UnitType.class.getResource("/images/unit/menu/ENGINEER.png").toExternalForm())),
    WORKER(10 , 0 ,3 , 0, 4, -1 , null, null, null,new Image(UnitType.class.getResource("/images/unit/menu/WORKER.png").toExternalForm())),
    BLACK_MONK(30, 10, 2, 1 ,8, -2, Weapons.MACE, null, false,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    MONK(30, 0 , 5, 0 , 5, -3,null, null, false,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    ARCHER_BOW(20, 10, 4, 5 ,7, 1, null ,null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER_BOW.png").toExternalForm())),
    SLAVES(5, 3, 4, 1 ,5, 1, null ,null, false,new Image(UnitType.class.getResource("/images/unit/menu/SLAVES.png").toExternalForm())),
    SLINGERS(10, 5, 4, 3 ,5, 1, null ,null, false ,new Image(UnitType.class.getResource("/images/unit/menu/SLINGERS.png").toExternalForm())),
    ASSASSINS(30, 10, 3, 1 ,9, 1, null ,null, false,new Image(UnitType.class.getResource("/images/unit/menu/ASSASSINS.png").toExternalForm())),
    HORSE_ARCHERS(30, 10, 5, 5 , 12, 1, null ,null, true,new Image(UnitType.class.getResource("/images/unit/menu/HORSE_ARCHERS.png").toExternalForm())),
    ARABIAN_SWORDSMAN(40, 12, 5, 1 ,16, 1, null ,null, false,new Image(UnitType.class.getResource("/images/unit/menu/ARABIAN_SWORDSMAN.png").toExternalForm())),
    FIRE_THROWERS(20, 15, 5, 4 , 9,1, null ,null, true,new Image(UnitType.class.getResource("/images/unit/menu/FIRE_THROWERS.png").toExternalForm())),
    MANGONEL(100, 15, 0, 3, 0,-4, null, null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    BALLISTA(100, 20, 0, 5, 0, -4,null, null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    CATAPULT(300, 20, 3, 3 , 0,-4,null, null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    TREBUCHET(500, 20, 0, 4, 0,-4, null, null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    SIEGE_TOWER(500, 0, 2, 4, 0, -4, null, null, false,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    BATTERING_RAM(500, 15, 2, 1, 0, -4,null, null,true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    MANTLET(30, 0, 5, 0, 0, -4, null, null, false,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm())),
    FIRE_BALLISTA(400, 15, 3, 5, 0, -4,null, null, true,new Image(UnitType.class.getResource("/images/unit/menu/ARCHER.png").toExternalForm()));
    private final Integer HP_IN_START;
    private final Integer DAMAGE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Integer PRICE;
    private final Integer IS_ARAB;
    private final Weapons WEAPON_NEEDED;
    private final Weapons Armour_Needed;
    private final Boolean CAN_DO_AIR_ATTACK;
    private final Image menuTexture;

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE, Integer PRICE, Integer IS_ARAB, Weapons WEAPON_NEEDED, Weapons armourNeeded, Boolean canDoAirAttack, Image menuTexture) {
        this.HP_IN_START = HP_IN_START;
        this.DAMAGE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
        this.WEAPON_NEEDED = WEAPON_NEEDED;
        Armour_Needed = armourNeeded;
        this.CAN_DO_AIR_ATTACK = canDoAirAttack;
        this.menuTexture = menuTexture;
    }

    public Integer getHP_IN_START() {
        return HP_IN_START;
    }

    public Integer getDAMAGE() {
        return DAMAGE;
    }

    public Integer getVELOCITY() {
        return VELOCITY;
    }

    public Integer getATTACK_RANGE() {
        return ATTACK_RANGE;
    }

    public Integer getIS_ARAB() {
        return IS_ARAB;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public Weapons getWEAPON_NEEDED() {
        return WEAPON_NEEDED;
    }

    public Weapons getArmour_Needed() {
        return Armour_Needed;
    }

    public Boolean getCAN_DO_AIR_ATTACK() {
        return CAN_DO_AIR_ATTACK;
    }

    public Image getMenuTexture() {
        return menuTexture;
    }
}
