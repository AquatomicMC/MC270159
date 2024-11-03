package bluesea.mc270159.mixin;

import bluesea.mc270159.MC270159;
import java.io.OutputStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

@Mixin(NbtIo.class)
public class NbtIoRecordMixin {
    @Unique
    private static long mc270159$recordTime;

    @Inject(
        method = "writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/io/OutputStream;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtIo;write(Lnet/minecraft/nbt/CompoundTag;Ljava/io/DataOutput;)V",
            shift = At.Shift.AFTER
        )
    )
    private static void writeCompressedBeforeClose(CompoundTag compoundTag, OutputStream outputStream, CallbackInfo ci) {
        if (MC270159.record) {
            mc270159$recordTime = Util.getMillis();
        }
    }

    @Inject(
        method = "writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/io/OutputStream;)V",
        at = @At(value = "RETURN")
    )
    private static void writeCompressedRETURN(CompoundTag compoundTag, OutputStream outputStream, CallbackInfo ci) {
        if (MC270159.record) {
            MC270159.LOGGER.info("Time to close: {}ms", Util.getMillis() - mc270159$recordTime);
            MC270159.record = false;
        }
    }
}
