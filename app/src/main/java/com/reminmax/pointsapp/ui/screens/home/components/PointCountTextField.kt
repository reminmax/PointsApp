package com.reminmax.pointsapp.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.ui.screens.home.HomeAction
import com.reminmax.pointsapp.ui.theme.dimensions

@Composable
fun PointCountTextField(
    value: String,
    errorMessage: String?,
    dispatchAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                dispatchAction(HomeAction.PointCountValueChanged(""))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = stringResource(R.string.clearTextField)
            )
        }
    }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                dispatchAction(HomeAction.PointCountValueChanged(newValue))
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = MaterialTheme.dimensions.minHeight),
            //textStyle =,
            label = {
                Text(
                    text = stringResource(R.string.enterPointCount),
                    style = MaterialTheme.typography.body1
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.pointCountPlaceholder),
                    style = MaterialTheme.typography.body1
                )
            },
            trailingIcon = if (value.isNotBlank()) trailingIconView else null,
            isError = !errorMessage.isNullOrEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    dispatchAction(HomeAction.GetPoints)
                }
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = Color(0xFFDADADA),
                cursorColor = MaterialTheme.colors.primary
            ),
        )
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                maxLines = 1
            )
        }
    }
}