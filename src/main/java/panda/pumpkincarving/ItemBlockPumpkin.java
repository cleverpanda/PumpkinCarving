package panda.pumpkincarving;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockPumpkin extends ItemBlock{

	public ItemBlockPumpkin(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        //list.add(new ItemStack(itemIn));

        	list.add(new ItemStack(itemIn, 1, 0));
        	list.add(new ItemStack(itemIn, 1, 1));
        	list.add(new ItemStack(itemIn, 1, 2));
        	list.add(new ItemStack(itemIn, 1, 3));

    }
    
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!block.isReplaceable(world, pos))
        {
            pos = pos.offset(facing);
        }

        if (stack.stackSize != 0 && player.canPlayerEdit(pos, facing, stack) && world.canBlockBePlaced(this.block, pos, false, facing, null, stack))
        {
            BlockCarvedPumpkin block2 = (BlockCarvedPumpkin) Block.getBlockFromItem(stack.getItem());
            IBlockState state2 = block2.getDefaultState().withProperty(BlockCarvedPumpkin.FACE, stack.getMetadata()).withProperty(BlockCarvedPumpkin.FACING, player.getHorizontalFacing().getOpposite());
            if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, state2))
            {
            	SoundType soundtype = SoundType.WOOD;
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }


}
