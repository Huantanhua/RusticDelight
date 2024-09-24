package com.phantomwing.rusticdelight.loot;

import com.mojang.serialization.Codec;
import com.phantomwing.rusticdelight.RusticDelight;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RusticDelight.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SQUID_DROPS_CALAMARI_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("squids_drop_calamari_modifier", SquidsDropCalamariModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
