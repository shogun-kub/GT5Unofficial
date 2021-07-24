package idealindustrial.itemgen.material.submaterial.render;

import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;

import java.awt.*;

public abstract class RenderInfo {

    protected Color color;

    public RenderInfo(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getColorAsInt() {
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    //for solid
    public abstract TextureSet getTextureSet();
    //forFluidForms
    public abstract IIconContainer getTexture();

}