package dev.lu15.voicechat.network.minecraft;

import dev.lu15.voicechat.network.minecraft.packets.VoiceStatePacket;
import dev.lu15.voicechat.network.minecraft.packets.HandshakeAcknowledgePacket;
import dev.lu15.voicechat.network.minecraft.packets.HandshakePacket;
import dev.lu15.voicechat.network.minecraft.packets.UpdateStatePacket;
import dev.lu15.voicechat.network.minecraft.packets.VoiceStatesPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.common.PluginMessagePacket;
import org.jetbrains.annotations.NotNull;

public final class MinecraftPacketHandler {

    private final @NotNull Map<String, Function<NetworkBuffer, Packet>> suppliers = new HashMap<>();

    public MinecraftPacketHandler() {
        this.register("voicechat:request_secret", HandshakePacket::new);
        this.register("voicechat:secret", HandshakeAcknowledgePacket::new);
        this.register("voicechat:update_state", UpdateStatePacket::new);
        this.register("voicechat:player_state", VoiceStatePacket::new);
        this.register("voicechat:player_states", VoiceStatesPacket::new);
    }

    public void register(@NotNull String id, @NotNull Function<NetworkBuffer, Packet> supplier) {
        this.suppliers.put(id, supplier);
    }

    public @NotNull Packet read(@NotNull String identifier, byte[] data) throws Exception {
        Function<NetworkBuffer, Packet> supplier = this.suppliers.get(identifier);
        if (supplier == null) throw new IllegalStateException(String.format("invalid packet id: %s", identifier));

        NetworkBuffer buffer = NetworkBuffer.wrap(data, 0, data.length);
        return supplier.apply(buffer);
    }

    public @NotNull PluginMessagePacket write(@NotNull Packet packet) {
        NetworkBuffer buffer = NetworkBuffer.resizableBuffer();
        packet.write(buffer);

        byte[] data = new byte[(int) buffer.readableBytes()];
        buffer.copyTo(0, data, 0, data.length);

        return new PluginMessagePacket(packet.id(), data);
    }

}