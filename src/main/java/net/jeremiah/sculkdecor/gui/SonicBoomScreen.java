package net.jeremiah.sculkdecor.gui;

import com.mojang.authlib.GameProfile;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class SonicBoomScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.sculkdecor.sonic_boom_generator");
    private static final Identifier BACKGROUND = SculkmansDecor.id("textures/gui/sonic_boom_gen.png");

    private static final int GUI_WIDTH = 236;
    private static final int HEADER_SIZE = 16;
    private static final int FOOTER_SIZE = 32;
    private static final int UNIT_SIZE = 18;
    private static final int CELL_HEIGHT = 36;
    private static final int SCROLL_MULTIPLER = UNIT_SIZE / 3;

    private final List<GameProfile> players = new ArrayList<>();
    private final BlockPos pos;

    private SonicBoomGeneratorBlockEntity entity;
    private int guiLeft = 0;
    private int guiTop = 0;
    private int ySize = 0;
    private int units = 0;
    private int scroll = 0;
    private int maxScroll = 0;
    private PlayerSkinProvider skinProvider;

    public SonicBoomScreen(BlockPos pos) {
        super(TITLE);
        this.pos = pos;
        this.client = MinecraftClient.getInstance();
    }

    @Override
    protected void init() {
        assert client != null && client.world != null;
        this.entity = (SonicBoomGeneratorBlockEntity) client.world.getBlockEntity(pos);

        int minUnits = MathHelper.ceil((float) (CELL_HEIGHT + 4) / (float) UNIT_SIZE);
        guiLeft = (width - GUI_WIDTH) / 2 + 2;
        guiTop = 32;
        units = Math.max(minUnits, (height - HEADER_SIZE - FOOTER_SIZE - 32 * 2) / UNIT_SIZE);
        ySize = HEADER_SIZE + units * UNIT_SIZE + FOOTER_SIZE;

        var buttonY = guiTop + ySize - 20 - 7;
        var buttonSize = 20;

        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("gui.sculkdecor.sonic_boom_generator.add_button"), this::addPlayer)
                .tooltip(Tooltip.of(Text.translatable("gui.sculkdecor.sonic_boom_generator.add_button.tooltip")))
                .position(guiLeft + GUI_WIDTH - 107, buttonY)
                .size(100, buttonSize)
                .build());

        players.clear();
        players.addAll(entity.getIgnoredPlayers());
        skinProvider = client.getSkinProvider();

        maxScroll = Math.max(0, players.size() - units) * (UNIT_SIZE / SCROLL_MULTIPLER);
    }

    private void addPlayer(ButtonWidget btn) {
        assert client != null;
        client.setScreen(new SonicBoomAddPlayerScreen(this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        players.clear();
        players.addAll(entity.getIgnoredPlayers());

        final var scrollOffset = scroll * SCROLL_MULTIPLER;
        final var nbPlayer = players.size();
        renderBackground(context);
        for (int i = 0; i < units; i++) {
            var y = guiTop + HEADER_SIZE + UNIT_SIZE * i;
            context.drawTexture(BACKGROUND, guiLeft, y, 0, HEADER_SIZE, GUI_WIDTH, UNIT_SIZE);
        }
        for (int i = 0; i < nbPlayer; i++) {
            final var plr = players.get(i);
            final var x = guiLeft + 9;
            final var y = guiTop + HEADER_SIZE + UNIT_SIZE * i + 1 - scrollOffset;
            if (y + UNIT_SIZE <= guiTop + HEADER_SIZE + 1 || y > guiTop + ySize - FOOTER_SIZE) continue;
            if (mouseX > guiLeft + 7 && mouseX < guiLeft + GUI_WIDTH - 7
                    && mouseY > y && mouseY < y + UNIT_SIZE) {
                context.fill(guiLeft + 7, y, guiLeft + GUI_WIDTH - 8, y + UNIT_SIZE, 0x88FFFFFF);
            }
            PlayerSkinDrawer.draw(context, skinProvider.loadSkin(plr),
                    x, y, 16);
            context.drawText(textRenderer, plr.getName(), x + 18, y + textRenderer.fontHeight / 2,
                    0xFFCCCCCC, false);
        }

        // draw header and footer after so the list isn't overlapping with them
        context.drawTexture(BACKGROUND, guiLeft, guiTop, 0, 0, GUI_WIDTH, HEADER_SIZE);
        var titleWidth = textRenderer.getWidth(TITLE);
        context.drawText(textRenderer, TITLE, (width - titleWidth) / 2, guiTop + textRenderer.fontHeight / 2,
                0xFF444444, false);

        context.drawTexture(BACKGROUND, guiLeft, guiTop + HEADER_SIZE + UNIT_SIZE * units, 0, HEADER_SIZE + UNIT_SIZE, GUI_WIDTH, FOOTER_SIZE);
        super.render(context, mouseX, mouseY, delta);
    }

    private GameProfile getHoveredGameProfile(int mouseX, int mouseY) {
        if (mouseX < guiLeft + 7 || mouseX > guiLeft + GUI_WIDTH - 7
                || mouseY < guiTop + HEADER_SIZE || mouseY > guiTop + ySize - FOOTER_SIZE) return null;

        mouseY += scroll * SCROLL_MULTIPLER - guiTop - HEADER_SIZE;
        var index = MathHelper.floor((float) mouseY / UNIT_SIZE);
        if (index < players.size()) {
            return players.remove(index);
        }
        return null;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) return true;

        scroll -= (int) amount;
        if (scroll >= 0 && scroll <= maxScroll) return false;
        scroll = MathHelper.clamp(scroll, 0, maxScroll);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            var gp = getHoveredGameProfile((int) mouseX, (int) mouseY);
            if (gp != null) {
                entity.removeIgnoredPlayerClient(gp);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void addIgnoredPlayer(GameProfile gp) {
        entity.addIgnoredPlayerClient(gp);
        players.add(gp);
    }
}
