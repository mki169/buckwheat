package com.danilkinkin.buckwheat.base

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Collapse(
    show: Boolean = true,
    onHide: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val height by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = TweenSpec(),
        finishedListener = {
            if (it == 0f) {
                onHide()
            }
        }
    )
    val maxHeight = remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                if (height == 1f) maxHeight.value = it.size.height.toFloat()
            }
            .collapse(
                if (maxHeight.value !== null) {
                    with(LocalDensity.current) { (maxHeight.value!! * height).toDp() }
                } else {
                    null
                }
            )
    ) {
        Box(Modifier.then(if(maxHeight.value !== null) { Modifier.requiredHeight(with(LocalDensity.current) { maxHeight.value!!.toDp() }) } else { Modifier })) {
            content()
        }
    }
}

@Suppress("ModifierInspectorInfo")
private fun Modifier.collapse(
    height: Dp? = null
): Modifier {
    val modifier = if (height != null) {
        Modifier
            .height(height)
            .clipToBounds()
    } else {
        Modifier
    }

    return this.then(modifier)
}