package io.github.apace100.apoli.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.apace100.apoli.Apoli;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class PowerCommand {

	public static final ResourceLocation COMMAND_POWER_SOURCE = Apoli.identifier("command");

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		// TODO: Clean up this mess.
		dispatcher.register(
				literal("power").requires(cs -> cs.hasPermission(2))
						.then(literal("grant")
								.then(argument("targets", EntityArgument.entities())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> {
															int i = 0;
															try {

																Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
																ResourceLocation power = command.getArgument("power", ResourceLocation.class);
																for (Entity target : targets) {
																	if (target instanceof LivingEntity) {
																		if (grantPower((LivingEntity) target, power)) {
																			i++;
																		}
																	}
																}
																if (i == 0) {
																	if (targets.size() == 1) {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.grant.fail.single", targets.iterator().next().getDisplayName(), power, COMMAND_POWER_SOURCE));
																	} else {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.grant.fail.multiple", targets.size(), power, COMMAND_POWER_SOURCE));
																	}
																} else if (targets.size() == 1 && i == 1) {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.grant.success.single", targets.iterator().next().getDisplayName(), power), true);
																} else {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.grant.success.multiple", i, power), true);
																}
															} catch (Exception e) {
																command.getSource().sendFailure(new TextComponent(e.getMessage()));
															}
															return i;
														}
												)
												.then(argument("source", ResourceLocationArgument.id())
														.executes((command) -> {
															int i = 0;
															try {
																Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
																ResourceLocation power = command.getArgument("power", ResourceLocation.class);
																ResourceLocation source = ResourceLocationArgument.getId(command, "source");
																for (Entity target : targets) {
																	if (target instanceof LivingEntity) {
																		if (grantPower((LivingEntity) target, power, source)) {
																			i++;
																		}
																	}
																}
																if (i == 0) {
																	if (targets.size() == 1) {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.grant.fail.single", targets.iterator().next().getDisplayName(), power, source));
																	} else {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.grant.fail.multiple", targets.size(), power, source));
																	}
																} else if (targets.size() == 1 && i == 1) {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.grant_from_source.success.single", targets.iterator().next().getDisplayName(), power, source), true);
																} else {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.grant_from_source.success.multiple", i, power, source), true);
																}
															} catch (Exception e) {
																command.getSource().sendFailure(new TextComponent(e.getMessage()));
															}
															return i;
														})))))
						.then(literal("revoke")
								.then(argument("targets", EntityArgument.entities())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> {
													int i = 0;
													Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
													ResourceLocation power = command.getArgument("power", ResourceLocation.class);
													try {
														for (Entity target : targets) {
															if (target instanceof LivingEntity) {
																if (revokePower((LivingEntity) target, power)) {
																	i++;
																}
															}
														}

														if (i == 0) {
															if (targets.size() == 1) {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke.fail.single", targets.iterator().next().getDisplayName(), power, COMMAND_POWER_SOURCE));
															} else {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke.fail.multiple", power, COMMAND_POWER_SOURCE));
															}
														} else if (targets.size() == 1) {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke.success.single", targets.iterator().next().getDisplayName(), power), false);
														} else {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke.success.multiple", i, power), false);
														}
													} catch (Exception e) {
														command.getSource().sendFailure(new TextComponent(e.getMessage()));
													}
													return i;
												})
												.then(argument("source", ResourceLocationArgument.id())
														.executes((command) -> {
															int i = 0;
															Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
															ResourceLocation power = command.getArgument("power", ResourceLocation.class);
															ResourceLocation source = ResourceLocationArgument.getId(command, "source");
															try {
																for (Entity target : targets) {
																	if (target instanceof LivingEntity) {
																		if (revokePower((LivingEntity) target, power, source)) {
																			i++;
																		}
																	}
																}

																if (i == 0) {
																	if (targets.size() == 1) {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke.fail.single", targets.iterator().next().getDisplayName(), power, source));
																	} else {
																		command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke.fail.multiple", power, source));
																	}
																} else if (targets.size() == 1) {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke_from_source.success.single", targets.iterator().next().getDisplayName(), power, source), false);
																} else {
																	command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke_from_source.success.multiple", i, power, source), false);
																}
															} catch (Exception e) {
																command.getSource().sendFailure(new TextComponent(e.getMessage()));
															}
															return i;
														})))))
						.then(literal("revokeall")
								.then(argument("targets", EntityArgument.entities())
										.then(argument("source", ResourceLocationArgument.id())
												.executes((command) -> {
													int i = 0;
													Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
													ResourceLocation source = ResourceLocationArgument.getId(command, "source");
													try {
														for (Entity target : targets) {
															if (target instanceof LivingEntity) {
																i += revokeAllPowersFromSource((LivingEntity) target, source);
															}
														}

														if (i == 0) {
															if (targets.size() == 1) {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke_all.fail.single", targets.iterator().next().getDisplayName(), source));
															} else {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.revoke_all.fail.multiple", source));
															}
														} else if (targets.size() == 1) {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke_all.success.single", targets.iterator().next().getDisplayName(), i, source), false);
														} else {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.revoke_all.success.multiple", targets.size(), i, source), false);
														}
													} catch (Exception e) {
														command.getSource().sendFailure(new TextComponent(e.getMessage()));
													}
													return i;
												}))))
						.then(literal("list")
								.then(argument("target", EntityArgument.entity())
										.executes((command) -> {
											int i = 0;
											Entity target = EntityArgument.getEntity(command, "target");
											if (target instanceof LivingEntity living) {
												LazyOptional<IPowerContainer> componentOptional = IPowerContainer.get(living);
												if (!componentOptional.isPresent()) {
													command.getSource().sendFailure(new TranslatableComponent("commands.apoli.list.fail"));
												} else {
													IPowerContainer component = componentOptional.orElseThrow(RuntimeException::new);
													StringBuilder powers = new StringBuilder();
													for (ResourceLocation powerType : component.getPowerTypes(false)) {
														if (i > 0)
															powers.append(", ");
														powers.append(powerType.toString());
														i++;
													}
													command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.list.pass", i, powers), false);
												}
											} else {
												command.getSource().sendFailure(new TranslatableComponent("commands.apoli.list.fail"));
											}
											return i;
										})
										.then(argument("subpowers", BoolArgumentType.bool())
												.executes((command) -> {
													int i = 0;
													Entity target = EntityArgument.getEntity(command, "target");
													boolean listSubpowers = BoolArgumentType.getBool(command, "subpowers");
													if (target instanceof LivingEntity living) {
														LazyOptional<IPowerContainer> componentOptional = IPowerContainer.get(living);
														if (!componentOptional.isPresent()) {
															command.getSource().sendFailure(new TranslatableComponent("commands.apoli.list.fail"));
														} else {
															IPowerContainer component = componentOptional.orElseThrow(RuntimeException::new);
															StringBuilder powers = new StringBuilder();
															for (ResourceLocation powerType : component.getPowerTypes(listSubpowers)) {
																if (i > 0)
																	powers.append(", ");
																powers.append(powerType.toString());
																i++;
															}
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.list.pass", i, powers), false);
														}
													} else {
														command.getSource().sendFailure(new TranslatableComponent("commands.apoli.list.fail"));
													}
													return i;
												}))))
						.then(literal("has")
								.then(argument("targets", EntityArgument.entities())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> {
													int i = 0;
													Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
													ResourceLocation power = command.getArgument("power", ResourceLocation.class);
													for (Entity target : targets) {
														if (target instanceof LivingEntity) {
															if (hasPower((LivingEntity) target, power)) {
																i++;
															}
														}
													}
													if (i == 0) {
														command.getSource().sendFailure(new TranslatableComponent("commands.execute.conditional.fail"));
													} else if (targets.size() == 1) {
														command.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
													} else {
														command.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass_count", i), false);
													}
													return i;
												}))))
						.then(literal("sources")
								.then(argument("target", EntityArgument.entity())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> {
													int i = 0;
													Entity target = EntityArgument.getEntity(command, "target");
													ResourceLocation power = command.getArgument("power", ResourceLocation.class);
													if (target instanceof LivingEntity living) {
														LazyOptional<IPowerContainer> componentOptional = IPowerContainer.get(living);
														if (!componentOptional.isPresent()) {
															command.getSource().sendFailure(new TranslatableComponent("commands.apoli.sources.fail", target.getDisplayName(), power));
														} else {
															IPowerContainer component = componentOptional.orElseThrow(RuntimeException::new);
															StringBuilder sources = new StringBuilder();
															for (ResourceLocation source : component.getSources(power)) {
																if (i > 0)
																	sources.append(", ");
																sources.append(source.toString());
																i++;
															}
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.sources.pass", target.getDisplayName(), i, power, sources), false);
														}
													} else {
														command.getSource().sendFailure(new TranslatableComponent("commands.apoli.sources.fail", target.getDisplayName(), power));
													}
													return i;
												}))))
						.then(literal("remove")
								.then(argument("targets", EntityArgument.entities())
										.then(argument("power", PowerTypeArgumentType.power())
												.executes((command) -> {
													int i = 0;
													Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
													ResourceLocation power = command.getArgument("power", ResourceLocation.class);
													try {
														for (Entity target : targets) {
															if (target instanceof LivingEntity) {
																if (revokePowerAllSources((LivingEntity) target, power)) {
																	i++;
																}
															}
														}

														if (i == 0) {
															if (targets.size() == 1) {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.remove.fail.single", targets.iterator().next().getDisplayName(), power));
															} else {
																command.getSource().sendFailure(new TranslatableComponent("commands.apoli.remove.fail.multiple", power));
															}
														} else if (targets.size() == 1) {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.remove.success.single", targets.iterator().next().getDisplayName(), power), false);
														} else {
															command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.remove.success.multiple", i, power), false);
														}
													} catch (Exception e) {
														command.getSource().sendFailure(new TextComponent(e.getMessage()));
													}
													return i;
												}))))
						.then(literal("clear")
								.then(argument("targets", EntityArgument.entities())
										.executes((command) -> {
											int i = 0;
											Collection<? extends Entity> targets = EntityArgument.getEntities(command, "targets");
											try {
												for (Entity target : targets) {
													if (target instanceof LivingEntity) {
														i += revokeAllPowers((LivingEntity) target);
													}
												}

												if (i == 0) {
													if (targets.size() == 1) {
														command.getSource().sendFailure(new TranslatableComponent("commands.apoli.clear.fail.single", targets.iterator().next().getDisplayName()));
													} else {
														command.getSource().sendFailure(new TranslatableComponent("commands.apoli.clear.fail.multiple"));
													}
												} else if (targets.size() == 1) {
													command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.clear.success.single", targets.iterator().next().getDisplayName(), i), false);
												} else {
													command.getSource().sendSuccess(new TranslatableComponent("commands.apoli.clear.success.multiple", targets.size(), i), false);
												}
											} catch (Exception e) {
												command.getSource().sendFailure(new TextComponent(e.getMessage()));
											}
											return i;
										}))));
	}

	private static boolean grantPower(LivingEntity entity, ResourceLocation power) {
		return grantPower(entity, power, COMMAND_POWER_SOURCE);
	}

	private static boolean grantPower(LivingEntity entity, ResourceLocation power, ResourceLocation source) {
		return IPowerContainer.get(entity).map(component -> {
			boolean success = component.addPower(power, source);
			if (success)
				component.sync();
			return success;
		}).orElse(false);
	}

	private static boolean revokePower(LivingEntity entity, ResourceLocation power) {
		return revokePower(entity, power, COMMAND_POWER_SOURCE);
	}

	private static boolean revokePower(LivingEntity entity, ResourceLocation power, ResourceLocation source) {
		return IPowerContainer.get(entity).map(component -> {
			if (component.hasPower(power, source)) {
				component.removePower(power, source);
				component.sync();
				return true;
			}
			return false;
		}).orElse(false);
	}

	private static boolean revokePowerAllSources(LivingEntity entity, ResourceLocation power) {
		return IPowerContainer.get(entity).map(component -> {
			List<ResourceLocation> sources = component.getSources(power);
			for (ResourceLocation source : sources) {
				component.removePower(power, source);
			}
			if (sources.size() > 0) {
				component.sync();
			}
			return true;
		}).orElse(false);
	}

	private static int revokeAllPowers(LivingEntity entity) {
		return IPowerContainer.get(entity).map(component -> {
			Set<ResourceLocation> powers = component.getPowerTypes(false);
			for (ResourceLocation power : powers) {
				revokePowerAllSources(entity, power);
			}
			if (powers.size() > 0)
				component.sync();
			return powers.size();
		}).orElse(0);
	}

	private static int revokeAllPowersFromSource(LivingEntity entity, ResourceLocation source) {
		return IPowerContainer.get(entity).map(component -> {
			int i = component.removeAllPowersFromSource(source);
			if (i > 0) {
				component.sync();
			}
			return i;
		}).orElse(0);
	}

	private static boolean hasPower(LivingEntity entity, ResourceLocation powerType) {
		return IPowerContainer.get(entity).map(x -> x.hasPower(powerType)).orElse(false);
	}
}
