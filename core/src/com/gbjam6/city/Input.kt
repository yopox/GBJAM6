package com.gbjam6.city

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.gbjam6.city.general.Util

/**
 * Manages inputs.
 *
 * DPAD is mapped to WASD / ZQSD / Arrows
 * A is mapped to O / SPACE
 * B is mapped to K / ESC
 * START is mapped to B / ENTER
 * SELECT is mapped to V / TAB
 */
interface Input {

    fun processInputs() {
        if (Util.inputFreeze > 0) {
            Util.inputFreeze--
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP) || Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                Util.inputFreeze = 16; up() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                Util.inputFreeze = 16; down() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                Util.inputFreeze = 16; left() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                Util.inputFreeze = 16; right() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.O) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                Util.inputFreeze = 16; a() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.K) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                Util.inputFreeze = 16; b() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.B) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                Util.inputFreeze = 16; start() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.V) || Gdx.input.isKeyPressed(Input.Keys.TAB)) {
                Util.inputFreeze = 16; select() ; Util.wasPressed = true
            } else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                Util.inputFreeze = 16; p() ; Util.wasPressed = true
            } else {
                Util.wasPressed = false
            }
        }
    }

    fun up() {}

    fun down() {}

    fun left() {}

    fun right() {}

    fun a() {}

    fun b() {}

    fun start() {}

    fun select() {}

    fun p() {}

}
