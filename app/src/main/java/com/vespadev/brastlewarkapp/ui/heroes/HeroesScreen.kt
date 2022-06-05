package com.vespadev.brastlewarkapp.ui.heroes

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vespadev.brastlewarkapp.R
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import com.vespadev.brastlewarkapp.ui.UIState
import com.vespadev.brastlewarkapp.ui.theme.Background
import com.vespadev.brastlewarkapp.ui.theme.BrastlewarkAppTheme
import com.vespadev.brastlewarkapp.ui.theme.CardCollapsedBackgroundColor
import com.vespadev.brastlewarkapp.ui.theme.CardExpandedBackgroundColor
import com.vespadev.brastlewarkapp.ui.theme.Purple500
import com.vespadev.brastlewarkapp.ui.theme.Teal200
import com.vespadev.brastlewarkapp.ui.twoDigitsNumberFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val EXPAND_ANIMATION_DURATION = 300
const val COLLAPSE_ANIMATION_DURATION = 300
const val FADE_IN_ANIMATION_DURATION = 350
const val FADE_OUT_ANIMATION_DURATION = 300

@ExperimentalCoroutinesApi
@Composable
fun HeroesScreen() {
    BrastlewarkAppTheme {
        val systemUiController = rememberSystemUiController()
        val primaryVariant = MaterialTheme.colors.primaryVariant

        SideEffect {
            systemUiController.setStatusBarColor(color = primaryVariant)
            systemUiController.setNavigationBarColor(color = Color.Black)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val heroesViewModel = hiltViewModel<HeroesViewModel>()
            val uiState by remember(heroesViewModel) { heroesViewModel.uiState }.collectAsState()
            when (uiState) {
                is UIState.Success -> {
                    (uiState as? UIState.Success)?.data?.let { heroes ->
                        CardsScreen(heroesViewModel, heroes)
                    }
                }
                is UIState.Error -> {  }
                else -> {}
            }
        }
    }
}

@Composable
fun CardsScreen(viewModel: HeroesViewModel, heroes: List<HeroEntity>) {
    val expandedHeroIds = viewModel.expandedHeroIdsList.collectAsState()
    var searchText by remember { mutableStateOf("") }
    Scaffold(
        backgroundColor = Background
    ) {
        Column {
            TopHeader(
                onSearchOnChange = { newValue ->
                    searchText = newValue
                    viewModel.onSearchOnChange(newValue)
                },
                onClearFilter = {
                    searchText = ""
                    viewModel.onSearchOnChange(searchText)
                }
            )

            Row {
                LazyColumn {
                    itemsIndexed(heroes) { _, hero ->
                        ExpandableCard(
                            item = hero,
                            onCardArrowClick = { viewModel.onCardArrowClicked(hero.id) },
                            expanded = expandedHeroIds.value.contains(hero.id))
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeader(
    onSearchOnChange: (String) -> Unit,
    onClearFilter: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple500)
            .padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            text = "Brastlewark City",
            color = Teal200,
            textAlign = TextAlign.Center,
        )
        TextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
                onSearchOnChange(newValue)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                CardTitle(title = "Search by ...", expanded = true)
            },
            trailingIcon = {
                Row {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = {
                            onClearFilter()
                            searchText = ""
                        }) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "cancel filter")
                        }
                    }
                    IconButton(onClick = {
                        onSearchOnChange(searchText)
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search by")
                    }
                }
            }
        )
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    item: HeroEntity,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
        if (expanded) CardExpandedBackgroundColor else CardCollapsedBackgroundColor
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 12.dp else 12.dp
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 8.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.purple_500
            )
        ),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    ProfileImage(imageUrl = item.imageUrl, size = 64.dp)
                }
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    CardTitle(title = item.name, expanded)
                    CardText("Age: ${item.age} years", expanded)
                }

                CardArrow(
                    degrees = arrowRotationDegree,
                    expanded = expanded,
                    onClick = onCardArrowClick
                )
            }
            ExpandableContent(
                visible = expanded,
                hero = item
            )
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    expanded: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.End),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_expand_less_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier.rotate(degrees),
                    tint = if (expanded) Teal200 else CardExpandedBackgroundColor
                )
            }
        )
    }
}

@Composable
fun CardTitle(title: String, expanded: Boolean = false) {
    Text(
        text = title,
        color = if (expanded) Teal200 else Purple500,
        textAlign = TextAlign.Left,
    )
}

@Composable
fun CardText(text: String, expanded: Boolean = true) {
    Text(
        text = text,
        color = if (expanded) Color.LightGray else Color.Gray,
        textAlign = TextAlign.Left,
    )
}

@Composable
fun ProfileImage(imageUrl: String, size: Dp) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .dispatcher(Dispatchers.IO)
            .build(),
        placeholder = painterResource(id = R.drawable.circle),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size, size)
            .clip(CircleShape)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    hero: HeroEntity
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    CardTitle("Hair Color", expanded = true)
                    CardText(text = hero.hair_color, expanded = true)
                }
                Column {
                    CardTitle("Weight", expanded = true)
                    CardText(text = hero.weight.twoDigitsNumberFormat(), expanded = true)
                }
                Column {
                    CardTitle("Height", expanded = true)
                    CardText(text = hero.height.twoDigitsNumberFormat(), expanded = true)
                }
            }
            Spacer(modifier = Modifier.heightIn(10.dp))
            CardTitle("Professions", expanded = true)
            ListProfessions(professions = hero.professions)
            Spacer(modifier = Modifier.heightIn(10.dp))
            if (hero.friends.isEmpty()) {
                CardTitle("Doesn't have any friend", expanded = true)
            } else {
                CardTitle("Friends", expanded = true)
                ListFriends(friends = hero.friends)
            }
        }
    }
}

@Composable
fun ListProfessions(professions: List<String>) {
    professions.forEach { profession ->
        Column {
            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                CardText(text = profession, expanded = true)
            }
        }
    }
}

@Composable
fun ListFriends(friends: List<HeroEntity>) {
    friends.forEach { friend ->
        Column {
            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                ProfileImage(imageUrl = friend.imageUrl, size = 24.dp)
                Spacer(modifier = Modifier.widthIn(10.dp))
                CardText(text = friend.name, expanded = true)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BrastlewarkAppTheme {
        val heroesViewModel = hiltViewModel<HeroesViewModel>()
        CardsScreen(
            viewModel = heroesViewModel,
            heroes = listOf(
            HeroEntity(
                id = "1",
                name = "Tobus Quickwhistle",
                imageUrl = "http://www.publicdomainpictures.net/pictures/10000/nahled/thinking-monkey-11282237747K8xB.jpg",
                age = 37,
                weight = 121.0,
                height = 1.79,
                hair_color = "Negro",
                professions = listOf(),
                friends = listOf()
            ),
            HeroEntity(
                id = "2",
                name = "Fizkin Voidbustervv",
                imageUrl = "http://www.publicdomainpictures.net/pictures/120000/nahled/white-hen.jpg",
                age = 27,
                weight = 98.0,
                height = 1.65,
                hair_color = "Negro",
                professions = listOf(),
                friends = listOf()
            ),
            HeroEntity(
                id = "3",
                name = "Malbin Chromerocket",
                imageUrl = "http://www.publicdomainpictures.net/pictures/30000/nahled/maple-leaves-background.jpg",
                age = 37,
                weight = 121.0,
                height = 1.79,
                hair_color = "Negro",
                professions = listOf(),
                friends = listOf()
            ),
            HeroEntity(
                id = "4",
                name = "Midwig Gyroslicer",
                imageUrl = "http://www.publicdomainpictures.net/pictures/10000/nahled/1-1275919724d1Oh.jpg",
                age = 37,
                weight = 121.0,
                height = 1.79,
                hair_color = "Negro",
                professions = listOf(),
                friends = listOf()
            ),
            HeroEntity(
                id = "5",
                name = "Malbin Magnaweaver",
                imageUrl = "http://www.publicdomainpictures.net/pictures/10000/nahled/zebra-head-11281366876AZ3M.jpg",
                age = 37,
                weight = 121.0,
                height = 1.79,
                hair_color = "Negro",
                professions = listOf(),
                friends = listOf()
            )
        ))
    }
}