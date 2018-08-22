package com.gbjam6.city

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.gbjam6.city.general.*
import com.gbjam6.city.graphics.Building
import com.gbjam6.city.graphics.Helper
import com.gbjam6.city.graphics.Menu
import com.gbjam6.city.logic.Citizen
import com.gbjam6.city.states.City
import com.gbjam6.city.states.States
import java.lang.Math.abs

/**
 * Manages the menus' logic.
 */
class MenuManager(private val gbJam6: GBJam6) {

    var menus = mutableListOf<Menu>()
    var citizensInReach: List<Citizen>? = null
    var placingB: Building? = null
    var placingC: Citizen? = null
    private var frame = 0

    companion object {
        var helper = Helper()
    }

    /**
     * Called when the player selects a spot of the map.
     */
    fun open(): States {
        val x = City.camera.position.x

        // Checks if the player clicked on a building
        val building = Util.getBuilding()

        // Adds the corresponding menu
        if (building == null) {
            menus.add(Menu(MenuType.CREATION, "SELECT CATEGORY", x + 4f, Def.menuY, gbJam6))
        } else {
            val items = Def.customMenus[building.lBuilding.name] ?: Def.menus[MenuType.BUILDING]!!
            menus.add(Menu(MenuType.BUILDING, "SELECT ACTION", x + 4f, Def.menuY, gbJam6, items))
            menus.last().changeValidity(building)
        }

        // Shows the helper
        Util.updateMenuHelper(menus)
        MenuManager.helper.visible = true

        return States.MENU
    }

    /**
     * Called to close a menu.
     */
    fun close() {
        if (menus.any())
            menus = menus.dropLast(1).toMutableList()
    }

    /**
     * Called when the player selects an option from the menu.
     */
    fun select(pointerY: Float): States {

        // Gets camera position
        val position = City.camera.position

        // Gets the selected building
        val selectedB = Util.getBuilding()

        if (City.state == States.MENU) {

            // A menu item is selected
            val menu = menus.last()

            when (menu.type) {

                // The player selects a category
                MenuType.CREATION -> {
                    // Gets all buildings from selected category
                    val categoryB = Def.buildings.filter { it.type == BuildingType.valueOf(menu.items[menu.cursorPos]) }
                    val items = MutableList(categoryB.size) { categoryB[it].name }
                    items.add("RETURN")

                    // Makes building gray if the player doesn't have enough stone to build it
                    val validity = Array(items.size) { i -> if (i < categoryB.size) categoryB[i].cost <= City.ressources.stone else true }

                    // Adds the menu
                    menus.add(Menu(MenuType.CATEGORY, menu.items[menu.cursorPos], position.x + 4f, Def.menuY, gbJam6, items.toTypedArray(), validity))
                }

                // The player chooses a building to build
                MenuType.CATEGORY -> {
                    if (menu.items[menu.cursorPos] == "RETURN") {
                        close()
                        return States.MENU
                    }
                    // Places the building only if it is possible
                    if (menu.activated[menu.cursorPos]) {
                        // Creates placingB
                        placingB = Building(Def.buildings.first { it.name == menu.items[menu.cursorPos] }, position.x, -16f, gbJam6.manager)
                        updateBuilding(pointerY)
                        frame = 0

                        // Closes the helper
                        MenuManager.helper.visible = false

                        return States.PLACE_BUILDING
                    }
                }

                // The player checks a building
                MenuType.BUILDING -> {
                    when (menu.items[menu.cursorPos]) {

                        "CITIZENS" -> {
                            // Creates the list of citizens' names
                            val citizens = selectedB!!.citizens
                            val names = MutableList(citizens.size) { citizens[it].name }

                            // Creates menu with appropriate title
                            val title = if (names.isEmpty()) "NO CITIZENS!" else "CHECK&MOVE"
                            names.add("RETURN")
                            menus.add(Menu(MenuType.CITIZENS, title, position.x + 4, Def.menuY, gbJam6, names.toTypedArray()))
                        }

                        "BIRTH" -> {
                            if (menu.activated[menu.cursorPos]) {
                                // Creates the new citizen
                                placingC = Citizen(Def.names.random(), selectedB!!)
                                selectedB.citizens.add(placingC!!)

                                // Updates ressources
                                City.ressources.citizens += 1
                                City.ressources.happiness -= Def.BIRTH_COST

                                // Opens the helper
                                MenuManager.helper.visible = true

                                return States.PLACE_CITIZEN
                            }
                        }

                        "HYDRATE" -> {
                            val validity = arrayOf(selectedB!!.citizens.size < 2, selectedB.citizens.size > 0, true)
                            menus.add(Menu(MenuType.HYDRATE, "ACTION", position.x + 4, Def.menuY, gbJam6, validity = validity))
                        }
                    }
                }

                // The player interacts with a well
                MenuType.HYDRATE -> {
                    when (menu.items[menu.cursorPos]) {
                        "RETURN" -> close()
                        "ADD" -> {
                            // Gets citizens in range
                            val buildings = City.buildings.filter { abs(it.x + 9 - selectedB!!.x) < 70 || abs(selectedB.x + selectedB.width - it.x - 9) < 70 }
                            val citizens = mutableListOf<Citizen>()

                            for (build in buildings)
                                for (cit in build.citizens.filter { it.water.not() })
                                    citizens.add(cit)

                            citizensInReach = citizens

                            // Adds the citizens in reach menu
                            val names = MutableList(citizens.size) { citizens[it].name }
                            names.add("RETURN")
                            menus.add(Menu(MenuType.ADD, "REACHABLE", position.x + 4, Def.menuY, gbJam6, names.toTypedArray()))
                        }
                        "REMOVE" -> {
                            // Shows watered citizens
                            val citizens = selectedB!!.wateredCitizens
                            val names = MutableList(citizens.size) { citizens[it].name }
                            names.add("RETURN")
                            menus.add(Menu(MenuType.REMOVE, "REMOVE", position.x + 4, Def.menuY, gbJam6, names.toTypedArray()))
                        }
                    }
                }

                // The player wants to link a citizen to the well
                MenuType.ADD -> {
                    when (menu.items[menu.cursorPos]) {
                        "RETURN" -> close()
                        else -> {
                            if (menu.cursorPos < citizensInReach!!.size) {
                                selectedB!!.wateredCitizens.add(citizensInReach!!.elementAt(menu.cursorPos))
                                citizensInReach!!.elementAt(menu.cursorPos).water = true
                                citizensInReach!!.elementAt(menu.cursorPos).well = selectedB
                            }
                            close()
                        }
                    }
                }

                // The player wants to unlink a citizen from the well
                MenuType.REMOVE -> {
                    when (menu.items[menu.cursorPos]) {
                        "RETURN" -> close()
                        else -> {
                            if (menu.cursorPos < selectedB!!.wateredCitizens.size) {
                                selectedB.wateredCitizens[menu.cursorPos].water = false
                                selectedB.wateredCitizens[menu.cursorPos].well = null
                                selectedB.wateredCitizens.removeAt(menu.cursorPos)
                            }
                            close()
                        }
                    }
                }

                // The user selects a citizen
                MenuType.CITIZENS -> {
                    when (menu.items[menu.cursorPos]) {
                        "RETURN" -> close()
                        else -> {
                            if (Util.housingLeft()) {
                                // Gets the citizen to move
                                placingC = selectedB!!.citizens[menu.cursorPos]

                                // Opens the helper
                                MenuManager.helper.visible = true

                                // Remove from well
                                if (placingC!!.water) {
                                    placingC!!.well!!.wateredCitizens.remove(placingC!!)
                                    placingC!!.well = null
                                    placingC!!.water = false
                                }

                                return States.PLACE_CITIZEN
                            }
                        }
                    }
                }
                MenuType.CONFIRM -> {
                }
                MenuType.IMPROVE -> {
                }
            }
            return States.MENU

        } else if (City.state == States.PLACE_BUILDING) {

            // The building is placed
            if (placingB!!.validPos) {
                Util.placeBuilding(placingB!!)
                menus.clear()
                placingB = null
            } else {
                return States.PLACE_BUILDING
            }

        } else if (City.state == States.PLACE_CITIZEN) {
            // Checks the citizen can be placed in this building
            if (selectedB != null && selectedB.citizens.size < selectedB.lBuilding.capacity) {
                // Removes placingC from its old building
                placingC!!.building.citizens.remove(placingC!!)

                // Places it in its new one
                placingC!!.building = selectedB
                selectedB.citizens.add(placingC!!)

                // Closes the helper
                MenuManager.helper.visible = false

                // Go back to [States.IDLE]
                placingC = null
                menus.clear()

            } else {
                // The citizen cannot be placed in [selectedB]
                return States.PLACE_CITIZEN
            }
        }

        return States.IDLE

    }

    /**
     * Called when the user wants to select another item.
     */
    fun moveCursor(i: Int) {
        val menu = menus.last()
        menu.cursorPos = (menu.cursorPos + menu.items.size + i) % menu.items.size

        // Updates the helper
        Util.updateMenuHelper(menus)
    }

    /**
     * Draws the visible menu.
     */
    fun drawMenu(batch: SpriteBatch, font: BitmapFont) {
        // Draws the menu
        if (menus.any() && placingB == null && placingC == null) {
            menus.last().draw(batch, font)
        }
    }

    /**
     * Draws the moving building
     */
    fun drawBuilding(batch: SpriteBatch) {
        placingB?.let {
            if (it.validPos || !it.validPos && frame < 30)
                it.draw(batch)
        }
    }

    /**
     * Moves [placingB] with the camera.
     */
    fun updateBuilding(y: Float) {
        placingB?.let {
            val x = City.camera.position.x
            it.x = x - it.lBuilding.door.first - Math.floor((it.lBuilding.door.second - it.lBuilding.door.first) / 2.0).toFloat()
            it.y = y - 2
            it.validPos = it.isValid()
        }
    }

    /**
     * Moves [menus] when the camera moved (for instance during [States.PLACE_BUILDING]).
     */
    fun updateMenu() {
        val x = City.camera.position.x
        for (menu in menus)
            menu.x = x + 4
    }

    /**
     * Flips [placingB].
     */
    fun flip(y: Float) {
        val x = City.camera.position.x
        placingB?.flip()
        updateBuilding(y)
    }

    /**
     * Called each frame, used to make [placingB] blink.
     */
    fun update() {
        frame = (frame + 1) % 60
    }

    fun drawHelper(batch: SpriteBatch, smallFont: BitmapFont) {
        val x = City.camera.position.x
        helper.draw(batch, smallFont, x - 76f)
    }

    /**
     * Used to update the menu when a
     */
    fun tick() {
        val menu = menus.lastOrNull()
        if (menu != null && City.state == States.MENU)
            menu.changeValidity(Util.getBuilding())
    }

}