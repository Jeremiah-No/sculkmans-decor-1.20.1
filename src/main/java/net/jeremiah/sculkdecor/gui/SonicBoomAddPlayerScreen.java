package net.jeremiah.sculkdecor.gui;

import com.mojang.authlib.GameProfile;
import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.SonicBoomGeneratorBlockEntity;
import net.jeremiah.sculkdecor.utils.PlayerUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public final class SonicBoomAddPlayerScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.sculkdecor.sonic_boom_generator");
    private static final Identifier BACKGROUND = SculkmansDecor.id("textures/gui/sonic_boom_gen.png");

    private static final int GUI_WIDTH = 236;
    private static final int HEADER_SIZE = 16;
    private static final int FOOTER_SIZE = 32;
    private static final int UNIT_SIZE = 18;
    private static final int GUI_HEIGHT = HEADER_SIZE + UNIT_SIZE + FOOTER_SIZE;

    private final SonicBoomScreen scr;
    private int guiLeft = 0;
    private int guiTop = 0;

    private TextFieldWidget textInput;

    public SonicBoomAddPlayerScreen(SonicBoomScreen sonicBoomScreen) {
        super(TITLE);
        this.scr = sonicBoomScreen;
    }

    @Override
    protected void init() {
        guiLeft = (width - GUI_WIDTH) / 2;
        guiTop = (height - GUI_HEIGHT) / 2;

        textInput = new TextFieldWidget(
                textRenderer,
                guiLeft + 7, guiTop + HEADER_SIZE,
                GUI_WIDTH - 14, 18,
                Text.translatable("gui.sculkdecor.sonic_boom_generator.add_button")
        );
        textInput.setMaxLength(40);
        this.addDrawableChild(textInput);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.sculkdecor.sonic_boom_generator.add_button"), this::addPlayer)
                .position(guiLeft + GUI_WIDTH - 107, guiTop + GUI_HEIGHT - 20 - 7)
                .size(100, 20)
                .build());
    }

    private void addPlayer(ButtonWidget btn) {
        final var str = textInput.getText();
        assert client != null;
        if (str.isEmpty()) {
            client.setScreen(scr);
            return;
        }
        GameProfile gp;
        try {
            gp = new GameProfile(UUID.fromString(str), null);
        } catch (Exception _e) {
            gp = new GameProfile(null, str);
        }

        gp = PlayerUtils.fillGameProfile(gp);
        if (gp == null || !gp.isComplete()) {
            assert client.player != null;
            client.player.sendMessage(Text.translatable("gui.sculkdecor.sonic_boom_generator.add_button.failed_add", str));
            return;
        }

        scr.addIgnoredPlayer(gp);
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(BACKGROUND, guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        assert client != null && client.world != null;
        client.setScreen(scr);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
