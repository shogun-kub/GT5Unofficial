package idealindustrial.tile.meta;

public interface IUpdatableTileEntity {

    void onFirstTick(long timer, boolean serverSide);
    void onPreTick(long timer, boolean serverSide);
    void onTick(long timer, boolean serverSide);
    void onPostTick(long timer, boolean serverSide);
}