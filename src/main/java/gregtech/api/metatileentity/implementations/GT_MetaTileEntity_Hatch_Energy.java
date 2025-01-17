package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_Hatch_Energy extends GT_MetaTileEntity_Hatch {

    private boolean isCreativeHatch = false;
    public GT_MetaTileEntity_Hatch_Energy(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, new String[]{"Energy Injector for Multiblocks", "Accepts up to 2 Amps"});
    }

    public GT_MetaTileEntity_Hatch_Energy(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Energy(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Energy(String aName, int aTier, String aDescription, ITexture[][][] aTextures, boolean isCreative) {
        super(aName, aTier, 0, aDescription, aTextures);
        isCreativeHatch = isCreative;
    }

    public GT_MetaTileEntity_Hatch_Energy(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, boolean isCreative) {
        super(aName, aTier, 0, aDescription, aTextures);
        isCreativeHatch = isCreative;
    }

    public GT_MetaTileEntity_Hatch_Energy(int aID, String aName, String aNameRegional, int aTier, boolean isCreative) {
        super(aID, aName, aNameRegional, aTier, 0, new String[]{"Energy Injector for Multiblocks", "Accepts up to 2 Amps"});
        isCreativeHatch = isCreative;
    }

    @Override
    public long getEUVar() {
        if(isCreativeHatch) {
            return maxEUStore();
        } else {
            return super.getEUVar();
        }
    }

    public boolean isCreativeHatch() {
        return isCreativeHatch;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier]};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier] * 8;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Energy(mName, mTier, mDescriptionArray, mTextures, isCreativeHatch);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }
}