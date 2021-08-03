package idealindustrial.teststuff.testTile;

import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import idealindustrial.tile.base.II_BaseMachineTile;
import idealindustrial.tile.base.II_BaseTile;
import idealindustrial.tile.gui.base.II_GenericGuiContainer;
import idealindustrial.tile.meta.II_BaseMetaTile_Facing1Output;
import idealindustrial.tile.meta.II_MetaTile;
import idealindustrial.util.inventory.II_ArrayRecipedInventory;
import idealindustrial.util.inventory.II_EmptyInventory;
import idealindustrial.util.misc.II_Paths;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.stream.Stream;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class II_TestMachine extends II_BaseMetaTile_Facing1Output<II_BaseMachineTile> {

    public II_TestMachine(II_BaseMachineTile baseTile) {
        super(baseTile, "test",
                Stream.of(MACHINE_BRONZE_BOTTOM, MACHINE_BRONZE_TOP, MACHINE_BRONZE_SIDE, TURBINE[4],
                        MACHINE_8V_BOTTOM, MACHINE_8V_TOP, MACHINE_8V_SIDE, TURBINE_ACTIVE[4])
                        .map(GT_RenderedTexture::new).toArray(ITexture[]::new),
                Stream.of(MACHINE_BRONZE_BOTTOM, MACHINE_BRONZE_TOP, MACHINE_BRONZE_SIDE, TURBINE[4],
                        MACHINE_8V_BOTTOM, MACHINE_8V_TOP, MACHINE_8V_SIDE, TURBINE_ACTIVE[4])
                        .map(GT_RenderedTexture::new).toArray(ITexture[]::new)
                );
        inventoryIn = new II_ArrayRecipedInventory(1, 64);
        inventoryOut = new II_ArrayRecipedInventory(1, 64);
        inventorySpecial = II_EmptyInventory.INSTANCE;
        hasInventory = true;
    }

    @Override
    public GuiContainer getClientGUI(EntityPlayer player, int internalID) {
        return new II_GenericGuiContainer(new II_TestContainer(getBase(), player), II_Paths.PATH_GUI + "BasicGui.png");
    }

    @Override
    public Container getServerGUI(EntityPlayer player, int internalID) {
        return new II_TestContainer(getBase(), player);
    }

    @Override
    public II_MetaTile<II_BaseMachineTile> newMetaTile(II_BaseMachineTile baseTile) {
        II_TestMachine testMachine = new II_TestMachine(baseTile);
        testMachine.name = name;
        testMachine.baseTextures = baseTextures;
        testMachine.overlays = overlays;
        return testMachine;
    }
}
