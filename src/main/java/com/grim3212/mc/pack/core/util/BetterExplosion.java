package com.grim3212.mc.pack.core.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BetterExplosion extends Explosion {

	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3d> playerKnockbackMap;

	@SideOnly(Side.CLIENT)
	public BetterExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
		this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
	}

	@SideOnly(Side.CLIENT)
	public BetterExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking, List<BlockPos> affectedPositions) {
		this(worldIn, entityIn, x, y, z, size, flaming, smoking);
		this.affectedBlockPositions.addAll(affectedPositions);
	}

	public BetterExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking) {
		super(worldIn, entityIn, x, y, z, size, flaming, smoking);
		this.affectedBlockPositions = Lists.<BlockPos> newArrayList();
		this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d> newHashMap();
		this.worldObj = worldIn;
		this.exploder = entityIn;
		this.explosionSize = size;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA(boolean destroyBlocks, boolean hurtEntities) {
		if (destroyBlocks) {
			Set<BlockPos> set = Sets.<BlockPos> newHashSet();

			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < 16; ++k) {
					for (int l = 0; l < 16; ++l) {
						if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
							double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
							double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
							double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
							double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
							d0 = d0 / d3;
							d1 = d1 / d3;
							d2 = d2 / d3;
							float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
							double d4 = this.explosionX;
							double d6 = this.explosionY;
							double d8 = this.explosionZ;

							for (; f > 0.0F; f -= 0.22500001F) {
								BlockPos blockpos = new BlockPos(d4, d6, d8);
								IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

								if (iblockstate.getMaterial() != Material.AIR) {
									float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity) null, this);
									f -= (f2 + 0.3F) * 0.3F;
								}

								if (f > 0.0F && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
									set.add(blockpos);
								}

								d4 += d0 * 0.30000001192092896D;
								d6 += d1 * 0.30000001192092896D;
								d8 += d2 * 0.30000001192092896D;
							}
						}
					}
				}
			}

			this.affectedBlockPositions.addAll(set);
		}

		if (hurtEntities) {
			float f3 = this.explosionSize * 2.0F;
			int k1 = MathHelper.floor_double(this.explosionX - (double) f3 - 1.0D);
			int l1 = MathHelper.floor_double(this.explosionX + (double) f3 + 1.0D);
			int i2 = MathHelper.floor_double(this.explosionY - (double) f3 - 1.0D);
			int i1 = MathHelper.floor_double(this.explosionY + (double) f3 + 1.0D);
			int j2 = MathHelper.floor_double(this.explosionZ - (double) f3 - 1.0D);
			int j1 = MathHelper.floor_double(this.explosionZ + (double) f3 + 1.0D);
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
			net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
			Vec3d vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);

			for (int k2 = 0; k2 < list.size(); ++k2) {
				Entity entity = (Entity) list.get(k2);

				if (!entity.isImmuneToExplosions()) {
					double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) f3;

					if (d12 <= 1.0D) {
						double d5 = entity.posX - this.explosionX;
						double d7 = entity.posY + (double) entity.getEyeHeight() - this.explosionY;
						double d9 = entity.posZ - this.explosionZ;
						double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

						if (d13 != 0.0D) {
							d5 = d5 / d13;
							d7 = d7 / d13;
							d9 = d9 / d13;
							double d14 = (double) this.worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
							double d10 = (1.0D - d12) * d14;
							entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
							double d11 = 1.0D;

							if (entity instanceof EntityLivingBase) {
								d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
							}

							entity.motionX += d5 * d11;
							entity.motionY += d7 * d11;
							entity.motionZ += d9 * d11;

							if (entity instanceof EntityPlayer) {
								EntityPlayer entityplayer = (EntityPlayer) entity;

								if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
									this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
								}
							}
						}
					}
				}
			}
		}
	}
}