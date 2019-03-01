package com.grim3212.mc.pack.core.item;

import java.util.Set;

import com.grim3212.mc.pack.GrimPack;
import com.grim3212.mc.pack.core.manual.IManualEntry.IManualItem;
import com.grim3212.mc.pack.core.manual.pages.Page;

import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

public class ItemManualTool extends ItemTool implements IManualItem {

	protected ItemManualTool(String name, IItemTier tier, Set<Block> effectiveBlocksIn, Item.Properties props) {
		this(name, 0.0f, 0.0f, tier, effectiveBlocksIn, props);
	}

	protected ItemManualTool(String name, float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Item.Properties props) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, props);
		setRegistryName(new ResourceLocation(GrimPack.modID, name));
	}

	@Override
	public Page getPage(ItemStack stack) {
		return null;
	}

}
