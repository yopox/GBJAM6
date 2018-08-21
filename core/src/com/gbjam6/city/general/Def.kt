package com.gbjam6.city.general

import com.badlogic.gdx.graphics.Color

enum class MenuType {
    CREATION, CATEGORY, BUILDING, CITIZENS, CONFIRM, IMPROVE
}

enum class BuildingType {
    CITIZENS, HAPPINESS, FOOD, RESEARCH, STONE, WATER
}

data class LBuilding(val type: BuildingType, val name: String, val capacity: Int, var door: Pair<Int, Int>, var s8: Pair<Int, Int>, var s16: Pair<Int, Int>)

data class Ressources(var citizens: Int = 0, var food: Int = 0, var stone: Int = 0, var happiness: Int = 0, var research: Int = 0)

object Def {

    // GENERAL
    val startingRessources = Ressources(happiness = 400, stone = 300)
    val speed1 = 120

    // COLORS
    val color1 = Color.valueOf("000000")
    val color2 = Color.valueOf("545454")
    val color3 = Color.valueOf("A9A9A9")
    val color4 = Color.valueOf("FFFFFF")

    // SIZE
    val nChunks = 50
    val menuWidth = 72f
    val menuY = 52f

    // MENUS
    val menus = mapOf(
            MenuType.CREATION to arrayOf("CITIZENS", "HAPPINESS", "FOOD", "RESEARCH", "STONE", "WATER"),
            MenuType.BUILDING to arrayOf("CITIZENS", "USE", "UPGRADE", "REPAIR", "DESTROY"),
            MenuType.CONFIRM to arrayOf("YES", "NO")
    )

    // BUILDINGS
    val buildings = listOf(
            LBuilding(BuildingType.CITIZENS, "CITIZENS1", 5, Pair(34, 41), Pair(34, 41), Pair(24, 41)),
            LBuilding(BuildingType.HAPPINESS, "HAPPINESS1", 5, Pair(13, 20), Pair(13, 26), Pair(13, 39)),
            LBuilding(BuildingType.FOOD, "FOOD1", 5, Pair(19, 39), Pair(19, 39), Pair(19, 39)),
            LBuilding(BuildingType.RESEARCH, "RESEARCH1", 5, Pair(20, 28), Pair(20, 29), Pair(20, 38)),
            LBuilding(BuildingType.STONE, "STONE1", 5, Pair(6, 19), Pair(6, 30), Pair(6, 22)),
            LBuilding(BuildingType.WATER, "WATER1", 5, Pair(0, 17), Pair(0, 17), Pair(0, 17))
    )
    val customMenus = mapOf<String, Array<String>>(
            "WATER1" to arrayOf("CITIZENS", "REPAIR")
    )

    // ACHIEVEMENTS
    val achievements = listOf<Triple<String, String, Boolean>>(
            Triple("ACH1", "Do this n times.", false),
            Triple("ACH2", "Do that n times.", false)
    )

}