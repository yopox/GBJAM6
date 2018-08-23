package com.gbjam6.city.general

import com.badlogic.gdx.graphics.Color
import com.gbjam6.city.logic.Ressources

enum class MenuType {
    CREATION, CATEGORY, BUILDING, CITIZENS, CONFIRM, IMPROVE, HYDRATE, ADD, REMOVE
}

enum class BuildingType {
    CITIZENS, HAPPINESS, FOOD, RESEARCH, STONE, OTHER
}

data class LBuilding(val type: BuildingType, val name: String, val capacity: Int, var door: Pair<Int, Int>, var s8: Pair<Int, Int>, var s16: Pair<Int, Int>, val cost: Int, var unlock: Boolean, val upgradeCost: Int = (cost*1.5).toInt())

object Def {

    // GENERAL
    val startingRessources = Ressources(food = 200, happiness = 400, stone = 500)

    // GAME DESIGN
    const val SPEED = 120
    var BIRTH_COST = 100
    var LIFE_TIME = 300
    const val DAMAGED_LIMIT_PCT = 0.90
    var BUILD_LIFE_TIME = 300
    val WELL_RANGE = 80
    val EXCHANGEVALUE = 100
    val EXCHANGETIME = 10
    val HOUSELIMIT = 6
    val HOUSEPLUSLIMIT = 3
    val FARMLIMIT = 100
    val FARMPLUSLIMIT = 50
    val FACTORYLIMIT = 100
    val FACTORYPLUSLIMIT = 50
    val SCHOOLLIMIT = 4
    val SCHOOLCITIZENCOST = 75
    val WAREHOUSELIMIT = 200
    val HOSPITALCITIZENLIFE = 600
    val CRAFTMANBUILDINGLIFE = 600


    // COLORS
    val color1: Color = Color.valueOf("000000")
    val color2: Color = Color.valueOf("545454")
    val color3: Color = Color.valueOf("A9A9A9")
    val color4: Color = Color.valueOf("FFFFFF")

    // SIZE
    const val nChunks = 50
    const val menuWidth = 72f
    const val menuY = 52f
    const val helperWidth = 64f
    const val helperY = 46f
    const val speedY = 54f
    const val speedOffset = 4f

    // MENUS
    val menus = mapOf(
            MenuType.CREATION to arrayOf("CITIZENS", "HAPPINESS", "FOOD", "RESEARCH", "STONE", "OTHER"),
            MenuType.BUILDING to arrayOf("CITIZENS", "REPAIR", "DESTROY"),
            MenuType.CONFIRM to arrayOf("YES", "NO"),
            MenuType.HYDRATE to arrayOf("ADD", "REMOVE", "RETURN")
    )

    // BUILDINGS
    val buildings = listOf(
            LBuilding(BuildingType.CITIZENS, "HOUSE", 6, Pair(34, 41), Pair(34, 41), Pair(24, 41), 100,true),
            LBuilding(BuildingType.HAPPINESS, "TAVERN", 2, Pair(13, 20), Pair(13, 26), Pair(13, 39), 100,true),
            LBuilding(BuildingType.FOOD, "FARM", 2, Pair(19, 39), Pair(19, 39), Pair(19, 39), 100,true),
            LBuilding(BuildingType.RESEARCH, "LABORATORY", 2, Pair(20, 28), Pair(20, 29), Pair(20, 38), 100,true),
            LBuilding(BuildingType.STONE, "FACTORY", 2, Pair(6, 19), Pair(6, 30), Pair(6, 22), 100,true),
            LBuilding(BuildingType.OTHER, "WELL", 0, Pair(0, 17), Pair(0, 17), Pair(0, 17), 100,true),
            LBuilding(BuildingType.STONE, "CRAFTMAN", 1, Pair(20, 36), Pair(20, 36), Pair(20, 36), 100,false),
            LBuilding(BuildingType.FOOD,"WAREHOUSE", 1, Pair(24,51),Pair(24,51),Pair(24,51),200,false),
            LBuilding(BuildingType.HAPPINESS, "GARDEN",1, Pair(43,88),Pair(43,88),Pair(43,88),200,false),
            LBuilding(BuildingType.RESEARCH,"HOSPITAL",1, Pair(20,43), Pair(20,54),Pair(20,64),200,false),
            LBuilding(BuildingType.CITIZENS,"SCHOOL", 4, Pair(67,79),Pair(31,79),Pair(21,79),200,false)
    )

    val upgradedBuilding = listOf(
            LBuilding(BuildingType.CITIZENS, "HOUSE+",9, Pair(34, 41),Pair(34, 41),Pair(24, 41),150,true),
            LBuilding(BuildingType.HAPPINESS, "TAVERN+", 3, Pair(13, 20), Pair(13, 26), Pair(13, 39), 150,true),
            LBuilding(BuildingType.FOOD, "FARM+", 3, Pair(19, 39), Pair(19, 39), Pair(19, 39), 150,true),
            LBuilding(BuildingType.RESEARCH, "LABORATORY+", 3, Pair(20, 28), Pair(20, 29), Pair(20, 38), 150,true),
            LBuilding(BuildingType.STONE, "FACTORY+", 3, Pair(6, 19), Pair(6, 30), Pair(6, 22), 150,true)
    )

    val destroyedRessources = listOf(
            "HOUSE", "HOUSE+", "TAVERN", "TAVERN+", "FARM", "FARM+",
            "LABORATORY", "LABORATORY+", "FACTORY", "FACTORY+",
            "CRAFTMAN", "HOSPITAL", "SCHOOL", "WAREHOUSE"
    )
    val customMenus = mapOf(
            "WELL" to arrayOf("HYDRATE", "REPAIR", "DESTROY"),
            "HOUSE" to arrayOf("CITIZENS", "BIRTH", "UPGRADE", "REPAIR", "DESTROY"),
            "HOUSE+" to arrayOf("CITIZENS", "BIRTH", "REPAIR", "DESTROY"),
            "TAVERN" to arrayOf("CITIZENS", "UPGRADE", "REPAIR", "DESTROYE"),
            "LABORATORY" to arrayOf("CITIZENS", "UPGRADE", "REPAIR", "DESTROYE"),
            "FACTORY" to arrayOf("CITIZENS", "UPGRADE", "REPAIR", "DESTROYE"),
            "FARM" to arrayOf("CITIZENS", "UPGRADE", "REPAIR", "DESTROYE"),
            "GARDEN" to arrayOf("CITIZENS","EXCHANGE","REPAIR","DESTROY")
    )

    // ACHIEVEMENTS
    val achievements = listOf(
            Triple("ACH1", "Do this n times.", false),
            Triple("ACH2", "Do that n times.", false)
    )

    // NAMES
    val names = listOf("Jean",
            "Pas Jean","Aazouf","Abaddon","Abalysan","Abzalon","Actraus","Admimar","Aebron","Aegis","Ael","Aell","Aelrakys","Aeris","Agasha","Agathorn","Aghars","Agon","Aion","Aka","Akarius","Akashana","Akilons","Akodo Tomo","Akyrh","Alahel","Alak Dül","alamar","Alanna","Alark","Alatarielle","Albyor","Alchys","Aldareis","aldarel","Aldou","Alejandro","Alekshan","Alexandre","alge'n","Alhvor","Altan","Alyana","Amatsu","Anamelek","Andurill","Anemar","Angie","Angus","Angye","Anth Rhopy","Aquilon","Aramil","Aravilar","Arcanius","Arch Sinner","Archaos","Arduin Angcam","Arkan","Arkane","Arkos","Armanack","Arnase","Arse","Artanis","Artarien","Arthis","Arthus","Arwen","Ash","Ashragor","Asilurth","Asterion","Aubedorée","Azranil","Bakemonor","Balafrus","Bandidaska","Barthelby","Basara","Bastior","Bennardi","Bilechi","Blader","Blaz","Blobac","Bortzy","Bouch","Brator","Brissaud","Brutar","Buace","Burrich","Byst","Caine","Cal","Calion","Caracal","Céléniel","Chabi","Charlatimus","Cheub","Chouartz","Chubby","Cixi","Clarisse","Claw","Corren","Corus","Crim","Crowyn","Cuchulain","Curios","Cyit","Cyol","Cyrull","Dain","Dakeyras","Dalvyn","Damz","Dargeun","Darius","Darmus","Darniel","Darok","Derfenak","Dergen","Desmond","Devon","Discab","Djaal","Dreike","Drew","Driele","Duncan","Dunkel","Dvalinn","Dworkin","Ebonit","Eel Brodavan","Eickos","Eilis","Ekke","Elbj","Elendil","Elenril","Enas","Endel","Enethaeron","Entrax","Enval","Eol","Epone","Eredren","Eregior","Erquaël","Erwang","Eslhe","Ethudian","Evlyn","Ewak","Eymerich","Eyolas","Faenry","Faeny","Faeriss","Farwander","Faucon de lune","Fedaykin","Fedd","Fenaloeth","Fenrir","Feusange","Finkel","Finraenor","Fitz","Foredrak","Froston","Froum","Fynorel","Gakhad","Galenor","Galenor","Galfon","Galhad","Gallad","Garth","Geheimnis","Genseric","Georetny","Gerd","Gerre","Ghorghor","Ghorgor","Ghylian","Giftbestcom","Glad","Globac","Gloktar","Glorim","Glouk","Gnarl","Gnock","Goldar","Gorons","Gortak","Gouelan","Gramlot","Graoumf","Grimdus","Grimmbart","Grinlen","Grisard","Grisord","Grömm","Grouik","Grung","Gungir","Hades","Hakufu","Halex","Halift","harlok","hauoka","Haymos","Helkior","Hepi","Heras Trydo","Hherylian","Hiaron","Hilarion","Hinata","Hoel","HruKiru","Iblitz","Idefel","Ileuad","Incanus","Intylzah","Iparcos","Iroke","Isilwen","Iuchi","Iverindor","Jaffar","Jalil","Jarvin","Jerrel","Julius","Jushban","Kaar","Kabafort","Kaizen","Kakita","Kalimshar","Kamuchi","Kara","Karel","Karnarok","Kazad-dum","Keldorn","Kellyan","Kendashi","Kenllen","Kentril","Kerien","Kernos","Kev","Khazou","Kherylian","Khid","Khiguard","Khomenor","Khyros","Killiam","Krahor","Krater","Krog","Kronos","Kueyen","Kunden","Kushban","Kylordan","Kymisan","Kyoka","Kyrios","Lankeshire","Lantalasse","Lasseyka","Lavos","Lee Hong","Leo","Lequi","Lexynian","Licken","Lieween","Ligarnes","Lodek","Loh","Lordanor","Lordar","Lothar","Loukae","Ludark","Ludoco","Lufkin","Luindin","Lydae","Lyle","lysandir","Mahar","Mahyar","Maldoror","Maliadus","Malkendar","Malkiak","Mando","Manox","Mansour","Markkisil","Marwenna","Masika","Meeks","Melan","Mespheber","Meuarth","Meurarh","Mikkhaël","Milamber","Mililith","Minky","Minorard","Miraky","Miriantir","Mirthor","Moledrass","Morcar","Morcar","Mordicus","Moreau","Morkdull","Morwan","Moy","Murmure","Musazaï","Myranda","Myrtil","Mystile","Naliah","Narcir","Narral","Naskyrien","Nekronen","Nerath","Netheb","Ninik","Nirannor","Norhel","Odhanan","Odonite","Olric","Onirim","Onizuka","Orgone","Orn","Oronard","Otargos","Oxidor","Palazar","Percey","Perin","Pierral","Pierrus","Pinpin","Plex","Pockels","Psotic","Raeps","Ragus","Rahyll","Ramius","Randin","Rankor","Ranx","Ratafia","Raventher","Ravny","Reador","Rectulo","Redyan","Remoon","Rkanjar","Roan","Rotten","Rumix","Runkah","Sahe","Sam","Samael","Sandax","Sanzo","Scap","Schimill","Sedna","Sensi","Sentenza","Septimus","Sergeiski","Serguisan","Sethie","Sgleurts","Shaan","Shad","Shadrak","Shaïman","Shaniah","Sharvira","Sheas","Sherinford","Sherkan","Shiguru","Shinai","Shuroan","Silvana","Silver","Silvermo","Simeus","Sindir","Sirhun","Sisse","Sivan","Siwu","Slucha","Soho","Sokar","Sokhef","Solkjorn","Sombrelune","Soolveih","Sparkle","Sphax","Steomp","Sulphe","Sun-Tzy","Sylicer","Syzia","Taïrendil","Tahn","Tamva","Taranis","Taroth","Taybott","Tchernopuss","Tenser","Tepes","Terremer","Thadeus","Thanatos","Thathane","Tholdak","Thoralff","Thorin","Thrudgar","Thur","Tigertat","Tlön","Tolunks","Topper","Torgrim","Torken","Tozogawa","Tratore","Treme","Trickster","Twinky","Tykyuk","Ugo","Ungarth","Uryen","Usul","Vain","Valmyr","Valor","Vanion","Varan","Vardjlork","Varkal","Varyus","Veidt","Vendemein","Vensu","Victorio","Victorius","Victoryah","Vilad","Villon","Vince","vinciane","Violhaine","Vlaxonne","Waargh","Walcom","Waldham","Walosprit","Wamaris","Wang","Warfen","Watson","Wazz","Whysmeryll","Willicmac","Wismerhill","Wodian","Wyzzini","Xamaris","Xill","Xorc","Yamael","Yameld","Yaneck","Yann","Yataka","Yick","Yolian","Yorg","Yorl","Yrgaël","Yrrag","Yukio","Zack","Zaf","Zenithal","Ziz","Zool")

    // DESCRIPTIONS
    const val backupDesc = "MISSING :-c\nADD ME IN\nDEF.DESCRIPTIONS"
    val descriptions = mapOf(
            "CITIZENS" to "DESC OF\nCITIZENS",
            "HAPPINESS" to "DESC OF\nHAPP",
            "FOOD" to "DESC OF\nFOOD",
            "RESEARCH" to "DESC OF\nRESEARCH",
            "STONE" to "DESC OF\nSTONE",
            "RETURN" to "GO BACK",
            "REPAIR" to "OCULUS\nREPARO :>",
            "BIRTH" to "DESC OF\nBIRTH",
            "EXCHANGE" to "DESC OF\nEXCHANGE"
    )

    fun getDescription(name: String) = descriptions[name] ?: backupDesc

}