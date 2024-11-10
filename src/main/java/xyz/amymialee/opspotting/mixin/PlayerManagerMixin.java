package xyz.amymialee.opspotting.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.CommonPlayerSpawnInfo;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @WrapOperation(method = "setViewDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"))
    private void a(@NotNull PlayerManager instance, Packet<?> packet, Operation<Void> original, int viewDistance) {
        var deop = new ChunkLoadDistanceS2CPacket(viewDistance);
        var op = new ChunkLoadDistanceS2CPacket(32);
        for (var serverPlayerEntity : instance.getPlayerList()) {
            serverPlayerEntity.networkHandler.sendPacket(serverPlayerEntity.hasPermissionLevel(4) ? op : deop);
        }
    }

    @WrapOperation(method = "onPlayerConnect", at = @At(value = "NEW", target = "(IZLjava/util/Set;IIIZZZLnet/minecraft/network/packet/s2c/play/CommonPlayerSpawnInfo;Z)Lnet/minecraft/network/packet/s2c/play/GameJoinS2CPacket;"))
    private GameJoinS2CPacket b(int playerEntityId, boolean bl, Set<RegistryKey<World>> set, int i, int j, int k, boolean bl2, boolean bl3, boolean bl4, CommonPlayerSpawnInfo commonPlayerSpawnInfo, boolean bl5, @NotNull Operation<GameJoinS2CPacket> original, ClientConnection connection, @NotNull ServerPlayerEntity player) {
        return original.call(playerEntityId, bl, set, i, player.hasPermissionLevel(4) ? 32 : j, k, bl2, bl3, bl4, commonPlayerSpawnInfo, bl5);
    }
}