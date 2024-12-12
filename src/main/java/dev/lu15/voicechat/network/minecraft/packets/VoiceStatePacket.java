package dev.lu15.voicechat.network.minecraft.packets;

import dev.lu15.voicechat.VoiceState;
import dev.lu15.voicechat.network.minecraft.Packet;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

public record VoiceStatePacket(@NotNull VoiceState state) implements Packet {

    public VoiceStatePacket(@NotNull NetworkBuffer buffer) {
        this(buffer.read(VoiceState.NETWORK_TYPE));
    }

    @Override
    public void write(@NotNull NetworkBuffer writer) {
        writer.write(VoiceState.NETWORK_TYPE, this.state);
    }

    @Override
    public void read(@NotNull NetworkBuffer buffer) {

    }

    @Override
    public @NotNull String id() {
        return "voicechat:player_state";
    }

}
