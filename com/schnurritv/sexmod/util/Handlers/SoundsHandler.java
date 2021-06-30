package com.schnurritv.sexmod.util.Handlers;

import java.util.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.registries.*;
import com.schnurritv.sexmod.util.*;

public class SoundsHandler
{
    public static final SoundEvent[] GIRLS_JENNY_AFTERSESSIONMOAN;
    public static final SoundEvent[] GIRLS_JENNY_AHH;
    public static final SoundEvent[] GIRLS_JENNY_BJMOAN;
    public static final SoundEvent[] GIRLS_JENNY_GIGGLE;
    public static final SoundEvent[] GIRLS_JENNY_HAPPYOH;
    public static final SoundEvent[] GIRLS_JENNY_HEAVYBREATHING;
    public static final SoundEvent[] GIRLS_JENNY_HMPH;
    public static final SoundEvent[] GIRLS_JENNY_HUH;
    public static final SoundEvent[] GIRLS_JENNY_LIGHTBREATHING;
    public static final SoundEvent[] GIRLS_JENNY_LIPSOUND;
    public static final SoundEvent[] GIRLS_JENNY_MMM;
    public static final SoundEvent[] GIRLS_JENNY_MOAN;
    public static final SoundEvent[] GIRLS_JENNY_SADOH;
    public static final SoundEvent[] GIRLS_JENNY_SIGH;
    public static final SoundEvent[] MISC_PLOB;
    public static final SoundEvent[] MISC_BELLJINGLE;
    public static final SoundEvent[] MISC_BEDRUSTLE;
    public static final SoundEvent[] MISC_SLAP;
    public static final SoundEvent[] MISC_TOUCH;
    public static final SoundEvent[] MISC_POUNDING;
    public static final SoundEvent[] MISC_SMALLINSERTS;
    public static final SoundEvent[] MISC_CUMINFLATION;
    private static SoundEvent[][] soundCategorys;
    public static final SoundsHandler INSTANCE;
    static HashMap<SoundEvent, Integer> lastRandomSound;
    
    public static void registerSounds() {
        for (int soundCategoryIndex = 0; soundCategoryIndex < SoundsHandler.soundCategorys.length; ++soundCategoryIndex) {
            final SoundEvent[] soundCategory = SoundsHandler.soundCategorys[soundCategoryIndex];
            for (int soundIndex = 0; soundIndex < soundCategory.length; ++soundIndex) {
                final String path = SoundsHandler.INSTANCE.getClass().getDeclaredFields()[soundCategoryIndex].getName().toLowerCase().replace("_", ".");
                String categoryName;
                try {
                    categoryName = path.split("\\.")[2];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    categoryName = path.split("\\.")[1];
                }
                soundCategory[soundIndex] = registerSound(path + "." + categoryName + soundIndex);
            }
        }
    }
    
    public static SoundEvent registerSound(final String path) {
        final ResourceLocation location = new ResourceLocation("sexmod", path);
        final SoundEvent event = new SoundEvent(location);
        event.setRegistryName(path);
        ForgeRegistries.SOUND_EVENTS.register((IForgeRegistryEntry)event);
        return event;
    }
    
    public static SoundEvent Random(final SoundEvent[] soundArray) {
        if (SoundsHandler.lastRandomSound.get(soundArray[0]) == null) {
            SoundsHandler.lastRandomSound.put(soundArray[0], -69);
        }
        int random;
        do {
            random = Reference.RANDOM.nextInt(soundArray.length);
        } while (random == SoundsHandler.lastRandomSound.get(soundArray[0]));
        SoundsHandler.lastRandomSound.replace(soundArray[0], random);
        return soundArray[random];
    }
    
    static {
        GIRLS_JENNY_AFTERSESSIONMOAN = new SoundEvent[5];
        GIRLS_JENNY_AHH = new SoundEvent[10];
        GIRLS_JENNY_BJMOAN = new SoundEvent[13];
        GIRLS_JENNY_GIGGLE = new SoundEvent[5];
        GIRLS_JENNY_HAPPYOH = new SoundEvent[3];
        GIRLS_JENNY_HEAVYBREATHING = new SoundEvent[8];
        GIRLS_JENNY_HMPH = new SoundEvent[5];
        GIRLS_JENNY_HUH = new SoundEvent[2];
        GIRLS_JENNY_LIGHTBREATHING = new SoundEvent[12];
        GIRLS_JENNY_LIPSOUND = new SoundEvent[10];
        GIRLS_JENNY_MMM = new SoundEvent[9];
        GIRLS_JENNY_MOAN = new SoundEvent[8];
        GIRLS_JENNY_SADOH = new SoundEvent[2];
        GIRLS_JENNY_SIGH = new SoundEvent[2];
        MISC_PLOB = new SoundEvent[1];
        MISC_BELLJINGLE = new SoundEvent[1];
        MISC_BEDRUSTLE = new SoundEvent[2];
        MISC_SLAP = new SoundEvent[2];
        MISC_TOUCH = new SoundEvent[2];
        MISC_POUNDING = new SoundEvent[10];
        MISC_SMALLINSERTS = new SoundEvent[5];
        MISC_CUMINFLATION = new SoundEvent[1];
        SoundsHandler.soundCategorys = new SoundEvent[][] { SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN, SoundsHandler.GIRLS_JENNY_AHH, SoundsHandler.GIRLS_JENNY_BJMOAN, SoundsHandler.GIRLS_JENNY_GIGGLE, SoundsHandler.GIRLS_JENNY_HAPPYOH, SoundsHandler.GIRLS_JENNY_HEAVYBREATHING, SoundsHandler.GIRLS_JENNY_HMPH, SoundsHandler.GIRLS_JENNY_HUH, SoundsHandler.GIRLS_JENNY_LIGHTBREATHING, SoundsHandler.GIRLS_JENNY_LIPSOUND, SoundsHandler.GIRLS_JENNY_MMM, SoundsHandler.GIRLS_JENNY_MOAN, SoundsHandler.GIRLS_JENNY_SADOH, SoundsHandler.GIRLS_JENNY_SIGH, SoundsHandler.MISC_PLOB, SoundsHandler.MISC_BELLJINGLE, SoundsHandler.MISC_BEDRUSTLE, SoundsHandler.MISC_SLAP, SoundsHandler.MISC_TOUCH, SoundsHandler.MISC_POUNDING, SoundsHandler.MISC_SMALLINSERTS, SoundsHandler.MISC_CUMINFLATION };
        INSTANCE = new SoundsHandler();
        SoundsHandler.lastRandomSound = new HashMap<SoundEvent, Integer>();
    }
}
