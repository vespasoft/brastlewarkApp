package com.vespadev.brastlewarkapp.data

import com.vespadev.brastlewarkapp.data.models.Hero
import com.vespadev.brastlewarkapp.domain.ResultException
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun List<Hero>.mapToHeroEntity(): List<HeroEntity> =
    this.map {
        HeroEntity(
            id = it.id,
            name = it.name,
            imageUrl = it.thumbnail,
            age = it.age,
            weight = it.weight,
            height = it.height,
            hair_color = it.hair_color,
            professions = it.professions,
            friends = getFriends(this, it.friends)
        )
    }

fun List<Hero>.filterHeroes(params: String?): List<Hero> = params?.let { param ->
    this.filter { hero -> hero.name.contains(param, ignoreCase = true) }
} ?: this


fun getFriends(heroes: List<Hero>, friends: List<String>): List<HeroEntity> =
    heroes.filter { hero -> hero.name in friends }.mapToHeroEntity()

fun Throwable.resolveError(): ResultException =
    when (this) {
        is SocketTimeoutException -> {
            ResultException.NetworkException(message = "connection error!")
        }
        is ConnectException -> {
            ResultException.NetworkException(message = "no internet access!")
        }
        is UnknownHostException -> {
            ResultException.NetworkException(message = "no internet access!")
        }
        is HttpException -> {
            when (this.code()) {
                502 -> {
                    ResultException.NetworkException(message = this.localizedMessage)
                }
                401 -> {
                    ResultException.AuthenticationException("authentication error!")
                }
                400 -> {
                    ResultException.ServiceException(message = this.localizedMessage)
                }
                else -> {
                    ResultException.GenericException(this.localizedMessage)
                }
            }
        }
        else -> {
            ResultException.GenericException(this.localizedMessage)
        }
    }

fun animals(): List<String> = listOf(
    "https://www.gratistodo.com/wp-content/uploads/2016/08/fondos-iphone-24.jpg",
    "https://t1.ea.ltmcdn.com/es/posts/4/7/7/los_animales_mas_exoticos_del_mundo_3774_orig.jpg",
    "https://media.istockphoto.com/photos/jaguar-looking-at-camera-pantanal-wetlands-brazil-picture-id1152709116?k=20&m=1152709116&s=612x612&w=0&h=sBERog12oge8eTJINadcAbAIHrTSuxIcx0a2xHFwnoQ=",
    "https://postgradoveterinaria.com/wp-content/uploads/animales-exoticos.jpg",
    "https://hospitalveterinariodonostia.com/wp-content/uploads/2018/12/6-lugares-donde-puedes-ver-animales-exoticos-6.jpg",
    "https://i.pinimg.com/originals/5f/40/38/5f40389cf67b6fc4ec3314aae899a239.png",
    "https://atlasanimal.com/wp-content/uploads/2021/03/Animales-Exoticos.jpg",
    "https://t2.ea.ltmcdn.com/es/posts/4/7/7/slow_loris_3774_0_600.jpg",
    "https://cvsauces.com/nuevo/wp-content/uploads/2017/02/reptiles1.jpg",
    "https://media.istockphoto.com/photos/knobbed-hornbill-picture-id1202411093?k=20&m=1202411093&s=612x612&w=0&h=KNp6AHda3v5D-HspXRuNj-NgppzFlMFk6mcOptek7Bw=",
    "https://cope-cdnmed.agilecontent.com/resources/jpg/9/4/1555648942449.jpg",
    "https://margachipaulaga.files.wordpress.com/2014/02/animals_primates_snarling-mandrill.jpg",
    "https://i.pinimg.com/550x/79/d8/24/79d8246cc274de7b2e4ca09a00dcb386.jpg",
    "https://demascotas.info/wp-content/uploads/2020/10/Sin-titulo-14.png",
    "https://www.hospitalveterinariglories.com/wp-content/uploads/2021/06/25-06-21-El-cuidado-de-animales-exo%CC%81ticos.jpg",
    "https://animales.market/wp-content/uploads/2022/01/Animales-market-Blog-Educacion-Animales-exoticos.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRf-b4wDrIi7IoglvSZ1CkWvWp-7eDSHV8Bsg&usqp=CAU",
    "https://fotografias.antena3.com/clipping/cmsimages01/2015/01/19/E347ECF9-A741-47FC-99C9-79E81B629D16/58.jpg",
    "https://diario16.com/wp-content/uploads/2020/02/Manis_crassicaudata_29600051791.jpg",
    "http://www.villapopea.com/wp-content/uploads/sites/28/2017/12/animales_en_peligro_de_extincion-1080x675.jpg"
)

