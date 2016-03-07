package org.tendiwa.backend.modules.roguelike.aspects

import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.chunks.chunkWithTile
import org.tendiwa.backend.space.chunks.get
import org.tendiwa.backend.space.lighting.lighting
import org.tendiwa.backend.space.transparency.transparency
import org.tendiwa.collections.withoutLast
import org.tendiwa.existence.NoReactionAspect
import org.tendiwa.existence.NoStimuliAspectKind
import org.tendiwa.plane.grid.constructors.centeredGridRectangle
import org.tendiwa.plane.grid.constructors.segmentTo
import org.tendiwa.plane.grid.dimensions.by
import org.tendiwa.plane.grid.masks.contains

class NPCVision : NoReactionAspect(kind) {
    companion object {
        val kind = NoStimuliAspectKind()
        val VISION_RANGE = 21 by 21
    }

    fun canSee(reality: Reality, target: Position): Boolean {
        fun a(c: Int): Int {
            return c + 1
        }
        reality.hostOf(this).run {
            val closeEnough =
                position
                    .tile
                    .let { centeredGridRectangle(it, VISION_RANGE) }
                    .contains(target.tile)
            val isVisible =
                position.tile
                    .segmentTo(target.tile)
                    .tilesList
                    .withoutLast()
                    .all { reality.space.transparency.isTransparent(it) }
            val isLit =
                reality.space.lighting
                    .chunkWithTile(position.tile)
                    .get(position.tile) > 0
            return closeEnough && isVisible && isLit
        }
    }
}