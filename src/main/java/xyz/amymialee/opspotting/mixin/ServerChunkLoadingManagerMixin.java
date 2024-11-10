package xyz.amymialee.opspotting.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ServerChunkLoadingManagerMixin {
    @WrapOperation(method = "method_17219", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;isWithinDistance(IIIII)Z", ordinal = 1))
    private boolean opspotting$maxed(int x1, int z1, int x2, int z2, int distance, @NotNull Operation<Boolean> original, ChunkPos  chunkPos, int i, MutableObject<ChunkDataS2CPacket> mutableObject, @NotNull ServerPlayerEntity player) {
        return original.call(x1, z1, x2, z2, player.hasPermissionLevel(4) ? 32 : distance);
    }
}