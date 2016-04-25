package com.grim3212.mc.industry.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemLowGravityController extends Item {

	public boolean On;
	private int tickCount;

	public ItemLowGravityController() {
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (this.tickCount > 10) {
			this.On = (!this.On);
			this.tickCount = 0;
		}
		return par1ItemStack;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
		if (this.On) {
			List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) entityIn.getPosition().getX(), (double) entityIn.getPosition().getY(), (double) entityIn.getPosition().getZ(), (double) (entityIn.getPosition().getX() + 1), (double) (entityIn.getPosition().getY() + 1), (double) (entityIn.getPosition().getZ() + 1)).expand(16.0D, 16.0D, 16.0D));
			
			for (int i = 0; i < entityList.size(); i++) {
				if (((Entity) entityList.get(i)).motionY < 2.0D)
					((Entity) entityList.get(i)).motionY += 0.038D;
			}
		}
		this.tickCount += 1;
	}
}