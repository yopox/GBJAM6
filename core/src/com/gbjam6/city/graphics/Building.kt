package com.gbjam6.city.graphics

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.gbjam6.city.general.Def
import com.gbjam6.city.general.LBuilding
import com.gbjam6.city.logic.Ressources
import com.gbjam6.city.general.Util
import com.gbjam6.city.logic.Citizen
import com.gbjam6.city.states.City
import javax.annotation.Resource
import javax.annotation.Resources
import kotlin.math.min

class Building(lBuilding: LBuilding, var x: Float, var y: Float, val manager: AssetManager) {

    var life = Def.BUILD_LIFE_TIME
    val citizens = mutableListOf<Citizen>()
    val citizensToKill = mutableListOf<Citizen>()
    val wateredCitizens = mutableListOf<Citizen>()
    var exchangeTimer = Def.EXCHANGETIME

    private var sprite = Sprite(manager.get("sprites/buildings/${lBuilding.name}.png", Texture::class.java))
    val width = sprite.width
    var lBuilding = lBuilding.copy()
    var validPos: Boolean = true

    /**
     * Flip the building.
     */
    fun flip() {
        sprite.flip(true, false)
        lBuilding.door = lBuilding.door.copy(
                width.toInt() - lBuilding.door.second,
                width.toInt() - lBuilding.door.first)
        lBuilding.s8 = lBuilding.s8.copy(
                width.toInt() - lBuilding.s8.second,
                width.toInt() - lBuilding.s8.first)
        lBuilding.s16 = lBuilding.s16.copy(
                width.toInt() - lBuilding.s16.second,
                width.toInt() - lBuilding.s16.first)
    }

    /**
     * Draw the building.
     */
    fun draw(batch: SpriteBatch) {
        batch.draw(sprite, Util.getPixel(x), Util.getPixel(y))
    }

    /**
     * Returns true if the building can be placed
     */
    fun isValid(): Boolean {

        // The door must be placed on a flat surface
        val door = lBuilding.door
        val chunk1 = City.hills.chunks[Math.floor((x + door.first) / 32.0).toInt() + Def.nChunks / 2]
        val chunk2 = City.hills.chunks[Math.floor((x + door.second) / 32.0).toInt() + Def.nChunks / 2]
        if (chunk1.slope != 0 || chunk2.slope != 0) {
            return false
        }

        // No collisions
        val collision = City.buildings.firstOrNull { if (x < it.x) it.x - x <= width else x - it.x <= it.width }
        if (collision != null) {
            return false
        }

        // Slopes limit

        // Left slope
        val leftChunkNb = Math.floor(x / 32.0).toInt() + Def.nChunks / 2
        if (City.hills.chunks[leftChunkNb].slope < 0) {
            // 1st case : the start of the building is in a negative slope
            val overflowLeft = 32 - (x + Def.nChunks * 32) % 32
            println("s8L : ${lBuilding.s8.first} ; s16L : ${lBuilding.s16.first} ; overflowL : $overflowLeft")
            when (City.hills.chunks[leftChunkNb].slope) {
                -8 -> if (overflowLeft > lBuilding.s8.first) return false
                -16 -> if (overflowLeft > lBuilding.s16.first) return false
            }
        } else if (lBuilding.door.first > 32 && City.hills.chunks[leftChunkNb + 1].slope < 0) {
            // 2nd case : the part of the building before the door is in a negative slope, not the beggining
            val overflowLeft = 64 - (x + Def.nChunks * 32) % 32
            println("s8L : ${lBuilding.s8.first} ; s16L : ${lBuilding.s16.first} ; overflowL : $overflowLeft")
            when (City.hills.chunks[leftChunkNb - 1].slope) {
                -8 -> if (overflowLeft > lBuilding.s8.first) return false
                -16 -> if (overflowLeft > lBuilding.s16.first) return false
            }
        }

        // Right slope
        val rightChunkNb = Math.floor((x + width) / 32.0).toInt() + Def.nChunks / 2
        if (City.hills.chunks[rightChunkNb].slope > 0) {
            // 1st case : the end of the building is in a positive slope
            val overflowRight = (x + width + Def.nChunks * 32) % 32
            println("s8R : ${lBuilding.s8.second} ; s16R : ${lBuilding.s16.second} ; overflowR : $overflowRight starting pixel : ${width - overflowRight}")
            when (City.hills.chunks[rightChunkNb].slope) {
                8 -> if (width - overflowRight < lBuilding.s8.second) return false
                16 -> if (width - overflowRight < lBuilding.s16.second) return false
            }
        } else if (rightChunkNb > 0 && width - lBuilding.door.second > 32 && City.hills.chunks[rightChunkNb - 1].slope > 0) {
            // 2nd case : the part of the building after the door is in a positive slope, not the end
            val overflowRight = 32 + (x + width + Def.nChunks * 32) % 32
            println("s8R : ${lBuilding.s8.second} ; s16R : ${lBuilding.s16.second} ; overflowR : $overflowRight starting pixel : ${width - overflowRight}")
            when (City.hills.chunks[rightChunkNb - 1].slope) {
                8 -> if (width - overflowRight < lBuilding.s8.second) return false
                16 -> if (width - overflowRight < lBuilding.s16.second) return false
            }
        }

        return true

    }

    fun canUse(): Boolean {
        return true
    }

    fun canRepair(): Boolean {
        return ((1-this.life/Def.BUILD_LIFE_TIME.toFloat())*this.lBuilding.cost+1).toInt() <= City.ressources.stone && this.life != Def.BUILD_LIFE_TIME //TODO:CHANGER Def. pa City.upgaret
    }

    fun updateTexture(){
        sprite.texture = manager.get("sprites/buildings/${this.lBuilding.name}.png", Texture::class.java)
    }
    fun canUpgrade(): Boolean {
        return (City.ressources.stone >= this.lBuilding.upgradeCost)
    }

    /**
     * Called when the player places the building.
     */
    fun onPlaced() {
        // Update stones count
        City.ressources.stone -= this.lBuilding.cost

        // Update limits
        when (lBuilding.name) {
            "FACTORY" -> City.limits.stone += Def.FACTORYLIMIT
            "FARM" -> City.limits.food += Def.FARMLIMIT
            "HOUSE" -> City.limits.citizens += Def.HOUSELIMIT
            "SCHOOL" -> {
                City.limits.citizens += Def.SCHOOLLIMIT
                Def.BIRTH_COST = Def.SCHOOLCITIZENCOST
            }
            "WAREHOUSE"-> {
                City.limits.food += Def.WAREHOUSELIMIT
                City.limits.stone += Def.WAREHOUSELIMIT
            }
            "HOSPITAL" -> Def.LIFE_TIME = Def.HOSPITALCITIZENLIFE
            "CRAFTMAN" -> Def.BUILD_LIFE_TIME = Def.CRAFTMANBUILDINGLIFE
        }

        // Make sure limits don't go over 999
        City.limits.stone = min(City.limits.stone, 999)
        City.limits.food = min(City.limits.food, 999)
        City.limits.citizens = min(City.limits.citizens, 999)
    }

    /**
     * Returns the ressources producted by the building.
     */
    fun getProduction(): Ressources {
        return when (lBuilding.name) {
            "FACTORY","FACTORY+" -> Ressources(stone = citizens.size, food = -citizens.size)
            "FARM","FARM+" -> Ressources(food = citizens.size * 3)
            "HOUSE","HOUSE+" -> Ressources(food = -citizens.size)
            "TAVERN","TAVERN+" -> Ressources(happiness = citizens.size * 1, food = -citizens.size)
            "LABORATORY","LABORATORY+" -> Ressources(research = citizens.size * 3, food = -citizens.size)
            "SCHOOL" -> Ressources(food = -citizens.size)
            "WAREHOUSE" -> Ressources(food = citizens.size*5)
            "CRAFTMAN" -> Ressources(stone = citizens.size*2, food = -citizens.size)
            "HOSPITAL" -> Ressources(research = citizens.size*5, food = -citizens.size)
            "GARDEN" -> Ressources(happiness = citizens.size, food = -citizens.size)
            else -> Ressources()
        }
    }

    /**
     * Returns the description of the building.
     * It will be displayed in [Helper].
     */
    fun getDescription(): String {
        var description = "Citizen(s) : \n${citizens.size}/${lBuilding.capacity}\nIntegrity : \n${this.life}/${Def.BUILD_LIFE_TIME}"
        if (this.lBuilding.name == "GARDEN")
            description += "\n Coldown :\n${this.exchangeTimer}/${Def.EXCHANGETIME}"
        return description
    }

    /**
     * Called each tick. Make citizens and the building older.
     */
    fun older(ressources: Ressources, buildingsToDestroy: MutableList<Building>) {
        // Make citizens older
        citizens.map { it.older() }

        // Kill dead citizens
        for (citizen in citizensToKill) {
            citizens.remove(citizen)
            if (citizen.water) {
                citizen.well!!.wateredCitizens.remove(citizen)
                citizen.well = null
            }
            if (citizen.life == 0)
                ressources.citizens -= 1
        }
        citizensToKill.clear()
        // Update exchangeTimer for the Garden
        if (this.lBuilding.name == "GARDEN" && exchangeTimer<Def.EXCHANGETIME)
            exchangeTimer ++
        // Make the building older
        life -= 1
        if (life <= 0) {
            buildingsToDestroy.add(this)
        }
        if (life <= Def.BUILD_LIFE_TIME*Def.DAMAGED_LIMIT_PCT && lBuilding.name in Def.destroyedRessources) {
            sprite.texture = manager.get("sprites/buildings/destroyed/${lBuilding.name} DESTROYED.png", Texture::class.java)
        }
    }

    fun upgrade() {
        City.ressources.stone -= this.lBuilding.upgradeCost
        when (this.lBuilding.name) {
            "HOUSE" -> {
                this.lBuilding = Def.upgradedBuilding[0].copy()
                City.limits.citizens += Def.HOUSEPLUSLIMIT
            }
            "TAVERN" -> this.lBuilding = Def.upgradedBuilding[1].copy()
            "FARM" ->{
                this.lBuilding = Def.upgradedBuilding[2].copy()
                City.limits.food += Def.FARMPLUSLIMIT
            }
            "LABORATORY" -> this.lBuilding = Def.upgradedBuilding[3].copy()
            "FACTORY" -> {
                this.lBuilding = Def.upgradedBuilding[4].copy()
                City.limits.stone += Def.FACTORYPLUSLIMIT
            }
        }
        // Make sure limits don't go over 999
        City.limits.stone = min(City.limits.stone, 999)
        City.limits.food = min(City.limits.food, 999)
        City.limits.citizens = min(City.limits.citizens, 999)
    }
}