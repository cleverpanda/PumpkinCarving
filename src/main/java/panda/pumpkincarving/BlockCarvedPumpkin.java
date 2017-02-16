package panda.pumpkincarving;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;

public class BlockCarvedPumpkin extends BlockHorizontal
{
    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;
    private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>()
    {
        public boolean apply(@Nullable IBlockState state)
        {
            return state != null && (state.getBlock() == PumpkinCarving.carvedPumpkin || state.getBlock() == PumpkinCarving.carvedPumpkinLit);
        }
    };
    
   public static final PropertyInteger FACE = PropertyInteger.create("face", 0,3);

    protected BlockCarvedPumpkin()
    {
        super(Material.GOURD, MapColor.ADOBE);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FACE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }
    
    public boolean canDispenserPlace(World worldIn, BlockPos pos)
    {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos)
    {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos);

        if (blockpattern$patternhelper != null)
        {
            for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i)
            {
                BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                worldIn.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
            }

            EntitySnowman entitysnowman = new EntitySnowman(worldIn);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(entitysnowman);

            for (int j = 0; j < 120; ++j)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (int i1 = 0; i1 < this.getSnowmanPattern().getThumbLength(); ++i1)
            {
                BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(0, i1, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR);
            }
        }
        else
        {
            blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos);

            if (blockpattern$patternhelper != null)
            {
                for (int k = 0; k < this.getGolemPattern().getPalmLength(); ++k)
                {
                    for (int l = 0; l < this.getGolemPattern().getThumbLength(); ++l)
                    {
                        worldIn.setBlockState(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                worldIn.spawnEntityInWorld(entityirongolem);

                for (int j1 = 0; j1 < 120; ++j1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + worldIn.rand.nextDouble(), (double)blockpos.getY() + worldIn.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                for (int k1 = 0; k1 < this.getGolemPattern().getPalmLength(); ++k1)
                {
                    for (int l1 = 0; l1 < this.getGolemPattern().getThumbLength(); ++l1)
                    {
                        BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR);
                    }
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        
    	return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(FACE, meta);
    }
    
    

    @Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
    	IBlockState newstate = this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(FACE, meta);
    	System.out.println(meta);
    	
    	return newstate;
    
    }

	/**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	return getDefaultState().withProperty(FACE, getActualMeta(meta)).withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
    }
    
    public int getActualMeta(int meta)
    { // returns the FACE value for given meta.
    	if(meta >= 0 && meta <= 3) return 0;
    	else if(meta >= 4 && meta <= 7) return 1;
    	else if(meta >= 8 && meta <= 11) return 2;
    	else if(meta >= 12 && meta <= 15) return 3;
    	else return 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING,FACE});
    }
    

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
    	return (state.getValue(FACING).getHorizontalIndex() + state.getValue(FACE) * 4);
    }
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        //list.add(new ItemStack(itemIn));

        	list.add(new ItemStack(itemIn, 1, 0));
        	list.add(new ItemStack(itemIn, 1, 1));
        	list.add(new ItemStack(itemIn, 1, 2));
        	list.add(new ItemStack(itemIn, 1, 3));

    }

    protected BlockPattern getSnowmanBasePattern()
    {
        if (this.snowmanBasePattern == null)
        {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[] {" ", "#", "#"}).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.snowmanBasePattern;
    }

    protected BlockPattern getSnowmanPattern()
    {
        if (this.snowmanPattern == null)
        {
            this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[] {"^", "#", "#"}).where('^', BlockWorldState.hasState(IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.snowmanPattern;
    }

    protected BlockPattern getGolemBasePattern()
    {
        if (this.golemBasePattern == null)
        {
            this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[] {"~ ~", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern()
    {
        if (this.golemPattern == null)
        {
            this.golemPattern = FactoryBlockPattern.start().aisle(new String[] {"~^~", "###", "~#~"}).where('^', BlockWorldState.hasState(IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.golemPattern;
    }
}