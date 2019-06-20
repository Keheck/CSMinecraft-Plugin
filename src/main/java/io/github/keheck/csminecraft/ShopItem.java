package io.github.keheck.csminecraft;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItem
{
    private Material item;
    private int price;
    private int position;
    private String customName;

    public ShopItem(Material item, int price, int position)
    {
        this.price = price;
        this.item = item;
        this.position = position;
    }

    public ShopItem setCustomName(String customName)
    {
        if(customName != null && !customName.equals(""))
        {
            this.customName = customName;
        }

        return this;
    }

    public void addToInv(Inventory inv)
    {
        ItemStack itemStack = new ItemStack(item);
        if(customName != null)
        {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(customName);
            itemStack.setItemMeta(meta);
        }

        inv.setItem(18+position, new ItemStack(item));

        ItemStack itemPrice = new ItemStack(Material.GOLD_INGOT, price);
        inv.setItem(27+position, itemPrice);
    }
}
