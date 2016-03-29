package com.grim3212.mc.decor.item;

import java.util.List;

import com.grim3212.mc.core.client.RenderHelper;
import com.grim3212.mc.core.part.IPartItems;
import com.grim3212.mc.core.util.RecipeHelper;
import com.grim3212.mc.decor.GrimDecor;
import com.grim3212.mc.decor.block.DecorBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class DecorItems implements IPartItems {

	public static Item glass_shard;
	public static Item frame;

	public static List<IRecipe> frames;

	@Override
	public void initItems() {
		glass_shard = (new Item()).setUnlocalizedName("glass_shard").setCreativeTab(GrimDecor.INSTANCE.getCreativeTab());
		frame = new ItemFrame().setUnlocalizedName("frame").setCreativeTab(GrimDecor.INSTANCE.getCreativeTab());

		GameRegistry.registerItem(glass_shard, "glass_shard");
		GameRegistry.registerItem(frame, "frame");
	}

	@Override
	public void addRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.glass, 1), new Object[] { "##", "##", '#', glass_shard }));
		DecorBlocks.lights.add(RecipeHelper.getLatestIRecipe());

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(frame, 2, 0), new Object[] { "  #", " # ", "#  ", '#', "plankWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(frame, 2, 1), new Object[] { "  #", " # ", "#  ", '#', "ingotIron" }));
		frames = RecipeHelper.getLatestIRecipes(2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderItems() {
		RenderHelper.renderItem(glass_shard);
		RenderHelper.renderVariantForge(frame, new String[] { "wood", "iron" });
	}

}
