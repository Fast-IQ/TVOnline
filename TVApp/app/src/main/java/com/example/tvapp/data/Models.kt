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
        )
    )
}
