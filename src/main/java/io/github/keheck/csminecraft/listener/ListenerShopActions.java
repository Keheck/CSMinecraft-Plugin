package io.github.keheck.csminecraft.listener;

import io.github.keheck.csminecraft.CSMinecraft;
import io.github.keheck.csminecraft.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
                else
                {
                    event.setCancelled(true);
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
                        newInvWep.setItem(20, new ItemStack(Material.WOOD_SWORD));
                        newInvWep.setItem(21, new ItemStack(Material.STONE_SWORD));
                        newInvWep.setItem(22, new ItemStack(Material.IRON_SWORD));
                        newInvWep.setItem(23, new ItemStack(Material.DIAMOND_SWORD));
                        newInvWep.setItem(24, new ItemStack(Material.BOW));

                        newInvWep.setItem(29, new ItemStack(Material.GOLD_INGOT, 3));
                        newInvWep.setItem(30, new ItemStack(Material.GOLD_INGOT, 7));
                        newInvWep.setItem(31, new ItemStack(Material.GOLD_INGOT, 11));
                        newInvWep.setItem(32, new ItemStack(Material.GOLD_INGOT, 15));
                        newInvWep.setItem(33, new ItemStack(Material.GOLD_INGOT, 9));

                        newInvWep.setItem(36, money);
                        event.getWhoClicked().openInventory(newInvWep);
                        break;
                    case ARROW:
                        Inventory newInvArr = Bukkit.createInventory(null, 45, "Arrows");
                        newInvArr.setItem(21, new ItemStack(Material.TNT));
                        ItemStack poisArr = new ItemStack(Material.TIPPED_ARROW);
                        PotionMeta poisArrMeta = (PotionMeta)poisArr.getItemMeta();
                        poisArrMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, false, false), true);
                        poisArr.setItemMeta(poisArrMeta);
                        newInvArr.setItem(22, poisArr);
                        newInvArr.setItem(23, new ItemStack(Material.SPECTRAL_ARROW));

                        newInvArr.setItem(30, new ItemStack(Material.GOLD_INGOT, 7));
                        newInvArr.setItem(31, new ItemStack(Material.GOLD_INGOT, 6));
                        newInvArr.setItem(32, new ItemStack(Material.GOLD_INGOT, 4));

                        newInvArr.setItem(36, money);
                        player.openInventory(newInvArr);
                        break;
                    case POTION:
                        Inventory newInvUti = Bukkit.createInventory(null, 45, "Utility");
                        if(map.isCT(player))
                        {
                            ItemStack defuser = new ItemStack(Material.IRON_HOE);
                            ItemMeta defMeta = defuser.getItemMeta();
                            defMeta.setDisplayName(Map.getCtColor() + "Defuser");
                            defuser.setItemMeta(defMeta);
                            newInvUti.setItem(21, defuser);

                            ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                            newInvUti.setItem(23, armor);

                            newInvUti.setItem(30, new ItemStack(Material.GOLD_INGOT, 4));
                            newInvUti.setItem(32, new ItemStack(Material.GOLD_INGOT, 7));
                        }
                        else
                        {
                            ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                            newInvUti.setItem(22, armor);
                            newInvUti.setItem(31, new ItemStack(Material.GOLD_INGOT, 7));
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

                    if(item.getType() == Material.BOW)
                        item.addEnchantment(Enchantment.ARROW_INFINITE, 1);

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

                    ItemStack item = event.getCurrentItem();
                    if(item.getType() == Material.TNT)
                        playerInv.setItem(29, new ItemStack(Material.ARROW));

                    playerInv.setItem(2, event.getCurrentItem());
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

                        if(item.getType() == Material.IRON_HOE && playerInv.getItem(8) == null)
                        {
                            map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                            playerInv.setItem(8, item);
                            player.closeInventory();
                        }
                        else if(item.getType() == Material.IRON_CHESTPLATE && player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null)
                        {
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

                        if(item.getType() == Material.IRON_CHESTPLATE && player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null)
                        {
                            map.itemBought(player, inv.getItem(event.getSlot()+9).getAmount());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20000000, 1, false, false));
                            player.closeInventory();
                        }
                    }
                }
        }
    }
}
