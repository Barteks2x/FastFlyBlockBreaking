package com.github.barteks2x.fastflybreak;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class FFBB {
    @Redirect(method = "getBlockBreakingSpeed",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z"))
    public boolean blockBreakSpeed(PlayerEntity _this){
        return true;
    }
}
