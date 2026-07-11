package com.jetnsync.parkingapp.core.presentation

import com.jetnsync.parkingapp.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LEADING ICON
 *
 * @param supportingText
 *
 * @author zyzz
 */
@Composable
fun NormalTextField(
    modifier: Modifier = Modifier ,
    value: String ,
    onValueChange: (String) -> Unit ,
    placeholder: String ,
    @DrawableRes leadingIcon: Int? = null ,
    iconTint : Color = MaterialTheme.colorScheme.onBackground ,
    leadingIconLabel: String? = null ,
    supportingText: String? = null ,
    titleText: String? = null ,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium ,
    titleTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant ,
    background: Color = MaterialTheme.colorScheme.background ,
    onBackground: Color = MaterialTheme.colorScheme.onBackground ,
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f) ,
    fontSize: TextUnit = 16.sp ,
    fontWeight: FontWeight = FontWeight.Normal ,
    enabled: Boolean = true ,
    singleLine: Boolean = true ,
    maxLines: Int = 10 ,
    keyboardType: KeyboardType = KeyboardType.Unspecified ,
    imeAction: ImeAction = ImeAction.Unspecified ,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = OutlinedTextFieldDefaults.shape ,
    onImeAction: () -> Unit = {}
) {
    if (titleText != null) {
        Column {
            Text(
                text = titleText ,
                style = titleTextStyle ,
                fontWeight = FontWeight.Bold,
                color = titleTextColor
            )
            VerticalSpace(5.dp)
            OutlinedTextField(
                value = value ,
                onValueChange = onValueChange ,
                modifier = modifier
//                    .dropShadow(
//                        shape = shape,
//                        shadow = Shadow(
//                            radius = 5.dp,
//                            color = MaterialTheme.colorScheme.onBackground.copy(0.2f) ,
//                            spread = 1.dp,
//                            offset = DpOffset(x = 1.dp, y =1.dp)
//                        )
//                    )
                ,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = background ,
                    focusedContainerColor = background ,
                    unfocusedIndicatorColor = background ,
                    focusedIndicatorColor = borderColor
                ) ,
                shape = shape ,
                textStyle = TextStyle(
                    color = onBackground ,
                    fontSize = fontSize ,
                    fontWeight = fontWeight
                ) ,
                enabled = enabled ,
                maxLines = maxLines ,
                singleLine = singleLine ,
                placeholder = {
                    Text(
                        placeholder ,
                        style = TextStyle(
                            color = onBackground.copy(0.7f) ,
                            fontSize = fontSize ,
                            fontWeight = fontWeight
                        )
                    )
                } ,
                leadingIcon = {
                    if (leadingIcon != null) {
                        Icon(
                            painter = painterResource(leadingIcon) ,
                            contentDescription = leadingIconLabel ,
                            modifier = Modifier.size(20.dp) ,
                            tint = iconTint
                        )
                    } else {
                        null
                    }
                } ,
                keyboardOptions = KeyboardOptions(
                    capitalization =capitalization,
                    keyboardType = keyboardType ,
                    imeAction = imeAction
                ) ,
                keyboardActions = KeyboardActions(
                    onAny = {
                        onImeAction()
                    },
                ) ,
                supportingText = if (supportingText != null) {
                    {
                        Text(supportingText)
                    }
                } else {
                    null
                },
                visualTransformation = visualTransformation
            )
        }
    }else{
        OutlinedTextField(
            value = value ,
            onValueChange = onValueChange ,
            modifier = modifier ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = background ,
                focusedContainerColor = background ,
                unfocusedIndicatorColor = background ,
                focusedIndicatorColor = borderColor
            ) ,
            shape = shape ,
            textStyle = TextStyle(
                color = onBackground ,
                fontSize = fontSize ,
                fontWeight = fontWeight
            ) ,
            enabled = enabled ,
            maxLines = maxLines ,
            singleLine = singleLine ,
            placeholder = {
                Text(
                    placeholder ,
                    style = TextStyle(
                        color = onBackground.copy(0.7f) ,
                        fontSize = fontSize ,
                        fontWeight = fontWeight
                    )
                )
            } ,
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(leadingIcon) ,
                        contentDescription = leadingIconLabel ,
                        modifier = Modifier.size(25.dp) ,
                        tint = onBackground
                    )
                } else {
                    null
                }
            } ,
            keyboardOptions = KeyboardOptions(
                capitalization = capitalization,
                keyboardType = keyboardType ,
                imeAction = imeAction
            ) ,
            keyboardActions = KeyboardActions(
                onAny = {
                    onImeAction()
                }
            ) ,
            supportingText = if (supportingText != null) {
                {
                    Text(supportingText)
                }
            } else {
                null
            },
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier ,
    value: String ,
    onValueChange: (String) -> Unit ,
    placeholder: String ,
    titleText: String ,
    @DrawableRes leadingIcon: Int? = null ,
    leadingIconTint : Color = MaterialTheme.colorScheme.onBackground ,
    leadingIconLabel: String? = null ,
    @DrawableRes trailingIcon: Int? = null ,
    trailingIconTint : Color = MaterialTheme.colorScheme.onBackground ,
    trailingIconLabel: String? = null ,
    supportingText: String? = null ,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium ,
    titleTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant ,
    background: Color = MaterialTheme.colorScheme.background ,
    onBackground: Color = MaterialTheme.colorScheme.onBackground ,
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f) ,
    fontSize: TextUnit = 16.sp ,
    fontWeight: FontWeight = FontWeight.Normal ,
    enabled: Boolean = true ,
    singleLine: Boolean = true ,
    maxLines: Int = 10 ,
    keyboardType: KeyboardType = KeyboardType.NumberPassword ,
    imeAction: ImeAction = ImeAction.Unspecified ,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    shape: Shape = OutlinedTextFieldDefaults.shape ,
    onImeAction: () -> Unit = {}
) {
    var visible by remember {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = titleText ,
            style = titleTextStyle ,
            fontWeight = FontWeight.Bold ,
            color = titleTextColor
        )
        VerticalSpace(5.dp)

        OutlinedTextField(
            value = value ,
            onValueChange = onValueChange ,
            modifier = modifier
//                    .dropShadow(
//                        shape = shape,
//                        shadow = Shadow(
//                            radius = 5.dp,
//                            color = MaterialTheme.colorScheme.onBackground.copy(0.2f) ,
//                            spread = 1.dp,
//                            offset = DpOffset(x = 1.dp, y =1.dp)
//                        )
//                    )
            ,
            visualTransformation = if(visible){
                VisualTransformation.None
            }else{
                PasswordVisualTransformation()
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = background ,
                focusedContainerColor = background ,
                unfocusedIndicatorColor = background ,
                focusedIndicatorColor = borderColor
            ) ,
            shape = shape ,
            textStyle = TextStyle(
                color = onBackground ,
                fontSize = fontSize ,
                fontWeight = fontWeight
            ) ,
            enabled = enabled ,
            maxLines = maxLines ,
            singleLine = singleLine ,
            placeholder = {
                Text(
                    placeholder ,
                    style = TextStyle(
                        color = onBackground.copy(0.7f) ,
                        fontSize = fontSize ,
                        fontWeight = fontWeight
                    )
                )
            } ,
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(leadingIcon) ,
                        contentDescription = leadingIconLabel ,
                        modifier = Modifier.size(25.dp) ,
                        tint = onBackground
                    )
                } else {
                    null
                }
            } ,
            trailingIcon = {
                Box(
                    Modifier.clickable(
                        enabled = enabled,
                        onClick = {
                            visible = !visible
                        },
                        indication = null,
                        interactionSource = null
                    )
                ){
                    Icon(
                        painter = painterResource(
                            if(visible){
                                R.drawable.visibility_off
                            }else{
                                R.drawable.visibility
                            }
                        ) ,
                        contentDescription = leadingIconLabel ,
                        modifier = Modifier.size(20.dp) ,
                        tint = leadingIconTint
                    )
                }
            } ,
            keyboardOptions = KeyboardOptions(
                capitalization = capitalization ,
                keyboardType = keyboardType ,
                imeAction = imeAction
            ) ,
            keyboardActions = KeyboardActions(
                onAny = {
                    onImeAction()
                } ,
            ) ,
            supportingText = if (supportingText != null) {
                {
                    Text(supportingText)
                }
            } else {
                null
            }
        )
    }
}

