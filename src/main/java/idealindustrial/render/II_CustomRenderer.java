package idealindustrial.render;

import gregtech.api.interfaces.IFastRenderedTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

public interface II_CustomRenderer {

    boolean renderWorldBlock(IBlockAccess world, IFastRenderedTileEntity tileEntity, int x, int y, int z, Block block, RenderBlocks renderBlocks);

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Block block, RenderBlocks renderBlocks, int meta);
}
