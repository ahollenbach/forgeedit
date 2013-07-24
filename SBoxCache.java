package maddjak.forgeedit;

import java.util.Vector;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import cc.apoc.bboutline.BBoxCache;
import cc.apoc.bboutline.Config;
import cc.apoc.bboutline.PacketHandler;
import cc.apoc.bboutline.util.BBoxFactory;
import cc.apoc.bboutline.util.BBoxInt;

public class SBoxCache extends BBoxCache {

	public SBoxCache(Config config, World world, int dimensionId,
			IChunkProvider chunkProvider) {
		super(config, world, dimensionId, chunkProvider);
	}
	
	/**
     * Fill the structure bounding box cache, ignores already present ones.
     */
	@Override
    public synchronized void update() {
														  //minX,minY,minZ,maxX,maxY,maxZ
        StructureBoundingBox bb = new StructureBoundingBox();
		BBoxInt structureStartBB = BBoxFactory.createBBoxInt(bb);
        if (!cache.containsKey(structureStartBB)) {
            cache.put(structureStartBB, new Vector<BBoxInt>());
            FMLLog.info("[%d] new cache entries: %d", dimensionId, 1);
        }
    }
}
