package com.example.tvapp.data

data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val streamUrl: String,
    val category: String = "general",
    val epgId: String? = null,
    val channelImageUrl: String? = null
)

data class Program(
    val channelId: String,
    val title: String,
    val description: String?,
    val startTime: Long,
    val endTime: Long,
    val iconUrl: String? = null
) {
    fun isLive(currentTime: Long): Boolean {
        return currentTime in startTime..endTime
    }
    
    fun getDuration(): Long = endTime - startTime
    
    fun getElapsedTime(currentTime: Long): Long {
        return if (currentTime > startTime) currentTime - startTime else 0
    }
}

object ChannelList {
    val channels = listOf(
        // Федеральные каналы
        Channel(
            id = "c1r",
            name = "Первый канал",
            logoUrl = "https://via.placeholder.com/400x225/E31E24/FFFFFF?text=Perviy+Kanal",
            streamUrl = "https://streaming.goodstream.icu/live/210.m3u8",
            category = "federal",
            epgId = "c1r",
            channelImageUrl = "https://img.youtube.com/vi/jfKfPnJR9yw/maxresdefault.jpg"
        ),
        Channel(
            id = "rossiya1",
            name = "Россия 1",
            logoUrl = "https://via.placeholder.com/400x225/0066CC/FFFFFF?text=Rossiya+1",
            streamUrl = "https://streaming.goodstream.icu/live/211.m3u8",
            category = "federal",
            epgId = "rossiya1",
            channelImageUrl = "https://img.youtube.com/vi/7HqZdXqLzKw/maxresdefault.jpg"
        ),
        Channel(
            id = "ntv",
            name = "НТВ",
            logoUrl = "https://via.placeholder.com/400x225/009933/FFFFFF?text=NTV",
            streamUrl = "https://streaming.goodstream.icu/live/213.m3u8",
            category = "federal",
            epgId = "ntv",
            channelImageUrl = "https://img.youtube.com/vi/MnqOGWBmMJE/maxresdefault.jpg"
        ),
        Channel(
            id = "5tv",
            name = "5 Канал",
            logoUrl = "https://via.placeholder.com/400x225/FF6600/FFFFFF?text=5+Kanal",
            streamUrl = "https://streaming.goodstream.icu/live/8.m3u8",
            category = "federal",
            epgId = "5tv"
        ),
        Channel(
            id = "rossiya24",
            name = "Россия 24",
            logoUrl = "https://via.placeholder.com/400x225/CC0000/FFFFFF?text=Rossiya+24",
            streamUrl = "https://streaming.goodstream.icu/live/30.m3u8",
            category = "news",
            epgId = "rossiya24"
        ),
        Channel(
            id = "tvc",
            name = "ТВ Центр",
            logoUrl = "https://via.placeholder.com/400x225/FFCC00/FFFFFF?text=TV+Centr",
            streamUrl = "https://streaming.goodstream.icu/live/13.m3u8",
            category = "federal",
            epgId = "tvc"
        ),
        Channel(
            id = "ren",
            name = "РЕН ТВ",
            logoUrl = "https://via.placeholder.com/400x225/FF0066/FFFFFF?text=REN+TV",
            streamUrl = "https://streaming.goodstream.icu/live/14.m3u8",
            category = "federal",
            epgId = "ren"
        ),
        Channel(
            id = "sts",
            name = "СТС",
            logoUrl = "https://via.placeholder.com/400x225/FF6600/FFFFFF?text=STS",
            streamUrl = "https://streaming.goodstream.icu/live/296.m3u8",
            category = "entertainment",
            epgId = "sts"
        ),
        Channel(
            id = "domashniy",
            name = "Домашний",
            logoUrl = "https://via.placeholder.com/400x225/FF3399/FFFFFF?text=Domashniy",
            streamUrl = "https://streaming.goodstream.icu/live/17.m3u8",
            category = "entertainment",
            epgId = "domashniy"
        ),
        Channel(
            id = "tv3",
            name = "ТВ-3",
            logoUrl = "https://via.placeholder.com/400x225/9933CC/FFFFFF?text=TV-3",
            streamUrl = "https://streaming.goodstream.icu/live/18.m3u8",
            category = "entertainment",
            epgId = "tv3"
        ),
        Channel(
            id = "pz",
            name = "Пятница!",
            logoUrl = "https://via.placeholder.com/400x225/FF00CC/FFFFFF?text=Pyatnitsa",
            streamUrl = "https://streaming.goodstream.icu/live/19.m3u8",
            category = "entertainment",
            epgId = "pz"
        ),
        Channel(
            id = "2x2",
            name = "2х2",
            logoUrl = "https://via.placeholder.com/400x225/FF3300/FFFFFF?text=2x2",
            streamUrl = "https://streaming.goodstream.icu/live/20.m3u8",
            category = "entertainment",
            epgId = "2x2"
        ),
        Channel(
            id = "mir",
            name = "МИР",
            logoUrl = "https://via.placeholder.com/400x225/0066FF/FFFFFF?text=MIR",
            streamUrl = "https://streaming.goodstream.icu/live/22.m3u8",
            category = "news",
            epgId = "mir"
        ),
        Channel(
            id = "spas",
            name = "СПАС",
            logoUrl = "https://via.placeholder.com/400x225/FFD700/FFFFFF?text=SPAS",
            streamUrl = "https://streaming.goodstream.icu/live/15.m3u8",
            category = "religious",
            epgId = "spas"
        ),
        Channel(
            id = "kultura",
            name = "Культура",
            logoUrl = "https://via.placeholder.com/400x225/003399/FFFFFF?text=Kultura",
            streamUrl = "https://streaming.goodstream.icu/live/9.m3u8",
            category = "culture",
            epgId = "kultura"
        ),
        Channel(
            id = "karusel",
            name = "Карусель",
            logoUrl = "https://via.placeholder.com/400x225/FF6600/FFFFFF?text=Karusel",
            streamUrl = "https://streaming.goodstream.icu/live/232.m3u8",
            category = "kids",
            epgId = "karusel"
        ),
        Channel(
            id = "otv",
            name = "ОТВ",
            logoUrl = "https://via.placeholder.com/400x225/0099CC/FFFFFF?text=OTV",
            streamUrl = "https://streaming.goodstream.icu/live/12.m3u8",
            category = "regional",
            epgId = "otv"
        ),
        Channel(
            id = "che",
            name = "Че",
            logoUrl = "https://via.placeholder.com/400x225/CC0000/FFFFFF?text=Che",
            streamUrl = "https://streaming.goodstream.icu/live/23.m3u8",
            category = "entertainment",
            epgId = "che"
        ),
        Channel(
            id = "dom kino",
            name = "Дом Кино",
            logoUrl = "https://via.placeholder.com/400x225/9933CC/FFFFFF?text=Dom+Kino",
            streamUrl = "https://streaming.goodstream.icu/live/44.m3u8",
            category = "movies",
            epgId = "dom_kino"
        ),
        Channel(
            id = "telecafe",
            name = "Телекафе",
            logoUrl = "https://via.placeholder.com/400x225/FF9933/FFFFFF?text=Telecafe",
            streamUrl = "https://streaming.goodstream.icu/live/26.m3u8",
            category = "lifestyle",
            epgId = "telecafe"
        ),
        Channel(
            id = "ruhd",
            name = "РУДЕНЬ",
            logoUrl = "https://via.placeholder.com/400x225/CC0000/FFFFFF?text=RUDEN",
            streamUrl = "https://streaming.goodstream.icu/live/111.m3u8",
            category = "news",
            epgId = "ruhd"
        ),
        Channel(
            id = "mult",
            name = "МУЛЬТ",
            logoUrl = "https://via.placeholder.com/400x225/FF6600/FFFFFF?text=MULT",
            streamUrl = "https://streaming.goodstream.icu/live/112.m3u8",
            category = "kids",
            epgId = "mult"
        ),
        Channel(
            id = "muztv",
            name = "МУЗ-ТВ",
            logoUrl = "https://via.placeholder.com/400x225/FF0099/FFFFFF?text=MUZ-TV",
            streamUrl = "https://streaming.goodstream.icu/live/618.m3u8",
            category = "music",
            epgId = "muztv"
        ),
        Channel(
            id = "tv1000",
            name = "TV1000",
            logoUrl = "https://via.placeholder.com/400x225/9933CC/FFFFFF?text=TV1000",
            streamUrl = "https://streaming.goodstream.icu/live/114.m3u8",
            category = "movies",
            epgId = "tv1000"
        ),
        Channel(
            id = "match",
            name = "Матч ТВ",
            logoUrl = "https://via.placeholder.com/400x225/009933/FFFFFF?text=Match+TV",
            streamUrl = "https://streaming.goodstream.icu/live/6.m3u8",
            category = "sport",
            epgId = "match"
        ),
        
        // Охота и рыбалка
        Channel(
            id = "ohota",
            name = "Охота и рыбалка",
            logoUrl = "https://via.placeholder.com/400x225/2E7D32/FFFFFF?text=Ohota+i+Rybalka",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/116.m3u8",
            category = "hunting_fishing",
            epgId = "ohota"
        ),
        Channel(
            id = "rybolov",
            name = "Рыбалка TV",
            logoUrl = "https://via.placeholder.com/400x225/0288D1/FFFFFF?text=Rybalka+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/117.m3u8",
            category = "hunting_fishing",
            epgId = "rybolov"
        ),
        Channel(
            id = "dikaya_ohota",
            name = "Дикая охота",
            logoUrl = "https://via.placeholder.com/400x225/5D4037/FFFFFF?text=Dikaya+Ohota",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/118.m3u8",
            category = "hunting_fishing",
            epgId = "dikaya_ohota"
        ),
        Channel(
            id = "fishmaster",
            name = "FishMaster",
            logoUrl = "https://via.placeholder.com/400x225/00796B/FFFFFF?text=FishMaster",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/119.m3u8",
            category = "hunting_fishing",
            epgId = "fishmaster"
        ),
        
        // Природа и животные
        Channel(
            id = "animal_planet_ru",
            name = "Animal Planet Россия",
            logoUrl = "https://via.placeholder.com/400x225/0066CC/FFFFFF?text=Animal+Planet",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/120.m3u8",
            category = "nature",
            epgId = "animal_planet_ru"
        ),
        Channel(
            id = "nauka2",
            name = "Наука 2.0",
            logoUrl = "https://via.placeholder.com/400x225/1976D2/FFFFFF?text=Nauka+2.0",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/121.m3u8",
            category = "nature",
            epgId = "nauka2"
        ),
        Channel(
            id = "moya_planeta",
            name = "Моя планета",
            logoUrl = "https://via.placeholder.com/400x225/388E3C/FFFFFF?text=Moya+Planeta",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/122.m3u8",
            category = "nature",
            epgId = "moya_planeta"
        ),
        Channel(
            id = "zhivaya_planeta",
            name = "Живая планета",
            logoUrl = "https://via.placeholder.com/400x225/7CB342/FFFFFF?text=Zhivaya+Planeta",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/123.m3u8",
            category = "nature",
            epgId = "zhivaya_planeta"
        ),
        Channel(
            id = "dikiy",
            name = "Дикий мир",
            logoUrl = "https://via.placeholder.com/400x225/8BC34A/FFFFFF?text=Dikiy+Mir",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/124.m3u8",
            category = "nature",
            epgId = "dikiy"
        ),
        Channel(
            id = "zoo_tv",
            name = "ZOO TV",
            logoUrl = "https://via.placeholder.com/400x225/FDD835/000000?text=ZOO+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/125.m3u8",
            category = "nature",
            epgId = "zoo_tv"
        ),
        
        // Релакс и здоровье
        Channel(
            id = "relax",
            name = "Релакс ТВ",
            logoUrl = "https://via.placeholder.com/400x225/BA68C8/FFFFFF?text=Relax+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/126.m3u8",
            category = "relax",
            epgId = "relax"
        ),
        Channel(
            id = "zdorovoe",
            name = "Здоровое ТВ",
            logoUrl = "https://via.placeholder.com/400x225/4DB6AC/FFFFFF?text=Zdorovoe+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/127.m3u8",
            category = "relax",
            epgId = "zdorovoe"
        ),
        Channel(
            id = "kushe",
            name = "Кухня ТВ",
            logoUrl = "https://via.placeholder.com/400x225/FF7043/FFFFFF?text=Kuhnya+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/128.m3u8",
            category = "lifestyle",
            epgId = "kushe"
        ),
        Channel(
            id = "usadba",
            name = "Усадьба",
            logoUrl = "https://via.placeholder.com/400x225/8D6E63/FFFFFF?text=Usadba",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/129.m3u8",
            category = "lifestyle",
            epgId = "usadba"
        ),
        Channel(
            id = "zagorodny",
            name = "Загородный",
            logoUrl = "https://via.placeholder.com/400x225/66BB6A/FFFFFF?text=Zagorodny",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/130.m3u8",
            category = "lifestyle",
            epgId = "zagorodny"
        ),
        
        // Кино и сериалы дополнительные
        Channel(
            id = "kino_comedy",
            name = "Кинокомедия",
            logoUrl = "https://via.placeholder.com/400x225/EF5350/FFFFFF?text=KinoKomediya",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/131.m3u8",
            category = "movies",
            epgId = "kino_comedy"
        ),
        Channel(
            id = "kino_series",
            name = "Киносериал",
            logoUrl = "https://via.placeholder.com/400x225/EC407A/FFFFFF?text=KinoSerial",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/132.m3u8",
            category = "movies",
            epgId = "kino_series"
        ),
        Channel(
            id = "kino_hit",
            name = "Кинохит",
            logoUrl = "https://via.placeholder.com/400x225/AB47BC/FFFFFF?text=KinoHit",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/133.m3u8",
            category = "movies",
            epgId = "kino_hit"
        ),
        Channel(
            id = "kino_tv",
            name = "Кино ТВ",
            logoUrl = "https://via.placeholder.com/400x225/7E57C2/FFFFFF?text=Kino+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/134.m3u8",
            category = "movies",
            epgId = "kino_tv"
        ),
        Channel(
            id = "ilovecinema",
            name = "I Love Cinema",
            logoUrl = "https://via.placeholder.com/400x225/5C6BC0/FFFFFF?text=I+Love+Cinema",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/135.m3u8",
            category = "movies",
            epgId = "ilovecinema"
        ),
        Channel(
            id = "nash_kino",
            name = "Наше кино",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Наше+кино",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/136.m3u8",
            category = "movies",
            epgId = "nash_kino"
        ),
        Channel(
            id = "indian_kino",
            name = "Индийское кино",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Индийское+кино",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/137.m3u8",
            category = "movies",
            epgId = "indian_kino"
        ),
        Channel(
            id = "tv1000_russian",
            name = "TV1000 Русское кино",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=TV1000+Русское+кино",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/138.m3u8",
            category = "movies",
            epgId = "tv1000_russian"
        ),
        Channel(
            id = "tv1000_action",
            name = "TV1000 Action",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=TV1000+Action",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/139.m3u8",
            category = "movies",
            epgId = "tv1000_action"
        ),
        Channel(
            id = "sony_scifi",
            name = "Сони Sci-Fi",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Сони+Sci-Fi",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/140.m3u8",
            category = "movies",
            epgId = "sony_scifi"
        ),
        Channel(
            id = "sony_channel",
            name = "Сони Канал",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Сони+Канал",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/141.m3u8",
            category = "movies",
            epgId = "sony_channel"
        ),
        Channel(
            id = "sony_turbo",
            name = "Сони Турбо",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Сони+Турбо",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/142.m3u8",
            category = "movies",
            epgId = "sony_turbo"
        ),
        
        // Документальные
        Channel(
            id = "history_ru",
            name = "History Россия",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=History+Россия",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/143.m3u8",
            category = "documentary",
            epgId = "history_ru"
        ),
        Channel(
            id = "viasat_history",
            name = "Viasat History",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Viasat+History",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/144.m3u8",
            category = "documentary",
            epgId = "viasat_history"
        ),
        Channel(
            id = "viasat_nature",
            name = "Viasat Nature",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Viasat+Nature",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/145.m3u8",
            category = "nature",
            epgId = "viasat_nature"
        ),
        Channel(
            id = "viasat_explore",
            name = "Viasat Explore",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Viasat+Explore",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/146.m3u8",
            category = "documentary",
            epgId = "viasat_explore"
        ),
        Channel(
            id = "discovery_ru",
            name = "Discovery Россия",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Discovery+Россия",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/147.m3u8",
            category = "documentary",
            epgId = "discovery_ru"
        ),
        Channel(
            id = "nat_geo_wild",
            name = "Nat Geo Wild",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Nat+Geo+Wild",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/148.m3u8",
            category = "nature",
            epgId = "nat_geo_wild"
        ),
        Channel(
            id = "bbc_earth",
            name = "BBC Earth",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=BBC+Earth",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/149.m3u8",
            category = "nature",
            epgId = "bbc_earth"
        ),
        Channel(
            id = "24_doc",
            name = "24 Док",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=24+Док",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/150.m3u8",
            category = "documentary",
            epgId = "24_doc"
        ),
        Channel(
            id = "mir_serialov",
            name = "Мир Сериалов",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Мир+Сериалов",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/151.m3u8",
            category = "series",
            epgId = "mir_serialov"
        ),
        Channel(
            id = "mira_detektiv",
            name = "Мир Детективов",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Мир+Детективов",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/152.m3u8",
            category = "series",
            epgId = "mira_detektiv"
        ),
        
        // Музыкальные
        Channel(
            id = "ru_tv",
            name = "RU.TV",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=RU.TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/153.m3u8",
            category = "music",
            epgId = "ru_tv"
        ),
        Channel(
            id = "europa_plus",
            name = "Europa Plus TV",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Europa+Plus+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/154.m3u8",
            category = "music",
            epgId = "europa_plus"
        ),
        Channel(
            id = "bridge_tv",
            name = "Bridge TV",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Bridge+TV",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/155.m3u8",
            category = "music",
            epgId = "bridge_tv"
        ),
        Channel(
            id = "bridge_hit",
            name = "Bridge TV Хит",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Bridge+TV+Хит",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/156.m3u8",
            category = "music",
            epgId = "bridge_hit"
        ),
        Channel(
            id = "bridge_classic",
            name = "Bridge TV Classic",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Bridge+TV+Classic",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/157.m3u8",
            category = "music",
            epgId = "bridge_classic"
        ),
        Channel(
            id = "shanson_tv",
            name = "Шансон ТВ",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Шансон+ТВ",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/158.m3u8",
            category = "music",
            epgId = "shanson_tv"
        ),
        Channel(
            id = "first_music",
            name = "Первый Музыкальный",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Первый+Музыкальный",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/159.m3u8",
            category = "music",
            epgId = "first_music"
        ),
        
        // Спорт
        Channel(
            id = "match_premier",
            name = "Матч Премьер",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Матч+Премьер",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/160.m3u8",
            category = "sport",
            epgId = "match_premier"
        ),
        Channel(
            id = "match_arena",
            name = "Матч! Арена",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Матч+Арена",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/161.m3u8",
            category = "sport",
            epgId = "match_arena"
        ),
        Channel(
            id = "match_igra",
            name = "Матч! Игра",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Матч+Игра",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/162.m3u8",
            category = "sport",
            epgId = "match_igra"
        ),
        Channel(
            id = "match_strana",
            name = "Матч! Страна",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Матч+Страна",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/163.m3u8",
            category = "sport",
            epgId = "match_strana"
        ),
        Channel(
            id = "khl_tv",
            name = "КХЛ ТВ",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=КХЛ+ТВ",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/164.m3u8",
            category = "sport",
            epgId = "khl_tv"
        ),
        Channel(
            id = "football_tv",
            name = "Футбол ТВ",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Футбол+ТВ",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/165.m3u8",
            category = "sport",
            epgId = "football_tv"
        ),
        
        // Детские
        Channel(
            id = "tiji",
            name = "TiJi",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=TiJi",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/166.m3u8",
            category = "kids",
            epgId = "tiji"
        ),
        Channel(
            id = "gulli",
            name = "Gulli",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Gulli",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/167.m3u8",
            category = "kids",
            epgId = "gulli"
        ),
        Channel(
            id = "boom",
            name = "Boomerang",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Boomerang",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/168.m3u8",
            category = "kids",
            epgId = "boom"
        ),
        Channel(
            id = "disney_ru",
            name = "Disney Россия",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Disney+Россия",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/169.m3u8",
            category = "kids",
            epgId = "disney_ru"
        ),
        Channel(
            id = "starchild",
            name = "StarChild",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=StarChild",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/170.m3u8",
            category = "kids",
            epgId = "starchild"
        ),
        
        // Новости
        Channel(
            id = "rain",
            name = "Дождь",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Дождь",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/171.m3u8",
            category = "news",
            epgId = "rain"
        ),
        Channel(
            id = "rtvi",
            name = "RTVI",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=RTVI",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/172.m3u8",
            category = "news",
            epgId = "rtvi"
        ),
        Channel(
            id = "euronews_ru",
            name = "Euronews Русский",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Euronews+Русский",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/173.m3u8",
            category = "news",
            epgId = "euronews_ru"
        ),
        
        // Развлекательные
        Channel(
            id = "friday_int",
            name = "Friday! International",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Friday+International",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/174.m3u8",
            category = "entertainment",
            epgId = "friday_int"
        ),
        Channel(
            id = "paramount_comedy",
            name = "Paramount Comedy",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Paramount+Comedy",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/175.m3u8",
            category = "entertainment",
            epgId = "paramount_comedy"
        ),
        Channel(
            id = "black_silver",
            name = "Black & Silver",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Black+&+Silver",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/176.m3u8",
            category = "entertainment",
            epgId = "black_silver"
        ),
        Channel(
            id = "start_world",
            name = "Start World",
            logoUrl = "https://via.placeholder.com/400x225/555555/FFFFFF?text=Start+World",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/177.m3u8",
            category = "entertainment",
            epgId = "start_world"
        )
    )
}
