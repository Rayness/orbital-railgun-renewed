package io.github.mishkis.orbital_railgun.client.rendering;

import org.joml.Matrix4f;

/**
 * The AfterLevel event no longer carries the projection matrix, so it is
 * captured at the head of LevelRenderer#renderLevel by a mixin.
 */
public class OrbitalRailgunMatrices {
    public static final Matrix4f PROJECTION = new Matrix4f();
}
