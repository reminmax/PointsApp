package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.dimensions
import com.reminmax.pointsapp.ui.theme.spacing

@Composable
fun ChartStyleOptions(
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    chartStyle: LinearChartStyle,
    modifier: Modifier = Modifier,
) {
    Row(Modifier.selectableGroup()) {
        LinearChartStyle.values().forEach { style ->
            Row(
                modifier
                    .height(MaterialTheme.dimensions.minHeight)
                    .selectable(
                        selected = (style == chartStyle),
                        onClick = {
                            onChartStyleSelected(style)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = MaterialTheme.spacing.large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (style == chartStyle),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = style.text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = MaterialTheme.spacing.large)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChartStyleOptionsPreview() {
    PointsAppTheme {
        ChartStyleOptions(
            onChartStyleSelected = {},
            chartStyle = LinearChartStyle.DEFAULT,
            modifier = Modifier
        )
    }
}