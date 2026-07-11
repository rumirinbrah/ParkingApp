package com.jetnsync.parkingapp.feature_auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jetnsync.parkingapp.core.presentation.VerticalSpace
import com.jetnsync.parkingapp.feature_auth.presentation.login.LogInRoot
import com.jetnsync.parkingapp.feature_auth.presentation.signup.SignUpRoot
import kotlinx.coroutines.launch

@Composable
fun AuthPage(
    modifier: Modifier = Modifier,
    navToHome : () -> Unit

) {

    val pager = rememberPagerState() {
        2
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        Modifier.fillMaxSize()
    ){
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = pager.currentPage
            ) {
                Tab(
                    selected = pager.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pager.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text("SignUp")
                    }
                )

                Tab(
                    selected = pager.currentPage == 1,
                    onClick = {
                        scope.launch {
                            pager.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text("LogIn")
                    }
                )
            }
            VerticalSpace(5.dp)
            HorizontalPager(
                pager,
                modifier = modifier.fillMaxSize()
                    .padding(16.dp)
            ) {page->
                when(page){
                    0->{
                        SignUpRoot(
                            navToHome = navToHome
                        )
                    }
                    1->{
                        LogInRoot(
                            navToHome = navToHome
                        )
                    }
                }
            }
        }
    }
}