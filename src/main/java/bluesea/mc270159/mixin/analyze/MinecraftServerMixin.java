package bluesea.mc270159.mixin.analyze;

import bluesea.mc270159.MC270159;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique
    private static long mc270159$recordTime;
    @Unique
    private static long mc270159$childRecordTime;

    @Inject(
        method = "saveEverything",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;saveAll()V")
    )
    private void saveEverythingBeforeSavePlayerList(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        mc270159$recordTime = Util.getMillis();
    }

    @Inject(
        method = "saveEverything",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;saveAll()V", shift = At.Shift.AFTER)
    )
    private void saveEverythingAfterSavePlayerList(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        MC270159.saveInfo("- Players", Util.getMillis() - mc270159$recordTime);
        mc270159$childRecordTime = Util.getMillis();
    }

    @Inject(
        method = "saveEverything",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z", shift = At.Shift.AFTER)
    )
    private void saveEverythingAfterSaveAllChunks(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        MC270159.saveInfo("- AllChunks", Util.getMillis() - mc270159$childRecordTime);
    }

    @Inject(
        method = "saveAllChunks",
        at = @At(value = "HEAD")
    )
    private void saveAllChunksBeforeSaveLevels(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        mc270159$recordTime = Util.getMillis();
    }

    @Inject(
        method = "saveAllChunks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"
        )
    )
    private void saveAllChunksAfterSaveLevels(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        MC270159.saveInfo("  - Levels", Util.getMillis() - mc270159$recordTime);
        mc270159$recordTime = Util.getMillis();
    }

    @Inject(
        method = "saveAllChunks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;saveDataTag(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V"
        )
    )
    private void saveAllChunksBeforeSaveDataTag(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        MC270159.saveInfo("  - Others", Util.getMillis() - mc270159$recordTime);
        mc270159$recordTime = Util.getMillis();
    }

    @Inject(
        method = "saveAllChunks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;saveDataTag(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V",
            shift = At.Shift.AFTER
        )
    )
    private void saveAllChunksAfterSaveDataTag(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        MC270159.saveInfo("  - DataTag", Util.getMillis() - mc270159$recordTime);
    }
}
