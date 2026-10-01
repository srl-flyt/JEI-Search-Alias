package com.github.srl_flyt.jeisearchalias;

import com.github.srl_flyt.jeisearchalias.config.AliasConfig;
import net.minecraftforge.fml.common.Mod;

@Mod(JeiSearchAlias.MODID)
public class JeiSearchAlias {
    public static final String MODID = "jei_search_alias";
    public JeiSearchAlias() {
        AliasConfig.init();
    }
}