package dev.experimental.apoli.common.action.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.experimental.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalInt;

public record CommandConfiguration(String command,
								   int permissionLevel) implements IDynamicFeatureConfiguration {
	public static final MapCodec<CommandConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("command").forGetter(CommandConfiguration::command),
			Codec.INT.optionalFieldOf("permission_level", 4).forGetter(CommandConfiguration::permissionLevel)
	).apply(instance, CommandConfiguration::new));

	public static final Codec<CommandConfiguration> CODEC = MAP_CODEC.codec();

	public OptionalInt execute(Entity entity) {
		MinecraftServer server = entity.level.getServer();
		if (server != null) {
			CommandSourceStack source = new CommandSourceStack(
					CommandSource.NULL,
					entity.position(),
					entity.getRotationVector(),
					entity.level instanceof ServerLevel sl ? sl : null,
					this.permissionLevel(),
					entity.getName().getString(),
					entity.getDisplayName(),
					entity.level.getServer(),
					entity);
			return OptionalInt.of(server.getCommands().performCommand(source, this.command()));
		}
		return OptionalInt.empty();
	}

	public OptionalInt execute(Level world, BlockPos pos) {
		MinecraftServer server = world.getServer();
		if (server != null) {
			String blockName = world.getBlockState(pos).getBlock().getDescriptionId();
			CommandSourceStack source = new CommandSourceStack(
					CommandSource.NULL,
					new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
					new Vec2(0, 0),
					(ServerLevel) world,
					this.permissionLevel(),
					blockName,
					new TranslatableComponent(blockName),
					server,
					null);
			return OptionalInt.of(server.getCommands().performCommand(source, this.command()));
		}
		return OptionalInt.empty();
	}
}
