package com.example.tvapp.data

data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val streamUrl: String,
    val category: String = "general",
    val epgId: String? = null
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
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/Channel_One_Russia_logo_2024.svg/200px-Channel_One_Russia_logo_2024.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/1.m3u8",
            category = "federal",
            epgId = "c1r"
        ),
        Channel(
            id = "rossiya1",
            name = "Россия 1",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Russia_1_logo_%282017%29.svg/200px-Russia_1_logo_%282017%29.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/2.m3u8",
            category = "federal",
            epgId = "rossiya1"
        ),
        Channel(
            id = "ntv",
            name = "НТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/NTV_logo_2021.svg/200px-NTV_logo_2021.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/4.m3u8",
            category = "federal",
            epgId = "ntv"
        ),
        Channel(
            id = "5tv",
            name = "5 Канал",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/5_kanal_logo_2022.svg/200px-5_kanal_logo_2022.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/5.m3u8",
            category = "federal",
            epgId = "5tv"
        ),
        Channel(
            id = "rossiya24",
            name = "Россия 24",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Russia_24_logo.svg/200px-Russia_24_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/102.m3u8",
            category = "news",
            epgId = "rossiya24"
        ),
        Channel(
            id = "tvc",
            name = "ТВ Центр",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/TV_Center_logo.svg/200px-TV_Center_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/10.m3u8",
            category = "federal",
            epgId = "tvc"
        ),
        Channel(
            id = "ren",
            name = "РЕН ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Ren_tv_logo_2015.svg/200px-Ren_tv_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/6.m3u8",
            category = "federal",
            epgId = "ren"
        ),
        Channel(
            id = "sts",
            name = "СТС",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/STS_logo_2015.svg/200px-STS_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/7.m3u8",
            category = "entertainment",
            epgId = "sts"
        ),
        Channel(
            id = "domashniy",
            name = "Домашний",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Domashniy_logo_2015.svg/200px-Domashniy_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/8.m3u8",
            category = "entertainment",
            epgId = "domashniy"
        ),
        Channel(
            id = "tv3",
            name = "ТВ-3",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/TV3_logo_2023.svg/200px-TV3_logo_2023.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/9.m3u8",
            category = "entertainment",
            epgId = "tv3"
        ),
        Channel(
            id = "pz",
            name = "Пятница!",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Pyatnitsa%21_logo_2015.svg/200px-Pyatnitsa%21_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/11.m3u8",
            category = "entertainment",
            epgId = "pz"
        ),
        Channel(
            id = "2x2",
            name = "2х2",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/2x2_logo_2015.svg/200px-2x2_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/12.m3u8",
            category = "entertainment",
            epgId = "2x2"
        ),
        Channel(
            id = "mir",
            name = "МИР",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/MIR_TV_logo.svg/200px-MIR_TV_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/103.m3u8",
            category = "news",
            epgId = "mir"
        ),
        Channel(
            id = "spas",
            name = "СПАС",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Spas_logo.svg/200px-Spas_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/104.m3u8",
            category = "religious",
            epgId = "spas"
        ),
        Channel(
            id = "kultura",
            name = "Культура",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/18/Russia_K_logo.svg/200px-Russia_K_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/105.m3u8",
            category = "culture",
            epgId = "kultura"
        ),
        Channel(
            id = "karusel",
            name = "Карусель",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/106.m3u8",
            category = "kids",
            epgId = "karusel"
        ),
        Channel(
            id = "otv",
            name = "ОТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/OTV_logo.svg/200px-OTV_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/107.m3u8",
            category = "regional",
            epgId = "otv"
        ),
        Channel(
            id = "che",
            name = "Че",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Che_logo.svg/200px-Che_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/108.m3u8",
            category = "entertainment",
            epgId = "che"
        ),
        Channel(
            id = "dom kino",
            name = "Дом Кино",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/109.m3u8",
            category = "movies",
            epgId = "dom_kino"
        ),
        Channel(
            id = "telecafe",
            name = "Телекафе",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/110.m3u8",
            category = "lifestyle",
            epgId = "telecafe"
        ),
        Channel(
            id = "ruhd",
            name = "РУДЕНЬ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Russia_24_logo.svg/200px-Russia_24_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/111.m3u8",
            category = "news",
            epgId = "ruhd"
        ),
        Channel(
            id = "mult",
            name = "МУЛЬТ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/112.m3u8",
            category = "kids",
            epgId = "mult"
        ),
        Channel(
            id = "muztv",
            name = "МУЗ-ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/113.m3u8",
            category = "music",
            epgId = "muztv"
        ),
        Channel(
            id = "tv1000",
            name = "TV1000",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/114.m3u8",
            category = "movies",
            epgId = "tv1000"
        ),
        Channel(
            id = "match",
            name = "Матч ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/115.m3u8",
            category = "sport",
            epgId = "match"
        ),
        
        // Охота и рыбалка
        Channel(
            id = "ohota",
            name = "Охота и рыбалка",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/2x2_logo_2015.svg/200px-2x2_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/116.m3u8",
            category = "hunting_fishing",
            epgId = "ohota"
        ),
        Channel(
            id = "rybolov",
            name = "Рыбалка TV",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/2x2_logo_2015.svg/200px-2x2_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/117.m3u8",
            category = "hunting_fishing",
            epgId = "rybolov"
        ),
        Channel(
            id = "dikaya_ohota",
            name = "Дикая охота",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/2x2_logo_2015.svg/200px-2x2_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/118.m3u8",
            category = "hunting_fishing",
            epgId = "dikaya_ohota"
        ),
        Channel(
            id = "fishmaster",
            name = "FishMaster",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/2x2_logo_2015.svg/200px-2x2_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/119.m3u8",
            category = "hunting_fishing",
            epgId = "fishmaster"
        ),
        
        // Природа и животные
        Channel(
            id = "animal_planet_ru",
            name = "Animal Planet Россия",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/120.m3u8",
            category = "nature",
            epgId = "animal_planet_ru"
        ),
        Channel(
            id = "nauka2",
            name = "Наука 2.0",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/121.m3u8",
            category = "nature",
            epgId = "nauka2"
        ),
        Channel(
            id = "moya_planeta",
            name = "Моя планета",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/122.m3u8",
            category = "nature",
            epgId = "moya_planeta"
        ),
        Channel(
            id = "zhivaya_planeta",
            name = "Живая планета",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/123.m3u8",
            category = "nature",
            epgId = "zhivaya_planeta"
        ),
        Channel(
            id = "dikiy",
            name = "Дикий мир",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/124.m3u8",
            category = "nature",
            epgId = "dikiy"
        ),
        Channel(
            id = "zoo_tv",
            name = "ZOO TV",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/125.m3u8",
            category = "nature",
            epgId = "zoo_tv"
        ),
        
        // Релакс и здоровье
        Channel(
            id = "relax",
            name = "Релакс ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/126.m3u8",
            category = "relax",
            epgId = "relax"
        ),
        Channel(
            id = "zdorovoe",
            name = "Здоровое ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/127.m3u8",
            category = "relax",
            epgId = "zdorovoe"
        ),
        Channel(
            id = "kushe",
            name = "Кухня ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/128.m3u8",
            category = "lifestyle",
            epgId = "kushe"
        ),
        Channel(
            id = "usadba",
            name = "Усадьба",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/129.m3u8",
            category = "lifestyle",
            epgId = "usadba"
        ),
        Channel(
            id = "zagorodny",
            name = "Загородный",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/130.m3u8",
            category = "lifestyle",
            epgId = "zagorodny"
        ),
        
        // Кино и сериалы дополнительные
        Channel(
            id = "kino_comedy",
            name = "Кинокомедия",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/131.m3u8",
            category = "movies",
            epgId = "kino_comedy"
        ),
        Channel(
            id = "kino_series",
            name = "Киносериал",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/132.m3u8",
            category = "movies",
            epgId = "kino_series"
        ),
        Channel(
            id = "kino_hit",
            name = "Кинохит",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/133.m3u8",
            category = "movies",
            epgId = "kino_hit"
        ),
        Channel(
            id = "kino_tv",
            name = "Кино ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/134.m3u8",
            category = "movies",
            epgId = "kino_tv"
        ),
        Channel(
            id = "ilovecinema",
            name = "I Love Cinema",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/135.m3u8",
            category = "movies",
            epgId = "ilovecinema"
        ),
        Channel(
            id = "nash_kino",
            name = "Наше кино",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/136.m3u8",
            category = "movies",
            epgId = "nash_kino"
        ),
        Channel(
            id = "indian_kino",
            name = "Индийское кино",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/137.m3u8",
            category = "movies",
            epgId = "indian_kino"
        ),
        Channel(
            id = "tv1000_russian",
            name = "TV1000 Русское кино",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/138.m3u8",
            category = "movies",
            epgId = "tv1000_russian"
        ),
        Channel(
            id = "tv1000_action",
            name = "TV1000 Action",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/139.m3u8",
            category = "movies",
            epgId = "tv1000_action"
        ),
        Channel(
            id = "sony_scifi",
            name = "Сони Sci-Fi",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/140.m3u8",
            category = "movies",
            epgId = "sony_scifi"
        ),
        Channel(
            id = "sony_channel",
            name = "Сони Канал",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/141.m3u8",
            category = "movies",
            epgId = "sony_channel"
        ),
        Channel(
            id = "sony_turbo",
            name = "Сони Турбо",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/142.m3u8",
            category = "movies",
            epgId = "sony_turbo"
        ),
        
        // Документальные
        Channel(
            id = "history_ru",
            name = "History Россия",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/143.m3u8",
            category = "documentary",
            epgId = "history_ru"
        ),
        Channel(
            id = "viasat_history",
            name = "Viasat History",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/144.m3u8",
            category = "documentary",
            epgId = "viasat_history"
        ),
        Channel(
            id = "viasat_nature",
            name = "Viasat Nature",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/145.m3u8",
            category = "nature",
            epgId = "viasat_nature"
        ),
        Channel(
            id = "viasat_explore",
            name = "Viasat Explore",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/146.m3u8",
            category = "documentary",
            epgId = "viasat_explore"
        ),
        Channel(
            id = "discovery_ru",
            name = "Discovery Россия",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/147.m3u8",
            category = "documentary",
            epgId = "discovery_ru"
        ),
        Channel(
            id = "nat_geo_wild",
            name = "Nat Geo Wild",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/148.m3u8",
            category = "nature",
            epgId = "nat_geo_wild"
        ),
        Channel(
            id = "bbc_earth",
            name = "BBC Earth",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/149.m3u8",
            category = "nature",
            epgId = "bbc_earth"
        ),
        Channel(
            id = "24_doc",
            name = "24 Док",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Telecafe_logo.svg/200px-Telecafe_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/150.m3u8",
            category = "documentary",
            epgId = "24_doc"
        ),
        Channel(
            id = "mir_serialov",
            name = "Мир Сериалов",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/151.m3u8",
            category = "series",
            epgId = "mir_serialov"
        ),
        Channel(
            id = "mira_detektiv",
            name = "Мир Детективов",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Dom_kino_logo.svg/200px-Dom_kino_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/152.m3u8",
            category = "series",
            epgId = "mira_detektiv"
        ),
        
        // Музыкальные
        Channel(
            id = "ru_tv",
            name = "RU.TV",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/153.m3u8",
            category = "music",
            epgId = "ru_tv"
        ),
        Channel(
            id = "europa_plus",
            name = "Europa Plus TV",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/154.m3u8",
            category = "music",
            epgId = "europa_plus"
        ),
        Channel(
            id = "bridge_tv",
            name = "Bridge TV",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/155.m3u8",
            category = "music",
            epgId = "bridge_tv"
        ),
        Channel(
            id = "bridge_hit",
            name = "Bridge TV Хит",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/156.m3u8",
            category = "music",
            epgId = "bridge_hit"
        ),
        Channel(
            id = "bridge_classic",
            name = "Bridge TV Classic",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/157.m3u8",
            category = "music",
            epgId = "bridge_classic"
        ),
        Channel(
            id = "shanson_tv",
            name = "Шансон ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/158.m3u8",
            category = "music",
            epgId = "shanson_tv"
        ),
        Channel(
            id = "first_music",
            name = "Первый Музыкальный",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Muz_tv_logo.svg/200px-Muz_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/159.m3u8",
            category = "music",
            epgId = "first_music"
        ),
        
        // Спорт
        Channel(
            id = "match_premier",
            name = "Матч Премьер",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/160.m3u8",
            category = "sport",
            epgId = "match_premier"
        ),
        Channel(
            id = "match_arena",
            name = "Матч! Арена",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/161.m3u8",
            category = "sport",
            epgId = "match_arena"
        ),
        Channel(
            id = "match_igra",
            name = "Матч! Игра",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/162.m3u8",
            category = "sport",
            epgId = "match_igra"
        ),
        Channel(
            id = "match_strana",
            name = "Матч! Страна",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/163.m3u8",
            category = "sport",
            epgId = "match_strana"
        ),
        Channel(
            id = "khl_tv",
            name = "КХЛ ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/164.m3u8",
            category = "sport",
            epgId = "khl_tv"
        ),
        Channel(
            id = "football_tv",
            name = "Футбол ТВ",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Match_tv_logo.svg/200px-Match_tv_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/165.m3u8",
            category = "sport",
            epgId = "football_tv"
        ),
        
        // Детские
        Channel(
            id = "tiji",
            name = "TiJi",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/166.m3u8",
            category = "kids",
            epgId = "tiji"
        ),
        Channel(
            id = "gulli",
            name = "Gulli",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/167.m3u8",
            category = "kids",
            epgId = "gulli"
        ),
        Channel(
            id = "boom",
            name = "Boomerang",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/168.m3u8",
            category = "kids",
            epgId = "boom"
        ),
        Channel(
            id = "disney_ru",
            name = "Disney Россия",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/169.m3u8",
            category = "kids",
            epgId = "disney_ru"
        ),
        Channel(
            id = "starchild",
            name = "StarChild",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Karusel_logo_2019.svg/200px-Karusel_logo_2019.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/170.m3u8",
            category = "kids",
            epgId = "starchild"
        ),
        
        // Новости
        Channel(
            id = "rain",
            name = "Дождь",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Russia_24_logo.svg/200px-Russia_24_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/171.m3u8",
            category = "news",
            epgId = "rain"
        ),
        Channel(
            id = "rtvi",
            name = "RTVI",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Russia_24_logo.svg/200px-Russia_24_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/172.m3u8",
            category = "news",
            epgId = "rtvi"
        ),
        Channel(
            id = "euronews_ru",
            name = "Euronews Русский",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Russia_24_logo.svg/200px-Russia_24_logo.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/173.m3u8",
            category = "news",
            epgId = "euronews_ru"
        ),
        
        // Развлекательные
        Channel(
            id = "friday_int",
            name = "Friday! International",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Pyatnitsa%21_logo_2015.svg/200px-Pyatnitsa%21_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/174.m3u8",
            category = "entertainment",
            epgId = "friday_int"
        ),
        Channel(
            id = "paramount_comedy",
            name = "Paramount Comedy",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Pyatnitsa%21_logo_2015.svg/200px-Pyatnitsa%21_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/175.m3u8",
            category = "entertainment",
            epgId = "paramount_comedy"
        ),
        Channel(
            id = "black_silver",
            name = "Black & Silver",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Pyatnitsa%21_logo_2015.svg/200px-Pyatnitsa%21_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/176.m3u8",
            category = "entertainment",
            epgId = "black_silver"
        ),
        Channel(
            id = "start_world",
            name = "Start World",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Pyatnitsa%21_logo_2015.svg/200px-Pyatnitsa%21_logo_2015.svg.png",
            streamUrl = "https://streaming.televizor-24-tochka.ru/live/177.m3u8",
            category = "entertainment",
            epgId = "start_world"
        )
    )
}
