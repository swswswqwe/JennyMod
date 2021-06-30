package com.schnurritv.sexmod.girls;

import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import software.bernie.geckolib3.core.controller.*;
import net.minecraft.world.*;
import com.schnurritv.sexmod.util.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;
import com.schnurritv.sexmod.util.Handlers.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.builder.*;
import software.bernie.geckolib3.core.manager.*;
import software.bernie.geckolib3.core.event.*;
import com.schnurritv.sexmod.events.*;
import java.util.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.Packages.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import com.google.common.collect.*;

public abstract class GirlEntity extends EntitySheep implements IAnimatable
{
    private final AnimationFactory factory;
    public EntityAIBase aiWander;
    public EntityAIBase aiLookAtPlayer;
    public static ArrayList<GirlEntity> girlEntities;
    public int currentModel;
    protected String girlName;
    public boolean shouldBeAtTarget;
    public boolean shouldBeAtTargetYaw;
    public boolean shouldGetDamage;
    public Vec3d targetPos;
    public float targetYaw;
    public boolean playerIsInPosition;
    public Vec3d playerCamPos;
    protected float playerYaw;
    public GameType playerGameMode;
    public boolean isForPreloading;
    public EntityPlayer playerSheHasSexWith;
    public double walkSpeed;
    public String animationFollowUp;
    public boolean playerIsThrusting;
    public boolean playerIsCumming;
    public int paymentItemsAmount;
    public PaymentItems paymentItem;
    private static final Set<Item> TEMPTATION_ITEMS;
    public AnimationController actionController;
    public AnimationController movementController;
    public AnimationController eyesController;
    public EnumMap<AnimationParameters, Boolean> animationParameters;
    int preloadTick;
    
    private EnumMap<AnimationParameters, Boolean> createAnimationParameters() {
        return new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
            {
                this.put(AnimationParameters.AUTOHEAD, true);
                this.put(AnimationParameters.AUTOEYES, true);
                this.put(AnimationParameters.MOVEMENTACTIVE, true);
                this.put(AnimationParameters.ISDOINGACTION, false);
                this.put(AnimationParameters.STARTSTRIP, false);
                this.put(AnimationParameters.STARTBLOWJOB, false);
                this.put(AnimationParameters.SUCKBLOWJOB, false);
                this.put(AnimationParameters.CUMBLOWJOB, false);
                this.put(AnimationParameters.THRUSTBLOWJOB, false);
                this.put(AnimationParameters.STARTPAYMENT, false);
                this.put(AnimationParameters.PLAYERSHOULDBERENDERED, false);
                this.put(AnimationParameters.STARTDOGGY, false);
                this.put(AnimationParameters.WAITDOGGY, false);
                this.put(AnimationParameters.DOGGYSTART, false);
                this.put(AnimationParameters.DOGGYSLOW, false);
                this.put(AnimationParameters.DOGGYFAST, false);
                this.put(AnimationParameters.DOGGYCUM, false);
                this.put(AnimationParameters.DASH, false);
                this.put(AnimationParameters.HUG, false);
            }
        };
    }
    
    protected boolean canDespawn() {
        return false;
    }
    
    protected GirlEntity(final World worldIn) {
        super(worldIn);
        this.factory = new AnimationFactory((IAnimatable)this);
        this.currentModel = 1;
        this.girlName = "girl";
        this.shouldBeAtTarget = false;
        this.shouldBeAtTargetYaw = false;
        this.shouldGetDamage = true;
        this.playerIsInPosition = false;
        this.isForPreloading = false;
        this.animationFollowUp = "";
        this.playerIsThrusting = false;
        this.playerIsCumming = false;
        this.paymentItemsAmount = 0;
        this.paymentItem = PaymentItems.DIAMOND;
        this.actionController = new AnimationController((IAnimatable)this, "action", 10.0f, this::predicate);
        this.movementController = new AnimationController((IAnimatable)this, "movement", 5.0f, this::predicate);
        this.eyesController = new AnimationController((IAnimatable)this, "eyes", 10.0f, this::predicate);
        this.animationParameters = this.createAnimationParameters();
        this.preloadTick = 0;
        GirlEntity.girlEntities.add(this);
        this.walkSpeed = 0.35;
    }
    
    protected GirlEntity(final World worldIn, final boolean isForPreloading) {
        super(worldIn);
        this.factory = new AnimationFactory((IAnimatable)this);
        this.currentModel = 1;
        this.girlName = "girl";
        this.shouldBeAtTarget = false;
        this.shouldBeAtTargetYaw = false;
        this.shouldGetDamage = true;
        this.playerIsInPosition = false;
        this.isForPreloading = false;
        this.animationFollowUp = "";
        this.playerIsThrusting = false;
        this.playerIsCumming = false;
        this.paymentItemsAmount = 0;
        this.paymentItem = PaymentItems.DIAMOND;
        this.actionController = new AnimationController((IAnimatable)this, "action", 10.0f, this::predicate);
        this.movementController = new AnimationController((IAnimatable)this, "movement", 5.0f, this::predicate);
        this.eyesController = new AnimationController((IAnimatable)this, "eyes", 10.0f, this::predicate);
        this.animationParameters = this.createAnimationParameters();
        this.preloadTick = 0;
        this.isForPreloading = isForPreloading;
        this.walkSpeed = 0.35;
    }
    
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }
    
    protected void initEntityAI() {
        Reference.server = this.getServer();
        this.walkSpeed = 0.35;
        this.aiWander = (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, this.walkSpeed);
        this.aiLookAtPlayer = (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, (Class)EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 0.4, false, (Set)GirlEntity.TEMPTATION_ITEMS));
    }
    
    public void updateAITasks() {
        if (this.isForPreloading) {
            ++this.preloadTick;
            if (this.preloadTick > 20) {
                this.world.removeEntity((Entity)this);
            }
        }
        if (this.shouldBeAtTarget) {
            this.setPositionAndRotationDirect(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.targetYaw, 0.0f, 1, true);
        }
        else if (this.shouldBeAtTargetYaw) {
            this.setRotation(this.targetYaw, this.rotationPitch);
        }
    }
    
    protected void ChangeTransitionTicks(final int ticks) {
        if (this.actionController.transitionLengthTicks != ticks) {
            PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeTransitionTicks(ticks, this.getPosition()));
        }
    }
    
    protected void TurnPlayerIntoCamera(final EntityPlayerMP player, final boolean autoMoveCamera) {
        this.playerGameMode = player.interactionManager.getGameType();
        player.setGameType(GameType.SPECTATOR);
        final Vec3d forward = player.getForward();
        if (autoMoveCamera) {
            player.setPositionAndUpdate(player.posX + forward.x * 0.35, player.posY, player.posZ + forward.z * 0.35);
        }
        this.targetYaw = player.rotationYawHead + 180.0f;
    }
    
    protected void TurnPlayerIntoCamera(final EntityPlayerMP player) {
        this.playerGameMode = player.interactionManager.getGameType();
        player.setGameType(GameType.SPECTATOR);
        final Vec3d forward = player.getForward();
        player.setPositionAndUpdate(player.posX + forward.x * 0.35, player.posY, player.posZ + forward.z * 0.35);
        this.targetYaw = player.rotationYawHead + 180.0f;
    }
    
    protected void prepareAction() {
        this.tasks.removeTask(this.aiLookAtPlayer);
        this.tasks.removeTask(this.aiWander);
        this.getNavigator().clearPath();
        this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
            {
                this.put(AnimationParameters.AUTOHEAD, false);
                this.put(AnimationParameters.AUTOEYES, false);
            }
        });
        this.TurnPlayerIntoCamera(Objects.requireNonNull(this.getServer()).getPlayerList().getPlayerByUUID(this.playerSheHasSexWith.getPersistentID()));
        this.targetPos = this.getInFrontOfPlayer();
    }
    
    public static ArrayList<GirlEntity> getGirlsByPos(final BlockPos pos) {
        final ArrayList<GirlEntity> girlList = new ArrayList<GirlEntity>();
        for (final GirlEntity sophi : GirlEntity.girlEntities) {
            if (sophi.getPosition().getX() == pos.getX() && sophi.getPosition().getY() == pos.getY() && sophi.getPosition().getZ() == pos.getZ()) {
                girlList.add(sophi);
            }
        }
        int amountOfGirls;
        try {
            amountOfGirls = girlList.get(0).world.playerEntities.size() + 1;
        }
        catch (IndexOutOfBoundsException e) {
            amountOfGirls = GirlEntity.girlEntities.get(0).world.playerEntities.size();
        }
        if (girlList.size() != amountOfGirls) {
            girlList.clear();
            for (final GirlEntity girl : GirlEntity.girlEntities) {
                BlockPos distance = girl.getPosition().subtract((Vec3i)pos);
                distance = new BlockPos(Math.abs(distance.getX()), Math.abs(distance.getY()), Math.abs(distance.getZ()));
                if (distance.getX() + distance.getY() + distance.getZ() <= 2) {
                    girlList.add(girl);
                }
            }
        }
        return girlList;
    }
    
    protected BlockPos findBlock(final BlockPos pos) {
        int step = 1;
        int dir = -1;
        BlockPos searchPos = pos;
        while (step < 22) {
            for (int move = 0; move < 2; ++move) {
                dir *= -1;
                for (int stepTaken = 0; stepTaken < step; ++stepTaken) {
                    searchPos = searchPos.add(0, 0, dir);
                    for (int y = -3; y < 4; ++y) {
                        if (this.world.getBlockState(searchPos.add(0, y, dir)).getBlock() == Blocks.BED) {
                            return searchPos.add(0, y, dir);
                        }
                    }
                }
                for (int stepTaken = 0; stepTaken < step; ++stepTaken) {
                    searchPos = searchPos.add(dir, 0, 0);
                    for (int y = -3; y < 4; ++y) {
                        if (this.world.getBlockState(searchPos.add(dir, y, 0)).getBlock() == Blocks.BED) {
                            return searchPos.add(dir, y, 0);
                        }
                    }
                }
                ++step;
            }
        }
        return null;
    }
    
    protected ResourceLocation getLootTable() {
        return LootTableHandler.GIRL;
    }
    
    public void changeOutfit(final BlockPos pos) {
        PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeSkin(this.currentModel ^ 0x1, pos));
    }
    
    public abstract void startAnimation(final String p0);
    
    protected abstract <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> p0);
    
    protected void createAnimation(final String path, final boolean looped, final AnimationEvent event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(path, Boolean.valueOf(looped)));
    }
    
    protected void ChangeAnimationParameter(final AnimationParameters parameterName, final boolean parameterValue) {
        if (this.animationParameters.get(parameterName) != parameterValue) {
            PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeAnimationParameter(this.getPosition(), parameterName, parameterValue));
            PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeAnimationParameter(this.getPosition(), parameterName, parameterValue));
        }
    }
    
    protected void ChangeAnimationParameters(final EnumMap<AnimationParameters, Boolean> unfilteredParameters) {
        final EnumMap<AnimationParameters, Boolean> filteredParameters = new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class);
        for (final Map.Entry<AnimationParameters, Boolean> parameter : unfilteredParameters.entrySet()) {
            if (this.animationParameters.get(parameter.getKey()) != parameter.getValue()) {
                filteredParameters.put(parameter.getKey(), parameter.getValue());
            }
        }
        if (filteredParameters.size() != 0) {
            PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeAnimationParameter(this.getPosition(), filteredParameters));
            PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeAnimationParameter(this.getPosition(), filteredParameters));
        }
    }
    
    public void registerControllers(final AnimationData data) {
        final AnimationController.ISoundListener movementSoundListener = (AnimationController.ISoundListener)new AnimationController.ISoundListener() {
            public <EntitySophi extends IAnimatable> void playSound(final SoundKeyframeEvent<EntitySophi> event) {
                if ("idle".equals(event.sound)) {
                    GirlEntity.this.ChangeTransitionTicks(10);
                }
            }
        };
        this.movementController.registerSoundListener(movementSoundListener);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }
    
    protected void resetPlayer() {
        this.playerCamPos = null;
        HandlePlayerMovement.active = true;
        PacketHandler.INSTANCE.sendToServer((IMessage)new ResetGirl(this.getPosition(), true));
    }
    
    protected void resetGirl() {
        this.playerCamPos = null;
        this.playerIsThrusting = false;
        this.playerIsCumming = false;
        HandlePlayerMovement.active = true;
        this.setNoGravity(false);
        this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
            {
                this.put(AnimationParameters.ISDOINGACTION, false);
                this.put(AnimationParameters.AUTOHEAD, true);
                this.put(AnimationParameters.AUTOEYES, true);
                this.put(AnimationParameters.MOVEMENTACTIVE, true);
                this.put(AnimationParameters.PLAYERSHOULDBERENDERED, false);
            }
        });
        PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeGirlMovement(0.5, this.getPosition()));
        PacketHandler.INSTANCE.sendToServer((IMessage)new ResetGirl(this.getPosition()));
    }
    
    @SideOnly(Side.CLIENT)
    public static void sendThrust(final UUID playerUUID) {
        for (final GirlEntity girl : GirlEntity.girlEntities) {
            if (girl.playerSheHasSexWith != null && girl.playerSheHasSexWith.getPersistentID().equals(playerUUID) && !girl.playerIsThrusting) {
                girl.thrust();
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void sendCum(final UUID playerUUID) {
        for (final GirlEntity girl : GirlEntity.girlEntities) {
            if (girl.playerSheHasSexWith != null && girl.playerSheHasSexWith.getPersistentID().equals(playerUUID) && !girl.playerIsCumming) {
                girl.cum();
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    protected abstract void thrust();
    
    @SideOnly(Side.CLIENT)
    protected abstract void cum();
    
    protected void moveCamera(final double x, final double y, final double z, final float yaw, final float pitch) {
        if (this.playerCamPos == null) {
            this.playerCamPos = this.playerSheHasSexWith.getPositionVector();
        }
        Vec3d newPos = this.playerCamPos;
        newPos = newPos.add(-Math.sin((this.playerYaw + 90.0f) * 0.017453292519943295) * x, 0.0, Math.cos((this.playerYaw + 90.0f) * 0.017453292519943295) * x);
        newPos = newPos.add(0.0, y, 0.0);
        newPos = newPos.add(-Math.sin(this.playerYaw * 0.017453292519943295) * z, 0.0, Math.cos(this.playerYaw * 0.017453292519943295) * z);
        if (this.world.isRemote) {
            PacketHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.playerSheHasSexWith.getPersistentID().toString(), newPos, this.playerYaw + yaw, pitch));
        }
        else {
            this.playerSheHasSexWith.setPositionAndRotation(newPos.x, newPos.y, newPos.z, this.playerYaw + yaw, pitch);
            this.playerSheHasSexWith.setPositionAndUpdate(newPos.x, newPos.y, newPos.z);
            this.playerSheHasSexWith.setVelocity(0.0, 0.0, 0.0);
        }
    }
    
    protected abstract void checkFollowUp();
    
    protected void say(final String msg) {
        PacketHandler.INSTANCE.sendToAllAround((IMessage)new SendChatMessage("<" + this.girlName + "> " + msg), new NetworkRegistry.TargetPoint(this.dimension, (double)this.getPosition().getX(), (double)this.getPosition().getY(), (double)this.getPosition().getZ(), 40.0));
    }
    
    protected void say(final String msg, final boolean noPrefix) {
        if (noPrefix) {
            PacketHandler.INSTANCE.sendToAllAround((IMessage)new SendChatMessage(msg), new NetworkRegistry.TargetPoint(this.dimension, (double)this.getPosition().getX(), (double)this.getPosition().getY(), (double)this.getPosition().getZ(), 40.0));
        }
    }
    
    protected void playSoundAroundHer(final SoundEvent sound) {
        if (this.world.isRemote) {
            this.world.playSound((double)this.getPosition().getX(), (double)this.getPosition().getY(), (double)this.getPosition().getZ(), sound, SoundCategory.NEUTRAL, 1.0f, 1.0f, false);
        }
        this.world.playSound((EntityPlayer)null, this.getPosition(), sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
    
    protected void playSoundAroundHer(final SoundEvent sound, final float volume) {
        if (this.world.isRemote) {
            this.world.playSound((double)this.getPosition().getX(), (double)this.getPosition().getY(), (double)this.getPosition().getZ(), sound, SoundCategory.NEUTRAL, volume, 1.0f, false);
            return;
        }
        this.world.playSound((EntityPlayer)null, this.getPosition(), sound, SoundCategory.PLAYERS, volume, 1.0f);
    }
    
    protected Vec3d getInFrontOfPlayer() {
        final float playerYaw = this.playerSheHasSexWith.rotationYaw;
        return this.playerSheHasSexWith.getPositionVector().add(-Math.sin(playerYaw * 0.017453292519943295), 0.0, Math.cos(playerYaw * 0.017453292519943295));
    }
    
    public AnimationFactory getFactory() {
        return this.factory;
    }
    
    static {
        GirlEntity.girlEntities = new ArrayList<GirlEntity>();
        TEMPTATION_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.ENDER_PEARL, Items.DYE });
    }
    
    public enum PaymentItems
    {
        DIAMOND, 
        GOLD, 
        EMERALD;
    }
    
    public enum AnimationParameters
    {
        AUTOHEAD, 
        AUTOEYES, 
        MOVEMENTACTIVE, 
        ISDOINGACTION, 
        STARTSTRIP, 
        STARTBLOWJOB, 
        SUCKBLOWJOB, 
        CUMBLOWJOB, 
        THRUSTBLOWJOB, 
        STARTPAYMENT, 
        PLAYERSHOULDBERENDERED, 
        DRAWITEMS, 
        STARTDOGGY, 
        WAITDOGGY, 
        DOGGYSTART, 
        DOGGYSLOW, 
        DOGGYFAST, 
        DOGGYCUM, 
        DASH, 
        HUG;
    }
}
