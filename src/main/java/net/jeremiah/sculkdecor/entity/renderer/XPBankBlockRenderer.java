package net.jeremiah.sculkdecor.entity.renderer;

import net.jeremiah.sculkdecor.SculkmansDecor;
import net.jeremiah.sculkdecor.entity.XPBankBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@SuppressWarnings("unused")
public final class XPBankBlockRenderer implements BlockEntityRenderer<XPBankBlockEntity> {
    public XPBankBlockRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(XPBankBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f/16f, 0.5f/16f, 0.5f/16f);
        matrices.scale(15/16f, 15/16f, 15/16f);
        var fullPercent = (float)entity.getStoredXP() / (float)XPBankBlockEntity.MAX_STORED_XP;
        entity.renderer_animation = entity.renderer_animation + (fullPercent - entity.renderer_animation) * (tickDelta * 0.1f);
        var progress = entity.renderer_animation;
        var vc = vertexConsumers.getBuffer(RenderLayer.getText(SculkmansDecor.id("textures/fluid/liquid_experience.png")));
        var mat = matrices.peek().getPositionMatrix();

        drawQuad(vc, mat,
                0, 0, 0,
                1, progress, 0,
                1, 1, 1,
                0, 1 - progress,
                1, 1,
                light
        );

        drawQuad(vc, mat,
                0, 0, 1,
                0, progress, 0,
                1, 1, 1,
                0, 1 - progress,
                1, 1,
                light
        );

        drawQuad(vc, mat,
                1, 0, 1,
                0, progress, 1,
                1, 1, 1,
                0, 1 - progress,
                1, 1,
                light
        );

        drawQuad(vc, mat,
                1, 0, 0,
                1, progress, 1,
                1, 1, 1,
                0, 1 - progress,
                1, 1,
                light
        );


        if (progress >= 0.001f) {
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            matrices.translate(0, -1, 0);
            drawQuad(vc, mat,
                    0, 1, progress,
                    1, 0, progress,
                    1, 1, 1,
                    0, 0,
                    1, 1,
                    light
            );

            drawQuad(vc, mat,
                    0, 0, 0,
                    1, 1, 0,
                    1, 1, 1,
                    0, 0,
                    1, 1,
                    light
            );
        }

        matrices.pop();
    }

    private static void drawQuad(VertexConsumer vertexConsumer, Matrix4f pos,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float r, float g, float b,
                                 float minU, float minV,
                                 float maxU, float maxV,
                                 int light) {
        vertexConsumer.vertex(pos, x1, y1, z1)
                .color(r, g, b, 1)
                .texture(minU, minV)
                .light(light)
                .next();

        vertexConsumer.vertex(pos, x1, y2, z1)
                .color(r, g, b, 1)
                .texture(minU, maxV)
                .light(light)
                .next();

        vertexConsumer.vertex(pos, x2, y2, z2)
                .color(r, g, b, 1)
                .texture(maxU, maxV)
                .light(light)
                .next();

        vertexConsumer.vertex(pos, x2, y1, z2)
                .color(r, g, b, 1)
                .texture(maxU, minV)
                .light(light)
                .next();
    }
}
