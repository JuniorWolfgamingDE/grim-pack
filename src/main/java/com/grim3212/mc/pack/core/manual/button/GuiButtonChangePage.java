package com.grim3212.mc.pack.core.manual.button;

import com.grim3212.mc.pack.core.manual.gui.GuiManualIndex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonChangePage extends GuiButton {

	private boolean isRight;

	public GuiButtonChangePage(int id, int j, int k, boolean isRight) {
		super(id, j, k, 18, 10, "");
		this.isRight = isRight;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getInstance().textureManager.bindTexture(GuiManualIndex.texture);
			int k = 211;
			int l = 0;

			if (flag) {
				l += 10;
			}

			if (this.isRight) {
				k += 18;
			}

			this.drawTexturedModalRect(this.x, this.y, k, l, 18, 10);
		}
	}
}
