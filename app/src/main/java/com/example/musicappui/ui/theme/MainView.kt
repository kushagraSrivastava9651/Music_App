package com.example.musicappui.ui



import TitleDescriptionScreen
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicappui.Login_data.AuthViewModel
import com.example.musicappui.Login_data.LoginScreen
import com.example.musicappui.Login_data.NavigationGraph
import com.example.musicappui.Login_data.UserRepository
import com.example.musicappui.MainViewModel
import com.example.musicappui.R
import com.example.musicappui.Screen
import com.example.musicappui.chat.Content
import com.example.musicappui.screensInBottom
import com.example.musicappui.screensInDrawer
import com.example.musicappui.ui.theme.AboutUsScreen
import com.example.musicappui.ui.theme.AccountDialog
import com.example.musicappui.ui.theme.AccountView
import com.example.musicappui.ui.theme.BrowseView
import com.example.musicappui.ui.theme.ContactScreen
import com.example.musicappui.ui.theme.FavoriteListScreen
import com.example.musicappui.ui.theme.FeedbackListScreen
import com.example.musicappui.ui.theme.FetchNewsViewModel
import com.example.musicappui.ui.theme.HomeView
import com.example.musicappui.ui.theme.Item
import com.example.musicappui.ui.theme.Library
import com.example.musicappui.ui.theme.MusicAppUITheme
import com.example.musicappui.ui.theme.MyThemedApp
import com.example.musicappui.ui.theme.PollSurveyScreen
import com.example.musicappui.ui.theme.SettingsScreen
import com.example.musicappui.ui.theme.SubscriptionView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import favoriteList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(navController: NavController) {
    val fetchNewsViewModel: FetchNewsViewModel = viewModel()
    val categoriesState by fetchNewsViewModel.categoriesState

    val scaffoldState :ScaffoldState= rememberScaffoldState()
    val scope: CoroutineScope= rememberCoroutineScope()
    val viewModel:MainViewModel= viewModel( )

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val modifier=if(isSheetFullScreen)Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    val currentScreen=remember{
        viewModel.current_screen.value
    }

    val dialogOpen= remember {
        mutableStateOf(false)
    }

    val title= remember {
        //change that with current screen title
        mutableStateOf(currentScreen.title)
    }


    val roundedCornerRadius=if(isSheetFullScreen)0.dp else 12.dp

    val modalSheetState= rememberModalBottomSheetState(initialValue= ModalBottomSheetValue.Hidden,
        confirmValueChange = {it!=ModalBottomSheetValue.HalfExpanded}
    )

    //on which view are currently are
    val controller: NavController= rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute =navBackStackEntry?.destination?.route


    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                screensInBottom.forEach { item ->
                    val isSelected = currentRoute == item.bRoute
                    val tint = if (isSelected) Color(0xFF6200EE) else Color.Gray // Replace with your theme colors
                    BottomNavigationItem(
                        selected = isSelected,
                        onClick = {
                            controller.navigate(item.bRoute) {
                                title.value = item.bTitle
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.bTitle,
                                tint = tint
                            )
                        },
                        label = {
                            Text(
                                text = item.bTitle,
                                color = tint,
                                style = MaterialTheme.typography.caption
                            )
                        },
                        selectedContentColor = tint,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
    }




    ModalBottomSheetLayout(
        sheetState=modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent ={

            MoreBottomSheet(modifier=modifier,controller)
        } ) {
        Scaffold(
            bottomBar=  bottomBar ,
            topBar = {
                TopAppBar(title = { Text(title.value) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if(modalSheetState.isVisible)
                                    modalSheetState.hide()
                                else
                                    modalSheetState.show()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert,contentDescription = null)
                        }
                    },
                    navigationIcon = { IconButton(onClick = {
                        //open the drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                    }}
                )
            },
            scaffoldState=scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)){
                    items(screensInDrawer){
                            item->
                        DrawerIem(selected = currentRoute==item.dRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute=="add_account"){
                                dialogOpen.value=true
                            }
                            else {
                                controller.navigate(item.dRoute)
                                title.value=item.dTitle
                            }
                        }
                    }
                }
            }


        ) {
            Navigation(navController = controller, viewModel = viewModel, pd = it){
                // Content lambda: Specify what should be displayed within the Navigation composable
                // For example, you can show a text composable:
                Text("Navigation Content")
            }
            AccountDialog(dialogOpen = dialogOpen)
        }
    }

}






@Composable
fun DrawerIem(
    selected:Boolean,
    item: Screen.DrawerScreen,
    onDrawerItemClicked :() ->Unit
){
    val background = if (selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.h5,
        )
    }

}
@Composable
fun MoreBottomSheet(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val backgroundColor = MaterialTheme.colors.surface
    val contentColor = MaterialTheme.colors.onSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Settings Action
            ActionRow(
                iconId = R.drawable.baseline_settings_24,
                label = "Settings",
                textColor = contentColor,
                backgroundColor = backgroundColor,
                onClick = { navController.navigate("settings_screen") }
            )

            // Share Action
            ActionRow(
                iconId = R.drawable.baseline_share_24,
                label = "Share",
                textColor = contentColor,
                backgroundColor = backgroundColor,
                onClick = {
                    // Add navigation or share logic here
                }
            )

            // Help Action
            ActionRow(
                iconId = R.drawable.baseline_help_center_24,
                label = "Help",
                textColor = contentColor,
                backgroundColor = backgroundColor,
                onClick = { navController.navigate("chat_screen") }
            )
        }
    }
}
@Composable
fun ActionRow(
    @DrawableRes iconId: Int,
    label: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(backgroundColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = label,
                tint = textColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Label
        Text(
            text = label,
            fontSize = 20.sp,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun Navigation(navController:NavController,viewModel: MainViewModel,pd:PaddingValues,  content: @Composable () -> Unit) {

    val authViewModel: AuthViewModel = viewModel()
    val darkThemeSwitchState = remember { mutableStateOf(false) }
    val notificationSwitchState = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val userRepository = remember {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        UserRepository(firebaseAuth, firestore)
    }

    /*
        NavHost(
            navController = navController as NavHostController,
            startDestination = Screen.DrawerScreen.Account.route,
            modifier = Modifier.padding(pd)
        ) {


            composable(Screen.BottomScreen.Home.bRoute) {
                //TODO home screen


                HomeView(navController)
            }


            composable(
                route = "details/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType; defaultValue = 0 })
            ) { backStackEntry ->
                val fetchNewsViewModel: FetchNewsViewModel = viewModel()
                val categoriesState by fetchNewsViewModel.categoriesState
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                val item = categoriesState.list.find { it.id == itemId }
                item?.let {
                    TitleDescriptionScreen(
                        itemId = it.id,
                        title = it.title,
                        description = it.description,
                        it.image

                    )
                } ?: run {


                }
            }




            composable(Screen.BottomScreen.Browse.bRoute) {
                //TODO Browse screen
                BrowseView(navController)
            }

            composable(Screen.BottomScreen.Library.bRoute) {
                //TODO Library screen
                Library()
            }


            composable(Screen.DrawerScreen.Account.route) {

                AccountView(authViewModel)
            }
            composable(Screen.DrawerScreen.Subscription.route) {
                SubscriptionView()
            }

            composable("chat_screen") {
                Content(navController)
            }
            composable("settings_screen") {
                MyThemedApp(
                    isDarkModeEnabled = darkThemeSwitchState.value,
                    content = {
                        SettingsScreen(
                            darkThemeSwitchState = darkThemeSwitchState,
                            notificationSwitchState = notificationSwitchState,
                            applyTheme = { darkModeEnabled: Boolean ->
                                darkThemeSwitchState.value = darkModeEnabled
                            }
                        )
                    }
                )
            }



        }
    }

     */

    // Wrap the entire Navigation with MyThemedApp to apply the dark mode theme
    MyThemedApp(
        isDarkModeEnabled = darkThemeSwitchState.value,
        content = {
            NavHost(
                navController = navController as NavHostController,
                startDestination = Screen.BottomScreen.Home.route  ,
                modifier = Modifier.padding(pd)
            ) {
                // Composables for different screens
                composable(Screen.BottomScreen.Home.bRoute) {
                    HomeView(navController)
                }


                composable(
                    route = "details/{itemId}",
                    arguments = listOf(navArgument("itemId") { type = NavType.IntType; defaultValue = 0 })
                ) { backStackEntry ->
                    val fetchNewsViewModel: FetchNewsViewModel = viewModel()
                    val categoriesState by fetchNewsViewModel.categoriesState
                    val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                    val item = categoriesState.list.find { it.id == itemId }
                    item?.let {
                        TitleDescriptionScreen(
                            itemId = it.id,
                            title = it.title,
                            description = it.description,
                            it.image

                        )
                    } ?: run {


                    }
                }
                composable(Screen.BottomScreen.Browse.bRoute) {
                    BrowseView(navController)
                }
                composable(Screen.BottomScreen.Library.bRoute) {

                    Library(navController)
                }
                composable("favorite_list_screen") {
                    FavoriteListScreen(favoriteList = favoriteList,navController)
                }

                composable("contact_us") {
                    ContactScreen(navController)
                }
                composable("about_us") {
                    AboutUsScreen(navController)
                }

                composable("polls") {
                    FeedbackListScreen(navController)
                }
                composable("polls_and_surveys"){
                    PollSurveyScreen(navController)
                }

                composable(Screen.DrawerScreen.Account.route) {
                    AccountView(authViewModel)
                }
                composable(Screen.DrawerScreen.LogOut.route) {
                    authViewModel.logout()
                    LoginScreen(
                        authViewModel = authViewModel,
                        onNavigateToSignUp = { navController.navigate(com.example.musicappui.Login_data.Screen.SignupScreen.route) },
                        onSignInSuccess =  {
                            navController.navigate(com.example.musicappui.Login_data.Screen.MainView.route) {
                                popUpTo(com.example.musicappui.Login_data.Screen.SignupScreen.route) { inclusive = true }
                            }
                        }
                    )
                    

                   // NavigationGraph(navController,authViewModel)

                }
                composable(Screen.DrawerScreen.Subscription.route) {
                    SubscriptionView()
                }
                composable("chat_screen") {
                    Content(navController)
                }


                composable(
                    route = "details_screen/{item}",
                    arguments = listOf(navArgument("item") { type = NavType.StringType })
                ) { backStackEntry ->
                    val jsonItem = backStackEntry.arguments?.getString("item")
                    val item = Gson().fromJson(jsonItem, Item::class.java) // Deserialize the JSON back to an Item object

                    // Pass the deserialized item to the DetailsScreen
                    TitleDescriptionScreen(  item.id , item.title ,item.description,item.image)
                }








                composable("settings_screen") {
                    SettingsScreen(
                        darkThemeSwitchState = darkThemeSwitchState,
                        notificationSwitchState = notificationSwitchState,
                       applyTheme = { darkModeEnabled: Boolean ->
                            darkThemeSwitchState.value = darkModeEnabled
                       }
                    )
                }
            }
        }
    )

}


