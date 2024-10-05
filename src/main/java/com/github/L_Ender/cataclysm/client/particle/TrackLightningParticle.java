package com.github.L_Ender.cataclysm.client.particle;

import java.util.Locale;

import com.github.L_Ender.cataclysm.client.render.etc.LightningBoltData;
import com.github.L_Ender.cataclysm.client.render.etc.LightningRender;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Vector4f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class TrackLightningParticle extends Particle {
    private int r ;
    private int g ;
    private int b ;
    private LightningRender lightningRender = new LightningRender();

    public TrackLightningParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, int r, int g, int b) {
        super(world, x, y, z);
        this.setSize(6.0F, 6.0F);
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        Vec3 lightningTo = new Vec3(xd - x, yd - y, zd - z);
        this.lifetime = 5;
        int sections = 1 + (int) (9 * lightningTo.length());
        LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(0.5F, 0.1F, 0.5F, 0.85F, new Vector4f((float) r /255, (float) g /255, (float) b /255, 0.8F), 0.1F);
        LightningBoltData bolt = new LightningBoltData(boltData, Vec3.ZERO, lightningTo, sections)
                .size(0.1F + random.nextFloat() * 0.1F)
                .lifespan(this.lifetime)
                .spawn(LightningBoltData.SpawnFunction.CONSECUTIVE);
        lightningRender.update(this, bolt, 1.0F);
    }

    public boolean shouldCull() {
        return false;
    }


    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd -= (double) this.gravity;
        }
    }


    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp((double) partialTick, this.xo, this.x));
        float y = (float) (Mth.lerp((double) partialTick, this.yo, this.y));
        float z = (float) (Mth.lerp((double) partialTick, this.zo, this.z));
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        posestack.translate(x, y, z);
        lightningRender.render(partialTick, posestack, multibuffersource$buffersource);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OrbFactory implements ParticleProvider<TrackLightningParticle.OrbData> {

        @Override
        public Particle createParticle(TrackLightningParticle.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TrackLightningParticle particle;
            particle = new TrackLightningParticle(worldIn, x, y, z, (float)xSpeed, (float)ySpeed, (float)zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB());

            return particle;
        }
    }

    public static class OrbData implements ParticleOptions {
        public static final Deserializer<TrackLightningParticle.OrbData> DESERIALIZER = new Deserializer<TrackLightningParticle.OrbData>() {
            public TrackLightningParticle.OrbData fromCommand(ParticleType<TrackLightningParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int r = reader.readInt();
                reader.expect(' ');
                int g = reader.readInt();
                reader.expect(' ');
                int b = reader.readInt();
                return new TrackLightningParticle.OrbData(r, g, b);
            }

            public TrackLightningParticle.OrbData fromNetwork(ParticleType<TrackLightningParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new TrackLightningParticle.OrbData(buffer.readInt(), buffer.readInt(), buffer.readInt());
            }
        };

        private final int r;
        private final int g;
        private final int b;



        public OrbData(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;


        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(this.r);
            buffer.writeInt(this.g);
            buffer.writeInt(this.b);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                    this.r, this.g, this.b);
        }

        @Override
        public ParticleType<TrackLightningParticle.OrbData> getType() {
            return ModParticle.TRACK_LIGHTNING.get();
        }

        @OnlyIn(Dist.CLIENT)
        public int getR() {
            return this.r;
        }

        @OnlyIn(Dist.CLIENT)
        public int getG() {
            return this.g;
        }

        @OnlyIn(Dist.CLIENT)
        public int getB() {
            return this.b;
        }


        public static Codec<TrackLightningParticle.OrbData> CODEC(ParticleType<TrackLightningParticle.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.INT.fieldOf("r").forGetter(TrackLightningParticle.OrbData::getR),
                            Codec.INT.fieldOf("g").forGetter(TrackLightningParticle.OrbData::getG),
                            Codec.INT.fieldOf("b").forGetter(TrackLightningParticle.OrbData::getB)
                    ).apply(codecBuilder, TrackLightningParticle.OrbData::new)
            );
        }
    }
}
