package io.github.keheck.csminecraft;

import io.github.keheck.csminecraft.events.*;
import io.github.keheck.csminecraft.repeats.RepeatBombWarning;
import io.github.keheck.csminecraft.repeats.RepeatingCountdownVisual;
import io.github.keheck.csminecraft.repeats.beepstages.RepeatingBeepStage;
import io.github.keheck.csminecraft.repeats.beepstages.RepeatingBeepStage1;
import io.github.keheck.csminecraft.timers.*;
import io.github.keheck.csminecraft.util.ConfigValues;
import io.github.keheck.csminecraft.util.Constants;
import io.github.keheck.csminecraft.util.Numeric;
import io.github.keheck.csminecraft.util.loaders.LangLoader;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Map
{
    //private String name;
                    //main, a, b, t, ct
    private int[][] bounds;

    private boolean isGoing = false;
    private boolean defusing = false;
    private boolean inRound = false;
    private boolean canBuy = true;
    private boolean warmup = true;
    private boolean gameDone = false;
    private boolean switched = false;
    private boolean canPlayersMove = true;

    private ArrayList<Player> cts = new ArrayList<>();
    private ArrayList<Player> ts  = new ArrayList<>();
    private ArrayList<Player> deadCts = new ArrayList<>();
    private ArrayList<Player> deadTs  = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> tntShooter = new ArrayList<>();

    private HashMap<Player, Integer> money  = new HashMap<>();
    private HashMap<Player, Integer> kills  = new HashMap<>();
    private HashMap<Player, Integer> deaths = new HashMap<>();

    private int ctScore = 0;
    private int tScore = 0;

    private Player playerTelePending = null;
    private Player defuser = null;
    private Player planter = null;

    private Boolean pendingIsCT = null;
    private World world;
    private JavaPlugin plugin;
    private BossBar timer;
    private Location bombLoc = null;
    private Location pendingLoc = null;

    private TimerBombExplode explode;
    private TimerRoundStart roundStart;
    private TimerDefused defused;
    private TimerBombPlanted planted;

    private RepeatBombWarning warning;

    public RepeatingBeepStage beepStage;

    public Map(JavaPlugin plugin, World world, int[][] bounds)
    {
        this.bounds = bounds;
        this.world = world;
        this.plugin = plugin;

        System.out.println(LangLoader.get("map.game.warmup"));
        timer = plugin.getServer().createBossBar(ChatColor.BLUE.toString() + LangLoader.get("map.game.warmup"), BarColor.GREEN, BarStyle.SOLID);
        timer.setVisible(true);
        timer.setProgress(1);
    }

    public String getCenter()
    {
        int centerX = (bounds[0][0] + bounds[0][3]) / 2;
        int centerY = (bounds[0][1] + bounds[0][4]) / 2;
        int centerZ = (bounds[0][2] + bounds[0][5]) / 2;

        return "X: " + centerX + " Y:" + centerY + " Z: " + centerZ;
    }

    public JavaPlugin getPlugin() { return plugin; }

    public ArrayList<Player> getTs() { return ts; }

    public ArrayList<Player> getCts() { return cts; }

    public ArrayList<Player> getPlayers() { return players; }

    public ArrayList<Player> getTntShooter() { return tntShooter; }

    public boolean isDefusing() { return defusing; }

    public Player getDefuser() { return defuser; }

    public Player getPlanter() { return planter; }

    public boolean isInRound() { return inRound; }

    public void setDefuser(Player p, Boolean hasKit)
    {
        if(p == null && defuser != null)
            defuser.removePotionEffect(PotionEffectType.SLOW);

        defuser = p;
        defusing = p != null;

        if(hasKit != null)
        {
            defused = new TimerDefused(plugin, hasKit ? Constants.DEFUSE_KIT : Constants.DEFUSE_NOKIT, this);
        }
        else
        {
            if(defused != null && !defused.isCancelled())
                defused.cancel();

            defused = null;
        }
    }

    public void setPlanter(Player planter)
    {
        if(planter != null)
        {
            planted = new TimerBombPlanted(plugin, this);
            planter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 10, false, false));
        }
        else
        {
            if(planted != null && !planted.isCancelled()) planted.cancel();
            this.planter.removePotionEffect(PotionEffectType.SLOW);
        }

        this.planter = planter;
    }

    public boolean join(Player player)
    {
        if(isGoing)
        {
            return false;
        }

        if(cts.size() < 5 && ts.size() < 5)
        {
            Random rand = new Random();

            if(rand.nextBoolean())
            //if(player.getPlayerListName().equals("Keheck"))
            //if(false)
            {
                cts.add(player);
                players.add(player);
                pendingIsCT = true;
            }
            else
            {
                ts.add(player);
                players.add(player);
                pendingIsCT = false;
            }
        }
        else if(cts.size() < 5)
        {
            cts.add(player);
            pendingIsCT = true;
        }
        else if(ts.size() < 5)
        {
            ts.add(player);
            pendingIsCT = false;
        }
        else
        {
            return false;
        }

        money.put(player, Constants.MONEY_CAP);

        CSMinecraft.PLAYERS_IN_GAME.add(player);
        playerTelePending = player;
        timer.addPlayer(player);
        return true;
    }

    public static int getItemType(ItemStack item)
    {
        switch(item.getType())
        {
            case WOOD_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLD_SWORD:
                return 1;
            case TNT:
                return 2;
            case IRON_HOE:
                return 3;
            default:
                return -1;
        }
    }

    public boolean isSameTeam(Player player1, Player player2)
    {
        return (cts.contains(player1) && cts.contains(player2)) || (ts.contains(player1) && ts.contains(player2));
    }

    public void leave(Player player)
    {
        cts.remove(player);
        ts.remove(player);
        players.remove(player);
        timer.removePlayer(player);
        CSMinecraft.PLAYERS_IN_GAME.remove(player);
    }

    public void teleportPlayer()
    {
        if(pendingIsCT != null && playerTelePending != null)
        {
            if(pendingIsCT)
            {
                int xDiff = Math.abs(bounds[4][0] - bounds[4][3]);
                int zDiff = Math.abs(bounds[4][2] - bounds[4][5]);

                Random rand = new Random();

                int spawnX = rand.nextInt(xDiff) + bounds[4][0];
                int spawnY = bounds[4][1];
                int spawnZ = rand.nextInt(zDiff) + bounds[4][2];

                playerTelePending.teleport(new Location(world, spawnX+.5, spawnY, spawnZ+.5));
            }
            else
            {
                int xDiff = Math.abs(bounds[3][0] - bounds[3][3]);
                int zDiff = Math.abs(bounds[3][2] - bounds[3][5]);

                Random rand = new Random();

                int spawnX = rand.nextInt(xDiff) + bounds[3][0];
                int spawnY = bounds[3][1];
                int spawnZ = rand.nextInt(zDiff) + bounds[3][2];

                playerTelePending.teleport(new Location(world, spawnX+.5, spawnY, spawnZ+.5));
            }

            pendingIsCT = null;
            playerTelePending = null;
        }
    }

    private boolean hasPlayer(Player player) { return cts.contains(player) || ts.contains(player); }

    public void start()
    {
        isGoing = true;
        warmup = false;

        for(Player p : cts)
        {
            pendingIsCT = true;
            playerTelePending = p;
            money. put(p, 5);
            kills. put(p, 0);
            deaths.put(p, 0);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.ROUND_START, 10, false, false));
            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            p.getInventory().clear();
            teleportPlayer();
        }
        for(Player p : ts)
        {
            pendingIsCT = false;
            playerTelePending = p;
            money. put(p, 5);
            kills. put(p, 0);
            deaths.put(p, 0);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.ROUND_START, 10, false, false));
            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            p.getInventory().clear();
            teleportPlayer();
        }

        plugin.getServer().getPluginManager().callEvent(new EventGameStart(this));
        setupTimer(BarColor.YELLOW);
        setupRound();
    }

    public void setInRound(boolean inRound) { this.inRound = inRound; }

    public void addToCtScore() { ctScore++; }

    public void addToTScore() { tScore++; }

    private String getWinner()
    {
        return tScore == 16 ?
                getTColor() + LangLoader.get("map.game.end.twin") : ctScore == 16 ?
                getCtColor() + LangLoader.get("map.game.end.ctwin") : LangLoader.get("map.game.end.draw");
    }

    public void initAfterRound()
    {
        if(defuser != null)
            setDefuser(null, null);

        if(tScore == 16 || ctScore == 16 || tScore + ctScore == 30)
        {
            for(Player player : players)
            {
                player.sendMessage(LangLoader.get("map.game.end.player_message") + getWinner());
            }

            gameDone = true;
        }

        if(tScore + ctScore == 15)
        {
            int pointChache = tScore;
            tScore = ctScore;
            ctScore = pointChache;

            ArrayList<Player> playerCache = new ArrayList<>(cts);
            cts.clear();
            cts.addAll(ts);
            ts.clear();
            ts.addAll(playerCache);

            for(Player player : players)
            {
                player.sendMessage(LangLoader.get("map.game.half"));
                money.put(player, Constants.MONEY_START);
            }

            switched = true;
            inRound = false;
        }

        new TimerRoundEnd(plugin, this);
        visual.cancel();
        timer.setVisible(false);

        if(visual != null && !visual.isCancelled()) visual.cancel();
        if(warning != null && !warning.isCancelled()) warning.cancel();
        if(beepStage != null && !beepStage.isCancelled()) beepStage.cancel();
        if(countdown != null && !countdown.isCancelled()) countdown.cancel();
    }

    public void setupRound()
    {
        if(gameDone)
        {
            stopGame();
            return;
        }

        canPlayersMove = false;
        canBuy = true;
        inRound = false;

        if(bombLoc != null)
            world.getBlockAt(bombLoc).setType(Material.AIR);

        if(explode != null && !explode.isCancelled()) explode.cancel();
        if(visual != null && !visual.isCancelled()) visual.cancel();
        if(countdown != null && !countdown.isCancelled()) countdown.cancel();
        if(roundStart != null && !roundStart.isCancelled()) roundStart.cancel();
        if(warning != null && !warning.isCancelled()) warning.cancel();
        if(beepStage != null && !beepStage.isCancelled()) beepStage.cancel();

        bombLoc = null;

        deadTs.clear();
        deadCts.clear();

        double x = ((double)bounds[0][0] + (double)bounds[0][3]) / 2;
        double y = ((double)bounds[0][1] + (double)bounds[0][4]) / 2;
        double z = ((double)bounds[0][2] + (double)bounds[0][5]) / 2;

        Location center = new Location(world, x, y, z);

        double radX = Math.abs(x - bounds[0][0]);
        double radY = Math.abs(y - bounds[0][1]);
        double radZ = Math.abs(z - bounds[0][2]);

        Collection<Entity> entities = world.getNearbyEntities(center, radX, radY, radZ);

        for (Entity entity : entities)
        {
            if (entity instanceof Item)
            {
                entity.remove();
            }
        }

        for(Player p : cts)
        {
            p.setGameMode(GameMode.SURVIVAL);

            pendingIsCT = true;
            playerTelePending = p;
            teleportPlayer();
            if(p.getInventory().getItem(0) == null || switched)
            {
                ItemStack itemStack = new ItemStack(Material.WOOD_SWORD);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setUnbreakable(true);
                itemStack.setItemMeta(meta);
                p.getInventory().setItem(0, itemStack);
            }

            p.getInventory().setItem(8, null);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.ROUND_START, 10, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Constants.ROUND_START, 128, false, false));
            p.sendMessage(getCtColor() + "=====================");
            p.sendMessage(getTColor()  + "Money: " + money.get(p) + "G");
            p.sendMessage(getTColor()  + "Kills: " + kills.get(p));
            p.sendMessage(getTColor()  + "Deaths: " + deaths.get(p));
            p.sendMessage(getCtColor() + "=====================");

            p.setHealth(20);

            //p.setDisplayName(ChatColor.AQUA + p.getDisplayName());
        }
        for(Player p : ts)
        {
            p.setGameMode(GameMode.SURVIVAL);

            pendingIsCT = false;
            playerTelePending = p;
            teleportPlayer();
            if(p.getInventory().getItem(0) == null || switched)
            {
                ItemStack itemStack = new ItemStack(Material.WOOD_SWORD);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setUnbreakable(true);
                itemStack.setItemMeta(meta);
                p.getInventory().setItem(0, itemStack);
            }

            p.getInventory().setItem(8, null);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Constants.ROUND_START, 10, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Constants.ROUND_START, 128, false, false));
            p.sendMessage(getCtColor() + "=====================");
            p.sendMessage(getTColor()  + "Money: " + money.get(p) + "G");
            p.sendMessage(getTColor()  + "Kills: " + kills.get(p));
            p.sendMessage(getTColor()  + "Deaths: " + deaths.get(p));
            p.sendMessage(getCtColor() + "=====================");

            p.setHealth(20);

            //p.setDisplayName(ChatColor.GOLD + p.getDisplayName());
        }

        if(ts.size() > 0)
        {
            Random random = new Random();

            ArrayList<String> lore = new ArrayList<>();
            lore.add(LangLoader.get("map.game.item.bomb.lore1"));
            lore.add(LangLoader.get("map.game.item.bomb.lore2"));
            lore.add(LangLoader.get("map.game.item.bomb.lore3"));
            lore.add(LangLoader.get("map.game.item.bomb.lore4"));

            ItemStack tnt = new ItemStack(Material.TNT);
            ItemMeta meta = tnt.getItemMeta();
            meta.setDisplayName(getTColor() + LangLoader.get("map.game.item.bomb"));
            meta.setLore(lore);
            tnt.setItemMeta(meta);

            ts.get(random.nextInt(ts.size())).getInventory().setItem(8, tnt);
        }

        setupTimer(BarColor.YELLOW);
        visual = new RepeatingCountdownVisual(plugin, this, Constants.ROUND_START);
        roundStart = new TimerRoundStart(plugin, this);
    }

    public void setCanBuy(boolean canBuy) { this.canBuy = canBuy; }

    public boolean canBuy() { return canBuy; }

    public int getMoney(Player p) { return money.get(p); }

    public void itemBought(Player p, int price) { if(!warmup) money.put(p, money.get(p)-price); }

    private TimerRoundCountdown countdown;
    private RepeatingCountdownVisual visual;

    public BossBar getTimer() { return timer; }

    public void setupTimer(BarColor color)
    {
        timer.setVisible(false);

        timer = plugin.getServer().createBossBar(getTColor() + tScore + ChatColor.RESET.toString() + " : " + getCtColor() + ctScore, color, BarStyle.SOLID);

        for(Player player : cts)
            timer.addPlayer(player);

        for(Player player : ts)
            timer.addPlayer(player);

        timer.setVisible(true);
        timer.setProgress(1);
    }

    public void startCountdown()
    {
        canPlayersMove = true;
        setupTimer(BarColor.GREEN);

        countdown = new TimerRoundCountdown(plugin, this);
        visual = new RepeatingCountdownVisual(plugin, this, Constants.ROUND_LENGTH);
        new TimerDisableShop(plugin, this);

        for(Player p : players)
        {
            p.removePotionEffect(PotionEffectType.SLOW);
            p.removePotionEffect(PotionEffectType.JUMP);
        }
    }

    public static Map getMapForPlayer(Player p)
    {
        Set<String> keys = CSMinecraft.MAPS.keySet();
        Map map = null;

        for(String key : keys)
        {
            if(CSMinecraft.MAPS.get(key).hasPlayer(p))
            {
                map = CSMinecraft.MAPS.get(key);
                break;
            }
        }

        return map;
    }

    private static int capMoney(int money) { return money < 0 ? 0 : Constants.MONEY_CAP < money ? Constants.MONEY_CAP : money; }

    public void onPlayerKill(Player died, Player killer)
    {
        boolean isCt = cts.contains(died);

        if(isCt)
        {
            deadCts.add(died);

            int xDiff = Math.abs(bounds[4][0] - bounds[4][3]);
            int zDiff = Math.abs(bounds[4][2] - bounds[4][5]);

            Random rand = new Random();

            int spawnX = rand.nextInt(xDiff) + bounds[4][0];
            int spawnY = bounds[4][1];
            int spawnZ = rand.nextInt(zDiff) + bounds[4][2];

            died.setBedSpawnLocation(new Location(world, spawnX+.5, spawnY, spawnZ+.5), true);
        }
        else
        {
            deadTs.add(died);

            int xDiff = Math.abs(bounds[3][0] - bounds[3][3]);
            int zDiff = Math.abs(bounds[3][2] - bounds[3][5]);

            Random rand = new Random();

            int spawnX = rand.nextInt(xDiff) + bounds[3][0];
            int spawnY = bounds[3][1];
            int spawnZ = rand.nextInt(zDiff) + bounds[3][2];

            died.setBedSpawnLocation(new Location(world, spawnX+.5, spawnY, spawnZ+.5), true);
        }

        deaths.put(died, deaths.get(died)+1);
        if(killer != null) kills.put(killer, kills.get(killer)+1);

        if(deadCts.size() == cts.size() && cts.size() != 0)
            plugin.getServer().getPluginManager().callEvent(new EventTWin(plugin, this));
        else if(deadTs.size() == ts.size() && ts.size() != 0)
        {
            if(bombLoc == null)
                plugin.getServer().getPluginManager().callEvent(new EventCTWin(plugin, this));
        }
    }

    public void addMoneyToPlayer(Player player, int money, String reason)
    {
        Integer currentMoney = this.money.get(player);
        currentMoney += money;
        currentMoney = capMoney(currentMoney);
        this.money.put(player, currentMoney);
        player.sendMessage(ChatColor.YELLOW.toString() + (money < 0 ? money : "+" + money) + ChatColor.GREEN.toString() + " (" + reason + ")");
    }

    public static String getCtColor() { return ChatColor.AQUA.toString(); }

    public static String getTColor() { return ChatColor.GOLD.toString(); }

    public boolean isCT(Player player) { return cts.contains(player); }

    public boolean isValidBombPlacement(Location loc)
    {
        int[] bombA = new int[6];
        int[] bombB = new int[6];
        int[] blockPos = {loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()};

        System.arraycopy(bounds[1], 0, bombA, 0, bounds[1].length);
        System.arraycopy(bounds[2], 0, bombB, 0, bounds[1].length);

        if((Numeric.between(bombA[0], blockPos[0], bombA[3]) && Numeric.between(bombA[1], blockPos[1], bombA[4]) && Numeric.between(bombA[2], blockPos[2], bombA[5])) ||
            Numeric.between(bombB[0], blockPos[0], bombB[3]) && Numeric.between(bombB[1], blockPos[1], bombB[4]) && Numeric.between(bombB[2], blockPos[2], bombB[5]))
        //if(Numeric.between(bombALoc1, loc, bombALoc2) || Numeric.between(bombBLoc1, loc, bombBLoc2))
        {
            countdown.cancel();
            return true;
        }

        return false;
    }

    public void stopGame()
    {
        CSMinecraft.PLAYERS_IN_GAME.removeAll(players);

        for(Player player : players)
        {
            player.teleport(ConfigValues.hubLoc);
            player.setGameMode(GameMode.SURVIVAL);
        }

        cts.clear();
        ts.clear();
        deadCts.clear();
        deadTs.clear();
        players.clear();

        money.clear();
        kills.clear();
        deaths.clear();

        ctScore = 0;
        tScore = 0;

        playerTelePending = null;
        defuser = null;
        planter = null;

        pendingIsCT = null;
        bombLoc = null;
        pendingLoc = null;

        isGoing = false;
        defusing = false;
        inRound = false;
        canBuy = true;
        warmup = true;
        gameDone = false;
        switched = false;

        if(explode != null && !explode.isCancelled()) explode.cancel();
        if(visual != null && !visual.isCancelled()) visual.cancel();
        if(countdown != null && !countdown.isCancelled()) countdown.cancel();
        if(roundStart != null && !roundStart.isCancelled()) roundStart.cancel();
        if(warning != null && !warning.isCancelled()) warning.cancel();
        if(beepStage != null && !beepStage.isCancelled()) beepStage.cancel();

        timer.setVisible(false);
    }

    public void resetPlayer(Player player)
    {
        playerTelePending = player;
        pendingIsCT = isCT(player);

        teleportPlayer();
    }

    public void setupBombPlaced()
    {
        for(Player player : players)
        {
            player.sendTitle(ChatColor.RED + LangLoader.get("map.game.bomb_placed.title"), ChatColor.RED + LangLoader.get("map.game.bomb_placed.sub"), 20, 40, 20);
        }

        explode = new TimerBombExplode(plugin, this);
        warning = new RepeatBombWarning(plugin, this);
        beepStage = new RepeatingBeepStage1(plugin, this);
    }

    public int[] getMainBounds() { return bounds[0]; }

    public Location getBombLoc() { return bombLoc; }

    public void setBombLoc(Location bombLoc)
    {
        this.bombLoc = bombLoc;
        pendingLoc = null;
    }

    public Location getPendingLoc() { return pendingLoc; }

    public void setPendingLoc(Location pendingLoc) { this.pendingLoc = pendingLoc; }

    public World getWorld() { return world; }

    public boolean isWarmup() { return warmup; }

    public RepeatingCountdownVisual getVisual() { return visual; }

    public TimerRoundCountdown getCountdown() { return countdown; }

    public boolean canPlayersMove() { return canPlayersMove; }
}
