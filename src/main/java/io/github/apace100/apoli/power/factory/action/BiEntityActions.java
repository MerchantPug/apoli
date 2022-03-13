package io.github.apace100.apoli.power.factory.action;


public class BiEntityActions {

	//@SuppressWarnings("unchecked")
	public static void register() {
        /*register(AndAction.getFactory(ApoliDataTypes.BIENTITY_ACTIONS));
        register(ChanceAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
        register(IfElseAction.getFactory(ApoliDataTypes.BIENTITY_ACTION, ApoliDataTypes.BIENTITY_CONDITION));
        register(ChoiceAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
        register(IfElseListAction.getFactory(ApoliDataTypes.BIENTITY_ACTION, ApoliDataTypes.BIENTITY_CONDITION));
        register(DelayAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
        register(NothingAction.getFactory());*/

        /*register(new ActionFactory<>(Apoli.identifier("invert"), new SerializableData()
            .add("action", ApoliDataTypes.BIENTITY_ACTION),
            (data, entities) -> {
                ((ActionFactory<Pair<Entity, Entity>>.Instance)data.get("action")).accept(new Pair<>(entities.getRight(), entities.getLeft()));
            }));*/
        /*register(new ActionFactory<>(Apoli.identifier("actor_action"), new SerializableData()
            .add("action", ApoliDataTypes.ENTITY_ACTION),
            (data, entities) -> {
                ((ActionFactory<Entity>.Instance)data.get("action")).accept(entities.getLeft());
            }));*/
        /*register(new ActionFactory<>(Apoli.identifier("target_action"), new SerializableData()
            .add("action", ApoliDataTypes.ENTITY_ACTION),
            (data, entities) -> {
                ((ActionFactory<Entity>.Instance)data.get("action")).accept(entities.getRight());
            }));*/

       /* register(new ActionFactory<>(Apoli.identifier("mount"), new SerializableData(),
            (data, entities) -> {
                entities.getLeft().startRiding(entities.getRight(), true);
                if(!entities.getLeft().world.isClient && entities.getRight() instanceof PlayerEntity) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(entities.getLeft().getId());
                    buf.writeInt(entities.getRight().getId());
                    ServerPlayNetworking.send((ServerPlayerEntity) entities.getRight(), ModPackets.PLAYER_MOUNT, buf);
                }
            }));*/
       /* register(new ActionFactory<>(Apoli.identifier("set_in_love"), new SerializableData(),
            (data, entities) -> {
                if(entities.getRight() instanceof AnimalEntity && entities.getLeft() instanceof PlayerEntity) {
                    ((AnimalEntity)entities.getRight()).lovePlayer((PlayerEntity)entities.getLeft());
                }
            }));*/
        /*register(new ActionFactory<>(Apoli.identifier("tame"), new SerializableData(),
            (data, entities) -> {
                if(entities.getRight() instanceof TameableEntity && entities.getLeft() instanceof PlayerEntity) {
                    if(!((TameableEntity)entities.getRight()).isTamed()) {
                        ((TameableEntity)entities.getRight()).setOwner((PlayerEntity)entities.getLeft());
                    }
                }
            }));*/
        /*register(new ActionFactory<>(Apoli.identifier("add_velocity"), new SerializableData()
            .add("x", SerializableDataTypes.FLOAT, 0F)
            .add("y", SerializableDataTypes.FLOAT, 0F)
            .add("z", SerializableDataTypes.FLOAT, 0F)
            .add("client", SerializableDataTypes.BOOLEAN, true)
            .add("server", SerializableDataTypes.BOOLEAN, true)
            .add("set", SerializableDataTypes.BOOLEAN, false),
            (data, entities) -> {
                Entity actor = entities.getLeft(), target = entities.getRight();
                if (target instanceof PlayerEntity
                    && (target.world.isClient ?
                        !data.getBoolean("client") : !data.getBoolean("server")))
                    return;
                Vec3f vec = new Vec3f(data.getFloat("x"), data.getFloat("y"), data.getFloat("z"));
                TriConsumer<Float, Float, Float> method = target::addVelocity;
                if(data.getBoolean("set"))
                    method = target::setVelocity;
                Space.transformVectorToBase(target.getPos().subtract(actor.getPos()), vec, actor.getYaw(), true); // vector normalized by method
                method.accept(vec.getX(), vec.getY(), vec.getZ());
                target.velocityModified = true;
            }));*/
		//register(DamageAction.getFactory());
	}

   /* private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }*/
}
