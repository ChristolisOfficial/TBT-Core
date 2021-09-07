package com.christolis.tbtcore.game;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;

import com.christolis.tbtcore.Main;
import com.christolis.tbtcore.util.WorldControl;

/**
 * Map.java
 *
 * Represents an instance of a map and its properties
 */
public class Map {

    private File folder;
    private File instanceFolder;
    private World world;
    private String title;
    private String ID;
    private Location center;

    private CuboidArea playArea;

    // Blue
    private Location blueSpawn;
    private Location blueCage;
    private CuboidArea blueScore;

    // Red
    private Location redSpawn;
    private Location redCage;
    private CuboidArea redScore;

    /**
     * Constructs a new map and automatically creates
     * a new running instance out of it.
     *
     * Please do note that the original map has to contain
     * a map.yml, otherwise the Map will not be made.
     *
     * @param folder A File that points to the original map to be made
     */
    public Map(File folder) throws Exception {
        File[] contents = folder.listFiles();
        boolean foundConfig = false;

        this.folder = folder;

        for (File content : contents) {
            if (content.isFile()) {
                if (content.getName().toLowerCase().equals("map.yml")) {
                    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(content);
                    foundConfig = true;
                    this.title = cfg.getString("map-name");

                    // Will only get called once, as the loop breaks
                    // some lines below
                    createInstance();

                    this.center = parseLocation(this.world, cfg.getString("center-spawn"), true);
                    this.playArea = new CuboidArea(
                            parseLocation(this.world, cfg.getString("playarea-a"), false),
                            parseLocation(this.world, cfg.getString("playarea-b"), false)
                    );

                    // Blue
                    this.blueSpawn = parseLocation(this.world, cfg.getString("blue-spawn"), true);
                    this.blueCage = parseLocation(this.world, cfg.getString("blue-cage"), false);
                    this.blueScore = new CuboidArea(
                            parseLocation(this.world, cfg.getString("blue-score-a"), false),
                            parseLocation(this.world, cfg.getString("blue-score-b"), false)
                    );

                    // Red 
                    this.redSpawn = parseLocation(this.world, cfg.getString("red-spawn"), true);
                    this.redCage = parseLocation(this.world, cfg.getString("red-cage"), false);
                    this.redScore = new CuboidArea(
                            parseLocation(this.world, cfg.getString("red-score-a"), false),
                            parseLocation(this.world, cfg.getString("red-score-b"), false)
                    );
                    break;
                }
            }
        }

        if (!foundConfig) {
            throw new Exception("Could not find map configuration!");
        } else {
        }
    }

    public void createInstance() {
        this.ID = RandomStringUtils.randomAlphanumeric(5);
        String worldFolderName = this.title + "_" + this.ID;

        this.instanceFolder = new File(
            Main.instance.getDataFolder().getPath() + File.separator + "running" +
            File.separator + worldFolderName);

        if (this.instanceFolder == null) {
            this.instanceFolder.mkdirs();
        } 

        WorldControl.copyWorld(folder, this.instanceFolder);
        this.world = Bukkit.getServer().createWorld(
                new WorldCreator(this.instanceFolder.getPath())
        );
    }

    public void shutdown() {
        for (Player player : world.getPlayers()) {
            TBTPlayer tbtplayer = TBTPlayer.find(player);

            if (tbtplayer != null)
                tbtplayer.getGame().removePlayer(tbtplayer, false);
        }

        WorldControl.unloadWorld(this.world);
        WorldControl.removeWorld(this.instanceFolder);
    }

    public String getID() {
        return ID;
    }

    public World getWorld() {
        return world;
    }

    public String getTitle() {
        return title;
    }

    public Location getCenterLocation() {
        return center;
    }

    public CuboidArea getPlayArea() {
        return playArea;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public Location getBlueCage() {
        return blueCage;
    }

    public CuboidArea getBlueScore() {
        return blueScore;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getRedCage() {
        return redCage;
    }

    public CuboidArea getRedScore() {
        return redScore;
    }

    /**
     * Parses any given location in string notation and
     * returns a new Location instance from it.
     *
     * @param str The input location.
     * @param rot Should be set to true if head rotation fields are to be written.
     */
    public static Location parseLocation(World world, String str, boolean rot) {
        String[] locRaw = str.split(" ", 5);
        double[] locCoords = new double[5];

        for (int i = 0; i < ((rot) ? 5 : 3); i++) {
            locCoords[i] = Double.parseDouble(locRaw[i]);
        }

        if (rot) {
            return new Location(world,
                    locCoords[0], locCoords[1], locCoords[2],
                    (float) locCoords[3], (float) locCoords[4]);
        } else {
            return new Location(world,
                    locCoords[0], locCoords[1], locCoords[2]);
        }
    }
}
