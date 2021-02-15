package panda.pumpkincarving;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

@EventBusSubscriber
@Mod(modid = PumpkinCarving.MODID, name = PumpkinCarving.NAME, version = PumpkinCarving.VERSION)
public class PumpkinCarving {

	public static final String MODID = "pumpkincarving";
	public static final String NAME = "Pumpkin Carving";
	public static final String VERSION = "1.4.0";
	
	public static final Block carvedPumpkin = new BlockCarvedPumpkin();
	public static final Block carvedPumpkinLit = new BlockCarvedPumpkin().setLightLevel(1.0F);


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		addBlock(carvedPumpkin,"carvedpumpkin");
		addBlock(carvedPumpkinLit,"litcarvedpumpkin");
		
		if (event.getSide() == Side.CLIENT) {
			for(int i = 0; i < 4; i++){
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(carvedPumpkin), i, new ModelResourceLocation(new ResourceLocation(PumpkinCarving.MODID, "carvedpumpkin"), "face="+ i +",facing=north"));
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(carvedPumpkinLit), i, new ModelResourceLocation(new ResourceLocation(PumpkinCarving.MODID, "litcarvedpumpkin"), "face="+ i +",facing=north"));
			}
		}
	}
	
	@SubscribeEvent
	  public static void registerRecipes(Register<IRecipe> event) {
	    IRecipe recipe = CraftingManager.getRecipe(new ResourceLocation("minecraft:lit_pumpkin"));
		if (recipe != null)
		{
			event.getRegistry().register(new FakeRecipe(recipe));
		}	  
	}

	private static Block addBlock(Block block, String name) {

		block.setRegistryName(name);
		block.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);

		ForgeRegistries.BLOCKS.register(block);
		final ItemBlock itemBlock = new ItemBlockPumpkin(block);
		itemBlock.setRegistryName(name);

		itemBlock.setUnlocalizedName(block.getRegistryName().getResourceDomain() + "." + name);
		ForgeRegistries.ITEMS.register(itemBlock);
		return block;
	}
	
}