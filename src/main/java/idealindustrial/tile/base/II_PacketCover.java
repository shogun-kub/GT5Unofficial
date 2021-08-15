package idealindustrial.tile.base;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.net.GT_Packet;
import idealindustrial.tile.interfaces.base.II_BaseTile;
import idealindustrial.util.misc.II_TileUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class II_PacketCover extends GT_Packet {
    int x, y, z, side;
    int coverID;
    long coverValue;

    public II_PacketCover() {
        super(true);
    }

    public II_PacketCover(II_BaseTile tile, int side, int coverID, long coverValue) {
        super(false);
        this.x = tile.getXCoord();
        this.y = tile.getYCoord();
        this.z = tile.getZCoord();
        this.side = side;
        this.coverID = coverID;
        this.coverValue = coverValue;
    }

    public II_PacketCover(int x, int y, int z, int side, int coverID, long coverValue) {
        super(false);
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.coverID = coverID;
        this.coverValue = coverValue;
    }

    @Override
    public byte getPacketID() {
        return 8;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput(5 * 4 + 8);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeInt(side);
        out.writeInt(coverID);
        out.writeLong(coverValue);
        return out.toByteArray();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new II_PacketCover(aData.readInt(), aData.readInt(), aData.readInt(), aData.readInt(), aData.readInt(), aData.readLong());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld != null) {
            TileEntity baseTile = aWorld.getTileEntity(x, y, z);
            if (baseTile instanceof II_BaseTileImpl) {
                ((II_BaseTileImpl) baseTile).setCoverAtSide(side, coverID, coverValue);
            }
        }
    }

}
