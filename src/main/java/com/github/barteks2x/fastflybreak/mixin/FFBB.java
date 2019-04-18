package com.github.barteks2x.fastflybreak.mixin;

import com.github.barteks2x.fastflybreak.Config;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Properties;

@Mixin(PlayerEntity.class)
public abstract class FFBB extends LivingEntity {
    @Shadow @Final public PlayerAbilities abilities;

    protected FFBB(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Redirect(method = "getBlockBreakingSpeed",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z"))
    public boolean blockBreakSpeed(PlayerEntity _this){
        return this.onGround || this.abilities.allowFlying || this.abilities.flying || Config.alswaysFastBreaking;
    }
}
