package com.bundles.tooltip;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bundle Tooltip UI
 */
public class BundleTooltip {

    /**
     * Bundle Tooltip Texture Location
     */
    private static final ResourceLocation BUNDLE_LOCATION = new ResourceLocation(BundleResources.MOD_ID, "textures/gui/container/bundle.png");

    /**
     * Draw the Bundle Tooltip UI
     *
     * @param event Render Tooltip Event
     */
    public static void draw(final RenderTooltipEvent.Pre event) {
        renderImage(event);
    }

    /**
     * Render the Tooltip Image
     *
     * @param event Render Tooltip Event
     */
    private static void renderImage(final RenderTooltipEvent.Pre event) {
        final FontRenderer font = event.getFontRenderer();
        final int mouseX = event.getX();
        final int mouseY = event.getY();
        final MatrixStack matrixStack = event.getMatrixStack();
        final ItemStack bundle = event.getStack();
        final List<ItemStack> contents = BundleItem.getContents(bundle).collect(Collectors.toList());
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final int weight = BundleItem.getWeight(bundle);
        int i = gridSizeX(contents);
        int j = gridSizeY(contents);
        boolean flag = weight >= BundleResources.MAX_BUNDLE_ITEMS;
        GuiUtils.preItemToolTip(bundle);

        drawHoveringText(bundle, matrixStack, getTooltipFromItem(bundle), event, i, j, flag, contents, itemRenderer);
        drawBorder(mouseX, mouseY, i, j, matrixStack);

        GuiUtils.postItemToolTip();
    }

    /**
     * Render a Tooltip Slot
     *
     * @param x Slot X Coordinate
     * @param y Slot Y Coordinate
     * @param index Slot Index
     * @param isBundleFull If the Bundle is Full
     * @param fontRenderer Font Renderer
     * @param matrixStack Matrix Stack
     * @param itemRenderer Item Renderer
     * @param contents Bundle Content
     */
    private static void renderSlot(int x, int y, int index, boolean isBundleFull, FontRenderer fontRenderer, MatrixStack matrixStack, ItemRenderer itemRenderer, List<ItemStack> contents) {
        if (index >= contents.size()) {
            blit(matrixStack, x, y, isBundleFull ? Texture.BLOCKED_SLOT : Texture.SLOT);
        } else {
            ItemStack itemstack = contents.get(index);
            blit(matrixStack, x, y, Texture.SLOT);
            itemRenderer.blitOffset = 500;
            itemRenderer.renderAndDecorateItem(itemstack, x + 1, y + 1);
            itemRenderer.renderGuiItemDecorations(fontRenderer, itemstack, x + 1, y + 1);
            itemRenderer.blitOffset = 400;
        }
    }

    /**
     * Render the Tooltip Lines
     *
     * @param stack Bundle Item Stack
     * @param matrixStack Matrix Stack
     * @param textLines Tooltip Text Lines
     * @param event Render Tooltip Event
     * @param gridSizeX Tooltip Grid Width
     * @param gridSizeY Tooltip Grid Height
     * @param flag If the Bundle is full
     * @param contents Bundle Content
     * @param itemRenderer Item Renderer
     */
    private static void drawHoveringText(@Nonnull final ItemStack stack, MatrixStack matrixStack, List<? extends ITextProperties> textLines, final RenderTooltipEvent.Pre event, int gridSizeX, int gridSizeY, boolean flag, List<ItemStack> contents, ItemRenderer itemRenderer) {
        int backgroundColor = GuiUtils.DEFAULT_BACKGROUND_COLOR;
        int borderColorStart = GuiUtils.DEFAULT_BORDER_COLOR_START;
        int borderColorEnd = GuiUtils.DEFAULT_BORDER_COLOR_END;

        if (!textLines.isEmpty()) {
            int mouseX = event.getX();
            int mouseY = event.getY();
            int screenWidth = event.getScreenWidth();
            int screenHeight = event.getScreenHeight();
            int maxTextWidth = event.getMaxWidth();
            FontRenderer font = event.getFontRenderer();
            int tooltipTextWidth = 0;
            boolean needsWrap = false;
            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            int tooltipHeight = 8;
            final int zLevel = 400;

            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();

            for (ITextProperties textLine : textLines) {
                int textLineWidth = font.width(textLine);
                if (textLineWidth > tooltipTextWidth)
                    tooltipTextWidth = textLineWidth;
            }

            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) {
                    tooltipTextWidth = (mouseX > screenWidth / 2) ? (mouseX - 12 - 8) : (screenWidth - 16 - mouseX);
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<ITextProperties> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    ITextProperties textLine = textLines.get(i);
                    List<ITextProperties> wrappedLine = font.getSplitter().splitLines(textLine, tooltipTextWidth, Style.EMPTY);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (ITextProperties line : wrappedLine) {
                        int lineWidth = font.width(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }

                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;
                tooltipX = mouseX > screenWidth / 2 ? (mouseX - 16 - tooltipTextWidth) : (mouseX + 12);
            }

            int tooltipY = mouseY - 12;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2;
                }
            }

            if (tooltipY < 4) {
                tooltipY = 4;
            }
            else if (tooltipY + tooltipHeight + 4 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 4;
            }

            RenderTooltipEvent.Color colorEvent = new RenderTooltipEvent.Color(stack, textLines, matrixStack, tooltipX, tooltipY, font, backgroundColor, borderColorStart, borderColorEnd);
            MinecraftForge.EVENT_BUS.post(colorEvent);
            backgroundColor = colorEvent.getBackground();
            borderColorStart = colorEvent.getBorderStart();
            borderColorEnd = colorEvent.getBorderEnd();

            matrixStack.pushPose();
            Matrix4f mat = matrixStack.last().pose();
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, textLines, matrixStack, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));

            IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            matrixStack.translate(0.0D, 0.0D, zLevel);

            int tooltipTop = tooltipY;

            for (int i = 0; i < titleLinesCount; ++i) {
                ITextProperties line = textLines.get(i);
                if (line != null) {
                    font.drawInBatch(LanguageMap.getInstance().getVisualOrder(line), (float)tooltipX, (float)tooltipY, -1, true, mat, renderType, false, 0, 15728880);
                }
                tooltipY += (i + 1 == titleLinesCount) ? 2 : 10;
            }

            tooltipY += 10;

            for(int i = 0, k = 0; i < gridSizeY; ++i) {
                for(int j = 0; j < gridSizeX; ++j) {
                    int gridTooltipX = tooltipX + j * 18 + 1;
                    int gridTooltipY = tooltipY + i * 20 + 1;
                    renderSlot(gridTooltipX, gridTooltipY, k++, flag, font, matrixStack, itemRenderer, contents);
                    tooltipY += gridTooltipY + 2;
                }
            }

            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();

            if(!bufferBuilder.building()) {
                bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            }

            tooltipY += 10;

            for (int i = titleLinesCount; i < textLines.size(); ++i) {
                ITextProperties line = textLines.get(i);
                if (line != null) {
                    font.drawInBatch(LanguageMap.getInstance().getVisualOrder(line), (float)tooltipX, (float)tooltipY, -1, true, mat, renderType, false, 0, 15728880);
                }
                tooltipY += 10;
            }

            renderType.endBatch();
            matrixStack.popPose();

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, textLines, matrixStack, tooltipX, tooltipTop, font, tooltipTextWidth, tooltipHeight));

            RenderSystem.enableDepthTest();
            RenderSystem.enableRescaleNormal();
        }
    }

    /**
     * Render a Slot inside the Tooltip
     *
     * @param matrixStack Matrix Stack
     * @param x Slot X Coordinate
     * @param y Slot Y Coordinate
     */
    public static void renderSlotHighlight(MatrixStack matrixStack, int x, int y) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        fillGradient(matrixStack.last().pose(), bufferbuilder, x, y, x + 16, y + 16);
        tessellator.end();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
        tessellator.end();
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    /**
     * Get the Item Tooltip Lines
     *
     * @param itemStack Bundle Item Stack
     * @return Item Tooltip Lines
     */
    private static List<ITextComponent> getTooltipFromItem(ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        return itemStack.getTooltipLines(minecraft.player, minecraft.options.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
    }

    /**
     * Draw a gradient
     *
     * @param matrix Matrix
     * @param bufferBuilder Buffer Builder
     * @param x1 Start X Coordinate
     * @param y1 Start Y Coordinate
     * @param x2 End X Coordinate
     * @param y2 End Y Coordinate
     */
    protected static void fillGradient(Matrix4f matrix, BufferBuilder bufferBuilder, int x1, int y1, int x2, int y2) {
        float blitOffset = 400;
        int color = -2130706433;
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        float f4 = (float)(color >> 24 & 255) / 255.0F;
        float f5 = (float)(color >> 16 & 255) / 255.0F;
        float f6 = (float)(color >> 8 & 255) / 255.0F;
        float f7 = (float)(color & 255) / 255.0F;
        bufferBuilder.vertex(matrix, (float)x2, (float)y1, blitOffset).color(f1, f2, f3, f).endVertex();
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, blitOffset).color(f1, f2, f3, f).endVertex();
        bufferBuilder.vertex(matrix, (float)x1, (float)y2, blitOffset).color(f5, f6, f7, f4).endVertex();
        bufferBuilder.vertex(matrix, (float)x2, (float)y2, blitOffset).color(f5, f6, f7, f4).endVertex();
    }

    /**
     * Draw the Tooltip Slot Border
     *
     * @param x Tooltip X Coordinate
     * @param y Tooltip Y Coordinate
     * @param slotWidth Tooltip Slot Width
     * @param slotHeight Tooltip Slot Height
     * @param matrixStack Matrix Stack
     */
    private static void drawBorder(int x, int y, int slotWidth, int slotHeight, MatrixStack matrixStack) {
        blit(matrixStack, x, y, Texture.BORDER_CORNER_TOP);
        blit(matrixStack, x + slotWidth * 18 + 1, y, Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < slotWidth; ++i) {
            blit(matrixStack, x + 1 + i * 18, y, Texture.BORDER_HORIZONTAL_TOP);
            blit(matrixStack, x + 1 + i * 18, y + slotHeight * 20, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < slotHeight; ++j) {
            blit(matrixStack, x, y + j * 20 + 1, Texture.BORDER_VERTICAL);
            blit(matrixStack, x + slotWidth * 18 + 1, y + j * 20 + 1, Texture.BORDER_VERTICAL);
        }

        blit(matrixStack, x, y + slotHeight * 20, Texture.BORDER_CORNER_BOTTOM);
        blit(matrixStack, x + slotWidth * 18 + 1, y + slotHeight * 20, Texture.BORDER_CORNER_BOTTOM);
    }

    /**
     * Blit a Texture on the Tooltip
     *
     * @param matrixStack Matrix Stack
     * @param x Texture X Coordinate
     * @param y Texture Y Coordinate
     * @param texture Texture to draw
     */
    private static void blit(MatrixStack matrixStack, int x, int y, Texture texture) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(BUNDLE_LOCATION);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();
        if(bufferBuilder.building()) {
            bufferBuilder.end();
        }
        AbstractGui.blit(matrixStack, x, y, 400, (float)texture.x, (float)texture.y, texture.w, texture.h, 128, 128);
    }

    /**
     * Get the Tooltip Height
     *
     * @param contents Bundle Contents
     * @return Tooltip Height
     */
    private static int getHeight(List<ItemStack> contents) {
        return gridSizeY(contents) * 20 + 2 + 4;
    }

    /**
     * Get the Tooltip Width
     *
     * @param contents Bundle Contents
     * @return Tooltip Width
     */
    private static int getWidth(List<ItemStack> contents) {
        return gridSizeX(contents) * 18 + 2;
    }

    /**
     * Get the Tooltip Slots on X Axis
     *
     * @param contents Bundle Contents
     * @return Tooltip Slots on X Axis
     */
    private static int gridSizeX(List<ItemStack> contents) {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)contents.size() + 1.0D)));
    }

    /**
     * Get the Tooltip Slots on Y Axis
     *
     * @param contents Bundle Contents
     * @return Tooltip Slots on Y Axis
     */
    private static int gridSizeY(List<ItemStack> contents) {
        return (int)Math.ceil(((double)contents.size() + 1.0D) / (double)gridSizeX(contents));
    }

    /**
     * Tooltip Slots
     */
    @OnlyIn(Dist.CLIENT)
    enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        /**
         * Tooltip Slot X Coordinate
         */
        public final int x;
        /**
         * Tooltip Slot Y Coordinate
         */
        public final int y;
        /**
         * Tooltip Slot Width
         */
        public final int w;
        /**
         * Tooltip Slot Height
         */
        public final int h;

        /**
         * Constructor. Set up Tooltip Slot properties
         *
         * @param x Tooltip Slot X Coordinate
         * @param y Tooltip Slot Y Coordinate
         * @param width Tooltip Slot Width
         * @param height Tooltip Slot Height
         */
        Texture(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
        }
    }
}