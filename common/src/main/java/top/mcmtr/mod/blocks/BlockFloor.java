package top.mcmtr.mod.blocks;


public class BlockFloor extends BlockChangeModelBase{
    public BlockFloor() {
        super(1, Properties.of().requiresCorrectToolForDrops().strength(2.0F));
    }
}