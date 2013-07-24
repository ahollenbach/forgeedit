package maddjak.forgeedit.client;

import maddjak.forgeedit.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
        @Override
        public void registerRenderers(){
                MinecraftForgeClient.preloadTexture(BLOCK_PNG);
                MinecraftForgeClient.preloadTexture(ITEMS_PNG);
        }
}