package com.andavin.images.v1_13_R2;

import com.andavin.images.PacketListener;
import com.andavin.images.PacketListener.EntityListener;
import com.andavin.images.PacketListener.Hand;
import com.andavin.images.PacketListener.InteractType;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import java.lang.reflect.Field;

import static com.andavin.reflect.Reflection.findField;
import static com.andavin.reflect.Reflection.getFieldValue;

/**
 * @since September 21, 2019
 * @author Andavin
 */
class PlayerConnectionProxy extends PlayerConnection {

    private static final Field ENTITY_ID = findField(PacketPlayInUseEntity.class, "a");
    private final EntityListener listener;

    PlayerConnectionProxy(PlayerConnection connection, EntityListener listener) {
        super(((CraftServer) Bukkit.getServer()).getServer(), connection.networkManager, connection.player);
        this.listener = listener;
    }

    @Override
    public void a(PacketPlayInUseEntity packet) {
        PacketListener.call(this.player.getBukkitEntity(), getFieldValue(ENTITY_ID, packet),
                packet.b() == EnumEntityUseAction.ATTACK ? InteractType.LEFT_CLICK : InteractType.RIGHT_CLICK,
                packet.c() == EnumHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND, this.listener);
        super.a(packet);
    }
}