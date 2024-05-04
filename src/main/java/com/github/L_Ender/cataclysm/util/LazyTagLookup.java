package com.github.L_Ender.cataclysm.util;

import net.minecraft.tags.TagKey;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

public record LazyTagLookup<TYPE>(TagKey<TYPE> key, Lazy<ITag<TYPE>> lazyTag) {

    public static <TYPE> LazyTagLookup<TYPE> create(IForgeRegistry<TYPE> registry, TagKey<TYPE> key) {
        return new LazyTagLookup<>(key, Lazy.of(() -> manager(registry).getTag(key)));
    }

    public ITag<TYPE> tag() {
        return lazyTag.get();
    }

    public boolean contains(TYPE element) {
        return tag().contains(element);
    }

    public boolean isEmpty() {
        return tag().isEmpty();
    }

    public static <TYPE> ITagManager<TYPE> manager(IForgeRegistry<TYPE> registry) {
        ITagManager<TYPE> tags = registry.tags();
        if (tags == null) {
            throw new IllegalStateException("Expected " + registry.getRegistryName() + " to have tags.");
        }
        return tags;
    }
}