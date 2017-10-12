package com.grim3212.mc.pack.world.gen.structure;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class StructureStorage extends WorldSavedData {

	private final List<StructureData> structures = Lists.newArrayList();

	public StructureStorage(String name) {
		super(name);
	}

	public StructureData getStructureData(String structName) {
		for (int i = 0; i < structures.size(); i++) {
			if (structName.equals(structures.get(i).getStructName()))
				return structures.get(i);
		}

		// If no account found, make one and run method again
		setStructureData(new StructureData(structName));
		return getStructureData(structName);
	}

	public void setStructureData(StructureData account) {
		boolean hasData = false;
		for (int i = 0; i < structures.size(); i++) {
			if (account.getStructName().equals(structures.get(i).getStructName())) {
				structures.set(i, account);
				hasData = true;
			}
		}

		if (!hasData)
			structures.add(account);
		markDirty();
	}

	@Override
	public void readFromNBT(@Nonnull NBTTagCompound nbt) {
		if (nbt.hasKey("Structures")) {
			// Clear structure bbs
			structures.clear();

			NBTTagList structList = nbt.getTagList("Structures", NBT.TAG_COMPOUND);

			for (int i = 0; i < structList.tagCount(); i++) {
				NBTTagCompound struct = structList.getCompoundTagAt(i);

				if (struct.hasKey("StructName") && struct.hasKey("BBs")) {
					StructureData data = new StructureData(struct.getString("StructName"));

					NBTTagList bbs = struct.getTagList("BBs", NBT.TAG_INT_ARRAY);
					for (int j = 0; j < bbs.tagCount(); j++) {
						data.getStructures().add(new StructureBoundingBox(bbs.getIntArrayAt(i)));
					}

					// Add the structure data back
					structures.add(data);
				}
			}
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
		NBTTagList structList = new NBTTagList();

		for (StructureData data : structures) {
			NBTTagCompound struct = new NBTTagCompound();

			struct.setString("StructName", data.getStructName());

			NBTTagList tagList = new NBTTagList();
			for (StructureBoundingBox sbb : data.getStructures()) {
				tagList.appendTag(sbb.toNBTTagIntArray());
			}
			struct.setTag("BBs", tagList);

			structList.appendTag(struct);
		}

		nbt.setTag("Structures", structList);

		return nbt;
	}

	public void addBBSave(String structName, StructureBoundingBox bb) {
		getStructureData(structName).getStructures().add(bb);
		markDirty();
	}

	public static final String IDENTIFIER = "GrimStructures";

	public static StructureStorage getStructureStorage(World world) {
		StructureStorage data = (StructureStorage) world.getPerWorldStorage().getOrLoadData(StructureStorage.class, IDENTIFIER);
		if (data == null) {
			data = new StructureStorage(IDENTIFIER);
			world.getPerWorldStorage().setData(IDENTIFIER, data);
		}
		return data;
	}
}
