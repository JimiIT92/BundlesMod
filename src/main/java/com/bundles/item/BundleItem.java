package com.bundles.item;

import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import com.bundles.init.BundleSounds;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Bundle Item
 *
 * @author JimiIT92
 */
public class BundleItem extends Item {

    /**
     * Constructor. Set the Bundle Item properties
     */
    public BundleItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1));
    }

    /**
     * Check if the Bundle should show the durability bar
     *
     * @param stack Bundle Item Stack
     * @return True if the Bundle should show the durability bar
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getContentWeight(stack) > 0;
    }

    /**
     * Get the durability bar width
     *
     * @param stack Bundle Item Stack
     * @return Durability Bar Width
     */
    @Override
    public int getDamage(ItemStack stack) {
        return 64 - getContentWeight(stack);
    }

    /**
     * Get the max durability bar width
     *
     * @param stack Bundle Item Stack
     * @return Max Durability Bar Width
     */
    @Override
    public int getMaxDamage(ItemStack stack) {
        return BundleResources.MAX_BUNDLE_ITEMS;
    }

    /**
     * Get the Durability Bar Color
     *
     * @param stack Item Stack
     * @return Durability Bar Color
     */
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return color(floor(0.4F * 255.0F), floor(0.4F * 255.0F), floor(255.0F));
    }

    /**
     * Get the Item weight inside the Bundle
     *
     * @param stack Item Stack
     * @return Item Weight
     */
    public static int getWeight(ItemStack stack) {
        Item item = stack.getItem();
        if(item.equals(BundleItems.BUNDLE.get())) {
            return 4 + getContentWeight(stack);
        }
        if((item.equals(Items.BEEHIVE) || item.equals(Items.BEE_NEST)) && stack.hasTag()) {
            CompoundNBT nbt = stack.getTagElement("BlockEntityTag");
            if(nbt != null && !nbt.getList("Bees", 10).isEmpty()) {
                return BundleResources.MAX_BUNDLE_ITEMS;
            }
        }
        return BundleResources.MAX_BUNDLE_ITEMS / stack.getMaxStackSize();
    }

    /**
     * Get the Bundle fullness
     *
     * @param stack Bundle Item Stack
     * @return Bundle Fullness
     */
    private static int getContentWeight(ItemStack stack) {
        return getContents(stack).mapToInt(contentStack -> getWeight(contentStack) * contentStack.getCount()).sum();
    }

    /**
     * Check if the Bundle has some content
     *
     * @param stack Bundle Item Stack
     * @return True if the Bundle has some content
     */
    public boolean hasContent(ItemStack stack) {
        return getContents(stack).findAny().isPresent();
    }

    /**
     * Get the Bundle Content
     *
     * @param stack Bundle Item Stack
     * @return Bundle Content
     */
    public static Stream<ItemStack> getContents(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt == null) {
            return Stream.empty();
        }
        return getBundleItems(nbt).stream().map(CompoundNBT.class::cast).map(ItemStack::of);
    }

    /**
     * Get the Bundle Items
     *
     * @param nbt Bundle Item Stack NBT Tag
     * @return Bundle Items
     */
    private static ListNBT getBundleItems(CompoundNBT nbt) {
        return nbt.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, 10);
    }

    /**
     * Display the fullness tooltip
     *
     * @param stack Item Stack
     * @param world World
     * @param tooltips Item Stack Tooltips
     * @param tooltipFlag Tooltip Flag
     */
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, @Nonnull ITooltipFlag tooltipFlag) {
        tooltips.add((new TranslationTextComponent("item.bundles.bundle.fullness", getContentWeight(stack), BundleResources.MAX_BUNDLE_ITEMS)).withStyle(TextFormatting.GRAY));
    }

    /**
     * Floors a number
     *
     * @param value Value to floor
     * @return Floored value
     */
    private int floor(float value) {
        int intValue = (int)value;
        return value < (float)intValue ? intValue - 1 : intValue;
    }

    /**
     * Get an RGB color
     *
     * @param red Red value
     * @param green Green value
     * @param blue Blue value
     * @return RGB Color
     */
    private int color(int red, int green, int blue) {
        int redAndGreen = (red << 8) + green;
        return (redAndGreen << 8) + blue;
    }

    /**
     * Play a sound when one item is removed from the Bundle
     *
     * @param entity Entity removing the item
     */
    private void playRemoveOneSound(Entity entity) {
        entity.playSound(BundleSounds.BUNDLE_REMOVE_ONE.get(), 0.8F, 0.8F + entity.level.getRandom().nextFloat() * 0.4F);
    }

    /**
     * Play a sound when one item is added to the Bundle
     *
     * @param entity Entity adding the item
     */
    private void playInsertSound(Entity entity) {
        entity.playSound(BundleSounds.BUNDLE_INSERT.get(), 0.8F, 0.8F + entity.level.getRandom().nextFloat() * 0.4F);
    }

    /**
     * Play a sound when all items are removed from the Bundle
     *
     * @param entity Entity removing the items
     */
    private void playDropContentsSound(Entity entity) {
        entity.playSound(BundleSounds.BUNDLE_DROP_CONTENTS.get(), 0.8F, 0.8F + entity.level.getRandom().nextFloat() * 0.4F);
    }

    /**
     * Drop Bundle Contents on right click
     *
     * @param world World reference
     * @param player Player dropping contents
     * @param hand Interaction Hand
     * @return Action Result
     */
    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(dropContents(stack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return ActionResult.sidedSuccess(stack, world.isClientSide());
        }
        return ActionResult.fail(stack);
    }

    /**
     * Drop Bundle contents
     *
     * @param stack Bundle Item Stack
     * @param player Player dropping contents
     * @return True if all the content has been dropped
     */
    private static boolean dropContents(ItemStack stack, PlayerEntity player) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(!nbt.contains(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION)) {
            return false;
        }
        if(player instanceof ServerPlayerEntity) {
            ListNBT listNbt = nbt.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, 10);

            for (int i = 0; i < listNbt.size(); i++) {
                CompoundNBT itemNbt = listNbt.getCompound(i);
                ItemStack itemStack = ItemStack.of(itemNbt);
                player.drop(itemStack, true);
            }
        }

        stack.removeTagKey(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION);
        return true;
    }

    /**
     * Check if an Item can be put inside a Bundle
     *
     * @param stack Item Stack
     * @return True if the Item can be put inside a Bundle
     */
    private static boolean canFit(ItemStack stack) {
        if(stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        if(item instanceof BlockItem) {
            ITag<Block> blockTag = BlockTags.getAllTags().getTag(BundleResources.BUNDLE_IGNORED_BLOCKS_TAG);
            return blockTag == null || !blockTag.contains(((BlockItem)item).getBlock());
        }
        ITag<Item> itemTag = ItemTags.getAllTags().getTag(BundleResources.BUNDLE_IGNORED_ITEMS_TAG);
        return itemTag == null || !itemTag.contains(item);
    }

    /**
     * Remove the first Item from the Bundle
     *
     * @param stack Bundle Item Stack
     * @return Bundle Item Stack without the first Item
     */
    private static Optional<ItemStack> removeOne(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if(!nbt.contains(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION)) {
            return Optional.empty();
        }
        ListNBT listNbt = nbt.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, 10);
        if(listNbt.isEmpty()) {
            return Optional.empty();
        }
        CompoundNBT listItemNbt = listNbt.getCompound(0);
        ItemStack itemStackNbt = ItemStack.of(listItemNbt);
        listNbt.remove(0);
        if(listNbt.isEmpty()) {
            stack.removeTagKey(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION);
        }
        return Optional.of(itemStackNbt);
    }

    /**
     * Add an Item to the Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @return Quantity added
     */
    private static int add(ItemStack bundle, ItemStack stack) {
        if(canFit(stack)) {
            CompoundNBT bundleNbt = bundle.getOrCreateTag();
            if(!bundleNbt.contains(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION)) {
                bundleNbt.put(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, new ListNBT());
            }

            int contentWeight = getContentWeight(bundle);
            int weight = getWeight(stack);
            int stackSize = Math.min(stack.getCount(), (BundleResources.MAX_BUNDLE_ITEMS - contentWeight) / weight);
            if(stackSize == 0) {
                return 0;
            }
            ListNBT listNbt = bundleNbt.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, 10);
            Optional<CompoundNBT> optionalNbt = getMatchingItem(stack, listNbt);
            if(optionalNbt.isPresent()) {
                CompoundNBT itemStackNbt = optionalNbt.get();
                ItemStack itemStack = ItemStack.of(itemStackNbt);
                itemStack.grow(stackSize);
                itemStack.save(itemStackNbt);
                listNbt.remove(itemStackNbt);
                listNbt.add(0, itemStackNbt);
            } else {
                ItemStack itemStack = stack.copy();
                itemStack.setCount(stackSize);
                CompoundNBT stackNbt = new CompoundNBT();
                itemStack.save(stackNbt);
                listNbt.add(0, stackNbt);
            }

            bundle.save(bundleNbt);

            return stackSize;
        }
        return 0;
    }

    /**
     * Check if there is a matching Item Stack
     * inside the Bundle
     *
     * @param stack Item Stack
     * @param listNbt Bundle Items
     * @return Optional NBT Tag for Item Stack
     */
    private static Optional<CompoundNBT> getMatchingItem(ItemStack stack, ListNBT listNbt) {
        return stack.getItem() instanceof BundleItem ? Optional.empty() : listNbt.stream().filter(CompoundNBT.class::isInstance).map(CompoundNBT.class::cast).filter(nbt ->
                ItemStack.isSame(ItemStack.of(nbt), stack)).findFirst();
    }

    /**
     * Add an Item to the Bundle when the player right-clicks the Bundle on it,
     * or remove on Item if the Player right-clicks on an empty slot
     *
     * @param bundle Bundle Item Stack
     * @param slot Clicked Slot
     * @param player Player adding or removing the Item
     */
    public Map.Entry<ItemStack, Integer> overrideStackedOnOther(ItemStack bundle, Slot slot, PlayerEntity player) {
        ItemStack slotStack = slot.getItem();
        if(slotStack.isEmpty()) {
            this.playRemoveOneSound(player);
            removeOne(bundle).ifPresent(s -> add(bundle, safeInsert(slot, s)));
        } else if(canFit(slotStack)) {
            int weight = (BundleResources.MAX_BUNDLE_ITEMS - getContentWeight(bundle)) / getWeight(slotStack);
            int quantity = add(bundle, safeTake(slot, slotStack.getCount(), weight, player));
            if(quantity > 0) {
                this.playInsertSound(player);
            }
        }
        return new AbstractMap.SimpleEntry<>(bundle, slotStack.getCount());
    }

    /**
     * Add an Item to the Bundle if the Player right-clicks on it
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @param slot Clicked Slot
     * @param player Player adding the Item
     */
    public Map.Entry<ItemStack, Integer> overrideOtherStackedOnMe(ItemStack bundle, ItemStack stack, Slot slot, PlayerEntity player) {
        if(allowModification(slot, player)) {
            if(stack.isEmpty()) {
                removeOne(bundle).ifPresent(s -> {
                    this.playRemoveOneSound(player);
                    player.addItem(s);
                });
            } else {
                int quantity = add(bundle, stack);
                if(quantity > 0) {
                    this.playInsertSound(player);
                    stack.shrink(quantity);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(bundle, stack.getCount());
    }

    /**
     * Check if the Player can interact with the Slot
     *
     * @param slot Slot
     * @param player Player
     * @return True if the Player can interact with the Slot
     */
    private boolean allowModification(Slot slot, PlayerEntity player) {
        return slot.mayPickup(player) && slot.mayPlace(slot.getItem());
    }

    /**
     * Try to remove an Item from a Slot
     *
     * @param slot Slot
     * @param count Quantity to remove
     * @param weight Desired quantity
     * @param player Player removing the Item
     * @return Modified Item Stack (or empty if all items has been removed)
     */
    private Optional<ItemStack> tryRemove(Slot slot, int count, int weight, PlayerEntity player) {
        if(!slot.mayPickup(player) || (!allowModification(slot, player) && count < slot.getItem().getCount())) {
            return Optional.empty();
        }
        count = Math.min(count, weight);
        ItemStack itemStack = slot.remove(count);
        if(itemStack.isEmpty()) {
            return Optional.empty();
        }
        if(slot.getItem().isEmpty()) {
            slot.set(ItemStack.EMPTY);
        }
        return Optional.of(itemStack);
    }

    /**
     * Remove an Item from a Slot
     *
     * @param slot Slot
     * @param count Quantity to remove
     * @param weight Desired quantity
     * @param player Player removing the Item
     * @return Modified Item Stack (or empty if all items has been removed)
     */
    private ItemStack safeTake(Slot slot, int count, int weight, PlayerEntity player) {
        Optional<ItemStack> optionalItemStack = tryRemove(slot, count, weight, player);
        optionalItemStack.ifPresent(stack -> slot.onTake(player, stack));
        return optionalItemStack.orElse(ItemStack.EMPTY);
    }

    /**
     * Add an Item to a Slot
     *
     * @param slot Slot
     * @param stack Item Stack to add
     * @return Modified Item Stack
     */
    private ItemStack safeInsert(Slot slot, ItemStack stack) {
        if (!stack.isEmpty() && slot.mayPlace(stack)) {
            ItemStack itemstack = slot.getItem();
            int i = Math.min(stack.getCount(), slot.getMaxStackSize(stack) - itemstack.getCount());
            if (itemstack.isEmpty()) {
                slot.set(stack.split(i));
            } else if (ItemStack.isSame(itemstack, stack)) {
                stack.shrink(i);
                itemstack.grow(i);
                slot.set(itemstack);
            }
        }
        return stack;
    }
}