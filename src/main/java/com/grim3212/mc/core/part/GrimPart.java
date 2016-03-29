package com.grim3212.mc.core.part;

import com.grim3212.mc.core.config.GrimConfig;
import com.grim3212.mc.core.manual.ManualRegistry;
import com.grim3212.mc.core.manual.ModSection;
import com.grim3212.mc.core.proxy.CommonProxy;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class GrimPart {

	public static final String CLIENT_PROXY = "com.grim3212.mc.core.proxy.ClientProxy";
	public static final String COMMON_PROXY = "com.grim3212.mc.core.proxy.CommonProxy";

	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
	public static CommonProxy proxy;

	private String modid;
	private String name;
	private String version;
	private GrimConfig config;
	private GrimPartCreativeTab creativeTab;
	private ModSection modSection;
	private IPartItems[] items;
	private IPartTileEntities tileentities;
	private IPartEntities entities;

	public GrimPart(String modid, String name, String version) {
		this.modid = modid;
		this.name = name;
		this.version = version;
		this.config = setConfig();
		this.items = setItemParts();
		this.entities = setEntities();
		this.tileentities = setTileEntities();
		this.creativeTab = new GrimPartCreativeTab(this);
		ManualRegistry.registerMod(modSection = new ModSection(getName(), getModid()));
	}

	protected IPartEntities setEntities() {
		return null;
	}

	protected IPartTileEntities setTileEntities() {
		return null;
	}

	protected abstract IPartItems[] setItemParts();

	/**
	 * Make sure to add @EventHandler when overridden as well as call super
	 * 
	 * Registers the config change event
	 * 
	 * @param FMLPreInitializationEvent
	 *            event
	 */
	public void preInit(FMLPreInitializationEvent event) {
		ModMetadata data = event.getModMetadata();
		data.autogenerated = false;
		data.version = getVersion();
		data.name = getName();
		data.authorList.add("Grim3212");
		data.logoFile = "assets/" + getModid() + "/" + getModid() + ".png";

		MinecraftForge.EVENT_BUS.register(this);

		if (this.items != null) {
			// Initialize all items and blocks first
			for (int i = 0; i < this.items.length; i++) {
				this.items[i].initItems();
			}

			// Then render and create recipes
			for (int i = 0; i < this.items.length; i++) {
				this.items[i].addRecipes();

				if (event.getSide().isClient()) {
					this.items[i].renderItems();
				}
			}
		}

		if (this.entities != null) {
			this.entities.initEntities();

			if (event.getSide().isClient())
				this.entities.renderEntities();
		}

		if (event.getSide().isClient())
			setupManualPages(modSection);
	}

	/**
	 * Make sure to add @EventHandler when overridden
	 * 
	 * @param FMLInitializationEvent
	 *            event
	 */
	public void init(FMLInitializationEvent event) {
		if (this.tileentities != null) {
			this.tileentities.initTileEntities();

			if (event.getSide().isClient())
				this.tileentities.renderTileEntities();
		}
	}

	/**
	 * Make sure to add @EventHandler when overridden
	 * 
	 * @param FMLPostInitializationEvent
	 *            event
	 */
	public void postInit(FMLPostInitializationEvent event) {
	}

	protected abstract Item getCreativeTabIcon();

	protected abstract GrimConfig setConfig();

	protected abstract void setupManualPages(ModSection modSection);

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(this.modid)) {
			this.config.syncConfig();
		}
	}

	public String getModid() {
		return modid;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Configuration getConfig() {
		return config.config;
	}

	public GrimPartCreativeTab getCreativeTab() {
		return creativeTab;
	}
}
