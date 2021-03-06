package com.agilemods.materiamuto.common.emc;

import com.agilemods.materiamuto.api.emc.EMCRegistryState;
import com.agilemods.materiamuto.api.emc.IEMCItemHandler;
import com.agilemods.materiamuto.api.emc.IEMCMiscHandler;
import com.agilemods.materiamuto.api.emc.StackReference;
import com.agilemods.materiamuto.common.emc.handler.*;
import com.agilemods.materiamuto.common.emc.handler.ae2.AE2CraftingHandler;
import com.agilemods.materiamuto.common.emc.handler.ae2.AE2FacadeHandler;
import com.agilemods.materiamuto.common.emc.handler.ic2.IC2CraftingHandler;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class EMCRegistry {

    private static Map<StackReference, Double> emcMapping = Maps.newHashMap();
    private static Map<String, Double> genericEmcMapping = Maps.newHashMap();

    private static Set<StackReference> blacklist = Sets.newHashSet();

    private static LinkedList<IEMCItemHandler> itemHandlers = Lists.newLinkedList();
    private static LinkedList<IEMCMiscHandler> miscHandlers = Lists.newLinkedList();

    private static final EMCDelegate emcDelegate = new EMCDelegate();

    public static void blacklist(StackReference stackReference) {
        blacklist.add(stackReference);
    }

    public static void registerItemHandler(IEMCItemHandler itemHandler) {
        itemHandlers.add(itemHandler);
    }

    public static void registerMiscHandler(IEMCMiscHandler miscHandler) {
        miscHandlers.add(miscHandler);
    }

    public static void wipeout() {
        emcMapping.clear();
        genericEmcMapping.clear();
        blacklist.clear();
        itemHandlers.clear();
        miscHandlers.clear();
    }

    public static void setEMC(Fluid fluid, double value) {
        setGenericEMC(fluid.getName(), value);
    }

    public static double getEMC(Fluid fluid) {
        return getGenericEMC(fluid.getName());
    }

    public static void setGenericEMC(String ident, double value) {
        genericEmcMapping.put(ident, value);
    }

    public static double getGenericEMC(String ident) {
        Double value = genericEmcMapping.get(ident);
        return value == null ? 0 : value;
    }

    public static void setEMC(Block block, double value) {
        setEMC(new StackReference(block), value, false);
    }

    public static void setEMC(Item item, double value) {
        setEMC(new StackReference(item), value, false);
    }

    public static void setEMC_wild(Block block, double value) {
        setEMC(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    public static void setEMC_wild(Item item, double value) {
        setEMC(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    public static void setEMC(ItemStack itemStack, double value) {
        setEMC(new StackReference(itemStack), value, false);
    }

    public static void setEMC(StackReference stackReference, double value, boolean force) {
        if (!blacklist.contains(stackReference)) {
            if (force) {
                emcMapping.remove(stackReference);
            }
            emcMapping.put(stackReference, value);
        }
    }

    public static double getEMC(Object object) {
        if (object instanceof Block) {
            return getEMC((Block) object);
        }
        if (object instanceof Item) {
            return getEMC((Item) object);
        }
        if (object instanceof ItemStack) {
            return getEMC((ItemStack) object);
        }
        if (object instanceof StackReference) {
            return getEMC((StackReference) object);
        }
        return 0;
    }

    public static double getEMC(Block block) {
        return getEMC(new StackReference(block));
    }

    public static double getEMC(Item item) {
        return getEMC(new StackReference(item));
    }

    public static double getEMC(ItemStack itemStack) {
        return getEMC(new StackReference(itemStack));
    }

    public static double getEMC(StackReference stackReference) {
        if (!blacklist.contains(stackReference)) {
            Double value = emcMapping.get(stackReference);
            return value == null ? 0 : value;
        } else {
            return 0;
        }
    }

    public static void initialize() {
        registerMiscHandler(new VanillaCraftingHandler(2));
        registerMiscHandler(new FurnaceHandler(2));
        registerMiscHandler(new AE2CraftingHandler(2));
        registerMiscHandler(new IC2CraftingHandler(2));
        registerMiscHandler(new AE2FacadeHandler());
        registerItemHandler(new FluidHandler());
        registerItemHandler(new DenseOreHandler());

        fireHandlers(EMCRegistryState.PRE);
        fireHandlers(EMCRegistryState.PRE);

        initializeLazyValues();
        initializeLazyFluidValues();

        fireHandlers(EMCRegistryState.POST_LAZY);
        fireHandlers(EMCRegistryState.POST_LAZY);
        fireHandlers(EMCRegistryState.RECIPE);
        fireHandlers(EMCRegistryState.RECIPE);
        fireHandlers(EMCRegistryState.MISC);
        fireHandlers(EMCRegistryState.MISC);
        fireHandlers(EMCRegistryState.POST);
        fireHandlers(EMCRegistryState.POST);

        addFinalValues();
    }

    private static void initializeLazyValues() {
        setEMC(Blocks.brown_mushroom, 32);
        setEMC(Blocks.cactus, 8);
        setEMC(Blocks.dragon_egg, 262144);
        setEMC(Blocks.end_stone, 4);
        setEMC(Blocks.grass, 1);
        setEMC(Blocks.gravel, 4);
        setEMC(Blocks.ice, 1);
        setEMC(Blocks.mossy_cobblestone, 145);
        setEMC(Blocks.mycelium, 1);
        setEMC(Blocks.netherrack, 1);
        setEMC(Blocks.obsidian, 64);
        setEMC(Blocks.packed_ice, 4);
        setEMC(Blocks.pumpkin, 144);
        setEMC(Blocks.red_mushroom, 32);
        setEMC(Blocks.snow, 1);
        setEMC(Blocks.soul_sand, 49);
        setEMC(Blocks.torch, 9);
        setEMC(Blocks.vine, 8);
        setEMC(Blocks.waterlily, 16);
        setEMC(Blocks.web, 12);
        setEMC(Items.apple, 128);
        setEMC(Items.beef, 64);
        setEMC(Items.blaze_powder, 768);
        setEMC(Items.blaze_rod, 1536);
        setEMC(Items.bone, 144);
        setEMC(Items.carrot, 64);
        setEMC(Items.chicken, 64);
        setEMC(Items.clay_ball, 16);
        setEMC(Items.diamond, 8192);
        setEMC(Items.diamond_horse_armor, 40960);
        setEMC(Items.egg, 32);
        setEMC(Items.emerald, 16384);
        setEMC(Items.enchanted_book, 2048);
        setEMC(Items.ender_pearl, 1024);
        setEMC(Items.feather, 48);
        setEMC(Items.filled_map, 1472);
        setEMC(Items.flint, 4);
        setEMC(Items.ghast_tear, 4096);
        setEMC(Items.glowstone_dust, 384);
        setEMC(Items.gold_ingot, 2048);
        setEMC(Items.golden_horse_armor, 1024);
        setEMC(Items.gunpowder, 192);
        setEMC(Items.iron_horse_armor, 1280);
        setEMC(Items.iron_ingot, 256);
        setEMC(Items.leather, 64);
        setEMC(Items.milk_bucket, 833);
        setEMC(Items.melon, 16);
        setEMC(Items.nether_star, 139264);
        setEMC(Items.nether_wart, 24);
        setEMC(Items.porkchop, 64);
        setEMC(Items.quartz, 256);
        setEMC(Items.record_11, 2048);
        setEMC(Items.record_13, 2048);
        setEMC(Items.record_blocks, 2048);
        setEMC(Items.record_cat, 2048);
        setEMC(Items.record_chirp, 2048);
        setEMC(Items.record_far, 2048);
        setEMC(Items.record_mall, 2048);
        setEMC(Items.record_mellohi, 2048);
        setEMC(Items.record_stal, 2048);
        setEMC(Items.record_strad, 2048);
        setEMC(Items.record_wait, 2048);
        setEMC(Items.record_ward, 2048);
        setEMC(Items.redstone, 64);
        setEMC(Items.reeds, 32);
        setEMC(Items.rotten_flesh, 32);
        setEMC(Items.saddle, 192);
        setEMC(Items.slime_ball, 32);
        setEMC(Items.snowball, 1);
        setEMC(Items.spider_eye, 128);
        setEMC(Items.stick, 4);
        setEMC(Items.string, 12);
        setEMC(Items.wheat, 24);
        setEMC(Items.wheat_seeds, 16);
        setEMC_wild(Blocks.cobblestone, 1);
        setEMC_wild(Blocks.deadbush, 1);
        setEMC_wild(Blocks.dirt, 1);
        setEMC_wild(Blocks.double_plant, 32);
        setEMC_wild(Blocks.leaves2, 1);
        setEMC_wild(Blocks.leaves, 1);
        setEMC_wild(Blocks.log2, 32);
        setEMC_wild(Blocks.log, 32);
        setEMC_wild(Blocks.red_flower, 16);
        setEMC_wild(Blocks.sand, 1);
        setEMC_wild(Blocks.sapling, 32);
        setEMC_wild(Blocks.tallgrass, 1);
        setEMC_wild(Blocks.yellow_flower, 16);
        setEMC_wild(Items.coal, 128);
        setEMC_wild(Items.fish, 64);
        setEMC_wild(Items.potato, 64);

        // Dye Handling
        for (int i = 0; i < 16; i++) {
            double emc = 16;

            if (i == 3) {
                emc = 128;
            } else if (i == 4) {
                emc = 864;
            } else if (i == 15) {
                emc = 48;
            }

            setEMC(new ItemStack(Items.dye, 1, i), emc);
        }

        // Also add ore dictionary tags
        ImmutableSet<StackReference> immutableSet = ImmutableSet.copyOf(emcMapping.keySet());
        for (StackReference stackReference : immutableSet) {
            double emc = getEMC(stackReference);
            ItemStack itemStack = stackReference.toItemStack();

            for (int id : OreDictionary.getOreIDs(itemStack)) {
                String name = OreDictionary.getOreName(id);
                for (ItemStack oreStack : OreDictionary.getOres(name)) {
                    setEMC(oreStack, emc);
                }
            }
        }
    }

    private static void initializeLazyFluidValues() {
        setEMC(FluidRegistry.WATER, 1);
        setEMC(FluidRegistry.LAVA, 64);
    }

    private static void fireHandlers(EMCRegistryState state) {
        fireItemHandlers(state);
        fireMiscHandlers(state);
    }

    private static void fireItemHandlers(EMCRegistryState state) {
        LinkedList<IEMCItemHandler> itemList = Lists.newLinkedList();
        for (IEMCItemHandler handler : itemHandlers) {
            if (handler.getInsertionState() == state) {
                itemList.add(handler);
            }
        }

        for (Item item : (Iterable<Item>) GameData.getItemRegistry()) {
            LinkedList<ItemStack> subItems = Lists.newLinkedList();

            try {
                item.getSubItems(item, item.getCreativeTab(), subItems);
            } catch (Exception ignore) {
            }

            for (ItemStack itemStack : subItems) {
                for (IEMCItemHandler handler : itemHandlers) {
                    handler.handleItem(emcDelegate, itemStack);
                }
            }
        }
    }

    private static void fireMiscHandlers(EMCRegistryState state) {
        for (IEMCMiscHandler handler : miscHandlers) {
            if (handler.getInsertionState() == state) {
                handler.handle(emcDelegate);
            }
        }
    }

    private static void addFinalValues() {
        // Stone brick handling
        double stoneBrickEmc = getEMC(Blocks.stonebrick);
        for (int i = 1; i < BlockStoneBrick.field_150141_b.length; i++) {
            setEMC(new ItemStack(Blocks.stonebrick, 1, i), stoneBrickEmc);
        }

        // Anvil handling
        double anvilEmc = getEMC(new ItemStack(Blocks.anvil, 1, 0));
        setEMC(new ItemStack(Blocks.anvil, 1, 1), anvilEmc * 0.66D);
        setEMC(new ItemStack(Blocks.anvil, 1, 2), anvilEmc * 0.33D);

        // Name tag
        double string = getEMC(Items.string);
        double paper = getEMC(Items.paper);
        setEMC(Items.name_tag, string + paper);
    }
}
