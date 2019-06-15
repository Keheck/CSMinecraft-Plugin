package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import io.github.keheck.csminecraft.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ListenerShopActions implements Listener
{
    @EventHandler
    public void displayShop(PlayerToggleSneakEvent event)
    {
        if(event.getPlayer() != null)
        {
            Player player = event.getPlayer();

            if(CSMinecraft.PLAYERS_IN_GAME.contains(player))
            {
                Map map = Map.getMapForPlayer(player);

                if(map.canBuy())
                {
                    ItemStack money = new ItemStack(Material.GOLD_INGOT, map.getMoney(player));
                    ItemMeta monMeta = money.getItemMeta();
                    monMeta.setDisplayName(Map.getTColor() + "MONEY");
                    money.setItemMeta(monMeta);

                    ItemStack weapons = new ItemStack(Material.IRON_SWORD);
                    ItemMeta wepMeta = weapons.getItemMeta();
                    wepMeta.setDisplayName(ChatColor.GRAY.toString() + "WEAPONS");
                    weapons.setItemMeta(wepMeta);

                    ItemStack arrows = new ItemStack(Material.ARROW);
                    ItemMeta arrMeta = arrows.getItemMeta();
                    arrMeta.setDisplayName(Map.getTColor() + "ARROWS");
                    arrows.setItemMeta(arrMeta);

                    ItemStack utility = new ItemStack(Material.POTION);
                    ItemMeta utiMeta = utility.getItemMeta();
                    utiMeta.setDisplayName(Map.getCtColor() + "Utility");
                    ((PotionMeta)utiMeta).addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 0, 0), true);
                    utility.setItemMeta(utiMeta);

                    Inventory inventory;

                    if(map.isCT(player))
                        inventory = Bukkit.createInventory(null, 45, "CT-Shop");
                    else
                        inventory = Bukkit.createInventory(null, 45, "T-Shop");

                    inventory.setItem(36, money);
                    inventory.setItem(20, weapons);
                    inventory.setItem(22, arrows);
                    inventory.setItem(24, utility);

                    player.openInventory(inventory);
                }
            }
        }
    }

    @EventHandler
    public void advance(InventoryClickEvent event)
    {
        Inventory inv = event.getInventory();

        if((inv.getName().equals("CT-Shop") || inv.getName().equals("T-Shop")) && event.getWhoClicked() instanceof Player)
        {
            Material material = event.getCurrentItem() != null ? event.getCurrentItem().getType() : null;
            Player player = (Player)event.getWhoClicked();
            Map map = Map.getMapForPlayer(player);

            ItemStack money = new ItemStack(Material.GOLD_INGOT, map.getMoney(player));
            ItemMeta monMeta = money.getItemMeta();
            monMeta.setDisplayName(Map.getTColor() + "MONEY");
            money.setItemMeta(monMeta);

            if(material != null)
            {
                switch (material)
                {
                    case IRON_SWORD:
                        Inventory newInvWep = Bukkit.createInventory(null, 45, "Weapons");
                        new ShopItem(Material.WOOD_SWORD, 3, 2).addToInv(newInvWep);
                        new ShopItem(Material.STONE_SWORD, 12, 3).addToInv(newInvWep);
                        new ShopItem(Material.IRON_SWORD, 24, 4).addToInv(newInvWep);
                        new ShopItem(Material.DIAMOND_SWORD, 35, 5).addToInv(newInvWep);
                        new ShopItem(Material.BOW, 13, 6).addToInv(newInvWep);

                        newInvWep.setItem(36, money);
                        event.getWhoClicked().openInventory(newInvWep);
                        break;
                    case ARROW:
                        Inventory newInvArr = Bukkit.createInventory(null, 45, "Arrows");
                        //new ShopItem(Material.TNT, 5, 3).addToInv(newInvArr);

                        ItemStack poisArr = new ItemStack(Material.TIPPED_ARROW);
                        PotionMeta poisArrMeta = (PotionMeta)poisArr.getItemMeta();
                        poisArrMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, false, false), true);
                        poisArr.setItemMeta(poisArrMeta);
                        newInvArr.setItem(22, poisArr);
                        newInvArr.setItem(31, new ItemStack(Material.GOLD_INGOT, 16));

                        new ShopItem(Material.SPECTRAL_ARROW, 13, 5).addToInv(newInvArr);

                        newInvArr.setItem(36, money);
                        player.openInventory(newInvArr);
                        break;
                    case POTION:
                        Inventory newInvUti = Bukkit.createInventory(null, 45, "Utility");
                        if(map.isCT(player))
                        {
                            new ShopItem(Material.IRON_HOE, 10, 3).setCustomName(Map.getCtColor() + "Defuser").addToInv(newInvUti);
                            new ShopItem(Material.IRON_CHESTPLATE, 13, 5).addToInv(newInvUti);
                        }
                        else
                        {
                            new ShopItem(Material.IRON_CHESTPLATE, 13, 4).addToInv(newInvUti);
                        }
                        newInvUti.setItem(36, money);
                        player.openInventory(newInvUti);
                        break;
                    case GOLD_INGOT:
                        player.openInventory(inv);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void buy(InventoryClickEvent event)
    {
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        PlayerInventory playerInv = player.getInventory();
        Map map = Map.getMapForPlayer(player);

        switch(inv.getName())
        {
            case "Weapons":
                if(event.getCurrentItem().getType() == Material.GOLD_INGOT)
                {
                    player.openInventory(inv);
                    break;
                }

                if(map.getMoney(player) >= inv.getItem(event.getSlot()+9).getAmount())
                {
                    map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());

                    ItemStack item = event.getCurrentItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                    playerInv.setItem(item.getType() == Material.BOW ? 1 : 0, item);
                    player.closeInventory();
                }
                break;
            case "Arrows":
                if(event.getCurrentItem().getType() == Material.GOLD_INGOT)
                {
                    player.openInventory(inv);
                    break;
                }

                if(map.getMoney(player) >= inv.getItem(event.getSlot()+9).getAmount())
                {
                    map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                    playerInv.setItem(2, event.getCurrentItem());

                    /*if(event.getCurrentItem().getType() == Material.TNT)
                    {
                        playerInv.setItem(29, new ItemStack(Material.ARROW));
                    }*/

                    player.closeInventory();
                }
                break;
            case "Utility":
                if(event.getCurrentItem().getType() == Material.GOLD_INGOT)
                {
                    player.openInventory(inv);
                    break;
                }

                if(map.isCT(player))
                {
                    if(map.getMoney(player) >= inv.getItem(event.getSlot()+9).getAmount())
                    {
                        ItemStack item = event.getCurrentItem();

                        if(item.getType() == Material.IRON_HOE)
                        {
                            if(playerInv.getItem(8) != null)
                            {
                                player.openInventory(inv);
                                return;
                            }

                            map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                            playerInv.setItem(8, item);
                            player.closeInventory();
                        }
                        else if(item.getType() == Material.IRON_CHESTPLATE)
                        {
                            if(player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null)
                            {
                                player.openInventory(inv);
                                return;
                            }

                            map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20000000, 2, false, false));
                            player.closeInventory();
                        }
                    }
                }
                else
                {
                    if(map.getMoney(player) >= inv.getItem(event.getSlot()+9).getAmount())
                    {
                        ItemStack item = event.getCurrentItem();

                        if(item.getType() == Material.IRON_CHESTPLATE)
                        {
                            if(player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null)
                            {
                                player.openInventory(inv);
                                return;
                            }

                            map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20000000, 1, false, false));
                            player.closeInventory();
                        }
                    }
                }
        }
    }
}
