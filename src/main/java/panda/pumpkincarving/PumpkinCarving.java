package panda.pumpkincarving;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PumpkinCarving.MODID, name = PumpkinCarving.NAME, version = PumpkinCarving.VERSION)
public class PumpkinCarving {

	public static final String MODID = "pumpkincarving";
	public static final String NAME = "Pumpkin Carving";
	public static final String VERSION = "0.1";
	
	public static Block carvedPumpkin = new BlockCarvedPumpkin();
	public static Block carvedPumpkinLit = new BlockCarvedPumpkin().setLightLevel(1.0F);


	@Instance(MODID)
	public static PumpkinCarving instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		addBlock(carvedPumpkin,"carvedpumpkin");
		addBlock(carvedPumpkinLit,"litcarvedpumpkin");
		
		if (event.getSide() == Side.CLIENT) {
			for(int i = 0; i<4; i++){
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(carvedPumpkin), i,new ModelResourceLocation(new ResourceLocation(PumpkinCarving.MODID, "carvedpumpkin"), "face="+i+",facing=north"));
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(carvedPumpkinLit), i,new ModelResourceLocation(new ResourceLocation(PumpkinCarving.MODID, "litcarvedpumpkin"), "face="+i+",facing=north"));
			}
		}
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		
		

		removeRecipe(Item.getItemFromBlock(Blocks.LIT_PUMPKIN));
		
		for(int m = 0; m<4; m++){
			GameRegistry.addRecipe(new ItemStack(carvedPumpkinLit, 1,m), new Object[] {"A", "B", 'A', new ItemStack(carvedPumpkin,1,m), 'B', Blocks.TORCH});
		}
		
		
		GameRegistry.addRecipe(new ItemStack(carvedPumpkin,1,0), new Object[] {"  ", "BA", 'A', Blocks.PUMPKIN, 'B', Items.SHEARS});
		GameRegistry.addRecipe(new ItemStack(carvedPumpkin,1,1), new Object[] {"B ", " A", 'A', Blocks.PUMPKIN, 'B', Items.SHEARS});
		GameRegistry.addRecipe(new ItemStack(carvedPumpkin,1,2), new Object[] {" B", " A", 'A', Blocks.PUMPKIN, 'B', Items.SHEARS});
		GameRegistry.addRecipe(new ItemStack(carvedPumpkin,1,3), new Object[] {" A", " B", 'A', Blocks.PUMPKIN, 'B', Items.SHEARS});
		
		
		GameRegistry.addRecipe(new ItemStack(Items.PUMPKIN_SEEDS, 4), new Object[] {"M", 'M', carvedPumpkin});
		GameRegistry.addShapelessRecipe(new ItemStack(Items.PUMPKIN_PIE), new Object[] {carvedPumpkin, Items.SUGAR, Items.EGG});

	}

	private static Block addBlock(Block block, String name) {

		block.setRegistryName(name);
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);
		GameRegistry.register(block);
		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(name);
		itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);
		GameRegistry.register(itemBlock);

		block.setCreativeTab(CreativeTabs.DECORATIONS);

		return block;
	}
	
	public static void removeRecipe(Item item)
	{
	
		List<IRecipe> recipies = CraftingManager.getInstance().getRecipeList();
	
		Iterator<IRecipe> remover = recipies.iterator();
		
		while (remover.hasNext())
		{
			ItemStack itemstack = remover.next().getRecipeOutput();
			
			if(itemstack != null && itemstack.getItem() == item)
			{
				remover.remove();
			}
			
			
		}
		
	}
}