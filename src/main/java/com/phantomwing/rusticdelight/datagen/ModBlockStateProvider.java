package com.phantomwing.rusticdelight.datagen;

import com.phantomwing.rusticdelight.RusticDelight;
import com.phantomwing.rusticdelight.block.ModBlocks;
import com.phantomwing.rusticdelight.block.custom.BellPepperCropBlock;
import com.phantomwing.rusticdelight.block.custom.CottonCropBlock;
import com.phantomwing.rusticdelight.block.custom.PancakeBlock;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RusticDelight.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCottonCrop((CropBlock) ModBlocks.COTTON_CROP.get(), "cotton_stage", "cotton_stage");
        makeBellPepperCrop((CropBlock) ModBlocks.BELL_PEPPER_CROP.get(), "bell_peppers_stage", "bell_peppers_stage");

        simpleBlockWithItem(ModBlocks.WILD_COTTON.get(), models().cross(blockTexture(ModBlocks.WILD_COTTON.get()).getPath(),
                blockTexture(ModBlocks.WILD_COTTON.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.WILD_BELL_PEPPERS.get(), models().cross(blockTexture(ModBlocks.WILD_BELL_PEPPERS.get()).getPath(),
                blockTexture(ModBlocks.WILD_BELL_PEPPERS.get())).renderType("cutout"));

        simpleBlockWithItem(ModBlocks.POTTED_WILD_COTTON.get(), models()
                .singleTexture("potted_wild_cotton",
                        new ResourceLocation("flower_pot_cross"),
                        "plant",
                        blockTexture(ModBlocks.WILD_COTTON.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_WILD_BELL_PEPPERS.get(), models()
                .singleTexture("potted_wild_bell_peppers",
                        new ResourceLocation("flower_pot_cross"),
                        "plant",
                        blockTexture(ModBlocks.WILD_BELL_PEPPERS.get())).renderType("cutout"));

        farmersDelightBag(ModBlocks.COTTON_SEEDS_BAG.get());
        farmersDelightBag(ModBlocks.BELL_PEPPER_SEEDS_BAG.get());

        farmersDelightCrate(ModBlocks.COTTON_BOLL_CRATE.get());
        farmersDelightCrate(ModBlocks.BELL_PEPPER_GREEN_CRATE.get());
        farmersDelightCrate(ModBlocks.BELL_PEPPER_YELLOW_CRATE.get());
        farmersDelightCrate(ModBlocks.BELL_PEPPER_RED_CRATE.get());

        pieBlock(ModBlocks.CHERRY_BLOSSOM_CHEESECAKE.get());

        pancakeBlock(ModBlocks.HONEY_PANCAKES.get());
        pancakeBlock(ModBlocks.CHOCOLATE_PANCAKES.get());
        pancakeBlock(ModBlocks.CHERRY_BLOSSOM_PANCAKES.get());
        pancakeBlock(ModBlocks.VEGETABLE_PANCAKES.get());
    }

    public void makeCottonCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cottonStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cottonStates(BlockState state, CropBlock block, String modelName, String textureName) {
        var ageProperty = state.getValue(((CottonCropBlock) block).getAgeProperty());
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + ageProperty,
                new ResourceLocation(RusticDelight.MOD_ID, "block/" + textureName + ageProperty)).renderType("cutout"));

        return models;
    }

    public void makeBellPepperCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> bellPepperStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] bellPepperStates(BlockState state, CropBlock block, String modelName, String textureName) {
        var ageProperty = state.getValue(((BellPepperCropBlock) block).getAgeProperty());
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().cross(modelName + ageProperty,
                new ResourceLocation(RusticDelight.MOD_ID, "block/" + textureName + ageProperty)).renderType("cutout"));

        return models;
    }

    private void farmersDelightCrate(Block block) {
        String blockName = blockName(block);
        this.simpleBlock(block,
                models().cubeBottomTop(blockName, resourceBlock(blockName + "_side"), resourceBlock("crate_bottom"), resourceBlock(blockName + "_top")));
    }

    private void farmersDelightBag(Block block) {
        String blockName = blockName(block);
        this.simpleBlock(block, models().withExistingParent(blockName, "cube")
                .texture("particle", resourceBlock(blockName + "_top"))
                .texture("down", resourceBlock("bag_bottom"))
                .texture("up", resourceBlock(blockName + "_top"))
                .texture("north", resourceBlock("bag_side_tied"))
                .texture("south", resourceBlock("bag_side_tied"))
                .texture("east", resourceBlock("bag_side"))
                .texture("west", resourceBlock("bag_side"))
        );
    }

    private void pieBlock(Block block) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                            int bites = state.getValue(PieBlock.BITES);
                            String suffix = bites == 0 ? "" : "_slice" + bites;
                            return ConfiguredModel.builder()
                                    .modelFile(existingModel(blockName(block) + suffix))
                                    .rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                                    .build();
                        }
                );
    }

    private void pancakeBlock(Block block) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                            int bites = state.getValue(PancakeBlock.SERVINGS);
                            String suffix = "_stage" + bites;
                            return ConfiguredModel.builder()
                                    .modelFile(existingModel(blockName(block) + suffix))
                                    .rotationY(((int) state.getValue(PancakeBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                                    .build();
                        }
                );
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return new ResourceLocation(RusticDelight.MOD_ID, "block/" + path);
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(resourceBlock(path), models().existingFileHelper);
    }
}
