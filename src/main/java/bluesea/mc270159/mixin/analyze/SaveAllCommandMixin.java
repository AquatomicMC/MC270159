package bluesea.mc270159.mixin.analyze;

import bluesea.mc270159.MC270159;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.SaveAllCommand;

@Mixin(SaveAllCommand.class)
public class SaveAllCommandMixin {
    @Unique
    private static long mc270159$recordTime;

    @Inject(
        method = "saveAll",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveEverything(ZZZ)Z", shift = At.Shift.BEFORE)
    )
    private static void beforeSave(CommandSourceStack commandSourceStack, boolean bl, CallbackInfoReturnable<Integer> cir) {
        MC270159.analyze = true;
        MC270159.analyzeReport = new StringBuilder();
        MC270159.analyzeReport.append("Save Report:");
        mc270159$recordTime = Util.getMillis();
    }

    @Inject(
        method = "saveAll",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveEverything(ZZZ)Z", shift = At.Shift.AFTER)
    )
    private static void afterSave(CommandSourceStack commandSourceStack, boolean bl, CallbackInfoReturnable<Integer> cir) {
        MC270159.saveInfo("Everything", Util.getMillis() - mc270159$recordTime);
        MC270159.analyze = false;
        commandSourceStack.sendSystemMessage(Component.literal(MC270159.analyzeReport.toString()));
    }
}
