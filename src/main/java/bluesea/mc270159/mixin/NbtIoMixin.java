package bluesea.mc270159.mixin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.NbtIo;

@Mixin(NbtIo.class)
public class NbtIoMixin {
    @Redirect(
        method = "writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V",
        at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;newOutputStream(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStream;")
    )
    private static OutputStream writeCompressed(Path path, OpenOption[] options) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream;
    }
}
